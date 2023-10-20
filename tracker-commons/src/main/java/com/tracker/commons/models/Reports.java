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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings({ "serial" })
@EqualsAndHashCode
@ToString(includeFieldNames = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reports implements Serializable {

	private Long userId;
	private Long teamId;
	private String teamName;
	private String name;
	
	private Integer fbFeedCount;
	private Integer fbLikeCount;
	private String fbLikeCountFormatter;
	private Integer fbCommentCount;
	private Integer fbShareCount;
	
	private Integer twFeedCount;
	private Integer twReTweetCount;
	private Integer twFavCount;
	
	private Integer inFeedCount;
	private Integer inLikeCount;
	private String inLikeCountFormatter;
	private Integer inCommentCount;
	
	private Integer ytFeedCount;
	private Integer ytViewCount;
	
	private Integer tournamentsCount;
	private Integer blogCount;
	private Integer eventCount;
	private Integer printCount;
	private Integer leadsCount;
	
	private Integer totalActivity;
	private String totalActivityFormatter;
	
	private Integer totalPost;
	private Integer totalInteractions;
	private Double totalEffectivenessRate;

}
