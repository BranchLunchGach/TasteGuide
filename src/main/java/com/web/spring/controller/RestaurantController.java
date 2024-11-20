package com.web.spring.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.spring.dto.RestaurantReq;
import com.web.spring.entity.Restaurant;
import com.web.spring.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
	
	private final RestaurantService restaurantService;
	
	@PostMapping("/restaurant")
	public ResponseEntity<?> restaurantRecommend(@RequestBody RestaurantReq restaurantReq) {
		
		long start = System.currentTimeMillis();
		List<Restaurant> list = new ArrayList<>();
		Queue<Restaurant> pq = new PriorityQueue<>();
		
		System.out.println("[RestaurantController] 실행중...");
		pq = restaurantService.restaurantRecommend(restaurantReq.getMenu(), restaurantReq.getCoreKeyword(), restaurantReq.getMainKeyword(), restaurantReq.getAvgX(), restaurantReq.getAvgY());
		System.out.println("[RestaurantController] 실행끝...");
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		
		long end = System.currentTimeMillis();
		System.out.println("걸린시간 : " + (end-start) + "ms");
		
		return ResponseEntity.status(201).body(list);
	}
	
	@PostMapping("/hello-restaurant")
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
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		
		long end = System.currentTimeMillis();
		System.out.println("걸린시간 : " + (end-start) + "ms");
		
		return ResponseEntity.status(201).body(list);
	}
	
	@PostMapping("/ai-restaurant")
	public ResponseEntity<?> aiRecommend(@RequestBody RestaurantReq restaurantReq) {
		
		long start = System.currentTimeMillis();
		List<Restaurant> list = new ArrayList<>();
		Queue<Restaurant> pq = new PriorityQueue<>();
		
		System.out.println("[RestaurantController] 실행중...");
		pq = restaurantService.aiRecommend(restaurantReq.getMenu());
		System.out.println("[RestaurantController] 실행끝...");
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());

		long end = System.currentTimeMillis();
		System.out.println("걸린시간 : " + (end-start) + "ms");
		
		return ResponseEntity.status(201).body(list);
	}

}
