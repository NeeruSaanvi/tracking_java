package com.tracker.ui.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.tracker.commons.models.Lead;
import com.tracker.commons.models.User;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.LeadService;
import com.tracker.services.repositories.LeadRepository;
import com.tracker.services.utils.SessionVariables;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

import lombok.val;

@Controller
public class LeadController extends BaseController {
	
	@Autowired
	private LeadService leadService;
	
	@Autowired
	private LeadRepository leadRepository;

	@RequestMapping("/lead")
	public ModelAndView lead(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		
		ModelAndView model = new ModelAndView();
    	model.setViewName("lead");
		
		@SuppressWarnings("unchecked")
		val membersList = (List<User>) UserUtils.getSessionsMap().get(SessionVariables.MEMBERS);
		model.addObject("membersList", membersList);
		
		return model;
	}
	
	@GetMapping("/rest/leadList")
	public @ResponseBody JsonWrapper<Lead> getLeads(@ModelAttribute final PaginationRequestDTO dto,
			@RequestParam(name = "period") String periodStr,
			@RequestParam(name = "staff") String memberStr) {

		dto.setPage((dto.getPage() / dto.getSize()) + 1);
		
		List<Long> userList = new ArrayList<Long>();
		
		Date toDate = Calendar.getInstance().getTime();
		Date fromDate = Calendar.getInstance().getTime();
		
		if(StringUtils.isNotBlank(periodStr)) {
			if(periodStr.equals("7")) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -7);
				fromDate = cal.getTime();
			}
			
			if(periodStr.equals("30")) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -30);
				fromDate = cal.getTime();
			}

			if(periodStr.equals("90")) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -90);
				fromDate = cal.getTime();
			}
		}
		
		if(StringUtils.isNotBlank(memberStr) && !memberStr.equals("0")) {
			userList.add(Long.parseLong(memberStr));
		}
		
		final List<Lead> list = leadService.getLeadList(UserUtils.getLoggedInUserId(), dto, fromDate, toDate, userList);
		final int count = leadService.getLeadListCount(UserUtils.getLoggedInUserId(), dto, fromDate, toDate, userList);

		return new JsonWrapper<Lead>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}
	
	@GetMapping(value = "/leadExporttoExcel")
	public ResponseEntity<InputStreamResource> excelReport(
			@RequestParam(name = "period") String periodStr,
			@RequestParam(name = "staff") String memberStr) throws Exception {
		
		List<Long> userList = new ArrayList<Long>();
		
		Date toDate = Calendar.getInstance().getTime();
		Date fromDate = Calendar.getInstance().getTime();
		
		if(StringUtils.isNotBlank(periodStr)) {
			if(periodStr.equals("7")) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -7);
				fromDate = cal.getTime();
			}
			
			if(periodStr.equals("30")) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -30);
				fromDate = cal.getTime();
			}

			if(periodStr.equals("90")) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -90);
				fromDate = cal.getTime();
			}
		}
		
		if(StringUtils.isNotBlank(memberStr) && !memberStr.equals("0")) {
			userList.add(Long.parseLong(memberStr));
		}
		
		ByteArrayOutputStream out = leadService.generateExcelReport(UserUtils.getLoggedInUserId(), fromDate, toDate, userList);
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/octet-stream");
		headers.add("Content-Disposition", "attachment; filename=leadReport.xls");
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}
}
