package com.web.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class TasteGuideApplication {
	/*
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private ChoiceRepository choiceRepository;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		LocalDateTime nowDateTime = LocalDate.now().atStartOfDay();
		LocalDateTime twoDay = nowDateTime.minusDays(2);
		System.out.println(twoDay);
		choiceRepository.findChoice(twoDay, 1).orElseThrow().forEach((choice)->System.out.println(choice));
	}
*/
	
	public static void main(String[] args) {
		SpringApplication.run(TasteGuideApplication.class, args);
		
	}
	
//	// 크롤링 사용 예제
//	public static void main(String[] args) {
//		
//		Scanner sc = new Scanner(System.in);
//		System.out.print(">> 메뉴를 입력하세요 : ");
//		String menu = sc.nextLine();
//				
//		RestaurantService service = new RestaurantService();
//		String coreKeywords = "주차 가능, 반려 동물 동반, 노키즈존";
//		String mainKeywords = "기념일, 가족모임, 양많음";
//		
//		Queue<Restaurant> restauransts = service.restaurantRecommend(menu, coreKeywords, mainKeywords);
//		
//		System.out.println("\n ============================ 매장 추천 ============================ ");
//		System.out.println(">>>> 첫 번째 레스토랑 추천");
//		System.out.println(restauransts.poll());
//		
//		System.out.println(">>>> 두 번째 레스토랑 추천");
//		System.out.println(restauransts.poll());
//		
//		System.out.println(">>>> 세 번째 레스토랑 추천");
//		System.out.println(restauransts.poll());
//		
//		System.out.println("\n ============================ 매장 재추천 ============================ ");
//		System.out.println(">>>> [재추천] 첫 번째 레스토랑 추천");
//		System.out.println(restauransts.poll());
//		
//		System.out.println(">>>> [재추천] 두 번째 레스토랑 추천");
//		System.out.println(restauransts.poll());
//		
//		System.out.println(">>>> [재추천] 세 번째 레스토랑 추천");
//		System.out.println(restauransts.poll());
//		
//		
//	}

}
