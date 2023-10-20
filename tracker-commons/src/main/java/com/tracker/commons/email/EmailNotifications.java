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
package com.tracker.commons.email;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tracker.commons.models.BaseTracebleModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "email_notifications")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(includeFieldNames=true)  


public class EmailNotifications extends BaseTracebleModel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long emailId;

	private String CC;
	private String BCC;
	private String sender;
	private String replyTo;
	private String recipients;
	private String subject;
	
	@Transient
	private String attachmentName;
	
	@Column(columnDefinition = "mediumtext")
	private String body;
	
	@Column(columnDefinition = "longblob")
	private byte[] attachment;
	
	@Column(columnDefinition = "int")
	private Boolean status = false;
	private Integer retryCount = 0; 

}
