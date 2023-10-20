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

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.Team;
import com.tracker.commons.models.UserTeam;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.TeamRepository;
import com.tracker.services.repositories.UserTeamRepository;
import com.tracker.services.utils.SessionVariables;
import com.tracker.services.utils.UserUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeamService {

	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private UserTeamRepository userTeamRepository;


	public List<Team> findAll(final PaginationRequestDTO dto) {
		
		List<Team> lstTeam = teamRepository.findByCreatedBy(dto.getRequest(), UserUtils.getLoggedInUserId()).getContent();
		
		for(Team team : lstTeam) {
			
			List<UserTeam> userTeam = userTeamRepository.findByTeamId(team.getTeamId());
			String[] members = new String[userTeam.size()];
			int index = 0;
			for(UserTeam uTeam : userTeam) {
				members[index] = String.valueOf(uTeam.getUserId());
				index++;
			}
			
			team.setTeamMembers(members);
		}
		
		return lstTeam;
	} 
	
	public List<Team> findAll() {
		
		List<Team> lstTeam = teamRepository.findByCreatedBy(UserUtils.getLoggedInUserId());
		
		for(Team team : lstTeam) {
			
			List<UserTeam> userTeam = userTeamRepository.findByTeamId(team.getTeamId());
			String[] members = new String[userTeam.size()];
			int index = 0;
			for(UserTeam uTeam : userTeam) {
				members[index] = String.valueOf(uTeam.getUserId());
				index++;
			}
			
			team.setTeamMembers(members);
		}
		
		return lstTeam;
	}

	public int findAllCount() {
		return Long.valueOf(teamRepository.count()).intValue();
	} 
	
	public Team findTeamById(final Long teamId) {
		
		log.debug("Finding the team by id:" + teamId);
		
		if(teamId == null) {
			throw new UserException("Please provide team id");
		}
		
		val team = teamRepository.findByTeamId(teamId);
		
		if(team == null) {
			throw new UserException("No team found by this id");
		}
		
		
		return team;
	}
	
	public void deleteTeam(Long teamId) {
		final Team dbIdTeam = findTeamById(teamId);
		if(dbIdTeam == null) {
			throw new UserException("No team found by this id.");
		} 
		
		teamRepository.delete(dbIdTeam);
		
		List<UserTeam> lstuTeam = userTeamRepository.findByTeamId(teamId);
		if(lstuTeam != null && lstuTeam.size() > 0) {
			for(UserTeam ut : lstuTeam) {
				userTeamRepository.deleteByTeamId(ut.getTeamId());
			}
		}
		
	}
	
	public Team saveTeam(Team team) { 
		
		Team savedTeam = new Team();
		
		if(team.getTeamId() == null) {
			Team teamObj = new Team();
			teamObj.setTeamName(team.getTeamName());
			teamObj.setDateCreated(ZonedDateTime.now());
			teamObj.setCreatedBy(UserUtils.getLoggedInUserId());
			teamObj.setDateModified(ZonedDateTime.now());
			teamObj.setModifiedBy(UserUtils.getLoggedInUserId());
			
			savedTeam = teamRepository.save(teamObj);
			
		}else {
			final Team dbIdTeam = findTeamById(team.getTeamId());
			if(dbIdTeam == null) {
				throw new UserException("No team found by this id.");
			} 
			dbIdTeam.setTeamId(team.getTeamId());
			dbIdTeam.setTeamName(team.getTeamName());
			dbIdTeam.setDateModified(ZonedDateTime.now());
			dbIdTeam.setModifiedBy(UserUtils.getLoggedInUserId());
			
			savedTeam = teamRepository.save(dbIdTeam);
		}
		
		List<UserTeam> lstuTeam = userTeamRepository.findByTeamId(savedTeam.getTeamId());
		if(lstuTeam != null && lstuTeam.size() > 0) {
			for(UserTeam ut : lstuTeam) {
				userTeamRepository.deleteByTeamId(ut.getTeamId());
			}
		}
		
		if(team.getMembersArray() != null && team.getMembersArray().split(",").length > 0) {
			for(String str : team.getMembersArray().split(",")) {
				UserTeam userTeam = new UserTeam();
				userTeam.setTeamId(savedTeam.getTeamId());
				userTeam.setUserId(Long.parseLong(str));
				System.out.println("str:: "+str);
				userTeamRepository.save(userTeam);
			}
		}
		
		val teams = teamRepository.findByCreatedBy(UserUtils.getLoggedInUserId());
		UserUtils.getSessionsMap().put(SessionVariables.TEAM, teams);
		
		
		return savedTeam;
	}
	
	
	

}
