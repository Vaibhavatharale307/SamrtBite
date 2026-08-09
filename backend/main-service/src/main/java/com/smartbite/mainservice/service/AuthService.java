package com.smartbite.mainservice.service;

import com.smartbite.mainservice.dto.AuthResponse;
import com.smartbite.mainservice.dto.LoginRequest;
import com.smartbite.mainservice.dto.LoginResponse;
import com.smartbite.mainservice.dto.RegisterRequest;

public interface AuthService {
	
	AuthResponse register(RegisterRequest request);
	
	LoginResponse login(LoginRequest request);
	
	
	

}
