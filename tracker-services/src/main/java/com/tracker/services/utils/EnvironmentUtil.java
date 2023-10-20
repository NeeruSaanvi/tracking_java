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

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentUtil {

	public static final String DEFAULT = "default";
	public static final String DEV = "development";
	public static final String TEST = "test";
	public static final String UNIT_TEST = "unit-test";
	public static final String STAGING = "staging";
	public static final String PROD = "production";

	@Autowired
	private Environment environment;

	public boolean isDefault() {
		return containsEnv(DEFAULT);
	}

	public boolean isDev() {
		return containsEnv(DEV);
	}

	public boolean isTest() {
		return containsEnv(TEST);
	}

	public boolean isUnitTest() {
		return containsEnv(UNIT_TEST);
	}

	public boolean isStaging() {
		return containsEnv(STAGING);
	}

	public boolean isProd() {
		return containsEnv(PROD);
	}
	
	public String getActiveProfile() {
		for(String prof : environment.getActiveProfiles()) {
			return prof;
		}
		return DEFAULT;
	}

	public boolean containsEnv(String profile) {

		if(Arrays.stream(environment.getActiveProfiles())
				.anyMatch( env -> (env.equalsIgnoreCase(profile)) ))  {
			return true;
		}

		return false;
	}
	
	public String getBaseUrl() {
		if(isDefault() || isDev() || isTest() || isUnitTest() || isStaging()) {
			return "http://localhost:7073";
		}
		
		if(isProd()) {
			return "https://www.studentportal.com";
		}
		
		return "http://localhost:7073";
	}
}
