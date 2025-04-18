package com.capgemini.test.domain.ports;

import com.capgemini.test.infrastructure.clients.notfications.EmailRequest;
import com.capgemini.test.infrastructure.clients.notfications.SmsRequest;

public interface NotificationPort {
    void sendEmail(EmailRequest request);
    void sendSms(SmsRequest request);
}