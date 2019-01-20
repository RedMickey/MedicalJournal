package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.User;
import com.example.michel.rest_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "/test1", produces = "application/json")
    public Map test1(@RequestBody Map<String, String> req){
        String s = req.get("s");
        s = bCryptPasswordEncoder.encode(s);
        //return s+" seccess";
        return Collections.singletonMap("response", s);
    }

    @GetMapping("/test2")
    public String tes2(){
        return "seccess";
    }

    @GetMapping("/test3")
    public String tes3(){
        return "seccess Admin";
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
