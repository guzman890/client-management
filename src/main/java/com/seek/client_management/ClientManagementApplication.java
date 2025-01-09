package com.seek.client_management;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ClientManagementApplication {
	@Autowired
	private Flyway flyway;

	public static void main(String[] args) {
		SpringApplication.run(ClientManagementApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			flyway.migrate();
		};
	}

}
