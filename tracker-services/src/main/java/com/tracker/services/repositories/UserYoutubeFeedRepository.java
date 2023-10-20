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
package com.tracker.services.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.UserYoutubeFeed1; 

@Repository
@Transactional
public interface UserYoutubeFeedRepository extends JpaRepository<UserYoutubeFeed1, Long>{
	
	public UserYoutubeFeed1 findById(String id);
	
	public List<UserYoutubeFeed1> findByVideoIdIn(List<String> ids);
}
