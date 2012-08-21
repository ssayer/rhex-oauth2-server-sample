package org.mitre.rhex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5606496095440598350L;

	public InvalidTokenException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidTokenException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
