package com.tracker.ui.controllers;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.ScheduleReport;
import com.tracker.commons.models.Team;
import com.tracker.commons.models.User;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.ScheduleReportService;
import com.tracker.services.utils.SessionVariables;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ScheduleReportController extends BaseController {
	
	@Autowired
	private ScheduleReportService scheduleReportService;

	@RequestMapping("/scheduleReports")
	public ModelAndView scheduleReports(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		log.info("Report page has been called, loading the page...");
		ModelAndView model = new ModelAndView();
    	model.setViewName("scheduleReports");
		
		@SuppressWarnings("unchecked")
		val membersList = (List<User>) UserUtils.getSessionsMap().get(SessionVariables.MEMBERS);
		model.addObject("membersList", membersList);
		
		@SuppressWarnings("unchecked")
		val teamList = (List<Team>) UserUtils.getSessionsMap().get(SessionVariables.TEAM);
		model.addObject("teamList", teamList);
		
		val keywordsFB = UserUtils.getLoggedInUser().getFbKeywords();
		val keywordsIN = UserUtils.getLoggedInUser().getInstaKeywords();
		val keywordsTW = UserUtils.getLoggedInUser().getTwtKeywords();
		val keywordsYT = UserUtils.getLoggedInUser().getYoutubeKeywords();
		
		List<String> keywordsList = new ArrayList<String>();
		
		if(keywordsFB != null) {
			keywordsList.addAll(Arrays.asList( keywordsFB.split(",")));
		}
		if(keywordsIN != null) {
			keywordsList.addAll(Arrays.asList( keywordsIN.split(",")));
		}
		if(keywordsTW != null) {
			keywordsList.addAll(Arrays.asList( keywordsTW.split(",")));
		}
		if(keywordsYT != null) {
			keywordsList.addAll(Arrays.asList( keywordsYT.split(",")));
		}
		
		List<String> keywords = keywordsList.stream()
			     .distinct()
			     .collect(Collectors.toList());
		model.addObject("keywordsList", keywords);
		
		return model;
	}
	
	@GetMapping("/rest/scheduleReportList")
	public @ResponseBody JsonWrapper<ScheduleReport> getMarketPrices(@ModelAttribute final PaginationRequestDTO dto) {

		final List<ScheduleReport> list = scheduleReportService.findAll(dto, UserUtils.getLoggedInUserId());
		final int count = scheduleReportService.findAllCount();
		
		scheduleReportService.scheduleReport();
		
		return new JsonWrapper<ScheduleReport>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}
	
	@PostMapping("/rest/saveScheduleReport")
	public @ResponseBody String update(
			@RequestParam(name = "scheduleId") String scheduleIdStr,
			@RequestParam(name = "reportName") String reportName,
			@RequestParam(name = "members[]", required = false) final String[] membersArray,
			@RequestParam(name = "team[]", required = false) final String[] teamArray,
			@RequestParam(name = "frequency", required = false) String frequency,
			@RequestParam(name = "startDate") final String startDate,
			@RequestParam(name = "reportType") final String reportType,
			@RequestParam(name = "perviousChangeInclude", required = false) Boolean perviousChangeInclude,
			@RequestParam(name = "recipient1") String recipient1,
			@RequestParam(name = "recipient2") String recipient2,
			@RequestParam(name = "recipient3") String recipient3,
			@RequestParam(name = "recipient4") String recipient4,
			@RequestParam(name = "recipient5") String recipient5
			) {
		
		LocalDateTime reportStartDate = null;
		Long scheduleId = 0l;
		String members = "", team = "";
		
		if(StringUtils.isBlank(reportName)) {
			throw new UserException("Please enter Report Name.");
		}
		
		if(StringUtils.isNotBlank(startDate)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			reportStartDate = LocalDateTime.parse(startDate+" 00:00", formatter);
		}else {
			throw new UserException("Please enter valid start date.");
		}
		
		if(membersArray != null && membersArray.length > 0) {
			members = String.join(",", membersArray);
		}
		
		if(teamArray != null && teamArray.length > 0) {
			team = String.join(",", teamArray);
		}
		
		if(StringUtils.isNotBlank(scheduleIdStr)) {
			scheduleId = Long.parseLong(scheduleIdStr);
		}
		
		ScheduleReport scheduleReport = new ScheduleReport();
		
		if(scheduleId > 0) {
			scheduleReport.setScheduleId(scheduleId);
			scheduleReport.setReportName(reportName);
			scheduleReport.setTeam(team);
			scheduleReport.setMembers(members);
			scheduleReport.setFrequency(frequency);
			scheduleReport.setStartDate(reportStartDate);
			scheduleReport.setReportType(reportType);
			scheduleReport.setPerviousChangeInclude(perviousChangeInclude);
			scheduleReport.setRecipient1(recipient1);
			scheduleReport.setRecipient2(recipient2);
			scheduleReport.setRecipient3(recipient3);
			scheduleReport.setRecipient4(recipient4);
			scheduleReport.setRecipient5(recipient5);
			scheduleReport.setDateCreated(ZonedDateTime.now());
			scheduleReport.setCreatedBy(UserUtils.getLoggedInUserId());
			scheduleReport.setDateModified(ZonedDateTime.now());
			scheduleReport.setModifiedBy(UserUtils.getLoggedInUserId());
			scheduleReportService.saveScheduleReport(scheduleReport);
		}else {
			scheduleReport.setReportName(reportName);
			scheduleReport.setTeam(team);
			scheduleReport.setMembers(members);
			scheduleReport.setFrequency(frequency);
			scheduleReport.setStartDate(reportStartDate);
			scheduleReport.setReportType(reportType);
			scheduleReport.setPerviousChangeInclude(perviousChangeInclude);
			scheduleReport.setRecipient1(recipient1);
			scheduleReport.setRecipient2(recipient2);
			scheduleReport.setRecipient3(recipient3);
			scheduleReport.setRecipient4(recipient4);
			scheduleReport.setRecipient5(recipient5);
			scheduleReport.setDateCreated(ZonedDateTime.now());
			scheduleReport.setCreatedBy(UserUtils.getLoggedInUserId());
			scheduleReport.setDateModified(ZonedDateTime.now());
			scheduleReport.setModifiedBy(UserUtils.getLoggedInUserId());
			scheduleReportService.saveScheduleReport(scheduleReport);
		}
		
		
		
		return "success";
	}
	 

}
