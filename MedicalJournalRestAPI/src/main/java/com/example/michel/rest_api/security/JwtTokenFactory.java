package com.example.michel.rest_api.security;

import com.example.michel.rest_api.security.models.CustomUserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtTokenFactory {
    private final JwtConfig jwtConfig;

    public JwtTokenFactory(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String createAccessJwtToken(CustomUserPrincipal customUserPrincipal){
        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(customUserPrincipal.getUsername())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", customUserPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("email", customUserPrincipal.getEmail())
                .claim("userId", customUserPrincipal.getUserId())
                .claim("genderId", customUserPrincipal.getGenderId())
                .setIssuedAt(new Date(now))
                //.setExpiration(new Date(now + jwtConfig.getExpiration()))  // in milliseconds
                .setExpiration(new Date(now + 2*60*1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getAccessSecret().getBytes())
                .compact();
    }

    public String createRefreshToken(CustomUserPrincipal customUserPrincipal){
        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(customUserPrincipal.getUsername())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", customUserPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("email", customUserPrincipal.getEmail())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(now))
                //.setExpiration(new Date(now + jwtConfig.getExpiration()))  // in milliseconds
                .setExpiration(new Date(now + 10200*60*1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getRefreshSecret().getBytes())
                .compact();
    }
}
