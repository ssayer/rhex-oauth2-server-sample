package org.mitre.rhex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerException extends RuntimeException {

	private static final long serialVersionUID = -2782536407042164607L;

	public ServerException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
