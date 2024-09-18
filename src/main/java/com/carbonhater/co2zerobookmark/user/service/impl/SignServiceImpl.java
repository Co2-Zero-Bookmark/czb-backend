package com.carbonhater.co2zerobookmark.user.service.impl;

import com.carbonhater.co2zerobookmark.user.model.SignInResultDTO;
import com.carbonhater.co2zerobookmark.user.model.SignUpResultDto;
import com.carbonhater.co2zerobookmark.user.model.UserDTO;
import com.carbonhater.co2zerobookmark.user.model.UserRole;
import com.carbonhater.co2zerobookmark.user.repository.UserRepository;
import com.carbonhater.co2zerobookmark.user.repository.entity.User;
import com.carbonhater.co2zerobookmark.user.service.SignService;
import com.carbonhater.co2zerobookmark.jwt.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SignServiceImpl implements SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    public SignServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public SignUpResultDto signUp(String username, String password, String role) {
        System.out.println("START - SignService - signUp");
        User user;
        if (role.equalsIgnoreCase("admin")) {
            user = User.builder()
                    .userEmail(username)
                    .userPassword(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .build();
        } else {
            user = User.builder()
                    .userEmail(username)
                    .userPassword(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }
        User savedUser = userRepository.save(user);
        SignUpResultDto signUpResultDto = new SignUpResultDto();
        if( !savedUser.getUserEmail().isEmpty() ) {
            setSuccessResult(signUpResultDto);
        } else {
            System.out.println("회원가입 실패! ");
        }
        return new SignUpResultDto(true, 200, "Sign up successful");
    }

    @Override
    public SignInResultDTO signIn(UserDTO loginRequestDTO) {
        // 로그인 로직이 필요하면 추가
        try {
            // UsernamePasswordAuthenticationToken으로 인증 시도
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserEmail(), loginRequestDTO.getPassword()));

            // 권한 설정 (예: ROLE_ADMIN)
            String authority = "ROLE_USER";  // 기본적으로 ADMIN 역할 사용
            List<String> authorities = List.of(authority);  // 권한을 리스트로 변환

            // JWT 토큰 생성
            String token = jwtTokenProvider.createToken(authentication.getName(), authorities);

            // 로그인 성공 시 결과 반환
            return SignInResultDTO.builder().success(true).status(200).token(token).message("Login 성공, 토큰 반환").build();

        } catch (AuthenticationException e) {
            // 로그인 실패 시 결과 반환
            return new SignInResultDTO(false, 401, "Login failed: " + e.getMessage());
        }
    }

    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user.getUserId();
    }

    public void setSuccessResult( SignUpResultDto result ) {
        result.setSuccess(true);
        result.setCode(200);
        result.setMsg("Login 성공");
    }
}
