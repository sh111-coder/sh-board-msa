package com.shboard.shboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ShboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShboardApplication.class, args);
	}

}
