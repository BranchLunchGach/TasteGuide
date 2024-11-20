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

public class TasteGuideApplication implements CommandLineRunner {
	
	@Autowired
	RestaurantService restaurantService;
	
	String startX = "126.983197";
	String startY = "37.570176";
	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		Queue<Restaurant> pq = new PriorityQueue<>();
		pq = restaurantService.restaurantRecommend("파스타", null, "양많음, 가성비", startX, startY);
		
		System.out.println("===================== 식당 추천 시작 =====================");
		for (int i = 0; i < 3; i++)
			System.out.println(pq.poll());
		System.out.println("===================== 식당 추천 끝 =====================");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TasteGuideApplication.class, args);
		
	}
}
