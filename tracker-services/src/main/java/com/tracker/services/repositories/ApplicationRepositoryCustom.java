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

import com.tracker.commons.dtos.Application;
import com.tracker.commons.dtos.SportsUserProAppDetails;

public interface ApplicationRepositoryCustom {
	
	public Application fetchApplication(Long userId, Long companyId);

	public SportsUserProAppDetails fetchApplicationSports(Long userId, Long companyId);
}
