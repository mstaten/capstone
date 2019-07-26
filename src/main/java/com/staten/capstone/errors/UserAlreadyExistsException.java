package com.staten.capstone.errors;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException() {}

    public UserAlreadyExistsException(final String message, final Throwable cause) {
        super(message,cause);
    }

    public UserAlreadyExistsException(final String message) {
        super(message);
    }

    public UserAlreadyExistsException(final Throwable cause) {
        super(cause);
    }
}
