package com.carbonhater.co2zerobookmark.user.repository;

import com.carbonhater.co2zerobookmark.user.repository.entity.CustomUserDetails;
import com.carbonhater.co2zerobookmark.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

//    public User getByUid(Long userId); //user_id DB에 존재
    Optional<User> findByUserEmail(String userEmail);
}
