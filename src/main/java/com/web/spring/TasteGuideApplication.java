package com.web.spring;


import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.web.spring.entity.Restaurant;
import com.web.spring.service.RestaurantService;

@SpringBootApplication

public class TasteGuideApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasteGuideApplication.class, args);
		
	}
}
