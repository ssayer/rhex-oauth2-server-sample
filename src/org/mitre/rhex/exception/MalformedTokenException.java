package org.mitre.rhex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MalformedTokenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5432667894751238814L;

	public MalformedTokenException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MalformedTokenException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
