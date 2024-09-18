package com.carbonhater.co2zerobookmark.user.service;

import com.carbonhater.co2zerobookmark.user.model.SignInResultDTO;
import com.carbonhater.co2zerobookmark.user.model.SignUpResultDto;
import com.carbonhater.co2zerobookmark.user.model.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public interface SignService {
    public SignUpResultDto signUp(String username, String password, String role);

    public SignInResultDTO signIn( UserDTO loginRequestDTO);
}
