package com.example.demo.sms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SMSRequest {
    private final String phoneNumber;
    private final String message;

}
