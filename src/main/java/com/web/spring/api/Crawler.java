package com.web.spring.api;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Crawler {
	// ChromeDriver 초기화
		int count = 0;
	    WebDriver driver = null;
	    WebDriverWait wait = null;
	    ChromeOptions options = null;
	    StringBuilder sb = null;
	    
	    public Crawler(int count, int zero) {
	    	this.count = count;
	        
	    	driver = new ChromeDriver();
	    	wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    	sb = new StringBuilder();
	    	WebDriverManager.chromedriver().setup();  
	    }
	    
	    public Crawler(int count) {
	    	this.count = count;
	    	
	    	log.info("[Crawler] 생성 중...");
	    	options = new ChromeOptions();
	        //options.addArguments("--headless"); // headless 모드 활성화
	    	//이놈이 핵심 옵션..
	    	options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
	        options.addArguments("--disable-features=NetworkService");
	        options.addArguments("--headless=new"); // 새로운 headless 모드 시도
	        options.addArguments("--disable-gpu"); // GPU 비활성화 (Windows 환경에서 가끔 필요)
	        options.addArguments("--window-size=1920,1080"); // 화면 크기 설정 (해상도 문제 방지)
	        options.addArguments("--force-device-scale-factor=1"); // 스케일링 강제 설정
	        options.addArguments("--disable-dev-shm-usage"); // 메모리 공유 비활성화
	        options.addArguments("--no-sandbox"); // 샌드박스 비활성화
	        options.addArguments("--remote-allow-origins=*");
	        options.addArguments("--blink-settings=imagesEnabled=true"); // 이미지 로딩 허용
	        
	    	driver = new ChromeDriver(options);
	    	wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    	sb = new StringBuilder();
	    	WebDriverManager.chromedriver().setup();  
	    	log.info("[Crawler] 생성 완료...");
	    }
	    
	    public List<String> aiRecommend(String keyword) {
	    	 try {
    		 	// JavaScript로 검색어를 입력
	        	keywordSearch(driver, wait, keyword);    
	        	
	        	log.info("[Crawler] aiRecommend() 실행...");
	            
	            // 가게 이름 요소를 두 가지 경우에 맞게 찾기
	            List<WebElement> shopLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div>.place_bluelink>.TYaxT, div.place_bluelink.C6RjW > span.YwYLL")));
	            	                        
	            //가게들의 리뷰들을 담을 컬렉션
	            List<String> infos = new ArrayList<>();
	            
	            for (int i = 0; i < Math.min(count, shopLinks.size()); i++) {
	                WebElement shop = shopLinks.get(i);
	                
	                detailPageActiveCheck(driver, shop);
	                
	                frameChangeByEntryIframe(driver, wait);
	                
	                log.info("[Crawler - 반복 {}/{}] aiRecommend() 반복 시작", i+1, count);
	                
	                //오늘 휴무인지 체크
	                List<WebElement> times = driver.findElements(By.cssSelector(".A_cdD>em"));
	                if(!times.isEmpty()) {
	                	String time = times.get(0).getText();
	                	if(time.equals("오늘 휴무")) sb.append("오늘 휴무").append("}");
	                	else  sb.append("0").append("}");
	                } else sb.append("0");
	                
	                int btnCnt = 0;
	                WebElement button = null;
	                boolean isClick = false;
	                
	                while (btnCnt < 3) {
	                	try {
		                	button = driver.findElement(By.cssSelector("span.U7pYf"));
		                	((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
			                isClick = true;
			                break;
		                } catch (NoSuchElementException e) {
		                	Thread.sleep(1000);
		                	btnCnt++;
		                }
	                }
	                
	                if (!isClick) {
	                	frameChange(driver, wait, "searchIframe");
		                sb.setLength(0);	
		                continue;
	                }
	                
	                Thread.sleep(2000);
	                
	                List<WebElement> openTime = driver.findElements(By.cssSelector("div.w9QyJ div.H3ua4"));
	                
	                if(openTime.size() == 1)
	                	sb.append(openTime.get(0).getText()).append("}");
	                else 
	                	sb.append(openTime.get(1).getText()).append("}");
	                
	                log.info("[Crawler - 반복 {}/{}] aiRecommend() 메뉴 크롤링...", i+1, count);
	                
	                // ======================== 메뉴 탭 클릭 부분 ========================
	                try {
	                	WebElement menuTab = getTab(driver, "메뉴"); 
	                	tabClickAndActiveCheck(driver, menuTab);
	                } catch (NullPointerException e) {
	                	log.warn("[Crawler - 반복 {}/{}] aiRecommend() 메뉴 NullPointerException 발생", i+1, count);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                } catch (StaleElementReferenceException e) {
	                	log.warn("[Crawler - 반복 {}/{}] aiRecommend() 메뉴 StaleElementReferenceException 발생", i+1, count);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                } catch (JavascriptException e) {
	                	log.warn("[Crawler - 반복 {}/{}] aiRecommend() 메뉴 JavascriptException 발생", i+1, count);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                }
	                
	                //메뉴명 5개 가져오기
	                List<String> menu = new ArrayList<>();
	    	    	List<WebElement> allMenuElements = driver.findElements(By.cssSelector(".E2jtL, li.order_list_item"));
	    	    	int count = 0;	    	    
	    	    	
	    	    	// 최대 5개만 가져오기
	    	    	List<WebElement> menuElements = allMenuElements.size() > 5 ? allMenuElements.subList(0, 5) : allMenuElements;
	    	    	
	    	    	for (WebElement w : menuElements) {

	    	        	String name = w.findElement(By.cssSelector(".lPzHi, .info_detail>.tit")).getText();	            

	    	        	sb.append(name).append("}");
	    	        	count++;
	    	        }
	    	    	
	    	    	for (int j = 0; j < (5-count); j++)
	    	    		sb.append("0}");
	                                
	                frameChange(driver, wait, "searchIframe");
	                
	                infos.add(sb.toString());
	                sb.setLength(0);	                
	            }
	            return infos;
	        } catch (Exception e) {
	            e.printStackTrace();
	        } 
	        
	        return null;
	    }
	    
	    public List<String> hello(String menu, String x, String y) {	        
	        
	    	try {
	        	// 네이버 지도 페이지로 접속
		        driver.get("https://map.naver.com");
				
				 // JavaScript로 검색어를 입력
		        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input.input_search")));
		        searchBox.sendKeys(y + "," + x);
		        searchBox.sendKeys(Keys.ENTER);
		        
				Thread.sleep(2000);
				
		        searchBox.sendKeys(menu);
		        searchBox.sendKeys(Keys.ENTER);
		        		        
		        Thread.sleep(2000);

		        JavascriptExecutor js = (JavascriptExecutor) driver;
		        while (!(Boolean) js.executeScript("return document.querySelector('iframe#searchIframe') !== null;")) {
		            log.warn("[Crawler] hello() iframe이 아직 로드되지 않았습니다...");
		            Thread.sleep(500); // 짧은 시간 대기 후 반복 확인
		        }		       
		        
		        driver.switchTo().frame("searchIframe");
		        
		        log.info("[Crawler] hello() 네이버 검색 진행 후 가게 찾으러 가기");
		        
		        return reviewCrawlin2(menu);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	
	    	return null;
	    }
		
		public List<String> reviewCrawling(String keyword) { 
			
	        try {
	        	Thread.sleep(1000); 
	        	log.info("[Crawler] reviewCrawling 실행중이지비");
	            
	            //JavaScript로 검색어를 입력
	        	keywordSearch(driver, wait, keyword);    
	        	
	        	log.info("[Crawler] reviewCrawling 네이버 검색 진행 후 가게 찾으러 가기");
	            
	            //가게 이름 요소를 두 가지 경우에 맞게 찾기
	            List<WebElement> shopLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div>.place_bluelink>.TYaxT, div.place_bluelink.C6RjW > span.YwYLL")));
	            	                        
	            //가게들의 리뷰들을 담을 컬렉션
	            List<String> infos = new ArrayList<>();

	            // 최대 2개의 상세 페이지 URL 가져오기
	            for (int i = 0; i < Math.min(count, shopLinks.size()); i++) {
	                WebElement shop = shopLinks.get(i);
	                
	                WebElement shopType = driver.findElement(By.cssSelector("span.KCMnt"));               
	                
	                log.info("[Crawler - 반복 {}/{}] reviewCrawlin() 반복 시작", i+1, count);
	                sb.append(shop.getText()).append("}"); //sb.append("[매장 이름] : " + shop.getText()).append("\n");
	                sb.append(shopType.getText()).append("}"); //sb.append("[매장 타입] : " + shopType.getText()).append("\n");
	                
	                detailPageActiveCheck(driver, shop);
	                
	                frameChangeByEntryIframe(driver, wait);
	                
	                log.info("[Crawler - 반복 {}/{}] reviewCrawling() 내부의 EntryIframe 변환 완료", i+1, count);
	                
	                String mainImg = driver.findElement(By.cssSelector("div.fNygA>a>img, div.GWWbE>a>img")).getDomAttribute("src");
	                
	                List<WebElement> times = driver.findElements(By.cssSelector(".A_cdD>em"));
	                if(!times.isEmpty()) {
	                	String time = times.get(0).getText();
	                	if(time.equals("오늘 휴무")) sb.append("오늘 휴무").append("}");
	                	else  sb.append("0").append("}");
	                } else sb.append("0");
	                
	                //주소 및 위치 가져와 보기.
	                WebElement address = driver.findElement(By.cssSelector("span.LDgIH"));	               
	                sb.append(address.getText()).append("}"); //sb.append("[매장 주소] : " + address.getText()).append("\n");

	                try {
	                    WebElement location = driver.findElement(By.cssSelector("div.nZapA"));
	                    List<WebElement> locationNum = driver.findElements(By.cssSelector("span.DNzQ2"));
	                    
	                    if(locationNum.size() == 0) sb.append(location.getText().replace("\n", " ").replace("미터", "")).append("}"); 
	                    else {
	                    	String num = "";
	                    	for(WebElement w : locationNum) {
	                    		num += w.getText();
	                    	}
	                    	System.out.println(num);
	                    	sb.append(location.getText().replace("\n", " ").replace("미터", "").replace(num, "")).append("}");
	                    }
	                } catch (NoSuchElementException e) {
	                	sb.append("0").append("}"); //sb.append("[매장 위치] : 주소 미작성").append("\n");
	                }    
	                
	                // ======================== 메뉴 탭 클릭 부분 ========================
	                
	                log.info("[Crawler - 반복 {}/{}] reviewCrawlin() 메뉴 크롤링중...", i+1, count);

	                try {
	                	WebElement menuTab = getTab(driver, "메뉴"); 
	                	tabClickAndActiveCheck(driver, menuTab);
	                } catch (NullPointerException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawling() NullPointerException 발생", i+1, count);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                } catch (StaleElementReferenceException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawling() StaleElementReferenceException 발생", i+1, count);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                } catch (JavascriptException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawling() JavascriptException 발생", i+1, count);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                }
	                
	                String menus = menuCrawling(driver);
	                sb.append(menus);                         
	                
	                log.info("[Crawler - 반복 {}/{}] reviewCrawlin() 리뷰 크롤링중...", i+1, count);
	                
	                // ======================== 리뷰 탭 클릭 부분 ========================
	                try {
	                	WebElement reviewTab = getTab(driver, "리뷰");
	                	// 리뷰 탭 클릭 및 활성화 확인
		                tabClickAndActiveCheck(driver, reviewTab);  
	                } catch (NullPointerException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawlin() NullPointerException 발생", i+1, count);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                } catch (JavascriptException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawlin() JavascriptException 발생", i+1, count);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                }
	                
	                // 별점 및 총 리뷰(블로그 리뷰, 방문자 리뷰) 개수 가져오기.
	                List<WebElement> reviewTotalCnt = driver.findElements(By.cssSelector("span.PXMot"));
	                String reviewTotal = "";	              
	                
	                if(reviewTotalCnt.size() == 3) {
	                	for (int j = 0; j < reviewTotalCnt.size(); j++) {
	                		if(j == 0) {
	                			String starReview = reviewTotalCnt.get(j).getText().replaceAll("[^0-9]", "");
	                			double review = Integer.parseInt(starReview) * 0.01;
		                    	sb.append(String.format("%.2f", review)).append("}");
	                		} else {
	                			String review = reviewTotalCnt.get(j).getText().replaceAll("[^0-9]", "");
		                    	sb.append(review).append("}");
	                		}
	                	}
	                } else if (reviewTotalCnt.size() == 1) { //블로그 리뷰만 존재하는경우...
	                	sb.append("0").append("}");
	                	sb.append("0").append("}");
	                	for (WebElement reviewCnt : reviewTotalCnt) {
	                    	String review = reviewCnt.getText().replaceAll("[^0-9]", "");
	                    	sb.append(review).append("}");
	                    }
	                }  else { //별점 리뷰가 존재하지 않을경우..
	                	sb.append("0").append("}");
	                	for (WebElement reviewCnt : reviewTotalCnt) {
	                    	String review = reviewCnt.getText().replaceAll("[^0-9]", "");
	                    	sb.append(review).append("}");
	                    }
	                }             

	                List<String> keywordReviews = reviewRecommendKeyword(driver);
	                for(String keywordReview : keywordReviews) 
	                	sb.append(keywordReview).append("}");
	                
	                List<String> textReviews = reviewCrawling(driver);
	                for(String textReview : textReviews) 
	                	sb.append(textReview).append("}");
	                
	                log.info("[Crawler - 반복 {}/{}] reviewCrawlin() 정보 크롤링중...", i+1, count);
	                                
	                // ======================== 정보 탭 클릭 부분 ========================
	                WebElement infoTab = getTab(driver, "정보");  
	                
	                if(infoTab != null) {
	                	tabClickAndActiveCheck(driver, infoTab);
	                    
	                    String info = infoCrawling(driver);
	                    String service = restaurantServiceCrawling(driver);
	                    sb.append(info).append("}"); //sb.append("[매장 소개] : " + info).append("\n");
	                    sb.append(service).append("}"); //sb.append("[매장 서비스] : " + service).append("\n");
	                } else {
	                	sb.append("0").append("}");
	                	sb.append("0").append("}");
	                }
	                                
	                frameChange(driver, wait, "searchIframe");
	                
	                sb.append(mainImg).append("}");
	                infos.add(sb.toString());
	                sb.setLength(0);	                
	            }
	            return infos;
	            
	        } catch (Exception e) {
	            log.error("[Crawler] reviewCrawling() ERRER!!! {}", e);
	        } 
	        
	        return null;
	    }
		
		public List<String> reviewCrawlin2(String keyword) { 
			
			log.info("[Crawler] reviewCrawling2 실행중이지비");        	
			
	        try {     	           	            	        	
	        	
	            // 가게 이름 요소를 두 가지 경우에 맞게 찾기
	            List<WebElement> shopLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div>.place_bluelink>.TYaxT, div.place_bluelink.C6RjW > span.YwYLL")));
	            
	            //가게들의 리뷰들을 담을 컬렉션
	            List<String> infos = new ArrayList<>();	            

	            // 최대 2개의 상세 페이지 URL 가져오기
	            for (int i = 0; i < Math.min(count, shopLinks.size()); i++) {	    
	            	
	            	log.info("[Crawler - 반복 {}/{}] reviewCrawlin2() 반복 시작", i+1, count);
	            	
	                WebElement shop = shopLinks.get(i);
	                
	                WebElement shopType = driver.findElement(By.cssSelector("span.KCMnt"));	                
	                
	                sb.append(shop.getText()).append("}"); //sb.append("[매장 이름] : " + shop.getText()).append("\n");
	                sb.append(shopType.getText()).append("}"); //sb.append("[매장 타입] : " + shopType.getText()).append("\n");
	                
	                detailPageActiveCheck(driver, shop);
	                
	                frameChangeByEntryIframe(driver, wait);
	                
	                log.info("[Crawler - 반복 {}/{}] reviewCrawlin2() EntryIframe으로 전환 성공", i+1, count);
	                
	                String mainImg = driver.findElement(By.cssSelector("div.fNygA>a>img, div.GWWbE>a>img")).getDomAttribute("src");
	                
	                List<WebElement> times = driver.findElements(By.cssSelector(".A_cdD>em"));
	                if(!times.isEmpty()) {
	                	String time = times.get(0).getText();
	                	if(time.equals("오늘 휴무")) sb.append("오늘 휴무").append("}");
	                	else  sb.append("0").append("}");
	                } else sb.append("0").append("}");
	                
	                //주소 및 위치 가져와 보기 , 요소가 있는 경우 텍스트를 가져와 출력 , 요소가 없는 경우 "주소 미작성" 출력
	                WebElement address = driver.findElement(By.cssSelector("span.LDgIH"));	               
	                sb.append(address.getText()).append("}"); //sb.append("[매장 주소] : " + address.getText()).append("\n");	                
	                
	                try {
	                    WebElement location = driver.findElement(By.cssSelector("div.nZapA"));
	                    List<WebElement> locationNum = driver.findElements(By.cssSelector("span.DNzQ2"));
	                    // 요소가 있는 경우 텍스트를 가져와 출력
	                    if(locationNum.size() == 0) sb.append(location.getText().replace("\n", " ").replace("미터", "")).append("}"); 
	                    else {
	                    	String num = locationNum.get(0).getText();
	                    	sb.append(location.getText().replace("\n", " ").replace("미터", "").replace(num, "")).append("}");
	                    }
	                } catch (NoSuchElementException e) {
	                    // 요소가 없는 경우 "주소 미작성" 출력
	                	sb.append("0").append("}"); //sb.append("[매장 위치] : 주소 미작성").append("\n");
	                } 
	                
	                log.info("[Crawler - 반복 {}/{}] reviewCrawlin2() 메뉴 크롤링중...", i+1, count);
	                
	                // ======================== 메뉴 탭 클릭 부분 ========================
	                // 메뉴 탭 클릭 및 활성화 확인
	                try {
	                	WebElement menuTab = getTab(driver, "메뉴"); 
	                	tabClickAndActiveCheck(driver, menuTab);
	                } catch (NullPointerException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawlin2() NullPointerException 발생", i, count, e);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                } catch (StaleElementReferenceException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawlin2() StaleElementReferenceException 발생", i, count, e);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                } catch (JavascriptException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawlin2() JavascriptException 발생", i, count, e);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                }
	                
	                String menus = menuCrawling(driver);
	                sb.append(menus);                         
	                
	                log.info("[Crawler - 반복 {}/{}] reviewCrawlin2() 리뷰 크롤링중...", i+1, count);
	                
	                // ======================== 리뷰 탭 클릭 부분 ========================    
	                try {
	                	WebElement reviewTab = getTab(driver, "리뷰");
	                	// 리뷰 탭 클릭 및 활성화 확인
		                tabClickAndActiveCheck(driver, reviewTab);  
	                } catch (NullPointerException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawlin2() 리뷰 NullPointerException 발생", i, count, e);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                } catch (JavascriptException e) {
	                	log.warn("[Crawler - 반복 {}/{}] reviewCrawlin2() 리뷰 JavascriptException 발생", i, count, e);
	                	sb.setLength(0);
	                	frameChange(driver, wait, "searchIframe");
	                	continue;
	                }

	                
	                // 별점 및 총 리뷰(블로그 리뷰, 방문자 리뷰) 개수 가져오기.
	                List<WebElement> reviewTotalCnt = driver.findElements(By.cssSelector("span.PXMot"));
	                String reviewTotal = "";	              
	                
	                if(reviewTotalCnt.size() == 3) {
	                	for (int j = 0; j < reviewTotalCnt.size(); j++) {
	                		if(j == 0) {
	                			String starReview = reviewTotalCnt.get(j).getText().replaceAll("[^0-9]", "");
	                			double review = Integer.parseInt(starReview) * 0.01;
		                    	sb.append(String.format("%.2f", review)).append("}");
	                		} else {
	                			String review = reviewTotalCnt.get(j).getText().replaceAll("[^0-9]", "");
		                    	sb.append(review).append("}");
	                		}
	                	}
	                } else if (reviewTotalCnt.size() == 1) { //블로그 리뷰만 존재하는경우...
	                	sb.append("0").append("}");
	                	sb.append("0").append("}");
	                	for (WebElement reviewCnt : reviewTotalCnt) {
	                    	String review = reviewCnt.getText().replaceAll("[^0-9]", "");
	                    	sb.append(review).append("}");
	                    }
	                }  else { //별점 리뷰가 존재하지 않을경우..
	                	sb.append("0").append("}");
	                	for (WebElement reviewCnt : reviewTotalCnt) {
	                    	String review = reviewCnt.getText().replaceAll("[^0-9]", "");
	                    	sb.append(review).append("}");
	                    }
	                }              
	                

	                List<String> keywordReviews = reviewRecommendKeyword(driver);
	                for(String keywordReview : keywordReviews) 
	                	sb.append(keywordReview).append("}");
	                
	                List<String> textReviews = reviewCrawling(driver);
	                for(String textReview : textReviews) 
	                	sb.append(textReview).append("}");
	                
	                log.info("[Crawler - 반복 {}/{}] reviewCrawlin2() 정보 크롤링중...", i+1, count);
	                                
	                // ======================== 정보 탭 클릭 부분 ========================
	                WebElement infoTab = getTab(driver, "정보");  
	                
	                if(infoTab != null) {
	                	tabClickAndActiveCheck(driver, infoTab);
	                    
	                    String info = infoCrawling(driver);
	                    String service = restaurantServiceCrawling(driver);
	                    sb.append(info).append("}"); //sb.append("[매장 소개] : " + info).append("\n");
	                    sb.append(service).append("}"); //sb.append("[매장 서비스] : " + service).append("\n");
	                } else {
	                	sb.append("0").append("}");
	                	sb.append("0").append("}");
	                }
	                                
	                frameChange(driver, wait, "searchIframe");
	                
	                sb.append(mainImg).append("}"); //메인 이미지 저장
	                infos.add(sb.toString());
	                sb.setLength(0);
	            }
	            return infos;
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        } 
	        
	        return null;
	    }
		
		public void close() {
			 driver.quit();
		}
		
		 // 네이버 지도 접속 후 검색어를 입력 및 iframe 전환
		private void keywordSearch(WebDriver driver, WebDriverWait wait, String keyword) throws InterruptedException {
			 // 네이버 지도 페이지로 접속
	        driver.get("https://map.naver.com");
			
			 // JavaScript로 검색어를 입력
	        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input.input_search")));
	        searchBox.sendKeys(keyword);
	        searchBox.sendKeys(Keys.ENTER);	       
	        
	        Thread.sleep(2000);

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        while (!(Boolean) js.executeScript("return document.querySelector('iframe#searchIframe') !== null;")) {;
	            Thread.sleep(500); // 짧은 시간 대기 후 반복 확인
	        }
	        driver.switchTo().frame("searchIframe");


		}
		
		// 상세 페이지 활성화 확인
	    private void detailPageActiveCheck(WebDriver driver, WebElement detailPage) throws InterruptedException {
	    	// JavaScript로 클릭 실행
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", detailPage);
	        while (!driver.getCurrentUrl().contains("/place/")) {
	            //System.out.println("상세페이지 탭이 활성화되지 않았습니다. 재시도합니다...");
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", detailPage);
	            Thread.sleep(1000); // 재시도 후 대기
	        }
	    }
	    
	    // ======================== 리뷰 탭 클릭 부분 ========================
	    private WebElement getTab(WebDriver driver, String tabName) {
	    	List<WebElement> menuItems = driver.findElements(By.cssSelector(".flicking-camera>a"));
	                        
	        for (WebElement menuItem : menuItems) {
	            String menuText = menuItem.getText().trim();
	            if (tabName.equals(menuText)) return menuItem;
	        }
	        
	        return null;
	    }
	    
	    
	    // 탭 활성화 확인
	    private void tabClickAndActiveCheck(WebDriver driver, WebElement clickTab) throws InterruptedException, JavascriptException {    	
	    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickTab);
	    	
	        while (!clickTab.getAttribute("aria-selected").equals("true")) {
	        	//System.out.println(clickTab.getText() + "탭이 활성화되지 않았습니다. 재시도합니다...");
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickTab);
	            Thread.sleep(500); // 재시도 후 대기
	        }        
	        Thread.sleep(2000);
	    }
	    
	    private void frameChange(WebDriver driver, WebDriverWait wait, String iframeName) {
	       // 원래 검색 페이지로 돌아가기
	       driver.switchTo().defaultContent(); //driver.navigate().back(); // 이전 페이지로 돌아가기
	        
	       // iframe 전환
	       wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe#" + iframeName)));
	    }
	    
	    //entryIframe 변경 체크 (예외 처리 포함)
	    private void frameChangeByEntryIframe(WebDriver driver, WebDriverWait wait) throws InterruptedException {
	    	while (true) {
	            try {
	            	frameChange(driver, wait, "entryIframe");
	                break; // 성공하면 반복 종료
	            } catch (NoSuchFrameException e) {
	                //System.out.println("entryIframe을 찾을 수 없습니다. 다시 시도합니다...");
	                Thread.sleep(500); // 잠시 대기 후 다시 시도
	            }
	        }
	    }
	    
	 // 메뉴 탭에서 메뉴 정보 가져오기
	    private String menuCrawling(WebDriver driver) {
	    	List<String> menu = new ArrayList<>();
	    	List<WebElement> allMenuElements = driver.findElements(By.cssSelector(".E2jtL, li.order_list_item"));
	    	StringBuilder sb = new StringBuilder();
	    	int count = 0;
	    	String imgUrl = "0"; // 기본 값 설정	    
	    	
	    	// 최대 5개만 가져오기
	    	List<WebElement> menuElements = allMenuElements.size() > 5 ? allMenuElements.subList(0, 5) : allMenuElements;

	    	for (WebElement w : menuElements) {

	        	List<WebElement> imgElements = w.findElements(By.cssSelector(".YBmM2 img, .img_box img"));

	        	if (!imgElements.isEmpty()) {
	        	    imgUrl = imgElements.get(0).getDomAttribute("src");
	        	}

	        	String name = w.findElement(By.cssSelector(".lPzHi, .info_detail>.tit")).getText();	            
	        	String price = w.findElement(By.cssSelector(".GXS1X, .price")).getText();

	        	sb.append(imgUrl + "\\\\" + name + "\\\\" + price).append("}");
	        	count++;
	        }
	    	
	    	for (int i = 0; i < (5-count); i++)
	    		sb.append("0]0]0}");
	    	
	    	
	    	return sb.toString();
	    }
	    
	    //리뷰 탭에서 "이런 점이 좋았어요" 크롤링
	    private List<String> reviewRecommendKeyword(WebDriver driver) throws InterruptedException {
	    	int i = 0;
	    	List<String> reviewKeywords = new ArrayList<>();
	    	boolean isReviewSticker = false;   
	       
	        while (i < 3) {
	        	try {
	        		WebElement button = driver.findElement(By.cssSelector("a.dP0sq"));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
	                isReviewSticker = true;
	                break;
	        	} catch(NoSuchElementException e) {
	        		log.warn("[Crawler] reviewRecommendKeyword() 버튼을 찾지 못했습니다. 재시도 중 {} / {}", i+1, 3);
	                i++;
	                
	                try {
	                    Thread.sleep(1000); // 재시도 전 대기
	                } catch (InterruptedException ie) {
	                    ie.printStackTrace();
	                }
	        	}
	        }           
	        
	        if (isReviewSticker) {
	        	Thread.sleep(2000);
	            List<WebElement> reviewBenefits = driver.findElements(By.cssSelector(".MHaAm"));
	            
	            for (WebElement benefits : reviewBenefits) {
	            	System.out.println(benefits.getText());
	            	String keyword = benefits.findElement(By.cssSelector(".t3JSf")).getText(); 
	            	String text = benefits.findElement(By.cssSelector(".CUoLy")).getText();
	            	String cnt = text.replace("이 키워드를 선택한 인원", "").trim();  // "256"만 남기도록 텍스트를 대체합니다.
	            	
	            	reviewKeywords.add(keyword + ", " + cnt);
	            }
	        } else {
	        	int j = 0;
	        	while(j < 10) {
	        		reviewKeywords.add("0");
	        		j++;
	        	}
	        }
	        
	        return reviewKeywords;
	    }
	    
	    //리뷰 탭에서 리뷰 더보기 버튼 클릭
	    private void reviewPlusButtonClick(WebDriver driver) throws InterruptedException {    	
	    	int cnt = 0;
	    	
	        while (true) {
	            List<WebElement> moreButtons = driver.findElements(By.cssSelector("a.fvwqf"));
	            if (!moreButtons.isEmpty() && cnt < 4) { // 클릭하는 숫자 1당 리뷰 +10개
	                WebElement moreButton = moreButtons.get(0);
	                
	                // 더보기 버튼이 존재하면 클릭
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", moreButton);                       
	                Thread.sleep(500); // 다음 버튼 클릭 후 대기
	                cnt++;
	            } else {
	                //System.out.println("리뷰 더보기의 버튼이 더 이상 존재하지 않습니다.");
	                break;
	            }
	        }
	    }
	    
	    // 리뷰 크롤링
	    private List<String> reviewCrawling(WebDriver driver) {
	    	// 리뷰를 담는 List 생성
	    	List<String> reviews = new ArrayList<>();
	    	List<WebElement> reviewsElement = driver.findElements(By.cssSelector("li.EjjAW")); 
	    	
	    	try {
	    		int reviewCnt = 0;
		        for (WebElement r : reviewsElement) {
		        	if (reviewCnt >= 5) break;  // 리뷰가 5개에 도달하면 루프 종료	            
		            reviewCnt++;
		            String review = r.findElement(By.cssSelector(".pui__vn15t2>a")).getText();
		            reviews.add(review);
		        }
		        
		        // 부족한 리뷰를 "0"으로 채우기
		        while (reviews.size() < 5)
		            reviews.add("0");
		        
	    	} catch (NoSuchElementException e) {
	    		int cnt = 0;
	    		while(cnt < 5) {
	    			cnt++;
	    			log.warn("[Crawler] reviewCrawling() 리뷰 요소가 존재하지 않습니다. 전부 0으로 채우겠습니다.");
	    			reviews.add("0");
	    		}
	    	}
	        
	        return reviews;
	    }
	    
	    //정보 탭에서 매장 소개 크롤링
	    private String infoCrawling(WebDriver driver) {
	         String info = "0";  // 기본값 설정
	         try {
	             WebElement infoElement = driver.findElement(By.cssSelector(".place_section.no_margin.Od79H"));
	             info = infoElement.findElement(By.cssSelector(".place_section_content .T8RFa")).getText();
	         } catch (NoSuchElementException e) {
	        	 log.warn("[Crawler] infoCrawling() 매장 소개 없음..");
	         }
	         return info;
	    }
	    
	    //정보 탭에서 편의시설 및 서비스 크롤링
	    private String restaurantServiceCrawling(WebDriver driver) {
	        
	        StringBuilder sb = new StringBuilder();
	        sb.append("");
	        
	        try {
	        	WebElement service = driver.findElement(By.cssSelector(".place_section.no_margin.VMtyJ"));
	        	List<WebElement> servicese = service.findElements(By.cssSelector(".c7TR6 div.owG4q"));
	        	for (WebElement s : servicese) sb.append(s.getText() + ", ");

	            // 주차 가능 여부를 확인할 수 있는 kBQcG 클래스를 가진 요소가 있는지 찾기
	            List<WebElement> parkingElements = driver.findElements(By.cssSelector(".kBQcG"));
	            if (parkingElements.isEmpty()) sb.append("주차불가");
	            else sb.append("주차가능");
	            
	        } catch(NoSuchElementException e) {
	        	log.warn("[Crawler] restaurantServiceCrawling() 정보탭 어딘가에서 에러 발생...");
	        	return "0";	        	
	        }
	        
	        return sb.toString();
	    }
}
