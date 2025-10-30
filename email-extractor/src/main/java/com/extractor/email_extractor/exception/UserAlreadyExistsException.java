package com.extractor.email_extractor.exception;

public class UserAlreadyExistsException extends RuntimeException {

    // Constructor to accept a custom error message
    public UserAlreadyExistsException(String message) {
        super(message); // Pass the message to the parent RuntimeException
    }
}
