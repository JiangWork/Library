package com.klatencor.klara.future.support;

public class ConversionException extends Exception {

	public ConversionException() {
		
	}
	
	public ConversionException(String message) {
		super(message);
	}
	
	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}
}
