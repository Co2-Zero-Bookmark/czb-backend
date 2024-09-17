package com.carbonhater.co2zerobookmark.security;

import com.carbonhater.co2zerobookmark.jwt.JWTTokenProvider;
import com.carbonhater.co2zerobookmark.user.model.UserRole;
import com.carbonhater.co2zerobookmark.user.repository.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.*;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTTokenProvider jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 추후 request본문에서 JSON data 파싱하는 로직으로 변경
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        System.out.println("login success");
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)  // GrantedAuthority에서 역할 이름 추출
                .collect(Collectors.toList());
//        String role = UserRole.ADMIN.getValue();
        // 뽑아낸 username, role을 기반으로 jwt토큰 생성
        String token = jwtUtil.createToken(username, roles ); // expiredMS accessToken 유지시간

        response.addHeader("Authorization", "Bearer " + token);

    }

    //로그인 실패시 실행하는 메소드는 일단 시큐리티 자체 필터 체인 따라 예외 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}
/*
    로그인 요청을 처리하는 부분에서, 사용자가 올바르게 인증되면 JWTUtil.createJwt()를 호출하여 JWT를 생성
 */