package com.capgemini.test.infrastructure.clients.dni;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "dniClient", url = "http://localhost:1080")
public interface DniFeignClient {

    @PostMapping("/check-dni")
    void checkDni(@RequestBody Map<String, String> request);
}
