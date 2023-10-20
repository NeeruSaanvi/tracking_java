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

import com.tracker.commons.models.Notification; 

@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom{

	public List<Notification> findByOwnerId(Long ownerid);
	
	public Notification findByOwnerIdAndType(Long ownerId, String type);
	
	public Notification findByOwnerIdAndTypeAndStatus(Long ownerId, String type, String status);
	
}
