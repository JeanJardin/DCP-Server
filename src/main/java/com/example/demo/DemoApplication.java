package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**

 This is the main class of the application that contains the main method.

 The class is annotated with @SpringBootApplication, which enables Spring Boot

 auto-configuration and component scanning. The @EnableScheduling annotation

 enables the scheduling of tasks in the application.
 */
@SpringBootApplication
@EnableScheduling
public class DemoApplication {
	/**
	 The main method that starts the Spring Boot application.
	 @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	/**
	 A simple endpoint for testing purposes.
	 @return a string message
	 */
	@GetMapping("")
	public String hello(){
		return "Hello !";
	}
}
