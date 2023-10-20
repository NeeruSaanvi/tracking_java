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
@Table(name = "user_print")
@EqualsAndHashCode
@Data
public class Prints implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "print_id")
	private Long printId;
	
	@Column(name = "print_publish_name")
	private String printPublishName;
	
	@Column(name = "print_art_name")
	private String printArtName;
	
	@Column(name = "print_cov_type")
	private String printCovType;
	
	@Column(name = "print_img_path")
	private String printImgPath;
	
	private String userId;
	private String sharedTO;
	private String status;
	
	@Column(name = "print_issue_date")
	private LocalDate printIssueDate;
	
	@Column(name = "posted_date")
	private LocalDate postedDate;
	
	private String firstLastname;

}
