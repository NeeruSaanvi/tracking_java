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

import com.tracker.commons.models.UserTeam; 

@Repository
@Transactional
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

	public List<UserTeam> findByTeamId(Long teamId);
	
	public List<UserTeam> findByUserId(Long userId);
	
	public void deleteByTeamId(Long teamId);
	
	public void deleteByUserId(Long userId);

}
