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
@Table(name = "dashboardstats")
@EqualsAndHashCode
@Data
public class DashboardStats implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "ds_id")
	private Long dsId;
	
	@Column(name = "ds_companyID")
	private Long dsCompanyID;
	
	@Column(name = "fb_totalpost")
	private Integer fbTotalpost;
	
	@Column(name = "fb_totalinteraction")
	private Integer fbTotalInteraction;
	
	@Column(name = "twt_totalpost")
	private Integer twtTotalPost;
	
	@Column(name = "twt_totalinteraction")
	private Integer twtTotalInteraction;
	
	@Column(name = "insta_totalpost")
	private Integer instaTotalPost;
	
	@Column(name = "insta_totalinteraction")
	private Integer instaTotalInteraction;
	
	@Column(name = "yt_totalpost")
	private Integer ytTotalPost;
	
	@Column(name = "yt_totalinteraction")
	private Integer ytTotalInteraction;
	
	@Column(name = "fb_totalpost_quartly")
	private Integer fbTotalpostQuartly = 0;
	
	@Column(name = "fb_totalinteraction_quartly")
	private Integer fbTotalInteractionQuartly;
	
	@Column(name = "twt_totalpost_quartly")
	private Integer twtTotalPostQuartly;
	
	@Column(name = "twt_totalinteraction_quartly")
	private Integer twtTotalInteractionQuartly;
	
	@Column(name = "insta_totalpost_quartly")
	private Integer instaTotalPostQuartly;
	
	@Column(name = "insta_totalinteraction_quartly")
	private Integer instaTotalInteractionQuartly;
	
	@Column(name = "yt_totalpost_quartly")
	private Integer ytTotalPostQuartly;
	
	@Column(name = "yt_totalinteraction_quartly")
	private Integer ytTotalInteractionQuartly;
	
	@Column(name = "fb_totalpost_yearly")
	private Integer fbTotalpostYearly;
	
	@Column(name = "fb_totalinteraction_yearly")
	private Integer fbTotalInteractionYearly;
	
	@Column(name = "twt_totalpost_yearly")
	private Integer twtTotalPostYearly;
	
	@Column(name = "twt_totalinteraction_yearly")
	private Integer twtTotalInteractionYearly;
	
	@Column(name = "insta_totalpost_yearly")
	private Integer instaTotalPostYearly;
	
	@Column(name = "insta_totalinteraction_yearly")
	private Integer instaTotalInteractionYearly;
	
	@Column(name = "yt_totalpost_yearly")
	private Integer ytTotalPostYearly;
	
	@Column(name = "yt_totalinteraction_yearly")
	private Integer ytTotalInteractionYearly;
	
	@Column(name = "top_pro_userid")
	private Integer topProUserId;
	
	@Column(name = "top_pro_FirstLastname")
	private String topProFirstLastName;
	
	@Column(name = "top_pro_total_activity")
	private Double topProTotalActivity;
	
	@Column(name = "ds_updated_time")
	private LocalDate dsUpdatedTime;
	
	
}
