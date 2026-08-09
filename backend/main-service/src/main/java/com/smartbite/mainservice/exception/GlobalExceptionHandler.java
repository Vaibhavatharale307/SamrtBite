package com.smartbite.mainservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.smartbite.mainservice.dto.ErrorResponsE;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponsE> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponsE(ex.getMessage()));
    }

    @ExceptionHandler(FoodUnavailableException.class)
    public ResponseEntity<ErrorResponsE> handleFoodUnavailable(FoodUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponsE(ex.getMessage()));
    }

    @ExceptionHandler(OrderCancellationException.class)
    public ResponseEntity<ErrorResponsE> handleOrderCancellation(OrderCancellationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponsE(ex.getMessage()));
    }

    @ExceptionHandler(SlotFullException.class)
    public ResponseEntity<ErrorResponsE> handleSlotFull(SlotFullException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponsE(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponsE> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponsE("Access Denied: " + ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponsE> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponsE(message));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponsE> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponsE(ex.getMessage()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponsE> handlePayment(PaymentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponsE(ex.getMessage()));
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<String> handleRestClientResponse(RestClientResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ex.getResponseBodyAsString());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponsE> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponsE("Unexpected server error: " + ex.getMessage()));
    }
}
