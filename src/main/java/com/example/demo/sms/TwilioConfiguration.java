package com.example.demo.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwilioConfiguration {
    private String accountSid;
    private String authToken;
    private String trialNumber;

}
