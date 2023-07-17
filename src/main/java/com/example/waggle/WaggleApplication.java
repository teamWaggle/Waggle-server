package com.example.waggle;

import com.example.waggle.component.jwt.SecurityUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

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
