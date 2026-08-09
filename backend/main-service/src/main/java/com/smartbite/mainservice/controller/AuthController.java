package com.smartbite.mainservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbite.mainservice.dto.AuthResponse;
import com.smartbite.mainservice.dto.LoginRequest;
import com.smartbite.mainservice.dto.LoginResponse;
import com.smartbite.mainservice.dto.RegisterRequest;
import com.smartbite.mainservice.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class AuthController {
	
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request)
    { 
    	return ResponseEntity.ok(authService.register(request)); 
    	
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request)
    {
    	return ResponseEntity.ok(authService.login(request)); 
    	
    }
    
    
    @GetMapping("/test") 
    public String test() 
    { 
    	return "Jwt working Successfully"; 
    	
    }
}
