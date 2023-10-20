/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception exception)
			throws Exception {


	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView)
			throws Exception {

	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

		/*final HandlerMethod handlerMethod = (HandlerMethod) handler;

		final String emailAddress = request.getParameter("emailaddress");
		final String password = request.getParameter("password");

		if(StringUtils.isEmpty(emailAddress) || StringUtils.containsWhitespace(emailAddress) ||
				StringUtils.isEmpty(password) || StringUtils.containsWhitespace(password)) {
			throw new Exception("Invalid User Id or Password. Please try again.");
		}
		 */
		return true;
	}


}
