package com.web.spring.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.springframework.stereotype.Service;

import com.web.spring.dto.MenuRes;
import com.web.spring.entity.Menu;
import com.web.spring.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
	
	private final MenuRepository menuRepository;
	
	public List<MenuRes> nugury(Map<String, List<String>> menuList) {
		//대분류, 국가와 관련된 음식만 가져오기
		List<Menu> menus = menuRepository.findByNationInAndMajorCategorieIn(menuList.get("nation"), menuList.get("category")).orElseThrow();
		Map<Menu, Integer> menuMap = new HashMap<>();
		Map<String, Stack<String>> menuReason = new HashMap<>();
		//현재 요일 구해오기
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int date = cal.get(Calendar.DAY_OF_WEEK); // 1 일요일, 7 토요일
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		
		String season = "";
		switch (month) {
			case 3: case 4: case 5: {
				season = "봄";
			}
			case 6: case 7: case 8: {
				season = "여름";
			}
			case 9: case 10: case 11: {
				season = "가을";
			}
			case 1: case 2: case 12: {
				season = "겨울";
			}
		}
		
		String time = "";
		switch (hour) {
			case 7: case 8: case 9: case 10: {
				time = "아침";
			}
			case 11: case 12: case 13: case 14: case 15: {
				time = "점심";
			}
			case 17: case 18: case 19: case 20: case 21: {
				time = "저녁";
			}
		}
		
		String holiday = (date==1||date==7) ? "휴일" : "평일";

		//가중치 계산
		for(Menu menu : menus) {
			menuReason.put(menu.getMenuName(), new Stack<>());
			int gajungchi = 0;

			//계절이 일치하면 가중치
			if(menu.getSeason().equals(season)) {
				gajungchi += 6;
				menuReason.get(menu.getMenuName()).push(season);
			} else if(menu.getSeason().equals("전체")) {
				gajungchi += 4;
				menuReason.get(menu.getMenuName()).push(season);
			}

			//요일이 일치하면 가중치
			if(menu.getHoliday().equals(holiday)) {
				gajungchi += 6;
				menuReason.get(menu.getMenuName()).push(holiday);
			} else if(menu.getHoliday().equals("전체")) {
				gajungchi += 4;
				menuReason.get(menu.getMenuName()).push(holiday);
			}
			
			//시간이 일치하면 가중치
			if(menu.getTime().equals(time)) {
				gajungchi += 6;
				menuReason.get(menu.getMenuName()).push(time);
			} else if(menu.getTime().equals("전체")) {
				gajungchi += 4;
				menuReason.get(menu.getMenuName()).push(time);
			}
			
			switch (menuList.get("soup").get(0)) {
				case "true": {				
					if(menu.isSoup()) {
						gajungchi += 11;
						menuReason.get(menu.getMenuName()).push("soup");
					}
				}
				case "false": {				
					if(!menu.isSoup()) {
						gajungchi += 11;
						menuReason.get(menu.getMenuName()).push("soup");
					}
				}
			}
			
			// 사용자가 가벼운, 크리미를 원함
			for(String keyword : menuList.get("keyword")) {
				//메뉴의 컨셉에 가벼운 or 크리미 포함되어있으면 가중치 +11
				if(menu.getKeyword().contains(keyword)) gajungchi += 11;
				menuReason.get(menu.getMenuName()).push(keyword);
			}
			
			
			//[<'파슽타',0>,<'햄버거',0>,<'피자',0>]
			menuMap.put(menu, gajungchi);
		}
		
		//정렬
		List<Map.Entry<Menu, Integer>> menuResult = new LinkedList<>(menuMap.entrySet());
		menuResult.sort(((o1, o2) -> menuMap.get(o2.getKey()) - menuMap.get(o1.getKey())));
		
		menuResult.forEach((menu)-> System.out.println(menu.getKey().getMenuName()));
		
		List<MenuRes> menuResList = new ArrayList<>();
		Menu menu = menuResult.get(0).getKey();
		
		menuResList.add(new MenuRes(menu, menuReason.get(menu.getMenuName())));
		boolean fin = false;
		int index = 1;
		while(!fin) {
			Menu secondMenu = menuResult.get(index).getKey();
			if(!secondMenu.getMenuName().equals(menu.getMenuName())) {
				menuResList.add(new MenuRes(secondMenu, menuReason.get(menu.getMenuName())));
				fin = true;
			}
			index++;
		}
		
		return menuResList;
	}
}
