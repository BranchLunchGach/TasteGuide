package com.web.spring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.spring.api.MapAPI;
import com.web.spring.dto.RestaurantReq;
import com.web.spring.entity.Restaurant;
import com.web.spring.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
	
	private final RestaurantService restaurantService;
	private final MapAPI mapAPI;
	
	@PostMapping("/restaurant")
	public ResponseEntity<?> restaurantRecommend(@RequestBody RestaurantReq restaurantReq) {
		
		List<Restaurant> list = new ArrayList<>();
		Queue<Restaurant> pq = new PriorityQueue<>();
		
		System.out.println("[RestaurantController] 실행중...");
		System.out.println("menu: " + restaurantReq.getMenu() + ", coreKeyword: " + restaurantReq.getCoreKeyword() + ", mainKeyword: " + restaurantReq.getMainKeyword());
		pq = restaurantService.restaurantRecommend(restaurantReq.getMenu(), restaurantReq.getCoreKeyword(), restaurantReq.getMainKeyword());
		System.out.println("[RestaurantController] 실행끝...");
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		
		return ResponseEntity.status(201).body(list);
	}
	
	@PostMapping("/hello-restaurant")
//	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<?> helloRecommend(@RequestBody RestaurantReq restaurantReq) {
		
		long start = System.currentTimeMillis();
		List<Restaurant> list = new ArrayList<>();
		Queue<Restaurant> pq = new PriorityQueue<>();
		
		System.out.println("[RestaurantController] 실행중...");
		pq = restaurantService.helloRecommend(restaurantReq.getMenu(), restaurantReq.getAvgX(), restaurantReq.getAvgY());
		System.out.println("[RestaurantController] 실행끝...");
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		
		long end = System.currentTimeMillis();
		System.out.println("걸린시간 : " + (end-start) + "ms");
		
		return ResponseEntity.status(201).body(list);
	}

}
