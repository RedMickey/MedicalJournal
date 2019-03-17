package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.User;
import com.example.michel.rest_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Date updateUserSynchronizationTime(Integer userId){
        Timestamp synchronizationTimestamp = new Timestamp(new Date().getTime());
        userRepository.updateUserSynchronizationTime(userId, synchronizationTimestamp);
        return synchronizationTimestamp;
    }
}
