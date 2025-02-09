package com.carbonhater.co2zerobookmark.config;


import com.carbonhater.co2zerobookmark.jwt.JWTAuthenticationFilter;
import com.carbonhater.co2zerobookmark.jwt.JWTTokenProvider;
import com.carbonhater.co2zerobookmark.security.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//비밀번호 암호화 관련: PasswordEncoder의 구현체 대입 -> Bean 등록
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    private JWTTokenProvider jwtUtil;
//    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTTokenProvider jwtUtil ) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
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
                        .requestMatchers("/api/v1/users/sign-up", "/api/v1/users/login", "/api/v1/**").permitAll()
                        .requestMatchers("/api/v1/folders/**").authenticated()
                        .anyRequest().authenticated())
//                .userDetailsService(userDetailsService()) todo
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTAuthenticationFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
//                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    // UserDetailsService 빈으로 등록



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

//
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); // 평문 비밀번호 비교( 오직 테스트용 )
        return new BCryptPasswordEncoder();
    }

}
