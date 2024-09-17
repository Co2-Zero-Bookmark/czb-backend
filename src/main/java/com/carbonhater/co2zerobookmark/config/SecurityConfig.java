package com.carbonhater.co2zerobookmark.config;


import com.carbonhater.co2zerobookmark.security.JWTFilter;
import io.swagger.v3.oas.models.info.Info;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // BCrypt 해시 함수로 암호화하는 구현체
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

//LoginFilter와 JwtUtil import
import com.carbonhater.co2zerobookmark.security.LoginFilter;
import com.carbonhater.co2zerobookmark.security.JWTUtil;
import com.carbonhater.co2zerobookmark.user.service.impl.CustomUserDetailServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;

//비밀번호 암호화 관련: PasswordEncoder의 구현체 대입 -> Bean 등록
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    @Autowired
    private final JWTUtil jwtUtil;
    @Autowired
    private final CustomUserDetailServiceImpl customUserDetailsService; // CustomUserDetailServiceImpl 주입
//    @Autowired
//    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, CustomUserDetailServiceImpl customUserDetailsService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }


    //antMatchers->requestMatchers
    //authorizeReuqest-> authorizeHttpRequest
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // LoginFilter 등록
//        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);


        http
                .csrf(csrf -> csrf.disable())// CSRF 보호 비활성화 (REST 환경)
                .formLogin((auth) -> auth.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/users/signup", "api/v1/users/login", "/api/v1/*").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
//                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    // UserDetailsService 빈으로 등록
    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;  // CustomUserDetailServiceImpl 등록
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//
////        AuthenticationManagerBuilder authenticationManagerBuilder =
////                http.getSharedObject(AuthenticationManagerBuilder.class);
////        authenticationManagerBuilder
////                .userDetailsService(customUserDetailsService)
////                .passwordEncoder(passwordEncoder());
//        return configuration.getAuthenticationManager();
////        return authenticationManagerBuilder.build();
//    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // 평문 비밀번호 비교( 오직 테스트용 )
//        return new BCryptPasswordEncoder();
    }

}
