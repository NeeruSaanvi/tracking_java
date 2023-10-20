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
@Table(name = "user_youtubefeeds1")
@EqualsAndHashCode
@Data
public class UserYoutubeFeed1 implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "video_id")
	private String videoId;
	
	private String channel_id;
	
	private String title;
	private String text;
	
	private Integer total_views;
	
	private Integer total_likes;
	
	private String thumb;
	private String video_url;
	
	private String privacy;
	
	@Column(name = "created_date")
	private LocalDate createdDate;
	
	@Column(name = "download_date")
	private LocalDate downloadDate;
	
	
}
