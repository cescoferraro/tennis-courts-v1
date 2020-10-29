package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Controller
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @RequestMapping(path = "/reservation", method = RequestMethod.POST)
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) throws Exception {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @RequestMapping(path = "/reservation/past", method = RequestMethod.GET)
    public ResponseEntity<List<ReservationDTO>> findPastReservation() {
        return ResponseEntity.ok(reservationService.findPastReservation());
    }

    @RequestMapping(path = "/reservation/{reservationId}", method = RequestMethod.GET)
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable("reservationId") Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @RequestMapping(path = "/reservation/cancel/{id}", method = RequestMethod.POST)
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("id") Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @RequestMapping(path = "/reservation/reschedule/{reservationId}/{scheduleId}", method = RequestMethod.POST)
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable("reservationId") Long reservationId, @PathVariable("scheduleId") Long scheduleId) throws Exception {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
