package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.ReminderTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface ReminderTimeRepository extends CrudRepository<ReminderTime, UUID> {

    @Query(
            value = "select rt._id_reminder_time, rt.reminder_time, rt._id_pill_reminder, rt._id_measurement_reminder, rt.synch_time, rt.change_type " +
                    "from reminder_time rt inner join pill_reminder pr on rt._id_pill_reminder=pr._id_pill_reminder " +
                    "where rt.synch_time >= :timestamp and pr.user_id = :userId " +
                    "union " +
                    "select rt._id_reminder_time, rt.reminder_time, rt._id_pill_reminder, rt._id_measurement_reminder, rt.synch_time, rt.change_type " +
                    "from reminder_time rt inner join measurement_reminder mr on rt._id_measurement_reminder=mr._id_measurement_reminder " +
                    "where rt.synch_time >= :timestamp and mr.user_id = :userId",
            nativeQuery = true)
    List<ReminderTime> getReminderTimeForSynchronization(
            @Param("timestamp") Timestamp synchronizationTimestamp, @Param("userId") Integer userId);

    @Modifying
    @Query(
            value = "UPDATE reminder_time SET synch_time = :timestamp, change_type = 3 WHERE _id_reminder_time = :id",
            nativeQuery = true)
    void updateAndMarkAsDeletedById(@Param("id") UUID id, @Param("timestamp") Timestamp synchronizationTimestamp);

    List<ReminderTime> getAllByIdPillReminderEqualsAndChangeTypeLessThanEqual(UUID idReminder, int changeType);

    List<ReminderTime> getAllByIdMeasurementReminderEqualsAndChangeTypeLessThanEqual(UUID idReminder, int changeType);
}
