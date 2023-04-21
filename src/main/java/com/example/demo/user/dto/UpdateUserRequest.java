package com.example.demo.user.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    String fullname;
    String email;
    String phoneNum;
}
