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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings({ "serial" })
@Entity
@Table(name = "user_store")
@EqualsAndHashCode
@Data
public class UserStore implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id")
	private Long storeId;
	
	@Column(name = "store_userID")
	private Long storeUserID;
	
	private LocalDate store_date;
	private String store_name;
	private String store_time_CheckIN;
	private String store_chkIn_manager_Duty;
	private String store_chkIn_dept_manager;
	private Long store_inventory_level;
	private String store_comp_win_shelf_space;
	private Long store_shelves_condition;
	private Long store_end_caps_aisle_look;
	private String store_feedback;
	private String store_new_product_trend;
	private String store_brand_observed_discuss;
	private String store_event_name_people;
	private String store_photos;
	private String store_notes;
	private String store_followup;
	private LocalDate posted_date_time;

	@Transient
	private String email;
	
	@Transient
	private String firstLastname;
	
	@Transient
	private String store_end_caps_aisle_lookStr;
	
	@Transient
	private String store_shelves_conditionStr;
	
	@Transient
	private String store_inventory_levelStr;
}
