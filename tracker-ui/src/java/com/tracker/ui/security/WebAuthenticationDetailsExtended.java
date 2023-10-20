package com.tracker.ui.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class WebAuthenticationDetailsExtended extends WebAuthenticationDetails { 

	private static final long serialVersionUID = 1L;
	HttpServletRequest request;

	public WebAuthenticationDetailsExtended(HttpServletRequest request) {
		super(request); 
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	} 
}
