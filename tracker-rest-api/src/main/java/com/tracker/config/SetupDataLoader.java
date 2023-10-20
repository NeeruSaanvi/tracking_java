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
package com.tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.Constants;
import com.tracker.commons.UserTypeEnum;
import com.tracker.commons.models.User;
import com.tracker.services.repositories.UserRepository;
import com.tracker.services.utils.PasswordUtil;
 

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository; 

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		if (alreadySetup) {
			return;
		}
/*
		if(userRepository.findByEmailAndActiveTrue("viralpatel79@yahoo.com") == null) {
			
			final User user = new User();
			user.setDeaults();
			user.setUserId(Long.valueOf(1));
			user.setFirstName("Viral");
			user.setLastName("Patel");
			user.setEmail("viralpatel79@yahoo.com");
			user.setPhone("404-578-2670");
			user.setUserType(UserTypeEnum.ADMIN);
			user.setPasswordSalt(PasswordUtil.generatePassword(30));
			user.setPassword(PasswordUtil.encryptPassword(user.getPasswordSalt() + "password"));
			user.setEmailValidated(true); 
			user.setCreatedBy(Constants.SYSTEM_DEFAULT_USER_ID);
			user.setModifiedBy(Constants.SYSTEM_DEFAULT_USER_ID);
			 
			user.setDeaults();
			
			userRepository.save(user);  
			
			for(int i=1; i < 10; i++) {
				final User u = new User();
				user.setDeaults();
				u.setUserId(Long.valueOf(i + 1));
				u.setFirstName("Viral" + i);
				u.setLastName("Patel" + i);
				u.setEmail(i + "viralpatel79@yahoo.com" + 1);
				u.setPhone("404-578-2670");
				u.setUserType(UserTypeEnum.ADMIN);
				u.setPasswordSalt(PasswordUtil.generatePassword(30));
				u.setPassword(PasswordUtil.encryptPassword(u.getPasswordSalt() + "password"));
				u.setEmailValidated(true);
				u.setCreatedBy(Constants.SYSTEM_DEFAULT_USER_ID);
				u.setModifiedBy(Constants.SYSTEM_DEFAULT_USER_ID);
				
				u.setDeaults();
	
				userRepository.save(u);  
			}
		}  
*/
		alreadySetup = true;

	}
}
