package com.web.spring.dto;

import java.util.List;

import com.web.spring.entity.Menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MenuRes {
	private String category;
	private String menuName;
	private int calorie;
	private double fat;
	private double carbohydrate;
	private double protein;
	private String imgUrl;
	private List<String> recoReason;
	private String selectNation;
	private String selectCategory;
	private String selectSoup;
	private String selectKeyword;
	
	
	public MenuRes(Menu menu, List<String> recoReason,String selectNation, String selectCategory,String selectSoup,String selectKeyword) {
		category = menu.getCategory();
		menuName = menu.getMenuName();
		calorie = menu.getCalorie();
		fat = menu.getFat();
		carbohydrate = menu.getCarbohydrate();
		protein = menu.getProtein();
		imgUrl = menu.getImgUrl();
		this.recoReason = recoReason;
		this.selectNation = selectNation;
		this.selectCategory = selectCategory;
		this.selectSoup = selectSoup;
		this.selectKeyword = selectKeyword;

		
	}
}
	
	
