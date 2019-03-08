package com.example.michel.rest_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private JwtConfig jwtConfig;

    private UserDetailsServiceImpl userDetailsService;

    public SecurityTokenConfig(BCryptPasswordEncoder bCryptPasswordEncoder, JwtConfig jwtConfig, UserDetailsServiceImpl userDetailsService){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtConfig.getUrl()).permitAll()
                .antMatchers(HttpMethod.POST, "/user/test").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
                .addFilter(new JwtTokenAuthorizationFilter(jwtConfig))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/


        /*http.csrf().disable()
                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
                .addFilterAfter(new JwtTokenAuthorizationFilter(jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                // allow all POST requests
                .antMatchers(HttpMethod.POST, jwtConfig.getUrl()).permitAll()
                .antMatchers(HttpMethod.POST, "/user/test1").permitAll()
                .antMatchers(HttpMethod.GET, new String[]{"/user/test2"}).permitAll()
                .antMatchers("/user/test3").hasRole("2")
                // any other requests must be authenticated
                .anyRequest().authenticated();*/

        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtConfig.getUrl()).permitAll()
                .antMatchers(HttpMethod.POST, "/user/test1").permitAll()
                .antMatchers(HttpMethod.POST, "/today/**").permitAll()
                .antMatchers(HttpMethod.POST, "/reminders/**").permitAll()
                .antMatchers(HttpMethod.GET, new String[]{"/user/test2"}).permitAll()
                .antMatchers("/user/test3").hasRole("2")
                // any other requests must be authenticated
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
                .addFilterAfter(new JwtTokenAuthorizationFilter(jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED));

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    /*@Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://example.com"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
