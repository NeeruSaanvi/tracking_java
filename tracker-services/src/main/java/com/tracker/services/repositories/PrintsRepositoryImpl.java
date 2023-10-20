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

import com.tracker.commons.models.Prints;
import com.tracker.services.dto.PaginationRequestDTO;

@Repository
@Transactional
public class PrintsRepositoryImpl implements PrintsRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Prints> getPrintList(Long companyID, PaginationRequestDTO dto) {
		
		Query nativeQuery = buildQuery(companyID, dto, false);
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<Prints> tList = new ArrayList<Prints>();
		
		for (Object[] obj : list) {
			
			Prints yt = new Prints();
			yt.setPrintId(Long.parseLong(obj[0].toString()));
			yt.setFirstLastname(obj[8].toString());
			String date = obj[6].toString();
			String dateStr[] = date.split(" ");
	        LocalDate localDate = LocalDate.parse(dateStr[0]);
			yt.setPrintIssueDate(localDate);
			yt.setPrintPublishName(obj[2].toString());
			yt.setPrintArtName(obj[3].toString());
			
			tList.add(yt);
			
		}

		return tList;
		
	}
	
	@Override
	public Integer getPrintListCount(Long companyID, PaginationRequestDTO dto) {
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
		 
		String queryStr = "Select DISTINCT * from (Select up.print_id,up.userid as userid, up.print_publish_name, " + 
				"		        up.print_art_name, up.print_cov_type, up.print_img_path, up.print_issue_date, 'PM' as print, u.FirstLastname from user_print up " + 
				"						LEFT JOIN users u on up.userid=u.userid " + 
				"						LEFT JOIN company_ref cr on up.userid=cr.userid " + 
				"						where cr.companyID=:companyID " + 
				"						AND (find_in_set('all',up.sharedTO) or find_in_set(:shareTO,up.sharedTO)) " + 
				"						UNION " + 
				"						Select DISTINCT uc.id as print_id,uc.user_id as userid, uc.source AS print_publish_name, uc.headline as print_art_name, " + 
				"						uc.sourceType AS print_cov_type, uc.link as print_img_path, uc.created_date as print_issue_date, 'CS' as print, u.FirstLastname " + 
				"						from user_customscoops uc " + 
				"						LEFT JOIN users u on uc.user_id=u.userid " +
				"						where uc.sourceType='Magazine' AND find_in_set(:shareTO,uc.CS_sponsorID)  " + 
				"						) a";
		
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
