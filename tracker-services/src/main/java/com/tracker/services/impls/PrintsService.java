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
import com.tracker.commons.models.Prints;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.PrintsRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PrintsService {

	@Autowired
	private PrintsRepository printsRepository;


	public List<Prints> findAll(final PaginationRequestDTO dto) {
		return printsRepository.findAll(dto.getRequest()).getContent();
	} 

	public int findAllCount() {
		return Long.valueOf(printsRepository.count()).intValue();
	} 
	
	public List<Prints> getPrintList(Long companyId, PaginationRequestDTO dto){
		return printsRepository.getPrintList(companyId, dto);
	}
	
	public int getPrintListCount(Long companyId, PaginationRequestDTO dto){
		return printsRepository.getPrintListCount(companyId, dto);
	}
	
	public Prints findPrintById(final Long webId) {
		
		log.debug("Finding the web by id:" + webId);
		
		if(webId == null) {
			throw new UserException("Please provide web id");
		}
		
		val web = printsRepository.findByPrintId(webId);
		
		if(web == null) {
			throw new UserException("No web found by this id");
		}
		
		
		return web;
	}
	
	public Prints saveTournaments(Prints web) { 
		if(web.getPrintId() == null) {
			throw new UserException("Please provide blog id to be updated");
		}
		
		final Prints dbIdWeb = findPrintById(web.getPrintId());
		if(dbIdWeb == null) {
			throw new UserException("No web found by this id.");
		} 
		
		/*
		 * dbIdWeb.setBlogname(web.getBlogname());
		 * dbIdWeb.setCoveragetype(web.getCoveragetype());
		 * dbIdWeb.setLink(web.getLink());
		 */
		
		return printsRepository.save(dbIdWeb);
	}
	
	
	

}
