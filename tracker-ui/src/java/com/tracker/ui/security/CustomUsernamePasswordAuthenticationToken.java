package com.tracker.ui.security;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials); 
	}

	public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials, List<GrantedAuthority> grantedAuthorities) {
		super(principal, credentials, grantedAuthorities); 
	}

	@Override
	public void eraseCredentials() {
		//do nothing..
	}


}
