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

import java.util.Date;
import java.util.List;

import com.tracker.commons.dtos.ReportExportDTO;
import com.tracker.commons.dtos.ReportGraphResponse;
import com.tracker.commons.dtos.ReportStatResponse;
import com.tracker.commons.models.Reports;
import com.tracker.services.dto.PaginationRequestDTO;

public interface ReportsRepositoryCustom {

	public List<Reports> getReports(PaginationRequestDTO dto, Date fromDate, Date toDate, String staff,
			String keyword, Boolean count, Boolean graphQuery, Boolean fromExport, Long comoanyId);

	public List<ReportGraphResponse> getReportsGraphs(PaginationRequestDTO dto, Date fromDate, Date toDate, String staff,
			String keyword, Boolean count, String graphType, Long userId);

	public ReportStatResponse getReportsStat(PaginationRequestDTO dto, Date fromDate, Date toDate, String staff,
			String keyword, Boolean count, Long companyId);

	public List<Reports> generateSchduleReport(Date fromDate, Date toDate, List<String> staff, Long companyId);
	
	public List<ReportExportDTO> fetchReportsByKeyWords(Date fromDate, Date toDate, String staff, List<String> keywordArrayList, Integer grandTotalPost, Long companyId);

	public List<Object[]> getTotalFBFeedByUser(List<Long> userId, String keywords, Date fromDate, Date toDate,
			String appendQuery, Boolean graphQuery);

	public List<Object[]> getTotalINFeedByUser(List<Long> userId, String keywords, Date fromDate, Date toDate,
			String appendQuery, Boolean graphQuery);

	public List<Object[]> getTotalTWFeedByUser(List<Long> userId, String keywords, Date fromDate, Date toDate,
			String appendQuery, Boolean graphQuery);


}
