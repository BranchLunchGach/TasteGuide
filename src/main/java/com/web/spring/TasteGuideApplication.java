package com.web.spring;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;
import com.web.spring.api.Crawler;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class TasteGuideApplication {
	
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
