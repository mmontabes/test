package com.capgemini.test.infrastructure.clients.notfications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationClient", url = "http://localhost:1080")
public interface NotificationFeignClient {

    @PostMapping("/email")
    void sendEmail(@RequestBody EmailRequest emailRequest);

    @PostMapping("/sms")
    void sendSms(@RequestBody SmsRequest smsRequest);
}