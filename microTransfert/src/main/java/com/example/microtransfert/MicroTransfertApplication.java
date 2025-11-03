package com.example.microtransfert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroTransfertApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroTransfertApplication.class, args);
	}

}
