package com.carbonhater.co2zerobookmark.user.repository.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String email;
    private String userPassword;
    private int userPoint;

    public CustomUserDetails(User user) {
        this.userId = user.getUserId();
        this.email = user.getUserEmail();
        this.userPassword = user.getUserPassword();
        this.userPoint = user.getUserPoint();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.getPassword();
    }

    @Override
    public String getUsername() {
        return this.getUsername();
        /*
            일반적인 로직에서는 this.email을 사용해도 되지만, Spring Security와
            관련된 인증 절차에서는 getUsername()을 재정의해서 이메일을 반환하는 방식으로 처리
         */
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


}
