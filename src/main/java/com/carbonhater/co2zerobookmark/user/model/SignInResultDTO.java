package com.carbonhater.co2zerobookmark.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignInResultDTO {

    private boolean success;
    private String message;
    private int status;

    private String token;

    public SignInResultDTO(boolean success, int code, String msg, String token) {

        this.success = success;
        this.status = status;
        this.message = message;
        this.token = token;
    }
    // Constructor for failed login
    public SignInResultDTO( boolean success, int status, String message) {
        this.success = success;
        this.status = status;
        this.message = message;
    }
}
