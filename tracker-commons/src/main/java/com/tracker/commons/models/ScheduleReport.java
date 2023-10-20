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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "schedule_report")
@EqualsAndHashCode
@Data
public class ScheduleReport /* extends BaseTracebleModel */ implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scheduleId;
	
	private String reportName;
	private String frequency;
	private String team;
	private String members;
	private LocalDateTime startDate;
	private String reportType;
	private Boolean perviousChangeInclude;
	private String recipient1;
	private String recipient2;
	private String recipient3;
	private String recipient4;
	private String recipient5;
	
	private ZonedDateTime dateCreated;

	private ZonedDateTime dateModified;

	private Long createdBy;
	
	private Long modifiedBy;

}
