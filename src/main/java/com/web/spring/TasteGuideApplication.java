package com.web.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.web.spring.repository.UserRepository;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@SpringBootApplication

public class TasteGuideApplication {
	/*
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		
		//userRepository.deleteByUserId("kosta4");
	}
	*/
	public static void main(String[] args) {
		SpringApplication.run(TasteGuideApplication.class, args);
		
	}
	
	
}
