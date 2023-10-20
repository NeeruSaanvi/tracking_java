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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.commons.dtos.DashboardMostUsedKeuwordsResponse;
import com.tracker.commons.dtos.DashboardSocialActivityPerSiteResponse;
import com.tracker.commons.dtos.DashboardSocialMediaStats;
import com.tracker.commons.models.DashboardMostusedKeywordYearly;
import com.tracker.commons.models.DashboardSocialmediaPerSite;
import com.tracker.commons.models.DashboardStats;
import com.tracker.services.repositories.DashboardMostusedKeywordYearlyRepository;
import com.tracker.services.repositories.DashboardRepository;
import com.tracker.services.repositories.DashboardStatRepository;
import com.tracker.services.repositories.ReportsRepository;
import com.tracker.services.utils.UserUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DashboardService {

	@Autowired
	private DashboardRepository dashboardRepository;
	
	@Autowired
	private DashboardStatRepository dashboardStatRepository;
	
	@Autowired
	private ReportsRepository reportsRepository;
	
	@Autowired
	private DashboardMostusedKeywordYearlyRepository dashboardMostusedKeywordYearlyRepository;
	
	private NumberFormat numberFormat = NumberFormat.getInstance();
	
	
	public DashboardSocialMediaStats fetchTotalFbFeebByClient(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds) {
		
		log.debug("fetchTotalFbFeebByClient called");
		
		//val objectList = dashboardRepository.getTotalFBFeedByClientAndDate(companyId, keywords, fromDate, toDate, userIds);
		val objectList = reportsRepository.getTotalFBFeedByUser(userIds, keywords, fromDate, toDate, "", false);
		
		val response = new DashboardSocialMediaStats();
		Integer recTotal = 0, recTotalLike = 0, recTotalComment = 0, recTotalShare = 0;
		for (Object[] obj : objectList) {
			recTotal = ((BigInteger) obj[0]).intValue();
			recTotalLike = ((BigDecimal) obj[1]).intValue();
			recTotalComment = ((BigDecimal) obj[2]).intValue();
			recTotalShare = Integer.parseInt(obj[3].toString());
		}

		response.setTotalPost(recTotal);
		response.setTotalInteraction(recTotalLike + recTotalComment + recTotalShare);
		response.setTotalInteractionRate(response.getTotalPost() == 0 ? 0D
					: Double.valueOf(response.getTotalInteraction() / Double.valueOf(response.getTotalPost())));
		
		return response;
	}
	
	public DashboardSocialMediaStats fetchTotalInstaFeebByClient(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds) {
		
		//val objectList = dashboardRepository.getTotalInstaFeedByClientAndDate(companyId, keywords, fromDate, toDate, userIds);
		val objectList = reportsRepository.getTotalINFeedByUser(userIds, keywords, fromDate, toDate, "", false);
		
		val response = new DashboardSocialMediaStats();
		Integer recTotal = 0, recTotalLike = 0, recTotalComment = 0;
		for (Object[] obj : objectList) {
			recTotal = ((BigInteger) obj[0]).intValue();
			recTotalLike = ((BigDecimal) obj[1]).intValue();
			recTotalComment = ((BigDecimal) obj[2]).intValue();
		}

		response.setTotalPost(recTotal);
		response.setTotalInteraction(recTotalLike + recTotalComment);
		response.setTotalInteractionRate(response.getTotalPost() == 0 ? 0D
					: Double.valueOf(response.getTotalInteraction() / Double.valueOf(response.getTotalPost())));
		
		return response;
	}
	
	public DashboardSocialMediaStats fetchTotalTwFeebByClient(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds) {
		
		//val objectList = dashboardRepository.getTotalTwFeedByClientAndDate(companyId, keywords, fromDate, toDate, userIds);
		val objectList = reportsRepository.getTotalTWFeedByUser(userIds, keywords, fromDate, toDate, "", false);
		
		val response = new DashboardSocialMediaStats();
		Integer recTotal = 0, recTotalLike = 0, recTotalComment = 0;
		for (Object[] obj : objectList) {
			recTotal = ((BigInteger) obj[0]).intValue();
			recTotalLike = ((BigDecimal) obj[1]).intValue();
			recTotalComment = ((BigDecimal) obj[2]).intValue();
		}

		response.setTotalPost(recTotal);
		response.setTotalInteraction(recTotalLike + recTotalComment);
		response.setTotalInteractionRate(response.getTotalPost() == 0 ? 0D
					: Double.valueOf(response.getTotalInteraction() / Double.valueOf(response.getTotalPost())));
		
		return response;
	}
	
	public DashboardSocialMediaStats fetchTotalYtFeebByClient(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds) {
		
		val objectList = dashboardRepository.getTotalYtFeedByClientAndDate(companyId, keywords, fromDate, toDate, userIds);
		
		val response = new DashboardSocialMediaStats();
		Integer recTotal = 0, recTotalView = 0;
		for (Object[] obj : objectList) {
			recTotal = ((BigInteger) obj[0]).intValue();
			recTotalView = ((BigDecimal) obj[1]).intValue();
		}

		response.setTotalPost(recTotal);
		response.setTotalInteraction(recTotalView);
		response.setTotalInteractionRate(response.getTotalPost() == 0 ? 0D
					: Double.valueOf(response.getTotalInteraction() / Double.valueOf(response.getTotalPost())));
		
		return response;
	}
	
	
	public List<DashboardSocialActivityPerSiteResponse> fetchSocialActivityPerSiteClient(final Long userId){
		
		val responseList = new ArrayList<DashboardSocialActivityPerSiteResponse>();
		
		DashboardStats  dashboardStats = dashboardStatRepository.findByDsCompanyID(userId);
		
		if(dashboardStats != null) {
			
			DashboardSocialActivityPerSiteResponse response = new DashboardSocialActivityPerSiteResponse();
			
			//Monthly Data
			Integer totalPosts = dashboardStats.getFbTotalpost();
			Integer totalInteractions = dashboardStats.getFbTotalInteraction();
			Double interactionRate = totalInteractions == 0 ? 0D : Double.valueOf(totalInteractions / totalPosts);

			Integer totalInPost = dashboardStats.getInstaTotalPost();
			Integer totalInInteractions = dashboardStats.getInstaTotalInteraction();
			Double interactionInRate = totalInInteractions == 0 ? 0D : Double.valueOf(totalInPost / totalInInteractions);

			Integer totalTwPost = dashboardStats.getTwtTotalPost();
			Integer totalTwInteractions = dashboardStats.getTwtTotalInteraction();
			Double interactionTwRate = totalTwInteractions == 0 ? 0D : Double.valueOf(totalTwPost / totalTwInteractions);

			Integer totalYTPost = dashboardStats.getYtTotalPost();
			Integer totalYTInteractions = dashboardStats.getYtTotalInteraction();
			Double interactionYTRate = totalYTInteractions == 0 ? 0D : Double.valueOf(totalYTPost / totalYTInteractions);
			
			response.setPeriodType("Monthly");
			response.setFbTotalPosts(totalPosts);
			response.setFbTotalInteractions(totalInteractions);
			response.setFbInteractionRate(interactionRate);

			response.setInTotalPosts(totalInPost);
			response.setInTotalInteractions(totalInInteractions);
			response.setInInteractionRate(interactionInRate);

			response.setTwTotalPosts(totalTwPost);
			response.setTwTotalInteractions(totalTwInteractions);
			response.setTwInteractionRate(interactionTwRate);

			response.setYtTotalPosts(totalYTPost);
			response.setYtTotalInteractions(totalYTInteractions);
			response.setYtInteractionRate(interactionYTRate);
			
			responseList.add(response);

			System.out.println(""+dashboardStats.getFbTotalpostQuartly());
			//Quartly Data
			Integer totalPostsQuartly = dashboardStats.getFbTotalpostQuartly() == null ? 0 : dashboardStats.getFbTotalpostQuartly();
			Integer totalInteractionsQuartly = dashboardStats.getFbTotalInteractionQuartly() == null ? 0 : dashboardStats.getFbTotalInteractionQuartly();
			Double interactionRateQuartly = totalInteractionsQuartly == 0 ? 0D : Double.valueOf(totalInteractionsQuartly / totalPostsQuartly);

			Integer totalInPostQuartly = dashboardStats.getInstaTotalPostQuartly() == null ? 0 : dashboardStats.getInstaTotalPostQuartly();
			Integer totalInInteractionsQuartly = dashboardStats.getInstaTotalInteractionQuartly() == null ? 0 : dashboardStats.getInstaTotalInteractionQuartly();
			Double interactionInRateQuartly = totalInInteractionsQuartly == 0 ? 0D : Double.valueOf(totalInPostQuartly / totalInInteractionsQuartly);

			Integer totalTwPostQuartly = dashboardStats.getTwtTotalPostQuartly() == null ? 0 : dashboardStats.getTwtTotalPostQuartly();
			Integer totalTwInteractionsQuartly = dashboardStats.getTwtTotalInteractionQuartly() == null ? 0 : dashboardStats.getTwtTotalInteractionQuartly();
			Double interactionTwRateQuartly = totalTwInteractionsQuartly == 0 ? 0D : Double.valueOf(totalTwPostQuartly / totalTwInteractionsQuartly);

			Integer totalYTPostQuartly = dashboardStats.getYtTotalPostQuartly() == null ? 0 : dashboardStats.getYtTotalPostQuartly();
			Integer totalYTInteractionsQuartly = dashboardStats.getYtTotalInteractionQuartly() == null ? 0 : dashboardStats.getYtTotalInteractionQuartly();
			Double interactionYTRateQuartly = totalYTInteractionsQuartly == 0 ? 0D : Double.valueOf(totalYTPostQuartly / totalYTInteractionsQuartly);
			
			response = new DashboardSocialActivityPerSiteResponse();
			
			response.setPeriodType("Quarterly");
			response.setFbTotalPosts(totalPostsQuartly);
			response.setFbTotalInteractions(totalInteractionsQuartly);
			response.setFbInteractionRate(interactionRateQuartly);

			response.setInTotalPosts(totalInPostQuartly);
			response.setInTotalInteractions(totalInInteractionsQuartly);
			response.setInInteractionRate(interactionInRateQuartly);

			response.setTwTotalPosts(totalTwPostQuartly);
			response.setTwTotalInteractions(totalTwInteractionsQuartly);
			response.setTwInteractionRate(interactionTwRateQuartly);

			response.setYtTotalPosts(totalYTPostQuartly);
			response.setYtTotalInteractions(totalYTInteractionsQuartly);
			response.setYtInteractionRate(interactionYTRateQuartly);
			
			responseList.add(response);
			
			//Yearly Data
			Integer totalPostsYearly = dashboardStats.getFbTotalpostYearly() == null ? 0 : dashboardStats.getFbTotalpostYearly();
			Integer totalInteractionsYearly = dashboardStats.getFbTotalInteractionYearly() == null ? 0 : dashboardStats.getFbTotalInteractionYearly();
			Double interactionRateYearly = totalInteractionsYearly == 0 ? 0D : Double.valueOf(totalInteractionsYearly / totalPostsYearly);

			Integer totalInPostYearly = dashboardStats.getInstaTotalPostYearly() == null ? 0 : dashboardStats.getInstaTotalPostYearly();
			Integer totalInInteractionsYearly = dashboardStats.getInstaTotalInteractionYearly() == null ? 0 : dashboardStats.getInstaTotalInteractionYearly();
			Double interactionInRateYearly = totalInInteractionsYearly == 0 ? 0D : Double.valueOf(totalInPostYearly / totalInInteractionsYearly);

			Integer totalTwPostYearly = dashboardStats.getTwtTotalPostYearly() == null ? 0 : dashboardStats.getTwtTotalPostYearly();
			Integer totalTwInteractionsYearly = dashboardStats.getTwtTotalInteractionYearly() == null ? 0 : dashboardStats.getTwtTotalInteractionYearly();
			Double interactionTwRateYearly = totalTwInteractionsYearly == 0 ? 0D : Double.valueOf(totalTwPostYearly / totalTwInteractionsYearly);

			Integer totalYTPostYearly = dashboardStats.getYtTotalPostYearly() == null ? 0 : dashboardStats.getYtTotalPostYearly();
			Integer totalYTInteractionsYearly = dashboardStats.getYtTotalInteractionYearly() == null ? 0 : dashboardStats.getYtTotalInteractionYearly();
			Double interactionYTRateYearly = totalYTInteractionsYearly == 0 ? 0D : Double.valueOf(totalYTPostYearly / totalYTInteractionsYearly);
			
			response = new DashboardSocialActivityPerSiteResponse();
			
			response.setPeriodType("Annually");
			response.setFbTotalPosts(totalPostsYearly);
			response.setFbTotalInteractions(totalInteractionsYearly);
			response.setFbInteractionRate(interactionRateYearly);

			response.setInTotalPosts(totalInPostYearly);
			response.setInTotalInteractions(totalInInteractionsYearly);
			response.setInInteractionRate(interactionInRateYearly);

			response.setTwTotalPosts(totalTwPostYearly);
			response.setTwTotalInteractions(totalTwInteractionsYearly);
			response.setTwInteractionRate(interactionTwRateYearly);

			response.setYtTotalPosts(totalYTPostYearly);
			response.setYtTotalInteractions(totalYTInteractionsYearly);
			response.setYtInteractionRate(interactionYTRateYearly);
			
			responseList.add(response);
			
		}
		
		return responseList;
	}

	public DashboardSocialActivityPerSiteResponse findAllTotals(final Long userId, final String periodType,
			final String keywordsFB, final String keywordsIN, final String keywordsTW,
			final String keywordsYT) {
		
		// -Facebook Feed Count
		Integer fbFeedCount = dashboardRepository.getTotalFBFeedCount(userId, keywordsFB, periodType, "feed");

		// Facebook Feed Like Count
		Integer fbLikeCount = dashboardRepository.getTotalFBFeedCount(userId, keywordsFB, periodType, "like");

		// Facebook Feed Comment Count
		Integer fbCommentCount = dashboardRepository.getTotalFBFeedCount(userId, keywordsFB, periodType, "comment");

		// Facebook Videos as Timeline Feeds
		Integer fbVideoFeedCount = dashboardRepository.getTotalFBFeedCount(userId, keywordsFB, periodType, "videoFeed");

		// Facebook Videos as Timeline Feeds/Likes
		Integer fbVideoLikeCount = dashboardRepository.getTotalFBFeedCount(userId, keywordsFB, periodType, "videoLike");

		// Facebook Videos as Timeline Feeds/Comments
		Integer fbVideoCommentCount = dashboardRepository.getTotalFBFeedCount(userId, keywordsFB, periodType,
				"videoComment");

		// Instagram Instragram Feed Count
		Integer inFeedCount = dashboardRepository.getTotalINFeedCount(userId, keywordsIN, periodType, "feed");

		// Instragram Like Count
		Integer inLikeCount = dashboardRepository.getTotalINFeedCount(userId, keywordsIN, periodType, "like");

		// Instragram Feed Comment Count
		Integer inCommentCount = dashboardRepository.getTotalINFeedCount(userId, keywordsIN, periodType, "comment");

		// Twitter Feed Count
		Integer twFeedCount = dashboardRepository.getTotalTWFeedCount(userId, keywordsTW, periodType, "feed");

		// Twitter Like Count
		Integer twLikeCount = dashboardRepository.getTotalTWFeedCount(userId, keywordsTW, periodType, "like");

		// Twitter Re Tweet Count
		Integer twReTweetCount = dashboardRepository.getTotalTWFeedCount(userId, keywordsTW, periodType, "retweet");

		// YouTube Feed Count
		DashboardSocialActivityPerSiteResponse ytFeedCount = dashboardRepository.getTotalYTFeedCount(userId, keywordsYT,
				periodType, "feed");
		DashboardSocialActivityPerSiteResponse ytHybridFeedCount = new DashboardSocialActivityPerSiteResponse();
		if (UserUtils.getLoggedInUser().getAcessType().equalsIgnoreCase("Hybrid")) {
			// YouTube Hybrid Feed Count
			ytHybridFeedCount = dashboardRepository.getTotalYTFeedCount(userId, keywordsYT, periodType, "hybrid");
		}

		Integer totalPosts = fbFeedCount + fbVideoFeedCount;
		Integer totalInteractions = fbLikeCount + fbCommentCount + fbVideoLikeCount + fbVideoCommentCount;
		Double interactionRate = totalInteractions == 0 ? 0D : Double.valueOf(totalInteractions / totalPosts);

		Integer totalInPost = inFeedCount;
		Integer totalInInteractions = inLikeCount + inCommentCount;
		Double interactionInRate = totalInInteractions == 0 ? 0D : Double.valueOf(totalInPost / totalInInteractions);

		Integer totalTwPost = twFeedCount;
		Integer totalTwInteractions = twLikeCount + twReTweetCount;
		Double interactionTwRate = totalTwInteractions == 0 ? 0D : Double.valueOf(totalTwPost / totalTwInteractions);

		Integer totalYTPost = 0;
		Integer totalYTInteractions = 0;
		Double interactionYTRate = 0.0;

		if (ytFeedCount != null) {
			totalYTPost = ytFeedCount.getYtRecTotal();
			totalYTInteractions = ytFeedCount.getYtRecViewTotal();

			if (ytHybridFeedCount != null) {
				totalYTPost += ytHybridFeedCount.getYtRecTotal() == null ? 0 : ytHybridFeedCount.getYtRecTotal();
				totalYTInteractions += ytHybridFeedCount.getYtRecTotal() == null ? 0 : ytHybridFeedCount.getYtRecTotal();
			}

			interactionYTRate = totalYTInteractions == 0 ? 0D :Double.valueOf(totalYTPost / totalYTInteractions);
		}

		//List<DashboardSocialActivityPerSiteResponse> activityResult = new ArrayList<>();
		DashboardSocialActivityPerSiteResponse response = new DashboardSocialActivityPerSiteResponse();

		response.setPeriodType(periodType);
		response.setFbTotalPosts(totalPosts);
		response.setFbTotalInteractions(totalInteractions);
		response.setFbInteractionRate(interactionRate);

		response.setInTotalPosts(totalInPost);
		response.setInTotalInteractions(totalInInteractions);
		response.setInInteractionRate(interactionInRate);

		response.setTwTotalPosts(totalTwPost);
		response.setTwTotalInteractions(totalTwInteractions);
		response.setTwInteractionRate(interactionTwRate);

		response.setYtTotalPosts(totalYTPost);
		response.setYtTotalInteractions(totalYTInteractions);
		response.setYtInteractionRate(interactionYTRate);

		//activityResult.add(response);

		return response;
	}
	
	public List<DashboardMostUsedKeuwordsResponse> fetchMostUsedKeywords(Long userId, List<String> keywordArrayList) {
		
		Map<String,DashboardMostUsedKeuwordsResponse> map = new HashMap<String, DashboardMostUsedKeuwordsResponse>();
		
		Date toDate = Calendar.getInstance().getTime();
		Date fromDate = Calendar.getInstance().getTime();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, -1);
		fromDate = cal.getTime();
		
		Integer totalFbCount = dashboardRepository.getMostUsedKeywordFb(userId, "", "fb", fromDate, toDate);
		Integer totalInCount = dashboardRepository.getMostUsedKeywordFb(userId, "", "in", fromDate, toDate);
		Integer totalTwCount = dashboardRepository.getMostUsedKeywordFb(userId, "", "tw", fromDate, toDate);
		Integer totalYtCount = dashboardRepository.getMostUsedKeywordFb(userId, "", "yt", fromDate, toDate);
		
		Integer totalAllMediaCount = totalFbCount + totalInCount + totalTwCount + totalYtCount;
		
		if(keywordArrayList != null ) {
			for(String keyword : keywordArrayList) {
				if(keyword.isEmpty()) 
					continue;
				
				Integer countPostfb = dashboardRepository.getMostUsedKeywordFb(userId, keyword, "fb", fromDate, toDate);
				Integer countPostin = dashboardRepository.getMostUsedKeywordFb(userId, keyword, "in", fromDate, toDate);
				Integer countPosttw = dashboardRepository.getMostUsedKeywordFb(userId, keyword, "tw", fromDate, toDate);
				Integer countPostyt = dashboardRepository.getMostUsedKeywordFb(userId, keyword, "yt", fromDate, toDate);
				
				Integer totalCount = countPostfb + countPostin + countPosttw + countPostyt;
				Double perPost = getDouble(Double.valueOf((totalCount * 100)) / totalAllMediaCount);
				
				val response = new DashboardMostUsedKeuwordsResponse();
				response.setSocialType("Facebook");
				response.setName(keyword+ " <span class=\"kt-badge kt-badge--info kt-badge--inline kt-badge--pill\" style=\"font-size: 1.0rem;\">"+ numberFormat.format(totalCount)+" </span>");
				response.setCount(totalCount);
				map.put(keyword, response);
				
				System.out.println("keyword:: "+keyword);
				System.out.println("userId:: "+userId);
			
				dashboardMostusedKeywordYearlyRepository.deleteByCompanyIdAndKeyword(userId, keyword);
				val dashboardResponse = new DashboardMostusedKeywordYearly();
				dashboardResponse.setCompanyId(userId);
				dashboardResponse.setKeyword(keyword);
				dashboardResponse.setFbCount(countPostfb);
				dashboardResponse.setInCount(countPostin);
				dashboardResponse.setTwCount(countPosttw);
				dashboardResponse.setYtCount(countPostyt);
				
				dashboardMostusedKeywordYearlyRepository.save(dashboardResponse);
			}

		}
		
		
		List<Map.Entry<String, DashboardMostUsedKeuwordsResponse>> entryList = new ArrayList<Map.Entry<String, DashboardMostUsedKeuwordsResponse>>(map.entrySet());

        Collections.sort(
                entryList, new Comparator<Map.Entry<String, DashboardMostUsedKeuwordsResponse>>() {
	            @Override
	            public int compare(Map.Entry<String, DashboardMostUsedKeuwordsResponse> o1,
	                               Map.Entry<String, DashboardMostUsedKeuwordsResponse> o2) {
	                return o2.getValue().getCount()
	                        - o1.getValue().getCount();
	            }
	        }
	    );
        
        List<DashboardMostUsedKeuwordsResponse> res = new ArrayList<DashboardMostUsedKeuwordsResponse>();
        
        for(Entry<String,DashboardMostUsedKeuwordsResponse> entry: entryList){
        	res.add(entry.getValue());
        }
		
		return res;
		
	}

	private Double getDouble(Double val) {
		return Math.round(val * 100D) / 100D;
	}
	
	public List<DashboardMostUsedKeuwordsResponse> fetchMostUsedKeywords(Long userId, DashboardSocialmediaPerSite commonSocialMedia) {
		
		Map<String,DashboardMostUsedKeuwordsResponse> map = new HashMap<String, DashboardMostUsedKeuwordsResponse>();
		
		Integer totalFbCount = commonSocialMedia.getTotalPostYearlyFb();
		Integer totalInCount = commonSocialMedia.getTotalPostYearlyIn();
		Integer totalTwCount = commonSocialMedia.getTotalPostYearlyTw();
		Integer totalYtCount = commonSocialMedia.getTotalPostYearlyYt();
		
		Integer totalAllMediaCount = totalFbCount + totalInCount + totalTwCount + totalYtCount;
		
		val dashboardCountPost = dashboardRepository.getMostUsedKeyword(userId);
		
		if(dashboardCountPost != null) {
			for(DashboardMostusedKeywordYearly obj : dashboardCountPost) {
				if(!TextUtils.isEmpty(obj.getKeyword())) {
				Integer countPostfb = obj.getFbCount();
				Integer countPostin = obj.getInCount();
				Integer countPosttw = obj.getTwCount();
				Integer countPostyt = obj.getYtCount();
				
				Integer totalCount = countPostfb + countPostin + countPosttw + countPostyt;
				Double perPost = getDouble(Double.valueOf ((totalCount * 100)) / totalAllMediaCount);
				
				val response = new DashboardMostUsedKeuwordsResponse();
				response.setSocialType("Facebook");
				response.setName(obj.getKeyword() +" <span class=\"kt-badge kt-badge--info kt-badge--inline kt-badge--pill\" style=\"font-size: 1.0rem;\">"+ numberFormat.format(totalCount)+" </span>");
				response.setCount(totalCount);
				map.put(obj.getKeyword(), response);
				}
				
			}
		}

		
		
		
		
		List<Map.Entry<String, DashboardMostUsedKeuwordsResponse>> entryList = new ArrayList<Map.Entry<String, DashboardMostUsedKeuwordsResponse>>(map.entrySet());

        Collections.sort(
                entryList, new Comparator<Map.Entry<String, DashboardMostUsedKeuwordsResponse>>() {
	            @Override
	            public int compare(Map.Entry<String, DashboardMostUsedKeuwordsResponse> o1,
	                               Map.Entry<String, DashboardMostUsedKeuwordsResponse> o2) {
	                return o2.getValue().getCount()
	                        - o1.getValue().getCount();
	            }
	        }
	    );
        
        List<DashboardMostUsedKeuwordsResponse> res = new ArrayList<DashboardMostUsedKeuwordsResponse>();
        
        for(Entry<String,DashboardMostUsedKeuwordsResponse> entry: entryList){
        	res.add(entry.getValue());
        }
		
		return res;
		
	}
	
	public DashboardSocialmediaPerSite fetchTotalFeebByClient(Long companyId) {
		
		val objectList = dashboardRepository.fetchTotalFeebByClient(companyId);
		
		DashboardSocialmediaPerSite response = new DashboardSocialmediaPerSite();
		
		if(objectList != null && objectList.size() > 0) {
			response = objectList.get(0);
			
			response.setTotalInteractionRateMonthlyFb(
					response.getTotalPostMonthlyFb() == null || response.getTotalPostMonthlyFb() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionMonthlyFb() == null ? 0 : response.getTotalInteractionMonthlyFb()
									/ Double.valueOf(response.getTotalPostMonthlyFb())));

			response.setTotalInteractionRateQuarterlyFb(
					response.getTotalPostQuarterlyFb() == null || response.getTotalPostQuarterlyFb() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionQuarterlyFb()  == null ? 0 : response.getTotalInteractionQuarterlyFb()
									/ Double.valueOf(response.getTotalPostQuarterlyFb())));

			response.setTotalInteractionRateYearlyFb(
					response.getTotalPostYearlyFb() == null || response.getTotalPostYearlyFb() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionYearlyFb() == null ? 0
									: response.getTotalInteractionYearlyFb()
											/ Double.valueOf(response.getTotalPostYearlyFb())));

			response.setTotalInteractionRateMonthlyIn(
					response.getTotalPostMonthlyIn() == null || response.getTotalPostMonthlyIn() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionMonthlyIn() == null ? 0 : response.getTotalInteractionMonthlyIn()
									/ Double.valueOf(response.getTotalPostMonthlyIn())));

			response.setTotalInteractionRateQuarterlyIn(
					response.getTotalPostQuarterlyIn() == null || response.getTotalPostQuarterlyIn() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionQuarterlyIn() == null ? 0 : response.getTotalInteractionQuarterlyIn()
									/ Double.valueOf(response.getTotalPostQuarterlyIn())));

			response.setTotalInteractionRateYearlyIn(
					response.getTotalPostYearlyIn() == null || response.getTotalPostYearlyIn() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionYearlyIn()  == null ? 0 : response.getTotalInteractionYearlyIn()
									/ Double.valueOf(response.getTotalPostYearlyIn())));

			response.setTotalInteractionRateMonthlyTw(
					response.getTotalPostMonthlyTw() == null || response.getTotalPostMonthlyTw() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionMonthlyTw() == null ? 0 : response.getTotalInteractionMonthlyTw()
									/ Double.valueOf(response.getTotalPostMonthlyTw())));

			response.setTotalInteractionRateQuarterlyTw(
					response.getTotalPostQuarterlyTw() == null || response.getTotalPostQuarterlyTw() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionQuarterlyTw() == null ? 0 : response.getTotalInteractionQuarterlyTw()
									/ Double.valueOf(response.getTotalPostQuarterlyTw())));

			response.setTotalInteractionRateYearlyTw(
					response.getTotalPostYearlyTw() == null || response.getTotalPostYearlyTw() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionYearlyTw() == null ? 0 : response.getTotalInteractionYearlyTw()
									/ Double.valueOf(response.getTotalPostYearlyTw())));

			response.setTotalInteractionRateMonthlyYt(
					response.getTotalPostMonthlyYt() == null || response.getTotalPostMonthlyYt() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionMonthlyYt() == null ? 0 : response.getTotalInteractionMonthlyYt()
									/ Double.valueOf(response.getTotalPostMonthlyYt())));

			response.setTotalInteractionRateQuarterlyYt(
					response.getTotalPostQuarterlyYt() == null || response.getTotalPostQuarterlyYt() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionQuarterlyYt() == null ? 0 : response.getTotalInteractionQuarterlyYt() 
									/ Double.valueOf(response.getTotalPostQuarterlyYt())));

			response.setTotalInteractionRateYearlyYt(
					response.getTotalPostYearlyYt() == null || response.getTotalPostYearlyYt() == 0 ? 0D
							: Double.valueOf(response.getTotalInteractionYearlyYt() == null ? 0 : response.getTotalInteractionYearlyYt()
									/ Double.valueOf(response.getTotalPostYearlyYt())));
		}

		
		
		return response;
	}
	
}
