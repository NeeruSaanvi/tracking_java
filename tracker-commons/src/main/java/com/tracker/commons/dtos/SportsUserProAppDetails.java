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
@Table(name = "user_pro_app_details")
@EqualsAndHashCode
@Data
public class SportsUserProAppDetails implements Serializable {
	
	@Transient
	private String city;
	
	@Transient
	private String firstLastName;
	
	@Transient
	private LocalDate dob;
	
	@Transient
	private String highestEducation;
	
	@Transient
	private String street;
	
	@Transient
	private String state;
	
	@Transient
	private String zipcode;
	
	@Transient
	private String email;
	
	@Transient
	private String phone;
	
	@Transient
	private String sec_phone;
	
	@Transient
	private String profile; // for Boating application
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long appid;
	
	private Long userId;
	private Long companyID;
	private String emergency_contact_no;
	private String highest_education;
	private String hunting_categories;
	private String target_species;
	private String primary_hunting_method;
	private int num_days_peryear_field;
	private String guide;
	private String guide_service_name;
	private int guide_num_clients;
	private String guide_service_website;
	private String ride_EnglishWestern;
	private String sub_riding_style;
	private String youAre;
	private String youAre_other;
	private String circuit_regulatedBy;
	private int year_totalDays;
	private String organization_affiliated;
	private String won_nationalTitle;
	private String own_or_lease;
	private String ride_for_organization;
	private String professionalTrainer;
	private String disciplinarySanctions;
	private String performer;
	private String familiar_brands;
	
	@Column(nullable = true)
	private int exercise_schedule;
	
	private String training_style;
	private int num_comp_ambassador;
	private String background_focus;
	private String paid_event_liketo_participate;
	private String events_participated;
	private int num_events_per_yr;
	private String fb_page_link;
	private int fb_num_followers;
	private String fb_fan_page_link;
	private int fb_fan_page_followers;
	private String insta_page_link;
	private int insta_num_followers;
	private String twt_page_link;
	private int twt_num_followers;
	private String yt_channel_link;
	@Column(nullable=true)
	private Integer yt_num_subscribers;
	private String forum_details;
	private String forum_post_schedule;
	private String tv_involvement;
	private String tv_shows_list;
	private String tv_shows_air;
	private String tv_show_air_quarter;
	private int tv_avg_viewers;
	private String additionals_sponsors;
	private String activity_on_sponsor_behalf;
	private String shirt_size;
	private String head_wear;
	private String exp_sponsor_products;
	private String why_to_select;
	private String organization_list;
	private String prefer_brands;
	private String photoVideoEquipment;
	private String exp_photoVideoEditing_soft;
	private String shooting_reloadAmmunition;
	private String short_bio;
	private String attach_resume;
	private String sports;
	private String local_store;
	private String discount_code;
	private String status;
	private LocalDate posted_date;
	
	@Transient
	private String boating_categories;
	
	@Transient
	private String boat_brands;
	
	@Transient
	private String boat_freshwaterSaltwater;
	
	@Transient
	private String boat_homeBodywater;
	
	@Transient
	private String forum_posting;

}
