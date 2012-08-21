package org.mitre.rhex.service;

import org.mitre.rhex.model.Token;

public interface TokenService {

	Token getToken(String client_id, String value);

	Token createToken(String clientId);

	boolean verifyClientToken(String tokenString, String clientId, String string);

}
