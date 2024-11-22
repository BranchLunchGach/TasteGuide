
package com.web.spring.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.spring.dto.MenuReq;
import com.web.spring.service.MenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MenuController {
	private final MenuService menuService;
	
	@PostMapping("/menu")
	public ResponseEntity<?> recommand(@RequestBody MenuReq menuReq){
		Map<String, List<String>> menuMap = new HashMap<>();
		
		List<String> nationList = (menuReq.getNation()=="") ? Arrays.asList("양식","한식","중식","일식", "아시안") : Arrays.asList(menuReq.getNation().split(","));
		menuMap.put("nation", nationList);
		
		List<String> categoryList = (menuReq.getCategory()=="") ? Arrays.asList("튀김류","조림 및 찜류","찌개 및 전골류","부침류", "무침 및 절임류", "빵류", "볶음류", "밥류", "면류", "국 및 탕류", "구이류") : Arrays.asList(menuReq.getCategory().split(","));
		menuMap.put("category", categoryList);
		
		List<String> keywordList = (menuReq.getKeyword()=="") ? Arrays.asList("가벼운","든든한","술과 어울리는") : Arrays.asList(menuReq.getKeyword().split(","));
		menuMap.put("keyword", keywordList);
		
		List<String> soupList = (menuReq.getSoup()=="") ? Arrays.asList("있음","없음") : Arrays.asList(menuReq.getSoup().split(","));
		menuMap.put("soup", soupList);
		
		List<String> selectNameList = (menuReq.getSelectName()==null) ? Arrays.asList("") : Arrays.asList(menuReq.getSelectName().split(","));
		menuMap.put("selectName", selectNameList);
		
		List<String> weatherList = Arrays.asList(menuReq.getWeather());
		menuMap.put("weather", weatherList);
		
		return ResponseEntity.status(201).body(menuService.recommend(menuMap));
	}
}
