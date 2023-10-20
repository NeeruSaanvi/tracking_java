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
package com.tracker.services.impls;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.commons.models.Reports;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.ReportsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportsService {

	@Autowired
	private ReportsRepository reportsRepository;
	
	public List<Reports> fetchReports(final PaginationRequestDTO dto, Date fromDate, Date toDate, String staff,
			String keyword, Boolean count, Boolean graphQuery, Boolean fromExport, Long comoanyId) {
		log.debug("Fetching reports");
		
		List<Reports> report = reportsRepository.getReports(dto, fromDate, toDate, staff, keyword, count, graphQuery, fromExport, comoanyId);
		return report;
	}

}
