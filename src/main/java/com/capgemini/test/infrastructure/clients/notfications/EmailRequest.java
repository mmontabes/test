package com.capgemini.test.infrastructure.clients.notfications;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    private String email;
    private String message;
}