package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.security.JwtConfig;
import com.example.michel.rest_api.security.JwtTokenFactory;
import com.example.michel.rest_api.security.models.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping(value = "/refreshToken", produces = "application/json")
    public @ResponseBody void refreshToken(@RequestBody Map<String, String> req, HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse){

        if (req.get("refreshToken") == null){
            httpServletResponse.setStatus(400);
            return;
        }

        Claims claims = null;

        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getRefreshSecret().getBytes())
                    .parseClaimsJws(req.get("refreshToken"))
                    .getBody();
        }
        catch (Exception ex){
            ex.printStackTrace();
            httpServletResponse.setStatus(400);
            return;
        }

        String username = claims.getSubject();
        List<String> authorities = (List<String>) claims.get("authorities");

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(authorities.get(0));

        CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(
                new org.springframework.security.core.userdetails.User(username, "", grantedAuthorities),
                claims.get("email", String.class), null, null
        );

        JwtTokenFactory jwtTokenFactory = new JwtTokenFactory(jwtConfig);

        String token = jwtTokenFactory.createAccessJwtToken(customUserPrincipal);

        httpServletResponse.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
    }

    @PostMapping(value = "/checkToken", produces = "application/json")
    public Map ref(){
        return Collections.singletonMap("workable", true);
    }
}
