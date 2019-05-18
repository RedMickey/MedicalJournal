package com.example.michel.rest_api;

import com.example.michel.rest_api.security.JwtConfig;
import com.example.michel.rest_api.utils.CalculationUtils;
import com.example.michel.rest_api.utils.ConvertingUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class AdditionalConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    public ConvertingUtils convertingUtils()
    {
        return new ConvertingUtils();
    }

    @Bean
    public CalculationUtils calculationUtils()
    {
        return new CalculationUtils();
    }

    /*@Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }*/

    /*@PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));   // It will set UTC timezone
    }*/
}
