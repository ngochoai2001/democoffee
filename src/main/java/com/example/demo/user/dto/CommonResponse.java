package com.example.demo.user.dto;

import lombok.Data;

@Data
public class CommonResponse {
    private String message;
    private int status;

    public CommonResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
