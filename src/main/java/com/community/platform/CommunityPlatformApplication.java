package com.community.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CommunityPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityPlatformApplication.class, args);
    }
}