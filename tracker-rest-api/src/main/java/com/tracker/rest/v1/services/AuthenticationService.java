/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.v1.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tracker.commons.dtos.AuthRefreshRequest;
import com.tracker.commons.dtos.AuthRequest;
import com.tracker.commons.dtos.AuthResponse;
import com.tracker.config.WebConfig;
import com.tracker.rest.security.TokenAuthenticationService;
import com.tracker.services.impls.UserService;
import com.tracker.services.utils.AuthenticationManagerExtended;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(WebConfig.BASE_REST_API_V1 + "/auth")
@Api(value = WebConfig.BASE_REST_API_V1 + "/auth", tags = { "Authentication Servie" })
@Slf4j
public class AuthenticationService {

	@Autowired
	private AuthenticationManagerExtended authenticationManagerExtended;

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Autowired
	private UserService userService;

	@ApiOperation(value = "Authenticate the user")
	@PostMapping("/login")
	public AuthResponse login(@RequestBody AuthRequest auth,
			HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		
		log.trace("Login method called..");

		val authentication = authenticationManagerExtended
				.authenticate(new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));
		
		return tokenAuthenticationService.generateToken(authentication); 
	}
	
	@ApiOperation(value = "Refresh the token")
	@PostMapping("/refresh")
	public AuthResponse refresh(@RequestBody AuthRefreshRequest authRefresh,
			HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		
		log.trace("Refresh token method called..");
		
		val user = tokenAuthenticationService.breakToken(authRefresh.getToken());
		return tokenAuthenticationService.generateToken(user); 
	} 

	/*@ApiOperation(value = "Forgot password")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "email", value = "User email", required = true, dataType = "String", paramType = "query"), })
	@GetMapping("/forgotPassword")
	public void forgotPassword(@RequestParam(required = true, value = "email") final String email) {
		userService.forgotPassword(email);
	}
*/
}
