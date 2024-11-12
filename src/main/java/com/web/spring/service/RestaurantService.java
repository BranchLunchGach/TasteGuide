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

@Service
public class RestaurantService {
	
	@Autowired
	private MapAPI mapAPI = new MapAPI();
	
	// ======================= 식당 3개 추천 기능 =======================
	public Queue<Restaurant> restaurantRecommend(String menu, String coreKeywords, String mainKeywords) {
		
		Queue<Restaurant> pq = new PriorityQueue<>();
		String startX = "126.9842915";
		String startY = "37.5697105";
		
		// Headless
		long startTime = System.currentTimeMillis();
		Crawler crawler = new Crawler(10);
		List<String> lists = crawler.reviewCrawling(menu); //String 하나가 가게 하나의 모든 정보..
		
		System.out.println("크롤링 데이터 사이즈 : " + lists.size());
		
		// 크롤링한 데이터로 Restaurant 객체 생성
		for (String data : lists) {
			System.out.println("이건되나..?");
			String[] datas = data.split("}");
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>> [가중치 계산중...] <<<<<<<<<<<<<<<<<<<<<\n " + data);			
			
			boolean dayOff = false;
			int score = 0;
			List<String> menus = new ArrayList<>();
			List<String> keywordReviews = new ArrayList<>();
			List<Integer> keywordReviewCnts = new ArrayList<>();
			int keywordReviewCnt = 0; //키워드 전체 리뷰 개수
			
			//메뉴 5가지 저장
			for (int i = 5; i <= 9; i++)
				menus.add(datas[i]);
			
			//키워드 리뷰 10개 13~22
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
			
			// test 로그
			for (String k : keywordReviews)
				System.out.println(k);
			
			//키워드 리뷰 전체 갯수 구하기.
			for (Integer cnt : keywordReviewCnts)
				keywordReviewCnt += cnt;		
			
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
					if(datas[24].replaceAll(" ", "").contains(k)) score += 500;
			}
			
			//
			System.out.println("매장이 청결해요 여부: " +  keywordReviews.contains("매장이청결해요"));
			if(keywordReviews.contains("매장이청결해요")) {
				System.out.println("매장이 청결해요 !! 점수는 ?  :: " + reviewScore("매장이청결해요", keywordReviews, keywordReviewCnts, keywordReviewCnt));
				score += reviewScore("매장이청결해요", keywordReviews, keywordReviewCnts, keywordReviewCnt);
			}
			
			//mainKeyword 가중치 부여
			if(mainKeywords != null) {
				System.out.println("mainKeyword 가중치 부여 시작");
				List<String> mainKeyword = Arrays.asList(mainKeywords.replaceAll(" ", "").split(","));
				//List<String> keywordReviews 여기서 확인... //List<Integer> keywordReviewCnt 값으로 전체 키워드수 확인
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
			
			
			//거리에 따른 가중치 부여
			String address = mapAPI.getGeocode(datas[3]);
			String endX = address.split(",")[0];
			String endY = address.split(",")[1];
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

			Restaurant restaurant = new Restaurant(datas[0], datas[1], dayOff, datas[3], datas[4], menus, datas[10], datas[11], datas[12], keywordReviews, datas[23], datas[24], distance, score);
			
			pq.add(restaurant);
		}
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("크롤링 걸린 시간 + 데이터 가공 : " + (endTime-startTime) + "ms");
		
		crawler.close();
		
		return pq;
	} // ======================= 식당 3개 추천 기능 =======================
	
	// ======================= 만남의 장소 식당 3개 추천 기능 =======================
		public Queue<Restaurant> helloRecommend(String menu, String avgX, String avgY) {
			
			Queue<Restaurant> pq = new PriorityQueue<>();

			Crawler crawler = new Crawler(10);
			List<String> lists = crawler.hello(menu, avgX, avgY); //String 하나가 가게 하나의 모든 정보..
						
			// 크롤링한 데이터로 Restaurant 객체 생성
			for (String data : lists) {
				
				System.out.println(data);
				
				String[] datas = data.split("}");
				System.out.println("split 지났씁니다~");				
				
				boolean dayOff = false;
				int score = 0;
				List<String> menus = new ArrayList<>();
				List<String> keywordReviews = new ArrayList<>();
				List<Integer> keywordReviewCnts = new ArrayList<>();
				
				//메뉴 5가지 저장
				for (int i = 5; i <= 9; i++)
					menus.add(datas[i]);
				System.out.println("메뉴 저장 지났씁니다~");			
				
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
				System.out.println("키워드 리뷰 저장 지났씁니다~");
				
				//가중치 로직 시작...
				if(datas[2].equals("오늘 휴무")) {
					dayOff = true;
					score -= 4000000;
				}
				
				System.out.println("리뷰 개수 구하기 전!");
				int totalReviewCnt = Integer.parseInt(datas[11]) + Integer.parseInt(datas[12]);
				score += totalReviewCnt;

				Restaurant restaurant = new Restaurant(datas[0], datas[1], dayOff, datas[3], datas[4], menus, datas[10], datas[11], datas[12], keywordReviews, datas[23], datas[24], 0, score);
				
				pq.add(restaurant);
			}
						
			crawler.close();
			
			return pq;
		} // ======================= 만남의장소 식당 3개 추천 기능 =======================
	
	
	private int reviewScore(String keyword, List<String> keywordReviews, List<Integer> keywordReviewCnts, int keywordReviewCnt) {
		int index = keywordReviews.indexOf(keyword);
		int reviewCnt = keywordReviewCnts.get(index);
		
		//double cnt = 3000/300 * 0.01; -> 전체키워드/선택키워드 * 0.01
		double tmp = keywordReviewCnt/reviewCnt * 0.01 + 1;
		int sum = (int) (tmp * 10); 
		
		return sum;
	}
}
