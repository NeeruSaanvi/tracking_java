/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.security;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.commons.dtos.AuthResponse;
import com.tracker.commons.models.User;
import com.tracker.services.utils.AuthenticationManagerExtended;
import com.tracker.services.utils.UserUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenAuthenticationService {

	@Value("${auth-token.expiration-in-milliseconds}")
	private final long expirationTime = (1000 * 60 * 60 * 24 * 365); // 5 years

	@Value("${auth-token.secret}")
	private String secret;
	
	@Autowired
	private ObjectMapper objectMapper;

	private static final String TOKEN_PREFIX = "Bearer";
	public static final String HEADER_STRING = "Authorization";

	@SuppressWarnings("unchecked")
	public AuthResponse generateToken(final Authentication auth) throws JsonProcessingException {

		val sessionMap = ((Map<String, Object>) auth.getPrincipal());
		val user = (User) sessionMap.get(SessionVariables.USER);

		return generateToken(user);
	}

	public AuthResponse generateToken(final User user) throws JsonProcessingException {
		
		val payload = objectMapper.writeValueAsString(user);	
		val expTime = new Date(System.currentTimeMillis() + expirationTime);
		val expTimeZoned = ZonedDateTime.ofInstant(expTime.toInstant(), /*UserUtils.getUserZoneId()*/ ZoneId.systemDefault());
		
		val JWT = Jwts.builder()
				.setSubject(user.getEmail())
				.setExpiration(expTime)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setIssuer("Tracker")
				.claim("scope", user.getMemType())
				.claim(SessionVariables.USER, payload)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();

		return new AuthResponse(TOKEN_PREFIX + " " + JWT, expTimeZoned) ;
	}

	public Authentication getAuthentication(final HttpServletRequest request) {
		val token = request.getHeader(HEADER_STRING);
		val user = breakToken(token);

		final List<GrantedAuthority> grantedAuthorities = new LinkedList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority(AuthenticationManagerExtended.FACEBOOK_AUTH));
		
		val sessionMap = new HashMap<String, Object>();
		sessionMap.put(SessionVariables.USER, user);

		return new UsernamePasswordAuthenticationToken(sessionMap, user.getEmail(), grantedAuthorities);
	}

	public User breakToken(String token) {

		log.debug(String.format("Validating Token '%s'", token));

		if (token != null) {

			try {
				// parse the token.
				val claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token.replace(TOKEN_PREFIX, "").trim())
						.getBody();

				val userJson = (String) claims.get(SessionVariables.USER);
				if (userJson == null) {
					log.debug("Token '{}' has no user information hence deemed invalid", token);
					throw new BadCredentialsException("Invalid authentication token presented.");
				}
				
				log.debug("Token '{}' has been successfully validated", token);
				try {
					return objectMapper.readValue(userJson, User.class);
				} catch (final Exception e) {
					log.error("Token User payload '{}' is not invalid", userJson, e);
					throw new BadCredentialsException("Invalid authentication token presented.");
				}
				 
			} catch (final Exception e) {
				log.debug("Token '{}' is deemed invalid", token);
				throw new BadCredentialsException("Invalid authentication token presented.");
			}
		}

		throw new BadCredentialsException("Invalid authentication token presented.");
	}
}
