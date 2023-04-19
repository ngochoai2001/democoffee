package com.example.demo.sms;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class TwilioOTPService implements OTPSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(TwilioOTPService.class);

    private static final Integer EXPIRE_MINS = 1;

    private  static LoadingCache<String, String> otpMap ;

    String message = "Your confirmation OTP is: ";
    private final TwilioConfiguration twilioConfiguration;
    @Autowired
    public TwilioOTPService(TwilioConfiguration twilioConfiguration){
        this.twilioConfiguration = twilioConfiguration;
        otpMap = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    public String load(String key) {
                        return "";
                    }
                    });

    }
    @Override
    public OTPDto sendOTP(String phoneNum) {
//        phoneNum = "+84868478340";
        OTPDto otpDto = null;
        try {
            String otp = generateOTP();
            PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
            PhoneNumber to = new PhoneNumber(phoneNum);
            Message.creator(to, from, message+otp).create();
            otpMap.put(phoneNum, otp);
            otpDto = new OTPDto();
            otpDto.setOtp(otp);
            otpDto.setPhoneNum(phoneNum);
            System.out.println(to);
        }catch(Exception e){
            LOGGER.info(e.getMessage());
        }
        return otpDto;

    }
    @Override
    public boolean validateOTP(OTPDto otpDto) throws ExecutionException {

        String userOTP = otpDto.getOtp();
        String sendedOTP = otpMap.get(otpDto.getPhoneNum());
        boolean result = sendedOTP == null? false: sendedOTP.equalsIgnoreCase(userOTP);
        otpMap.invalidate(otpDto.getPhoneNum());
        return result;

    }
    private String generateOTP(){
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
}
