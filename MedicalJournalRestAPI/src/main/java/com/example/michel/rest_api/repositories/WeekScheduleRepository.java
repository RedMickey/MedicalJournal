package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.WeekSchedule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface WeekScheduleRepository extends CrudRepository<WeekSchedule, UUID> {

    List<WeekSchedule> findAll();

    @Query(
            value = "select ws.mon, ws.tue, ws.wed, ws.thu, ws.fri, ws.sat, ws.sun, ws._id_week_schedule, ws.change_type, ws.synch_time " +
                    "from week_schedule ws inner join cycle cl on ws._id_week_schedule=cl._id_week_schedule inner join pill_reminder pr on cl._id_cycle=pr._id_cycle " +
                    "where ws.synch_time >= :timestamp and pr.user_id= :userId " +
                    "union " +
                    "select ws.mon, ws.tue, ws.wed, ws.thu, ws.fri, ws.sat, ws.sun, ws._id_week_schedule, ws.change_type, ws.synch_time " +
                    "from week_schedule ws inner join cycle cl on ws._id_week_schedule=cl._id_week_schedule inner join measurement_reminder mr on cl._id_cycle=mr._id_cycle " +
                    "where ws.synch_time >= :timestamp and mr.user_id = :userId",
            nativeQuery = true)
    List<WeekSchedule> getWeekSchedulesForSynchronization(
            @Param("timestamp") Timestamp synchronizationTimestamp, @Param("userId") Integer userId);

    @Modifying
    @Query(
            value = "UPDATE week_schedule SET synch_time = :timestamp, change_type = 3 WHERE _id_week_schedule = :id",
            nativeQuery = true)
    void updateAndMarkAsDeletedById(@Param("id") UUID id, @Param("timestamp") Timestamp synchronizationTimestamp);

    WeekSchedule getByIdWeekScheduleEqualsAndChangeTypeLessThanEqual(UUID id, int changeType);
}
