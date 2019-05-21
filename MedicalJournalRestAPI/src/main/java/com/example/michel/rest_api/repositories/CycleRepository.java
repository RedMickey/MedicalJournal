package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.Cycle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface CycleRepository extends CrudRepository<Cycle, UUID> {

    @Query(
            value = "select cl._id_cycle, cl.period, cl.period_DM_type, cl.once_a_period, cl.once_a_period_DM_type, cl._id_week_schedule, " +
                    "cl._id_cycling_type, cl.change_type, cl.synch_time " +
                    "from cycle cl inner join pill_reminder pr on cl._id_cycle=pr._id_cycle " +
                    "where cl.synch_time >= :timestamp and pr.user_id = :userId " +
                    "union " +
                    "select cl._id_cycle, cl.period, cl.period_DM_type, cl.once_a_period, cl.once_a_period_DM_type, cl._id_week_schedule, " +
                    "cl._id_cycling_type, cl.change_type, cl.synch_time " +
                    "from cycle cl inner join measurement_reminder mr on cl._id_cycle=mr._id_cycle " +
                    "where cl.synch_time >= :timestamp and mr.user_id = :userId",
            nativeQuery = true)
    List<Cycle> getCycleDBEntriesForSynchronization(
            @Param("timestamp") Timestamp synchronizationTimestamp, @Param("userId") Integer userId);

    @Modifying
    @Query(
            value = "UPDATE cycle SET synch_time = :timestamp, change_type = 3 WHERE _id_cycle = :id",
            nativeQuery = true)
    void updateAndMarkAsDeletedById(@Param("id") UUID id, @Param("timestamp") Timestamp synchronizationTimestamp);
}
