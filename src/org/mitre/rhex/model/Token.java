package org.mitre.rhex.model;


public class Token {
	
	public Token(String clientId, String value, long expiration) {
		super();
		this.clientId = clientId;
		this.value = value;
		this.expiresAt = expiration;
	}

	String clientId;
	long expiresAt;
	String value;

	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public long getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	
	

}
