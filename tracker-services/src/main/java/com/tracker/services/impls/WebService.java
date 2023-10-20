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
import com.tracker.commons.models.Web;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.WebRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WebService {

	@Autowired
	private WebRepository webRepository;


	public List<Web> findAll(final PaginationRequestDTO dto) {
		return webRepository.findAll(dto.getRequest()).getContent();
	}
	
	public List<Web> getWebPostList(Long companyId, PaginationRequestDTO dto){
		return webRepository.getWebPostList(companyId, dto);
	}
	
	public int getWebPostListCount(Long companyId, PaginationRequestDTO dto){
		return webRepository.getWebPostListCount(companyId, dto);
	}

	public int findAllCount() {
		return Long.valueOf(webRepository.count()).intValue();
	} 
	
	public Web findWebById(final Long webId) {
		
		log.debug("Finding the web by id:" + webId);
		
		if(webId == null) {
			throw new UserException("Please provide web id");
		}
		
		val web = webRepository.findByBlogId(webId);
		
		if(web == null) {
			throw new UserException("No web found by this id");
		}
		
		
		return web;
	}
	
	public Web saveWeb(Web web) { 
		if(web.getBlogId() == null) {
			throw new UserException("Please provide blog id to be updated");
		}
		
		final Web dbIdWeb = findWebById(web.getBlogId());
		if(dbIdWeb == null) {
			throw new UserException("No web found by this id.");
		} 
		
		//dbIdWeb.setBlogId(web.getBlogId());
		dbIdWeb.setBlogname(web.getBlogname());
		dbIdWeb.setCoveragetype(web.getCoveragetype());
		dbIdWeb.setLink(web.getLink());
		
		return webRepository.save(dbIdWeb);
	}
	
	
	

}
