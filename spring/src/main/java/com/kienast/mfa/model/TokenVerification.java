package com.kienast.mfa.model;

public class TokenVerification {
	private String token;
	private String secret;
	
	public TokenVerification(String token, String secret) {
		super();
		this.token = token;
		this.secret = secret;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}

}
