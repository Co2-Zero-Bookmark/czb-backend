package com.carbonhater.co2zerobookmark.jwt;

import com.carbonhater.co2zerobookmark.user.service.impl.CustomUserDetailServiceImpl;
import com.carbonhater.co2zerobookmark.security.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private JWTTokenProvider jwtTokenProvider;

    public JWTAuthenticationFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("START - JwtAuthenticationFilter - doFilterInternal");

        String token = jwtTokenProvider.resolveToken(request);
//auhorization 헤더가 있는지 확인하고 "Bearer "로 시작하는지 검증하는 부분까지 구현되어 있습
        if( token != null && jwtTokenProvider.validateToken(token) ) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token); // roles
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

    }
}
