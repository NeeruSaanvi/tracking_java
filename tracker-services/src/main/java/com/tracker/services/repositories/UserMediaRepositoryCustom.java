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

import java.time.LocalDate;
import java.util.List;

import com.tracker.commons.models.CompanyRef;
import com.tracker.commons.models.DashboardMostusedKeywordYearly;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserFeed;
import com.tracker.commons.models.UserInstragramFeed;
import com.tracker.commons.models.UserMedia;
import com.tracker.commons.models.UserSponsorScores;
import com.tracker.commons.models.UserTeam;
import com.tracker.commons.models.UserTweets;
import com.tracker.services.dto.PaginationRequestDTO;

public interface UserMediaRepositoryCustom {
	
	
//	public void saveAccessToken1(Long userid, String mediasource,String access_token);
//	
	 UserMedia  getAccessToken(Long userid, String mediasource);
	 void  deleteAccessToken(Long userid, String mediasource,String token);
	 void  updateAccessToken(Long userid, String mediasource,String token);

	 
	


}
