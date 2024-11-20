package com.web.spring.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.util.Value;
import com.web.spring.entity.User;

import jakarta.annotation.PostConstruct;




public class GoogleAuthService2 {
	
	@Autowired
	Environment environment;
	
	@Value("${googleClientId}")
	 private String ClientId ;
	@Value("${googleClientSecret}")
	 private String ClientSecret ;
	@Value("${googleRedirectUri}") 
	 private String RedirectUri ;
	@Value("${googleTokenUrl}")
	 private String TokenUrl ;
	
	 private String accessToken;
	
	@PostConstruct
	public void init() {
		System.out.println("ClientId= "+ClientId);
    	System.out.println("ClientSecret= "+ClientSecret);
    	System.out.println("RedirectUri= "+RedirectUri);
    	System.out.println("TokenUrl= "+TokenUrl);
	}
		
   

    // Step 2: Google에서 반환한 code로 Access Token 교환
    public String exchangeCodeForToken(String code) {
    	System.out.println("exchangeCodeForToken start");
        // RestTemplate에 필요한 메시지 변환기를 추가합니다.  
    	System.out.println("step 1");
        RestTemplate restTemplate = new RestTemplate();
        
        // 요청 파라미터를 map으로 준비합니다.
    	System.out.println("step 2");
    	MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", ClientId);
        params.add("client_secret", ClientSecret);
        params.add("redirect_uri", RedirectUri);
        params.add("code", code);
        params.add("grant_type", "authorization_code");

        // HttpHeaders 설정
    	System.out.println("step 3");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  // application/x-www-form-urlencoded

        // 요청 바디에 파라미터를 포함한 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 실제 요청을 보내고 응답을 받음
        System.out.println("step 4");
        ResponseEntity<Map> response = restTemplate.exchange(
        		TokenUrl, 
            HttpMethod.POST, 
            request, 
            Map.class
        );
        System.out.println("response = "+response);
        // 응답에서 액세스 토큰을 추출
        System.out.println("step 5");
        Map<String, Object> responseBody = response.getBody();
        
        if (responseBody != null && responseBody.containsKey("access_token")) {
        	accessToken = (String)responseBody.get("access_token");
            return (String) responseBody.get("id_token"); // 액세스 토큰 반환
        } else {
            throw new RuntimeException("Failed to retrieve access token");
        }
    }
    //주소 , 전화번호 안 받아와져서 사용 보류
    public User getExtraUserInfo(User user) {
    	System.out.println("getUserInfo start");
       // restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        String url = "https://people.googleapis.com/v1/people/me";
        String personFields = "phoneNumbers,addresses,birthdays,ageRanges,genders";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+ accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String queryParams = "?personFields="+personFields;
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
        		url+queryParams,
        		HttpMethod.GET,
        		request,
        		String.class);
      
       System.out.println("people response = "+response);
       System.out.println("people response.body = "+response.getBody().replaceAll("\"", "").replaceAll("{", "").replaceAll("}", "").replaceAll("[", "").replaceAll("]", ""));
      
       
       
       
    	return user;
    }



	



	public GoogleAuthService2(Environment environment) {
		System.out.println("기본생성자 호출");
		//googleOauthClientId = environment.getProperty("googleOauthClientId");
		//googleOauthClientSecret = environment.getProperty("googleOauthClientSecret");
		//googleOauthRedirectUri = environment.getProperty("googleOauthRedirectUri");
		//googleOauthTokenUrl =  environment.getProperty("googleOauthTokenUrl");
	}
	

    
}
