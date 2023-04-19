package com.example.demo.sms;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

public interface OTPSender {
     OTPDto sendOTP(String phoneNum);
     boolean validateOTP(OTPDto otpDto) throws ExecutionException;
}
