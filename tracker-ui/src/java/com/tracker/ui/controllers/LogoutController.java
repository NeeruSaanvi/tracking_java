package com.tracker.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogoutController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(LogoutController.class);

	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {

		log.info("Logout controller has been called...going to clear the User session..");

		HttpSession session = request.getSession();
		if (session != null) {
			session.invalidate();
		}

		SecurityContextHolder.getContext().setAuthentication(null);

		log.info("User session has been cleared and forwarding the user to the login page");

		return new ModelAndView("redirect:/");
	}

}
