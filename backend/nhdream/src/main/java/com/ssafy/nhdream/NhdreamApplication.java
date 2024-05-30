package com.ssafy.nhdream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ssafy.nhdream.domain.*.repository")
public class NhdreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(NhdreamApplication.class, args);
	}

}
