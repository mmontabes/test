package com.capgemini.test.infrastructure.clients.notfications;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    private String phone;
    private String message;
}