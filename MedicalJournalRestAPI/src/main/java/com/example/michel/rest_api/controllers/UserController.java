package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.*;
import com.example.michel.rest_api.repositories.UserRepository;
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

    @CrossOrigin(origins = "http://localhost:4000")
    @PostMapping(value = "/test1", produces = "application/json")
    public Map<String, Integer> test1(){

        //List<WeekSchedule> l = weekScheduleRepository.findAll();

        return Collections.singletonMap("test", 1);
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

    @PostMapping("/getUserById")
    public User getUserById(@RequestBody Map<String, Integer> userId){
        User user = userRepository.findUserById(userId.get("userId"));
        user.setPassword("");
        return user;
    }

    @PostMapping("/updateUser")
    public Map updateUser(@RequestBody User user){
        if (user.getPassword() == null || user.getPassword().equals("")){
            User userBuf = userRepository.findUserById(user.getId());
            user.setPassword(userBuf.getPassword());
        }
        else
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return Collections.singletonMap("userEmail", user.getEmail());
    }

    @PostMapping("/sign-up")
    public User signUp(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User insertedUser = userRepository.save(user);
        insertedUser.setPassword("");
        return insertedUser;
    }

    // Exception handling methods
    @ResponseStatus(value=HttpStatus.CONFLICT)  // 409
    @ExceptionHandler({org.springframework.dao.DataIntegrityViolationException.class})
    public Map duplicateUniqField(org.springframework.dao.DataIntegrityViolationException ex) {
        return Collections.singletonMap("err", ex.getMessage());
    }

}
