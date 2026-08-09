package com.smartbite.mainservice.exception;

public class SlotFullException extends RuntimeException {
    public SlotFullException(String message) {
        super(message);
    }
}
