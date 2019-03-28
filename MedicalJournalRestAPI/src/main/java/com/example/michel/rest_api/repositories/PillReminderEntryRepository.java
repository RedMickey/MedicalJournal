package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.PillReminderEntry;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface PillReminderEntryRepository extends CrudRepository<PillReminderEntry, UUID> {

    @Query(
            value = "select pre._id_pill_reminder_entry, pre.is_done, pre.reminder_date, pre._id_pill_reminder, pre.reminder_time, " +
                    "pre.is_one_time, pre.synch_time, pre.change_type " +
                    "from pill_reminder_entry pre inner join pill_reminder pr on pre._id_pill_reminder=pr._id_pill_reminder " +
                    "where pre.synch_time >= :timestamp and pr.user_id = :userId",
            nativeQuery = true)
    List<PillReminderEntry> getPillReminderEntriesForSynchronization(
            @Param("timestamp") Timestamp synchronizationTimestamp, @Param("userId") Integer userId);

    @Modifying
    @Query(
            value = "UPDATE pill_reminder_entry SET synch_time = :timestamp, change_type = 3 WHERE _id_pill_reminder_entry = :id",
            nativeQuery = true)
    void updateAndMarkAsDeletedById(@Param("id") UUID id, @Param("timestamp") Timestamp synchronizationTimestamp);
}
