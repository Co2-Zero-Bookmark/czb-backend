package com.carbonhater.co2zerobookmark.user.login;

import com.carbonhater.co2zerobookmark.user.dto.LoginResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/users")
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
/*
    @GetMapping("/login")
    public ResponseEntity<LoginResponseDto> login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자가 존재하는지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Authentication failed.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 로그인한 사용자 정보 가져오기 (Principal에서 CustomUserDetails 사용 가능)
        String username = authentication.getName();  // 인증된 사용자 이름

        // 응답 생성: LoginResponseDto는 로그인 결과를 담은 DTO
        LoginResponseDto response = new LoginResponseDto(username, "Login successful");
        return ResponseEntity.status(HttpStatus.OK).body(LoginResponseDto);
    }
*/
