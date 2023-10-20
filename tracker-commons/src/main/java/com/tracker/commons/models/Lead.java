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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "user_lead")
@EqualsAndHashCode
@Data
public class Lead implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "lead_id")
	private Long leadId;
	
	@Column(name = "lead_deal_name")
	private String leadDealName;
	
	@Column(name = "lead_consu_firstname")
	private String leadConsuFirstname;
	
	@Column(name = "lead_consu_lastname")
	private String leadConsuLastname;
	
	@Column(name = "lead_del_add")
	private String leadDelAdd;
	
	@Column(name = "lead_consu_city")
	private String leadConsuCity;

	@Column(name = "lead_consu_state")
	private String leadConsuState;
	
	@Column(name = "lead_consu_zip")
	private String leadConsuZip;
	
	@Column(name = "lead_del_phone")
	private String leadDelPhone;
	
	@Column(name = "lead_buy_name")
	private String leadBuyName;
	
	@Column(name = "lead_buy_phone")
	private String leadBuyPhone;
	
	@Column(name = "lead_buy_email")
	private String leadBuyEmail;
	
	@Column(name = "lead_rel")
	private String leadRel;
	
	@Column(name = "lead_type")
	private String leadType;
	
	private String userId;
	private String sharedTO;
	private String status;
	
	@Column(name = "posted_date")
	private LocalDate postedDate;
	
	@Transient
	private String postedDateFormatter;
	
	private String firstLastname;

}
