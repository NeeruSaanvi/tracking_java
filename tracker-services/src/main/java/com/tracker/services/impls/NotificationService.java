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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.Notification;
import com.tracker.services.repositories.NotificationRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	public List<Notification> getNotificationByOwner(Long ownerId) {
		List<Notification> result = notificationRepository.findByOwnerId(ownerId);
		
		return result;
	}

	public Notification findNotificationById(final Long id) {

		log.debug("Finding the Notification by id:" + id);

		if (id == null) {
			throw new UserException("Invalid id");
		}

		val notification = notificationRepository.getOne(id);

		if (notification == null) {
			throw new UserException("No Notification found by this id");
		}

		return notification;
	}

	public Notification updateTemplate(Notification notification) {
		if (notification.getId() == null) {
			throw new UserException("Please provide blog id to be updated");
		}

		final Notification dbId = findNotificationById(notification.getId());
		if (dbId == null) {
			throw new UserException("No Notification found by this id.");
		}

		dbId.setSubject(notification.getSubject());
		dbId.setBodytext(notification.getBodytext());
		dbId.setSignature(notification.getSignature());
		dbId.setStatus(notification.getStatus());
		
		if(StringUtils.isNotBlank(notification.getAttachment())) {
			dbId.setAttachment(notification.getAttachment());
		}
		
		if(StringUtils.isNotBlank(notification.getAttachment1())) {
			dbId.setAttachment1(notification.getAttachment1());
		}
		
		if(StringUtils.isNotBlank(notification.getAttachment2())) {
			dbId.setAttachment2(notification.getAttachment2());
		}

		return notificationRepository.save(dbId);
	}

}
