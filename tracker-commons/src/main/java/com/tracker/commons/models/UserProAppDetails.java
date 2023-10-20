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
@Table(name = "user_pro_app_details")
@EqualsAndHashCode
@Data
public class UserProAppDetails implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long appId;
	
	private Long userId;
	
	private Long companyId;
	
	private String status;
	
	@Column(name = "discount_code")
	private String discountCode;
	
	@Column(name = "registration_date")
	private LocalDate registrationDate;
	
	private String highesteducation;
	private String shirt_size;
	private String glove_size;
	private String home_lake_river;
	private String species_type;
	private String tournament_fish_year;
	private String isfish_tournament_trails;
	private String isfish_saltwater;
	private String isown_boat;
	private String isusedproducts;
	private String useproducts;
	private String experiencewithproducts;
	private String media_platforms;
	private String facebook_personal_page;
	private String link_to_facebook_personal_page;
	private String facebook_fan_page;
	private String link_to_facebook_fan_page;
	private String twitter_personal_page;
	private String link_to_twitter_personal_page;
	private String instagram_personal_page;
	private String link_to_instagram_personal_page;
	private String isothersposors;
	private String other_sposors_1;
	private String select_fishing_pro_staff;
	private String other_information;
	private String video_link;
	
	

}
