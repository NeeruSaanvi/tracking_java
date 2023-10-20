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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.dtos.SportsUserProAppDetails;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserStore;
import com.tracker.services.dto.PaginationRequestDTO;

@Repository
@Transactional
public class UserStoreRepositoryImpl implements UserStoreRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<UserStore> getUserStoreList(Long companyID, PaginationRequestDTO dto) {
		
		Query nativeQuery = buildQuery(companyID, dto, false);
		
		@SuppressWarnings("unchecked")
		List<UserStore> list = nativeQuery.getResultList();
		
		//List<UserStore> tList = new ArrayList<UserStore>();
		
		/*
		 * for (Object[] obj : list) {
		 * 
		 * Web yt = new Web(); yt.setBlogId(Long.parseLong(obj[0].toString()));
		 * yt.setBlogname(obj[2].toString()); yt.setHeadline(obj[3].toString());
		 * yt.setLink(obj[4].toString()); String date = obj[5].toString(); String
		 * dateStr[] = date.split(" "); LocalDate localDate =
		 * LocalDate.parse(dateStr[0]); yt.setPostedDate(localDate);
		 * yt.setFirstLastname((obj[8].toString()));
		 * 
		 * tList.add(yt);
		 * 
		 * }
		 */

		return list;
		
	}
	
	@Override
	public Integer getUserStoreListCount(Long companyID, PaginationRequestDTO dto) {
		Query nativeQuery = buildQuery(companyID, dto, true);
		@SuppressWarnings("unchecked")
		List<UserStore> list = nativeQuery.getResultList();
		Integer count = 0;
		if(list != null) {
			count = list.size();
		}
        return count;
	}
	
	private Query buildQuery(Long companyID, PaginationRequestDTO dto, Boolean count) {
		 
		String queryStr = "select us.* FROM"
				+ "		user_store us"
				+ "		where store_userID in ("
				+ "			SELECT u.userid"
				+ "		    FROM users u"
				+ "			LEFT JOIN company_ref cr ON cr.userid=u.userid"
				+ "		WHERE cr.companyID = :companyID	)";
		
		//Query nativeQuery = em.createNativeQuery(queryStr);
		Query nativeQuery = em.createNativeQuery(queryStr, UserStore.class);
		nativeQuery.setParameter("companyID", companyID);
		
		if(!count) {
			nativeQuery.setFirstResult((dto.getPage() - 1) * dto.getSize());
			nativeQuery.setMaxResults(dto.getSize());
		}
		
		return nativeQuery;
	}  
	
	

}
