package com.carbonhater.co2zerobookmark.user.controller;

import com.carbonhater.co2zerobookmark.user.model.SignInResultDTO;
import com.carbonhater.co2zerobookmark.user.model.SignUpResultDto;
import com.carbonhater.co2zerobookmark.user.model.UserDTO;
import com.carbonhater.co2zerobookmark.user.model.UserRole;
import com.carbonhater.co2zerobookmark.user.repository.UserRepository;
import com.carbonhater.co2zerobookmark.user.repository.entity.CustomUserDetails;
import com.carbonhater.co2zerobookmark.user.repository.entity.User;
import com.carbonhater.co2zerobookmark.user.service.SignService;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import com.carbonhater.co2zerobookmark.jwt.JWTTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; //제거 고민
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class SignController {
    private final SignService signService;
    private final PasswordEncoder passwordEncoder;

    public SignController(SignService signService, PasswordEncoder passwordEncoder) {
        this.signService = signService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value="/sign-up")
    public SignUpResultDto signUp(@RequestParam String username,@RequestParam String password, @RequestParam String role) {
        return signService.signUp(username,password,role);
    }


    @PostMapping(value = "/login")
    public SignInResultDTO signIn(@RequestBody UserDTO loginRequestDTO) {

        return signService.signIn(loginRequestDTO);
    }


}
