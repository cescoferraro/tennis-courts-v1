package com.tenniscourts.schedules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);

    List<Schedule> findByStartDateTimeAfterAndEndDateTimeBefore(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT * FROM SCHEDULE s WHERE s.TENNIS_COURT_ID = :id AND s.START_DATE_TIME = :startDate ;", nativeQuery = true)
    List<Schedule> mutualAidFlag(@Param("id") Long id, @Param("startDate") LocalDateTime startDate);

}