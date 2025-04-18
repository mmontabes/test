package com.capgemini.test.infrastructure.clients.dni;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
 
import feign.Logger;
import okhttp3.OkHttpClient;

@FeignClient(name = "checkDniClient", url = "${external.service.url}", configuration = DniAdapter.FeignConfig.class)
public interface DniAdapter {
  @Configuration
  public class FeignConfig {
 
    @Bean(name = "customOkHttpClient")
    public OkHttpClient okHttpClient() {
      return new OkHttpClient();
    }
 
    @Bean(name = "customLogger")
    public Logger.Level feignLoggerLevel() {
      return Logger.Level.FULL; // Puedes ajustar el nivel de los logs según tu necesidad
    }
  }
 
  @PostMapping(value = "/check-dni")
  ResponseEntity<CheckDniResponse> check(@RequestBody CheckDniRequest request);
}
 
