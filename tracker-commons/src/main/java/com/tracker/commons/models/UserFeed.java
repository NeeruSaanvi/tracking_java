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
@Table(name = "user_feed")
@EqualsAndHashCode
@Data
public class UserFeed implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "user_feed_id")
	private Long user_feed_id;
	
	@Column(name = "user_id")
	private Long user_id;
	
	
	private String id;
	private String story;
	private String link;
	
	@Column(name = "likes_count")
	private Integer likes_count;
	
	@Column(name = "comment_count")
	private Integer comment_count;
	
	@Column(name = "share_count")
	private Integer share_count;
	
	@Column(name = "view_count")
	private Integer view_count;
	
	@Column(name = "created_time")
	private LocalDate created_time;
	
	@Column(name = "feed_type")
	private String feed_type;
	
	@Column(name = "photo_url")
	private String photo_url;
	
	@Column(name = "post_type")
	private String post_type;
	
	@Transient
	private String createdTimeFormatter;
	
}
