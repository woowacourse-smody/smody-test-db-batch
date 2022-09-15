package com.example.smodytestdbbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmodyTestDbBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmodyTestDbBatchApplication.class, args);
	}

}
