package com.carbonhater.co2zerobookmark.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // BCrypt 해시 함수로 암호화하는 구현체
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

//비밀번호 암호화 관련: PasswordEncoder의 구현체 대입 -> Bean 등록
@Configuration
@EnableWebSecurity(debug=true)
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig {
    @Autowired
    UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers((headers)-> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                )))
                .formLogin(form -> form
                        .loginPage("/api/v1/users/login")   // 커스텀 로그인 페이지 경로
                        .defaultSuccessUrl("/")     // 로그인 성공 후 리디렉션할 경로
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/users/logout")  // 로그아웃 URL
                        .logoutSuccessUrl("/")      // 로그아웃 성공 후 리디렉션할 경로
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/v1/users/**").permitAll() // 사용자 경로 허용/v1/folders/
                        .requestMatchers("/api/v1/users/myPage").authenticated()
                        .requestMatchers("/api/v1/users/{user_id}/badges").authenticated()
                        .anyRequest().authenticated()  // 나머지 경로 요청은 인증 필요
                )
                .userDetailsService(userDetailsService)
                .build();

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org")
                .and()
                .passwordCompare()
                .passwordEncoder(getPasswordEncoder())
                .passwordAttribute("userPassword");


    }


}