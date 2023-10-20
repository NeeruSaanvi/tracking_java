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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
import com.tracker.services.utils.UserUtils;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	
	public List<User> findAllUsersByCompany(String name, String email, PaginationRequestDTO dto, Boolean count) {

		String queryStr = "Select DISTINCT u.*, "
				+ "				 (Select IFNULL(count(social_type),0) from user_media where userid=u.userid and social_type='facebook') AS FB, "
				+ "				 (Select IFNULL(count(social_type),0) from user_media where userid=u.user"
				+ ""
				+ ""
				+ "id and social_type='twitter') AS twt, "
				+ "				 (Select IFNULL(count(social_type),0) from user_media where userid=u.userid and social_type='instagram') AS Insta,"
				+ " cr.staffRatings,cr.activation_reminder_date  " + "				 from users u "
				+ "				 LEFT JOIN company_ref cr ON cr.userid=u.userid "
				+ "				 WHERE u.status='Active' and cr.companyID=" + UserUtils.getLoggedInUserId();

		// queryStr = queryStr + " and u.status != 'Rejected' ";
		queryStr = queryStr
				+ " AND (u.userid IN (Select userid from user_media um2) OR u.userid NOT IN (Select userid from user_media um2)) ";

		if (StringUtils.isNotBlank(name)) {
			queryStr = queryStr + " and u.FirstLastname like '%" + name + "%'";
		}

		if (StringUtils.isNotBlank(email)) {
			queryStr = queryStr + " and u.email like '%" + email + "%'";
		}

		if (dto != null && StringUtils.isNoneBlank(dto.getSort())) {
			if (!dto.getSort().equalsIgnoreCase("userTeamName")) {
				queryStr = queryStr + " order by " + dto.getSort() + " " + dto.getDirection();
			}
		}

		Query nativeQuery = em.createNativeQuery(queryStr, User.class);
		if (!count) {
			PageRequest pr = dto.getRequest();
			nativeQuery.setFirstResult(dto.getPage());
			nativeQuery.setMaxResults(pr.getPageSize());
		}

		@SuppressWarnings("unchecked")
		List<User> userList = nativeQuery.getResultList();

		return userList;
	}

	@SuppressWarnings("unchecked")
	public String getProfilePicMS(Long userid, String mediasource) {

		String queryStr = "Select image, fullname from user_media where userid=" + userid + " and social_type='"
				+ mediasource + "'";
		Query nativeQuery = em.createNativeQuery(queryStr);
		String pic = "";
		List<Object[]> objectList = nativeQuery.getResultList();
		for (Object[] obj : objectList) {
			if (obj != null && obj[0] != null) {

				String imageUrl = obj[0].toString();

				if (StringUtils.isNotBlank(imageUrl)) {
					pic = imageUrl;
					break;
				}
			}
		}
		return pic;
	}

	
	@SuppressWarnings("unchecked")
	public String getAccessToken(Long userid, String mediasource) {

		String queryStr = "Select access_token from user_media where userid=" + userid + " and social_type='"
				+ mediasource + "'";
		Query nativeQuery = em.createNativeQuery(queryStr);
		String access_token = "";
		List<Object[]> objectList = nativeQuery.getResultList();
		for (Object[] obj : objectList) {
			if (obj != null && obj[0] != null) {

				String access_token1 = obj[0].toString();

				if (StringUtils.isNotBlank(access_token1)) {
					access_token = access_token1;
					break;
				}
			}
		}
		return access_token;
	}
	
	@SuppressWarnings("unchecked")
	public void saveAccessToken(Long userid, String mediasource,String access_token) {
		
		
//		 
		
		String queryStr = "INSERT INTO user_media ( " + "access_token, "
				+ "				social_type " + "	) " + "		VALUES "
				+ "			( " + "				" + access_token + ", " + "				"
				+ mediasource + ") where userid=" + userid ;


		
		Query nativeQuery = em.createNativeQuery(queryStr);
		
		nativeQuery.executeUpdate();
		
	}
	@SuppressWarnings("unchecked")
	public String getSports(Long userid) {

		String queryStr = "SELECT sports, '' as test FROM users WHERE userid=" + userid;
		Query nativeQuery = em.createNativeQuery(queryStr);
		String sports = "";
		List<Object[]> objectList = nativeQuery.getResultList();

		for (Object[] obj : objectList) {
			if (obj != null && obj[0] != null) {
				sports = obj[0].toString();
			}
		}
		return sports;
	}

	public void logUserPwdTrack(Long userId, String email, String fromPage, String password, LocalDate date) {

	}

	@Override
	public List<UserMedia> fetchUserMedia() {

		/*
		 * String queryStr = "SELECT DISTINCT userid, username  " +
		 * " FROM user_media um " + " WHERE social_type='instagram' " +
		 * " and username is not NULL " + " and userid > 0";
		 */

		String queryStr = "SELECT DISTINCT um.userid, um.username FROM user_media um  "
				+ "					 LEFT JOIN users u ON u.userid=um.userid "
				+ "					 WHERE u.memtype='Staff' AND u.status='Active' AND um.social_type='instagram'  "
				+ "					AND	um.isScheduled = 1 "
				+ "					 GROUP BY um.social_id ORDER BY u.userid ";

		Query nativeQuery = em.createNativeQuery(queryStr);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<UserMedia> tList = new ArrayList<UserMedia>();

		for (Object[] obj : list) {

			UserMedia yt = new UserMedia();
			yt.setUserid(Long.parseLong(obj[0].toString()));
			yt.setUsername(obj[1].toString());

			tList.add(yt);
		}

		return tList;
	}

	@Override
	public List<UserMedia> fetchInstagramUser() {

		String queryStr = "select userid, username1 " + " from user_instagram_usernames " + " UNION "
				+ " (select userid, username2 " + " from user_instagram_usernames "
				+ " where username2 is not NULL and username2 != '') " + " UNION " + " (select userid, username3 "
				+ " from user_instagram_usernames " + " where username3 is not NULL and username3 != '') ";

		Query nativeQuery = em.createNativeQuery(queryStr);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<UserMedia> tList = new ArrayList<UserMedia>();

		for (Object[] obj : list) {

			UserMedia yt = new UserMedia();
			yt.setUserid(Long.parseLong(obj[0].toString()));
			yt.setUsername(obj[1].toString());

			tList.add(yt);
		}

		return tList;
	}

	@Override
	public List<UserMedia> fetchYoutubeUserMedia() {

		String queryStr = "SELECT DISTINCT um.userid, um.username FROM user_media um  "
				+ "					 LEFT JOIN users u ON u.userid=um.userid "
				+ "					 WHERE u.memtype='Staff' AND u.status='Active' AND um.social_type='youtube'  "
				+ "					AND	um.isScheduled = 1 and username != '' "
				+ "					 GROUP BY um.social_id ORDER BY u.userid ";

		Query nativeQuery = em.createNativeQuery(queryStr);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<UserMedia> tList = new ArrayList<UserMedia>();

		for (Object[] obj : list) {

			UserMedia yt = new UserMedia();
			yt.setUserid(Long.parseLong(obj[0].toString()));
			yt.setUsername(obj[1].toString());

			tList.add(yt);
		}

		return tList;
	}
	@Override
	public List<UserTeam> fetchTeamByUserId(Long userId) {

		String queryStr = "select utm.teamMappingId, utm.teamId, ut.teamName " + " from user_team_mapping utm "
				+ " LEFT JOIN user_team ut on ut.teamId = utm.teamId " + " where utm.userId =:userId";

		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("userId", userId);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<UserTeam> tList = new ArrayList<UserTeam>();

		for (Object[] obj : list) {

			UserTeam yt = new UserTeam();
			yt.setTeamMappingId(Long.parseLong(obj[0].toString()));
			yt.setTeamId(Long.parseLong(obj[1].toString()));
			yt.setTeamName(obj[2].toString());

			tList.add(yt);
		}

		return tList;
	}


	@Override
	public List<UserTeam> fetchTeamByUserId(Long userId,List<Long> teamIds) {

		String queryStr = "select utm.teamMappingId, utm.teamId, ut.teamName " + " from user_team_mapping utm "
				+ " LEFT JOIN user_team ut on ut.teamId = utm.teamId " + " where utm.userId =:userId";

		if(teamIds.size() > 0) {
			queryStr += " AND utm.teamId in (:teamIds)";
		}
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("userId", userId);

		
		if(teamIds.size() > 0) {
			nativeQuery.setParameter("teamIds", teamIds);
		}
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<UserTeam> tList = new ArrayList<UserTeam>();

		for (Object[] obj : list) {

			UserTeam yt = new UserTeam();
			yt.setTeamMappingId(Long.parseLong(obj[0].toString()));
			yt.setTeamId(Long.parseLong(obj[1].toString()));
			yt.setTeamName(obj[2].toString());

			tList.add(yt);
		}

		return tList;
	}

	
	@Override
	public List<Long> fetchSponserTeamByUserId(Long userId) {

		String queryStr = "select utm.teamMappingId, utm.teamId, ut.teamName " + " from user_team_mapping utm "
				+ " LEFT JOIN user_team ut on ut.teamId = utm.teamId " + " where ut.createdBy =:userId";

		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("userId", userId);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<Long> tList = new ArrayList<Long>();

		for (Object[] obj : list) {

			UserTeam yt = new UserTeam();
			//yt.setTeamMappingId(Long.parseLong(obj[0].toString()));
			yt.setTeamId(Long.parseLong(obj[1].toString()));
			//yt.setTeamName(obj[2].toString());

			tList.add(Long.parseLong(obj[1].toString()));
		}

		return tList;
	}

	@Override
	public List<CompanyRef> getAnnualRankingListByCompnay(Long companyID, Long userId) {

		String queryStr = "SELECT DISTINCT cr.userid,cr.prostaffName,cr.rank,cr.rank_score "
				+ "					FROM company_ref cr "
				+ "					WHERE cr.companyID=:companyID AND cr.rank>0 ";
		if (userId > 0) {
			queryStr += "AND cr.userId=:userId";
		}
		queryStr += "					AND cr.rank_score>0  ORDER BY cr.rank asc";

		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);
		if (userId > 0) {
			nativeQuery.setParameter("userId", userId);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<CompanyRef> tList = new ArrayList<CompanyRef>();

		for (Object[] obj : list) {

			CompanyRef yt = new CompanyRef();
			yt.setUserid(Long.parseLong(obj[0].toString()));
			yt.setProstaffName(obj[1].toString());
			yt.setRank(Integer.parseInt(obj[2].toString()));
			yt.setRankScore(Integer.parseInt(obj[3].toString()));

			tList.add(yt);
		}

		return tList;

	}

	@Override
	public List<UserFeed> fetchFBData(Long companyId, Long userId, String keywords) {

		String queryStr = "SELECT DISTINCT u.userid, uf.id,uf.photo_url,uf.likes_count,uf.link,uf.story,uf.comment_count,"
				+ "		uf.share_count,uf.view_count,uf.created_time " + "		FROM user_feed uf"
				+ "		LEFT JOIN users u ON u.userid = uf.user_id "
				+ "		LEFT JOIN company_ref cr ON cr.userid=uf.user_id" + "		WHERE cr.companyID=:companyID ";

		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND uf.story REGEXP :keywords ";
		}
		queryStr += " AND u.userid=:userId	ORDER BY uf.created_time desc";

		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyId);
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}
		nativeQuery.setParameter("userId", userId);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<UserFeed> tList = new ArrayList<UserFeed>();

		for (Object[] obj : list) {

			UserFeed uf = new UserFeed();
			uf.setUser_id(Long.parseLong(obj[0].toString()));
			uf.setId(obj[1] == null ? "" : obj[1].toString());
			uf.setPhoto_url(obj[2] == null ? "" : obj[2].toString());
			uf.setLikes_count(obj[3] == null ? 0 : Integer.parseInt(obj[3].toString()));
			uf.setLink(obj[4] == null ? "" : obj[4].toString());
			uf.setStory(obj[5] == null ? "" : obj[5].toString());
			uf.setComment_count(obj[6] == null ? 0 : Integer.parseInt(obj[6].toString()));
			uf.setShare_count(obj[7] == null ? 0 : Integer.parseInt(obj[7].toString()));
			uf.setView_count(obj[8] == null ? 0 : Integer.parseInt(obj[8].toString()));

			String date = obj[9] == null ? "" : obj[9].toString();
			if (StringUtils.isNotBlank(date)) {
				LocalDate localDate = LocalDate.parse(date);
				uf.setCreated_time(localDate);
				String createdTimeFormatter = localDate.format(DateTimeFormatter.ofPattern("dd, MMM yyyy"));
				uf.setCreatedTimeFormatter(createdTimeFormatter);
			}
			tList.add(uf);
		}

		return tList;
	}

	@Override
	public List<UserTweets> fetchTWData(Long companyId, Long userId, String keywords) {

		String queryStr = "SELECT ut.user_tweets_id, ut.user_id, ut.id, ut.text, ut.link, ut.created_at, "
				+ " ut.retweet_count, ut.favorite_count, ut.download_date FROM user_tweets ut "
				+ "	LEFT JOIN company_ref cr ON cr.userid = ut.user_id "
				+ "	LEFT JOIN users u ON u.userid = ut.user_id " + "	WHERE cr.companyID=:companyID ";

		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND ut.text REGEXP :keywords ";
		}
		queryStr += " AND u.userid=:userId	ORDER BY ut.created_at desc";

		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyId);
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}
		nativeQuery.setParameter("userId", userId);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<UserTweets> tList = new ArrayList<UserTweets>();

		for (Object[] obj : list) {
			UserTweets ut = new UserTweets();
			ut.setUser_tweets_id(Long.parseLong(obj[0].toString()));
			ut.setUser_id(Long.parseLong(obj[1].toString()));
			ut.setId(obj[2] == null ? "" : obj[2].toString());
			ut.setText(obj[3] == null ? "" : obj[3].toString());
			ut.setLink(obj[4] == null ? "" : obj[4].toString());
			ut.setCreated_at(obj[5] == null ? "" : obj[5].toString());
			ut.setRetweet_count(obj[6] == null ? 0 : Integer.parseInt(obj[6].toString()));
			ut.setFavorite_count(obj[7] == null ? 0 : Integer.parseInt(obj[7].toString()));

			String date = obj[5] == null ? "" : obj[5].toString();
			String dateStr[] = date.split(" ");
			LocalDate localDate = LocalDate.parse(dateStr[0]);
			String createdAtFormatter = localDate.format(DateTimeFormatter.ofPattern("dd, MMM yyyy"));
			ut.setCreatedAtFormatter(createdAtFormatter);

			tList.add(ut);
		}
		return tList;
	}

	@Override
	public List<UserInstragramFeed> fetchInstaData(Long companyId, Long userId, String keywords) {

		String queryStr = "SELECT uif.instagram_feed_id, uif.user_id, uif.id, uif.text, uif.link,"
				+ " uif.likes, uif.comments_count, uif.likes_count, uif.thumbnail_image, uif.standard_image, uif.created_time"
				+ " FROM user_instagram_feed uif " + "		LEFT JOIN company_ref cr ON cr.userid = uif.user_id "
				+ "		LEFT JOIN users u ON u.userid = uif.user_id" + "		WHERE cr.companyID=:companyID ";

		if (!StringUtils.isEmpty(keywords)) {
			queryStr += " AND uif.text REGEXP :keywords ";
		}
		queryStr += " AND u.userid=:userId	GROUP BY uif.id ORDER BY uif.created_time desc";

		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyId);
		if (!StringUtils.isEmpty(keywords)) {
			nativeQuery.setParameter("keywords", keywords);
		}
		nativeQuery.setParameter("userId", userId);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<UserInstragramFeed> tList = new ArrayList<UserInstragramFeed>();

		for (Object[] obj : list) {
			UserInstragramFeed uif = new UserInstragramFeed();
			uif.setInstagramFeedId(Long.parseLong(obj[0].toString()));
			uif.setUserId(Long.parseLong(obj[1].toString()));
			uif.setId(obj[2] == null ? "" : obj[2].toString());
			uif.setText(obj[3] == null ? "" : obj[3].toString());
			uif.setLink(obj[4] == null ? "" : obj[4].toString());
			uif.setLikes(obj[5] == null ? 0 : Integer.parseInt(obj[5].toString()));
			uif.setCommentsCount(obj[6] == null ? 0 : Integer.parseInt(obj[6].toString()));
			uif.setLikesCount(obj[7] == null ? 0 : Integer.parseInt(obj[7].toString()));
			uif.setThumbnailImage(obj[8] == null ? "" : obj[8].toString());
			uif.setStandardImage(obj[9] == null ? "" : obj[9].toString());

			uif.setCreatedTime(obj[10] == null ? "" : obj[10].toString());
			try {
				long date = obj[10] == null ? 0l : Long.parseLong(obj[10].toString());
				LocalDate localDate = new java.sql.Date(date * 1000).toLocalDate();

//			
//			String date = obj[10] == null ? "" : obj[10].toString();
//			String dateStr[] = date.split(" ");
//			LocalDate localDate = LocalDate.parse(dateStr[0]);
				// String createdAtFormatter = localDate.format(DateTimeFormatter.ofPattern("dd,
				// MMM yyyy"));
				String createdTimeFormatter = localDate.format(DateTimeFormatter.ofPattern("dd, MMM yyyy"));
				uif.setCreatedTimeFormatter(createdTimeFormatter);
			} catch (Exception e) {
				e.printStackTrace();
//				try {
//					String date = obj[10] == null ? "" : obj[10].toString();
//					String dateStr[] = date.split(" ");
//					LocalDate localDate = LocalDate.parse(dateStr[0]);
//					//String createdAtFormatter = localDate.format(DateTimeFormatter.ofPattern("dd, MMM yyyy"));
//					String createdTimeFormatter = localDate.format(DateTimeFormatter.ofPattern("dd, MMM yyyy"));
//					uif.setCreatedTimeFormatter(createdTimeFormatter);
//				}catch(Exception e1) {
//					e1.printStackTrace();
//				}
			}

			tList.add(uif);
		}

		return tList;

	}

	@Override
	public List<User> getApplicationList(String profile, Long companyID, PaginationRequestDTO dto) {

		Query nativeQuery = buildQuery(profile, companyID, dto, false);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<User> tList = new ArrayList<User>();

		for (Object[] obj : list) {

			User yt = new User();
			yt.setUserId(Long.parseLong(obj[0].toString()));
			yt.setFirstLastName(obj[1].toString());
			yt.setPhone(obj[2].toString());
			yt.setEmail(obj[3].toString());
			yt.setStatus(obj[4].toString());

			String date = obj[5].toString();
			String dateStr[] = date.split(" ");
			LocalDate localDate = LocalDate.parse(dateStr[0]);
			yt.setRegistrationDate(localDate);

			tList.add(yt);

		}

		return tList;

	}

	@Override
	public Integer getApplicationListCount(String profile, Long companyID, PaginationRequestDTO dto) {
		Query nativeQuery = buildQuery(profile, companyID, dto, true);
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		Integer count = 0;
		if (list != null) {
			count = list.size();
		}
		return count;
	}

	private Query buildQuery(String profile, Long companyID, PaginationRequestDTO dto, Boolean count) {

		String queryStr = "SELECT u.userid, u.FirstLastname, u.phone, u.email, uad.status, u.registration_date FROM users u "
				+ "			LEFT JOIN user_pro_app_details uad ON uad.userid=u.userid "
				+ "			WHERE (uad.status='Pending' OR uad.status='Rejected') ";
		if (!profile.equalsIgnoreCase("sports")) {
			queryStr += "AND application_type = 'ProApplication'";
		}
		queryStr += " AND uad.companyID= :companyID";

		if (profile.equalsIgnoreCase("sports")) {
			queryStr += " ORDER BY uad.status asc";
		} else {
			queryStr += " ORDER BY uad.registration_date desc";
		}

		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyID);

		/*
		 * if(!count) { nativeQuery.setFirstResult((dto.getPage() - 1) * dto.getSize());
		 * nativeQuery.setMaxResults(dto.getSize()); }
		 */

		return nativeQuery;
	}

	@Override
	public UserSponsorScores fetchScoreSettings(Long companyId) {

		String queryStr = "select * from user_sponsor_scores where companyID=:companyID";

		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("companyID", companyId);

		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();

		List<UserSponsorScores> tList = new ArrayList<UserSponsorScores>();

		for (Object[] obj : list) {

			UserSponsorScores yt = new UserSponsorScores();
			yt.setId(Long.parseLong(obj[0].toString()));
			yt.setCompanyID(Long.parseLong(obj[1].toString()));
			yt.setFbpost(Integer.parseInt(obj[2].toString()));
			yt.setFblike(Integer.parseInt(obj[3].toString()));
			yt.setFbcomments(Integer.parseInt(obj[4].toString()));
			yt.setFbshares(Integer.parseInt(obj[5].toString()));

			yt.setTwtretweets(Integer.parseInt(obj[6].toString()));
			yt.setTwtretweets(Integer.parseInt(obj[7].toString()));
			yt.setTwtfavourites(Integer.parseInt(obj[8].toString()));

			yt.setInstaposts(Integer.parseInt(obj[9].toString()));
			yt.setInstalikes(Integer.parseInt(obj[10].toString()));
			yt.setInstacomments(Integer.parseInt(obj[11].toString()));

			yt.setYtposts(Integer.parseInt(obj[12].toString()));
			yt.setYtlikes(Integer.parseInt(obj[13].toString()));
			yt.setYtviews(Integer.parseInt(obj[14].toString()));

			yt.setLeadsubmt(Integer.parseInt(obj[15].toString()));
			yt.setEventworked(Integer.parseInt(obj[16].toString()));
			yt.setTournaments(Integer.parseInt(obj[17].toString()));
			yt.setWebhits(Integer.parseInt(obj[18].toString()));
			yt.setPrints(Integer.parseInt(obj[19].toString()));

			tList.add(yt);
		}

		if (tList != null && tList.size() > 0) {
			return tList.get(0);
		} else {
			return new UserSponsorScores();
		}

	}

	@Override
	public void dashboardMostusedKeyword(DashboardMostusedKeywordYearly dashboardResponse) {

		String queryStr = "INSERT INTO dashboard_mostused_keyword_ytd ( " + "				companyId, "
				+ "				keyword, " + "				fbCount, " + "				inCount, "
				+ "				twCount, " + "				ytCount " + "			) " + "		VALUES "
				+ "			( " + "				" + dashboardResponse.getCompanyId() + ", " + "				"
				+ dashboardResponse.getKeyword() + ", " + "				" + dashboardResponse.getFbCount() + ", "
				+ "				" + dashboardResponse.getInCount() + ", " + "				"
				+ dashboardResponse.getTwCount() + ", " + "				" + dashboardResponse.getYtCount() + ", "
				+ "			) ON DUPLICATE KEY UPDATE fbCount = " + dashboardResponse.getFbCount() + ", inCount = "
				+ dashboardResponse.getInCount() + ", " + "twCount = " + dashboardResponse.getTwCount() + ", ytCount = "
				+ dashboardResponse.getYtCount();

		Query nativeQuery = em.createNativeQuery(queryStr);

		nativeQuery.executeUpdate();

	}

}
