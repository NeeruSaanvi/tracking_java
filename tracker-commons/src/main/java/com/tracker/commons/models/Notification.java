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
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "Notif_templates")
@EqualsAndHashCode
@Data
public class Notification implements Serializable {

	private static  Map<String, String> typeMap = initTypeMap();
	
	private static HashMap<String, String> initTypeMap() {
		HashMap<String, String> map = new HashMap<String, String>(10);
		map.put("NEWS", "The email notification sent to pro staff when you add items to the News section.");
		map.put("EVENTS", "The email notification sent to pro staff when you add a new event.");
		map.put("MESSAGE", "The email notification sent to pro staff when you send a pro staff member a message.");
		map.put("TRAINING", "The email notification sent to pro staff when you add items to the Training Center.");
		map.put("WELCOME", "The email notification welcoming the person to your pro staff. This email also provides their AnglerTrack login information.");
		map.put("ACCEPTED", "The email letting the new pro staff member know that they have been accepted to the pro staff. This also provides their AnglerTrack login information.");
		map.put("REJECTED", "This is the email sent to the applicant if their application was rejected.");
		map.put("THANK YOU", "The Confirmation email sent to potential pro staff thanking them for submitting their application.");
		map.put("PROMOCODE", "This is the email sent to the applicant for Promo Code.");
		map.put("SOCIALPROMPT", "");
		map.put("WELCOMEEXISTINGPRO", "This is the email sent to the Existing Member added new to your Squad.");
		return map;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long ownerId;
	
	private String subject;
	private String bodytext;
	private String signature;
	private String type;
	private String attachment;
	private String attachment1;
	private String attachment2;
	private String status;
	//TODO: need to add label for each attachment in DB
	
	@Transient
	private MultipartFile attachmentFile;
	
	@Transient
	private MultipartFile attachmentFile1;
	
	@Transient
	private MultipartFile attachmentFile2;
	
	@Column(name = "created_date")
	private LocalDate createdDate;
	
	@Transient
	private String description;
	
	public String getDescription() {
		
		return typeMap.get( type.toUpperCase() );
	}

}
