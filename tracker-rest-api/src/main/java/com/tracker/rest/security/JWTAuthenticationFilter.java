/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.tracker.rest.converters.JsonWrapper;
import com.tracker.rest.exceptions.JsonView;
import com.tracker.services.utils.AuthenticationManagerExtended;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationFilter extends GenericFilterBean {

	private TokenAuthenticationService tokenAuthenticationService;
	private AuthenticationManagerExtended authManager; 

	@Override
	public void doFilter(final ServletRequest serRequest, final ServletResponse serResponse, final FilterChain filterChain)
			throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) serRequest;
		final HttpServletResponse response = (HttpServletResponse) serResponse;

		if(tokenAuthenticationService == null) {
			final ServletContext servletContext = request.getServletContext();
			final WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			tokenAuthenticationService = webApplicationContext.getBean(TokenAuthenticationService.class);
		}

		if(authManager == null) {
			final ServletContext servletContext = request.getServletContext();
			final WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			authManager = webApplicationContext.getBean(AuthenticationManagerExtended.class);
		}

		try {
			Authentication authentication = tokenAuthenticationService.getAuthentication(request);
			//authentication = authManager.authenticate(authentication);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
		}
		catch(final AuthenticationException e) {
			log.debug("Error while authenticating token:  " + e.getMessage());

			@SuppressWarnings("rawtypes")
			val res = new JsonWrapper();
			res.setSuccess(false);	
			res.setMessage(e.getMessage());

			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			JsonView.Render(res, response);
		}
		catch(final Exception e) {

			log.error("Error while authenticating token ", e);

			@SuppressWarnings("rawtypes")
			val res = new JsonWrapper();
			res.setSuccess(false);	
			res.setMessage("Interal Server Error Occured");	

			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			JsonView.Render(res, response);
		}
	}
}
