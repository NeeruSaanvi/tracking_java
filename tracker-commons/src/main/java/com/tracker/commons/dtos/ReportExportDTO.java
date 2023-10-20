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
package com.tracker.commons.dtos;

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
public class ReportExportDTO implements Serializable {

	private Long userId;
	private String memberName;
	private String teamName;
	private String keyword;
	private Integer totalPost;
	private Integer totalInteractions;
	private Double totalEffectivenessRate;
	private Integer totalYtViews;
	private Double percentagePost;
	private Integer totalSocialMediaPost;
	private Double postBrandedPercentange;
	
	private Integer tournamentsCount;
	private Integer blogCount;
	private Integer eventCount;
	private Integer printCount;
	private Integer leadsCount;
	private Integer totalActivity;
}
