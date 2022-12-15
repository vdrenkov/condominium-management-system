package com.mjt.condo.exceptions;

public class ApartmentNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	public ApartmentNotFoundException(String message) {
		super(message);
	}
}
