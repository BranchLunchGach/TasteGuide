package com.web.spring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class TasteGuideApplication {
	/*
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		
		//userRepository.deleteByUserId("kosta4");
	}
	*/
	public static void main(String[] args) {
		SpringApplication.run(TasteGuideApplication.class, args);
		
//		long startTime = System.currentTimeMillis();
//		Crawler crawler = new Crawler();
//		crawler.setCount(10);
//		
//		String avgX = "126.72859388";
//		String avgY = "37.51667508";
//		
//		List<String> infos = crawler.hello("파스타", avgX, avgY);
//		for(String info : infos) 
//			System.out.println(info);
//		
//		long endTime = System.currentTimeMillis();
//		System.out.println("10개 걸린 시간 : " +  (endTime-startTime) + "ms");
	}
	
	
}
