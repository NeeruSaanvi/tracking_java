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
package com.tracker.commons.models;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseTracebleModel extends BaseModel implements Serializable { 

	private ZonedDateTime dateCreated;

	private ZonedDateTime dateModified;

	private Long createdBy;
	
	private Long modifiedBy;
}
