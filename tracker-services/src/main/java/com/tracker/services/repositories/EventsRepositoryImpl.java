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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.Events;
import com.tracker.services.dto.PaginationRequestDTO;

@Repository
@Transactional
public class EventsRepositoryImpl implements EventsRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Events> getEventsList(Long companyID, PaginationRequestDTO dto) {
		
		Query nativeQuery = buildQuery(companyID, dto, false);
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<Events> tList = new ArrayList<Events>();
		
		for (Object[] obj : list) {
			
			Events yt = new Events();
			yt.setEventid(Long.parseLong(obj[0].toString()));
			yt.setEventtitle(obj[1].toString());
			String date = obj[6].toString();
	        LocalDate localDate = LocalDate.parse(date);
			yt.setEventStartDate(localDate);
			yt.setVenueaddress(obj[3].toString());
			yt.setAttendanceCount(Integer.parseInt(obj[10].toString()));
			yt.setFirstLastname(obj[21].toString());
			yt.setEventAttachment(obj[17] == null ? "" : obj[17].toString());
			
			tList.add(yt);
			
		}

		return tList;
		
	}
	
	@Override
	public Integer getEventsListCount(Long companyID, PaginationRequestDTO dto) {
		Query nativeQuery = buildQuery(companyID, dto, true);
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		Integer count = 0;
		if(list != null) {
			count = list.size();
		}
        return count;
	}
	
	private Query buildQuery(Long companyID, PaginationRequestDTO dto, Boolean count) {
		 
		String queryStr = "SELECT e.*, u.FirstLastname FROM events e  " + 
				"				LEFT JOIN users u ON u.userid=e.ownerid  " + 
				"				LEFT JOIN company_ref cr ON cr.userid=u.userid  " + 
				"				WHERE cr.companyID=:companyID " + 
				"				AND (find_in_set('all',e.sharedTO) OR find_in_set(:shareTO,e.sharedTO))";
		
		String shareTO = Long.toString(companyID);
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		nativeQuery.setParameter("shareTO", shareTO);
		
		if(!count) {
			nativeQuery.setFirstResult((dto.getPage() - 1) * dto.getSize());
			nativeQuery.setMaxResults(dto.getSize());
		}
		
		return nativeQuery;
	}  
	

}
