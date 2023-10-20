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

import com.tracker.commons.models.Lead;
import com.tracker.services.dto.PaginationRequestDTO;

public interface LeadRepositoryCustom {

	public List<Lead> getLeadList(Long companyID, PaginationRequestDTO dto, Date fromDate, Date toDate, List<Long> userList);

	public Integer getLeadListCount(Long companyID, PaginationRequestDTO dto, Date fromDate, Date toDate, List<Long> userList);

}
