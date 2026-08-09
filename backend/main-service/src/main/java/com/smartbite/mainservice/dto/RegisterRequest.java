package com.smartbite.mainservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import com.smartbite.mainservice.entity.RoleType;

/*
 * to receive registrationn details
 */
@Getter
@Setter
public class RegisterRequest {

	@NotBlank(message = "Name is required")
	@Size(min =4, max =55 , message = "Name should be between 3 and 50 characters")
	private String name;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid Email format")
	private String email;
	
	@NotBlank(message = "Password is Required")
	@Size(min=6, max =20, message = "Password must be between 6 and 20 characters")
	private String password;
	
	@NotBlank
	@Pattern(
			regexp = "^[6-9]\\d{9}$",
			message ="Phone Number must contain 10 digits and begin with 6--9"
			)
	private String phone;

	// Students do not need a canteen. Managers must select one while registering.
	private RoleType role = RoleType.STUDENT;

	private Long canteenId;
	
}
