package com.tracker.ui.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tracker.commons.models.Prints;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.PrintsService;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

@Controller
public class PrintsController extends BaseController {
	
	@Autowired
	private PrintsService printsService;

	@RequestMapping("/prints")
	public String prints(HttpServletRequest request, HttpServletResponse response) {
		return "prints";
	}
	
	@GetMapping("/rest/printsList")
	public @ResponseBody JsonWrapper<Prints> getMarketPrices(@ModelAttribute final PaginationRequestDTO dto) {

		dto.setPage((dto.getPage() / dto.getSize()) + 1);
		
		final List<Prints> list = printsService.getPrintList(UserUtils.getLoggedInUserId(), dto);
		final int count = printsService.getPrintListCount(UserUtils.getLoggedInUserId(), dto);

		return new JsonWrapper<Prints>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}
	

}
