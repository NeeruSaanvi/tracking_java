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
public class Application1 implements Serializable {

	private String FirstLastname;
	private String dob;
	private String state;	
	private String email;
	private String phone;
	private String street;
	private String city;
	private String zipcode;
	private String ref_status;
	private String appid;
	private String userid;
	private String highesteducation;
	private String current_gpa;
	private String shirt_size;
	private String glove_size;
	private String already_sponsored_categories;
	private String home_lake_river;
	private String species_type;
	private String isguide;
	private String school_college_angler;
	private String isusedproducts;
	private String useproducts;
	private String experiencewithproducts;
	private String media_platforms;
	
	private String fb_num_personalpage_followers;
	private String fb_num_fanpage_followers;
	private String twt_num_personalpage_followers;
	private String insta_num_personalpage_followers;
	private String link_to_facebook_personal_page;
	private String link_to_facebook_fan_page;
	private String link_to_twitter_personal_page;
	private String link_to_instagram_personal_page;
	private String isactive_on_forum_blog;
	private String isothersposors;
	private String other_sposors_1;
	private String select_fishing_pro_staff;
	private String other_information;
	private String video_link;
	private String isfish_tournament_trails;
	private String tournament_fish_year;
	private String bass_college_fishing_school;
	
	
	private String isfish_saltwater;
	private String isown_boat;
	private String isown_boatBrand;
	private String top_moments_career;
	private String tv_web_involvement;
	private String isvideo_fishing_trips_post;
	private String guilty_game_violation;
	private String photoVideoEquipment;
	
	private String exp_photoVideoEditing_soft;
	private String attach_resume;
	private String upload_pictures;
	private String status;
	
	private String posted_date;
	
	
	//Sportsman
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
	private int yt_num_subscribers;
	private String forum_details;
	private String forum_post_schedule;
	private String tv_involvement;
	private String tv_shows_list;
	private String tv_shows_air;
	private String tv_show_air_quarter;
	private int tv_avg_viewers;
	private String additionals_sponsors;
	private String activity_on_sponsor_behalf;
	private String head_wear;
	private String exp_sponsor_products;
	private String why_to_select;
	private String organization_list;
	private String prefer_brands;
	private String shooting_reloadAmmunition;
	private String short_bio;
	private String sports;
	private String local_store;
	private String discount_code;
	
	private String boating_categories;
	private String boat_brands;
	private String boat_freshwaterSaltwater;
	private String boat_homeBodywater;
	private String forum_posting;
}
