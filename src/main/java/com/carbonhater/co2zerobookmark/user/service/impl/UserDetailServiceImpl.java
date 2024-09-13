package com.carbonhater.co2zerobookmark.user.service.impl;

import com.carbonhater.co2zerobookmark.user.repository.UserRepository;
import com.carbonhater.co2zerobookmark.user.repository.entity.CustomUserDetails;
import com.carbonhater.co2zerobookmark.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        //JPA Entity
        Optional<User> user = userRepository.findByUserEmail(userEmail);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        //User Domain

        CustomUserDetails customUserDetails = new CustomUserDetails(user.get());
        return customUserDetails;
    }
}
