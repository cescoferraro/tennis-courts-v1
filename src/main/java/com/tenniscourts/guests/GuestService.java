package com.tenniscourts.guests;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRespository;


    public Guest findById(Long tennisCourtId) throws Exception {
        Optional<Guest> byId = guestRespository.findById(tennisCourtId);
        if (!byId.isPresent()) {
            throw new Exception("not present");
        }
        return byId.get();
    }

    public Guest findByName(String name) throws Exception {
        Optional<Guest> byId = guestRespository.findByNameContains(name);
        if (!byId.isPresent()) {
            throw new Exception("not present");
        }
        return byId.get();
    }

    public List<Guest> findAll() {
        return guestRespository.findAll();
    }

    public Guest createGuest(CreateGuestDTO createGuestDTO) {
        Guest guest = Guest.builder().name(createGuestDTO.getName()).build();
        return guestRespository.save(guest);
    }

    public Guest updateGuest(UpdateGuestDTO updateGuestDTO) throws Exception {
        Optional<Guest> byId = guestRespository.findById(updateGuestDTO.getId());
        if (!byId.isPresent()) {
            throw new Exception("not present");
        }
        Guest guest = byId.get();
        guest.setName(updateGuestDTO.getName());
        return guestRespository.save(guest);
    }

    public void deleteGuest(Long id) {
        guestRespository.deleteById(id);
    }
}
