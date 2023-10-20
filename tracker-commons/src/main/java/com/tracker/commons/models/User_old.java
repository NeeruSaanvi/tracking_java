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
import java.time.ZonedDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tracker.commons.UserTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/*
@SuppressWarnings({ "serial" })
@Entity
@Table(name = "users")*/
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
@ToString(includeFieldNames = true, exclude= {"password", "confirmPassword"})
@Data
public class User_old extends BaseTracebleModel implements Serializable {

	@Id
	@GeneratedValue
	private Long userId;

	@Enumerated(EnumType.STRING)
	private UserTypeEnum userType; 
	
	private String email;

	private String password;

	@Transient
	private String confirmPassword;

	private String passwordSalt;

	private String firstName;

	private String lastName;
	
	private String middleName; 

	private String phone;

	private Boolean active;

	private Boolean tempDisabled;

	private Boolean permDisabled;

	private ZonedDateTime lastLogin;

	private ZonedDateTime lastFailedLogin;

	private Boolean emailValidated;

	private Boolean passwordExpired;

	private String passwordResetToken;

	private ZonedDateTime passwordResetTokenExpireTime;

	private Integer numberOfFailedLogins;

	private String gender;
	
	private LocalDate dob;

	private Integer timeZone; 
	
	@JsonIgnore
	public String getPassword() {
	    return password;
	}

	@JsonProperty
	public void setPassword(String password) {
	    this.password = password;
	}
	
	@JsonIgnore
	public String getConfirmPassword() {
	    return confirmPassword;
	}

	@JsonProperty
	public void setConfirmPassword(String confirmPassword) {
	    this.confirmPassword = confirmPassword;
	}
	
	public void setDeaults() {
		this.active = true;
		this.tempDisabled = false;
		this.permDisabled = false; 
		this.emailValidated = false;
		this.passwordExpired = false;
		this.numberOfFailedLogins = 0;
		this.timeZone = 1; 
		super.setDateCreated(ZonedDateTime.now());
		super.setDateModified(ZonedDateTime.now());
	}
	
}
