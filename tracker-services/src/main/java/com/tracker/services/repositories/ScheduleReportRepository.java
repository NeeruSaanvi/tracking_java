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
package com.tracker.services.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.ScheduleReport; 

@Repository
@Transactional
public interface ScheduleReportRepository extends JpaRepository<ScheduleReport, Long> {

	public ScheduleReport findByReportName(String reportName);
	
	public ScheduleReport findByScheduleId(Long scheduleId);
	
	public List<ScheduleReport> findByCreatedBy(Long createdBy);

	public Page<ScheduleReport> findAll(Pageable pageRequest);
	
	public List<ScheduleReport> findAll();


}
