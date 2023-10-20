package com.tracker.ui.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.dtos.ReportGraphResponse;
import com.tracker.commons.dtos.ReportStatResponse;
import com.tracker.commons.models.Reports;
import com.tracker.commons.models.Team;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserTeam;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.ReportsService;
import com.tracker.services.impls.ScheduleReportService;
import com.tracker.services.repositories.ReportsRepository;
import com.tracker.services.repositories.UserTeamRepository;
import com.tracker.services.utils.SessionVariables;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ReportsController extends BaseController {
	
	@Autowired
	private ReportsService reportsService;
	
	@Autowired
	private UserTeamRepository userTeamRepository;
	
	@Autowired
	private ReportsRepository reportsRepository;
	
	@Autowired
	private ScheduleReportService scheduleReportService;
	
	
	@GetMapping(value = "/reportExporttoExcel")
	public ResponseEntity<InputStreamResource> excelReport(
			@RequestParam(name = "fromDate") String fromDate,
			@RequestParam(name = "toDate") String toDate,
			@RequestParam(name = "staff") String staffStr,
			@RequestParam(name = "keyword") String keywordStr,
			@RequestParam(name = "team") String teamStr) throws Exception {

		String keyword = "", staff = "";
		
		if(StringUtils.isNotBlank(keywordStr)) {
			String[] keywordArray = keywordStr.split(",");
			if(keywordArray != null && keywordArray.length > 0) {
				keyword = String.join("|", keywordArray);
			}
		}
		
		if(StringUtils.isNotBlank(staffStr)) {
			staff = staffStr;
		}
		
		if(StringUtils.isNotBlank(teamStr)) {
			String[] teamArray = teamStr.split(",");
			if(teamArray != null && teamArray.length > 0) {
				for(int i=0; i<teamArray.length; i++) {
					List<UserTeam> userTeam = userTeamRepository.findByTeamId(Long.parseLong(teamArray[i]));
					String[] members = new String[userTeam.size()];
					int index = 0;
					for(UserTeam uTeam : userTeam) {
						members[index] = String.valueOf(uTeam.getUserId());
						index++;
					}
					staff = staff +","+ String.join(",", members);
				}
			}
		}
		
		if(StringUtils.isBlank(staffStr)) {
			staff = staff.replaceFirst(",", "");
		}
		
		
		Date date1 = new Date();
		Date date2 = new Date();
		try {
			date1 = new SimpleDateFormat("MM/dd/yyyy").parse(fromDate);
			date2=new SimpleDateFormat("MM/dd/yyyy").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ByteArrayOutputStream out = scheduleReportService.generateExcelReport(keyword, staff, date1, date2, UserUtils.getLoggedInUserId());
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/octet-stream");
		headers.add("Content-Disposition", "attachment; filename=report.xls");
		log.info("step9..");
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		
	}

	
	 
	

	@RequestMapping("/reports")
	public ModelAndView reports(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		log.info("Report page has been called, loading the page...");
		ModelAndView model = new ModelAndView();
    	model.setViewName("reports");
		
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
	
	@GetMapping("/rest/reports/totalstat")
	public @ResponseBody ReportStatResponse getTotalStat(
			@RequestParam(name = "fromDate") String fromDate,
			@RequestParam(name = "toDate") String toDate,
			@RequestParam(name = "staff[]", required = false) final String[] staffArray,
			@RequestParam(name = "keyword[]", required = false ) final String[] keywordArray,
			@RequestParam(name = "team[]", required = false) final String[] teamArray,
			@RequestParam(name = "graphType") String graphType
			) {
		
		String keyword = "", staff = "";
		
		if(keywordArray != null && keywordArray.length > 0) {
			keyword = String.join("|", keywordArray);
		}
		
		if(staffArray != null && staffArray.length > 0) {
			staff = String.join(",", staffArray);
		}
		
		if(teamArray != null && teamArray.length > 0) {
			for(int i=0; i<teamArray.length; i++) {
				List<UserTeam> userTeam = userTeamRepository.findByTeamId(Long.parseLong(teamArray[i]));
				String[] members = new String[userTeam.size()];
				int index = 0;
				for(UserTeam uTeam : userTeam) {
					members[index] = String.valueOf(uTeam.getUserId());
					index++;
				}
				staff = staff +","+ String.join(",", members);
			}
		}
		
		if(staffArray == null) {
			staff = staff.replaceFirst(",", "");
		}
		
		Date date1 = new Date();
		Date date2 = new Date();
		try {
			date1 = new SimpleDateFormat("MM/dd/yyyy").parse(fromDate);
			date2=new SimpleDateFormat("MM/dd/yyyy").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		PaginationRequestDTO dto = null;
		val response =  reportsRepository.getReportsStat(dto, date1, date2, staff, keyword, true, UserUtils.getLoggedInUserId());
		
		return response;
	}
	
	@GetMapping("/rest/reports/chart")
	public @ResponseBody List<ReportGraphResponse> getChart(
			@RequestParam(name = "fromDate") String fromDate,
			@RequestParam(name = "toDate") String toDate,
			@RequestParam(name = "staff[]", required = false) final String[] staffArray,
			@RequestParam(name = "keyword[]", required = false ) final String[] keywordArray,
			@RequestParam(name = "team[]", required = false) final String[] teamArray,
			@RequestParam(name = "graphType") String graphType
			) {
		
		String keyword = "", staff = "";
		
		if(keywordArray != null && keywordArray.length > 0) {
			keyword = String.join("|", keywordArray);
		}
		
		if(staffArray != null && staffArray.length > 0) {
			staff = String.join(",", staffArray);
		}
		
		if(teamArray != null && teamArray.length > 0) {
			for(int i=0; i<teamArray.length; i++) {
				List<UserTeam> userTeam = userTeamRepository.findByTeamId(Long.parseLong(teamArray[i]));
				String[] members = new String[userTeam.size()];
				int index = 0;
				for(UserTeam uTeam : userTeam) {
					members[index] = String.valueOf(uTeam.getUserId());
					index++;
				}
				staff = staff +","+ String.join(",", members);
			}
		}
		
		if(staffArray == null) {
			staff = staff.replaceFirst(",", "");
		}
		
		Date date1 = new Date();
		Date date2 = new Date();
		try {
			date1 = new SimpleDateFormat("MM/dd/yyyy").parse(fromDate);
			date2=new SimpleDateFormat("MM/dd/yyyy").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		PaginationRequestDTO dto = null;
		List<ReportGraphResponse> reportList = reportsRepository.getReportsGraphs(dto, date1, date2, staff, keyword, true, graphType, null);
		
		return reportList;
	}
	
	@GetMapping("/rest/reports")
	public @ResponseBody JsonWrapper<Reports> getReports(@ModelAttribute final PaginationRequestDTO dto, 
			@QueryParam(value = "fromDate") String fromDate,
			@QueryParam(value = "toDate") String toDate,
			@RequestParam(value = "staff[]", required = false) final String[] staffArray,
			@RequestParam(name = "keyword[]", required = false ) final String[] keywordArray,
			@RequestParam(name = "team[]", required = false) final String[] teamArray
			) {

		String keyword = "", staff = "";
		
		if(keywordArray != null && keywordArray.length > 0) {
			keyword = String.join("|", keywordArray);
		}
		
		if(staffArray != null && staffArray.length > 0) {
			staff = String.join(",", staffArray);
		}
		
		if(teamArray != null && teamArray.length > 0) {
			for(int i=0; i<teamArray.length; i++) {
				List<UserTeam> userTeam = userTeamRepository.findByTeamId(Long.parseLong(teamArray[i]));
				String[] members = new String[userTeam.size()];
				int index = 0;
				for(UserTeam uTeam : userTeam) {
					members[index] = String.valueOf(uTeam.getUserId());
					index++;
				}
				staff = staff +","+ String.join(",", members);
			}
		}
		
		if(staffArray == null) {
			staff = staff.replaceFirst(",", "");
		}
		
		Date date1 = new Date();
		Date date2 = new Date();
		try {
			date1 = new SimpleDateFormat("MM/dd/yyyy").parse(fromDate);
			date2=new SimpleDateFormat("MM/dd/yyyy").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		final List<Reports> list = reportsService.fetchReports(dto, date1, date2, staff, keyword, false, false, false, UserUtils.getLoggedInUserId());
		final List<Reports> countlist = reportsService.fetchReports(dto, date1, date2, staff, keyword, true, false, false, UserUtils.getLoggedInUserId());
		int count = countlist.size();
		return new JsonWrapper<Reports>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}
	

}
