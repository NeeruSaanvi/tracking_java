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
package com.tracker.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.commons.models.UserPwdTrack;
import com.tracker.services.repositories.UserPwdTrackRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserPwdTrackService {

	@Autowired
	private UserPwdTrackRepository userPwdTrackRepository;


	
	/*
	 * public Web findWebById(final Long webId) {
	 * 
	 * log.debug("Finding the web by id:" + webId);
	 * 
	 * if(webId == null) { throw new UserException("Please provide web id"); }
	 * 
	 * val web = webRepository.findByBlogId(webId);
	 * 
	 * if(web == null) { throw new UserException("No web found by this id"); }
	 * 
	 * 
	 * return web; }
	 */
	public UserPwdTrack saveUserPwdTrack(UserPwdTrack userPwdTrack) { 
				
		return userPwdTrackRepository.save(userPwdTrack);
	}
	
	
	

}
