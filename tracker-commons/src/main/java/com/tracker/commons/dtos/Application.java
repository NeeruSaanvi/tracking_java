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
import java.time.LocalDate;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application implements Serializable {

	private Long userId;
	private String firstName;
	private String lastName;	
	private String[] userTeam;
	private String teamArray;
	private String discountCode;
	private Long FB;
	private Long twt;
	private Long Insta;
	private String firstLastName;
	private LocalDate dob;
	private String highestEducation;
	private String compName;
	private Integer companyId;
	private String email;
	private String phone;
	private String loginName;
	private String password;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	private String latitude;
	private String longitude;
	private String notifemail;
	private String notifySnapshotEmailId;
	private String atProfilePic = "Node";
	private String membershipType;
	private LocalDate membershipExpiry;
	private String memCustProfileId;
	private String fishingType;
	private String fishingOrganization;
	private String fbKeywords;
	private String twtKeywords;
	private String instaKeywords;
	private String youtubeKeywords;
	private String memType;
	private String acessType;
	private String memStatus;
	private String settings;
	private String status;
	private String userLogo;
	private String userDocument;
	private String notes;
	private LocalDate atRefreshDate;
	private String deviceToken;
	private LocalDate registrationDate;

	private String confirmPassword;
	
	private String passwordResetToken;
	
	private ZonedDateTime passwordResetTokenExpireTime;
	
	private String ship_street;
	private String ship_city;
	private String ship_state;
	private String ship_zipcode;
	private String sec_phone;
	private String references_1;
	private String references_2;
	private String references_3;
	
	private String shirt_size;
	private String glove_size;
	private String pant_size;
	private String prefer_hardware;
	
	private String home_lake_river;
	private String species_type;
	private String tournament_fish_year;
	private String isfish_tournament_trails;
	private String water_type;
	private String isguide;
	private String isguidelicence;
	private String license_number;
	private String guideyear;
	private String iswebsite;
	private String flyfishingcareer_1;
	private String flyfishingcareer_2;
	
	private String isusedproducts;
	private String useproducts;
	private String experiencewithproducts;
	
	private String social_media_platform;
	private String facebook_personal_page;
	private String link_to_facebook_fan_page;
	private String link_to_facebook_personal_page;
	private String twitter_personal_page;
	private String link_to_twitter_personal_page;
	private String link_to_twitter_fan_page;
	private String link_to_instagram_personal_page;
	private String instagram_fan_page;
	private String isactive_on_forum_blog;
	private String active_on_fishing_blog_1;
	private String active_on_fishing_blog_2;
	private String active_on_fishing_blog_3;
	private String active_on_fishing_blog_4;
	private String active_on_fishing_blog_5;
	private String active_on_fishing_blog_6;
	private String active_on_fishing_blog_7;
	private String active_on_fishing_blog_8; 
	private String active_on_fishing_blog_9;
	private String active_on_fishing_blog_10; 
	private String active_on_fishing_blog_11;
	private String active_on_fishing_blog_12;
	
	private String other_sposors_1;
	private String other_sposors_2;
	private String other_sposors_3;
	private String other_sposors_4;
	private String other_sposors_5;
	private String other_sposors_6;
	private String other_sposors_7;
	private String other_sposors_8;
	private String other_sposors_9;
	private String other_sposors_10;
	private String other_sposors_11;
	private String other_sposors_12;
	private String select_fishing_pro_staff;
	private String other_information;
	private String attach_resume;
	private String upload_pictures;

}
