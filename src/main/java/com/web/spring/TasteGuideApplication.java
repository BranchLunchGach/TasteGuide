package com.web.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.web.spring.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class TasteGuideApplication{
	/*
	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		List<String> categories = new ArrayList<>();
		categories.add("면류");
		categories.add("빵류");
		List<String> nations = new ArrayList<>();
		nations.add("양식");
		menuRepository.findByNationInAndMajorCategorieIn(nations, categories).orElseThrow().forEach((menu)->System.out.println(menu.getSubCategorie()));
	}
*/
	public static void main(String[] args) {
		SpringApplication.run(TasteGuideApplication.class, args);
		
	}

}
