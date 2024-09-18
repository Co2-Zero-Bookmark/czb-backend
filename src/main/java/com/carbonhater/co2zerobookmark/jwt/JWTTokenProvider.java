package com.carbonhater.co2zerobookmark.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTTokenProvider {
    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Value("${spring.jwt.secret}")
    private String secretKey = "secretKey";


    private final long tokenValidMillisecond = 1000L * 60 * 60;

    @PostConstruct
    protected void init() {
        System.out.println("START - JwtTokenProvider - init");
        System.out.println("secretKey : " + secretKey);

        secretKey
                = Base64.getEncoder()
                .encodeToString( secretKey.getBytes(StandardCharsets.UTF_8) );

        System.out.println("secretKey : " + secretKey);
        System.out.println("END - JwtTokenProvider - init");
    } // init

    public String createToken( String userUid, List<String> roles ) {
        System.out.println("START - JwtTokenProvider - createToken");

        Claims claims = (Claims) Jwts.claims().setSubject(userUid); // subject - uid
        claims.put("roles", roles);

        Date now = new Date();

        String token
                = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration( new Date(now.getTime() + tokenValidMillisecond) )
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        System.out.println("token : " + token);
        System.out.println("END - JwtTokenProvider - createToken");

        return token;
    } // createToken

    public String getUsername( String token ) {
        System.out.println("START - JwtTokenProvider - getUsername");

        String info = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                        .getSubject();
//                .get("username", String.class);

        System.out.println("info : " + info);
        System.out.println("END - JwtTokenProvider - getUsername");

        return info;
    } // getUsername
    public Authentication getAuthentication(String token) {
        System.out.println("START - JwtTokenProvider - getAuthentication");

        UserDetails userDetails
                = userDetailsService.loadUserByUsername( this.getUsername(token) );

        System.out.println("username : " + userDetails.getUsername());
        System.out.println("END - JwtTokenProvider - getAuthentication");

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    } // getAuthentication

    public String resolveToken( HttpServletRequest request ) {
        System.out.println("START - JwtTokenProvider - resolveToken");
        String tmpStr = request.getHeader("Authorization");
        if(tmpStr.startsWith("Bearer "))
            return tmpStr.substring(7);
       return null;
    }
    public boolean validateToken( String token ) {
        System.out.println("START - JwtTokenProvider - validateToken");

        boolean tmpBool = false;
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            tmpBool = !claims.getBody().getExpiration().before(new Date());
        } catch(Exception e) {
            tmpBool = false;
        }

        System.out.println("tmpBool : " + tmpBool);
        System.out.println("END - JwtTokenProvider - validateToken");

        return tmpBool;
    } // validateToken

}


