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

import com.tracker.commons.models.Tournaments;
import com.tracker.services.dto.PaginationRequestDTO;

@Repository
@Transactional
public class TournamentsRepositoryImpl implements TournamentsRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Tournaments> getAllTournament(Long companyID, PaginationRequestDTO dto, Boolean count) {

		String queryStr = "SELECT DISTINCT u.userid,u.FirstLastname, t.treferenceName, t.tournamentTitle, t.tfinishedPlace, t.tournamentDate, "
				+ " t.tournamentid FROM tournaments t " + 
				"			LEFT JOIN users u on t.staffid=u.userid" + 
				"			LEFT JOIN company_ref cr on t.staffid=cr.userid" + 
				"			WHERE cr.companyID=:companyID" + 
				"			AND t.status='Active' and (find_in_set('all',t.sharedTO) or find_in_set(:shareTO,t.sharedTO))";
		
		String shareTO = Long.toString(companyID);
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		nativeQuery.setParameter("shareTO", shareTO);
		
		if(!count) {
			nativeQuery.setFirstResult((dto.getPage() - 1) * dto.getSize());
			nativeQuery.setMaxResults(dto.getSize());
		}
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<Tournaments> tList = new ArrayList<Tournaments>();
		
		for (Object[] obj : list) {
			
			Tournaments yt = new Tournaments();
			yt.setUserId(Long.parseLong(obj[0].toString()));
			yt.setTreferenceName(obj[2].toString());
			yt.setTournamentTitle(obj[3].toString());
			yt.setTfinishedPlace(obj[4].toString());
			
			String date = obj[5].toString();
			try {
				LocalDate localDate = LocalDate.parse(date);
				yt.setTournamentDate(localDate);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			tList.add(yt);
			
		}

		return tList;
	}
	

}
