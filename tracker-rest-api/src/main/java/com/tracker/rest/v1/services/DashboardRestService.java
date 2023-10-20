/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.v1.services;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.commons.annotations.CustomResponse;
import com.tracker.commons.dtos.DashboardActiveMembersResponse;
import com.tracker.commons.dtos.DashboardMostUsedKeuwordsResponse;
import com.tracker.commons.dtos.DashboardSocialActivityTotalsResponse;
import com.tracker.config.WebConfig;
import com.tracker.rest.converters.JsonWrapper;
import com.tracker.services.impls.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(WebConfig.BASE_REST_API_V1 + "/dashboard")
@Api(value = WebConfig.BASE_REST_API_V1 + "/dashboard", tags = {"Users Servie - Operations about the User"})
@Slf4j
public class DashboardRestService {

	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "Get user by id")
	@ApiImplicitParams({		
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@GetMapping("/memberCounts")
	@CustomResponse
	public JsonWrapper<DashboardActiveMembersResponse> getMemberCount() {
		val r1 =  new DashboardActiveMembersResponse(25, 200);
		
		return new JsonWrapper<>(r1);
	}
	
	@ApiOperation(value = "Get user by id")
	@ApiImplicitParams({		
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@GetMapping("/mostUsedKeywords")
	public JsonWrapper<List<DashboardMostUsedKeuwordsResponse>> getMostUsedKeyWords() {
		
		val response1 = new DashboardMostUsedKeuwordsResponse();
		val response2 = new DashboardMostUsedKeuwordsResponse();
		val response3 = new DashboardMostUsedKeuwordsResponse();
		val response4 = new DashboardMostUsedKeuwordsResponse();
		val response5 = new DashboardMostUsedKeuwordsResponse();
		
		val keywords = new ArrayList<DashboardMostUsedKeuwordsResponse>();
		
		response1.setName("viral1");
		response1.setSocialType("Facebook");
		keywords.add(response1);
		
		response2.setName("viral2");
		response2.setSocialType("Twitter");
		keywords.add(response2);
		
		response3.setName("viral3");
		response3.setSocialType("Youtube");
		keywords.add(response3);
		
		response4.setName("viral4");
		response4.setSocialType("Facebook");
		keywords.add(response4);
		
		response5.setName("viral5");
		response5.setSocialType("Facebook");
		keywords.add(response5);
		
		return new JsonWrapper<>(keywords); 
	}
	
	@ApiOperation(value = "Get user by id")
	@ApiImplicitParams({		
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@GetMapping("/socialActivityTotals")
	public JsonWrapper<List<DashboardSocialActivityTotalsResponse>> socialActivityTotals() {
		
		val response1 = new DashboardSocialActivityTotalsResponse();
		val response2 = new DashboardSocialActivityTotalsResponse();
		val response3 = new DashboardSocialActivityTotalsResponse();
		val response4 = new DashboardSocialActivityTotalsResponse();
		
		val activityTotals = new ArrayList<DashboardSocialActivityTotalsResponse>();
		
		response1.setTotalType("YTD");
		response1.setId(1);
		response1.setTotalMembers(100);
		response1.setTotalPosts(110);
		response1.setTotalInteractions(300);
		response1.setInteractionRate(getDouble(Double.valueOf(response1.getTotalInteractions() / Double.valueOf(response1.getTotalPosts()))));
		response1.setPercentageChange(14.59);
		activityTotals.add(response1);
		
		response2.setTotalType("Month");
		response2.setId(2);
		response2.setTotalMembers(200);
		response2.setTotalPosts(120);
		response2.setTotalInteractions(350);
		response2.setInteractionRate(getDouble(Double.valueOf(response2.getTotalInteractions() / Double.valueOf(response2.getTotalPosts()))));
		response2.setPercentageChange(34.12);
		activityTotals.add(response2);
		
		response3.setTotalType("Quarter");
		response3.setId(3);
		response3.setTotalMembers(230);
		response3.setTotalPosts(120);
		response3.setTotalInteractions(340);
		response3.setInteractionRate(getDouble(Double.valueOf(response3.getTotalInteractions() / Double.valueOf(response3.getTotalPosts()))));
		response3.setPercentageChange(10.98);
		activityTotals.add(response3);
		
		response4.setTotalType("Year");
		response4.setId(4);
		response4.setTotalMembers(130);
		response4.setTotalPosts(220);
		response4.setTotalInteractions(840);
		response4.setInteractionRate(getDouble(Double.valueOf(response4.getTotalInteractions() / Double.valueOf(response4.getTotalPosts()))));
		response4.setPercentageChange(15.99);
		activityTotals.add(response4);
		
		
		return new JsonWrapper<>(355, 4, activityTotals); 
	}
	
	private Double getDouble(Double val) {
		return Math.round(val * 100D) / 100D;
	}

}
