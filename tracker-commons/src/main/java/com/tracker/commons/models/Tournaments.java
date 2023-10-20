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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "tournaments")
@EqualsAndHashCode
@Data
public class Tournaments implements Serializable {

	@Id
	@GeneratedValue
	private Long tournamentId;
	
	private String Anglername;
	private String tournamentTitle;
	private String tfinishedPlace;
	private String treferenceName;
	private String tcontactInfo;
	private String tournamentPlace;
	private String staffId;
	private String sharedTO;
	private String status;
	
	private LocalDate tournamentDate;
	
	@Transient
	private Long userId;
	
	@Transient
	private String firstLastname;
	

}
