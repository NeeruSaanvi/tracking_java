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
package com.tracker.commons.exceptions;

@SuppressWarnings("serial")
public class SystemException extends RuntimeException {

	public SystemException(String message, Exception exception) {
		super(message);
		super.setStackTrace(exception.getStackTrace());
	}
	
	public SystemException(Exception exception) {
		super(exception); 
	}


	public SystemException(String message) {
		super(message);
	}

	public SystemException() {
	}
}
