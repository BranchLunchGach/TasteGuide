package com.web.spring;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.web.spring.api.MapAPI;

@SpringBootTest
public class MapAPITest {
	@Autowired
	private MapAPI mapAPI;
	
	@Test
    void testGetGeocode() {
        String result = mapAPI.getGeocode("우정국로 2길 21");
        System.out.println("Geocode result: " + result);
        assertNotNull(result); // 결과가 null이 아니어야 합니다.
        assertFalse(result.contains("오류")); // 오류 메시지가 포함되지 않아야 합니다.
    }

    @Test
    void testGetLinearDistance() {
        String result = mapAPI.getLinearDistance("126.9842915", "37.5697105", "126.9", "37.5");
        System.out.println("Linear Distance result: " + result);
        assertNotNull(result); // 결과가 null이 아니어야 합니다.
        assertFalse(result.contains("오류")); // 오류 메시지가 포함되지 않아야 합니다.
    }

    @Test
    void testGetWalkingRoute() {
        String result = mapAPI.getWalkingRoute("126.9842915", "37.5697105", "126.9", "37.5");
        System.out.println("Walking Route result: " + result);
        assertNotNull(result); // 결과가 null이 아니어야 합니다.
        assertFalse(result.contains("API 호출 실패")); // API 호출 실패 메시지가 없어야 합니다.
    }
}
