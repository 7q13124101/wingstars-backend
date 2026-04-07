package com.wingstars.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.wingstars")
@EntityScan(basePackages = "com.wingstars")               // Quét Entity ở các module khác
@EnableJpaRepositories(basePackages = "com.wingstars")
public class WingstarsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WingstarsApplication.class, args);
    }
}
