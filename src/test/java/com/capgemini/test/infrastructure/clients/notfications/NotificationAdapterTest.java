package com.capgemini.test.infrastructure.clients.notfications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class NotificationAdapterTest {

    private NotificationFeignClient client;
    private NotificationAdapter adapter;

    @BeforeEach
    void setUp() {
        client = mock(NotificationFeignClient.class);
        adapter = new NotificationAdapter(client);
    }

    @Test
    void shouldSendEmail() {
        EmailRequest request = new EmailRequest("email@example.com", "Mensaje de prueba");
        adapter.sendEmail(request);
        verify(client).sendEmail(request);
    }

    @Test
    void shouldSendSms() {
        SmsRequest request = new SmsRequest("600123456", "Mensaje SMS");
        adapter.sendSms(request);
        verify(client).sendSms(request);
    }
}
