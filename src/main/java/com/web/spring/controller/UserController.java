package com.web.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.spring.entity.User;
import com.web.spring.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserService userService;
	
	@PostMapping("/users")
	public ResponseEntity<?> signUp(@RequestBody User user) {
		log.info("user signUp==>",user);
		userService.signUp(user);
		return ResponseEntity
						.status(201)
							.body("Register OK~~!!");
	}
	//duplicateCheck
		@GetMapping("/users/{id}")
		public String duplicateCheck(@PathVariable String id) {
			log.info("duplicateCheck id==>",id);
			return userService.duplicateCheck(id);
		}
	
}
