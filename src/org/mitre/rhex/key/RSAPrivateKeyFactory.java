package org.mitre.rhex.key;

import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public class RSAPrivateKeyFactory extends AbstractFactoryBean<RSAPrivateKey> {

	KeyStore keyStore;
	
	String keyAlias;
	String keyPassword;
	
	public KeyStore getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	@Override
	protected RSAPrivateKey createInstance() throws Exception {		
		return (RSAPrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());
	}

	@Override
	public Class<RSAPrivateKey> getObjectType() {
		return RSAPrivateKey.class;
	}

}

