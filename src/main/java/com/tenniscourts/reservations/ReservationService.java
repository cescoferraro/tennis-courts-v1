package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tenniscourts.reservations.ReservationStatus.READY_TO_PLAY;

@Service
@AllArgsConstructor
public class ReservationService {

    private final GuestService guestService;

    private final ScheduleService scheduleService;

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) throws Exception {
        return reservationMapper.map(bookReservationDB(createReservationRequestDTO));
    }

    public Reservation bookReservationDB(CreateReservationRequestDTO createReservationRequestDTO) throws Exception {
        Schedule schedule = scheduleService.findScheduleDB(createReservationRequestDTO.getScheduleId());
        Guest guest = guestService.findById(createReservationRequestDTO.getGuestId());
        Reservation reservation = reservationMapper.map(createReservationRequestDTO);
        reservation.setSchedule(schedule);
        reservation.setReservationStatus(READY_TO_PLAY);
        reservation.setGuest(guest);
        reservation.setValue(reservation.getValue());
        reservation.setRefundValue(getRefundValue(reservation));
        return reservationRepository.save(reservation);
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
        if (!READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        // task 7/8/9/10
        long hours = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours < 0) {
            return BigDecimal.ZERO;
        }
        if (hours > 0 && hours < 2 * 60) {
            return reservation.getValue().multiply(BigDecimal.valueOf(3 / 4));
        }
        if (hours >= 2 * 60 && hours < 12 * 60) {
            return reservation.getValue().multiply(BigDecimal.valueOf(1 / 2));
        }
        if (hours >= 12 * 60 && hours < 24 * 60) {
            return reservation.getValue().multiply(BigDecimal.valueOf(1 / 4));
        }

        if (hours >= 24 * 60) {
            return reservation.getValue();
        }
        return BigDecimal.ZERO;
    }

    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) throws Exception {
        Reservation previousReservation = cancel(previousReservationId);

        ScheduleDTO actualSchedule = scheduleService.findSchedule(scheduleId);

        // Schedul
        if (actualSchedule.getStartDateTime().equals(previousReservation.getSchedule().getStartDateTime())) {
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

    public List<ReservationDTO> findPastReservation() {
        List<Long> pastReservations = reservationRepository.findPastReservations();
        List<ReservationDTO> newWW = new ArrayList<>();
        for (Long r : pastReservations) {
            Optional<Reservation> byId = reservationRepository.findById(r);
            Reservation reservation = byId.get();
            newWW.add(reservationMapper.map(reservation));
        }
        return newWW;
    }
}
