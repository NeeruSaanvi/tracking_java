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

import com.tracker.commons.models.Tournaments;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.TournamentsService;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

@Controller
public class TournamentsController extends BaseController {
	
	@Autowired
	private TournamentsService tournamentsService;

	@RequestMapping("/tournaments")
	public String tournaments(HttpServletRequest request, HttpServletResponse response) {
		return "tournaments";
	}
	
	@GetMapping("/rest/tournamentsList")
	public @ResponseBody JsonWrapper<Tournaments> getMarketPrices(@ModelAttribute final PaginationRequestDTO dto) {

		dto.setPage((dto.getPage() / dto.getSize()) + 1);
		
		final List<Tournaments> list = tournamentsService.getAllTournament(UserUtils.getLoggedInUserId(), dto, false); 
		final List<Tournaments> countlist = tournamentsService.getAllTournament(UserUtils.getLoggedInUserId(), dto, true);
		int count = 0;
		
		if(countlist != null) {
			count = countlist.size();
		}
		
		return new JsonWrapper<Tournaments>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}
	

}
