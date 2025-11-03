package com.example.microjoueur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroJoueurApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroJoueurApplication.class, args);
	}

}
