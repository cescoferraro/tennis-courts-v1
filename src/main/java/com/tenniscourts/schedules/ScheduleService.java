package com.tenniscourts.schedules;

import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleMapper scheduleMapper;

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) throws Exception {
        return scheduleMapper.map(createScheduler(createScheduleRequestDTO));
    }

    private Schedule createScheduler(CreateScheduleRequestDTO createScheduleRequestDTO) throws Exception {
        System.out.println("=========");
        Long tennisCourtId = createScheduleRequestDTO.getTennisCourtId();
        System.out.println(tennisCourtId);
        Optional<TennisCourt> court = tennisCourtRepository.findById(tennisCourtId);
        if (court.isEmpty()) {
            throw new Exception("error");
        }
        System.out.println("&&&&&&&&&");
        Schedule schedule = Schedule.builder()
                .tennisCourt(court.get())
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1L))
                .build();
        return scheduleRepository.save(schedule);
    }

    public Map<String, List<String>> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, List<String>> result = new HashMap<>();
        List<TennisCourt> all = tennisCourtRepository.findAll();
        for (TennisCourt court : all) {
            List<String> list = new ArrayList<>();
            for (LocalDateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                for (int hour = 14; hour <= 23; hour++) {
                    List<Schedule> byTennisCourt_idAAndStartDateTime = scheduleRepository.mutualAidFlag(court.getId(), date.withHour(hour));
                    if (byTennisCourt_idAAndStartDateTime.size() == 0) list.add(date.withHour(hour).toString());
                }
            }
            result.put(court.getName(), list);
        }
        return result;
    }

    public Schedule findScheduleDB(Long scheduleId) throws Exception {
        Optional<Schedule> byId = scheduleRepository.findById(scheduleId);
        if (byId.isEmpty()) {
            throw new Exception("error");
        }
        return byId.get();
    }

    public ScheduleDTO findSchedule(Long scheduleId) throws Exception {
        return scheduleMapper.map(findScheduleDB(scheduleId));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
