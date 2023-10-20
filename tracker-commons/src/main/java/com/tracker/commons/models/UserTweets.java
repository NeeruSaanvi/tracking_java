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
@Table(name = "user_tweets")
@EqualsAndHashCode
@Data
public class UserTweets implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "user_tweets_id")
	private Long user_tweets_id;
	
	private Long user_id;
	
	private String id;
	private String text;
	private String link;
	
	private String created_at;
	private Integer retweet_count;
	private Integer favorite_count;
	
	private LocalDate download_date;
	
	@Transient
	private String createdAtFormatter;
	
}
