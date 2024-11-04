package com.web.spring.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.spring.entity.User;
import com.web.spring.exception.MemberAuthenticationException;
import com.web.spring.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional(readOnly = true)
	public String duplicateCheck(String userId) {
		User rUser=userRepository.duplicateCheck(userId);
		log.info("rUser ==> { }",rUser);
		if(rUser==null || rUser.equals("")) return "사용가능합니다.";
		else return "중복입니다.";
	}
	
	// 회원가입, 중복체크, 
		@Transactional
		public void signUp(User user) {
			if(userRepository.existsByUserId(user.getUserId()))
				throw new MemberAuthenticationException("중복된 아이디!!", "Duplicated ID!!");
			
			//비번 암호화
			String encPwd=passwordEncoder.encode(user.getPassword());
			log.info("encPwd ==> { }",encPwd);
			user.setPassword(encPwd);
			
			//Role 설정
			user.setRole("ROLE_USER");//지금은 앞부분에 ROLE_를 지정해주지 않는다!!
			
			User savedUser = userRepository.save(user);
			log.info("savedUser ==> { }",savedUser);
		}
	
	
	
}
