package com.web.spring.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import org.springframework.stereotype.Service;

import com.web.spring.dto.MenuRes;
import com.web.spring.entity.Menu;
import com.web.spring.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceExample {
	
	private final MenuRepository menuRepository;
	
	public List<MenuRes> nugury(Map<String, List<String>> menuList) {
		//대분류, 국가와 관련된 메뉴만 가져오기
		List<Menu> menus = menuRepository.findByNationInAndCategoryIn(menuList.get("nation"), menuList.get("category")).orElseThrow();
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
				break;
			}
			case 6: case 7: case 8: {
				season = "여름";
				break;
			}
			case 9: case 10: case 11: {
				season = "가을";
				break;
			}
			case 1: case 2: case 12: {
				season = "겨울";
				break;
			}
		}
		
		String time = "";
		switch (hour) {
			case 7: case 8: case 9: case 10: {
				time = "아침";
				break;
			}
			case 11: case 12: case 13: case 14: case 15: {
				time = "점심";
				break;
			}
			case 17: case 18: case 19: case 20: case 21: {
				time = "저녁";
				break;
			}
		}
		
		String holiday = (date==1||date==7) ? "휴일" : "평일";

		//가중치 계산
		for(Menu menu : menus) {
			Stack<String> reasonStack = new Stack<>();
			if(menuList.get("nation").contains(menu.getNation())) reasonStack.push(menu.getNation() + "에 해당하는 메뉴입니다.");
			if(menuList.get("category").contains(menu.getCategory())) reasonStack.push(menu.getCategory() + "에 해당하는 메뉴입니다.");
			int gajungchi = 0;

			//계절이 일치하면 가중치
			if(menu.getSeason().equals(season)) {
				gajungchi += 6;
				reasonStack.push(season + "에 어울리는 메뉴입니다.");
			} else if(menu.getSeason().equals("전체")) {
				gajungchi += 4;
				reasonStack.push(season + "에 어울리는 메뉴입니다.");
			}

			//요일이 일치하면 가중치
			if(menu.getHoliday().equals(holiday)) {
				gajungchi += 6;
				reasonStack.push(holiday + "에 어울리는 메뉴입니다.");
			} else if(menu.getHoliday().equals("전체")) {
				gajungchi += 4;
				reasonStack.push(holiday + "에 어울리는 메뉴입니다.");
			}
			
			//시간이 일치하면 가중치
			if(menu.getTime().equals(time)) {
				gajungchi += 6;
				reasonStack.push(time + "에 어울리는 메뉴입니다.");
			} else if(menu.getTime().equals("전체")) {
				gajungchi += 4;
				reasonStack.push(time + "에 어울리는 메뉴입니다.");
			}
			for(int i = 0; i < menuList.get("soup").size(); i++) {
				switch (menuList.get("soup").get(i)) {
					case "true": {				
						if(menu.isSoup()) {
							gajungchi += 11;
							reasonStack.push("국물이 있는 메뉴입니다.");
						}
						break;
					}
					case "false": {				
						if(!menu.isSoup()) {
							gajungchi += 11;
							reasonStack.push("국물이 없는 메뉴입니다.");
						}
						break;
					}
				}				
			}
			
			// 사용자가 가벼운, 크리미를 원함
			for(String keyword : menuList.get("keyword")) {
				//메뉴의 컨셉에 가벼운 or 크리미 포함되어있으면 가중치 +11
				if(menu.getKeyword().contains(keyword)) {
					gajungchi += 11;
					reasonStack.push(keyword + " 메뉴입니다.");
				}
			}
			
			
			//[<'파슽타',0>,<'햄버거',0>,<'피자',0>]
			menuMap.put(menu, gajungchi);
			menuReason.put(menu.getMenuName(), reasonStack);
		}
		
		//정렬
		List<Map.Entry<Menu, Integer>> menuResult = new LinkedList<>(menuMap.entrySet());
		menuResult.sort(((o1, o2) -> {
			int valueComparison = Integer.compare(menuMap.get(o2.getKey()), menuMap.get(o1.getKey()));
	        if (valueComparison != 0) {
	            return valueComparison;  // Integer 내림차순
	        } else {
	            return new Random().nextInt(2) * 2 - 1;  // 같은 값일 경우 랜덤 정렬
	        }
		}));
		menuResult.forEach((menu)->{
			System.out.println(menu.getKey().getMenuName());
			System.out.println(menu.getValue());
			System.out.println("----------------------------------");
		});
		
		List<MenuRes> menuResList = new ArrayList<>();
		Menu menu = menuResult.get(0).getKey();
		List<String> menuReasonList = new ArrayList<>();
		for(int i = 0; i<3; i++) {
			if(menuReason.get(menu.getMenuName()).isEmpty()) break;
			menuReasonList.add(menuReason.get(menu.getMenuName()).pop());
		}
		menuResList.add(new MenuRes(menu, menuReasonList, holiday, holiday, holiday, holiday));
		boolean fin = false;
		int index = 1;
		while(!fin) {
			Menu secondMenu = menuResult.get(index).getKey();
			if(!secondMenu.getMenuName().equals(menu.getMenuName())) {
				menuReasonList = new ArrayList<>();
				for(int i = 0; i<3; i++) {
					if(menuReason.get(secondMenu.getMenuName()).isEmpty()) break;
					menuReasonList.add(menuReason.get(secondMenu.getMenuName()).pop());
				}
				menuResList.add(new MenuRes(secondMenu, menuReasonList, holiday, holiday, holiday, holiday));
				fin = true;
			}
			index++;
		}
		
		return menuResList;
	}
}
