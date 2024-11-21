package com.web.spring.controller;

import java.util.ArrayList;
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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {
	
	private final RestaurantService restaurantService;
	
	@PostMapping("/restaurant")
	public ResponseEntity<?> restaurantRecommend(@RequestBody RestaurantReq restaurantReq) {

		List<Restaurant> list = new ArrayList<>();
		Queue<Restaurant> pq = new PriorityQueue<>();
		
		log.info("[RestaurantController] /restaurant 실행중...");
		try {
			pq = restaurantService.restaurantRecommend(restaurantReq.getMenu(), restaurantReq.getCoreKeyword(), restaurantReq.getMainKeyword(), restaurantReq.getAvgX(), restaurantReq.getAvgY());
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("[RestaurantController] /restaurant 실행 실패...");
		}
		log.info("[RestaurantController] /restaurant 실행끝...");
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		
		return ResponseEntity.status(201).body(list);
	}
	
	@PostMapping("/hello-restaurant")
	public ResponseEntity<?> helloRecommend(@RequestBody RestaurantReq restaurantReq) {
		
		List<Restaurant> list = new ArrayList<>();
		Queue<Restaurant> pq = new PriorityQueue<>();
		
		log.info("[RestaurantController] /hello-restaurant 실행중...");
		try {
			pq = restaurantService.helloRecommend(restaurantReq.getMenu(), restaurantReq.getAvgX(), restaurantReq.getAvgY());
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("[RestaurantController] /hello-restaurant 실행 실패...");
		}
		
		log.info("[RestaurantController] /hello-restaurant 실행끝...");
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());

		return ResponseEntity.status(201).body(list);
	}
	
	@PostMapping("/ai-restaurant")
	public ResponseEntity<?> aiRecommend(@RequestBody RestaurantReq restaurantReq) {
		
		List<Restaurant> list = new ArrayList<>();
		Queue<Restaurant> pq = new PriorityQueue<>();
		
		log.info("[RestaurantController] /ai-restaurant 실행중...");
		try {
			pq = restaurantService.aiRecommend(restaurantReq.getMenu());
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("[RestaurantController] /ai-restaurant 실행실패...");
		}
		
		log.info("[RestaurantController] /ai-restaurant 실행끝...");
		list.add(pq.poll());
		list.add(pq.poll());
		list.add(pq.poll());
		
		return ResponseEntity.status(201).body(list);
	}

}
