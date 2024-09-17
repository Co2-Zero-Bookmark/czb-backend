package com.carbonhater.co2zerobookmark.test;

import com.carbonhater.co2zerobookmark.security.JWTUtil;
import com.carbonhater.co2zerobookmark.user.model.SignInResultDTO;
import com.carbonhater.co2zerobookmark.user.model.UserDTO;
import com.carbonhater.co2zerobookmark.user.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/")
public class WebController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;  // JWTUtil로 변경

    @GetMapping("/")
    public String home(Model model) {
        log.info("=============log Test=============");
        model.addAttribute("message", "Hello, Thymeleaf!");
        return "index"; // 'index.html' 파일을 렌더링
    }

    @PostMapping(value = "/login")
    public SignInResultDTO login(@RequestBody UserDTO loginRequestDTO) {
        try {
            // UsernamePasswordAuthenticationToken으로 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserEmail(), loginRequestDTO.getPassword())
            );
            String authority =  UserRole.ADMIN.getValue();
            // JWT 토큰 생성
            String token = jwtUtil.createJwt(authentication.getName(),
                    authority,
                    3600000L
            );


            // 로그인 성공 시 결과 반환
            return new SignInResultDTO(true, 200, "Login successful", token);

        } catch (AuthenticationException e) {
            // 로그인 실패 시 결과 반환
            return new SignInResultDTO(false, 401, "Login failed: " + e.getMessage());
        }
    }
}

/*
todo JWTUtil=> JWTTokenProvider로 변경

 @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String token = jwtTokenProvider.createToken(username, "ROLE_USER");

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", token);
        return response;
    }
 */