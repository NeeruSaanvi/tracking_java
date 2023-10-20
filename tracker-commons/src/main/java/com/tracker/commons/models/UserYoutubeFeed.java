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
@Table(name = "user_youtubefeeds")
@EqualsAndHashCode
@Data
public class UserYoutubeFeed implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;
	
	private String video_id;
	private String channel_id;
	
	private String title;
	private String text;
	
	private Integer total_views;
	
	private Integer total_likes;
	
	private String thumb;
	private String video_url;
	
	private String privacy;
	
	@Column(name = "created_time")
	private LocalDate createdTime;
	
	@Column(name = "download_date")
	private LocalDate downloadDate;
	
	
}
