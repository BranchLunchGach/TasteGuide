package com.web.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//인증방식과 인가정책을 지정....
    	log.info("SecurityFilterChain======================>");
    	
    	http.csrf((auth)->auth.disable()); //csrf 공격을 방어하기 위한 토큰 주고받는 부분을 비활성화
    	/*
    	 1. secutrity에서 제공하는 폼을 비활성화
    	 2. React를 앞으로 사용
    	 3. formLogin을 disable하면 인증 시큐리티 아키텍쳐에서 사용안하는 부분이 있다. 
    	 	- UsernamePasswordAuthenticationFilter를 무력화시키는 방식으로 인증방식이 변경된다!!

    	 */
    	http.formLogin((auth)->auth.disable());
    	http.httpBasic((auth)->auth.disable());
    	
    	//경로별 인가작업
    	http.authorizeHttpRequests((auth) -> auth
    										 .requestMatchers("/menu").permitAll()
    										 .requestMatchers("/admin").hasRole("ADMIN")
    										 .anyRequest().authenticated());//인증을 거쳐서 들어와야한다.
		return http.build();
	}
    
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
    	log.info("BCryptPasswordEncoder call..============>");
    	return new BCryptPasswordEncoder();
    }
}
