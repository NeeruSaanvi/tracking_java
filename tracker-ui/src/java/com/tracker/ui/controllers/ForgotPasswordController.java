package com.tracker.ui.controllers;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.Constants;
import com.tracker.commons.models.User;
import com.tracker.services.dto.CreateUserDTO;
import com.tracker.services.impls.UserService;
import com.tracker.services.utils.AuthenticationManagerExtended;
import com.tracker.ui.security.CustomUsernamePasswordAuthenticationToken;

import lombok.extern.slf4j.Slf4j;

@Controller
@SuppressWarnings("unused")
@Slf4j
public class ForgotPasswordController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManagerExtended authManager;

	@RequestMapping("/forgotPassword")
	public String login() {
		log.info("Forgot password page has been called, loading the page..");

		return "forgotPassword";
	}

	@RequestMapping("/forgotPasswordSendEmail")
	public @ResponseBody String forgotPasswordSendEmail(final HttpServletRequest request) {
		log.info("Forgot password service is called..");

		userService.forgotPassword(request.getParameter("email"));

		return "success"; 
	}

	@RequestMapping("/resetForgottenPassword")
	public ModelAndView resetForgottenPassword(@RequestParam(value = "token") final String token, final HttpServletRequest request) {
		log.info("Reset forgotten password page has been called, loading the page..");
		
		clearErrors(request);

		request.setAttribute(Constants.ON_ERROR_GOTO_PAGE_REQUEST_ATTRIBUTE, "forgotPassword");

		final User user = userService.findUserByResetPasswordToken(token);

		final ModelAndView mv = new ModelAndView();
		mv.setViewName("resetForgottenPassword");
		mv.addObject("user", user);
		 
		return mv; 
	}

	@PostMapping("/resetForgottenPasswordSubmit")
	public @ResponseBody String resetForgottenPasswordSubmit(@ModelAttribute("createUser") CreateUserDTO createUser) {
		log.info("Reset forgotten password SAVE has been called, saving the new password of the user..");

		User user = userService.resetPasswordByToken(createUser);

		final List<GrantedAuthority> grantedAuthorities = new LinkedList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority(AuthenticationManagerExtended.FACEBOOK_AUTH)); 

		Authentication authentication = new CustomUsernamePasswordAuthenticationToken(user.getEmail(), createUser.getPassword(), new LinkedList<GrantedAuthority>());
		authentication = authManager.authenticate(authentication);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "success"; 
	}
	
}
