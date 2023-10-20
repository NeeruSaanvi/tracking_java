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

import com.tracker.commons.models.CompanyRef;
import com.tracker.commons.models.User; 

@Repository
@Transactional
public interface CompanyRefRepository extends JpaRepository<CompanyRef, Long> {
	
	public CompanyRef findByUseridAndCompanyId(Long userId, Long companyId);
	
	public List<CompanyRef> findByCompanyId(Long companyId);
	
	public List<User> findByUseridIn(List<Long > userId);
	
	public Long countByUserid(Long userId);
}
