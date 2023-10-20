package com.tracker.ui.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;

import com.tracker.commons.Constants;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	public static final Logger LOG = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc)
			throws IOException, ServletException {

		if(exc instanceof InvalidCsrfTokenException) {
			request.setAttribute(Constants.ERROR_MESSAGE, "Your page has expired. Please refresh your page and try again.");
		}

		//That means request was made from the browser using AJAX and we must return JSON output
		if (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) { 
			request.getRequestDispatcher("/accessDeniedRest").forward(request, response);
		}
		else {
			request.getRequestDispatcher("/accessDenied").forward(request, response);
		} 

	}
}