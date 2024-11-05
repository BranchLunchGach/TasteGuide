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
		
		List<String> nationList = (menuReq.getNation()==null) ? Arrays.asList("양식","한식","중식","일식") : Arrays.asList(menuReq.getNation().split(","));
		menuMap.put("nation", nationList);
		
		List<String> categoryList = (menuReq.getCategory()==null) ? Arrays.asList("밥류","국 및 탕류","튀김류","구이류", "빵 및 과자류") : Arrays.asList(menuReq.getCategory().split(","));
		menuMap.put("category", categoryList);
		
		List<String> keywordList = (menuReq.getKeyword()==null) ? Arrays.asList("가벼운","든든한","술과 어울리는") : Arrays.asList(menuReq.getKeyword().split(","));
		menuMap.put("keyword", keywordList);
		
		List<String> soupList = (menuReq.getSoup()==null) ? Arrays.asList("true","false") : Arrays.asList(menuReq.getSoup().split(","));
		menuMap.put("soup", soupList);
		
		return ResponseEntity.status(201).body(menuService.recommend(menuMap));
	}
}
