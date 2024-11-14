package com.web.spring.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.spring.entity.User;
import com.web.spring.exception.BoardSearchNotException;
import com.web.spring.exception.MemberAuthenticationException;
import com.web.spring.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;
	
	
	// 회원가입, 중복체크, 
		@Transactional
		public User signUp(User user) {
			if(userRepository.existsByUserId(user.getUserId()))
				throw new MemberAuthenticationException("중복된 아이디!!", "Duplicated ID!!");
			
			//비번 암호화
			String encPwd=passwordEncoder.encode(user.getPassword());
			log.info("encPwd ==> "+encPwd);
			user.setPassword(encPwd);
			
			//Role 설정
			user.setRole("ROLE_USER");//지금은 앞부분에 ROLE_를 지정해주지 않는다!!
			
			User savedUser = userRepository.save(user);
			log.info("savedUser ==>"+savedUser);
			return savedUser;
		}
		
		@Transactional(readOnly = true)
		public User findByUserId(String userId) {
			User rUser=userRepository.findByUserId(userId);
			log.info("rUser ==>"+rUser);
			return rUser;
			//if(rUser==null || rUser.equals("")) return "사용가능합니다.";
			//else return "중복입니다.";
		}
		@Transactional
		public int createSendEmail(String email) throws MessagingException {
			System.out.println("createSendEmail start");
			int number = (int)(Math.random() * (90000)) + 100000;
			log.info(number+"");
			String senderEmail = "gxc9706@gmail.com";
			MimeMessage message = javaMailSender.createMimeMessage();
			message.setFrom(senderEmail);
	        message.setRecipients(MimeMessage.RecipientType.TO, email);
	        message.setSubject("이메일 인증");
	        String body = "";
	        body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
	        body += "<h1>" + number + "</h1>";
	        body += "<h3>" + "감사합니다." + "</h3>";
	        message.setText(body,"UTF-8", "html");
	        
	        javaMailSender.send(message);
			
			return number;
		}
		
		@Transactional
		public String findUserId(User user) {
			log.info("name"+user.getName());
			log.info("phone"+user.getPhone());			
			User rUser = userRepository.findByNameAndPhone(user.getName(), user.getPhone());
			if(rUser != null) {
				log.info("rUser ="+ rUser.toString());
				String userId = rUser.getUserId();
				return userId;
			}else {
				return null;
			}
			
		}
		/*
		@Transactional
		public int updatePassword(String password, String userId) {
			User rUser = userRepository.findByUserId(userId);
			if(rUser != null) {
				log.info("rUser="+rUser.toString());
				String encPass= passwordEncoder.encode(password);
				rUser.setPassword(encPass);
				
				return 1;
			}
			return 0;
		}
		*/
		@Transactional
		public User updateUser(User user) {
			
			User rUser = userRepository.findByUserId(user.getUserId());
			if(rUser != null) {
				if(user.getPassword() != null && ! user.getPassword().equals("")) {
					rUser.setPassword(passwordEncoder.encode(user.getPassword()));					
				}
				if(user.getPhone() != null&& ! user.getPhone().equals("")) {
					rUser.setPhone(user.getPhone());
				}
			}else {
				throw new BoardSearchNotException("db에 해당하는 유저가 존재하지 않습니다.", "updateUser 실행 중 에러 발생");
			}
			return rUser;
		}
		
		@Transactional
		public int deleteUser(String userId) {
			userRepository.deleteByUserId(userId);
			return 1;
		}
	
		
	
	
}
