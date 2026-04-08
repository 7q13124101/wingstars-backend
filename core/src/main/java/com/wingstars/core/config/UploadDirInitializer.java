package com.wingstars.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UploadDirInitializer {

    @Bean
    ApplicationRunner ensureUploadDirExists(
            @Value("${file.upload-dir:uploads/}") String uploadDir
    ) {
        return args -> {
            Path baseDir = Paths.get(uploadDir).toAbsolutePath().normalize();
            try {
                Files.createDirectories(baseDir);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not create upload directory: " + baseDir, ex);
            }
        };
    }
}

