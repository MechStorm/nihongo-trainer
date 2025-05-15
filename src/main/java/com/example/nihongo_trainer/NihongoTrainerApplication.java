package com.example.nihongo_trainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NihongoTrainerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NihongoTrainerApplication.class, args);
	}

}
