package com.moov.moovservice;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class MoovserviceApplication {


	
	public static void main(String[] args) {
		SpringApplication.run(MoovserviceApplication.class, args);
	}

	@PostConstruct
	public void initHlr() throws IllegalArgumentException, IOException, InterruptedException {
	}

	@PostConstruct
	public void initEcms() {
		
	}
	
}
