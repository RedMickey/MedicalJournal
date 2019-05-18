package com.example.michel.rest_api.security;

import com.example.michel.rest_api.models.User;
import com.example.michel.rest_api.repositories.UserRepository;
import com.example.michel.rest_api.security.models.CustomUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /*@Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByName(s);
        if (user == null) {
            throw new UsernameNotFoundException(s);
        }
        List<GrantedAuthority> authorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_"+user.getRoleId().toString());

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }*/

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException(userEmail);
        }
        List<GrantedAuthority> authorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_"+user.getRoleId().toString());

        return new CustomUserPrincipal(
                new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities),
                user.getEmail(), user.getId(), user.getGenderId()
        );
    }
}
