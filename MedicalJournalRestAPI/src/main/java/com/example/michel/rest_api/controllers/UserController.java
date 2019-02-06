package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.MeasurementType;
import com.example.michel.rest_api.models.TestModel;
import com.example.michel.rest_api.models.User;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;
import com.example.michel.rest_api.repositories.MeasurementTypeRepository;
import com.example.michel.rest_api.repositories.UserRepository;
import com.example.michel.rest_api.services.PillReminderEntryFService;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PillReminderEntryFService pillReminderEntryFService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "/test1", produces = "application/json")
    public List<PillReminderEntryF> test1(@RequestBody Map<String, Integer> req){
        Integer s = req.get("s");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 1, 4, 0, 0, 0);
        java.util.Date date = new java.util.Date(calendar.getTimeInMillis());

        List<PillReminderEntryF> l = pillReminderEntryFService.getPillReminderEntriesByDate(date);
        //MeasurementType measurementType = measurementTypeRepository.findMeasurementTypeById(s);
        //MeasurementType measurementType = (MeasurementType) measurementTypeRepository.findMeasurementTypeByIdMeasurementType(s);
        //TestModel t = measurementTypeRepository.findmeasurementvaluetypeName(s);
        return l;
    }

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
