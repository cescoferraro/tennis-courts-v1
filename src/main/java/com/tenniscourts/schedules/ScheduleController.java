package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) throws Exception {
        System.out.println("%%%%%%%%%");
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO).getId())).build();
    }

    @RequestMapping(value = "/schedule/availability/{startDate}/{endDate}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<String>>> findSchedulesByDates(@PathVariable("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                                                          @PathVariable("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    //TODO: implement rest and swagger
    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable("id") Long scheduleId) throws Exception {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
