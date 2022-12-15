package com.mjt.condo.exceptions;

public class UserNotAuthenticatedException extends Exception{
    private static final long serialVersionUID = 1L;

	public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
