package com.example.micromatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroMatchApplication.class, args);
	}

}
