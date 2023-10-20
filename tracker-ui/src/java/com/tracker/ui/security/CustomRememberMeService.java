package com.tracker.ui.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;

import com.tracker.commons.Constants;

public class CustomRememberMeService implements RememberMeServices {
 
	public CustomRememberMeService() {

	}
 
	@Override
	public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
		if(request.getParameter(Constants.REMEMBER_ME_PARAM_NAME) != null) {
			String val = request.getParameter(Constants.REMEMBER_ME_PARAM_NAME);

			if(val.equals("true")) {

			}
		}
		return null;
	}

	@Override
	public void loginFail(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		// TODO Auto-generated method stub

	} 

}

