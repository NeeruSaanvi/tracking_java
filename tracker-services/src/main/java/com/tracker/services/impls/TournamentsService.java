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

import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.Tournaments;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.TournamentsRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TournamentsService {

	@Autowired
	private TournamentsRepository tournamentsRepository;


	public List<Tournaments> findAll(final PaginationRequestDTO dto) {
		return tournamentsRepository.findAll(dto.getRequest()).getContent();
	} 
	
	public List<Tournaments> getAllTournament(Long companyId, PaginationRequestDTO dto, Boolean count){
		return tournamentsRepository.getAllTournament(companyId, dto, count);
	}

	public int findAllCount() {
		return Long.valueOf(tournamentsRepository.count()).intValue();
	} 
	
	public Tournaments findTournamentById(final Long webId) {
		
		log.debug("Finding the web by id:" + webId);
		
		if(webId == null) {
			throw new UserException("Please provide web id");
		}
		
		val web = tournamentsRepository.findByTournamentId(webId);
		
		if(web == null) {
			throw new UserException("No web found by this id");
		}
		
		
		return web;
	}
	
	public Tournaments saveTournaments(Tournaments web) { 
		if(web.getTournamentId() == null) {
			throw new UserException("Please provide blog id to be updated");
		}
		
		final Tournaments dbIdWeb = findTournamentById(web.getTournamentId());
		if(dbIdWeb == null) {
			throw new UserException("No web found by this id.");
		} 
		
		/*
		 * dbIdWeb.setBlogname(web.getBlogname());
		 * dbIdWeb.setCoveragetype(web.getCoveragetype());
		 * dbIdWeb.setLink(web.getLink());
		 */
		
		return tournamentsRepository.save(dbIdWeb);
	}
	
	
	

}
