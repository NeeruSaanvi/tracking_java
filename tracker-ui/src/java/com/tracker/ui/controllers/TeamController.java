package com.tracker.ui.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.models.Team;
import com.tracker.commons.models.User;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.TeamService;
import com.tracker.services.utils.SessionVariables;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TeamController extends BaseController {
	
	@Autowired
	private TeamService teamService;

	@SuppressWarnings("unchecked")
	@RequestMapping("/team")
	public ModelAndView team(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

		log.info("Team controller has been called...");
		
		ModelAndView model = new ModelAndView();
    	model.setViewName("team");
    	
    	val membersList = (List<User>) UserUtils.getSessionsMap().get(SessionVariables.MEMBERS);
		model.addObject("membersList", membersList);

		return model;
	}
	
	@GetMapping("/rest/teamList")
	public @ResponseBody JsonWrapper<Team> fetchTeam(@ModelAttribute final PaginationRequestDTO dto) {

		final List<Team> list = teamService.findAll(dto);
		final int count = teamService.findAllCount();
		
		return new JsonWrapper<Team>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}
	
	@PostMapping("/rest/saveTeam")
	public @ResponseBody String update(@RequestBody final Team team) {		
		teamService.saveTeam(team);
		return "success";
	} 
	
	@PostMapping("/rest/deleteTeam")
	public @ResponseBody String delete(@QueryParam(value = "teamId") Long teamId) {	
		teamService.deleteTeam(teamId);
		return "success";
	} 

}
