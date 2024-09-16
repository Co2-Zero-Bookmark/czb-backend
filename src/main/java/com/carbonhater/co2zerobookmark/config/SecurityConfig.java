package com.carbonhater.co2zerobookmark.config;


import io.swagger.v3.oas.models.info.Info;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // BCrypt 해시 함수로 암호화하는 구현체
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import static org.springframework.security.config.Customizer.withDefaults;

//비밀번호 암호화 관련: PasswordEncoder의 구현체 대입 -> Bean 등록
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private final UserDetailsService userDetailsService;
//    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    //antMatchers->requestMatchers
    //authorizeReuqest-> authorizeHttpRequest
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())// CSRF 보호 비활성화 (REST 환경)
            .formLogin((auth) -> auth.disable())
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/v1/users/signup", "api/v1/users/login", "/api/v1/**").permitAll()
                    .anyRequest().authenticated())
                .sessionManagement((session) ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }


}
