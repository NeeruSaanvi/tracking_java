/*******************************************************************************
 * Copyright (c) 2018 by M and M Softwares LLC.                                    
 * All rights reserved.                                                       
 *                                                                              
 * This software is the confidential and proprietary information of 
 * M and M Softwares LLC ("Confidential Information"). 
 * You shall not disclose such confidential Information and shall 
 * use it only in accordance with  the terms of the license agreement 
 * you entered with M and M Softwares LLC.
 ******************************************************************************/
package com.tracker.services.utils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tracker.commons.Constants;
import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.User;
import com.tracker.services.repositories.UserRepository;

@Service
public class CommonUsersUtil {

	@Autowired
	private UserRepository userRepository;

	@SuppressWarnings("unchecked")
	public Map<String, Object> getSessionsMap() {
		return (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getCredentials();
	} 

	public Long getLoggedInUserIdElseThrowException() {
		try {
			return getLoggedInUser().getUserId();
		}
		catch(Exception e) {
			throw new UserException("User must be logged in.");
		}
	}

	public Long getLoggedInUserIdElseDefaultUserId() {
		try {
			return getLoggedInUser().getUserId();
		}
		catch(Exception e) {
			return Constants.SYSTEM_DEFAULT_USER_ID;
		}
	}

	public User getLoggedInUser() {
		try {
			return (User) getSessionsMap().get("USER");
		}
		catch(Exception e) {
			try {
				String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
				return userRepository.findByEmail(userEmail);
			}
			catch(Exception e1) {
				return null;
			}
		}
	}

}
