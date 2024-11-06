package com.web.spring.dto;

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
public class MenuReq {
	private String nation;
	private String category;
	private String keyword;
	private String soup;
	private String selectName;
	private String weather;
}
	
	
