package com.smartbite.mainservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
/*
 * DTO used to send error response to client
 */
public class ErrorResponsE {
	
	private String message;

}
