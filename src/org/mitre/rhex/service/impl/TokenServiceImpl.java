package org.mitre.rhex.service.impl;

import java.security.interfaces.RSAPrivateKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;
import org.mitre.rhex.exception.InvalidTokenException;
import org.mitre.rhex.exception.MalformedTokenException;
import org.mitre.rhex.exception.ServerException;
import org.mitre.rhex.key.ClientPublicKeyGateway;
import org.mitre.rhex.model.Token;
import org.mitre.rhex.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.nimbusds.jwt.ClaimsSet;
import com.nimbusds.jwt.Header.Type;
import com.nimbusds.jwt.JWA;
import com.nimbusds.jwt.JWSException;
import com.nimbusds.jwt.JWSHeader;
import com.nimbusds.jwt.JWTException;
import com.nimbusds.jwt.SignedJWT;

@Service
public class TokenServiceImpl implements TokenService {

	private static final int EXPIRES_SECONDS = 24 * 60 * 60;
	private Logger logger = Logger.getLogger(TokenService.class);
	private static final RowMapper<Token> tokenRowMapper = new RowMapper<Token>() {

		@Override
		public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Token(rs.getString("client_id"), rs.getString("value"), rs.getLong("expiration"));
		}};
	
	@Autowired(required=true)
	RSAPrivateKey serverKey;
	
	@Autowired(required=true)
	ClientPublicKeyGateway clientKeyGateway;

	@Autowired(required=true)
	JdbcTemplate jdbcTemplate;
	
	@Override
	public Token createToken(String clientId) {
	  	JWSHeader h = new JWSHeader(JWA.RS256);
        h.setType(Type.JWT);
        
        
        long expiration = (new Date().getTime() / 1000) + EXPIRES_SECONDS ;
        JSONObject claims = new JSONObject();
        claims.put("iss", "http://healthinfonet.org");
        claims.put("prn", clientId);
        claims.put("jti", UUID.randomUUID());
        claims.put("exp", expiration);
        SignedJWT jwt = new SignedJWT(h, new ClaimsSet(claims));
        
        String value = null;
        try {
        	
			jwt.rsaSign(serverKey);
	        value = jwt.serialize();
	        
	        
	        jdbcTemplate.update("DELETE FROM TOKENS WHERE CLIENT_ID = ?", clientId);
	        jdbcTemplate.update("INSERT INTO TOKENS (CLIENT_ID, VALUE, EXPIRATION) VALUES (?, ?, ?)", clientId, value, expiration);
	        
		} catch (JWSException e) {
			logger.error("CLIENT " + clientId +": Unable to sign token");
			throw new ServerException(e);
		} catch (JWTException e) {
			logger.error("CLIENT " + clientId +": Unable to create token");
			throw new ServerException(e);
		} 
        
		return new Token(clientId, value, expiration);
	}
	
	public boolean verifyClientToken(String tokenString, String clientId, String url) {
		try {
			
			
			SignedJWT sJwt = SignedJWT.parse(tokenString);
			
			boolean validSignature = sJwt.rsaVerify(clientKeyGateway.getPublicKey(clientId));
			boolean validClaims = verifyClaims(sJwt.getClaimsSet(), url, clientId);
			
			
			return validClaims && validSignature;

		} catch (JWTException e) {
			logger.warn("CLIENT [" + clientId + "]: " + "Unable to parse token", e);
			throw new MalformedTokenException(e);
		}  catch (JWSException e) {
			logger.error("CLIENT [" + clientId + "]: " +  "Unable to verify token signature", e);
			throw new ServerException(e);
		}
	}
	
	private boolean verifyClaims (ClaimsSet claimSet, String currentUrl, String clientId) {
		
		JSONObject claims  = claimSet.toJSONObject();
		String audience = (String) claims.get("aud");
		Integer expiration = (Integer) claims.get("exp");
		String issuer = (String) claims.get("iss");
		String principal = (String) claims.get("prn");
		
		if (audience == null || !audience.equals(currentUrl)) {
			logger.warn("Invalid Audience: " + audience);
		} else if (expiration == null || expiration < currentTimeInSeconds()) {
			logger.warn("Beyond expiration date for authorization request: " + new Date(expiration));
		} else if (issuer == null || !issuer.equals(clientId)) {
			logger.warn("Issuer not same as client identification: " + issuer);
		} else if (principal == null || !principal.equals(clientId)) {
			logger.warn("Principal claims not same as client identification: " + principal);
		} else {
			return true;
		}
		
		return false;
	}

	private long currentTimeInSeconds() {
		return new Date().getTime() / 1000;
	}


	@Override
	public Token getToken(String clientId, String value) {
		try {
			logger.debug("FINDING TOKEN");
			Token token = jdbcTemplate.queryForObject("SELECT * FROM TOKENS WHERE TOKENS.VALUE = ? && TOKENS.EXPIRATION > ?", tokenRowMapper, value, (new Date().getTime() / 1000));
			if (token == null || (!clientId.equals(token.getClientId()))) {
				logger.warn("CLIENT [" + clientId + "]: " +  " does not have a valid access token " + value);
				throw new InvalidTokenException();
			} 
			return token;
		} catch(EmptyResultDataAccessException e) {
			logger.warn("Couldn't find token with value = " + value + " and expiration > " + (new Date().getTime() / 1000));
			return null;
		}
	}

}
