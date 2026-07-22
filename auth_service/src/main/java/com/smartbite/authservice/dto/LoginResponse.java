package com.smartbite.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginResponse {
	
	
	// JWT token used for authenticating future requests
	private String token;
	
	//user's email who is logged in
	private String email;
	
	//role asssigned to user
	private String role;
	
	
	private String message;

}
