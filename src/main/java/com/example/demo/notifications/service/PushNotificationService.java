package com.example.demo.notifications.service;

import com.example.demo.notifications.dto.PushNotificationRequest;
import com.example.demo.notifications.model.Notification;
import com.example.demo.notifications.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushNotificationService {
	
    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    
    private FCMService fcmService;
    @Autowired
    private NotificationRepository notiRepo;
    
    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }
    
    
    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
            Notification noti = new Notification();
            noti.setTitle(request.getTitle());
            noti.setTopic(request.getTopic());
            noti.setMessage(request.getMessage());
            notiRepo.save(noti);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public List<Notification> getAllNotis(){
        return notiRepo.findAll();
    }
   
}