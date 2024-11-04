package com.web.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
