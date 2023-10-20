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

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.UserProAppDetails;
import com.tracker.commons.models.UserProAppDetailsSports; 

@Repository
@Transactional
public interface UserProAppDetailsSportsRepository extends JpaRepository<UserProAppDetailsSports, Long> {

	public UserProAppDetailsSports findByUserIdAndCompanyId(Long userId, Long companyId);
	public List<UserProAppDetails> findByCompanyId(Long companyId);
	public void deleteByUserId(Long userId);
	public List<UserProAppDetails> findByUserId(Long userId);
	public List<UserProAppDetails> findByUserIdIn(Collection<Long> userIds);
	
	public void deleteByUserIdAndCompanyId(Long userId, Long companyId);
}
