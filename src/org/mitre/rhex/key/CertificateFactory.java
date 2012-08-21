package org.mitre.rhex.key;


import java.security.KeyStore;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public class CertificateFactory extends AbstractFactoryBean<X509Certificate>{

	private KeyStore keyStore;
	private String alias;
	
	
	public KeyStore getKeyStore() {
		return keyStore;
	}
	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@Override
	protected X509Certificate createInstance() throws Exception {
		return (X509Certificate) keyStore.getCertificate(alias);
	}
	@Override
	public Class<X509Certificate> getObjectType() {
		return X509Certificate.class;
	}
	
	
	
}
