package com.smartbite.mainservice.exception;

public class FoodUnavailableException extends RuntimeException {
    public FoodUnavailableException(String message) {
        super(message);
    }
}
