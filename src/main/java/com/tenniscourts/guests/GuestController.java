package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Controller
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @RequestMapping(path = "/guest", method = RequestMethod.POST)
    public ResponseEntity<Guest> createGuest(@RequestBody CreateGuestDTO createGuestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.createGuest(createGuestDTO).getId())).build();
    }

    @RequestMapping(path = "/guest", method = RequestMethod.PUT)
    public Guest updateGuest(@RequestBody UpdateGuestDTO updateGuestDTO) throws Exception {
        return guestService.updateGuest(updateGuestDTO);
    }

    @RequestMapping(path = "/guest", method = RequestMethod.GET)
    public ResponseEntity<List<Guest>> findAllGuest() throws Exception {
        return ResponseEntity.ok(guestService.findAll());
    }

    @RequestMapping(path = "/guest/{id}", method = RequestMethod.DELETE)
    public void deleteGuest(@PathVariable("id") Long id) throws Exception {
        guestService.deleteGuest(id);
    }

    @RequestMapping(path = "/guest/{id}", method = RequestMethod.GET)
    public ResponseEntity<Guest> findGuest(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(guestService.findById(id));
    }

    @RequestMapping(path = "/guest/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Guest> findGuest(@PathVariable("name") String id) throws Exception {
        return ResponseEntity.ok(guestService.findByName(id));
    }
}
