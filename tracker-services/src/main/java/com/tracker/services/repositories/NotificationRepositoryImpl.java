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

import com.tracker.commons.models.Web;
import com.tracker.services.dto.PaginationRequestDTO;

@Repository
@Transactional
public class NotificationRepositoryImpl implements WebRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Web> getWebPostList(Long companyID, PaginationRequestDTO dto) {
		
		Query nativeQuery = buildQuery(companyID, dto, false);
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<Web> tList = new ArrayList<Web>();
		
		for (Object[] obj : list) {
			
			Web yt = new Web();
			yt.setBlogId(Long.parseLong(obj[0].toString()));
			yt.setBlogname(obj[2].toString());
			yt.setHeadline(obj[3].toString());
			yt.setLink(obj[4].toString());
			String date = obj[5].toString();
			String dateStr[] = date.split(" ");
	        LocalDate localDate = LocalDate.parse(dateStr[0]);
			yt.setPostedDate(localDate);
			yt.setFirstLastname((obj[8].toString()));
			
			tList.add(yt);
			
		}

		return tList;
		
	}
	
	@Override
	public Integer getWebPostListCount(Long companyID, PaginationRequestDTO dto) {
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
		 
		String queryStr = "Select DISTINCT * from  " + 
				"			    (Select ub.blogid,ub.userid as userid, ub.blogname, 'NA' as headline,ub.link, ub.posted_date as posted_date, 0 AS visitors, 'bb' as WB,u.FirstLastname  " + 
				"			  	from user_blog ub " + 
				"			  	LEFT JOIN users u on ub.userid=u.userid " + 
				"			  	LEFT JOIN company_ref cr on ub.userid=cr.userid " + 
				"			  	where cr.companyID=:companyID  " + 
				"			  	AND (find_in_set('all',ub.sharedTO) or find_in_set(:shareTO,ub.sharedTO)) " + 
				"					UNION " + 
				"			  	Select uc.id as blogid,uc.user_id as userid, uc.source as blogname, uc.headline as headline, uc.link,  " + 
				"			  	uc.created_date as posted_date, uc.visitors as visitors,'CS' as WB,u.FirstLastname " + 
				"			  	from user_customscoops uc "+
				"				LEFT JOIN users u on uc.user_id=u.userid" + 
				"			  	where find_in_set(:shareTO,uc.CS_sponsorID)) a";
		
		String shareTO = Long.toString(companyID);
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		nativeQuery.setParameter("shareTO", shareTO);
		nativeQuery.setParameter("shareTO", shareTO);
		
		if(!count) {
			nativeQuery.setFirstResult((dto.getPage() - 1) * dto.getSize());
			nativeQuery.setMaxResults(dto.getSize());
		}
		
		return nativeQuery;
	}  
	

}
