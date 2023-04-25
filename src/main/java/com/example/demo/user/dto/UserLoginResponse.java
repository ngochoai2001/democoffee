package com.example.demo.user.dto;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String success_url="http://com.hdv.magiccoffee/";
    private String accessToken;
    private String message;
    private int status;
}
