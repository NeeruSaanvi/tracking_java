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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;

import com.tracker.commons.models.Client;
import com.tracker.commons.models.User;

import lombok.val;

@SuppressWarnings("unchecked")
public class UserUtils {
	
	public static Map<String, Object> getSessionsMap() {
		val auth = SecurityContextHolder.getContext().getAuthentication();
		return (Map<String, Object>) auth.getPrincipal();
	} 

	public static User getLoggedInUser() {
		return (User) getSessionsMap().get(SessionVariables.USER);
	}
	
	/*public static UserTypeEnum getLoggedInUserType() {
		return getLoggedInUser().getUserType();
	}
	
	public static boolean isAdminUser() {
		return getLoggedInUserType().equals(UserTypeEnum.ADMIN);
	} */
	
	public static Long getLoggedInUserId() {
		return getLoggedInUser().getUserId();
	}
	
	public static Client getClient() {
		return (Client) getSessionsMap().get(SessionVariables.CLIENT);
	}
	
	public static List<Client> getClients() {
		return (List<Client>) getSessionsMap().get(SessionVariables.CLIENTS);
	}
	
	public static Map<Long, Client> getClientsMap() {
		val map = new HashMap<Long, Client>();
		
		if(getClient() != null) {
			map.put(getClient().getClientId(), getClient());
		}
		
		if(getClients() != null) {
			getClients().forEach( uni -> {
				map.put(uni.getClientId(), uni);
			});
		}
		
		return map;
	}
	
	 
	 
	public static boolean isUserLoggedIn() {
		try {
			getLoggedInUserId();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/*
	public static ZoneId getUserZoneId() {
		return UserUtils.isUserLoggedIn()
				? ZoneId.of(TimeZoneEnum.findById(UserUtils.getLoggedInUser().getTimeZone()).getTzName())
				: ZoneId.systemDefault();
	}*/
}
