/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.converters;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JsonWrapper<E> {

	private int totalCount;
	private int totalRecordsReturned;
	private E data;
	private boolean success = true;
	private String message = "";
	private boolean isList = false;
 
	public JsonWrapper(E data) {
		super();
		this.data = data;
		
		if (data != null && data instanceof List) {
			isList = true;
		}
	}

	public JsonWrapper(int totalCount, int totalRecordsReturned, E data) {
		super(); 
		this.totalCount = totalCount;
		this.totalRecordsReturned = totalRecordsReturned;
		this.data = data;
		
		if (data != null && data instanceof List) {
			isList = true;
		}
	} 

}
