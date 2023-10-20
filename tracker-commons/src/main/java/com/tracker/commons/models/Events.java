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
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "events")
@EqualsAndHashCode
@Data
public class Events implements Serializable {

	@Id
	@GeneratedValue
	private Long eventid;
	
	private String eventtitle;
	private String description;
	private String venueaddress;
	private String contactperson;
	private String contactphone;
	
	@Column(name = "event_start_date")
	private LocalDate eventStartDate;
	
	@Column(name = "event_start_time")
	private String eventStartTime;
	
	@Column(name = "event_end_date")
	private LocalDate eventEndDate;
	
	private int ownerid;
	
	@Column(name = "attendance_count")
	private int attendanceCount;
	
	@Column(name = "attendance_hours")
	private int attendanceHours;
	
	@Column(name = "ref_empid")
	private int refEmpid;
	
	private String status;
	
	@Column(name = "event_state")
	private String eventState;
	
	private String sharedTO;
	
	
	@Column(name = "posted_date")
	private LocalDate posted_date;
	
	@Column(name = "event_attachment")
	private String eventAttachment;
	
	@Column(name = "event_attachment1")
	private String eventAttachment1;
	
	@Column(name = "event_attachment2")
	private String eventAttachment2;
	
	@Column(name = "event_attachment3")
	private String eventAttachment3;
	
	private String firstLastname;
	

}
