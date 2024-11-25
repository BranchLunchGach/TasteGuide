package com.web.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.spring.api.Crawler;
import com.web.spring.api.MapAPI;
import com.web.spring.entity.Restaurant;

import io.jsonwebtoken.lang.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j 	
@Service
public class RestaurantService {
	
	@Autowired
	private MapAPI mapAPI = new MapAPI();
	
	// ======================= 식당 3개 추천 기능 =======================
	public Queue<Restaurant> restaurantRecommend(String menu, String coreKeywords, String mainKeywords, String startX, String startY) {
		
		log.info("[RestaurantService] restaurantRecommend() 시작!!");
		
		Queue<Restaurant> pq = new PriorityQueue<>();
		
		Crawler crawler = new Crawler(10);
		
		List<String> lists = crawler.reviewCrawling(menu); //String 하나가 가게 하나의 모든 정보..	
		
		// 크롤링한 데이터로 Restaurant 객체 생성
		for (String data : lists) {
			String[] datas = data.split("}");
			
			for(String d : datas) 
				System.out.println(d);
			
			log.info("[RestaurantService] restaurantRecommend() : {} 데이터 가공 시작", datas[0]);
						
			boolean dayOff = false;
			int score = 0;
			List<String> menus = new ArrayList<>();
			List<String> keywordReviews = new ArrayList<>();
			List<Integer> keywordReviewCnts = new ArrayList<>();
			List<String> textReviews = new ArrayList<>();
			int keywordReviewCnt = 0; //키워드 전체 리뷰 개수
			
			//메뉴 5가지 저장
			for (int i = 5; i <= 9; i++)
				menus.add(datas[i]);
			
			log.info("[RestaurantService] restaurantRecommend() : {} 키워드 리뷰 가공중...", datas[0]);
			
			//키워드 리뷰 10개 13~22
			if (!datas[13].equals("0")) {
				for (int i = 13; i<=22; i++) {
					try {
						String[] str = datas[i].replaceAll("\"", "").replaceAll(" ", "").split(",");
						keywordReviews.add(str[0]);
						keywordReviewCnts.add(Integer.parseInt(str[1])); //해당 키워드를 입력한 사람의 수를 대입
					} catch(ArrayIndexOutOfBoundsException e) {
						keywordReviews.add("0");
					}
				}
			} else {
				for (int i = 13; i<=22; i++) {
					keywordReviews.add("0");
				}
			}
			
			for (int i = 23; i <= 27; i++) {
				textReviews.add(datas[i]);
			}
			
			//키워드 리뷰 전체 갯수 구하기.
			for (Integer cnt : keywordReviewCnts)
				keywordReviewCnt += cnt;		
			
			log.info("[RestaurantService] restaurantRecommend() : {} 가중치 로직 시작...", datas[0]);
			
			//가중치 로직 시작...
			if(datas[2].equals("오늘 휴무")) {
				dayOff = true;
				score -= 4000000;
			}
			
			//리뷰 개수에 따라 가중치 부여 
			int reviewSum = Integer.parseInt(datas[11]) + Integer.parseInt(datas[12]);
			if (reviewSum >= 5000) 
				score += 20;
			else if (reviewSum >= 1000)
				score += 15;
			else if (reviewSum >= 500) 
				score += 12;
			else if (reviewSum >= 100)
				score += 7;
			else score += 5;
			
			
			//가능하면 좋겠어요!! 가중치 부여
			if(coreKeywords != null) {
				List<String> coreKeyword = Arrays.asList(coreKeywords.replaceAll(" ", "").split(","));
				
				for(String k : coreKeyword)
					if(datas[29].replaceAll(" ", "").contains(k)) score += 500;
			}
			
			//
			if(keywordReviews.contains("매장이청결해요")) {
				score += reviewScore("매장이청결해요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
			}
			
			//mainKeyword 가중치 부여
			if(mainKeywords != null) {
				List<String> mainKeyword = Arrays.asList(mainKeywords.replaceAll(" ", "").split(","));
				for(String k : mainKeyword) {

					switch(k) {
						case "가족모임" : 
							if(keywordReviews.contains("단체모임하기좋아요"))
								score += reviewScore("단체모임하기좋아요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
							
							break;
						
						case "단체" :
							if(keywordReviews.contains("단체모임하기좋아요"))
								score += reviewScore("단체모임하기좋아요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
							
							if(keywordReviews.contains("매장이넓어요"))
								score += reviewScore("매장이넓어요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
							
							break;
							
						case "기념일" :
							if(keywordReviews.contains("특별한날가기좋아요"))
								score += reviewScore("특별한날가기좋아요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
							
							if(keywordReviews.contains("뷰가좋아요"))
								score += reviewScore("뷰가좋아요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
							
							break;
							
						case "가성비" :
							if(keywordReviews.contains("가성비가좋아요"))
								score += reviewScore("가성비가좋아요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
							
							break;
						
						case "혼밥" :
							if(keywordReviews.contains("혼밥하기좋아요"))
								score += reviewScore("혼밥하기좋아요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
							
							break;
							
						case "양많음" :
							if(keywordReviews.contains("양이많아요"))
								score += reviewScore("양이많아요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
							
							break;
							
						case "현지맛" :
							if(keywordReviews.contains("현지맛에가까워요"))
								score += reviewScore("현지맛에가까워요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
							
							break;
					}
				}
			}
			
			log.info("[RestaurantService] restaurantRecommend() : {} 메인 키워드 가중치 끝 & 거리 가중치 시작...", datas[0]);
			
//			거리에 따른 가중치 부여
			String address = mapAPI.getGeocode(datas[3]);
			String endX = address.split(",")[0];
			String endY = address.split(",")[1];
			
			System.out.println("startX >> " + startX);
			System.out.println("startY >> " + startY);
			System.out.println("endX >> " + endX);
			System.out.println("endY >> " + endY);
			String strDistance = mapAPI.getLinearDistance(startX, startY, endX, endY);
			int distance = Integer.parseInt(strDistance);
			
			if (distance <= 300) {
				score += 20;
			} else if (distance <= 600) {
				score += 10;
			} else if (distance <= 1000) {
				score += 5;
			} else if (distance <= 2000) {
				score += 1;
			} else if (distance > 2000) {
				score -= 20;
			}
			
			//가중치 로직 끝...
			log.info("[RestaurantService] restaurantRecommend() : {} 데이터 가공 끝 & Restaurant 객체 생성 후 Queue.add()", datas[0]);
			Restaurant restaurant = new Restaurant(datas[0], datas[1], dayOff, datas[3], datas[4], menus, datas[10], datas[11], datas[12], keywordReviews, textReviews, datas[28], datas[29], datas[30], 0, score);
			
			pq.add(restaurant);
		}	
		
		crawler.close();
		
		return pq;
	} // ======================= 식당 3개 추천 기능 =======================
	
	// ======================= 만남의 장소 식당 3개 추천 기능 =======================
		public Queue<Restaurant> helloRecommend(String menu, String avgX, String avgY) {
			
			log.info("[RestaurantService] helloRecommend() 시작!!");
			
			Queue<Restaurant> pq = new PriorityQueue<>();

			Crawler crawler = new Crawler(10);
			List<String> lists = crawler.hello(menu, avgX, avgY); //String 하나가 가게 하나의 모든 정보..
						
			// 크롤링한 데이터로 Restaurant 객체 생성
			for (String data : lists) {			
				
				String[] datas = data.split("}");
				
				log.info("[RestaurantService] helloRecommend() : {} 데이터 가공 시작", datas[0]);
				
				boolean dayOff = false;
				int score = 0;
				List<String> menus = new ArrayList<>();
				List<String> keywordReviews = new ArrayList<>();
				List<Integer> keywordReviewCnts = new ArrayList<>();
				List<String> textReviews = new ArrayList<>();
				
				//메뉴 5가지 저장
				for (int i = 5; i <= 9; i++)
					menus.add(datas[i]);		
				
				log.info("[RestaurantService] helloRecommend() : {} 키워드 리뷰 가공중...", datas[0]);
				
				//키워드 리뷰 10개 저장 (13~22)
				if (!datas[13].equals("0")) {
					for (int i = 13; i<=22; i++) {
						String[] str = datas[i].replaceAll("\"", "").replaceAll(" ", "").split(",");
						keywordReviews.add(str[0]);
						keywordReviewCnts.add(Integer.parseInt(str[1])); //해당 키워드를 입력한 사람의 수를 대입
					}
				} else {
					for (int i = 13; i<=22; i++) {
						keywordReviews.add("0");
					}
				}
				
				for (int i = 23; i <= 27; i++) {
					textReviews.add(datas[i]);
				}
				
				log.info("[RestaurantService] helloRecommend() : {} 가중치 로직 시작...", datas[0]);
				
				//가중치 로직 시작...
				if(datas[2].equals("오늘 휴무")) {
					dayOff = true;
					score -= 4000000;
				}
				
				int totalReviewCnt = Integer.parseInt(datas[11]) + Integer.parseInt(datas[12]);
				score += totalReviewCnt;
				
				//거리 구하기
				String address = mapAPI.getGeocode(datas[3]);
				String endX = address.split(",")[0];
				String endY = address.split(",")[1];
				String strDistance = mapAPI.getLinearDistance(avgX, avgY, endX, endY);
				int distance = Integer.parseInt(strDistance);
				
				log.info("[RestaurantService] helloRecommend() : {} 가중치 로직 끝 & Restaurant 객체 생성 후 Queue.add()", datas[0]);

				Restaurant restaurant = new Restaurant(datas[0], datas[1], dayOff, datas[3], datas[4], menus, datas[10], datas[11], datas[12], keywordReviews, textReviews, datas[28], datas[29], datas[30], distance, score);
				
				pq.add(restaurant);
			}
						
			crawler.close();
			
			return pq;
		} // ======================= 만남의장소 식당 3개 추천 기능 =======================
		
		// ======================= Ai 추천 식당 3개 추천 기능 =======================
		// 0:매장 이름 } 1:오늘 휴무 } 2:영업 시간 } 3~7:메뉴명 5개 } 8 : 점수
		public Queue<Restaurant> aiRecommend(String menu) {
			
			log.info("[RestaurantService] aiRecommend 실행 중...");
			
			Queue<Restaurant> pq = new PriorityQueue<>();

			Crawler crawler = new Crawler(6);
			List<String> lists = crawler.aiRecommend(menu); //String 하나가 가게 하나의 모든 정보..
						
			// 크롤링한 데이터로 Restaurant 객체 생성
			for (String data : lists) {
				
				String[] datas = data.split("}");
				
				int c = 1;
				for (String d : datas) {
					log.info("[{} 번째 DATA] {}", c, d);
					c++;
				}
				
				boolean dayOff = false;
				int score = 0;
				List<String> menus = new ArrayList<>();
				
				//메뉴 5가지 저장
				for (int i = 3; i <= 7; i++)
					menus.add(datas[i]);
				
				if(datas[1].equals("오늘 휴무")) {
					dayOff = true;
					score -= 4000000;
				} else score = 0;

				//레스토랑 객체 생성
				Restaurant restaurant = new Restaurant(datas[0], datas[2], dayOff, menus, score);
					
				pq.add(restaurant);
			}
			
			log.info("[RestaurantController] aiRecommend 실행 끝 ...");
						
			crawler.close();
			
			return pq;
		} 
	
	private int reviewScore(String keyword, List<String> keywordReviews, List<Integer> keywordReviewCnts, int keywordReviewCnt) {
		int index = keywordReviews.indexOf(keyword);
		int reviewCnt = keywordReviewCnts.get(index);
		
		//double cnt = 3000/300 * 0.01; -> 전체키워드/선택키워드 * 0.01
		double tmp = keywordReviewCnt/reviewCnt * 0.01 + 1;
		int sum = (int) (tmp * 10); 
		
		return sum;
	}
}
