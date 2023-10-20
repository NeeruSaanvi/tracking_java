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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "user_pro_app_details")
@EqualsAndHashCode
@Data
public class UserProAppDetailsSports implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long appId;
	
	private Long userId;
	
	private Long companyId;
	
	private String status;
	
	@Column(name = "discount_code")
	private String discountCode;
	
	private String highest_education;
	private String hunting_categories;
	private String target_species;
	private String primary_hunting_method;
	private Integer num_days_peryear_field;
	private String guide;
	private String shirt_size;
	private String glove_size;
	private LocalDate posted_date;
	

}
