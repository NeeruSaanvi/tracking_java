package com.tracker.ui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tracker.commons.Constants;

public class BaseController {
	
	public void clearErrors(final HttpServletRequest request) {
		request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);  
		request.removeAttribute(Constants.ERROR_ATTRIBUTE);  
		request.removeAttribute(Constants.MESSAGE_ATTRIBUTE); 
	}
	 
//	@ExceptionHandler(MyApplicationException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public String handleAppException(MyApplicationException ex) {
//	  return ex.getMessage();
//	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleAppException(Exception ex) {
	  return ex.getMessage();
	}
}
