package com.capgemini.test.domain.ports;

public interface NotificationPort {
    void sendEmail(String email, String message);
    void sendSms(String phone, String message);
}