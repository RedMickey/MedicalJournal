package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findUserByName(String userName);

    User findUserByEmail(String userEmail);

}
