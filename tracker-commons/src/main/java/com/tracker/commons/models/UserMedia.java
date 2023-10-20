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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString(includeFieldNames = true, exclude= {"id"})
@SuppressWarnings({ "serial" })
@Entity
@Table(name = "user_media")
@EqualsAndHashCode
@Data
public class UserMedia implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name= "userid")
	private Long userid;
	
	private String username;
	
	@Column(name= "social_id")
	private String sociald;
	
	@Column(name= "social_type")
	private String socialType;
	
	@Column(name= "email")
	private String email;
	
	@Column(name= "access_token")
	private String accessToken;
	
	
	private String status;
	
	public boolean emptySocialID() {
		if(username == "null") {
			username = "";
		}
		if(sociald == "null") {
			sociald = "";
		}
		
		return StringUtils.isAllEmpty(sociald) && !StringUtils.isAllEmpty(username);
	}

}
