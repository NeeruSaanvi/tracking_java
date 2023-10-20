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

public interface UserRepositoryCustom {
	
	public List<User> findAllUsersByCompany(String name, String email, PaginationRequestDTO dto, Boolean count);
	
	public String getProfilePicMS(Long userid, String mediasource);
	public void saveAccessToken(Long userid, String mediasource,String access_token);
	
	public String getAccessToken(Long userid, String mediasource);

	
	public void logUserPwdTrack(Long userId, String email, String fromPage, String password, LocalDate date);
	
	public void dashboardMostusedKeyword(DashboardMostusedKeywordYearly dashboardResponse);

	public List<User> getApplicationList(String profile, Long companyID, PaginationRequestDTO dto);
	
	public List<CompanyRef> getAnnualRankingListByCompnay(Long companyID, Long userId);
	
	public Integer getApplicationListCount(String profile, Long companyId, PaginationRequestDTO dto);
	
	public UserSponsorScores fetchScoreSettings(Long companyId);

	public List<UserFeed> fetchFBData(Long companyId, Long userId, String keywords);

	public List<UserTweets> fetchTWData(Long companyId, Long userId, String keywords);

	public List<UserInstragramFeed> fetchInstaData(Long companyId, Long userId, String keywords);
	
	public List<UserTeam> fetchTeamByUserId(Long userId);

	
	public List<UserTeam> fetchTeamByUserId(Long userId,List<Long> teampIds);

	public List<UserMedia> fetchUserMedia();
	
	public String getSports(Long userid);

	public List<UserMedia> fetchYoutubeUserMedia();

	public List<UserMedia> fetchInstagramUser();

	public List<Long> fetchSponserTeamByUserId(Long userId);

}
