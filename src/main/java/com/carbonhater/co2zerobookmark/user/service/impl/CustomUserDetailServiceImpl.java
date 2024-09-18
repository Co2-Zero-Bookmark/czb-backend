package com.carbonhater.co2zerobookmark.user.service.impl;

import com.carbonhater.co2zerobookmark.user.repository.UserRepository;
import com.carbonhater.co2zerobookmark.user.repository.entity.CustomUserDetails;
import com.carbonhater.co2zerobookmark.user.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {
    private  UserRepository userRepository;
    @Autowired
    public CustomUserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CustomUserDetails형으로 사용자 정보를 가지고 온다. 정보의 유/무에 따라 예외 혹은 사용자 정보를 리턴
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
// Optional을 사용한 JPA 조회

//        return userRepository.findByUserEmail(userEmail)
//                .map(CustomUserDetails::new)  // 성공 시 User를 CustomUserDetails로 매핑
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));  // 실패 시 예외 처리
//        //JPA Entity
        Optional<User> user = userRepository.findByUserEmail(userEmail);
        if (user.isEmpty()) {
            System.out.println("User not found with email: " + userEmail);
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("User found with email: " + userEmail);
        return new CustomUserDetails(user.get());
    }
}
