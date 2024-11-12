package com.web.spring.controller;

import java.io.Console;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.spring.entity.User;
import com.web.spring.exception.BoardSearchNotException;
import com.web.spring.security.CustomUserDetails;
import com.web.spring.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserService userService;
	@CrossOrigin
	@PostMapping("/users")
	public ResponseEntity<?> signUp(@RequestBody User user) {
		log.info("user signUp==>",user);
		User rUser = userService.signUp(user);
		return ResponseEntity
						.status(201)
							.body(rUser);
	}
	//duplicateCheck
		@GetMapping("/users/{id}")
		public String duplicateCheck(@PathVariable String id) {
			log.info("duplicateCheck id==>",id);
			return userService.duplicateCheck(id);
		}
	@PostMapping("/mail")
	@ResponseBody
	public String SendEmail(@RequestParam String email) throws Exception {
		int number = userService.createSendEmail(email) ;
		String num = number+"";
		
		return num;
	}
	
	//요청명 미정
	@PostMapping("/findId")
	public ResponseEntity<?> findUserId(@RequestBody User user) {
		log.info("user = "+user);
		String userId = userService.findUserId(user);
		log.info("userId="+userId);
		if(userId == null) {
			throw new BoardSearchNotException("해당하는 id가 존재하지 않습니다.", "findId Error");
		}else {
			return ResponseEntity
					.status(200)
						.body(userId);
		}
		
	}
	/*
	 * 사전에 이메일 인증 받는다. 
	 * 성공 시 이메일 입력 칸을 수정 불가능하게 바꾼다.
	 * 패스워드 입력칸과 재확인 입력칸이 일치하는지 확인한다. 
	 * 버튼 누르면 /updatePass를 이메일 입력칸의 값과 패스워드 입력칸의 값을 가지고 axiox로 호출한다.
	 * result 값이 0이면 실패 1이면 성공 
	 */
	@PostMapping("/updatePass")
	public ResponseEntity<?> updatePassword(@RequestParam String password, @RequestParam String userId){
		log.info("password="+password);
		log.info("userId="+userId);
		
		int result = userService.updatePassword(password, userId);
		
		if(result ==0) {
			throw new BoardSearchNotException("해당하는 id가 존재하지 않습니다.", "updatePassword Error");
		}else {
			return ResponseEntity
					.status(200)
						.body(result);
		}
	}
	
	
}
