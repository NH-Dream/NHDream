package com.ssafy.nhdream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NhdreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(NhdreamApplication.class, args);
	}

}
