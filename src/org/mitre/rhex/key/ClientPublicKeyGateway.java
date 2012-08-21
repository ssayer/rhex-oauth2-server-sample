package org.mitre.rhex.key;

import java.security.interfaces.RSAPublicKey;

import org.springframework.cache.annotation.Cacheable;

public interface ClientPublicKeyGateway {

	public RSAPublicKey getPublicKey(String clientId);

}