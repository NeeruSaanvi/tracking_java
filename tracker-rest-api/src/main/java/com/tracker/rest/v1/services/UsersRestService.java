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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.commons.models.User;
import com.tracker.config.WebConfig;
import com.tracker.rest.converters.JsonWrapper;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(WebConfig.BASE_REST_API_V1 + "/users")
@Api(value = WebConfig.BASE_REST_API_V1 + "/users", tags = {"Users Servie - Operations about the User"})
public class UsersRestService {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(UsersRestService.class);

	@Autowired
	private UserService userService;

	@ApiOperation(value = "Get all users")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "Provide page number to retrieve", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "Provide number of records per page to retrieve", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "direction", value = "Provide sorting direction as asc or desc", required = false, dataType = "String", allowableValues = "ASC, DESC", paramType = "query"),
		@ApiImplicitParam(name = "sort", value = "Provide the attribute name to sort on", required = false, dataType = "String", allowMultiple = true, paramType = "query"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@RequestMapping(method = RequestMethod.GET) 
	public JsonWrapper<List<User>> findAll(@ModelAttribute final PaginationRequestDTO dto) {
		final List<User> list = userService.findAll(dto);
		final int count = userService.findAllCount();

		return new JsonWrapper<List<User>>(count, list.size(), list);
	} 

	@ApiOperation(value = "Get user by id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "User IidD", required = true, dataType = "Long", paramType = "path"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@GetMapping("/{id}")
	public User getUserById(@PathVariable("id") final Long id) {
		return userService.findUserById(id);
	}

	@ApiOperation(value = "Get user by email")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "email", value = "User email", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@GetMapping("/emails")
	public User getUserByEmail(@RequestParam("email") final String email) {
		return userService.findUserByEmail(email);
	}

	/*@ApiOperation(value = "Create user")
	@PostMapping("/")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	public User create(@RequestBody User user) {
		return userService.createUser(user);
	}

	@ApiOperation(value = "Update user") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "User id", required = true, dataType = "Long", paramType = "path"),
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@PutMapping("/{id}")
	public User update(@RequestBody final User user, @PathVariable("id") final Long id) {
		return userService.updateUser(user);
	} 

	@ApiOperation(value = "Delete user")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer X")
	})
	@DeleteMapping("/{id}")
	public void update(@PathVariable("id") final Long id) {
		userService.deleteUser(id);
	} 

	 */

}
