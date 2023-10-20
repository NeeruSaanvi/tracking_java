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
import java.time.OffsetDateTime;
import java.time.Instant;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tracker.commons.dtos.Application1;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "users")
@EqualsAndHashCode
@ToString(includeFieldNames = true, exclude= {"password", "confirmPassword"})
@Data
public class User /*extends BaseTracebleModel*/ implements Serializable {

	@Transient
	String linkedMediaHTML;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@Transient
	private String firstName;
	
	@Transient
	private String lastName;	
	
	@Transient
	private String[] userTeam;
	
	@Transient
	private String[] userTeamName;
	
	@Transient
	private String teamArray;
	
	@Transient
	private String discountCode;
	
	@Transient
	private Long FB;
	
	@Transient
	private Long twt;
	
	@Transient
	private Long Insta;
	
	@Transient
	private String profilePic;
	
	@Transient
	private String dobFormatter;
	
	@Transient
	private String shirtSize;
	
	@Transient
	private String gloveSize;
	
	@Transient
	private String instaUserName;
	
	@Transient
	private String youtubeUserName;
	
	@Transient
	private String instaUserId;
	
	@Transient
	private List<LinkedSocialMedia> mediaList;
	
	
	
	
	private String firstLastName;
	private LocalDate dob;
	private String highestEducation;
	private String compName;
	private Integer companyId;
	private String email;
	private String phone;
	private String loginName;
	private String password;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	private String latitude;
	private String longitude;
	private String notifemail;
	
	@Column(name = "notify_snapshot_emailid")
	private String notifySnapshotEmailId;
	
	@Column(name = "at_profile_pic")
	private String atProfilePic = "None";
	
	private String membershipType;
	private LocalDate membershipExpiry;
	private String memCustProfileId;
	private String fishingType;
	private String fishingOrganization;
	private String fbKeywords;
	private String twtKeywords;
	private String instaKeywords;
	private String youtubeKeywords;
	private String memType;
	
	@Column(name = "acess_type")
	private String acessType;
	
	@Column(name = "mem_status")
	private String memStatus;
	
	private String settings;
	private String status;
	
	@Column(name = "user_logo")
	private String userLogo;
	
	@Column(name = "user_document")
	private String userDocument;
	
	private String notes;
	
	@Column(name = "at_refresh_date")
	private LocalDate atRefreshDate;
	
	@Column(name = "device_token")
	private String deviceToken;
	
	@Column(name = "registration_date")
	private LocalDate registrationDate;

	@Transient
	private String confirmPassword;
	
	private String passwordResetToken;
	
	private ZonedDateTime passwordResetTokenExpireTime;
	
	private int migrated;
	
	@Transient
	private Application1 applicationData;
	
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
		//super.setDateCreated(ZonedDateTime.now());
		//super.setDateModified(ZonedDateTime.now());
	}
	
	public String getLinkedMediaHTML() {
		
		String retVal = "<span>";
		if( mediaList != null) {
			
			for (int i = 0; i < mediaList.size(); i++) {
				if("facebook".equalsIgnoreCase(mediaList.get(i).getSocialType())){
					retVal +="<i class=\"flaticon-facebook-logo-button  kt-font-brand\"  style=\"font-size:30px; text-align:center;\"></i>&nbsp;";
				}
				else if("instagram".equalsIgnoreCase(mediaList.get(i).getSocialType())){
					retVal +="<i class=\"flaticon-instagram-logo  kt-font-brand\"  style=\"font-size:30px;\"></i>&nbsp;";
				}
				else if("twitter".equalsIgnoreCase(mediaList.get(i).getSocialType())){
					retVal +="<i class=\"flaticon-twitter-logo-button  kt-font-brand\" style=\"font-size:30px;\"></i>&nbsp;";
				}
				else if("youtube".equalsIgnoreCase(mediaList.get(i).getSocialType())){
					retVal += "<i class=\"flaticon-youtube  kt-font-brand\"  style=\"font-size:30px;\"></i>&nbsp;";
				}
			}
		} 
		return retVal.concat("</span>");
	}
	
	public void setFirstLastName(String firstLastName) {
		this.firstLastName = firstLastName;
		String names[] = firstLastName.split(" ");
		this.firstName = names[0];
		if ( names.length ==2 ) {
			this.lastName =  StringUtils.isEmpty(names[1]) ? names[0] : names[1];
		} else {
			this.lastName = "";
		}
	}
	
	public String getPhone() {
		return StringUtils.isEmpty(phone) ? "0000000000" : phone;
	}

	/*
	CREATE TABLE `users` (
			  `userid` int(11) NOT NULL AUTO_INCREMENT,
			  `FirstLastname` varchar(150) NOT NULL,
			  `dob` date DEFAULT NULL,
			  `highesteducation` varchar(500) DEFAULT NULL,
			  `compname` varchar(150) NOT NULL,
			  `companyID` int(9) DEFAULT NULL,
			  `email` varchar(200) NOT NULL,
			  `phone` varchar(100) NOT NULL,
			  `loginname` varchar(50) NOT NULL,
			  `password` varchar(300) NOT NULL,
			  `street` varchar(800) DEFAULT NULL,
			  `city` varchar(200) DEFAULT NULL,
			  `state` varchar(200) DEFAULT NULL,
			  `zipcode` varchar(100) DEFAULT NULL,
			  `latitude` decimal(10,8) DEFAULT '0.00000000',
			  `longitude` decimal(11,8) DEFAULT '0.00000000',
			  `notifemail` varchar(700) DEFAULT NULL,
			  `notify_snapshot_emailid` text,
			  `at_profile_pic` varchar(8000) DEFAULT 'None',
			  `membershiptype` varchar(200) DEFAULT NULL,
			  `membershipexpiry` date DEFAULT NULL,
			  `memcustprofileID` varchar(1000) DEFAULT NULL,
			  `fishingtype` varchar(500) DEFAULT 'Freshwater',
			  `fishingorganization` varchar(800) DEFAULT NULL,
			  `fbkeywords` text CHARACTER SET utf8,
			  `twtkeywords` text CHARACTER SET utf8,
			  `instakeywords` text CHARACTER SET utf8,
			  `youtubekeywords` text CHARACTER SET utf8,
			  `memtype` varchar(20) NOT NULL,
			  `acess_type` varchar(700) DEFAULT 'Regular' COMMENT 'Regular,Hybrid',
			  `mem_status` varchar(200) DEFAULT 'AT',
			  `settings` varchar(500) DEFAULT 'no',
			  `status` varchar(20) NOT NULL,
			  `user_logo` varchar(1000) DEFAULT NULL,
			  `user_document` varchar(1000) DEFAULT NULL,
			  `notes` text,
			  `at_refresh_date` date DEFAULT NULL,
			  `device_token` text CHARACTER SET utf8,
			  `registration_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
			  PRIMARY KEY (`userid`),
			  KEY `userid` (`userid`),
			  KEY `memtype` (`memtype`),
			  KEY `status` (`status`)
			) ENGINE=InnoDB AUTO_INCREMENT=9070 DEFAULT CHARSET=latin1;

*/

}
