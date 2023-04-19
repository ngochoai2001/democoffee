package com.example.demo.user.dto;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String accessToken;
    private String message;
    private String status;
}
