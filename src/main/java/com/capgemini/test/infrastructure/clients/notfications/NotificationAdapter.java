package com.capgemini.test.infrastructure.clients.notfications;

import com.capgemini.test.domain.ports.NotificationPort;
import org.springframework.stereotype.Component;

@Component
public class NotificationAdapter implements NotificationPort {

    private final NotificationFeignClient notificationFeignClient;

    public NotificationAdapter(NotificationFeignClient notificationFeignClient) {
        this.notificationFeignClient = notificationFeignClient;
    }

    @org.springframework.web.bind.annotation.PostMapping("/email")
    @Override
    public void sendEmail(EmailRequest request) {
        notificationFeignClient.sendEmail(request);
    }

    @Override
    public void sendSms(SmsRequest request) {
        notificationFeignClient.sendSms(request);
    }
}
