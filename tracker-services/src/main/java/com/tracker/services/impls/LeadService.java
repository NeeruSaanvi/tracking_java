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

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.commons.models.Lead;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.LeadRepository;
import com.tracker.services.utils.JasperUtils;

@Service
public class LeadService {

	@Autowired
	private LeadRepository leadRepository;
	
	@Autowired
	private JasperUtils jasperUtils;

	
	public List<Lead> getLeadList(Long companyId, PaginationRequestDTO dto, Date fromDate, Date toDate, List<Long> userList){
		return leadRepository.getLeadList(companyId, dto, fromDate, toDate, userList);
	}
	
	public int getLeadListCount(Long companyId, PaginationRequestDTO dto, Date fromDate, Date toDate, List<Long> userList){
		return leadRepository.getLeadListCount(companyId, dto, fromDate, toDate, userList);
	}
	
	public ByteArrayOutputStream generateExcelReport(Long companyId, Date fromDate, Date toDate, List<Long> userList)
			throws Exception {
		
		Map<String, Object> properties = new HashMap<String, Object>();
		
		List<Lead> leads = leadRepository.getLeadList(companyId, null, fromDate, toDate, userList);
		
		ByteArrayOutputStream out = jasperUtils.generateLeadExcel("Reports", properties, leads);
		return out;
		
	}

}
