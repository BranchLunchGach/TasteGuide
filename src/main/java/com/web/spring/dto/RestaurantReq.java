package com.web.spring.dto;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestaurantReq {
	private String menu;
	private String coreKeyword;
	private String mainKeyword;
	private String avgX;
	private String avgY;
}
