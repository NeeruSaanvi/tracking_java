package com.tracker.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.ui.exceptions.JsonView;
import com.tracker.ui.exceptions.RestErrorResponse;
import com.tracker.ui.utils.UserUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/")
@Slf4j
public class RootController {

	@GetMapping("/")
	public String redirectWithUsingRedirectPrefix(final ModelMap model) {
		log.info("Root controller called...checking if User is authenticated..");

		if(UserUtils.isUserLoggedIn()) {
			log.info("User is already authenticated and hence being forwarded to the Dashboard");
			return "dashboard";//SocialLoginController.REDIRECT_SUCCESSFUL_LOGIN;
		}
		else {
			log.info("User session has either expired or User is not authenticated and hence being forwarded to the home page");
			return "login";
		}
	} 

	@GetMapping("/loginFailure")
	public ModelAndView loginFailure(final ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		log.info("Login failure controller called...");

		String message = "";
		if(request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) { 		   					 
			message = ((Exception) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)).getMessage() ;
		}

		final RestErrorResponse res = new RestErrorResponse();
		res.setStatus("fail"); 
		res.setMessage(message); 
		response.setStatus(HttpStatus.OK.value());
		return JsonView.Render(res, response);
	} 

}