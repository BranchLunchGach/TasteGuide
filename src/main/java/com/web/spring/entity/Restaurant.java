package com.web.spring.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Restaurant implements Comparable<Restaurant> {
	private String restaurantName; //매장 이름
	private String restaurantType; //매장 타입
	private boolean dayOff; //휴무 유무
	private String address; //매장 도로명 주소
	private String subwayAddress; //매장 근처 지하철역
	private List<String> menus; //대표 메뉴 5가지 -> 이미지URL,메뉴명,가격 (형식)
	private String horoscope; //별점 (존재하지 않을경우 0으로 부여)
	private String visitorReviewCnt; //방문자 리뷰 개수
	private String blogReviewCnt; //블로그 리뷰 개수
	private List<String> keywordReviews; //키워드 리뷰 10개
	private String restauranInfo; //매장 소개
	private String restauranService; //매장 편의시설 및 서비스
	
	private int restauranDistance; //현재 위치와 해당 레스토랑의 거리
	private int score; //가중치
	
	@Override
	public int compareTo(Restaurant o) {
		return o.score - this.score;
	}


}
