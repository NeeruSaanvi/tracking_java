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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.Client; 

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> { 
	List<Client> findAllByActiveTrue();
	 
	@Query(value = "select * from clients c "
			+ " join users us on us.campus_id = c.campus_id"
			+ " where us.user_id = :userId and c.active = true and u.active = true and us.active = true ", nativeQuery = true)
	Client findByUserId(@Param("userId") Long userId);
	
	Client findByClientIdAndActiveTrue(Long orgId);
	
	Page<Client> findByNameIgnoreCaseContainingAndActiveTrue(String name, Pageable page);
	Long countByNameIgnoreCaseContainingAndActiveTrue(String name);
	
	Client findByNameAndActiveTrue(String name);
	
	List<Client> findByActiveTrue();
}
 
