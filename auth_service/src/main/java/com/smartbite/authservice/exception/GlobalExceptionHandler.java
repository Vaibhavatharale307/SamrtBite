package com.smartbite.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.smartbite.authservice.dto.ErrorResponsE;

/*
 * Handling Exceptions Globally
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponsE> handleRuntimeException(RuntimeException ex){
		
		ErrorResponsE response  = new ErrorResponsE(ex.getMessage());
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		
		}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponsE> handleValidtionException(MethodArgumentNotValidException ex){
		
		String errorMessage = ex.getBindingResult()
				.getFieldError()
				.getDefaultMessage();
		
		ErrorResponsE response = new ErrorResponsE(errorMessage);
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
