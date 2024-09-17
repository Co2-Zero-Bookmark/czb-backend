package com.carbonhater.co2zerobookmark.security;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JWTUtil2 {

    private final SecretKey secretKey;

    public JWTUtil2(@Value("${spring.jwt.secret}") String secret) {
        // secretKeySpec 생성 -> HS256 알고리즘을 사용한 비밀키
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰에서 username 추출
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 토큰에서 role 추출
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // 토큰의 만료 여부 확인
    public Boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // JWT 생성
    public String createJwt(String username, String role, Long expiredMs) {
        return Jwts.builder()
                .setSubject(username)  // 토큰의 subject에 username 저장
                .claim("role", role)    // 토큰에 role 정보 추가
                .setIssuedAt(new Date()) // 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시간
                .signWith(secretKey)     // 서명 알고리즘 및 비밀키 설정
                .compact();
    }

    // 토큰에서 클레임(Claims) 추출
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // 서명된 secretKey로 검증
                .build()
                .parseClaimsJws(token)    // 파싱 후 JWT에서 Claims 추출
                .getBody();
    }
}

