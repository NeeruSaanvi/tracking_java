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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Comparator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.UserImage;
import com.tracker.commons.models.UserInstragramFeed;
import com.tracker.commons.models.UserTeam;
import com.tracker.commons.models.UserYoutubeFeed;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.utils.UserUtils;

import lombok.val;

@Repository
@Transactional
public class GalleryRepositoryImpl implements GalleryRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<UserYoutubeFeed> getYoutubeVideo(Long companyID, String keyword, List<Long> userList) {

		String queryStr = "SELECT uyf.id, uyf.video_url, uyf.total_likes,uyf.total_views, uyf.thumb,uyf.text, uyf.created_date as createdTime "
				+ " FROM user_youtubefeeds uyf " + 
				"								LEFT JOIN company_ref cr ON cr.userid = uyf.user_id " + 
				"			    			LEFT JOIN users u ON u.userid = uyf.user_id " + 
				"								LEFT JOIN user_media um ON um.userid=uyf.user_id " + 
				"								WHERE um.social_type='youtube' "+				
				" and cr.companyID =:companyID ";
		
		if (!StringUtils.isEmpty(keyword)) {
			queryStr += " AND uyf.text REGEXP :keyword ";
		}
		
		if(userList.size() > 0) {
			queryStr += " AND u.userid in (:userId)";
		}
		
		queryStr += "group by uyf.video_id ORDER BY uyf.created_date desc";

		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		
		if (!StringUtils.isEmpty(keyword)) {
			nativeQuery.setParameter("keyword", keyword);
		}
		
		if(userList.size() > 0) {
			nativeQuery.setParameter("userId", userList);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<UserYoutubeFeed> videoList = new ArrayList<UserYoutubeFeed>();
		
		for (Object[] obj : list) {
			
			UserYoutubeFeed yt = new UserYoutubeFeed(); 
			yt.setId(Long.parseLong(obj[0].toString()));
			yt.setVideo_url(obj[1].toString());
			yt.setTotal_likes(Integer.parseInt(obj[2].toString()));
			yt.setTotal_views(Integer.parseInt(obj[3].toString()));
			yt.setThumb(obj[4].toString());
			yt.setText(obj[5].toString());
			videoList.add(yt);
			
		}

		return videoList;
	}
	
	@Override
	public List<UserImage> getHighResImages(Long companyID){
		
		String queryStr = "select ui.id,ui.userId, ui.fileName, ui.text, ui.created_time as createdTime, u.FirstLastname as firstLastName FROM " + 
				"user_image ui " + 
				"LEFT JOIN company_ref cr ON cr.userid = ui.userId  " + 
				"LEFT JOIN users u ON u.userid = ui.userId  " + 
				"where cr.companyID = :companyID";
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<UserImage> imageList = new ArrayList<UserImage>();
		
		for (Object[] obj : list) {
			
			UserImage insta = new UserImage(); 
			insta.setId(Long.parseLong(obj[0].toString()));
			insta.setUserId(Long.parseLong(obj[1].toString()));
			insta.setFileName(obj[2].toString());
			insta.setText(obj[3] == null ? "" : obj[3].toString());
			
			String date = obj[4].toString();
	        LocalDate localDate = LocalDate.parse(date);
			insta.setCreatedTime(localDate);
			
			insta.setFirstLastName(obj[5].toString());
			imageList.add(insta);
			
		}

		return imageList;
	}
	
	
	public List<Object[]> buildBasicQuery( String staff,
		Long loggedInUserId) {
		
		String queryStr = "SELECT DISTINCT u.userid,u.FirstLastname, 0 as teamId, '' as teamName " + 
				" from users u 			 " + 
				" LEFT JOIN company_ref cr ON cr.userid=u.userid" + 
				"			WHERE cr.ref_status='Active' and u.memtype='Staff' and cr.companyID=:companyID";
		
//		if(StringUtils.isNotBlank(staff)) {
//			queryStr += " AND u.userid IN (:staff)";
//		}
//		
		Query nativeQuery = em.createNativeQuery(queryStr);
		
		
		if(loggedInUserId == null) {
			nativeQuery.setParameter("companyID", UserUtils.getLoggedInUserId());
		}else {
			nativeQuery.setParameter("companyID", loggedInUserId);
		}
		
//		if(StringUtils.isNotBlank(staff)) {
//			String[] strArray = staff.split(",");
//			List<Long> lst = new ArrayList<Long>();
//			for(String str : strArray) {
//				lst.add(Long.parseLong(str));
//			}
//			nativeQuery.setParameter("staff", lst);
//		}
		
		
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
	
	@Override
	public List<UserInstragramFeed> getInstagramImages(Long companyID, String keyword, List<Long> userList, Date fromDate, Date toDate) {

		List<Object[]> objectList = buildBasicQuery("", companyID);
		List<Long> userIds = new ArrayList<Long>();
		
		for(Object[] obj: objectList) {
			val userId = (Integer)obj[0];
			userIds.add(Long.valueOf(userId));
		}

		String queryStr = "SELECT DISTINCT  uif.link, uif.id,uif.text,uif.likes_count as likesCount, uif.standard_image as standardImage, "+
				"uif.comments_count as commentsCount, u.userid,u.state, u.FirstLastname as firstLastname, uif.created_time as createdTime " + 
				"FROM user_instagram_feed uif " + 
				
				"LEFT JOIN users u ON u.userid = uif.user_id " + 
				
				"WHERE " ;
			;
		if(userIds != null && userIds.size() > 0) {
			queryStr += "  uif.user_id IN (:userIds) ";
		}
		
		if (!StringUtils.isEmpty(keyword)) {
			queryStr += " AND uif.TEXT REGEXP :keywords ";
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
		
	queryStr += " and uif.created_time >= :fromDate and uif.created_time <= :toDate ";
		
		queryStr += "ORDER BY uif.created_time desc";

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
		nativeQuery.setParameter("fromDate", fromDate.getTime()/1000 );
		nativeQuery.setParameter("toDate",toDate.getTime()/1000);
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
		System.out.println("userId:  "+userList.size());
		

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
			

			imageList.add(insta);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
  	imageList.addAll(getFacebookImagesList( companyID,  keyword, userList,  fromDate,  toDate ));

   	
   	Collections.sort(imageList, new Comparator<UserInstragramFeed>() {
   	  public int compare(UserInstragramFeed o1, UserInstragramFeed o2) {
   	      if (o1.getCreatedTime() == null || o2.getCreatedTime() == null)
   	        return 0;
   	      return o1.getCreatedTime().compareTo(o2.getCreatedTime());
   	  }
   	});
   	Collections.reverse(imageList);
		return imageList;
	}
	

	public List<UserInstragramFeed> getFacebookImagesList(Long companyID, String keyword, List<Long> userList, Date fromDate, Date toDate) {

		val keywordsFB = UserUtils.getLoggedInUser().getFbKeywords();
		if(keywordsFB != null) {

		 keyword = String.join("|", Arrays.asList( keywordsFB.split(",")));
		}

		List<Object[]> objectList = buildBasicQuery("", companyID);
		List<Long> userIds = new ArrayList<Long>();
		
		for(Object[] obj: objectList) {
			val userId = (Integer)obj[0];
			userIds.add(Long.valueOf(userId));
		}
		


		String queryStr = "SELECT DISTINCT  uif.link, uif.id,uif.story,uif.likes_count as likesCount, uif.photo_url as standardImage, "+
				"uif.comment_count as commentsCount, u.userid,u.state, u.FirstLastname as firstLastname, uif.created_time as createdTime " + 
				"FROM user_feed uif " + 
				
				"LEFT JOIN users u ON u.userid = uif.user_id " + 
				
				"WHERE " ;
			;
		if(userIds != null && userIds.size() > 0) {
			queryStr += "  uif.user_id IN (:userIds) ";
		}
		
		if (!StringUtils.isEmpty(keyword)) {
			queryStr += " AND uif.story REGEXP :keywords ";
		}		
		

		
	queryStr += " and uif.created_time >= :fromDate and uif.created_time <= :toDate ";
		
		queryStr += "ORDER BY uif.created_time desc";

		Query nativeQuery = em.createNativeQuery(queryStr);
	//nativeQuery.setParameter("companyID", companyID);
		if (!StringUtils.isEmpty(keyword)) {

	nativeQuery.setParameter("keywords", keyword);
		}
try {
		String stringFromDate= new SimpleDateFormat("yyyy-MM-dd").format(fromDate);
		String stringToDate= new SimpleDateFormat("yyyy-MM-dd").format(toDate);
		nativeQuery.setParameter("fromDate", stringFromDate );
		nativeQuery.setParameter("toDate",stringToDate);
}catch(Exception e) {
	e.printStackTrace();
}
//		nativeQuery.setParameter("fromDate", fromDate.getTime()/1000 );
//		nativeQuery.setParameter("toDate",toDate.getTime()/1000);
		if(userIds != null && userIds.size() > 0) {
			nativeQuery.setParameter("userIds", userIds);
		}	
		
	
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<UserInstragramFeed> imageList = new ArrayList<UserInstragramFeed>();
		System.out.println(queryStr);
		
		System.out.println("companyID:  "+companyID);
		System.out.println(" keyword:  "+keyword);
		System.out.println("userId:  "+userList.size());
		

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

			insta.setSocial_type("facebook");
			

			imageList.add(insta);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
  
		return imageList;
	}
	public List<UserInstragramFeed> getFacebookImages(Long companyID, String keyword, List<Long> userList, Date fromDate, Date toDate) {


		String queryStr = "SELECT DISTINCT  uif.link, uif.id,uif.story,uif.likes_count as likesCount, uif.photo_url as standardImage, "+
				"uif.comment_count as commentsCount, u.userid,u.state, u.FirstLastname as firstLastname, uif.created_time as createdTime,um.social_type  " + 
				"FROM user_feed uif " + 
				"LEFT JOIN company_ref cr ON cr.userid = uif.user_id " + 
				"LEFT JOIN users u ON u.userid = uif.user_id " + 
				"LEFT JOIN user_media um ON um.userid=uif.user_id " + 
				"WHERE  um.social_type='facebook' " + 
				"and cr.companyID =:companyID";

		String[] keywordList = keyword.split("\\|");
		
		if(keywordList.length >0) {
			queryStr += " AND (";
		}
		
		for(int index = 0;index<keywordList.length;index++) {
			
			
			
			
			if(index == 0) {
				queryStr += "uif.story like" ;
				queryStr += " \"%"+keywordList[index]+"%\"";
			}else{
				queryStr += " OR uif.story like" ;
				queryStr += " \"%"+keywordList[index]+"%\"";
				//queryStr += "  uif.TEXT like + keywordList[index]";
		
				//queryStr += " OR uif.TEXT like +"\"%"+keyword[index]+"%\"";
			}
		}
		
		if(keywordList.length >0) {
			queryStr += " )";
		}
//		if(userList.size() > 0) {
//			queryStr += " AND uif.user_id in (:userId)";
//		}
		
	queryStr += " and uif.created_time >= :fromDate and uif.created_time <= :toDate ";
		
		queryStr += "ORDER BY uif.created_time desc";

		Query nativeQuery = em.createNativeQuery(queryStr);
	nativeQuery.setParameter("companyID", companyID);
		
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
//		nativeQuery.setParameter("fromDate", fromDate);
//		nativeQuery.setParameter("toDate",toDate);
		//1618790401
//
//		
	//1624856546124
		//1624856546124
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<UserInstragramFeed> imageList = new ArrayList<UserInstragramFeed>();
		System.out.println(queryStr);
		
		System.out.println("companyID:  "+companyID);
		System.out.println(" keyword:  "+keyword);
		System.out.println("userId:  "+userList.size());
		

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

			insta.setCreatedTime(obj[9] == null ? "" : obj[9].toString());
			}
//
//			String createdTimeFormatter = localDate.format(DateTimeFormatter.ofPattern("dd, MMM yyyy"));
			//Date date = new Date(obj[9].toString());
			if(obj[9]!= null) {

			insta.setCreatedTimeFormatter(obj[9] == null ? "" : obj[9].toString());
			}
			if(obj[10]!= null) {

			insta.setSocial_type(obj[10].toString());
			}

			imageList.add(insta);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
//		UserInstragramFeed insta = new UserInstragramFeed(); 
//		insta.setLink("http://www.cannabisreports.com/images/strains/no_image.png");
//		insta.setId("dfdwff");
//		insta.setText("hfjdshf");
//		insta.setLikesCount(2);
//		insta.setStandardImage("http://www.cannabisreports.com/images/strains/no_image.png");
//		insta.setCommentsCount(3);
//		insta.setFirstLastname("jh");
//		
////		long date = obj[9] == null ? 0l : Long.parseLong(obj[9].toString());
////		LocalDate localDate = new java.sql.Date(date * 1000).toLocalDate();
////		insta.setCreatedTime(obj[9] == null ? "" : obj[9].toString());
////
////		String createdTimeFormatter = localDate.format(DateTimeFormatter.ofPattern("dd, MMM yyyy"));
////		insta.setCreatedTimeFormatter(createdTimeFormatter);
//
//		imageList.add(insta);
		return imageList;
	}
	

}
