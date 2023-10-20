/*******************************************************************************
 * Copyright (c) 2018 by M and M Softwares LLC.                                    
 * All rights reserved.                                                       
 *                                                                              
 * This software is the confidential and proprietary information of 
 * M and M Softwares LLC ("Confidential Information"). 
 * You shall not disclose such confidential Information and shall 
 * use it only in accordance with  the terms of the license agreement 
 * you entered with M and M Softwares LLC.
 ******************************************************************************/
package com.tracker.services.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.MultiValueMap;

import com.tracker.commons.Constants;
import com.tracker.commons.exceptions.UserException;

import lombok.Data;

@SuppressWarnings("serial")
@Data 
public class PaginationRequestDTO implements  Serializable {

	private int page = 1;
	private int size = 10; 
	private String direction;
	private String sort; 
	
	public PaginationRequestDTO(MultiValueMap<String, String> params, String defaultSortField) {
		if(params != null) {
			  
			if (!params.containsKey("pagination[page]") && (Integer.valueOf(params.get("pagination[page]").get(0))) <= 0) {
				throw new UserException(Constants.INVALID_PAGE_VALUE);
			}
			
			if (!params.containsKey("pagination[perpage]") && (Integer.valueOf(params.get("pagination[perpage]").get(0))) <= 0) {
				throw new UserException(Constants.INVALID_PAGE_SIZE);
			}
			
			this.page = (Integer.valueOf(params.get("pagination[page]").get(0)));
			this.size = (Integer.valueOf(params.get("pagination[perpage]").get(0)));
			
			this.sort = params.containsKey("sort[field]") ? params.get("sort[field]").get(0) : defaultSortField;
			this.direction = params.containsKey("sort[sort]") ? params.get("sort[sort]").get(0) : Direction.ASC.name();
		}
	} 

	public PageRequest getRequest() { 
 
		if(direction != null && sort != null) {
			final Sort sortObj = new Sort( direction.equalsIgnoreCase(Direction.ASC.name()) ? Direction.ASC : Direction.DESC, 
					new ArrayList<String>(Arrays.asList(sort.split(","))) );

			return PageRequest.of(page , size, sortObj);
		}

		return PageRequest.of(page , size);
	} 
}
