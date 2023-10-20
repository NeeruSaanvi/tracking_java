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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString; 

@SuppressWarnings({ "serial" })
@Entity
@Table(name="config_properties")
@ToString(includeFieldNames = true, callSuper = true)
@AllArgsConstructor()
@NoArgsConstructor()
@Data
public class ConfigProperties implements Serializable {

	@Id
	@GeneratedValue 
	private Long Id; 

	private String property;

	@Column(nullable = true)
	private String propertyValue; 

}
