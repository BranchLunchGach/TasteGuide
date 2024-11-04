package com.web.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class TasteGuideApplication {
	/*
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private ChoiceRepository choiceRepository;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		LocalDateTime nowDateTime = LocalDate.now().atStartOfDay();
		LocalDateTime twoDay = nowDateTime.minusDays(2);
		System.out.println(twoDay);
		choiceRepository.findChoice(twoDay, 1).orElseThrow().forEach((choice)->System.out.println(choice));
	}
*/
	public static void main(String[] args) {
		SpringApplication.run(TasteGuideApplication.class, args);
		
	}

}
