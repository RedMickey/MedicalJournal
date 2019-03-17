package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.Pill;
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
}
