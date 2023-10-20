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
@Table(name = "dashboard_socialmedia_per_site")
@EqualsAndHashCode
@Data
public class DashboardSocialmediaPerSite implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Long companyId;
	
	@Transient
	private Double totalInteractionRateMonthlyFb;
	
	@Transient
	private Double totalInteractionRateQuarterlyFb;
	
	@Transient
	private Double totalInteractionRateYearlyFb;
	
	@Transient
	private Double totalInteractionRateMonthlyIn;
	
	@Transient
	private Double totalInteractionRateQuarterlyIn;
	
	@Transient
	private Double totalInteractionRateYearlyIn;
	
	@Transient
	private Double totalInteractionRateMonthlyTw;
	
	@Transient
	private Double totalInteractionRateQuarterlyTw;
	
	@Transient
	private Double totalInteractionRateYearlyTw;
	
	@Transient
	private Double totalInteractionRateMonthlyYt;
	
	@Transient
	private Double totalInteractionRateQuarterlyYt;
	
	@Transient
	private Double totalInteractionRateYearlyYt;
	
	private Integer totalPostMonthlyFb; 
	private Integer totalInteractionMonthlyFb;
	private Integer totalPostQuarterlyFb;
	private Integer totalInteractionQuarterlyFb;
	private Integer totalPostYearlyFb;
	private Integer totalInteractionYearlyFb;
	private Integer totalPostYTDFb;
	private Integer totalInteractionYTDFb;
	 
	private Integer totalPostMonthlyIn;
	private Integer totalInteractionMonthlyIn;
	private Integer totalPostQuarterlyIn;
	private Integer totalInteractionQuarterlyIn;
	private Integer totalPostYearlyIn;
	private Integer totalInteractionYearlyIn;
	private Integer totalPostYTDIn;
	private Integer totalInteractionYTDIn;
	 
	private Integer totalPostMonthlyTw;
	private Integer totalInteractionMonthlyTw;
	private Integer totalPostQuarterlyTw;
	private Integer totalInteractionQuarterlyTw;
	private Integer totalPostYearlyTw;
	private Integer totalInteractionYearlyTw;
	private Integer totalPostYTDTw;
	private Integer totalInteractionYTDTw;
	
	private Integer totalPostMonthlyYt;
	private Integer totalInteractionMonthlyYt;
	private Integer totalPostQuarterlyYt;
	private Integer totalInteractionQuarterlyYt;
	private Integer totalPostYearlyYt;
	private Integer totalInteractionYearlyYt;
	private Integer totalPostYTDYt;
	private Integer totalInteractionYTDYt;

}
