package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.*;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;
import com.example.michel.rest_api.repositories.MeasurementTypeRepository;
import com.example.michel.rest_api.repositories.UserRepository;
import com.example.michel.rest_api.repositories.WeekScheduleRepository;
import com.example.michel.rest_api.services.PillReminderEntryFService;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private WeekScheduleRepository weekScheduleRepository;

    @CrossOrigin(origins = "http://localhost:4000")
    @PostMapping(value = "/test1", produces = "application/json")
    public List<WeekSchedule> test1(@RequestBody Map<String, Integer> req){

        List<WeekSchedule> l = weekScheduleRepository.findAll();

        return l;
    }

    /*@CrossOrigin(origins = "http://localhost:4000")
    @PostMapping(value = "/test1", produces = "application/json")
    public WeekSchedule test1(@RequestBody Map<String, Integer> req){

        WeekSchedule weekSchedule = new WeekSchedule(UUID.randomUUID(), 1, 0, 1, 0, 0, 1, 0);
        weekScheduleRepository.save(weekSchedule);

        return weekSchedule;
    }*/

    @GetMapping("/test2")
    public String tes2(){
        return "seccess";
    }

    @GetMapping("/test3")
    public String tes3(){
        return "seccess Admin";
    }

    @PostMapping("/getUserByEmail")
    public User getUserByEmail(@RequestBody Map<String, String> userEmail){
        User user = userRepository.findUserByEmail(userEmail.get("userEmail"));
        user.setPassword("");
        return user;
    }

    @PostMapping("/updateUser")
    public Map updateUser(@RequestBody User user){ user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return Collections.singletonMap("userEmail", user.getEmail());
    }

    @PostMapping("/sign-up")
    public User signUp(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User insertedUser = userRepository.save(user);
        return insertedUser;
    }

    // Exception handling methods
    @ResponseStatus(value=HttpStatus.CONFLICT)  // 409
    @ExceptionHandler({org.springframework.dao.DataIntegrityViolationException.class})
    public Map duplicateUniqField(org.springframework.dao.DataIntegrityViolationException ex) {
        return Collections.singletonMap("err", ex.getMessage());
    }

}
