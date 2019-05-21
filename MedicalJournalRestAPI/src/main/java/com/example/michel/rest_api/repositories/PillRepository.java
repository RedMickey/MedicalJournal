package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.Pill;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface PillRepository extends CrudRepository<Pill, UUID> {

    @Query(
            value = "select  pl._id_pill, pl.pill_name, pl.pill_description, pl.synch_time, pl.change_type " +
                    "from pill pl inner join pill_reminder pr on pl._id_pill=pr._id_pill " +
                    "where pl.synch_time >= :timestamp and pr.user_id= :userId",
            nativeQuery = true)
    List<Pill> getPillsForSynchronization(
            @Param("timestamp") java.sql.Timestamp synchronizationTimestamp, @Param("userId") Integer userId);

    @Modifying
    @Query(
            value = "UPDATE pill SET synch_time = :timestamp, change_type = 3 WHERE _id_pill = :id",
            nativeQuery = true)
    void updateAndMarkAsDeletedById(@Param("id") UUID id, @Param("timestamp") Timestamp synchronizationTimestamp);

    @Query(
            value = "select  pl._id_pill, pl.pill_name, pl.pill_description, pl.synch_time, pl.change_type " +
                    "from pill pl inner join pill_reminder pr on pl._id_pill=pr._id_pill " +
                    "where pr._id_pill_reminder = :idPillReminder and pl.change_type<3",
            nativeQuery = true)
    Pill getPillByPillReminderId(@Param("idPillReminder") UUID idPillReminder);
}
