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

import com.tracker.commons.models.Events;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.EventsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventsService {

	@Autowired
	private EventsRepository eventsRepository;

	
	public List<Events> getEventsList(Long companyId, PaginationRequestDTO dto){
		return eventsRepository.getEventsList(companyId, dto);
	}
	
	public int getEventsListCount(Long companyId, PaginationRequestDTO dto){
		return eventsRepository.getEventsListCount(companyId, dto);
	}
	

}
