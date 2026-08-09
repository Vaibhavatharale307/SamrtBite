package com.smartbite.mainservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
	
	// JWT token used for authenticating future requests
	private String token;
	
	//user's email who is logged in
	private String email;
	
	//role asssigned to user
	private String role;
	
	// userId needed by frontend to call wallet and order APIs
	private Long userId;
	
	// user's full name for display
	private String name;

	private String phone;

	private Long canteenId;
	
	private String message;

}
