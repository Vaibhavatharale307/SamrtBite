package com.smartbite.authservice.service;

import com.smartbite.authservice.dto.AuthResponse;
import com.smartbite.authservice.dto.LoginRequest;
import com.smartbite.authservice.dto.LoginResponse;
import com.smartbite.authservice.dto.RegisterRequest;

public interface AuthService {
	
	AuthResponse register(RegisterRequest request);
	
	LoginResponse login(LoginRequest request);
	
	
	

}
