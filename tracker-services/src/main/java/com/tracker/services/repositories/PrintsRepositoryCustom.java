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

import com.tracker.commons.models.Prints;
import com.tracker.services.dto.PaginationRequestDTO;

public interface PrintsRepositoryCustom {

	public List<Prints> getPrintList(Long companyID, PaginationRequestDTO dto);

	public Integer getPrintListCount(Long companyID, PaginationRequestDTO dto);

}
