package com.example.michel.rest_api.security.websocket;

import com.example.michel.rest_api.security.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebSocketAuthenticatorService {

    @Autowired
    private JwtConfig jwtConfig;

    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String  tokenStr) throws Exception {
        // 3. Get the token
        String token = tokenStr.replace(jwtConfig.getPrefix(), "");

        Claims claims = Jwts.parser()
            .setSigningKey(jwtConfig.getAccessSecret().getBytes())
            .parseClaimsJws(token)
            .getBody();

        String username = claims.get("email", String.class);

        List<String> authorities = (List<String>) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        return auth;
    }
}
