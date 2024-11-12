package com.web.spring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.http.ResponseEntity;
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
	private Queue<Restaurant> pq = new PriorityQueue<>();
	
	@PostMapping("/restaurant")
	public ResponseEntity<?> restaurantRecommend(@RequestBody RestaurantReq restaurantReq) {
		
		List<Restaurant> list = new ArrayList<>();
		System.out.println("[RestaurantController] 실행중...");
		System.out.println("menu: " + restaurantReq.getMenu() + ", coreKeyword: " + restaurantReq.getCoreKeyword() + ", mainKeyword: " + restaurantReq.getMainKeyword());
		pq = restaurantService.restaurantRecommend(restaurantReq.getMenu(), restaurantReq.getCoreKeyword(), restaurantReq.getMainKeyword());
		System.out.println("[RestaurantController] 실행끝...");
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		
		return ResponseEntity.status(201).body(list);
	}

}
