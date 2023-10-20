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
public class YTChannelWorkflowResponseDTO implements Serializable {
	
	@JsonProperty("items[0].snippet.localized.title")
	private String title;
	
	@JsonProperty("items[0].contentDetails.relatedPlaylists.uploads")
	private String uploads;
	
	@JsonProperty("snippet.resourceId.videoId")
	private String videoId;
	
	@JsonProperty("snippet.playlistId")
	private String playlistId;
	
	private String status_code;
	
	private String id;

}
