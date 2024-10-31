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
	private String middleCategorie;
	private String subCategorie;
	private int calorie;
	private double fat;
	private double carbohydrate;
	private double protein;
	private String imgUrl;
	private List<String> recommReason;
	
	public MenuRes(Menu menu, List<String> a) {
		middleCategorie = menu.getMiddleCategorie();
		subCategorie = menu.getSubCategorie();
		calorie = menu.getCalorie();
		fat = menu.getFat();
		carbohydrate = menu.getCarbohydrate();
		protein = menu.getProtein();
		imgUrl = menu.getImgUrl();
		recommReason = a;
	}
}
	
	
