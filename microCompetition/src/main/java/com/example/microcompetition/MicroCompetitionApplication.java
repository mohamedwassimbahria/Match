package com.example.microcompetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroCompetitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroCompetitionApplication.class, args);
	}

}
