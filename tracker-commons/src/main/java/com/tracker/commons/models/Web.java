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
@Table(name = "user_blog")
@EqualsAndHashCode
@Data
public class Web implements Serializable {

	@Id
	@GeneratedValue
	private Long blogId;
	
	private String blogname;
	
	private String link;
	private String coveragetype;
	private String refproducts;
	private String userid;
	private String sharedTO;
	private String status;
	
	private String headline;
	private String firstLastname;
	
	@Column(name = "posted_date")
	private LocalDate postedDate;
	
	

}
