package com.smartbite.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbite.authservice.dto.AuthResponse;
import com.smartbite.authservice.dto.LoginRequest;
import com.smartbite.authservice.dto.LoginResponse;
import com.smartbite.authservice.dto.RegisterRequest;
import com.smartbite.authservice.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	/*
	 * Endpoint used for user registration
	 */
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
		return ResponseEntity.ok(authService.register(request));
	}
	
	/*
	 * Endpoint used for user login
	 */
	@PostMapping("/login")
	
		public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
			
			return ResponseEntity.ok(authService.login(request));
			
		
		}
	
	@GetMapping("/test")
	public String test() {
		return "Jwt working Successfully";
	}
	

}

