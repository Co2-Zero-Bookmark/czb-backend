package com.carbonhater.co2zerobookmark.security;

import com.carbonhater.co2zerobookmark.user.repository.UserRepository;
import com.carbonhater.co2zerobookmark.user.repository.entity.CustomUserDetails;
import com.carbonhater.co2zerobookmark.user.repository.entity.User;
import com.carbonhater.co2zerobookmark.user.service.UserDetailsService;
import com.carbonhater.co2zerobookmark.user.service.impl.CustomUserDetailServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtProvider;

    //    public JWTFilter(JWTUtil jwtProvider, UserDetailsService customUserDetailsService) {
//        this.jwtProvider = jwtProvider;
//        this.customUserDetailsService = customUserDetailsService;
//    }
    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtProvider = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtProvider.resolveToken(request);
//        if (token != null && jwtProvider.validateToken(token)) {
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }

        System.out.println("token값" + token);

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token2 = authorization.split(" ")[1];
        System.out.println("token값2" + token2);
        //토큰 소멸 시간 검증 logic 제거

        String username = jwtProvider.getUsername(token);
        String role = jwtProvider.getRole(token);
        // 토큰이 유효하고 SecurityContext에 인증 정보가 없을 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //userEntity를 생성하여 값 set
            User userEntity = new User();
            userEntity.setUserEmail(username);
            userEntity.setRole(role);
        }
//        if (!Objects.isNull(authorization)) {
//            String atk = authorization.substring(7);
//            try {
//                String email = jwtProvider.createJwt(atk,"ROLE_USER", 3600000L);
////                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
//                Authentication token = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//                SecurityContextHolder.getContext()
//                        .setAuthentication(token);
//            } catch (JwtException e) {
//                request.setAttribute("exception", e.getMessage());
//            }
//        }
        filterChain.doFilter(request, response);
    }

}
