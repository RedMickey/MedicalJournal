package com.example.michel.rest_api.security.filters;

import com.example.michel.rest_api.security.JwtConfig;
import com.example.michel.rest_api.security.JwtTokenFactory;
import com.example.michel.rest_api.security.models.CustomUserPrincipal;
import com.example.michel.rest_api.security.models.UserCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // We use auth manager to validate the user credentials
    private AuthenticationManager authManager;

    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            // 1. Get credentials from request
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);

            // 2. Create auth object (contains credentials) which will be used by auth manager
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    creds.getUsername(), creds.getPassword(), Collections.emptyList());

            // 3. Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.
            return authManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authResult.getPrincipal();

        JwtTokenFactory jwtTokenFactory = new JwtTokenFactory(jwtConfig);

        /*String token = Jwts.builder()
                .setSubject(authResult.getName())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", authResult.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("email", customUserPrincipal.getEmail())
                .setIssuedAt(new Date(now))
                //.setExpiration(new Date(now + jwtConfig.getExpiration()))  // in milliseconds
                .setExpiration(new Date(now + 60*1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getAccessSecret().getBytes())
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(authResult.getName())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", authResult.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("email", customUserPrincipal.getEmail())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(now))
                //.setExpiration(new Date(now + jwtConfig.getExpiration()))  // in milliseconds
                .setExpiration(new Date(now + 259200*60*1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getRefreshSecret().getBytes())
                .compact();*/

        String token = jwtTokenFactory.createAccessJwtToken(customUserPrincipal);
        String refreshToken = jwtTokenFactory.createRefreshToken(customUserPrincipal);

        // Add token to header
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
        response.addHeader("RefreshToken", refreshToken);
    }

}
