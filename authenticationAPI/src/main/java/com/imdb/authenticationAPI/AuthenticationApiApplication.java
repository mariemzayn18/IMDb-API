package com.imdb.authenticationAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.imdb.*")
public class AuthenticationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApiApplication.class, args);
	}

}
