/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.tracker.commons.Constants;
import com.tracker.commons.exceptions.UserException;
import com.tracker.rest.converters.JsonWrapper;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(final HttpServletRequest request, final HttpServletResponse response,
			final Exception exception) throws Exception {

		@SuppressWarnings("rawtypes")
		val res = new JsonWrapper();
		res.setSuccess(false);

		String message = "";
		if (exception instanceof UserException) {
			log.debug(String.format("User exception occured: %s ", exception.getMessage()));
			message = exception.getMessage();
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} 
		else if (exception instanceof AuthenticationException) {
			log.debug("Authentication failed: " + exception.getMessage());
			message = exception.getMessage();
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		} 
		else if (exception instanceof NoHandlerFoundException) {
			log.debug(String.format("No handler found exception: %s ", exception.getMessage()));
			message = exception.getMessage();
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} 
		else {
			message = Constants.SYSTEM_EXCEPTION_MESSAGE;
			log.error("System exception occured: ", exception);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		res.setMessage(message);
		return JsonView.Render(res, response);

	}
}
