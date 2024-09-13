package com.carbonhater.co2zerobookmark.user.repository.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
// User Entity 대신  시큐리티 처리하는 데이터 클래스
public class CustomUserDetails implements UserDetails {


    private Long userId;
    private String email;
    private String userPassword;
    private int userPoint;

    public CustomUserDetails(User user) {
        this.userId = user.getUserId();
        this.email = user.getUserEmail();
        this.userPassword=user.getUserPassword();
        this.userPoint = user.getUserPoint();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.email;
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
