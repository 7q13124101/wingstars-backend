package com.wingstars.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.wingstars")
public class WingstarsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WingstarsApplication.class, args);
    }
}
