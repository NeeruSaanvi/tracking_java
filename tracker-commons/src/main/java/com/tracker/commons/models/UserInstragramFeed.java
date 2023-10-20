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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "user_instagram_feed")
@EqualsAndHashCode
@Data
public class UserInstragramFeed implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "instagram_feed_id")
	private Long instagramFeedId;
	
	@Column(name = "user_id")
	private Long userId;
	
	
	private String id;
	private String text;
	private String link;
	
	private Integer likes;
	
	@Column(name = "comments_count")
	private Integer commentsCount;
	
	@Column(name = "likes_count")
	private Integer likesCount;
	
	@Column(name = "thumbnail_image")
	private String thumbnailImage;
	
	@Column(name = "standard_image")
	private String standardImage;
	
	@Column(name = "created_time")
	private String createdTime;
	
	@Transient
	private String createdTimeFormatter;
	
	private String social_type;

	
	private String postType;
	private Integer videoViewVount;
	private String userProfileThumbnailImage;
	
	@Transient
	private String keyword;
	
	@Transient
	private String firstLastname;
	
}
