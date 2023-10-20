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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserStore;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.UserRepository;
import com.tracker.services.repositories.UserStoreRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserStoreService {

	@Autowired
	private UserStoreRepository userStoreRepository;
	
	@Autowired
	private UserRepository userRepository;

	public List<UserStore> findAll(final PaginationRequestDTO dto) {
		return userStoreRepository.findAll(dto.getRequest()).getContent();
	}
	
	public List<UserStore> getUserStoreList(Long companyId, PaginationRequestDTO dto){
		return userStoreRepository.getUserStoreList(companyId, dto);
	}
	
	public int getUserStoreListCount(Long companyId, PaginationRequestDTO dto){
		return userStoreRepository.getUserStoreListCount(companyId, dto);
	}
	
	public UserStore findUserStoreById(final Long userStoreId) {

		log.debug("Finding the web by id:" + userStoreId);

		if (userStoreId == null) {
			throw new UserException("Please provide user store id");
		}

		val userStore = userStoreRepository.findByStoreId(userStoreId);

		if (userStore == null) {
			throw new UserException("No Store found by this id");
		}else {
			User user = userRepository.findByUserId(userStore.getStoreUserID());
			if(user != null) {
				userStore.setFirstLastname(user.getFirstLastName());
			}
		}

		return userStore;
	}
	 

}
