package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final GuestRepository guestRepository;

    private final ScheduleRepository scheduleRepository;

    private final ReservationMapper reservationMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {

        boolean hasExistingEntity = reservationRepository.findBySchedule_Id(createReservationRequestDTO.getScheduleId()).stream().anyMatch(reservation ->
                ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus()));

        if (hasExistingEntity) {
            throw new AlreadyExistsEntityException("There is already a reservation for this schedule");
        }

        return guestRepository.findById(createReservationRequestDTO.getGuestId()).map(guest ->
                scheduleRepository.findById(createReservationRequestDTO.getScheduleId()).map(schedule -> {
                    if (schedule.getStartDateTime().isBefore(LocalDateTime.now())) {
                        throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
                    }
                    return createReservation(createReservationRequestDTO, guest, schedule);
                })
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Schedule not found.");
                }))
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Guest not found.");
                });
    }

    private ReservationDTO createReservation(CreateReservationRequestDTO createReservationRequestDTO, Guest guest, Schedule schedule) {

        Reservation reservation = reservationMapper.map(createReservationRequestDTO);
        reservation.setGuest(guest);
        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        reservation.setValue(BigDecimal.valueOf(10));

        schedule.addReservation(reservation);

        return reservationMapper.map(reservationRepository.saveAndFlush(reservation));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (minutes >= 1440) {
            return reservation.getValue();
        } else if (minutes >= 720) {
            return reservation.getValue().multiply(new BigDecimal("0.75"));
        } else if (minutes >= 120) {
            return reservation.getValue().multiply(new BigDecimal("0.50"));
        } else if (minutes >= 1) {
            return reservation.getValue().multiply(new BigDecimal("0.25"));
        }

        return BigDecimal.ZERO;
    }

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
            "Cannot reschedule to the same slot.*/
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        Reservation previousReservation = cancel(previousReservationId);

        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }
}
