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
	@Column(name = "menu_id")
	private int menuId;
	
	@Column(name = "major_categorie")
	private String majorCategorie;
	
	@Column(name = "middle_categorie")
	private String middleCategorie;
	
	@Column(name = "sub_categorie")
	private String subCategorie;
	private boolean soup;
	private String recipe;
	//private int scoville;
	private String nation;
	private int calorie;
	private String concept;
	
	@Column(columnDefinition = "decimal(5,2)")
	private double fat;
	
	@Column(columnDefinition = "decimal(5,2)")
	private double carbohydrate;
	
	@Column(columnDefinition = "decimal(5,2)")
	private double protein;
	
	@Column(name = "img_url")
	private String imgUrl;
	private String weather;
	private String season;
	private String holiday;
	
}
