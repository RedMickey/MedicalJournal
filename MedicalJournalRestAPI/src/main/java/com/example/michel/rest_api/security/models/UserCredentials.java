package com.example.michel.rest_api.security.models;

import lombok.Data;

@Data
public class UserCredentials {
    private String username;
    private String password;
}
