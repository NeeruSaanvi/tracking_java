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
import java.time.*;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "company_ref")
@EqualsAndHashCode
@Data
public class CompanyRef implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long refid;
	
	private Long userid;
	
	
	private String prostaffName;
	private String compname;
	private Long companyId;
	private Integer staffRatings;
	private String fbkeywords;
	private String twitterkeywords;
	private String instakeywords;
	private String youtubekeywords;
	
	@Column(name = "data_download_flag")
	private int dataDownloadFlag;
	
	@Column(name = "data_pgdownload_flag")
	private int dataPgdownloadFlag;
	
	@Column(name = "data_download_date")
	private LocalDate dataDownloadDate;
	
	@Column(name = "data_download_flag_IN")
	private int dataDownloadFlagIN;
	
	@Column(name = "data_download_date_IN")
	private LocalDate dataDownloadDateIN;
	
	@Column(name = "activation_reminder_date")
	private LocalDate activationReminderDate;
	
	private Integer rank;
	
	@Column(name = "rank_score")
	private Integer rankScore;
	
	@Column(name = "rankUpdate_dated")
	private LocalDate rankUpdateDated;
	
	@Column(name = "pro_category")
	private String proCategory;
	
	@Column(name = "notification_terms")
	private String notificationTerms;
	
	@Column(name = "ref_status")
	private String refStatus;
	
	@Column(name = "referenced_date")
	private LocalDate referencedDate;
	
	@Column(name = "last_modified_data_download_date")
	private LocalDate lastModifiedDataDownloadDate;
	
	@Column(name = "last_modified_insta_data_download_date")
	private LocalDate lastModifiedInstaDataDownloadDate;
	
	@Column(name = "approved_date")
	private ZonedDateTime approvedDate;

	@Transient
	private String atProfilePic;

}
