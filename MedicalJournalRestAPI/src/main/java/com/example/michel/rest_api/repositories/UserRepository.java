package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findUserByName(String userName);

    User findUserByEmail(String userEmail);

    @Modifying
    @Query(
            value = "UPDATE user SET synchronization_time = :timestamp WHERE user_id = :id",
            nativeQuery = true)
    void updateUserSynchronizationTime(@Param("id") Integer userId, @Param("timestamp") Timestamp synchronizationTimestamp);

}
