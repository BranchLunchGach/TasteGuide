package com.web.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class TasteGuideApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TasteGuideApplication.class, args);
		
	}
	
}
