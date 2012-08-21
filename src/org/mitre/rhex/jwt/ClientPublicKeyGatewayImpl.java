package org.mitre.rhex.jwt;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import org.apache.log4j.Logger;
import org.mitre.rhex.exception.MalformedTokenException;
import org.mitre.rhex.exception.ServerException;
import org.mitre.rhex.key.ClientPublicKeyGateway;
import org.mitre.rhex.model.Client;
import org.mitre.rhex.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.support.RestGatewaySupport;

import com.nimbusds.jwt.JWK;
import com.nimbusds.jwt.JWK.AlgorithmFamily;
import com.nimbusds.jwt.JWKException;
import com.nimbusds.jwt.RSAKey;

@Service
public class ClientPublicKeyGatewayImpl extends RestGatewaySupport implements ClientPublicKeyGateway {
	
	private static final Logger logger = Logger.getLogger(ClientPublicKeyGateway.class);
	
	@Autowired
	ClientService clientService;
	
	@Override
	@Cacheable("clientKeys")
	public RSAPublicKey getPublicKey(String clientId) {
		Client details = clientService.getClient(clientId);
		String jwkUrl = details.getKeyUrl();
		logger.debug("CLIENT [" + clientId + "]: " + "retrieving public key");
		String key = getRestTemplate().getForObject(jwkUrl, String.class);
		KeyFactory keyFactory;
		RSAPublicKey publicKey = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			JWK jwk = JWK.parse(key);
			if (jwk.getAlgorithmFamily() == AlgorithmFamily.RSA) {
				RSAKey rsaJwk = (RSAKey) jwk;
				BigInteger mod = new BigInteger(rsaJwk.getModulus().decode());
				BigInteger exp = new BigInteger(rsaJwk.getExponent().decode());
			
				publicKey = (RSAPublicKey) keyFactory.generatePublic(new RSAPublicKeySpec(mod, exp));
			}
	
		} catch (NoSuchAlgorithmException e) {
			logger.error("No such algorithm", e);
			throw new ServerException(e);
		} catch (InvalidKeySpecException e) {
			logger.error("Invalid Key", e);
			throw new ServerException(e);
		} catch (JWKException e) {
			logger.error("CLIENT [" + clientId + "]: " + " JWK was malformed");
			throw new MalformedTokenException(e);
		}
		
		return publicKey;
	}
	
}
