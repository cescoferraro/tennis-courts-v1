package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public class GuestController extends BaseRestController {

  private final GuestService guestService;

  //TODO: implement rest and swagger
  public ResponseEntity<Void> newGuest(GuestDTO guestDTO) {
    return ResponseEntity.created(locationByEntity(guestService.add(guestDTO).getId())).build();
  }

  //TODO: implement rest and swagger
  public ResponseEntity<GuestDTO> findGuest(Long id) {
    return ResponseEntity.ok(guestService.findGuestById(id));
  }
}
