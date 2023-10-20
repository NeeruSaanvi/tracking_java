package com.tracker.ui.springboot;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class HttpsRedirectionFilter extends GenericFilterBean {

	private static final String HTTP = "http";
	private static final String HTTPS = "https://";
	private static final String X_FORWARDED_PROTO = "x-forwarded-proto";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String protocol = httpRequest.getHeader(X_FORWARDED_PROTO);
		
		if (HTTP.equalsIgnoreCase(protocol)) {
			String redirectURL = HTTPS + httpRequest.getServerName() + httpRequest.getRequestURI();
			httpResponse.sendRedirect(redirectURL);
		} else {
			filterChain.doFilter(request, response);
		}
	}
}