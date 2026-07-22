package com.smartbite.authservice.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartbite.authservice.dto.AuthResponse;
import com.smartbite.authservice.dto.LoginRequest;
import com.smartbite.authservice.dto.LoginResponse;
import com.smartbite.authservice.dto.RegisterRequest;
import com.smartbite.authservice.entity.Role;
import com.smartbite.authservice.entity.RoleType;
import com.smartbite.authservice.entity.User;
import com.smartbite.authservice.repository.RoleRepository;
import com.smartbite.authservice.repository.UserRepository;
import com.smartbite.authservice.security.JwtService;


import lombok.RequiredArgsConstructor;

/**
 * Service class responsible for handling
 * user authentication related operations.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	// Repository used to perform CRUD operations on User table
	private final UserRepository userRepository;
	
	// Repository used to fetch roles from Role table
	private final RoleRepository rolerepository;
	
	// Used to hash passwords before storing them in database
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	//generating jwt tokens
	private final JwtService jwtservice;

	/*
	 * registration of new user 
	 * 
	 * @param request registration details
	 * @return response => success
	 */
	@Override
	public AuthResponse register(RegisterRequest request) {
		
		  // Check if user already exists with given email
		if(userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		
		// Fetch STUDENT role from database
		Role studentRole = rolerepository
				.findByRoleName(RoleType.STUDENT)
				.orElseThrow(()-> new RuntimeException("Student role Not Found"));
		
		// Create new User entity object
		User user = new User();
		
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		// Assign STUDENT role to newly registered user
		user.setRole(studentRole);
		userRepository.save(user);
		
		
		return new AuthResponse("User Registered Successfully");
		
	}
	
	
	

	@Override
	public LoginResponse login(LoginRequest request) {
		
		//Fetch user using email
		
		User user = userRepository
				.findByEmail(request.getEmail())
				.orElseThrow(()->
				new RuntimeException("User not found"));
		
		//Comparing row password with encrypted password stored in database
		
		boolean isPasswordValid = passwordEncoder.matches(request.getPassword(), user.getPassword());
		
		//Throw exception if password does not  match
		
		if(!isPasswordValid) {
			
			throw new RuntimeException("Invalid Credentials");
		}
		
		 // Generate JWT token for authenticated user
		String token = jwtservice.generateToken(user);
		
		//Return success response
		return new LoginResponse(token , user.getEmail(), user.getRole().getRoleName().name(),"Login Success");
		
		
		
	
		
		
	}

}
