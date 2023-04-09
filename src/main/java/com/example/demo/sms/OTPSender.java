package com.example.demo.sms;
import reactor.core.publisher.Mono;

public interface OTPSender {
     OTPDto sendOTP(String phoneNum);
     boolean validateOTP(OTPDto otpDto);
}
