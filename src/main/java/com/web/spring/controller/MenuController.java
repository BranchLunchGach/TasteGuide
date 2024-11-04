package com.web.spring.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.spring.dto.MenuReq;
import com.web.spring.service.MenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;
	
	@PostMapping("/menu")
	public ResponseEntity<?> nugury(@RequestBody MenuReq menuReq){
		Map<String, List<String>> menuMap = new HashMap<>();
		menuMap.put("nation", Arrays.asList(menuReq.getNation().split(",")));
		menuMap.put("category", Arrays.asList(menuReq.getCategory().split(",")));
		menuMap.put("concept", Arrays.asList(menuReq.getKeyword().split(",")));
		menuMap.put("soup", Arrays.asList(menuReq.getSoup()));
		return ResponseEntity.status(201).body(menuService.nugury(menuMap));
	}
}
