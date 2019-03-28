package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface MeasurementReminderEntryRepository extends CrudRepository<MeasurementReminderEntry, UUID> {

    @Query(
            value = "select mre._id_measur_remind_entry, mre.value1, mre.value2, mre._id_measurement_reminder, mre.is_done, mre.reminder_date, " +
                    "mre.is_one_time, mre.reminder_time, mre.synch_time, mre.change_type " +
                    "from measurement_reminder_entry mre inner join measurement_reminder mr on mre._id_measurement_reminder=mr._id_measurement_reminder " +
                    "where mre.synch_time >= :timestamp and mr.user_id = :userId",
            nativeQuery = true)
    List<MeasurementReminderEntry> getMeasurementReminderEntriesForSynchronization(
            @Param("timestamp") Timestamp synchronizationTimestamp, @Param("userId") Integer userId);

    @Modifying
    @Query(
            value = "UPDATE measurement_reminder_entry SET synch_time = :timestamp, change_type = 3 WHERE _id_measur_remind_entry = :id",
            nativeQuery = true)
    void updateAndMarkAsDeletedById(@Param("id") UUID id, @Param("timestamp") Timestamp synchronizationTimestamp);
}
