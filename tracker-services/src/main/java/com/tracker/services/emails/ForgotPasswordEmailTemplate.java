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

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tracker.commons.models.User;

@Component
@Scope("prototype")
public class ForgotPasswordEmailTemplate extends EmailTemplate {

	private User user; 

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String formatMessage() {

		try {
			@SuppressWarnings("unused")
			final URLCodec urlCodec = new URLCodec();

			final String passwordLink =  super.getBaseURL() + "/resetForgottenPassword?token=" + urlCodec.encode(user.getPasswordResetToken());

			String body = "<br><br> <b>Dear " + StringUtils.capitalize(user.getFirstLastName()) + "</b>," +
					"<br><br> To reset your password please click on the following link." +

					   "<br><br> <a href='" + passwordLink + "'>  " +
					   "Reset password link" +
					   " </a>" +

					   "<br><br> You can also copy and paste the URL into your browser if above link does not work." +
					   "<br><br> " + passwordLink + "<br><br> ";

			return addStandardHeaderFooters( body);

		} 
		catch (Exception e) {
			return "";
		} 

	} 
}
