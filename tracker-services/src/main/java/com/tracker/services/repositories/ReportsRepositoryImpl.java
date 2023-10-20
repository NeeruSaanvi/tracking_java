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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.dtos.ReportExportDTO;
import com.tracker.commons.dtos.ReportGraphResponse;
import com.tracker.commons.dtos.ReportStatResponse;
import com.tracker.commons.models.Reports;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserSponsorScores;
import com.tracker.commons.models.UserTeam;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.UserService;
import com.tracker.services.utils.SessionVariables;
import com.tracker.services.utils.UserUtils;

import lombok.val;

@Repository
@Transactional
public class ReportsRepositoryImpl implements ReportsRepositoryCustom {

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	private DashboardRepository dashboardRepository;
	 
	@Autowired
	private UserSponcerScoreRepository userSponcerScoreRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	private NumberFormat numberFormat = NumberFormat.getInstance();
	
	
	public List<Object[]> buildSchedule(List<String> staff, Long loggedInUserId) {
		
		//String queryStr = "SELECT DISTINCT u.userid,u.FirstLastname from users u ";
		String queryStr = "SELECT DISTINCT u.userid,u.FirstLastname, tm.teamId, t.teamName " + 
				" from users u 			 " + 
				" LEFT JOIN user_team_mapping tm on tm.userId = u.userid " + 
				" left JOIN user_team t on t.teamId = tm.teamId " + 
				" LEFT JOIN company_ref cr ON cr.userid=u.userid" + 
				"			WHERE cr.ref_status='Active' and u.memtype='Staff' and cr.companyID=:companyID ";
		
		if(staff != null && staff.size() > 0 && !staff.get(0).equalsIgnoreCase("All")) {
			queryStr += " WHERE u.userid IN (:staff)";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		
		nativeQuery.setParameter("companyID", loggedInUserId);
		
		if(staff != null && staff.size() > 0 && !staff.get(0).equalsIgnoreCase("All")) {
			nativeQuery.setParameter("staff", staff);
		}
		
		@SuppressWarnings("unchecked")
		List<Object[]> objectList = nativeQuery.getResultList();
		
		return objectList;
	}
	
	public List<Object[]> buildBasicQuery1(PaginationRequestDTO dto, String staff,
			Boolean count, Long loggedInUserId) {
		
		String queryStr = "SELECT DISTINCT u.userid,u.FirstLastname, 0 as teamId, '' as teamName " + 
				" from users u " + 
				" LEFT JOIN company_ref cr ON cr.userid=u.userid" + 
				"			WHERE cr.ref_status='Active' and u.memtype='Staff' and cr.companyID=:companyID ";
		
		if(StringUtils.isNotBlank(staff)) {
			queryStr += " AND u.userid IN (:staff)";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		if(!count) {
			PageRequest pr = dto.getRequest();
			nativeQuery.setFirstResult(dto.getPage());
			nativeQuery.setMaxResults(pr.getPageSize());
		}
		
		if(loggedInUserId == null) {
			nativeQuery.setParameter("companyID", UserUtils.getLoggedInUserId());
		}else {
			nativeQuery.setParameter("companyID", loggedInUserId);
		}
		
		if(StringUtils.isNotBlank(staff)) {
			String[] strArray = staff.split(",");
			List<Long> lst = new ArrayList<Long>();
			for(String str : strArray) {
				lst.add(Long.parseLong(str));
			}
			nativeQuery.setParameter("staff", lst);
		}
		
		
		@SuppressWarnings("unchecked")
		List<Object[]> objectList = nativeQuery.getResultList();
		
		List<UserTeam> userTeam = fetchTeamByUserId(staff, UserUtils.getLoggedInUserId());
		
		for(Object[] object : objectList) {
			
			List<UserTeam> result = userTeam.stream()
				     .filter(item -> item.getUserId() == Long.parseLong(object[0].toString()))
				     .collect(Collectors.toList());
			if(result != null && result.size() > 0) {
				object[2] = result.get(0).getTeamId();
				object[3] = result.get(0).getTeamName();
			}
			
		}
		
		return objectList;
	}
	
	public List<UserTeam> fetchTeamByUserId(String staff, Long companyID){
		
		String queryStr = "select utm.userId, utm.teamId, ut.teamName  " + 
				"from user_team_mapping utm  " + 
				"LEFT JOIN user_team ut on ut.teamId = utm.teamId   " + 
				"where ut.createdBy = :companyID";
		
		if(StringUtils.isNotBlank(staff)) {
			queryStr += " AND utm.userId IN (:staff)";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		
		if(StringUtils.isNotBlank(staff)) {
			String[] strArray = staff.split(",");
			List<Long> lst = new ArrayList<Long>();
			for(String str : strArray) {
				lst.add(Long.parseLong(str));
			}
			nativeQuery.setParameter("staff", lst);
		}
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<UserTeam> tList = new ArrayList<UserTeam>();
		
		for (Object[] obj : list) {
			
			UserTeam yt = new UserTeam();
			yt.setUserId(Long.parseLong(obj[0].toString()));
			yt.setTeamId(Long.parseLong(obj[1].toString()));
			yt.setTeamName(obj[2].toString());
			
			tList.add(yt);
		}

		return tList;
	}
	
	public List<Object[]> buildBasicQuery(PaginationRequestDTO dto, String staff,
			Boolean count, Long loggedInUserId) {
		
		String queryStr = "SELECT DISTINCT u.userid,u.FirstLastname, 0 as teamId, '' as teamName " + 
				" from users u 			 " + 
				" LEFT JOIN company_ref cr ON cr.userid=u.userid" + 
				"			WHERE cr.ref_status='Active' and u.memtype='Staff' and cr.companyID=:companyID";
		
		if(StringUtils.isNotBlank(staff)) {
			queryStr += " AND u.userid IN (:staff)";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		if(!count) {
			PageRequest pr = dto.getRequest();
			nativeQuery.setFirstResult(dto.getPage());
			nativeQuery.setMaxResults(pr.getPageSize());
		}
		
		if(loggedInUserId == null) {
			nativeQuery.setParameter("companyID", UserUtils.getLoggedInUserId());
		}else {
			nativeQuery.setParameter("companyID", loggedInUserId);
		}
		
		if(StringUtils.isNotBlank(staff)) {
			String[] strArray = staff.split(",");
			List<Long> lst = new ArrayList<Long>();
			for(String str : strArray) {
				lst.add(Long.parseLong(str));
			}
			nativeQuery.setParameter("staff", lst);
		}
		
		
		@SuppressWarnings("unchecked")
		List<Object[]> objectList = nativeQuery.getResultList();
		
		if(loggedInUserId == null) {
			loggedInUserId = UserUtils.getLoggedInUserId();
		}
		
		List<UserTeam> userTeam = fetchTeamByUserId(staff, loggedInUserId);
		
		for(Object[] object : objectList) {
			
			List<UserTeam> result = userTeam.stream()
				     .filter(item -> item.getUserId() == Long.parseLong(object[0].toString()))
				     .collect(Collectors.toList());
			
			if(result != null && result.size() > 0) {
				object[2] = result.get(0).getTeamId();
				object[3] = result.get(0).getTeamName();
			}
			
		}
		
		return objectList;
	}
	
	@Override
	public List<ReportExportDTO> fetchReportsByKeyWords(Date fromDate, Date toDate, String staff, List<String> keywordArrayList, Integer grandTotalPost, Long companyId) {
		
		Map<String,ReportExportDTO> keywordMap = new HashMap<String, ReportExportDTO>();
		List<ReportExportDTO> resultKeywordList = new ArrayList<ReportExportDTO>();
		
		List<Object[]> objectList = buildBasicQuery(null, staff, true, companyId);
		List<Long> userIds = new ArrayList<Long>();
		
		for(Object[] obj: objectList) {
			val userId = (Integer)obj[0];
			userIds.add(Long.valueOf(userId));
		}
		
		if(keywordArrayList != null ) {
			
			val fbListTotal = getTotalFBFeedByKeyword(userIds, "", fromDate, toDate);
			val inListTotal = getTotalINFeedByKeyword(userIds, "", fromDate, toDate);
			val twListTotal = getTotalTWFeedByKeyword(userIds, "", fromDate, toDate);
			val ytListTotal = getTotalYTFeedByKeyword(userIds, "", fromDate, toDate);
			
			Integer grandTotalFeedCount = 0;
			for (Object[] objFb : fbListTotal) {
				grandTotalFeedCount += Integer.parseInt(objFb[0].toString()); 
			}
			
			for (Object[] objFb : inListTotal) {
				grandTotalFeedCount += Integer.parseInt(objFb[0].toString()); 
			}
			
			for (Object[] objFb : twListTotal) {
				grandTotalFeedCount += Integer.parseInt(objFb[0].toString()); 
			}
			
			for (Object[] objFb : ytListTotal) {
				grandTotalFeedCount += Integer.parseInt(objFb[0].toString()); 
			}
			
			for(String keyword : keywordArrayList) {
				
				Integer fbFeedCount = 0, fbLikeCount = 0, fbCommentCount = 0, fbShareCount = 0, fbInteractions = 0;	
				Integer inFeedCount = 0, inLikeCount = 0, inCommentCount = 0, inInteractions = 0;
				Integer twFeedCount = 0, twLikeCount = 0, twCommentCount = 0, twInteractions = 0;
				Integer ytFeedCount = 0, ytViewCount = 0;
				
				Integer totalFeedCount = 0, totalInteractions = 0;
				Double totalEffectivenessRate = 0d;
				
				val fbList = getTotalFBFeedByKeyword(userIds, keyword, fromDate, toDate);
				val inList = getTotalINFeedByKeyword(userIds, keyword, fromDate, toDate);
				val twList = getTotalTWFeedByKeyword(userIds, keyword, fromDate, toDate);
				val ytList = getTotalYTFeedByKeyword(userIds, keyword, fromDate, toDate);
				
				for (Object[] objFb : fbList) {
					fbFeedCount += Integer.parseInt(objFb[0].toString()); 
					fbLikeCount += Integer.parseInt(objFb[1].toString());
					fbCommentCount += Integer.parseInt(objFb[2].toString());
					fbShareCount += Integer.parseInt(objFb[3].toString());
					
					fbInteractions += fbLikeCount + fbCommentCount + fbShareCount;
				}
				
				for (Object[] objFb : inList) {
					inFeedCount += Integer.parseInt(objFb[0].toString()); 
					inLikeCount += Integer.parseInt(objFb[1].toString());
					inCommentCount += Integer.parseInt(objFb[2].toString());
					
					inInteractions += inLikeCount + inCommentCount;
				}
				
				for (Object[] objFb : twList) {
					twFeedCount += Integer.parseInt(objFb[0].toString()); 
					twLikeCount += Integer.parseInt(objFb[1].toString());
					twCommentCount += Integer.parseInt(objFb[2].toString());
					
					twInteractions += twLikeCount + twCommentCount;
				}
				
				for (Object[] objFb : ytList) {
					ytFeedCount += Integer.parseInt(objFb[0].toString()); 
					ytViewCount += Integer.parseInt(objFb[1].toString());
				}
				
				totalFeedCount = fbFeedCount + inFeedCount + twFeedCount;
				totalInteractions = fbInteractions + inInteractions + twInteractions;
				Double effectivenessRate = Double.valueOf(totalInteractions) / Double.valueOf(totalFeedCount);
				totalEffectivenessRate = effectivenessRate / 10;
				
				ReportExportDTO report = new ReportExportDTO();
				report.setKeyword(keyword);
				report.setTotalPost(totalFeedCount);
				report.setTotalInteractions(totalInteractions);
				report.setTotalEffectivenessRate(totalEffectivenessRate);
				report.setTotalYtViews(ytViewCount);
				
				Double percentagePost = (Double.valueOf(totalFeedCount))/grandTotalPost;
				report.setPercentagePost(getDouble(percentagePost));
				
				keywordMap.put(keyword, report);
			}
			
			resultKeywordList = keywordMap.values().stream().collect(Collectors.toList());
		}
		return resultKeywordList;
	}
	
	public List<User> getMembers(Long companyID) {

		String queryStr = "select u.* from users u  "
				+ "left join company_ref cr on cr.userid=u.userid " 
				+ "where cr.companyID= :companyID "
				+ " and cr.ref_status='Active' and u.memtype='Staff'"
				+ "group by u.userid order by FirstLastname ASC ";

		Query nativeQuery = em.createNativeQuery(queryStr, User.class);

		nativeQuery.setParameter("companyID", companyID);

		@SuppressWarnings("unchecked")
		List<User> list = nativeQuery.getResultList();

		return list;
	}
	
	@Override
	public ReportStatResponse getReportsStat(PaginationRequestDTO dto, Date fromDate, Date toDate, String staff,
			String keyword, Boolean count, Long companyId) {
		
		String keywordsFB = "", keywordsIN = "", keywordsTW = "";
		
		/*@SuppressWarnings("unchecked")
		val membersList = (List<User>) UserUtils.getSessionsMap().get(SessionVariables.MEMBERS);*/
		
		val companyUser = userRepository.getOne(companyId);
		
		val membersList = dashboardRepository.getMembers(companyId);
		
		
		if(StringUtils.isNotBlank(keyword)) {
			keywordsFB = keyword;
			keywordsIN = keyword;
			keywordsTW = keyword;
		}else {
			keywordsFB = userService.getKeywords(companyUser, "fbkeywords");
			keywordsIN = userService.getKeywords(companyUser, "instakeywords");
			keywordsTW = userService.getKeywords(companyUser, "twtkeywords");
		}
		
		//buildBasicQuery1(dto, staff, count, companyId);
		
		List<Object[]> objectList = buildBasicQuery(dto, staff, count, companyId);
		List<Long> userIds = new ArrayList<Long>();
		
		for(Object[] obj: objectList) {
			val userId = (Integer)obj[0];
			userIds.add(Long.valueOf(userId));
		}
		
		Boolean graphQuery = false;
		
		Integer fbFeedCount = 0, fbLikeCount = 0, fbCommentCount = 0, fbShareCount = 0;		
		ReportStatResponse response = new ReportStatResponse();
		long days = ChronoUnit.DAYS.between(fromDate.toInstant(), toDate.toInstant());
		
		
		val fbList = getTotalFBFeedByUser(userIds, keywordsFB, fromDate, toDate, "", graphQuery);		

		
		for (Object[] objFb : fbList) {
			fbFeedCount = Integer.parseInt(objFb[0].toString()); 
			fbLikeCount = ((BigDecimal) objFb[1]).intValue();
			fbCommentCount = ((BigDecimal) objFb[2]).intValue();
			fbShareCount = ((BigDecimal) objFb[3]).intValue();
			
			response.setTotalPostFb(fbFeedCount);
			response.setTotalPostFbFormatter(numberFormat.format(fbFeedCount));
			response.setTotalInteractionsFb(fbLikeCount + fbCommentCount + fbShareCount);
			response.setTotalInteractionsFbFormatter(numberFormat.format(response.getTotalInteractionsFb()));
			
			Double effectivenessRate = Double.valueOf((fbLikeCount + fbCommentCount + fbShareCount)) / Double.valueOf(fbFeedCount);
			effectivenessRate = effectivenessRate / 10;
			response.setEffectivenessRateFb(getDouble(effectivenessRate));
			
			response.setAvgPostsPerMemberFb(getDouble(Double.valueOf(fbFeedCount)/membersList.size()));
			response.setAvgInteractionsPerMemberFb(getDouble(Double.valueOf(response.getTotalInteractionsFb())/membersList.size()));
			
			response.setPostsPerWeekFb(getDouble(Double.valueOf(fbFeedCount)/(days/7)));
			
		}
	
		fbFeedCount = 0; fbLikeCount = 0; fbCommentCount = 0;	
		val inlist = getTotalINFeedByUser(userIds, keywordsIN, fromDate, toDate, "", graphQuery);
		
		for (Object[] objFb : inlist) {
			fbFeedCount = Integer.parseInt(objFb[0].toString()); 
			fbLikeCount = ((BigDecimal) objFb[1]).intValue();
			fbCommentCount = ((BigDecimal) objFb[2]).intValue();
			
			response.setTotalPostIn(fbFeedCount);
			response.setTotalPostInFormatter(numberFormat.format(fbFeedCount));
			response.setTotalInteractionsIn(fbLikeCount + fbCommentCount);
			response.setTotalInteractionsInFormatter(numberFormat.format(response.getTotalInteractionsIn()));
			
			Double effectivenessRate = Double.valueOf((fbLikeCount + fbCommentCount)) / Double.valueOf(fbFeedCount);
			effectivenessRate = effectivenessRate / 10;
			response.setEffectivenessRateIn(getDouble(effectivenessRate));
			
			response.setAvgPostsPerMemberIn(getDouble(Double.valueOf(fbFeedCount)/membersList.size()));
			response.setAvgInteractionsPerMemberIn(getDouble(Double.valueOf(response.getTotalInteractionsIn())/membersList.size()));
			
			response.setPostsPerWeekIn(getDouble(Double.valueOf(fbFeedCount)/(days/7)));
			
		}
		
		fbFeedCount = 0; fbLikeCount = 0; fbCommentCount = 0;
		val twlist = getTotalTWFeedByUser(userIds, keywordsTW, fromDate, toDate, "", graphQuery);
		
		for (Object[] objFb : twlist) {
			fbFeedCount = Integer.parseInt(objFb[0].toString()); 
			fbLikeCount = ((BigDecimal) objFb[1]).intValue();
			fbCommentCount = ((BigDecimal) objFb[2]).intValue();
			
			response.setTotalPostTw(fbFeedCount);
			response.setTotalPostTwFormatter(numberFormat.format(fbFeedCount));
			response.setTotalInteractionsTw(fbLikeCount + fbCommentCount);
			response.setTotalInteractionsTwFormatter(numberFormat.format(response.getTotalInteractionsTw()));
			
			Double effectivenessRate = Double.valueOf((fbLikeCount + fbCommentCount)) / Double.valueOf(fbFeedCount);
			effectivenessRate = effectivenessRate / 10;
			response.setEffectivenessRateTw(getDouble(effectivenessRate));
			
			response.setAvgPostsPerMemberTw(getDouble(Double.valueOf(fbFeedCount)/membersList.size()));
			response.setAvgInteractionsPerMemberTw(getDouble(Double.valueOf(response.getTotalInteractionsTw())/membersList.size()));
			
			response.setPostsPerWeekTw(getDouble(Double.valueOf(fbFeedCount)/(days/7)));
			
		}
		
		
		return response;
	}
	
	
	@Override
	public List<ReportGraphResponse> getReportsGraphs(PaginationRequestDTO dto, Date fromDate, Date toDate, String staff,
			String keyword, Boolean count, String graphType, Long loggedInUserId) {
		
		String keywordsFB = "", keywordsIN = "", keywordsTW = "", keywordsYT = "";
		
		if(StringUtils.isNotBlank(keyword)) {
			keywordsFB = keyword;
			keywordsIN = keyword;
			keywordsTW = keyword;
			keywordsYT = keyword;
		}else {
			keywordsFB = (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSFB);
			keywordsIN = (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSIN);
			keywordsTW = (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSTW);
			keywordsYT = (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSYT);
		}
		
		List<Object[]> objectList = buildBasicQuery(dto, staff, count, loggedInUserId);
		List<Long> userIds = new ArrayList<Long>();
		
		for(Object[] obj: objectList) {
			val userId = (Integer)obj[0];
			userIds.add(Long.valueOf(userId));
		}
		
		Boolean graphQuery = true;
		
		
		Integer fbFeedCount = 0, fbLikeCount = 0, fbCommentCount = 0, fbShareCount = 0;		
		List<ReportGraphResponse> response = new ArrayList<ReportGraphResponse>();
		
		val quaterlyMap = new HashMap<String, ReportGraphResponse>(); 
		
		if(graphType.equalsIgnoreCase("FB")) {
			val fbList = getTotalFBFeedByUser(userIds, keywordsFB, fromDate, toDate, "", graphQuery);
			
			for (Object[] objFb : fbList) {
				fbFeedCount = Integer.parseInt(objFb[0].toString()); 
				fbLikeCount = ((BigDecimal) objFb[1]).intValue();
				fbCommentCount = ((BigDecimal) objFb[2]).intValue();
				fbShareCount = ((BigDecimal) objFb[3]).intValue();
				String month = objFb[4].toString();
				
				val fbGraphResponse = new ReportGraphResponse();
				fbGraphResponse.setMonth(month);
				fbGraphResponse.setPostsPerMember(getDouble(Double.valueOf(fbFeedCount) / userIds.size()));
				Double effectivenessRate = Double.valueOf((fbLikeCount + fbCommentCount + fbShareCount)) / Double.valueOf(fbFeedCount);
				
				effectivenessRate = effectivenessRate / 10;
				
				fbGraphResponse.setEffectivenessRate(getDouble(effectivenessRate));
				response.add(fbGraphResponse);
				
				quaterlyMap.put(month, fbGraphResponse);
			}
		}
		
		if(graphType.equalsIgnoreCase("IN")) {
			val list = getTotalINFeedByUser(userIds, keywordsIN, fromDate, toDate, "", graphQuery);
			
			for (Object[] objFb : list) {
				fbFeedCount = Integer.parseInt(objFb[0].toString()); 
				fbLikeCount = ((BigDecimal) objFb[1]).intValue();
				fbCommentCount = ((BigDecimal) objFb[2]).intValue();
				String month = objFb[3].toString();
				
				val fbGraphResponse = new ReportGraphResponse();
				fbGraphResponse.setMonth(month);
				fbGraphResponse.setPostsPerMember(getDouble(Double.valueOf(fbFeedCount) / userIds.size()));
				Double effectivenessRate = Double.valueOf((fbLikeCount + fbCommentCount + fbShareCount)) / Double.valueOf(fbFeedCount);
				
				effectivenessRate = effectivenessRate / 10;
				
				fbGraphResponse.setEffectivenessRate(getDouble(effectivenessRate));
				response.add(fbGraphResponse);
				
				quaterlyMap.put(month, fbGraphResponse);
			}
		}
		
		if(graphType.equalsIgnoreCase("TW")) {
			val list = getTotalTWFeedByUser(userIds, keywordsTW, fromDate, toDate, "", graphQuery);
			
			for (Object[] objFb : list) {
				fbFeedCount = Integer.parseInt(objFb[0].toString()); 
				fbLikeCount = ((BigDecimal) objFb[1]).intValue();
				fbCommentCount = ((BigDecimal) objFb[2]).intValue();
				
				val fbGraphResponse = new ReportGraphResponse();
			

				if(objFb[3] != null) {
					String month = objFb[3].toString();

				fbGraphResponse.setMonth(month);
				fbGraphResponse.setPostsPerMember(getDouble(Double.valueOf(fbFeedCount) / userIds.size()));
				Double effectivenessRate = Double.valueOf((fbLikeCount + fbCommentCount)) / Double.valueOf(fbFeedCount);
				
				effectivenessRate = effectivenessRate / 10;
				
				fbGraphResponse.setEffectivenessRate(getDouble(effectivenessRate));
				response.add(fbGraphResponse);
				
				quaterlyMap.put(month, fbGraphResponse);
				}
			}
		}
		
		if(graphType.equalsIgnoreCase("YT")) {
			val list = getTotalYTFeedByUser(userIds, keywordsYT, fromDate, toDate, "", graphQuery);
			
			for (Object[] objFb : list) {
				fbFeedCount = Integer.parseInt(objFb[0].toString()); 
				fbLikeCount = ((BigDecimal) objFb[1]).intValue();
				String month = objFb[2].toString();
				
				val fbGraphResponse = new ReportGraphResponse();
				fbGraphResponse.setMonth(month);
				fbGraphResponse.setPostsPerMember(getDouble(Double.valueOf(fbFeedCount) / userIds.size()));
				Double effectivenessRate = Double.valueOf((fbLikeCount)) / Double.valueOf(fbFeedCount);
				
				effectivenessRate = effectivenessRate / 10;
				
				fbGraphResponse.setEffectivenessRate(getDouble(effectivenessRate));
				response.add(fbGraphResponse);
				
				quaterlyMap.put(month, fbGraphResponse);
			}
		}
		
		for(ReportGraphResponse r1 : response) {
			r1.setMonthNumber(Month.valueOf(r1.getMonth().toUpperCase()).getValue());
		}
		
		response.sort((ReportGraphResponse o1, ReportGraphResponse o2)->o1.getMonthNumber()-o2.getMonthNumber());
		
		//response.sort(Comparator.comparing(ReportGraphResponse::toString));
		
		return response;
		
	}
	
	private Double getDouble(Double val) {
		return Math.round(val * 100D) / 100D;
	}
	
	@Override
	public List<Reports> generateSchduleReport(Date fromDate, Date toDate, List<String> staff, Long companyId) {

		String keywordsFB = "", keywordsIN = "", keywordsTW = "", keywordsYT = "";
		
		User dbUser = userRepository.findByUserId(companyId);
		keywordsFB = userService.getKeywords(dbUser, "fbkeywords");
		keywordsIN = userService.getKeywords(dbUser, "instakeywords");
		keywordsTW = userService.getKeywords(dbUser, "twtkeywords");
		keywordsYT = userService.getKeywords(dbUser, "youtubekeywords");
		
		/*
		 * keywordsFB =
		 * (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSFB);
		 * keywordsIN =
		 * (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSIN);
		 * keywordsTW =
		 * (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSTW);
		 * keywordsYT =
		 * (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSYT);
		 */
		
		List<Object[]> objectList = new ArrayList<>();
		
		objectList = buildSchedule(staff, companyId);
		
		List<Reports> reportList = new ArrayList<Reports>();
		
		//val scoreInfo = userSponcerScoreRepository.findByCompanyID(companyId);
		List<UserSponsorScores> scoreInfoList = userSponcerScoreRepository.findByCompanyID(companyId);
		UserSponsorScores scoreInfo = new UserSponsorScores();
		if(scoreInfoList != null && scoreInfoList.size() > 0) {
			scoreInfo = scoreInfoList.get(0);
		}
		
		String fbAppendQuery = "", twAppendQuery = "", inAppendQuery = "" , ytAppendQuery= "";
		reportList = new ArrayList<Reports>();
		
		List<Long> userIds = new ArrayList<Long>();
		Integer fbFeedCount = 0, fbLikeCount = 0, fbCommentCount = 0, fbShareCount = 0;
		
		for(Object[] obj: objectList) {
			val userId = (Integer)obj[0];
			
			int totalActivity = 0;
			val firstLastName =  obj[1].toString();
			
			Reports reports = new Reports();
			reports.setName(firstLastName);
			reports.setUserId(Long.valueOf(userId));
			
			//#Facebook Related Content				
			//#-------------------------Facebook Feed Count
			
			userIds = new ArrayList<Long>();
			userIds.add(Long.valueOf(userId));
			
			val fbList = getTotalFBFeedByUser(userIds, keywordsFB, fromDate, toDate, fbAppendQuery, false);
			
			fbFeedCount = 0; fbLikeCount = 0; fbCommentCount = 0; fbShareCount = 0;
			for (Object[] objFb : fbList) {
				fbFeedCount += Integer.parseInt(objFb[0].toString()); 
				fbLikeCount += ((BigDecimal) objFb[1]).intValue();
				fbCommentCount += ((BigDecimal) objFb[2]).intValue();
				fbShareCount += ((BigDecimal) objFb[3]).intValue();
				
				reports.setFbFeedCount(fbFeedCount);
				reports.setFbLikeCount(fbLikeCount);
				reports.setFbCommentCount(fbCommentCount);
				reports.setFbShareCount(fbShareCount);				
				
			}
			
			val twList = getTotalTWFeedByUser(userIds, keywordsTW, fromDate, toDate, twAppendQuery, false);
			for (Object[] objTw : twList) {
				reports.setTwFavCount(((BigDecimal) objTw[2]).intValue());
				reports.setTwFeedCount(((BigInteger) objTw[0]).intValue());
				reports.setTwReTweetCount(((BigDecimal) objTw[1]).intValue());
			}
			
			val inList = getTotalINFeedByUser(userIds, keywordsIN, fromDate, toDate, inAppendQuery, false);
			for (Object[] objIn : inList) {
				reports.setInFeedCount(((BigInteger) objIn[0]).intValue());
				reports.setInLikeCount(((BigDecimal) objIn[1]).intValue());
				reports.setInCommentCount(((BigDecimal) objIn[2]).intValue());				
			}
			
			val ytList = getTotalYTFeedByUser(userIds, keywordsYT, fromDate, toDate, ytAppendQuery, false);
			for (Object[] objYt : ytList) {
				reports.setYtFeedCount(((BigInteger) objYt[0]).intValue());
				reports.setYtViewCount(((BigDecimal) objYt[1]).intValue());
			}
			
			//#Tournaments Creation Related Data	
			val tournamentData = getTournamentsData(Long.valueOf(userId), fromDate, toDate, "", "", companyId);
			for(Object[] objTou : tournamentData) {
				reports.setTournamentsCount(Integer.parseInt(objTou[0].toString()));
			}
			
			//#For Blog Creation
			val blogList = getTotalBlogCount(Long.valueOf(userId), fromDate, toDate, "", companyId);
			reports.setBlogCount(blogList);
			
			//#For Leads Creation
			val leadsList = getTotalLeads(Long.valueOf(userId), fromDate, toDate, "", companyId);
			for(Object[] objLead : leadsList) {
				reports.setLeadsCount(Integer.parseInt(objLead[0].toString()));
			}
			
			//#For Print Creation
			val printList = getTotalPrint(Long.valueOf(userId), fromDate, toDate, "", companyId);
			reports.setPrintCount(printList);
			
			//#For Events Creation
			val eventList = getTotalEvent(Long.valueOf(userId), fromDate, toDate, "", companyId);
			for(Object[] objEvent : eventList) {
				reports.setEventCount(Integer.parseInt(objEvent[0].toString()));
			}
			
			totalActivity = totalActivity + (reports.getFbFeedCount()) * (scoreInfo.getFbpost() == null ? 1 : scoreInfo.getFbpost()); 
			totalActivity = totalActivity + (reports.getFbLikeCount()) * (scoreInfo.getFblike() == null ? 1 : scoreInfo.getFblike());
			totalActivity = totalActivity + (reports.getFbCommentCount()) * (scoreInfo.getFbcomments() == null ? 1 : scoreInfo.getFbcomments());
			totalActivity = totalActivity + (reports.getFbShareCount()) * (scoreInfo.getFbshares() == null ? 1 : scoreInfo.getFbshares());
			
			totalActivity = totalActivity + (reports.getInFeedCount()) * (scoreInfo.getInstaposts() == null ? 1 : scoreInfo.getInstaposts());
			totalActivity = totalActivity + (reports.getInLikeCount()) * (scoreInfo.getInstalikes() == null ? 1 : scoreInfo.getInstalikes());
			totalActivity = totalActivity + (reports.getInCommentCount()) * (scoreInfo.getInstacomments() == null ? 1 : scoreInfo.getInstacomments());
			
			totalActivity = totalActivity + (reports.getTwFeedCount()) * (scoreInfo.getTwttweets() == null ? 1 : scoreInfo.getTwttweets());
			totalActivity = totalActivity + (reports.getTwReTweetCount()) * (scoreInfo.getTwtretweets() == null ? 1 : scoreInfo.getTwtretweets());
			totalActivity = totalActivity + (reports.getTwFavCount()) * (scoreInfo.getTwtfavourites() == null ? 1 : scoreInfo.getTwtfavourites());
			
			totalActivity = totalActivity + (reports.getYtFeedCount()) * (scoreInfo.getYtposts() == null ? 1 : scoreInfo.getYtposts());
			totalActivity = totalActivity + (reports.getYtViewCount()) * (scoreInfo.getYtviews() == null ? 1 : scoreInfo.getYtviews());
			
			totalActivity = totalActivity + (reports.getTournamentsCount()) * (scoreInfo.getTournaments() == null ? 1 : scoreInfo.getTournaments());
			
			totalActivity = totalActivity + (reports.getBlogCount()) * (scoreInfo.getWebhits() == null ? 1 : scoreInfo.getWebhits());
			
			totalActivity = totalActivity + (reports.getEventCount()) * (scoreInfo.getEventworked() == null ? 1 : scoreInfo.getEventworked());
			
			totalActivity = totalActivity + (reports.getPrintCount()) * (scoreInfo.getPrints() == null ? 1 : scoreInfo.getPrints());
			
			totalActivity = totalActivity + (reports.getLeadsCount()) * (scoreInfo.getLeadsubmt() == null ? 1 : scoreInfo.getLeadsubmt());
						
			reports.setTotalActivity(totalActivity);
			
			reportList.add(reports);
		}
		
		Collections.sort(reportList, new Comparator<Reports>() {
			@Override
	        public int compare(Reports o1, Reports o2) {
	            return o2.getTotalActivity().compareTo(o1.getTotalActivity());
	        }
	    });

		return reportList;
	}	
	
	@Override
	public List<Reports> getReports(PaginationRequestDTO dto, Date fromDate, Date toDate, String staff,
			String keyword, Boolean count, Boolean graphQuery, Boolean fromExport, Long companyId) {
		
		val companyUser = userRepository.getOne(companyId);
		
		String keywordsFB = "", keywordsIN = "", keywordsTW = "", keywordsYT = "";
		
		if(StringUtils.isNotBlank(keyword)) {
			if(!keyword.equals("-1")) {
				keywordsFB = keyword;
				keywordsIN = keyword;
				keywordsTW = keyword;
				keywordsYT = keyword;
			}
		}else {
			keywordsFB = userService.getKeywords(companyUser, "fbkeywords");
			keywordsIN = userService.getKeywords(companyUser, "instakeywords");
			keywordsTW = userService.getKeywords(companyUser, "twtkeywords");
			keywordsYT = userService.getKeywords(companyUser, "youtubekeywords");
		}
		
		List<Object[]> objectList = new ArrayList<>();
		
		objectList = buildBasicQuery(dto, staff, count, companyId);
		
		List<Reports> reportList = new ArrayList<Reports>();
		
		if(count && !fromExport) {
			for(Object[] obj: objectList) {
				val userId = (Integer)obj[0];
				Reports reports = new Reports();
				reports.setUserId(Long.valueOf(userId));
				reportList.add(reports);
			}
			return reportList;
		}
		
		
		List<UserSponsorScores> scoreInfoList = userSponcerScoreRepository.findByCompanyID(companyId);
		UserSponsorScores scoreInfo = new UserSponsorScores();
		if(scoreInfoList != null && scoreInfoList.size() > 0) {
			scoreInfo = scoreInfoList.get(0);
		}
		
		
		String fbAppendQuery = "", twAppendQuery = "", inAppendQuery = "" , ytAppendQuery= "";
		reportList = new ArrayList<Reports>();
		
		List<Long> userIds = new ArrayList<Long>();
		Integer fbFeedCount = 0, fbLikeCount = 0, fbCommentCount = 0, fbShareCount = 0;
		
		for(Object[] obj: objectList) {
			val userId = (Integer)obj[0];
			val teamId = obj[2] == null ? "0" : obj[2].toString();
			
			int totalActivity = 0;
			
			
			val firstLastName =  obj[1].toString();
			
			Reports reports = new Reports();
			reports.setName(firstLastName);
			reports.setUserId(Long.valueOf(userId));
			reports.setTeamId(Long.parseLong(teamId));
			reports.setTeamName(obj[3] == null ? "" : obj[3].toString());
			
			//#Facebook Related Content				
			//#-------------------------Facebook Feed Count
			
			userIds = new ArrayList<Long>();
			userIds.add(Long.valueOf(userId));
			
			val fbList = getTotalFBFeedByUser(userIds, keywordsFB, fromDate, toDate, fbAppendQuery, false);
			
			fbFeedCount = 0; fbLikeCount = 0; fbCommentCount = 0; fbShareCount = 0;
			for (Object[] objFb : fbList) {
				
				fbFeedCount += Integer.parseInt(objFb[0].toString()); 
				fbLikeCount += ((BigDecimal) objFb[1]).intValue();
				fbCommentCount += ((BigDecimal) objFb[2]).intValue();
				fbShareCount += ((BigDecimal) objFb[3]).intValue();
				
				reports.setFbFeedCount(fbFeedCount);
				reports.setFbLikeCount(fbLikeCount);
				reports.setFbLikeCountFormatter(numberFormat.format(fbLikeCount));
				reports.setFbCommentCount(fbCommentCount);
				reports.setFbShareCount(fbShareCount);				
				
			}
			
			val twList = getTotalTWFeedByUser(userIds, keywordsTW, fromDate, toDate, twAppendQuery, false);
			for (Object[] objTw : twList) {
				reports.setTwFavCount(((BigDecimal) objTw[2]).intValue());
				reports.setTwFeedCount(((BigInteger) objTw[0]).intValue());
				reports.setTwReTweetCount(((BigDecimal) objTw[1]).intValue());
			}
			
			val inList = getTotalINFeedByUser(userIds, keywordsIN, fromDate, toDate, inAppendQuery, false);
			for (Object[] objIn : inList) {
				reports.setInFeedCount(((BigInteger) objIn[0]).intValue());
				reports.setInLikeCount(((BigDecimal) objIn[1]).intValue());
				reports.setInLikeCountFormatter(numberFormat.format(reports.getInLikeCount()));
				reports.setInCommentCount(((BigDecimal) objIn[2]).intValue());				
			}
			
			val ytList = getTotalYTFeedByUser(userIds, keywordsYT, fromDate, toDate, ytAppendQuery, false);
			for (Object[] objYt : ytList) {
				reports.setYtFeedCount(((BigInteger) objYt[0]).intValue());
				reports.setYtViewCount(((BigDecimal) objYt[1]).intValue());
			}
			
			//#Tournaments Creation Related Data	
			val tournamentData = getTournamentsData(Long.valueOf(userId), fromDate, toDate, "", "", companyId);
			for(Object[] objTou : tournamentData) {
				reports.setTournamentsCount(Integer.parseInt(objTou[0].toString()));
			}
			
			//#For Blog Creation
			val blogList = getTotalBlogCount(Long.valueOf(userId), fromDate, toDate, "", companyId);
			reports.setBlogCount(blogList);
			
			//#For Leads Creation
			val leadsList = getTotalLeads(Long.valueOf(userId), fromDate, toDate, "", companyId);
			for(Object[] objLead : leadsList) {
				reports.setLeadsCount(Integer.parseInt(objLead[0].toString()));
			}
			
			//#For Print Creation
			val printList = getTotalPrint(Long.valueOf(userId), fromDate, toDate, "", companyId);
			reports.setPrintCount(printList);
			
			//#For Events Creation
			val eventList = getTotalEvent(Long.valueOf(userId), fromDate, toDate, "", companyId);
			for(Object[] objEvent : eventList) {
				reports.setEventCount(Integer.parseInt(objEvent[0].toString()));
			}
			
			if(scoreInfo == null) {
				scoreInfo = new UserSponsorScores();
			}
			
			totalActivity = totalActivity + (reports.getFbFeedCount()) * (scoreInfo.getFbpost() == null ? 1 : scoreInfo.getFbpost()); 
			totalActivity = totalActivity + (reports.getFbLikeCount()) * (scoreInfo.getFblike() == null ? 1 : scoreInfo.getFblike());
			totalActivity = totalActivity + (reports.getFbCommentCount()) * (scoreInfo.getFbcomments() == null ? 1 : scoreInfo.getFbcomments());
			totalActivity = totalActivity + (reports.getFbShareCount()) * (scoreInfo.getFbshares() == null ? 1 : scoreInfo.getFbshares());
			
			totalActivity = totalActivity + (reports.getInFeedCount()) * (scoreInfo.getInstaposts() == null ? 1 : scoreInfo.getInstaposts());
			totalActivity = totalActivity + (reports.getInLikeCount()) * (scoreInfo.getInstalikes() == null ? 1 : scoreInfo.getInstalikes());
			totalActivity = totalActivity + (reports.getInCommentCount()) * (scoreInfo.getInstacomments() == null ? 1 : scoreInfo.getInstacomments());
			
			totalActivity = totalActivity + (reports.getTwFeedCount()) * (scoreInfo.getTwttweets() == null ? 1 : scoreInfo.getTwttweets());
			totalActivity = totalActivity + (reports.getTwReTweetCount()) * (scoreInfo.getTwtretweets() == null ? 1 : scoreInfo.getTwtretweets());
			totalActivity = totalActivity + (reports.getTwFavCount()) * (scoreInfo.getTwtfavourites() == null ? 1 : scoreInfo.getTwtfavourites());
			
			totalActivity = totalActivity + (reports.getYtFeedCount()) * (scoreInfo.getYtposts() == null ? 1 : scoreInfo.getYtposts());
			totalActivity = totalActivity + (reports.getYtViewCount()) * (scoreInfo.getYtviews() == null ? 1 : scoreInfo.getYtviews());
			
			totalActivity = totalActivity + (reports.getTournamentsCount()) * (scoreInfo.getTournaments() == null ? 1 : scoreInfo.getTournaments());
			
			totalActivity = totalActivity + (reports.getBlogCount()) * (scoreInfo.getWebhits() == null ? 1 : scoreInfo.getWebhits());
			
			totalActivity = totalActivity + (reports.getEventCount()) * (scoreInfo.getEventworked() == null ? 1 : scoreInfo.getEventworked());
			
			totalActivity = totalActivity + (reports.getPrintCount()) * (scoreInfo.getPrints() == null ? 1 : scoreInfo.getPrints());
			
			totalActivity = totalActivity + (reports.getLeadsCount()) * (scoreInfo.getLeadsubmt() == null ? 1 : scoreInfo.getLeadsubmt());
						
			reports.setTotalActivity(totalActivity);
			reports.setTotalActivityFormatter(numberFormat.format(totalActivity));
			
			reportList.add(reports);
		}
		
		Collections.sort(reportList, new Comparator<Reports>() {
			@Override
	        public int compare(Reports o1, Reports o2) {
	            return o2.getTotalActivity().compareTo(o1.getTotalActivity());
	        }
	    });

		return reportList;
	}	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTotalEvent(Long userId, Date fromDate, Date toDate, String appendQuery, Long companyId) {

		String queryStr = "SELECT DISTINCT IFNULL(count(*),0) as rec_total, 'a' FROM events " + 
				"	WHERE status='Active' and ownerid=:userId AND (find_in_set('all',sharedTO) or find_in_set(:companyId,sharedTO)) ";
		queryStr += "  and event_start_date >= :fromDate and event_start_date<= :toDate";
		
		queryStr += appendQuery;
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);
		if(companyId == null) {
			companyId = UserUtils.getLoggedInUserId();
		}
		nativeQuery.setParameter("companyId", companyId);
		nativeQuery.setParameter("fromDate", fromDate.getTime()/1000);
		nativeQuery.setParameter("toDate", toDate.getTime()/1000);		

		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	public Integer getTotalPrint(Long userId, Date fromDate, Date toDate, String appendQuery, Long companyId) {

		String queryStr = " SELECT DISTINCT IFNULL(count(*),0) as rec_total, 'a' FROM user_print " + 
				"	WHERE status='Active' and userid=:userId AND (find_in_set('all',sharedTO) or find_in_set(:companyId,sharedTO)) ";
		
		queryStr += "  and print_issue_date >= :fromDate and print_issue_date<= :toDate ";
		
		queryStr += appendQuery;
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);
		if(companyId == null) {
			companyId = UserUtils.getLoggedInUserId();
		}
		nativeQuery.setParameter("companyId", companyId);
		nativeQuery.setParameter("fromDate", fromDate);
		nativeQuery.setParameter("toDate", toDate);		

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		Integer totalPrint = 0;
		
		for(Object[] objBlog : list) {
			totalPrint += Integer.parseInt(objBlog[0].toString());
		}
		
		String queryStr1 = "SELECT IFNULL(count(*),0) as rec_total_pcs,IFNULL(sum(visitors),0) as impression_total FROM user_customscoops " + 
				"	WHERE sourceType='Magazine' AND find_in_set(:userId, user_id) AND find_in_set(:companyId, CS_sponsorID) ";
		
		queryStr1 += "  and created_date >= :fromDate and created_date <= :toDate ";
		
		queryStr1 += appendQuery;
		
		Query nativeQuery1 = em.createNativeQuery(queryStr1);

		nativeQuery1.setParameter("userId", userId);
		if(companyId == null) {
			companyId = UserUtils.getLoggedInUserId();
		}
		nativeQuery1.setParameter("companyId", companyId);
		nativeQuery1.setParameter("fromDate", fromDate.getTime()/1000);
		nativeQuery1.setParameter("toDate", toDate.getTime()/1000);		

		@SuppressWarnings("unchecked")
		List<Object[]> list1 = nativeQuery1.getResultList();
		
		for(Object[] objScoop : list1) {
			totalPrint += Integer.parseInt(objScoop[0].toString());
		}

		return totalPrint;
	}
	
	public List<Object[]> getTotalLeads(Long userId, Date fromDate, Date toDate, String appendQuery, Long companyId) {

		String queryStr = " SELECT DISTINCT IFNULL(count(*),0) as rec_total, 'a' FROM user_lead " + 
				"	WHERE status='Active' and userid=:userId AND (find_in_set('all',sharedTO) or find_in_set(:companyId,sharedTO)) ";
		
		queryStr += "  and posted_date >= :fromDate and posted_date<= :toDate ";
		
		queryStr += appendQuery;
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);
		if(companyId == null) {
			companyId = UserUtils.getLoggedInUserId();
		}
		nativeQuery.setParameter("companyId", companyId);
		nativeQuery.setParameter("fromDate", fromDate.getTime()/1000);
		nativeQuery.setParameter("toDate", toDate.getTime()/1000);		

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	public List<Object[]> getTournamentsData(Long userId, Date fromDate, Date toDate, String appendQuery, String firstPlace, Long companyId) {

		String queryStr = "SELECT DISTINCT IFNULL(count(*),0) as rec_total, 'a' FROM tournaments " + 
				"		WHERE staffid=:userId AND (find_in_set('all',sharedTO) or find_in_set(:companyId,sharedTO)) ";
		
		if(StringUtils.isNotBlank(firstPlace) && firstPlace.equalsIgnoreCase("1st")) {
			queryStr += "  and tfinishedplace=1 ";
		}
		
		queryStr += "  and tournamentdate >= :fromDate and tournamentdate<= :toDate ";
		
		queryStr += appendQuery;
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);
		if(companyId == null) {
			companyId = UserUtils.getLoggedInUserId();
		}
		nativeQuery.setParameter("companyId", companyId);
		nativeQuery.setParameter("fromDate", fromDate);
		nativeQuery.setParameter("toDate", toDate);		

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	public Integer getTotalBlogCount(Long userId, Date fromDate, Date toDate, String appendQuery, Long companyId) {

		String queryStr = "SELECT DISTINCT IFNULL(count(*),0) as rec_total, 'a' FROM user_blog " + 
				"		WHERE user_blog.status='Active' and userid=:userId  AND (find_in_set('all',sharedTO) or find_in_set(:companyId,sharedTO)) ";
		
		queryStr += "  and posted_date >= :fromDate and posted_date <= :toDate ";
		
		queryStr += appendQuery;
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);
		if(companyId == null) {
			companyId = UserUtils.getLoggedInUserId();
		}
		nativeQuery.setParameter("companyId", companyId);
		nativeQuery.setParameter("fromDate", fromDate);
		nativeQuery.setParameter("toDate", toDate);		

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		Integer totalEntries = 0;
		
		for(Object[] objBlog : list) {
			totalEntries = Integer.parseInt(objBlog[0].toString());
		}
		
		String queryStr1 = "SELECT IFNULL(count(*),0) as rec_total_cs,IFNULL(sum(visitors),0) as impression_total " + 
				"			FROM user_customscoops " + 
				"			WHERE find_in_set(:userId, user_id) and find_in_set(:companyId, CS_sponsorID) ";
		
		queryStr1 += "  and created_date >= :fromDate and created_date <= :toDate ";
		
		queryStr1 += appendQuery;
		
		Query nativeQuery1 = em.createNativeQuery(queryStr1);

		nativeQuery1.setParameter("userId", userId);
		if(companyId == null) {
			companyId = UserUtils.getLoggedInUserId();
		}
		nativeQuery1.setParameter("companyId", companyId);
		nativeQuery1.setParameter("fromDate", fromDate);
		nativeQuery1.setParameter("toDate", toDate);		

		@SuppressWarnings("unchecked")
		List<Object[]> list1 = nativeQuery1.getResultList();
		
		for(Object[] objScoop : list1) {
			totalEntries += Integer.parseInt(objScoop[0].toString());
		}

		return totalEntries;
	}
	
	public List<Object[]> getTotalFBFeedByKeyword(List<Long> userId, String keywords, Date fromDate, Date toDate) {

		String queryStr = "SELECT count(DISTINCT link) AS rec_total,IFNULL(sum(likes_count),0) AS rec_total_likes, " + 
				"		IFNULL(sum(comment_count),0) AS rec_total_comments, "+
				" IFNULL(sum(share_count),0) AS rec_total_shares "; 
		
		if(StringUtils.isNotBlank(keywords)) {
			queryStr += ", '"+keywords+"' AS keyword";
		}
		
		queryStr += " FROM user_feed WHERE user_id IN (:userId) and created_time between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d') "
				+ " AND (feed_type !='video' OR feed_type = 'video')";
		
		if(StringUtils.isNotBlank(keywords)) {
			queryStr += " AND story REGEXP :keywords ";
			queryStr += "GROUP BY keyword";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);
		nativeQuery.setParameter("fromDate", fromDate);
		nativeQuery.setParameter("toDate", toDate);
		if(StringUtils.isNotBlank(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}		

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	public List<Object[]> getTotalTWFeedByKeyword(List<Long> userId, String keywords, Date fromDate, Date toDate) {
	
		String queryStr = "SELECT count(DISTINCT link) as rec_total,IFNULL(sum(retweet_count),0) as rec_total_retweet, " + 
				"		IFNULL(sum(favorite_count),0) as rec_total_fav ";
		if(StringUtils.isNotBlank(keywords)) {
			queryStr += ", '"+keywords+"' AS keyword";
		}
		queryStr += " FROM user_tweets where user_id in (:userId) and created_at>= :fromDate and created_at <= :toDate ";
		
		if(StringUtils.isNotBlank(keywords)) {
			queryStr += " AND TEXT REGEXP :keywords ";			
			queryStr += "GROUP BY keyword";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);
		
		try{
			String stringFromDate= new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
			String stringToDate= new SimpleDateFormat("yyyy-MM-dd").format(toDate);
			nativeQuery.setParameter("fromDate", stringFromDate );
			nativeQuery.setParameter("toDate",stringToDate);
			System.out.println("fromDate:  "+stringFromDate);
			System.out.println("toDate:  "+stringToDate);
			}catch(Exception e) {
				e.printStackTrace();
			}
//		nativeQuery.setParameter("fromDate", fromDate.getTime()/1000);
//		nativeQuery.setParameter("toDate", toDate.getTime()/1000);
		if(StringUtils.isNotBlank(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	public List<Object[]> getTotalINFeedByKeyword(List<Long> userId, String keywords, Date fromDate, Date toDate) {

		String queryStr = "SELECT count(DISTINCT link) as rec_total, IFNULL(sum(likes),0) as rec_total_likes,"
				+ " IFNULL(sum(comments_count),0) as rec_total_comments ";
		if(StringUtils.isNotBlank(keywords)) {
			queryStr += ", '"+keywords+"' AS keyword";
		}
		queryStr += " FROM user_instagram_feed where user_id in (:userId) and created_time >= :fromDate and created_time <= :toDate ";
		if(StringUtils.isNotBlank(keywords)) {
			queryStr += " AND TEXT REGEXP :keywords ";
			queryStr += "GROUP BY keyword";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);

		nativeQuery.setParameter("fromDate", fromDate.getTime() / 1000);
		nativeQuery.setParameter("toDate", toDate.getTime() / 1000);
		if(StringUtils.isNotBlank(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	public List<Object[]> getTotalYTFeedByKeyword(List<Long> userId, String keywords, Date fromDate, Date toDate) {

		String queryStr = "SELECT count(DISTINCT video_id) as rec_total, "
				+ " IFNULL(sum(total_views),0) as rec_total_views ";
		
		if(StringUtils.isNotBlank(keywords)) {
			queryStr += ", '"+keywords+"' AS keyword";
		}
		
		queryStr += " FROM user_youtubefeeds where user_id in (:userId) and created_date between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d') ";
		if(StringUtils.isNotBlank(keywords)) {
			queryStr += " AND TEXT REGEXP :keywords ";
			queryStr += "GROUP BY keyword";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);
		nativeQuery.setParameter("fromDate", fromDate.getTime()/1000);
		nativeQuery.setParameter("toDate", toDate.getTime()/1000);
		if(StringUtils.isNotBlank(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	@Override
	public List<Object[]> getTotalFBFeedByUser(List<Long> userId, String keywords, Date fromDate, Date toDate, String appendQuery, Boolean graphQuery) {

		String queryStr = "SELECT count(DISTINCT link) AS rec_total,IFNULL(sum(likes_count),0) AS rec_total_likes, " + 
				"		IFNULL(sum(comment_count),0) AS rec_total_comments,IFNULL(sum(share_count),0) AS rec_total_shares, monthname(created_time) as month " + 
				"		FROM user_feed WHERE created_time between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d') "
				+ " AND (feed_type !='video' OR feed_type = 'video')";
		
		
		
		if(userId != null && userId.size() > 0) {
			queryStr += " AND user_id IN (:userId) ";
		}
		
		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND story REGEXP :keywords ";
		}		
		
		queryStr += appendQuery;
		
		if(graphQuery) {
			queryStr += "GROUP BY month";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		
		nativeQuery.setParameter("fromDate", fromDate);
		nativeQuery.setParameter("toDate", toDate);
		
		if(userId != null && userId.size() > 0) {
			nativeQuery.setParameter("userId", userId);
		}
		
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	@Override
	public List<Object[]> getTotalTWFeedByUser(List<Long> userId, String keywords, Date fromDate, Date toDate, String appendQuery, Boolean graphQuery) {

		String queryStr = "SELECT count(DISTINCT link) as rec_total,IFNULL(sum(retweet_count),0) as rec_total_retweet, " + 
				"		IFNULL(sum(favorite_count),0) as rec_total_fav, monthname(created_at) as month " + 
				"		FROM user_tweets where created_at>= :fromDate and created_at <= :toDate   ";
		// created_at>= :fromDate and created_at <= :toDate  
		if(userId != null && userId.size() > 0) {
			queryStr += " AND  user_id IN (:userId) ";
		}
		
		if (!StringUtils.isEmpty(keywords)) {
		queryStr += " AND TEXT REGEXP :keywords ";
		}		
		
		queryStr += appendQuery;
		
		if(graphQuery) {
			queryStr += "GROUP BY month";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		try{
			String stringFromDate= new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
			String stringToDate= new SimpleDateFormat("yyyy-MM-dd").format(toDate);
			nativeQuery.setParameter("fromDate", stringFromDate );
			nativeQuery.setParameter("toDate",stringToDate);
			System.out.println("fromDate:  "+stringFromDate);
			System.out.println("toDate:  "+stringToDate);
			}catch(Exception e) {
				e.printStackTrace();
			}
//		nativeQuery.setParameter("fromDate", fromDate.getTime()/1000);
//		nativeQuery.setParameter("toDate", toDate.getTime()/1000);
	
		if(userId != null && userId.size() > 0) {
			nativeQuery.setParameter("userId", userId);
		}
		
	//	nativeQuery.setParameter("userId", 413);

		
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	@Override
	public List<Object[]> getTotalINFeedByUser(List<Long> userId, String keywords, Date fromDate, Date toDate, String appendQuery, Boolean graphQuery) {

		
//		String queryStr = "SELECT DISTINCT IFNULL(count(monthname(created_time)),0) AS rec_total,IFNULL(sum(likes_count),0) AS rec_total_likes, " + 
//				"		IFNULL(sum(comment_count),0) AS rec_total_comments,IFNULL(sum(share_count),0) AS rec_total_shares, monthname(created_time) as month " + 
//				"		FROM user_feed WHERE created_time between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d') "
//				+ " AND (feed_type !='video' OR feed_type = 'video')";
		
//		String queryStr = "SELECT count(DISTINCT link) as rec_total, IFNULL(sum(likes),0) as rec_total_likes,"
//				+ " IFNULL(sum(comments_count),0) as rec_total_comments, monthname(FROM_UNIXTIME(created_time)) as month ,IFNULL(count(monthname(FROM_UNIXTIME(created_time))),0) as rec_total1 " + 
//				"	FROM user_instagram_feed where created_time >= :fromDate and created_time <= :toDate ";
//		
		
		String queryStr = "SELECT count(DISTINCT link) as rec_total, IFNULL(sum(likes),0) as rec_total_likes,"
				+ " IFNULL(sum(comments_count),0) as rec_total_comments, monthname(FROM_UNIXTIME(created_time)) as month ,IFNULL(count(monthname(FROM_UNIXTIME(created_time))),0) as rec_total1 " + 
				"	FROM user_instagram_feed where created_time >= :fromDate and created_time <= :toDate ";
		
		if(userId != null && userId.size() > 0) {
			queryStr += " AND user_id IN (:userId) ";
		}
		
		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND TEXT REGEXP :keywords ";
		}		
		
		queryStr += appendQuery;
		
		if(graphQuery) {
			queryStr += "GROUP BY month";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);
//		try{
//			String stringFromDate= new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
//			String stringToDate= new SimpleDateFormat("yyyy-MM-dd").format(toDate);
//			nativeQuery.setParameter("fromDate", stringFromDate );
//			nativeQuery.setParameter("toDate",stringToDate);
//			System.out.println("fromDate:  "+stringFromDate);
//			System.out.println("toDate:  "+stringToDate);
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
	//	nativeQuery.setParameter("fromDate", fromDate.getTime() / 1000);
		nativeQuery.setParameter("fromDate", fromDate.getTime()/1000);

		nativeQuery.setParameter("toDate", toDate.getTime() / 1000);
		
		if(userId != null && userId.size() > 0) {
			nativeQuery.setParameter("userId", userId);
		}		
		
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	
	public List<Object[]> getTotalYTFeedByUser(List<Long> userId, String keywords, Date fromDate, Date toDate, String appendQuery, Boolean graphQuery) {

		String queryStr = "SELECT count(DISTINCT video_id) as rec_total,IFNULL(sum(total_views),0) as rec_total_views, monthname(created_date) as month " + 
				"	FROM user_youtubefeeds where created_date between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d') ";
		
		if(userId != null && userId.size() > 0) {
			queryStr += " AND user_id IN (:userId) ";
		}
		
		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND TEXT REGEXP :keywords ";
		}		
		
		queryStr += appendQuery;
		
		if(graphQuery) {
			queryStr += "GROUP BY month";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("fromDate", fromDate.getTime()/1000);
		nativeQuery.setParameter("toDate", toDate.getTime()/1000);
		
		if(userId != null && userId.size() > 0) {
			nativeQuery.setParameter("userId", userId);
		}
		
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}
	

}
