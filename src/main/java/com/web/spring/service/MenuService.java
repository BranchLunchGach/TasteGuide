package com.web.spring.service;

import java.util.HashMap;
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
		/*
		 1. (1차 필터링) DB의 menu 테이블에서 카테고리와 국가에 해당하는 메뉴만 1차적으로 걸러서 가져옴 (카테고리와 국가가 모두 일치하는 메뉴만)
		 			  만약 1차필터링에서 가져온 메뉴가 1개 이하일때는 디비에서 카테고리가 일치하는 메뉴 + 국가가 일치하는 메뉴 다 가져옴
		*/
		
		/*
		 2. 가져온 메뉴들을 map에 저장 Value : 가중치
			ex) (<"파스타",0>,<"국밥",0>....)
		*/
		Map<Menu, Integer> aa = new HashMap<>();
		
		//3. (2차 필터링) DB의 choice 테이블에서 사용자가 최근 2일동안 추천받은 메뉴들을 가져와서 map에서 제거
		
		//4. 각 메뉴들의 이유를 저장해두는 map 생성
		Map<String, Stack<String>> reason = new HashMap<>();
		//3차 필터링은 추후에 논문 참고하여 일치했을때 가중치 5~7점으로 세분화
		/*
		 5. (3차 필터링-1) 현재 날씨(기상청 API 사용)를 가져와서 메뉴의 날씨와 비교 - 일치하면 가중치+6, 전체일경우 가중치+4
		 				 가중치가 추가되면 이유 map에 날씨에 대한 이유 추가
		*/
		
		/*
		 6. (3차 필터링-2) 현재 계절(JAVA Calender 객체)을 가져와서 메뉴의 계절과 비교 - 일치하면 가중치+6, 전체일경우 가중치+4
		 				 가중치가 추가되면 이유 map에 계절에 대한 이유 추가
		 				 3~5월 봄, 6~8월 여름, 9~11월 가을, 12~2월 겨울
		 */
		
		/*
		 7. (3차 필터링-3) 현재 요일(JAVA Calender 객체)을 가져와서 메뉴의 요일과 비교 - 일치하면 가중치+6, 전체일경우 가중치+4
		 				 가중치가 추가되면 이유 map에 요일에 대한 이유 추가
		 				 토~일 휴일, 월~금 평일
		 */
		
		/*
		 8. (3차 필터링-3) 현재 시간(JAVA Calender 객체)을 가져와서 메뉴의 시간과 비교 - 일치하면 가중치+6, 전체일경우 가중치+4
		 				 가중치가 추가되면 이유 map에 시간에 대한 이유 추가
		 				 7~10시 아침, 10~15시 점심, 17~21시 저녁, 나머지 시간은 기타
		 */
		
		/*
		 9. (4차 필터링) 국물유무에 따라 가중치 부여 - 일치하면 가중치+11
		 			   가중치가 추가되면 이유 map에 국물유무에 대한 이유 추가
		 */

		/*
		 10. (5차 필터링) 키워드 따라 가중치 부여 - 일치하면 가중치+11
		 			   가중치가 추가되면 이유 map에 키워드에 대한 이유 추가
		 */
		
		//11. 메뉴 map을 가중치에 따라 정렬
		
		/*
		 12. map에서 제일 가중치가 높은 메뉴 2개 뽑아와서 choice 테이블에 추가
		 	 만약 menuName이 비슷하다면 2번째 추천에서 제외
		 */

		//13. 제일 가중치가 높은 메뉴 2개 반환
		return null;
	}
}
