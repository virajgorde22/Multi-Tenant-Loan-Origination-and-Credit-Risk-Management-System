package com.loanorigination.document.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StorageConfig {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Bean
    public Path storagePath() {

        Path path = Paths
                .get(uploadDir)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create upload directory",
                    e
            );
        }

        return path;
    }
}