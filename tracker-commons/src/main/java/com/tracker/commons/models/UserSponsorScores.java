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
@Table(name = "user_sponsor_scores")
@EqualsAndHashCode
@Data
public class UserSponsorScores implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long companyID;
	private Integer fbpost;
	private Integer fblike;
	private Integer fbcomments;
	private Integer fbshares;
	private Integer twttweets;
	private Integer twtretweets;
	private Integer twtfavourites;
	private Integer instaposts;
	private Integer instalikes;
	private Integer instacomments;
	private Integer ytposts;
	private Integer ytlikes;
	private Integer ytviews;
	private Integer leadsubmt;
	private Integer eventworked;
	private Integer tournaments;
	private Integer webhits;
	private Integer prints;
	
	@Column(name = "download_date")
	private LocalDate download_date;

}
