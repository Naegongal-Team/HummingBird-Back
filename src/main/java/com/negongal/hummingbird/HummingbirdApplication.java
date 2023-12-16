package com.negongal.hummingbird;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HummingbirdApplication {

	public static void main(String[] args) {
		SpringApplication.run(HummingbirdApplication.class, args);
	}

}
