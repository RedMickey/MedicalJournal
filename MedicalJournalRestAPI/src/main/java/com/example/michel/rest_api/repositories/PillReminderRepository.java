package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.PillReminder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface PillReminderRepository extends CrudRepository<PillReminder, UUID> {

    List<PillReminder> getPillRemindersBySynchTimeGreaterThanEqualAndUserIdEquals(
            Timestamp synchronizationTimestamp, Integer userId
    );

    @Modifying
    @Query(
            value = "UPDATE pill_reminder SET synch_time = :timestamp, change_type = 3 WHERE _id_pill_reminder = :id",
            nativeQuery = true)
    void updateAndMarkAsDeletedById(@Param("id") UUID id, @Param("timestamp") Timestamp synchronizationTimestamp);
}
