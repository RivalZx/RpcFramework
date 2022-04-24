package com.hezx.testmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class TestModuleApplication {
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
		SpringApplication.run(TestModuleApplication.class, args);
	}
	
}
