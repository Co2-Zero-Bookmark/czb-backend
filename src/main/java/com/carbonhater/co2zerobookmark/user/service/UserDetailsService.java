package com.carbonhater.co2zerobookmark.user.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException;
}
