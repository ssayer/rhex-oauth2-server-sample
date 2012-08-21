package org.mitre.rhex.model;


public class Client {
	
	private String clientId;
	private String jwkUrl;
	
	public Client(String clientId, String jwkUrl, String currentToken) {
		super();
		this.clientId = clientId;
		this.jwkUrl = jwkUrl;
	}
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getKeyUrl() {
		return jwkUrl;
	}

	public void setKeyUrl(String jwkUrl) {
		this.jwkUrl = jwkUrl;
	}
	
}
