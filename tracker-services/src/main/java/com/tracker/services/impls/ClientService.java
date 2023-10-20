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
import com.tracker.commons.models.Client;
import com.tracker.services.repositories.ClientRepository;
import com.tracker.services.utils.UserUtils;

import lombok.val;

@Service
public class ClientService {
	 
	@Autowired
	private ClientRepository universityRepository; 

	public List<Client> findAll() {
		return universityRepository.findAllByActiveTrue();
	}  
	
	public Client findbyId(Long uniId) { 
		
		if(uniId == null) {
			throw new UserException("Please provide university id");
		}
		
		val unisersity = universityRepository.findByClientIdAndActiveTrue(uniId);
		
		if(unisersity == null) {
			throw new UserException("No university found by this id");
		}
		
		if( !UserUtils.getClient().getClientId().equals(unisersity.getClientId())) {
			throw new UserException("You can't view this university");
		} 

		return unisersity;
	} 
	/*
	public int searchCount(String search, PaginationRequestDTO dto) {
		if(! UserUtils.isAdminUser()) {
			throw new UserException("You don't have enough permission to view the university");
		} 
		
		return universityRepository.countByNameIgnoreCaseContainingAndActiveTrue(search).intValue(); 
	}
	
	public List<Client> search(String search, PaginationRequestDTO dto) {
		if(! UserUtils.isAdminUser()) {
			throw new UserException("You don't have enough permission to view the university");
		}
		
		return universityRepository.findByNameIgnoreCaseContainingAndActiveTrue(search, dto.getRequest()).getContent();
	}
	
	public Client create(final Client universityDTO) {
		if(! UserUtils.isAdminUser()) {
			throw new UserException("You don't have permissiong to create a new university");
		}
		
		if(StringUtils.isBlank(universityDTO.getName())) {
			throw new UserException("University name cannot be empty");
		} 
		
		val existingUniversity = universityRepository.findByNameAndActiveTrue(universityDTO.getName());
		if(existingUniversity != null) {
			throw new UserException("University by this name already exists. Please select another name");
		}		
		
		val newUniversity = new Client();
		BeanUtils.copyProperties(universityDTO, newUniversity);
		
		newUniversity.setCreatedBy(UserUtils.getLoggedInUserId());
		newUniversity.setModifiedBy(UserUtils.getLoggedInUserId());
		newUniversity.setDateCreated(ZonedDateTime.now());
		newUniversity.setDateModified(ZonedDateTime.now()); 
		
		return universityRepository.save(newUniversity); 
	}
	
	public Client update(final Client universityDTO) {
		if(! UserUtils.isAdminUser()) {
			throw new UserException("You don't have permissiong to update a university");
		}
		
		if(universityDTO.getClientId() == null) {
			throw new UserException("University ID cannot be null");
		}
		
		if(StringUtils.isBlank(universityDTO.getName())) {
			throw new UserException("University name cannot be empty");
		}  
		
		val existingUniversity = universityRepository.findByNameAndActiveTrue(universityDTO.getName());
		if(existingUniversity != null && !existingUniversity.getClientId().equals(universityDTO.getClientId())) {
			throw new UserException("University by this name already exists. Please select another name");
		} 
		
		val universityToBeSaved = universityRepository.findByClientIdAndActiveTrue(universityDTO.getClientId());
		
		if(universityToBeSaved == null) {
			throw new UserException("Invalid university id provided");
		}
		
		universityDTO.setActive(true);
		universityDTO.setCreatedBy(universityToBeSaved.getCreatedBy());
		universityDTO.setDateCreated(universityToBeSaved.getDateCreated());
		
		BeanUtils.copyProperties(universityDTO, universityToBeSaved);
		universityToBeSaved.setModifiedBy(UserUtils.getLoggedInUserId());
		universityToBeSaved.setDateModified(ZonedDateTime.now());
		
		return universityRepository.save(universityToBeSaved); 
	}
	
	public void deleteUniversity(final Long uniId) {
		if(! UserUtils.isAdminUser()) {
			throw new UserException("You don't have permissiong to delete a university");
		}
		
		if(uniId == null) {
			throw new UserException("University ID cannot be null");
		}
		 
		val universityToBeDeleted = universityRepository.findByClientIdAndActiveTrue(uniId);
		
		if(universityToBeDeleted == null) {
			throw new UserException("Invalid university id provided");
		}  
		
		universityToBeDeleted.setActive(false);
		universityToBeDeleted.setModifiedBy(UserUtils.getLoggedInUserId());
		universityToBeDeleted.setDateModified(ZonedDateTime.now());
		 
		universityRepository.save(universityToBeDeleted);
	}
	*/
}
