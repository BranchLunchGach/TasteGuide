package com.web.spring.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
@Component
public class MapAPI {
	@Value("${naverClientId}")
	private  String clientId; // 네이버 API 클라이언트 ID
	@Value("${naverClientSecret}")
	private  String clientSecret; // 네이버 API 비밀 키
	@Value("${tMapAppKey}")
	private  String appKey; //티맵 API 앱 키
	
	// 주소를 받아 위도와 경도를 반환
    public String getGeocode(String address) {
        try {
        	String addr = URLEncoder.encode(address, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;
            
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            
            br.close();

            // JSON 응답을 처리
            JSONTokener tokener = new JSONTokener(response.toString());
            JSONObject object = new JSONObject(tokener);
            System.out.println(object);
            JSONArray arr = object.getJSONArray("addresses");

            
            System.out.println("44444444");
            if (arr.length() > 0) {
                JSONObject temp = (JSONObject) arr.get(0);
                return temp.get("x") + "," +  temp.get("y");
            } else {
                return "주소를 찾을 수 없습니다.";
            }
        } catch (Exception e) {
            return "오류: " + e.getMessage();
        }
    } // getGeocode()
    
 // 목적지와 출발지의 위도, 경도를 받아 도보 경로 반환
    public String getWalkingRoute(String startX, String startY, String endX, String endY) {
    	try {
    		// 한글로 된 출발지와 도착지 이름을 URL 인코딩
    		String startName = URLEncoder.encode("출발지", "UTF-8");
    		String endName = URLEncoder.encode("도착지", "UTF-8");

    		// 외부 도보 경로 API URL 설정 (POST 방식에서는 보통 query 파라미터를 사용하지 않음)
    		String url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&";

    		// JSON 형태의 요청 본문 생성
    		String requestJson = "{"
    			+ "\"startX\":\"" + startX + "\","
    			+ "\"startY\":\"" + startY + "\","
    			+ "\"endX\":\"" + endX + "\","
    			+ "\"endY\":\"" + endY + "\","
    			+ "\"startName\":\"" + startName + "\","
    			+ "\"endName\":\"" + endName + "\","
    			+ "\"reqCoordType\":\"WGS84GEO\","
    			+ "\"resCoordType\":\"WGS84GEO\""
    			+ "}";

    		// 헤더 설정 (content-type과 appKey)
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.APPLICATION_JSON);
    		headers.set("appKey", appKey); // appKey를 헤더로 추가

    		// HttpEntity 객체 생성 (요청 본문과 헤더를 포함)
    		HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

    		// RestTemplate을 사용해 POST 요청 보내기
    		RestTemplate restTemplate = new RestTemplate();
    		String response = restTemplate.postForObject(url, entity, String.class);

    		// JSON 응답 반환
    		return response;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "{\"error\": \"API 호출 실패\"}";
    	}
    } // getWalkingRoute()
    
    // 두 지점 간의 직선 거리 데이터 반환
    public String getLinearDistance(String startX, String startY, String endX, String endY) {
    	try {
            String apiURL = "https://apis.openapi.sk.com/tmap/routes/distance?startX="+startX+"&startY="+startY+"&endX="+endX+"&endY="+endY;

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("appKey", appKey);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            // 응답을 String으로 변환하여 파싱
            String responseString = response.toString();  // 응답을 String으로 받기

            JSONTokener tokener = new JSONTokener(responseString);  // 응답을 JSONTokener로 변환
            JSONObject object = new JSONObject(tokener);  // JSONObject로 파싱
            
            System.out.println(object);

            // 'distanceInfo'는 객체입니다.
            if (object.has("distanceInfo")) {
                JSONObject distanceInfo = object.getJSONObject("distanceInfo"); // 'distanceInfo' 객체 추출
                return String.valueOf(distanceInfo.get("distance")); // 'distance' 값만 반환
            } else {
                return "직선 거리를 찾을 수 없습니다.";
            }
        } catch (Exception e) {
            return "오류: " + e.getMessage();
        }
    } // getLinearDistance()
} // MapAPI
