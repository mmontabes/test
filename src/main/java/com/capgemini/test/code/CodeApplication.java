package com.capgemini.test.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.capgemini.test.infrastructure.clients")
@ComponentScan(basePackages = "com.capgemini.test")
public class CodeApplication {

  public static void main(String[] args) {
    SpringApplication.run(CodeApplication.class, args);
  }
}