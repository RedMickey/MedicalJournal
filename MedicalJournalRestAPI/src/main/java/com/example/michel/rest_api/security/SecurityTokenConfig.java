package com.example.michel.rest_api.security;

import com.example.michel.rest_api.security.filters.JwtTokenAuthorizationFilter;
import com.example.michel.rest_api.security.filters.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
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

    /*public class CustomFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws ServletException, IOException {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Headers",
                    "Authorization, content-type, xsrf-token, Cache-Control, remember-me, WWW-Authenticate");
            response.addHeader("Access-Control-Expose-Headers", "Authorization");
            chain.doFilter(request, response);

        }

    }*/

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

        //http.addFilterBefore(new CustomFilter(), ChannelProcessingFilter.class);

        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtConfig.getUrl()).permitAll()
                .antMatchers(HttpMethod.POST, "/user/test1").permitAll()
                .antMatchers(HttpMethod.POST, "/today/**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                //.antMatchers(HttpMethod.POST, "/reminders/**").permitAll()
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configAutenticacao = new CorsConfiguration();
        configAutenticacao.setAllowCredentials(true);
        configAutenticacao.setAllowedOrigins(Arrays.asList("*"));
        configAutenticacao.setAllowedHeaders(Arrays.asList("Authorization", "RefreshToken"));
        configAutenticacao.addAllowedHeader("Content-Type");
        configAutenticacao.addAllowedHeader("Accept");
        configAutenticacao.addAllowedMethod("POST");
        configAutenticacao.addAllowedMethod("GET");
        configAutenticacao.addAllowedMethod("DELETE");
        configAutenticacao.addAllowedMethod("PUT");
        configAutenticacao.addAllowedMethod("OPTIONS");
        configAutenticacao.setExposedHeaders(Arrays.asList("Authorization", "RefreshToken"));
        configAutenticacao.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configAutenticacao);
        return source;
    }
}
