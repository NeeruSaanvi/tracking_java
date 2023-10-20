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
package com.tracker.commons;

public class Constants {

	public static final Long SYSTEM_DEFAULT_USER_ID = 1l;
	
	public static final String ETHOS_ACCEPT_MEDIA_TYPE = "application/vnd.hedtech.v1+json";
	
	public static final String JASYPT_ENCRYPTOR_ID = "STRING_ENCRYPTOR";

	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	public static final String REMEMBER_ME_PARAM_NAME = "rememberMe";

	public static final String AUTH_HEADER_NAME = "Authorization"; 
	public static final String ON_ERROR_GOTO_PAGE_REQUEST_ATTRIBUTE = "ON_ERROR_GOTO_PAGE_REQUEST_ATTRIBUTE"; 
	public static final String SYSTEM_EXCEPTION_MESSAGE = "Internal Server Error Occured";
	public static final String MESSAGE_ATTRIBUTE = "MESSAGE_ATTRIBUTE";
	public static final String ERROR_ATTRIBUTE = "ERROR_ATTRIBUTE";

	public static final String INVALID_PAGE_VALUE = "Invalid page number provided";
	public static final String INVALID_PAGE_SIZE = "Invalid page size provided";

	public static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@#_^!%*?&])[A-Za-z\\d$@#_^!%*?&]{8,}";
}
