package com.example.michel.rest_api.security;

import lombok.Data;

@Data
public class UserCredentials {
    private String username;
    private String password;
    private Integer id;
    private String role;
}
