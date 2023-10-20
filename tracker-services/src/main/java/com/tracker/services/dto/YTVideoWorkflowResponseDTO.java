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
package com.tracker.services.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString(includeFieldNames = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class YTVideoWorkflowResponseDTO implements Serializable {
	
	@JsonProperty("items[0].snippet.title")
	private String title;
	
	private String video_id;
	
	@JsonProperty("items[0].snippet.channelId")
	private String channel_id;
	
	@JsonProperty("items[0].snippet.description")
	private String text;
	
	@JsonProperty("items[0].snippet.thumbnails.default.url")
	private String thumb;
	
	@JsonProperty("items[0].statistics.viewCount")
	private String viewCount;
	
	@JsonProperty("items[0].statistics.likeCount")
	private String likeCount;
	
	@JsonProperty("items[0].status.privacyStatus")
	private String privacyStatus;
	
	@JsonProperty("items[0].snippet.publishedAt")
	private String publishedAt;
	
	private Long user_id;
	
	private String status;

}
