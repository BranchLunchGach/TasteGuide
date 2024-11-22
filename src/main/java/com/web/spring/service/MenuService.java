package com.web.spring.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.web.spring.api.Crawler;
import com.web.spring.dto.MenuRes;
import com.web.spring.entity.Choice;
import com.web.spring.entity.Menu;
import com.web.spring.entity.Restaurant;
import com.web.spring.repository.ChoiceRepository;
import com.web.spring.repository.MenuRepository;
import com.web.spring.security.CustomUserDetails;

import io.jsonwebtoken.lang.Arrays;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
	
	private final MenuRepository menuRepository;
	private final ChoiceRepository choiceRepository;
	
	public List<MenuRes> recommend(Map<String, List<String>> menuReqList) {
		
		/*
		 1. (1차 필터링) DB의 menu 테이블에서 카테고리와 국가에 해당하는 메뉴만 1차적으로 걸러서 가져옴 (카테고리와 국가가 모두 일치하는 메뉴만)
		 			  만약 1차필터링에서 가져온 메뉴가 1개 이하일때는 디비에서 카테고리가 일치하는 메뉴 + 국가가 일치하는 메뉴 다 가져옴
		*/
		List<Menu> menus = menuRepository.findByNationInAndCategoryIn(menuReqList.get("nation"), menuReqList.get("category")).orElseThrow();
		if(menus.size()<=1) {
			menus = menuRepository.findByNationInOrCategoryIn(menuReqList.get("nation"), menuReqList.get("category")).orElseThrow();
		}
		/*
		 2. 가져온 메뉴들을 map에 저장 Value : 가중치
			ex) (<"파스타",0>,<"국밥",0>....)
		*/
		Map<Menu, Integer> menuMap = new HashMap<>();
		
		//3. (2차 필터링) DB의 choice 테이블에서 사용자가 최근 2일동안 추천받은 메뉴들을 가져와서 map에서 제거
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<Choice> choiceList = new ArrayList<>();
		if(authentication.getName()!=null && !authentication.getName().equals("anonymousUser")) {
			LocalDateTime nowDateTime = LocalDate.now().atStartOfDay();
			LocalDateTime twoDay = nowDateTime.minusDays(2);
			CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
			int userNo = customUserDetails.getUser().getUserNo();
			choiceList = choiceRepository.findChoice(twoDay, userNo).orElseThrow();
		}
		//4. 각 메뉴들의 이유를 저장해두는 map 생성
		Map<String, Stack<String>> menuReason = new HashMap<>();
		//3차 필터링은 추후에 논문 참고하여 일치했을때 가중치 5~7점으로 세분화
		/*
		 5. (3차 필터링-1) 현재 날씨(기상청 API 사용)를 가져와서 메뉴의 날씨와 비교 - 일치하면 가중치+6, 전체일경우 가중치+4
		 				 가중치가 추가되면 이유 map에 날씨에 대한 이유 추가

		 6. (3차 필터링-2) 현재 계절(JAVA Calender 객체)을 가져와서 메뉴의 계절과 비교 - 일치하면 가중치+6, 전체일경우 가중치+4
		 				 가중치가 추가되면 이유 map에 계절에 대한 이유 추가
		 				 3~5월 봄, 6~8월 여름, 9~11월 가을, 12~2월 겨울

		 7. (3차 필터링-3) 현재 요일(JAVA Calender 객체)을 가져와서 메뉴의 요일과 비교 - 일치하면 가중치+6, 전체일경우 가중치+4
		 				 가중치가 추가되면 이유 map에 요일에 대한 이유 추가
		 				 토~일 휴일, 월~금 평일

		 8. (3차 필터링-3) 현재 시간(JAVA Calender 객체)을 가져와서 메뉴의 시간과 비교 - 일치하면 가중치+6, 전체일경우 가중치+4
		 				 가중치가 추가되면 이유 map에 시간에 대한 이유 추가
		 				 7~10시 아침, 10~15시 점심, 17~21시 저녁, 나머지 시간은 기타
		 				 
		 9. (4차 필터링) 국물유무에 따라 가중치 부여 - 일치하면 가중치+11
		 			   가중치가 추가되면 이유 map에 국물유무에 대한 이유 추가
		 			   
		 10. (5차 필터링) 키워드 따라 가중치 부여 - 일치하면 가중치+11
		 			   가중치가 추가되면 이유 map에 키워드에 대한 이유 추가
		 */
	
		
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int date = cal.get(Calendar.DAY_OF_WEEK);
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
		
		for(Menu menu : menus) {
			Stack<String> reasons = new Stack<>();
			if(menuReqList.get("nation").contains(menu.getNation())) reasons.push(menu.getNation() + "에 해당하는 메뉴입니다.");
			if(menuReqList.get("category").contains(menu.getCategory())) reasons.push(menu.getCategory() + "에 해당하는 메뉴입니다.");
			int weight = 0;
			if(!choiceList.isEmpty()) {
				for(Choice c : choiceList) {
					if(menu.equals(c.getMenu())) {
						weight -= 100;
					}
				}
			}
			
			for(String selectName : menuReqList.get("selectName")) {
				if(menu.getMenuName().equals(selectName)) {
					weight -= 100;
				}				
			}
			
			for(int i = 0; i < menuReqList.get("soup").size(); i++) {
				switch (menuReqList.get("soup").get(i)) {
					case "있음": {				
						if(menu.isSoup()) {
							weight += 11;
							reasons.push("국물이 있는 메뉴입니다.");
						}
						break;
					}
					case "없음": {				
						if(!menu.isSoup()) {
							weight += 11;
							reasons.push("국물이 없는 메뉴입니다.");
						}
						break;
					}
				}				
			}
			
			for(String k : menuReqList.get("keyword")) {
				if(menu.getKeyword().contains(k)) {
					weight += 11;
					reasons.push(k + " 메뉴입니다.");
				}
			}
			
			if(menu.getSeason().equals(season)) {
				weight += 6;
				reasons.push(season + "에 어울리는 메뉴입니다.");
			} else if(menu.getSeason().equals("전체")) {
				weight += 4;
				reasons.push(season + "에 어울리는 메뉴입니다.");
			}

			if(menu.getHoliday().equals(holiday)) {
				weight += 6;
				reasons.push(holiday + "에 어울리는 메뉴입니다.");
			} else if(menu.getHoliday().equals("전체")) {
				weight += 4;
				reasons.push(holiday + "에 어울리는 메뉴입니다.");
			}
			
			if(menu.getTime().equals(time)) {
				weight += 6;
				reasons.push(time + "에 어울리는 메뉴입니다.");
			} else if(menu.getTime().equals("전체")) {
				weight += 4;
				reasons.push(time + "에 어울리는 메뉴입니다.");
			}
			
			for(String weather : menuReqList.get("weather")) {
				if(menu.getWeather().equals(weather)) {
					weight += 6;
					reasons.push(weather + "에 어울리는 메뉴입니다.");
				} else if(menu.getTime().equals("전체")) {
					weight += 4;
					reasons.push(weather + "에 어울리는 메뉴입니다.");
				}
			}
			
			menuMap.put(menu, weight);
			menuReason.put(menu.getMenuName(), reasons);
		}
		
		//11. 메뉴 map을 가중치에 따라 정렬
		List<Map.Entry<Menu, Integer>> menuResult = new LinkedList<>(menuMap.entrySet());
		menuResult.sort(((o1, o2) -> {
			int valueComparison = Integer.compare(menuMap.get(o2.getKey()), menuMap.get(o1.getKey()));
	        if (valueComparison != 0) {
	            return valueComparison;  // Integer 내림차순
	        } else {
	            return new Random().nextInt(2) * 2 - 1;  // 같은 값일 경우 랜덤 정렬
	        }
		}));
		/*
		 12. map에서 제일 가중치가 높은 메뉴 2개 뽑아와서 choice 테이블에 추가
		 	 만약 menuName이 비슷하다면 2번째 추천에서 제외
		 */
		String nation = "";
		for(String n : menuReqList.get("nation")) {
			nation += n + ",";
		}
		nation.substring(0, nation.length()-2);
		
		String category = "";
		for(String c : menuReqList.get("category")) {
			category += c + ",";
		}
		category.substring(0, category.length()-2);
		
		String keyword = "";
		for(String k : menuReqList.get("keyword")) {
			keyword += k + ",";
		}
		keyword.substring(0, keyword.length()-2);
		
		String soup = "";
		for(String s : menuReqList.get("soup")) {
			soup += s + ",";
		}
		soup.substring(0, soup.length()-2);
		
		List<MenuRes> menuResList = new ArrayList<>();
		Menu menu = menuResult.get(0).getKey();
		List<String> menuReasonList = new ArrayList<>();
		for(int i = 0; i<3; i++) {
			if(menuReason.get(menu.getMenuName()).isEmpty()) break;
			menuReasonList.add(menuReason.get(menu.getMenuName()).pop());
		}
		menuResList.add(new MenuRes(menu, menuReasonList, nation, category, soup, keyword));
		boolean fin = false;
		int index = 1;
		while(!fin) {
			if(menuResult.size()<=index) {
				Menu secondMenu = menuResult.get(1).getKey();
				menuReasonList = new ArrayList<>();
				for(int i = 0; i<3; i++) {
					if(menuReason.get(secondMenu.getMenuName()).isEmpty()) break;
					menuReasonList.add(menuReason.get(secondMenu.getMenuName()).pop());
				}
				menuResList.add(new MenuRes(secondMenu, menuReasonList, nation, category, soup, keyword));
				break;
			}
			Menu secondMenu = menuResult.get(index).getKey();
			if(!secondMenu.getCategory().equals(menu.getCategory())) {
				menuReasonList = new ArrayList<>();
				for(int i = 0; i<3; i++) {
					if(menuReason.get(secondMenu.getMenuName()).isEmpty()) break;
					menuReasonList.add(menuReason.get(secondMenu.getMenuName()).pop());
				}
				menuResList.add(new MenuRes(secondMenu, menuReasonList, nation, category, soup, keyword));
				fin = true;
			}
			index++;
		}
		
		//13. 제일 가중치가 높은 메뉴 2개 반환
		return menuResList;
	}
}