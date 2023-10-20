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

import com.tracker.commons.dtos.DashboardActiveMembersResponse;
import com.tracker.commons.dtos.DashboardSocialActivityPerSiteResponse;
import com.tracker.commons.models.CompanyRef;
import com.tracker.commons.models.DashboardMostusedKeywordYearly;
import com.tracker.commons.models.DashboardSocialmediaPerSite;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserInstragramFeed;

public interface DashboardRepositoryCustom {

	public Integer getTotalFBFeedCount(Long userId, String keywordsFB, String period, String feedType);

	public Integer getTotalINFeedCount(Long userId, String keywordsIN, String period, String feedType);

	public Integer getTotalTWFeedCount(Long userId, String keywordsTW, String period, String feedType);

	public DashboardSocialActivityPerSiteResponse getTotalYTFeedCount(Long userId, String keywordsYT,
			String period, String feedType);
	
	public List<DashboardSocialmediaPerSite> fetchTotalFeebByClient(Long companyId);
	
	public DashboardActiveMembersResponse getMemberCount(Long userId);

	public List<Object[]> getTotalFBFeedByClientAndDate(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds);

	public List<Object[]> getTotalInstaFeedByClientAndDate(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds);

	public List<Object[]> getTotalTwFeedByClientAndDate(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds);

	public List<Object[]> getTotalYtFeedByClientAndDate(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds);

	public Integer getMostUsedKeywordFb(Long userId, String word, String socialType, Date fromDate, Date toDate);

	public List<User> getMembers(Long companyID);

	public List<UserInstragramFeed> getInstagramImages(Long companyID, String keyword);

	public List<CompanyRef> getRanklist(Long companyID);
	public List<CompanyRef> getRanklist1(Integer companyID);

	public List<DashboardMostusedKeywordYearly> getMostUsedKeyword(Long companyId);
}
