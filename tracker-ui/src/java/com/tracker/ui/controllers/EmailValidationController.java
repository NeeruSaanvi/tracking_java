package com.tracker.ui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tracker.commons.Constants;
import com.tracker.services.impls.UserService;

@Controller
public class EmailValidationController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(ForgotPasswordController.class);

	@SuppressWarnings("unused")
	@Autowired
	private UserService userService;

	@RequestMapping("/emailValidation")
	public String emailValidation(@RequestParam(value = "token") final String token, final HttpServletRequest request) {
		log.info("Email validation page has been called, validating the email and token provided..");

		request.setAttribute(Constants.ON_ERROR_GOTO_PAGE_REQUEST_ATTRIBUTE, "home");

		request.setAttribute(Constants.MESSAGE_ATTRIBUTE, "You email has been validated. Please login to use the portal.");

		return "login";
	} 

}
