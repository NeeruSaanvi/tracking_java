package com.tracker.ui.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.Constants;
import com.tracker.commons.exceptions.SystemException;
import com.tracker.commons.exceptions.UserException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
class GlobalExceptionHandler {

	private static final String DEFAULT_ERROR_VIEW = "error";
	private static final String DEFAULT_LOGIN_VIEW = "home";

	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response,
			Exception exception) throws Exception {

		//If header is present that means it's AJAX Call and must return JSON Response
		if (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {

			String message = "";
			if (exception instanceof AuthenticationException) {
				log.debug(String.format("User authentication failed: %s ", exception.getMessage()));
				message = exception.getMessage();	
			} 
			else if (exception instanceof UserException) {
				log.debug(String.format("User exception occured: %s ", exception.getMessage()));
				message = exception.getMessage();	
			} 
			else {
				message = Constants.SYSTEM_EXCEPTION_MESSAGE;
				log.error("System exception occured: ", exception);
			} 

			RestErrorResponse res = new RestErrorResponse();
			res.setStatus("fail"); 
			res.setMessage(message); 
			response.setStatus(HttpStatus.OK.value());
			return JsonView.Render(res, response);
		}
		else {
			final ModelAndView mav = new ModelAndView();

			Exception finalException = exception;
			if (exception instanceof AuthenticationException) {
				finalException = new UserException(exception.getMessage());
				mav.addObject("exception", finalException);
				mav.addObject("url", request.getRequestURL());
				mav.setViewName(DEFAULT_LOGIN_VIEW);

				return mav;
			}
			else if (exception instanceof UserException) {
				log.debug(String.format("User exception occured: %s ", exception.getMessage()));

				finalException = exception; 
			} 
			else {
				finalException = new SystemException(Constants.SYSTEM_EXCEPTION_MESSAGE);
				log.error("System exception occured: ", exception);

				finalException = new SystemException("Internal Server Error Occurred.");
			}

			if(request.getAttribute(Constants.ON_ERROR_GOTO_PAGE_REQUEST_ATTRIBUTE) != null) {
				mav.setViewName(((String) request.getAttribute(Constants.ON_ERROR_GOTO_PAGE_REQUEST_ATTRIBUTE)));
			}
			else {
				mav.setViewName(DEFAULT_ERROR_VIEW);
			}

			mav.addObject("exception", finalException);
			mav.addObject("url", request.getRequestURL());
			request.setAttribute(Constants.ERROR_ATTRIBUTE, finalException.getMessage());

			return mav;
		}
	}
}