package com.techie.microservices.login;

import com.techie.microservices.login.model.User;
import com.techie.microservices.login.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class LoginServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(LoginServiceApplication.class, args);

	}

}
