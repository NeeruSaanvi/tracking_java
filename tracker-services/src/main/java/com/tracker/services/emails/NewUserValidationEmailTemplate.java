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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tracker.commons.models.User;

@Component
@Scope("prototype")
public class NewUserValidationEmailTemplate extends EmailTemplate {

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

			final String emailValidationdLink =  super.getBaseURL() + "/emailValidation?token="; // + /*urlCodec.encode(*/user.getPasswordResetToken(/*)*/);

			String body = "<br><br> <b>Dear " + user.getFirstLastName() + "</b>," +
					"<br><br> Thank you for choosing Coinxoom.com. In addition to providing you with high-quality, " +
					"low-cost services, we are committed to safeguarding your personal account information and protecting your privacy." + 

					"<br><br> <a href='" + emailValidationdLink + "'> Click here to validate your account. </a>" +

					"<br><br> You can also copy and paste the URL into your browser if above link does not work." +
					"<br><br> " + emailValidationdLink + "<br><br> ";

			return addStandardHeaderFooters( body);

		} 
		catch (Exception e) {
			return "";
		} 

	} 
}
