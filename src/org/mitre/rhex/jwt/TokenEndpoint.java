package org.mitre.rhex.jwt;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;
import org.mitre.rhex.exception.InvalidRequestException;
import org.mitre.rhex.exception.InvalidTokenException;
import org.mitre.rhex.exception.MalformedTokenException;
import org.mitre.rhex.exception.ServerException;
import org.mitre.rhex.key.ClientPublicKeyGateway;
import org.mitre.rhex.model.Token;
import org.mitre.rhex.service.TokenService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@SuppressWarnings("unused")
@Controller
@RequestMapping("/oauth/token")
public class TokenEndpoint {
	
	private final static Logger logger = Logger.getLogger(TokenEndpoint.class);
	private final static String ASSERTION_TYPE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
	private final static String GRANT_TYPE = "client_credentials";

	@Autowired
	TokenService tokenService;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public String handshake(HttpServletRequest req, @RequestParam(value="grant_type") String grantType, @RequestParam(value="client_id") String clientId, 
			@RequestParam(value="client_assertion") String tokenString, @RequestParam("client_assertion_type") String assertionType) {
		
		if (!assertionType.equals(ASSERTION_TYPE)) {
			logger.warn("CLIENT [" + clientId + "]: " + "Did not specify a valid client assertion type");
			throw new InvalidRequestException();
		}

		if (!grantType.equals(GRANT_TYPE)) {
			logger.warn("CLIENT [" + clientId + "]: " + "Did not specify a valid grant type");
			throw new InvalidRequestException();	
		}
		
		if (tokenService.verifyClientToken(tokenString, clientId, req.getRequestURL().toString())) {
			Token token = tokenService.createToken(clientId);
		     return token.getValue();
		    
		} else {
			logger.warn("CLIENT [" + clientId + "]: " + "Invalid token signature " + tokenString);
			throw new InvalidRequestException();
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public void verify(@RequestParam("client_id") String clientId, @RequestParam("token") String token) {
		if (tokenService.getToken(clientId, token) == null) {
			throw new InvalidTokenException();
		}
		
	}
	

}

