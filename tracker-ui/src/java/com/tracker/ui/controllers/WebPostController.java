package com.tracker.ui.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tracker.commons.models.Web;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.WebService;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WebPostController extends BaseController {
	
	@Autowired
	private WebService webService;

	@RequestMapping("/webPost")
	public String login(HttpServletRequest request, HttpServletResponse response) {

		log.info("Login controller has been called...");
		
		log.info("adfsd : " + request.getRequestURI());

		return "webPost";
	}
	
	@GetMapping("/rest/webPostList")
	public @ResponseBody JsonWrapper<Web> getWebPostList(@ModelAttribute final PaginationRequestDTO dto) {

		dto.setPage((dto.getPage() / dto.getSize()) + 1);
		
		final List<Web> list = webService.getWebPostList(UserUtils.getLoggedInUserId(), dto);
		final int count = webService.getWebPostListCount(UserUtils.getLoggedInUserId(), dto);
		
		return new JsonWrapper<Web>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}
	
	@PostMapping("/rest/saveWebPost")
	public @ResponseBody String update(@RequestBody final Web web) {
		webService.saveWeb(web);
		return "success";
	} 

}
