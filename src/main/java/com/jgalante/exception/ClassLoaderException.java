package com.jgalante.exception;

public class ClassLoaderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClassLoaderException(Exception e) {
		super(e);
	}

	public ClassLoaderException(String message) {
		super(message);
	}

	public ClassLoaderException(String message, Throwable cause) {
		super(message, cause);
	}

}
