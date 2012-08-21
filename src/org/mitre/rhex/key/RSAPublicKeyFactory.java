package org.mitre.rhex.key;

import java.security.KeyStore;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public class RSAPublicKeyFactory extends AbstractFactoryBean<RSAPublicKey>{

	KeyStore keyStore;
	String keyAlias;
	
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

	@Override
	protected RSAPublicKey createInstance() throws Exception {
		return (RSAPublicKey) keyStore.getCertificate(keyAlias).getPublicKey();
	}

	@Override
	public Class<RSAPublicKey> getObjectType() {
		return RSAPublicKey.class;
	}

}
