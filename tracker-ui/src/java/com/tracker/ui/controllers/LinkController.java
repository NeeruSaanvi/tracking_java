package com.tracker.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LinkController extends BaseController {

	@RequestMapping("/link1")
	public String link() {

		log.info("Login controller has been called...");
		
		//log.info("adfsd : " + request.getRequestURI());

		return "link";
	}

}
