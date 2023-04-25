package com.example.demo.notifications.controller;
import com.example.demo.notifications.dto.PushNotificationRequest;
import com.example.demo.notifications.dto.PushNotificationResponse;
import com.example.demo.notifications.service.PushNotificationService;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationController {
	
	
    private PushNotificationService pushNotificationService;
    
    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }
    
    @PostMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);
        System.out.println("princr");
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }
    @GetMapping("/notifications")
    public ResponseEntity getAllNotifications() {
        return new ResponseEntity<>( pushNotificationService.getAllNotis(), HttpStatus.OK);
    }
    
}