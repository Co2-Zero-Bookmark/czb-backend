package com.carbonhater.co2zerobookmark.user.service;


import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException;
}
