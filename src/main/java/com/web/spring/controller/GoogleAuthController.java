package com.web.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.web.spring.entity.User;
import com.web.spring.jwt.JWTUtil;
import com.web.spring.repository.UserRepository;
import com.web.spring.service.GoogleAuthService;
import com.web.spring.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class GoogleAuthController {
	
	@Autowired
	private JWTUtil jwtUtil;

    @Autowired
    private GoogleAuthService googleAuthService;
    @Autowired
    private UserRepository userRepository;

    

    // Step 2: Google에서 인증 후 리다이렉트된 URL에서 'code'를 받음
    @GetMapping("/google/code")
    public ResponseEntity<?> googleUserInfo(@RequestParam String code ) {
    	
    	System.out.println("google/callback 시작");
    	System.out.println("code = "+code);        
    	
    	String idToken = googleAuthService.exchangeCodeForToken(code);
    	System.out.println("idToken = "+idToken);
    	
    	DecodedJWT decodedJWT = JWT.decode(idToken);
    	
    	String email = decodedJWT.getClaim("email").asString();
    	String name = decodedJWT.getClaim("name").asString();
    	decodedJWT.getClaims().forEach((a,b)->{
    		System.out.println(a+"="+b);
    	});
    	System.out.println("email ="+email);
    	System.out.println("name ="+name);
    	User user = new User();
    	user.setUserId(email);
    	user.setName(name);
    	//user = googleAuthService.getExtraUserInfo(user);
    	
    	User rUser= userRepository.findByUserId(email);
    	if(rUser == null) {
    		System.out.println("rUser == null");
    		rUser = new User();    		
    		rUser.setName(name);
    		rUser.setUserId(email);    	
    	}
    	
    	System.out.println("rUser = "+rUser);
    	
    	return ResponseEntity.status(200).body(rUser); // or return JWT Token to client if needed
    	
    }
    @PostMapping("/google/token")
    public ResponseEntity<?> createToken(@RequestBody User user, HttpServletResponse response){
    	System.out.println("createToken start");
    	System.out.println("User = "+user);
    	String token = jwtUtil.createJwt(user, "ROLE_USER", 1000L*60*10L);
    	response.addHeader("Authorization", "Bearer " + token);
    	
    	return ResponseEntity.status(200).body(user);
    }
    
    
    
    
    
}
