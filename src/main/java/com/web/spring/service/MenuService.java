package com.web.spring.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.web.spring.dto.MenuRes;
import com.web.spring.entity.Menu;
import com.web.spring.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
	
	private final MenuRepository menuRepository;
	
	public List<MenuRes> nugury(Map<String, List<String>> menuMapp) {
		//대분류, 국가와 관련된 음식만 가져오기
		List<Menu> menus = menuRepository.findByNationInAndMajorCategorieIn(menuMapp.get("nation"), menuMapp.get("majorCategory")).orElseThrow();
		Map<Menu, Integer> menuMap = new HashMap<>();
		//현재 요일 구해오기
		Calendar cal = Calendar.getInstance();
		int date = cal.get(Calendar.DAY_OF_WEEK); // 1 일요일, 7 토요일
		String holiday = (date==1||date==7) ? "휴일" : "평일";

		//가중치 계산
		for(Menu menu : menus) {
			int gajungchi = 0;
			// 사용자가 가벼운, 크리미를 원함
			for(String concept : menuMapp.get("concept")) {
				//메뉴의 컨셉에 가벼운 or 크리미 포함되어있으면 가중치 +5
				if(menu.getConcept().contains(concept)) gajungchi += 5;
			}
			
			//날짜가 일치하면 가중치
			if(menu.getHoliday().equals(holiday)) gajungchi += 3;
			else if(menu.getHoliday().equals("전체")) gajungchi++;
			
			//[<'파슽타',0>,<'햄버거',0>,<'피자',0>]
			menuMap.put(menu, gajungchi);
		}
		
		//정렬
		List<Map.Entry<Menu, Integer>> menuList = new LinkedList<>(menuMap.entrySet());
		menuList.sort(((o1, o2) -> menuMap.get(o2.getKey()) - menuMap.get(o1.getKey())));
		
		menuList.forEach((menu)-> System.out.println(menu.getKey().getSubCategorie()));
		
		List<MenuRes> menuResList = new ArrayList<>();
		Menu menu = menuList.get(0).getKey();
		
		List<String> reason = new ArrayList<>();
		reason.add("안녕하세요");
		reason.add("반갑습니다");
		reason.add("좋습니다");
		menuResList.add(new MenuRes(menu, reason));
		boolean fin = false;
		int index = 1;
		while(!fin) {
			Menu secondMenu = menuList.get(index).getKey();
			if(!secondMenu.getMiddleCategorie().equals(menu.getMiddleCategorie())) {
				menuResList.add(new MenuRes(secondMenu, reason));
				fin = true;
			}
			index++;
		}
		
		return menuResList;
	}
}
