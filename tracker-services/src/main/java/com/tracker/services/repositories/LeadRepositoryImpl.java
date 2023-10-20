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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.Lead;
import com.tracker.services.dto.PaginationRequestDTO;

@Repository
@Transactional
public class LeadRepositoryImpl implements LeadRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Lead> getLeadList(Long companyID, PaginationRequestDTO dto, Date fromDate, Date toDate, List<Long> userList) {
		
		Query nativeQuery = buildQuery(companyID, dto, false, fromDate, toDate, userList);
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<Lead> tList = new ArrayList<Lead>();
		
		for (Object[] obj : list) {
			
			Lead yt = new Lead();
			yt.setLeadId(Long.parseLong(obj[0].toString()));
			yt.setLeadDealName(obj[1] == null ? "" : obj[1].toString());
			yt.setLeadBuyEmail(obj[2] == null ? "" : obj[2].toString());
			yt.setLeadDelPhone(obj[3] == null ? "" : obj[3].toString());
			yt.setLeadDelAdd(obj[4] == null ? "" : obj[4].toString());
			yt.setLeadType(obj[5] == null ? "" : obj[5].toString());
			yt.setFirstLastname(obj[8] == null ? "" : obj[8].toString());
			yt.setLeadRel(obj[9] == null ? "" : obj[9].toString());
			
			String date = obj[10].toString();
			if(StringUtils.isNotBlank(date)) {
				String dateArray[] = date.split(" ");
				date = dateArray[0];
			}
	        LocalDate localDate = LocalDate.parse(date);
			yt.setPostedDate(localDate);
			
			String createdTimeFormatter = localDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			yt.setPostedDateFormatter(createdTimeFormatter);
			
			tList.add(yt);
		}

		return tList;
		
	}
	
	@Override
	public Integer getLeadListCount(Long companyID, PaginationRequestDTO dto, Date fromDate, Date toDate, List<Long> userList) {
		Query nativeQuery = buildQuery(companyID, dto, true, fromDate, toDate, userList);
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		Integer count = 0;
		if(list != null) {
			count = list.size();
		}
        return count;
	}
	
	private Query buildQuery(Long companyID, PaginationRequestDTO dto, Boolean count, Date fromDate, Date toDate, List<Long> userList) {
		 
		String queryStr = "SELECT ul.lead_id, ul.lead_deal_name, ul.lead_buy_email, ul.lead_del_phone, " + 
				" ul.lead_del_add, ul.lead_type, tr.ref_id, tr.status AS refstatus,u.FirstLastname, ul.lead_rel, ul.posted_date  FROM user_lead ul " + 
				"							LEFT JOIN users u ON u.userid=ul.userid " + 
				"							LEFT JOIN company_ref cr ON ul.userid=cr.userid " + 
				"							LEFT JOIN trans_reference tr ON tr.entityID = ul.lead_id AND tr.user_id = cr.companyID " + 
				"							WHERE cr.companyID=:companyID " + 
				"							AND tr.entityid is NULL AND (find_in_set('all',ul.sharedTO) OR find_in_set(:shareTO,ul.sharedTO))"+
				"							AND ul.posted_date between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d') ";
		if(userList.size() > 0) {
			queryStr += " AND ul.userid in (:userId)";
		}
		
		String shareTO = Long.toString(companyID);
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		nativeQuery.setParameter("shareTO", shareTO);
		nativeQuery.setParameter("fromDate", fromDate);
		nativeQuery.setParameter("toDate", toDate);
		
		if(userList.size() > 0) {
			nativeQuery.setParameter("userId", userList);
		}
		
		if(!count && dto != null) {
			nativeQuery.setFirstResult((dto.getPage() - 1) * dto.getSize());
			nativeQuery.setMaxResults(dto.getSize());
		}
		
		return nativeQuery;
	}  
	

}
