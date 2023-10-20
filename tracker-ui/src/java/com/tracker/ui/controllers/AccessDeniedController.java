package com.tracker.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.Constants;
import com.tracker.ui.exceptions.JsonView;
import com.tracker.ui.exceptions.RestErrorResponse;

@Controller
public class AccessDeniedController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(AccessDeniedController.class);

	@RequestMapping("/accessDenied")
	public String accessDenied() {
		log.debug("Access Denied controller called...");
		return "accessDenied";
	} 

	@RequestMapping("/accessDeniedRest")
	public ModelAndView accessDeniedRest(final ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		log.debug("Access Denied REST controller called...");

		String message = "Access has been denied";
		if(request.getAttribute(Constants.ERROR_MESSAGE) != null) { 		   					 
			message = ((String) request.getAttribute(Constants.ERROR_MESSAGE));
		}

		final RestErrorResponse res = new RestErrorResponse();
		res.setStatus("fail"); 
		res.setMessage(message); 
		response.setStatus(HttpStatus.OK.value());
		return JsonView.Render(res, response);
	} 

}
