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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.User; 

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

	public User findByEmail(String email);
	
	public User findByEmailAndStatus(String username, String status);
	
	public List<User> findByUserIdIn(List<Long > userId);
	
	public User findByUserId(Long userId);

	public void deleteByUserId(Long userId);

	public User findByPasswordResetToken(String token);

	public Page<User> findAll(Pageable pageRequest);
	
	public User findByCompanyIdAndUserId(Integer companyId, Long userId);
	
	public List<User> findByCompanyId(Integer companyId);
	
	
	//public Long countAll();

	//public void deleteByFirstNameContaining(String firstName);
	

}
