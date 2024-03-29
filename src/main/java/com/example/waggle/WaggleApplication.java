package com.example.waggle;

import com.example.waggle.global.util.SecurityUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class WaggleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaggleApplication.class, args);
    }


    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(SecurityUtil.getCurrentUsername());
    }

}
