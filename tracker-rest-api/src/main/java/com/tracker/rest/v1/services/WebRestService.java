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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.commons.models.Web;
import com.tracker.config.WebConfig;
import com.tracker.rest.converters.JsonWrapper;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.WebService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(WebConfig.BASE_REST_API_V1 + "/web")
@Api(value = WebConfig.BASE_REST_API_V1 + "/web", tags = {"Users Servie - Operations about the User"})
public class WebRestService {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(WebRestService.class);

	@Autowired
	private WebService webService;

	@ApiOperation(value = "Get all Web")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "Provide page number to retrieve", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "Provide number of records per page to retrieve", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "direction", value = "Provide sorting direction as asc or desc", required = false, dataType = "String", allowableValues = "ASC, DESC", paramType = "query"),
		@ApiImplicitParam(name = "sort", value = "Provide the attribute name to sort on", required = false, dataType = "String", allowMultiple = true, paramType = "query"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@RequestMapping(method = RequestMethod.GET) 
	public JsonWrapper<List<Web>> findAll(@ModelAttribute final PaginationRequestDTO dto) {
		final List<Web> list = webService.findAll(dto);
		final int count = webService.findAllCount();

		return new JsonWrapper<List<Web>>(count, list.size(), list);
	} 

	@ApiOperation(value = "Update web") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "User id", required = true, dataType = "Long", paramType = "path"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@PostMapping("/saveWebPost")
	public Web update(@RequestBody final Web web) {
		return webService.saveWeb(web);
	} 
	
	@ApiOperation(value = "Delete Web")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@DeleteMapping("/deleteWebPost")
	public Web update(@RequestParam("blogId") final Long id) {
		
		Web web = webService.findWebById(id);
		web.setStatus("Delete");
		
		return webService.saveWeb(web);
	} 
	

}
