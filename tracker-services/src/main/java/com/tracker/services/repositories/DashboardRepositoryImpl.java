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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.tracker.commons.dtos.DashboardActiveMembersResponse;
import com.tracker.commons.dtos.DashboardSocialActivityPerSiteResponse;
import com.tracker.commons.models.CompanyRef;
import com.tracker.commons.models.DashboardMostusedKeywordYearly;
import com.tracker.commons.models.DashboardSocialmediaPerSite;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserInstragramFeed;
import com.tracker.commons.models.UserTeam;
import com.tracker.services.utils.UserUtils;

import lombok.val;

@Repository
@Transactional
public class DashboardRepositoryImpl implements DashboardRepositoryCustom {

	@PersistenceContext
	EntityManager em;
	
	@Override
	public List<DashboardSocialmediaPerSite> fetchTotalFeebByClient(Long companyId) {
		
		String queryStr = "SELECT * "
				+ "		FROM dashboard_socialmedia_per_site "
				+ "		WHERE companyId= :companyId";

		Query nativeQuery = em.createNativeQuery(queryStr, DashboardSocialmediaPerSite.class);

		nativeQuery.setParameter("companyId", companyId);

		@SuppressWarnings("unchecked")
		List<DashboardSocialmediaPerSite> list = nativeQuery.getResultList();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTotalFBFeedByClientAndDate(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds) {

		String queryStr = "SELECT count(DISTINCT link) AS rec_total, IFNULL(sum(likes_count),0) AS rec_total_likes, "
				+ "		IFNULL(sum(comment_count),0) AS rec_total_comments, IFNULL(sum(share_count),0) AS rec_total_shares " 
				+ "		FROM user_feed UF "
				+"		WHERE user_id IN (:userIds)"
				+"      AND created_time between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d')  ";

		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND story REGEXP :keywords ";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userIds", userIds);
		nativeQuery.setParameter("fromDate", fromDate);
		nativeQuery.setParameter("toDate", toDate);
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTotalInstaFeedByClientAndDate(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds) {

		String queryStr = "SELECT count(DISTINCT link) as rec_total, IFNULL(sum(likes),0) as rec_total_likes,IFNULL(sum(comments_count),0) as rec_total_comments "
				+ "		FROM user_instagram_feed UIF " 
				+ "		WHERE UIF.user_id IN (:userIds)"
				+ "		AND created_time>= :fromDate AND created_time <= :toDate ";

		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND text REGEXP :keywords ";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userIds", userIds);
		nativeQuery.setParameter("fromDate", fromDate.getTime() / 1000);
		nativeQuery.setParameter("toDate", toDate.getTime() / 1000);
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}

	@Override
	public List<Object[]> getTotalTwFeedByClientAndDate(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds) {

		String queryStr = "SELECT count(DISTINCT link) as rec_total,IFNULL(sum(retweet_count),0) as rec_total_retweet, "
				+ "		IFNULL(sum(favorite_count),0) as rec_total_fav " 
				+ "		FROM user_tweets UT "
				+ "		WHERE UT.user_id IN (:userIds)"
				+ "		and created_at>= :fromDate and created_at <= :toDate";

		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND text REGEXP :keywords ";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userIds", userIds);
		nativeQuery.setParameter("fromDate", fromDate);
		nativeQuery.setParameter("toDate", toDate);
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTotalYtFeedByClientAndDate(Long companyId, String keywords, Date fromDate, Date toDate, List<Long> userIds) {

		String queryStr = "SELECT count(DISTINCT video_id) as rec_total,IFNULL(sum(total_views),0) as rec_total_views  "
				+ "		FROM user_youtubefeeds UY " 
				+ "		WHERE created_date between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d') ";
		
		if(userIds != null && userIds.size() > 0) {
			queryStr += " AND UY.user_id IN (:userIds) ";
		}

		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND text REGEXP :keywords ";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("fromDate", fromDate);
		nativeQuery.setParameter("toDate", toDate);
		
		if(userIds != null && userIds.size() > 0) {
			nativeQuery.setParameter("userIds", userIds);
		}
		
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}

		List<Object[]> list = nativeQuery.getResultList();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashboardActiveMembersResponse getMemberCount(Long companyID) {
		DashboardActiveMembersResponse response = new DashboardActiveMembersResponse();

		String queryStrActive = "SELECT DISTINCT u.userid, u.FirstLastname,u.at_profile_pic,u.city,u.state,u.phone,cr.staffRatings,cr.activation_reminder_date," + 
				"			  1 AS FB, 1 AS twt, 1 as Insta" + 
				"			  FROM users u" + 
				"			  LEFT JOIN company_ref cr ON cr.userid=u.userid" + 
				"			  WHERE u.status='Active' and cr.companyID= :companyID " + 
				" AND u.userid IN (Select userid from user_media um2)  ";

		Query nativeQuery = em.createNativeQuery(queryStrActive);

		nativeQuery.setParameter("companyID", companyID);

		List<Object[]> list = nativeQuery.getResultList();
		int activeMembers = 0;
		int inactiveMembers = 0;

		activeMembers = list.size();
		
		String queryStrInActive = "SELECT DISTINCT u.userid, u.FirstLastname,u.at_profile_pic,u.city,u.state,u.phone,cr.staffRatings, " + 
				"			  cr.activation_reminder_date,0 AS FB, 0 AS twt, 0 as Insta " + 
				"			  FROM users u " + 
				"			  LEFT JOIN company_ref cr ON cr.userid=u.userid " + 
				"			  WHERE u.status='Active' and cr.companyID=:companyID "
				+ "  AND u.userid NOT IN(Select userid from user_media um2) ";
		Query nativeQueryInActive = em.createNativeQuery(queryStrInActive);

		nativeQueryInActive.setParameter("companyID", companyID);

		List<Object[]> listInActive = nativeQueryInActive.getResultList();
		inactiveMembers = listInActive.size();

		response.setActiveMembers(activeMembers);
		response.setInactiveMembers(inactiveMembers);

		return response;
	}
	
	@Override
	public List<User> getMembers(Long companyID) {

		/*String queryStr = "select DISTINCT u.* from users u  "
				+ "left join company_ref cr on cr.userid=u.userid " 
				+ "where cr.companyID= :companyID "
				+ " and u.status='Active' and u.memtype='Staff' "
				+ "group by u.userid order by FirstLastname ASC ";*/
		String queryStr = "SELECT DISTINCT u.*, cr.staffRatings,cr.activation_reminder_date, " + 
				"			  1 AS FB, 1 AS twt, 1 as Insta " + 
				"			  FROM users u " + 
				"			  LEFT JOIN company_ref cr ON cr.userid=u.userid " + 
				"			  WHERE u.status='Active' and cr.companyID = :companyID " + 
				"AND (u.userid IN (Select userid from user_media um2) OR u.userid NOT IN (Select userid from user_media um2)) ";

		Query nativeQuery = em.createNativeQuery(queryStr, User.class);

		nativeQuery.setParameter("companyID", companyID);

		@SuppressWarnings("unchecked")
		List<User> list = nativeQuery.getResultList();

		return list;
	}
	
	@Override
	public List<CompanyRef> getRanklist(Long companyID) {
		

		String queryStr = "SELECT cr.prostaffName, cr.rank_score, u.at_profile_pic as atProfilePic, cr.userid " + 
				"			FROM company_ref cr "
				+ "         LEFT JOIN users u ON u.userid = cr.userid " + 
				"			WHERE cr.companyID=:companyID AND cr.rank>0 AND cr.rankUpdate_dated>= :yearStartDay AND cr.rankUpdate_dated <= :today" + 
				"			AND cr.rank_score>0 ORDER BY cr.rank asc";
		
		/*String queryStr = "SELECT top_pro_FirstLastname as prostaffName, top_pro_total_activity as rank_score, " + 
				" u.at_profile_pic as atProfilePic, top_pro_userid as userid" + 
				" FROM dashboardStats ds" + 
				" LEFT JOIN users u on u.userid = ds.top_pro_userid" + 
				" WHERE ds_companyID=:companyID";*/

		Query nativeQuery = em.createNativeQuery(queryStr);
		
		Date today = new Date();
		Date yearStartDay = new Date();
		
		
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, 1);
		yearStartDay = cal.getTime();
		
		nativeQuery.setParameter("companyID", companyID);
      nativeQuery.setParameter("yearStartDay", yearStartDay);
      nativeQuery.setParameter("today", today);
//        try{
//    		String stringFromDate= new SimpleDateFormat("yyyy-MM-dd").format(yearStartDay);
//    		String stringToDate= new SimpleDateFormat("yyyy-MM-dd").format(today);
//            nativeQuery.setParameter("yearStartDay", stringFromDate);
//            nativeQuery.setParameter("today", stringToDate);
//    		System.out.println("fromDate:  "+stringFromDate);
//    		System.out.println("toDate:  "+stringToDate);
//    	}catch(Exception e) {
//    			e.printStackTrace();
//    	}
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<CompanyRef> refList = new ArrayList<CompanyRef>();
		
		for (Object[] obj : list) {
			CompanyRef ref = new CompanyRef();
			ref.setProstaffName(obj[0].toString());
            ref.setRankScore(Integer.parseInt(obj[1].toString()));
			///BigDecimal b1 = new BigDecimal(obj[1].toString());
			//ref.setRankScore(b1.intValue());
			ref.setAtProfilePic(obj[2].toString());
			ref.setUserid(Long.parseLong(obj[3].toString()));
			refList.add(ref);
			
		}
//		CompanyRef ref = new CompanyRef();
//		ref.setProstaffName("dkjh");
//        ref.setRankScore(23);
//		///BigDecimal b1 = new BigDecimal(obj[1].toString());
//		//ref.setRankScore(b1.intValue());
//		ref.setAtProfilePic("http://www.cannabisreports.com/images/strains/no_image.png");
//		//ref.setUserid(companyID);
//		refList.add(ref);

		return refList;
	}
	
	
	@Override
	public List<CompanyRef> getRanklist1(Integer companyID) {
		
		String queryStr = "SELECT cr.prostaffName, cr.rank_score, u.at_profile_pic as atProfilePic, cr.userid " + 
				"			FROM company_ref cr "
				+ "         LEFT JOIN users u ON u.userid = cr.userid " + 
				"			WHERE cr.companyID=:companyID AND cr.rank>0 AND cr.rankUpdate_dated>= :yearStartDay AND cr.rankUpdate_dated <= :today" + 
				"			AND cr.rank_score>0 ORDER BY cr.rank asc";
		
		/*String queryStr = "SELECT top_pro_FirstLastname as prostaffName, top_pro_total_activity as rank_score, " + 
				" u.at_profile_pic as atProfilePic, top_pro_userid as userid" + 
				" FROM dashboardStats ds" + 
				" LEFT JOIN users u on u.userid = ds.top_pro_userid" + 
				" WHERE ds_companyID=:companyID";*/

		Query nativeQuery = em.createNativeQuery(queryStr);
		
		Date today = new Date();
		Date yearStartDay = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, 1);
		yearStartDay = cal.getTime();
		
		nativeQuery.setParameter("companyID", companyID);
        nativeQuery.setParameter("yearStartDay", yearStartDay);
        nativeQuery.setParameter("today", today);
//        try{
//    		String stringFromDate= new SimpleDateFormat("yyyy-MM-dd").format(yearStartDay);
//    		String stringToDate= new SimpleDateFormat("yyyy-MM-dd").format(today);
//            nativeQuery.setParameter("yearStartDay", stringFromDate);
//            nativeQuery.setParameter("today", stringToDate);
//    		System.out.println("fromDate:  "+stringFromDate);
//    		System.out.println("toDate:  "+stringToDate);
//    		}catch(Exception e) {
//    			e.printStackTrace();
//    		}
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<CompanyRef> refList = new ArrayList<CompanyRef>();
		
		
		
		for (Object[] obj : list) {
			CompanyRef ref = new CompanyRef();
			ref.setProstaffName(obj[0].toString());
            ref.setRankScore(Integer.parseInt(obj[1].toString()));
			///BigDecimal b1 = new BigDecimal(obj[1].toString());
			//ref.setRankScore(b1.intValue());
			ref.setAtProfilePic(obj[2].toString());
			ref.setUserid(Long.parseLong(obj[3].toString()));
			refList.add(ref);
			
		}
//		CompanyRef ref = new CompanyRef();
//		ref.setProstaffName("dkjh");
//        ref.setRankScore(23);
//		///BigDecimal b1 = new BigDecimal(obj[1].toString());
//		//ref.setRankScore(b1.intValue());
//		ref.setAtProfilePic("http://www.cannabisreports.com/images/strains/no_image.png");
//		//ref.setUserid(companyID);
//		refList.add(ref);

		return refList;
	}
	public List<Object[]> buildBasicQuery( String staff,
			Long loggedInUserId) {
			
			String queryStr = "SELECT DISTINCT u.userid,u.FirstLastname, 0 as teamId, '' as teamName " + 
					" from users u 			 " + 
					" LEFT JOIN company_ref cr ON cr.userid=u.userid" + 
					"			WHERE cr.ref_status='Active' and u.memtype='Staff' and cr.companyID=:companyID";
			
//			if(StringUtils.isNotBlank(staff)) {
//				queryStr += " AND u.userid IN (:staff)";
//			}
//			
			Query nativeQuery = em.createNativeQuery(queryStr);
			
			
			if(loggedInUserId == null) {
				nativeQuery.setParameter("companyID", UserUtils.getLoggedInUserId());
			}else {
				nativeQuery.setParameter("companyID", loggedInUserId);
			}
			
//			if(StringUtils.isNotBlank(staff)) {
//				String[] strArray = staff.split(",");
//				List<Long> lst = new ArrayList<Long>();
//				for(String str : strArray) {
//					lst.add(Long.parseLong(str));
//				}
//				nativeQuery.setParameter("staff", lst);
//			}
			
			
			@SuppressWarnings("unchecked")
			List<Object[]> objectList = nativeQuery.getResultList();
			
			if(loggedInUserId == null) {
				loggedInUserId = UserUtils.getLoggedInUserId();
			}
			
			List<UserTeam> userTeam = fetchTeamByUserId("", loggedInUserId);
			
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
		
		if(!TextUtils.isBlank(staff)){
			queryStr += " AND utm.userId IN (:staff)";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		
		if(!TextUtils.isBlank(staff)) {
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
	
	@Override
	public List<UserInstragramFeed> getInstagramImages(Long companyID, String keyword){
		List<Object[]> objectList = buildBasicQuery("", companyID);
		List<Long> userIds = new ArrayList<Long>();
		
		for(Object[] obj: objectList) {
			val userId = (Integer)obj[0];
			userIds.add(Long.valueOf(userId));
		}
//		String queryStr = "SELECT DISTINCT uif.link, uif.id,uif.text,uif.likes_count as likesCount, uif.standard_image as standardImage, "+
//				" uif.comments_count as commentsCount, u.userid,u.state, u.FirstLastname as firstLastname, uif.created_time as createdTime  " + 
//				"FROM user_instagram_feed uif " + 
//				
//				"WHERE um.social_type='instagram'  " + 
//				"and cr.companyID =:companyID ";
		String queryStr = "SELECT DISTINCT  uif.link, uif.id,uif.text,uif.likes_count as likesCount, uif.standard_image as standardImage, "+
				"uif.comments_count as commentsCount, u.userid,u.state, u.FirstLastname as firstLastname, uif.created_time as createdTime " + 
				"FROM user_instagram_feed uif " + 
				
				"LEFT JOIN users u ON u.userid = uif.user_id " + 
				
				"WHERE " ;
			;
		if(userIds != null && userIds.size() > 0) {
			queryStr += "  uif.user_id IN (:userIds) AND ";
		}
		
		if (!StringUtils.isEmpty(keyword)) {
			queryStr += "  uif.TEXT REGEXP :keywords AND ";
		}		
		
//		String queryStr = "SELECT DISTINCT uif.link, uif.id,uif.text,uif.likes_count as likesCount, uif.standard_image as standardImage, "+
//				" uif.comments_count as commentsCount, u.userid,u.state, u.FirstLastname as firstLastname, uif.created_time as createdTime " + 
//				"FROM user_instagram_feed uif " + 
//				"LEFT JOIN users u ON u.userid = uif.user_id " + 
//
//				"where uif.user_id=:userId ";
//		
		//String queryStr ="SELECT DISTINCT uif.id, uif.link as linkurl,uif.text,uif.likes_count,uif.standard_image FROM user_instagram_feed uif where uif.user_id=12133 and ( (uif.text like 'Angler Track%' or uif.text like '%Angler Track%') ) GROUP BY uif.id ORDER BY uif.created_time desc";
//		if(!keyword.contains("|")) {
//		if (!StringUtils.isEmpty(keyword)) {
//			queryStr += " AND uif.TEXT REGEXP :keyword ";
//		}
//		}
		//keyword= "Angler Track|anlertrack.net";

//		if(keyword.contains("|")) {
//			keyword.replace("|",":");
//		}
		//keyword = keyword.replace("|","| ");
//		String[] keywordList = keyword.split("\\|");
//		
//		if(keywordList.length >0) {
//			queryStr += " AND (";
//		}
//		
//		for(int index = 0;index<keywordList.length;index++) {
//			
//			if(index == 0) {
//				queryStr += "uif.TEXT like" ;
//				queryStr += " \"%"+keywordList[index]+"%\"";
//			}else{
//				queryStr += " OR uif.TEXT like" ;
//				queryStr += " \"%"+keywordList[index]+"%\"";
//				//queryStr += "  uif.TEXT like + keywordList[index]";
//		
//				//queryStr += " OR uif.TEXT like +"\"%"+keyword[index]+"%\"";
//			}
//		}
//		
//		if(keywordList.length >0) {
//			queryStr += " )";
//		}
//		if(userList.size() > 0) {
//			queryStr += " AND uif.user_id in (:userId)";
//		}
		
	queryStr += "  uif.created_time >= :fromDate and uif.created_time <= :toDate ";
		
	queryStr +=	"ORDER BY uif.likes_count desc " +
			"limit 20 ";

		Query nativeQuery = em.createNativeQuery(queryStr);
	//nativeQuery.setParameter("companyID", companyID);
		
		if (!StringUtils.isEmpty(keyword)) {

	nativeQuery.setParameter("keywords", keyword);
		}

//		if(!keyword.contains("|")) {
//
//		
//		if (!StringUtils.isEmpty(keyword)) {
//			if(keyword.contains("angler")) {
//				
//				nativeQuery.setParameter("keyword", "Angler Track");
//
//			}else {
//			nativeQuery.setParameter("keyword", keyword);
//		}
//		}
//		}
		//nativeQuery.setParameter("keyword", "Angler Track");

		
//		if(userList.size() > 0) {
//			nativeQuery.setParameter("userId", userList);
//		}
//		nativeQuery.setParameter("userId", UserUtils.getLoggedInUserId());
//		try{
//		String stringFromDate= new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
//		String stringToDate= new SimpleDateFormat("yyyy-MM-dd").format(toDate);
//		nativeQuery.setParameter("fromDate", stringFromDate );
//		nativeQuery.setParameter("toDate",stringToDate);
//		System.out.println("fromDate:  "+stringFromDate);
//		System.out.println("toDate:  "+stringToDate);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
	Date today = new Date();

	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MONTH, -1);
	Date today31 = cal.getTime();
	
		nativeQuery.setParameter("fromDate", today31.getTime()/1000 );
		nativeQuery.setParameter("toDate",today.getTime()/1000);
		if(userIds != null && userIds.size() > 0) {
			nativeQuery.setParameter("userIds", userIds);
		}	
		//1618790401
//
//		
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<UserInstragramFeed> imageList = new ArrayList<UserInstragramFeed>();
		System.out.println(queryStr);
		
		System.out.println("companyID:  "+companyID);
		System.out.println(" keyword:  "+keyword);
		System.out.println("userId:  "+userIds.size());
		

		System.out.println("size:"+list.size());


		for (Object[] obj : list) {
			
			try {
			UserInstragramFeed insta = new UserInstragramFeed(); 
			if(obj[0]!= null) {
			 insta.setLink(obj[0].toString());
			}
			if(obj[1]!= null) {

			insta.setId(obj[1].toString());
			}
			if(obj[2]!= null) {

			insta.setText(obj[2].toString());
			}
			insta.setLikesCount(Integer.parseInt(obj[3].toString()));
			
			if(obj[4]!= null) {
				
				insta.setStandardImage(obj[4].toString());
				
			}
			if(obj[5]!= null) {

			insta.setCommentsCount(Integer.parseInt(obj[5].toString()));
			}
			if(obj[8]!= null) {

			insta.setFirstLastname(obj[8].toString());
			}
//			
		//	long date = obj[9] == null ? 0l : Long.parseLong(obj[9].toString());
//			LocalDate localDate = new java.sql.Date(date * 1000).toLocalDate();
			if(obj[9]!= null) {
				try{
					Date date = new Date ();
					date.setTime(Long.parseLong(obj[9].toString())*1000);
					String stringFromDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
					insta.setCreatedTime(stringFromDate);

					}catch(Exception e) {
						e.printStackTrace();
						insta.setCreatedTime(obj[9] == null ? "" : obj[9].toString());

					}

		//	insta.setCreatedTime(obj[9] == null ? "" : obj[9].toString());
			}
//
//			String createdTimeFormatter = localDate.format(DateTimeFormatter.ofPattern("dd, MMM yyyy"));
			//Date date = new Date(obj[9].toString());
			if(obj[9]!= null) {

			insta.setCreatedTimeFormatter(obj[9] == null ? "" : obj[9].toString());
			}

			insta.setSocial_type("instagram");
			if(testImage(insta.getStandardImage())) {
				imageList.add(insta);
			}
			
			if(imageList.size() ==10)

				break;
			//imageList.add(insta);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
   //	imageList.addAll(getFacebookImages( companyID,  keyword, userList,  fromDate,  toDate ));

   	
//   	
		return imageList;

	}
	
	public List<UserInstragramFeed> getInstagramImages1(Long companyID, String keyword) {

		String queryStr = "SELECT DISTINCT uif.link, uif.id,uif.text,uif.likes_count as likesCount, uif.standard_image as standardImage, uif.comments_count, u.userid,u.state " + 
				"FROM user_instagram_feed uif " + 
				"LEFT JOIN company_ref cr ON cr.userid = uif.user_id " + 
				"LEFT JOIN users u ON u.userid = uif.user_id " + 
				//"LEFT JOIN user_media um ON um.userid=uif.user_id " + 
				"WHERE "+
				//"um.social_type='instagram'  " + 
				"cr.companyID =:companyID "+
				"and uif.created_time>= :today31 and uif.created_time <= :today ";
		if (!StringUtils.isEmpty(keyword)) {
			queryStr += " AND uif.text REGEXP :keyword ";
		}
		queryStr +=	"ORDER BY uif.likes_count desc " +
				"limit 10 ";

		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		
		Date today = new Date();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -95);
		Date today31 = cal.getTime();
		
		long todayL = today.getTime() / 1000;
		long today31L = today31.getTime() / 1000;
		
		nativeQuery.setParameter("today31", today31L);
		nativeQuery.setParameter("today", todayL);
		
		if (!StringUtils.isEmpty(keyword)) {
			nativeQuery.setParameter("keyword", keyword);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<UserInstragramFeed> imageList = new ArrayList<UserInstragramFeed>();
		
		//int i = 0;
		
		for (Object[] obj : list) {
			
			//if(i < 10) {
				UserInstragramFeed insta = new UserInstragramFeed(); 
				insta.setLink(obj[0].toString());
				insta.setId(obj[1].toString());
				insta.setText(obj[2].toString());
				insta.setLikesCount(Integer.parseInt(obj[3].toString()));
				insta.setStandardImage(obj[4].toString());
				insta.setCommentsCount(Integer.parseInt(obj[5].toString()));
				
				if(testImage(insta.getStandardImage())) {
					imageList.add(insta);
				}
				
			//}
			//i++;
			
		}

		return imageList;
	}
	
	public Boolean testImage(String url) {
		try {
			BufferedImage image = ImageIO.read(new URL(url));
			if (image != null) {
				return true;
			} else {
				return false;
			}

		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public List<DashboardMostusedKeywordYearly> getMostUsedKeyword(Long companyId) {
		
		String queryStr = "SELECT * "
				+ "		FROM dashboard_mostused_keyword_ytd "
				+ "		WHERE companyId= :companyId";

		Query nativeQuery = em.createNativeQuery(queryStr, DashboardMostusedKeywordYearly.class);

		nativeQuery.setParameter("companyId", companyId);

		
		List<DashboardMostusedKeywordYearly> list = nativeQuery.getResultList();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getMostUsedKeywordFb(Long userId, String kword, String socialType, Date fromDate, Date toDate) {

		String queryStr = "SELECT IFNULL(count(*),0) AS rec_total, 'a' ";

		if (socialType.equalsIgnoreCase("fb")) {
			queryStr += "		FROM user_feed UF ";
		}

		if (socialType.equalsIgnoreCase("in")) {
			queryStr += "		FROM user_instagram_feed UF ";
		}

		if (socialType.equalsIgnoreCase("tw")) {
			queryStr += "		FROM user_tweets UF ";
		}

		if (socialType.equalsIgnoreCase("yt")) {
			queryStr += "		FROM user_youtubefeeds UF ";
		}

		queryStr += "		LEFT JOIN company_ref  CR ON CR.userid=UF.user_id " + "		WHERE CR.companyID= :userId ";

		if (!StringUtils.isEmpty(kword)) {
			if (socialType.equalsIgnoreCase("fb")) {
				queryStr += " AND story LIKE :kword ";
			} else {
				queryStr += " AND text LIKE :kword ";
			}
		}
		
		if (socialType.equalsIgnoreCase("fb")) {
			queryStr += "and created_time between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d')";
		}
		
		if (socialType.equalsIgnoreCase("tw")) {
			queryStr += " and created_at>= :fromDate and created_at <= :toDate ";
		}
		
		if (socialType.equalsIgnoreCase("in")) {
			queryStr += " and created_time >= :fromDate and created_time <= :toDate ";
		}
		
		if (socialType.equalsIgnoreCase("yt")) {
			queryStr += " and created_date between DATE_FORMAT(:fromDate, '%Y-%m-%d') and DATE_FORMAT(:toDate, '%Y-%m-%d') ";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);
		if (!StringUtils.isEmpty(kword)) {
			nativeQuery.setParameter("kword", "%" + kword + "%");
		}
		
		if (socialType.equalsIgnoreCase("fb")) {
			nativeQuery.setParameter("fromDate", fromDate);
			nativeQuery.setParameter("toDate", toDate);
		}
		
		if (socialType.equalsIgnoreCase("tw")) {
			nativeQuery.setParameter("fromDate", fromDate);
			nativeQuery.setParameter("toDate", toDate);
		}
		
		if (socialType.equalsIgnoreCase("in")) {
			nativeQuery.setParameter("fromDate", fromDate.getTime() / 1000);
			nativeQuery.setParameter("toDate", toDate.getTime() / 1000);
		}
		
		if (socialType.equalsIgnoreCase("yt")) {
			nativeQuery.setParameter("fromDate", fromDate);
			nativeQuery.setParameter("toDate", toDate);
		}

		List<Object[]> list = nativeQuery.getResultList();
		Integer count = 0;

		for (Object[] obj : list) {
			count += ((BigInteger) obj[0]).intValue();
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashboardSocialActivityPerSiteResponse getTotalYTFeedCount(Long userId, String keywordsTW, String period,
			String feedType) {

		Query nativeQuery = buildYTQuery(userId, keywordsTW, period, feedType);

		DashboardSocialActivityPerSiteResponse response = new DashboardSocialActivityPerSiteResponse();

		List<Object[]> list = nativeQuery.getResultList();

		for (Object[] obj : list) {

			Integer ytRecTotal = ((BigInteger) obj[0]).intValue();
			Integer ytRecViewTotal = ((BigDecimal) obj[1]).intValue();

			response.setYtRecTotal(ytRecTotal);
			response.setYtRecViewTotal(ytRecViewTotal);
		}

		return response;
	}

	private Query buildYTQuery(Long userId, String keywordsTW, String period, String feedType) {

		String queryStr = "select IFNULL(count(*),0) as ytRecTotal, IFNULL(sum(ytf.total_views),0) as ytRecViewTotal ";

		if (feedType.equalsIgnoreCase("feed")) {
			queryStr += " FROM user_youtubefeeds ytf WHERE ytf.user_id = :userId ";
		}

		if (feedType.equalsIgnoreCase("hybrid")) {
			queryStr += " FROM user_youtubefeeds ytf " 
					+ " LEFT JOIN users u ON u.userid = ytf.user_id "
					+ "	LEFT JOIN company_ref cr ON cr.userid= ytf.user_id " 
					+ " WHERE cr.companyID = :userId";
		}

		if (!StringUtils.isEmpty(keywordsTW)) {
			queryStr += " AND ytf.text REGEXP :keywordsTW ";
		}

		if (!StringUtils.isEmpty(period)) {
			queryStr += " and ytf.created_date>= :today31 and ytf.created_date <= :today ";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);

		if (!StringUtils.isEmpty(keywordsTW)) {
			nativeQuery.setParameter("keywordsTW", keywordsTW);
		}

		if (!StringUtils.isEmpty(period)) {
			Date today = new Date();

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date today31 = cal.getTime();

			cal.add(Calendar.MONTH, -3);
			Date quartly = cal.getTime();

//			cal.set(Calendar.DAY_OF_MONTH, 1);
//			cal.set(Calendar.MONTH, Calendar.JANUARY);
//				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			cal.set(Calendar.DAY_OF_YEAR, -1);

			Date yearly = cal.getTime();

			System.out.print("jhgajgsdg"+ yearly.getTime());
			if (period.equalsIgnoreCase("Monthly")) {
				nativeQuery.setParameter("today31", today31);
			}

			if (period.equalsIgnoreCase("Quarterly")) {
				nativeQuery.setParameter("today31", quartly);
			}

			if (period.equalsIgnoreCase("Annually")) {
				nativeQuery.setParameter("today31", yearly);
			}

			nativeQuery.setParameter("today", today);
		}

		return nativeQuery;
	}

	@Override
	public Integer getTotalTWFeedCount(Long userId, String keywordsTW, String period, String feedType) {
		Query nativeQuery = buildTWQuery(userId, keywordsTW, period, feedType);
		if (feedType.equalsIgnoreCase("like") || feedType.equalsIgnoreCase("retweet")) {
			return ((BigDecimal) nativeQuery.getSingleResult()).intValue();
		}
		return ((BigInteger) nativeQuery.getSingleResult()).intValue();
	}

	private Query buildTWQuery(Long userId, String keywordsTW, String period, String feedType) {
		String queryStr = "select ";

		if (feedType.equalsIgnoreCase("feed")) {
			queryStr += " IFNULL(count(*),0) as rec_total ";
		}

		if (feedType.equalsIgnoreCase("like")) {
			queryStr += " IFNULL(sum(favorite_count),0) as rec_total ";
		}

		if (feedType.equalsIgnoreCase("retweet")) {
			queryStr += " IFNULL(sum(retweet_count),0) as rec_total ";
		}

		queryStr += " FROM user_tweets where user_id= :userId ";

		if (!StringUtils.isEmpty(keywordsTW)) {
			queryStr += " AND text REGEXP :keywordsTW ";
		}

		if (!StringUtils.isEmpty(period)) {
			queryStr += " and created_at>= :today31 and created_at <= :today ";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);

		if (!StringUtils.isEmpty(keywordsTW)) {
			nativeQuery.setParameter("keywordsTW", keywordsTW);
		}

		if (!StringUtils.isEmpty(period)) {
			Date today = new Date();

			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date today31 = cal.getTime();

			cal.add(Calendar.MONTH, -3);
			Date quartly = cal.getTime();

//			cal.set(Calendar.DAY_OF_MONTH, 1);
//			cal.set(Calendar.MONTH, Calendar.JANUARY);
//				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			cal.set(Calendar.DAY_OF_YEAR, -1);

				Date yearly = cal.getTime();

			if (period.equalsIgnoreCase("Monthly")) {
				nativeQuery.setParameter("today31", today31);
			}

			if (period.equalsIgnoreCase("Quarterly")) {
				nativeQuery.setParameter("today31", quartly);
			}

			if (period.equalsIgnoreCase("Annually")) {
				nativeQuery.setParameter("today31", yearly);
			}

			nativeQuery.setParameter("today", today);
		}

		return nativeQuery;
	}

	@Override
	public Integer getTotalINFeedCount(Long userId, String keywordsIN, String period, String feedType) {
		Query nativeQuery = buildINQuery(userId, keywordsIN, period, feedType);
		if (feedType.equalsIgnoreCase("like") || feedType.equalsIgnoreCase("comment")) {
			return ((BigDecimal) nativeQuery.getSingleResult()).intValue();
		}
		return ((BigInteger) nativeQuery.getSingleResult()).intValue();
	}

	private Query buildINQuery(Long userId, String keywordsIN, String period, String feedType) {

		String queryStr = "select";

		if (feedType.equalsIgnoreCase("feed")) {
			queryStr += " IFNULL(count(*),0) as rec_total ";
		}

		if (feedType.equalsIgnoreCase("like")) {
			queryStr += " IFNULL(sum(likes),0) as rec_total ";
		}

		if (feedType.equalsIgnoreCase("comment")) {
			queryStr += " IFNULL(sum(comments_count),0) as rec_total ";
		}

		queryStr += " FROM user_instagram_feed where user_id= :userId ";

		if (!StringUtils.isEmpty(keywordsIN)) {
			queryStr += " AND text REGEXP :keywordsIN ";
		}

		if (!StringUtils.isEmpty(period)) {
			queryStr += " and created_time>= :today31 and created_time <= :today ";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);

		if (!StringUtils.isEmpty(keywordsIN)) {
			nativeQuery.setParameter("keywordsIN", keywordsIN);
		}

		if (!StringUtils.isEmpty(period)) {
			Date today = new Date();

			Calendar cal = Calendar.getInstance();
			
			cal.add(Calendar.MONTH, -1);
			Date today31 = cal.getTime();

			cal.add(Calendar.MONTH, -3);
			Date quartly = cal.getTime();

//			cal.set(Calendar.DAY_OF_MONTH, 1);
//			cal.set(Calendar.MONTH, Calendar.JANUARY);
//				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			cal.set(Calendar.DAY_OF_YEAR, -1);

			Date yearly = cal.getTime();

			if (period.equalsIgnoreCase("Monthly")) {
				nativeQuery.setParameter("today31", today31);
			}

			if (period.equalsIgnoreCase("Quarterly")) {
				nativeQuery.setParameter("today31", quartly);
			}

			if (period.equalsIgnoreCase("Annually")) {
				nativeQuery.setParameter("today31", yearly);
			}

			nativeQuery.setParameter("today", today);
		}

		return nativeQuery;
	}

	@Override
	public Integer getTotalFBFeedCount(Long userId, String keywordsFB, String period, String feedType) {
		Query nativeQuery = buildFBQuery(userId, keywordsFB, period, feedType);
		if (feedType.equalsIgnoreCase("like") || feedType.equalsIgnoreCase("comment")
				|| feedType.equalsIgnoreCase("videoLike") || feedType.equalsIgnoreCase("videoComment")) {
			return ((BigDecimal) nativeQuery.getSingleResult()).intValue();
		}
		return ((BigInteger) nativeQuery.getSingleResult()).intValue();
	}

	private Query buildFBQuery(Long userId, String keywordsFB, String period, String feedType) {

		String queryStr = "select ";

		if (feedType.equalsIgnoreCase("feed")) {
			queryStr += " IFNULL(count(*),0) as rec_total ";
		}

		if (feedType.equalsIgnoreCase("like")) {
			queryStr += " IFNULL(sum(likes_count),0) as rec_total ";
		}

		if (feedType.equalsIgnoreCase("comment")) {
			queryStr += " IFNULL(sum(comment_count),0) as rec_total ";
		}

		if (feedType.equalsIgnoreCase("videoFeed")) {
			queryStr += " IFNULL(count(*),0) as rec_total ";
		}

		if (feedType.equalsIgnoreCase("videoLike")) {
			queryStr += " IFNULL(sum(likes_count),0) as rec_total ";
		}

		if (feedType.equalsIgnoreCase("videoComment")) {
			queryStr += " IFNULL(sum(comment_count),0) as rec_total ";
		}

		queryStr += "from user_feed where user_id= :userId ";

		if (feedType.equalsIgnoreCase("feed") || feedType.equalsIgnoreCase("like")
				|| feedType.equalsIgnoreCase("comment")) {
			queryStr += " AND feed_type != 'video'  ";
		}

		if (feedType.equalsIgnoreCase("videoFeed") || feedType.equalsIgnoreCase("videoLike")
				|| feedType.equalsIgnoreCase("videoComment")) {
			queryStr += " AND feed_type='video'  ";
		}

		if (!StringUtils.isEmpty(keywordsFB)) {
			queryStr += " AND story REGEXP :keywordsFB ";
		}

		if (!StringUtils.isEmpty(period)) {
			queryStr += " AND created_time>= :today31 and created_time <= :today ";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.setParameter("userId", userId);

		if (!StringUtils.isEmpty(keywordsFB)) {
			nativeQuery.setParameter("keywordsFB", keywordsFB);
		}

		if (!StringUtils.isEmpty(period)) {
			Date today = new Date();

//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DATE, -31);
//			Date today31 = cal.getTime();
//
//			cal.add(Calendar.DATE, -90);
//			Date quartly = cal.getTime();
//
////			cal.set(Calendar.DAY_OF_MONTH, 1);
////			cal.set(Calendar.MONTH, Calendar.JANUARY);
////				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
//			cal.add(Calendar.DATE, -365);

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date today31 = cal.getTime();

			cal.add(Calendar.MONTH, -3);
			Date quartly = cal.getTime();

//			cal.set(Calendar.DAY_OF_MONTH, 1);
//			cal.set(Calendar.MONTH, Calendar.JANUARY);
//				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			cal.set(Calendar.DAY_OF_YEAR, -1);

				Date yearly = cal.getTime();

			if (period.equalsIgnoreCase("Monthly")) {
				nativeQuery.setParameter("today31", today31);
			}

			if (period.equalsIgnoreCase("Quarterly")) {
				nativeQuery.setParameter("today31", quartly);
			}

			if (period.equalsIgnoreCase("Annually")) {
				nativeQuery.setParameter("today31", yearly);
			}

			nativeQuery.setParameter("today", today);
		}

		return nativeQuery;
	}

	

}
