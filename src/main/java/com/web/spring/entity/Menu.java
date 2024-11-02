package com.web.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_no")
	private int menuNo; 
	private String category; // 대분류 ex) 밥류, 면류
	
	@Column(name = "menu_name")
	private String menuName; // 메뉴명 ex) 파스타, 덮밥 
	
	private int calorie;
	
	@Column(columnDefinition = "decimal(5,2)")
	private double carbohydrate;
	
	@Column(columnDefinition = "decimal(5,2)")
	private double fat;
	
	@Column(columnDefinition = "decimal(5,2)")
	private double protein;

	private String nation; // ex) 한식, 중식
	private boolean soup;
	private String season;
	private String holiday;
	private String time;
	private String weather;
	private String keyword;
	
	@Column(name = "img_url")
	private String imgUrl;
	
}
