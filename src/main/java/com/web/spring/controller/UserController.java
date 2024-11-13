package com.web.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.web.spring.entity.User;
import com.web.spring.exception.BoardSearchNotException;
import com.web.spring.service.UserService;

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
		log.info("user signUp==>"+user);
		User rUser = userService.signUp(user);
		return ResponseEntity
						.status(201)
							.body(rUser);
	}
	//duplicateCheck
	@CrossOrigin
	@GetMapping("/users/{id}")
	public User findByUserId(@PathVariable String id) {
		log.info("findByUserId id==>",id);
		return userService.findByUserId(id);
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
	/*
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
	*/
	@PutMapping("/users")
	public ResponseEntity<?> updateUser(@RequestBody User user){
		System.out.println("user="+user);
		User rUser = userService.updateUser(user);
		System.out.println("rUser="+rUser);
		
		return ResponseEntity.status(200).body(rUser);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String userId){
		userService.deleteUser(userId);
		return ResponseEntity.status(200).body("성공");
	}
	
}
