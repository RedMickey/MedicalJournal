package com.example.michel.rest_api.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class JwtConfig {

    //@Value("${security.jwt.url:/auth/**}")
    @Value("${security.jwt.url:/user/sign-up}")
    private String Url;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{10*24*60*60}}")
    private int expiration;

    @Value("${security.jwt.accessSecret:JwtSecretKey}")
    private String accessSecret;

    @Value("${security.jwt.refreshSecret:RefreshJwtSecretKey}")
    private String refreshSecret;
}
