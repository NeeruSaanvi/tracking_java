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
package com.tracker.services.emails;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ContactUsEmailTemplate extends EmailTemplate {

	public String formatMessage(String bodyStr) {
		String body = "<br><br> <b>Dear Coinxoom," + "<br><br>" + " " + bodyStr + "";
		return addStandardHeaderFooters(body);
	}

	@Override
	public String formatMessage() {
		return "";
	}
}
