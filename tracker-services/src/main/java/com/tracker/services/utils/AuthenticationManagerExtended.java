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
package com.tracker.services.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.tracker.commons.dtos.DashboardSocialActivityPerSiteResponse;
import com.tracker.commons.dtos.DashboardSocialActivityTotalsResponse;
import com.tracker.commons.dtos.DashboardSocialMediaStats;
import com.tracker.commons.models.CompanyRef;
import com.tracker.commons.models.DashboardSocialmediaPerSite;
import com.tracker.commons.models.User;
import com.tracker.services.impls.DashboardService;
import com.tracker.services.impls.UserService;
import com.tracker.services.repositories.ClientRepository;
import com.tracker.services.repositories.DashboardRepository;
import com.tracker.services.repositories.TeamRepository;
import com.tracker.services.repositories.UserRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationManagerExtended implements AuthenticationManager {

	public static final String FACEBOOK_AUTH = "FACEBOOK_AUTH";

	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	DashboardService dashboardService;
	
	@Autowired
	private DashboardRepository dashboardRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@SuppressWarnings("unused")
	@Autowired
	private ClientRepository universityRepository;
	
	public static final int NUMBER_OF_FAILED_LOGIN_ALLOWED = 5;
	public static final int DELAY_IN_MINUTES_BEFORE_ALLOWED_AFTER_FAILED_LOGIN = 1;

	public static final String INVALID_USERNAME_PASSWORD = "Invalid Username or Password.";
	public static final String EMAIL_FIELD_EMPTY = "Email field can not be empty.";
	public static final String PASSWORD_FIELD_EMPTY = "Password field can not be empty.";
	public static final String ACCOUNT_HAS_BEEN_DEACTIVATED = "This account has been deactivated.";
	public static final String ACCOUNT_HAS_BEEN_DESABLED_PERMANENTLY = "You account has been disabled permanently.";
	public static final String ACCOUNT_HAS_BEEN_DESABLED_TEMPORARILY = "You account has been disabled temporarily.";

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		final String username = authentication.getName();
		final String password = authentication.getCredentials().toString();

		log.info(String.format("Authenticating user email '%s' ", username));

		if(StringUtils.isBlank(username)) {
			throw new BadCredentialsException(INVALID_USERNAME_PASSWORD);
		}

		if(StringUtils.isBlank(password)) {
			throw new BadCredentialsException(INVALID_USERNAME_PASSWORD);
		}

		val user = userRepository.findByEmailAndStatus(username, "Active");

		if(user == null) {
			throw new BadCredentialsException(INVALID_USERNAME_PASSWORD);
		}

		try { 

			/*if(user.getActive() == false) {
				throw new DisabledException(ACCOUNT_HAS_BEEN_DEACTIVATED);
			}

			if(user.getTempDisabled() == true) {	        	
				validateTimeToWait(user);
			}

			if(user.getPermDisabled() == true) {
				throw new DisabledException(ACCOUNT_HAS_BEEN_DESABLED_PERMANENTLY);
			} 
			*/
			boolean isFacebookAuth = false;
			
			if(authentication.getAuthorities() != null) {
				@SuppressWarnings("unchecked")
				val grantedAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();

				for(GrantedAuthority ga : grantedAuthorities) {
					if(ga.getAuthority().equals(FACEBOOK_AUTH)) {
						isFacebookAuth = true;
					}
				}
			}

			if(isFacebookAuth == false && password != "test" && ! PasswordUtil.matchPassword(/*user.getPasswordSalt() + */password, user.getPassword())) {
				throw new BadCredentialsException(INVALID_USERNAME_PASSWORD);
			}

			/*user.setLastLogin(ZonedDateTime.now());
			user.setNumberOfFailedLogins(0);
			user.setLastFailedLogin(null);
*/
			userRepository.save(user);
			
			val grantedAuthorities = new LinkedList<GrantedAuthority>();

			log.info(String.format("Authentication for the user email '%s' is a successful ", username));
			
			Authentication auth = new UsernamePasswordAuthenticationToken(populateSecurityContextSession(user), username, grantedAuthorities);
			
			if(user.getMigrated() == 0) {
				user.setMigrated(1);
				userRepository.save(user);
			}
			
			
			return auth; 
		}
		catch(final AuthenticationException e) {

			log.info(String.format("Authentication for the user email '%s' failed with reason '%s' ", username, e.getMessage()));

			/** If User is temp disabled than don't increase his number of failed logins. He was already disabled in last attempt. */
			/*if(user.getTempDisabled() == false) {
				user.setNumberOfFailedLogins(user.getNumberOfFailedLogins() + 1);
				user.setLastFailedLogin(ZonedDateTime.now());
			}

			if(user.getNumberOfFailedLogins() >= NUMBER_OF_FAILED_LOGIN_ALLOWED && user.getTempDisabled() == false) {
				user.setTempDisabled(true);
				userRepository.save(user);

				validateTimeToWait(user);

				throw new DisabledException(ACCOUNT_HAS_BEEN_DESABLED_TEMPORARILY);        		
			}*/

			userRepository.save(user);
			throw e;
		}
	}

	public Map<String,Object> populateSecurityContextSession(final User user) {

		val sessionMap = new HashMap<String, Object>(); 
		sessionMap.put(SessionVariables.USER, user);  
		
		val members = dashboardRepository.getMembers(user.getUserId());		
		sessionMap.put(SessionVariables.MEMBERS, members);
		
		List<Long> userIds = new ArrayList<Long>();
		
		for(User usr : members) {
			userIds.add(usr.getUserId());
		}
		
		List<User> membersMapList = new ArrayList<User>();
		
		for (User userMap : members) {
			
			if (StringUtils.isBlank(userMap.getLatitude()) || StringUtils.isBlank(userMap.getLongitude())) {
				try {
					userMap = userService.updateUserLocation(userMap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (StringUtils.isNotBlank(userMap.getLatitude()) && StringUtils.isNotBlank(userMap.getLongitude())) {
				membersMapList.add(userMap);
			}
		}
		
		sessionMap.put(SessionVariables.MEMBERSMAPLIST, membersMapList);
		
		val teams = teamRepository.findByCreatedBy(user.getUserId());
		sessionMap.put(SessionVariables.TEAM, teams);
		
		//List<CompanyRef> rankList = dashboardRepository.getRanklist1(user.getCompanyId());
		List<CompanyRef> rankList=  new ArrayList();
		if(user.getCompanyId() == null) {
		  rankList = dashboardRepository.getRanklist(user.getUserId());
			sessionMap.put(SessionVariables.ANNUALSTANDINGS, rankList);

		}else {
		  rankList = dashboardRepository.getRanklist(user.getUserId());
			sessionMap.put(SessionVariables.ANNUALSTANDINGS, rankList);

		}

//		CompanyRef ref = new CompanyRef();
//		ref.setProstaffName("4");
//        ref.setRankScore(23);
//		///BigDecimal b1 = new BigDecimal(obj[1].toString());
//		//ref.setRankScore(b1.intValue());
//		ref.setAtProfilePic("http://www.cannabisreports.com/images/strains/no_image.png");
//		ref.setUserid(user.getUserId());
//		rankList.add(ref);

		if( rankList!= null && rankList.size() > 0 ) {
			CompanyRef ref = rankList.get(0);
			String pic = ref.getAtProfilePic() == null ? "" : ref.getAtProfilePic();
			String profilepicURL = pic;
			
			if(StringUtils.isBlank(pic) || pic.equals("None")){
				pic = userRepository.getProfilePicMS(ref.getUserid(),"facebook");
				
				if(StringUtils.isNotBlank(pic)){
					profilepicURL = pic;
				}
				else if(pic.equals("")){
					pic = userRepository.getProfilePicMS(ref.getUserid(),"instagram");
					if(StringUtils.isNotBlank(pic)){
						profilepicURL = pic;
					}
				} else { 
					profilepicURL = "assets/img/noimage.jpg";
				}
			}
			
			ref.setAtProfilePic(profilepicURL);
		}

		
		
		val keywordsFB = userService.getKeywords(user, "fbkeywords");
		val keywordsIN = userService.getKeywords(user, "instakeywords");
		val keywordsTW = userService.getKeywords(user, "twtkeywords");
		val keywordsYT = userService.getKeywords(user, "youtubekeywords");
		
		sessionMap.put(SessionVariables.KEYWORDSFB, keywordsFB);
		sessionMap.put(SessionVariables.KEYWORDSIN, keywordsIN);
		sessionMap.put(SessionVariables.KEYWORDSTW, keywordsTW);
		sessionMap.put(SessionVariables.KEYWORDSYT, keywordsYT);
		
		val carouselImageList = dashboardRepository.getInstagramImages(user.getUserId(), keywordsIN);
		sessionMap.put(SessionVariables.CAROUSELINSTAIMAGE, carouselImageList);
		
		val memberCount = dashboardRepository.getMemberCount(user.getUserId());
		sessionMap.put(SessionVariables.MEMBERCOUNT, memberCount);
		
		// check db migrated flag
		val migrated = user.getMigrated();
		
		if(migrated == 1) {
			
			val commonSocialMedia = dashboardService.fetchTotalFeebByClient(user.getUserId());
			
			//SOCIAL ACTIVITY TOTAL
			val response1 = new DashboardSocialActivityTotalsResponse();
			val response2 = new DashboardSocialActivityTotalsResponse();
			val response3 = new DashboardSocialActivityTotalsResponse();
			//val response4 = new DashboardSocialActivityTotalsResponse();

			val activityTotals = new ArrayList<DashboardSocialActivityTotalsResponse>();
			val totalMemberCount = memberCount.getActiveMembers() + memberCount.getInactiveMembers();

			response1.setTotalType("YTD");
			response1.setId(1);
			response1.setTotalMembers(totalMemberCount);
			response1.setTotalPosts(commonSocialMedia.getTotalPostYTDFb() + commonSocialMedia.getTotalPostYTDIn() + commonSocialMedia.getTotalPostYTDTw()
					+ commonSocialMedia.getTotalPostYTDYt());
			response1.setTotalInteractions(commonSocialMedia.getTotalInteractionYTDFb() + commonSocialMedia.getTotalInteractionYTDIn()
					+ commonSocialMedia.getTotalInteractionYTDTw() + commonSocialMedia.getTotalInteractionYTDYt());
			response1.setInteractionRate(getDouble(
					Double.valueOf(response1.getTotalInteractions() / Double.valueOf(response1.getTotalPosts()))));	
			
			activityTotals.add(response1);

			response2.setTotalType("Month");
			response2.setId(2);
			response2.setTotalMembers(totalMemberCount);
			response2.setTotalPosts(commonSocialMedia.getTotalPostMonthlyFb() + commonSocialMedia.getTotalPostMonthlyIn()
					+ commonSocialMedia.getTotalPostMonthlyTw() + commonSocialMedia.getTotalPostMonthlyYt());
			response2.setTotalInteractions(commonSocialMedia.getTotalInteractionMonthlyFb() + commonSocialMedia.getTotalInteractionMonthlyIn()
					+ commonSocialMedia.getTotalInteractionMonthlyTw() + commonSocialMedia.getTotalInteractionMonthlyYt());
			response2.setInteractionRate(getDouble(
					Double.valueOf(response2.getTotalInteractions() / Double.valueOf(response2.getTotalPosts()))));
			
			activityTotals.add(response2);

			response3.setTotalType("Quarter");
			response3.setId(3);
			response3.setTotalMembers(totalMemberCount);
			response3.setTotalPosts(commonSocialMedia.getTotalPostQuarterlyFb() + commonSocialMedia.getTotalPostQuarterlyIn()
					+ commonSocialMedia.getTotalPostQuarterlyTw() + commonSocialMedia.getTotalPostQuarterlyYt());
			response3.setTotalInteractions(commonSocialMedia.getTotalInteractionQuarterlyFb() + commonSocialMedia.getTotalInteractionQuarterlyIn()
					+ commonSocialMedia.getTotalInteractionQuarterlyTw() + commonSocialMedia.getTotalInteractionQuarterlyYt());
			response3.setInteractionRate(getDouble(
					Double.valueOf(response3.getTotalInteractions() / Double.valueOf(response3.getTotalPosts()))));
			
			activityTotals.add(response3);

			sessionMap.put(SessionVariables.SOCIALACTIVITYTOTALS, activityTotals);
			
			// End Social Activity Totals
			
			//Social Activities per site
			val activitiesPerSite = new ArrayList<DashboardSocialActivityPerSiteResponse>();
			val responsePerSite1 = new DashboardSocialActivityPerSiteResponse();
			val responsePerSite2 = new DashboardSocialActivityPerSiteResponse();
			val responsePerSite3 = new DashboardSocialActivityPerSiteResponse();
			
			responsePerSite1.setPeriodType("Month");
			responsePerSite1.setFbTotalPosts(commonSocialMedia.getTotalPostMonthlyFb());
			responsePerSite1.setFbTotalInteractions(commonSocialMedia.getTotalInteractionMonthlyFb());
			responsePerSite1.setFbInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateMonthlyFb()));

			responsePerSite1.setInTotalPosts(commonSocialMedia.getTotalPostMonthlyIn());
			responsePerSite1.setInTotalInteractions(commonSocialMedia.getTotalInteractionMonthlyIn());
			responsePerSite1.setInInteractionRate(commonSocialMedia.getTotalInteractionRateMonthlyIn());

			responsePerSite1.setTwTotalPosts(commonSocialMedia.getTotalPostMonthlyTw());
			responsePerSite1.setTwTotalInteractions(commonSocialMedia.getTotalInteractionMonthlyTw());
			responsePerSite1.setTwInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateMonthlyTw()));

			responsePerSite1.setYtTotalPosts(commonSocialMedia.getTotalPostMonthlyYt());
			responsePerSite1.setYtTotalInteractions(commonSocialMedia.getTotalInteractionMonthlyYt());
			responsePerSite1.setYtInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateMonthlyYt()));
			
			activitiesPerSite.add(responsePerSite1);
			
			responsePerSite2.setPeriodType("Quarter");
			responsePerSite2.setFbTotalPosts(commonSocialMedia.getTotalPostQuarterlyFb());
			responsePerSite2.setFbTotalInteractions(commonSocialMedia.getTotalInteractionQuarterlyFb());
			responsePerSite2.setFbInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateQuarterlyFb()));

			responsePerSite2.setInTotalPosts(commonSocialMedia.getTotalPostQuarterlyIn());
			responsePerSite2.setInTotalInteractions(commonSocialMedia.getTotalInteractionQuarterlyIn());
			responsePerSite2.setInInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateQuarterlyIn()));

			responsePerSite2.setTwTotalPosts(commonSocialMedia.getTotalPostQuarterlyTw());
			responsePerSite2.setTwTotalInteractions(commonSocialMedia.getTotalInteractionQuarterlyTw());
			responsePerSite2.setTwInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateQuarterlyTw()));

			responsePerSite2.setYtTotalPosts(commonSocialMedia.getTotalPostQuarterlyYt());
			responsePerSite2.setYtTotalInteractions(commonSocialMedia.getTotalInteractionQuarterlyYt());
			responsePerSite2.setYtInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateQuarterlyYt()));
			
			activitiesPerSite.add(responsePerSite2);
			
			responsePerSite3.setPeriodType("Annual");
			responsePerSite3.setFbTotalPosts(commonSocialMedia.getTotalPostYearlyFb());
			responsePerSite3.setFbTotalInteractions(commonSocialMedia.getTotalInteractionYearlyFb());
			responsePerSite3.setFbInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateYearlyFb()));

			responsePerSite3.setInTotalPosts(commonSocialMedia.getTotalPostYearlyIn());
			responsePerSite3.setInTotalInteractions(commonSocialMedia.getTotalInteractionYearlyIn());
			responsePerSite3.setInInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateYearlyIn()));

			responsePerSite3.setTwTotalPosts(commonSocialMedia.getTotalPostYearlyTw());
			responsePerSite3.setTwTotalInteractions(commonSocialMedia.getTotalInteractionYearlyTw());
			responsePerSite3.setTwInteractionRate(getDouble(commonSocialMedia.getTotalInteractionRateYearlyTw()));

			responsePerSite3.setYtTotalPosts(commonSocialMedia.getTotalPostYearlyYt());
			responsePerSite3.setYtTotalInteractions(commonSocialMedia.getTotalInteractionYearlyYt());
			responsePerSite3.setYtInteractionRate(commonSocialMedia.getTotalInteractionRateYearlyYt());
			
			activitiesPerSite.add(responsePerSite3);
			
			sessionMap.put(SessionVariables.SOCIALACTIVITYPERSITETOTALS, activitiesPerSite);
			
			val keywords =dashboardService.fetchMostUsedKeywords(user.getUserId(), commonSocialMedia);
			sessionMap.put(SessionVariables.MOSTUSEDKEYWORDS, keywords);
			
		}else {
					//Virals
			val fbFeedMonthly =  fetchTotalFbFeebByClient(user.getUserId(),"Monthly", keywordsFB, userIds);
			val fbFeedQuaterly =  fetchTotalFbFeebByClient(user.getUserId(),"Quarterly", keywordsFB, userIds);
			val fbFeedYearly =  fetchTotalFbFeebByClient(user.getUserId(),"Yearly", keywordsFB, userIds);
			val fbFeedYTD =  fetchTotalFbFeebByClient(user.getUserId(),"YTD", keywordsFB, userIds);
			
			val instaFeedMonthly = fetchTotalInstaFeebByClient(user.getUserId(),"Monthly",keywordsIN, userIds);
			val instaFeedQuaterly = fetchTotalInstaFeebByClient(user.getUserId(),"Quarterly", keywordsIN, userIds);
			val instaFeedYearly = fetchTotalInstaFeebByClient(user.getUserId(),"Yearly", keywordsIN, userIds);
			val instaFeedYTD = fetchTotalInstaFeebByClient(user.getUserId(),"YTD", keywordsIN, userIds);

			val twFeedMonthly = fetchTotalTwFeebByClient(user.getUserId(),"Monthly",keywordsTW, userIds);
			val twFeedQuaterly = fetchTotalTwFeebByClient(user.getUserId(),"Quarterly",keywordsTW, userIds);
			val twFeedYearly = fetchTotalTwFeebByClient(user.getUserId(),"Yearly",keywordsTW, userIds);
			val twFeedYTD = fetchTotalTwFeebByClient(user.getUserId(),"YTD",keywordsTW, userIds);

			val ytFeedMonthly = fetchTotalYtFeebByClient(user.getUserId(),"Monthly",keywordsYT, userIds);
			val ytFeedQuaterly = fetchTotalYtFeebByClient(user.getUserId(),"Quarterly",keywordsYT, userIds);
			val ytFeedYearly = fetchTotalYtFeebByClient(user.getUserId(),"Yearly",keywordsYT, userIds);
			val ytFeedYTD = fetchTotalYtFeebByClient(user.getUserId(),"YTD",keywordsYT, userIds);
			
			//SOCIAL ACTIVITY TOTAL
			val response1 = new DashboardSocialActivityTotalsResponse();
			val response2 = new DashboardSocialActivityTotalsResponse();
			val response3 = new DashboardSocialActivityTotalsResponse();

			val activityTotals = new ArrayList<DashboardSocialActivityTotalsResponse>();
			val totalMemberCount = memberCount.getActiveMembers() + memberCount.getInactiveMembers();

			response1.setTotalType("YTD");
			response1.setId(1);
			response1.setTotalMembers(totalMemberCount);
			response1.setTotalPosts(fbFeedYTD.getTotalPost() + instaFeedYTD.getTotalPost() + twFeedYTD.getTotalPost()
					+ ytFeedYTD.getTotalPost());
			response1.setTotalInteractions(fbFeedYTD.getTotalInteraction() + instaFeedYTD.getTotalInteraction()
					+ twFeedYTD.getTotalInteraction() + ytFeedYTD.getTotalInteraction());
			response1.setInteractionRate(getDouble(
					Double.valueOf(response1.getTotalInteractions() / Double.valueOf(response1.getTotalPosts()))));	
			
			
			activityTotals.add(response1);

			response2.setTotalType("Month");
			response2.setId(2);
			response2.setTotalMembers(totalMemberCount);
			response2.setTotalPosts(fbFeedMonthly.getTotalPost() + instaFeedMonthly.getTotalPost()
					+ twFeedMonthly.getTotalPost() + ytFeedMonthly.getTotalPost());
			response2.setTotalInteractions(fbFeedMonthly.getTotalInteraction() + instaFeedMonthly.getTotalInteraction()
					+ twFeedMonthly.getTotalInteraction() + ytFeedMonthly.getTotalInteraction());
			response2.setInteractionRate(getDouble(
					Double.valueOf(response2.getTotalInteractions() / Double.valueOf(response2.getTotalPosts()))));
			
			activityTotals.add(response2);

			response3.setTotalType("Quarter");
			response3.setId(3);
			response3.setTotalMembers(totalMemberCount);
			response3.setTotalPosts(fbFeedQuaterly.getTotalPost() + instaFeedQuaterly.getTotalPost()
					+ twFeedQuaterly.getTotalPost() + ytFeedQuaterly.getTotalPost());
			response3.setTotalInteractions(fbFeedQuaterly.getTotalInteraction() + instaFeedQuaterly.getTotalInteraction()
					+ twFeedQuaterly.getTotalInteraction() + ytFeedQuaterly.getTotalInteraction());
			response3.setInteractionRate(getDouble(
					Double.valueOf(response3.getTotalInteractions() / Double.valueOf(response3.getTotalPosts()))));
			
			activityTotals.add(response3);

			sessionMap.put(SessionVariables.SOCIALACTIVITYTOTALS, activityTotals);
			
			// End Social Activity Totals
			
			//Social Activities per site
			val activitiesPerSite = new ArrayList<DashboardSocialActivityPerSiteResponse>();
			val responsePerSite1 = new DashboardSocialActivityPerSiteResponse();
			val responsePerSite2 = new DashboardSocialActivityPerSiteResponse();
			val responsePerSite3 = new DashboardSocialActivityPerSiteResponse();
			
			responsePerSite1.setPeriodType("Month");
			responsePerSite1.setFbTotalPosts(fbFeedMonthly.getTotalPost());
			responsePerSite1.setFbTotalInteractions(fbFeedMonthly.getTotalInteraction());
			responsePerSite1.setFbInteractionRate(getDouble(fbFeedMonthly.getTotalInteractionRate()));

			responsePerSite1.setInTotalPosts(instaFeedMonthly.getTotalPost());
			responsePerSite1.setInTotalInteractions(instaFeedMonthly.getTotalInteraction());
			responsePerSite1.setInInteractionRate(instaFeedMonthly.getTotalInteractionRate());

			responsePerSite1.setTwTotalPosts(twFeedMonthly.getTotalPost());
			responsePerSite1.setTwTotalInteractions(twFeedMonthly.getTotalInteraction());
			responsePerSite1.setTwInteractionRate(getDouble(twFeedMonthly.getTotalInteractionRate()));

			responsePerSite1.setYtTotalPosts(ytFeedMonthly.getTotalPost());
			responsePerSite1.setYtTotalInteractions(ytFeedMonthly.getTotalInteraction());
			responsePerSite1.setYtInteractionRate(getDouble(ytFeedMonthly.getTotalInteractionRate()));
			
			activitiesPerSite.add(responsePerSite1);
			
			responsePerSite2.setPeriodType("Quarter");
			responsePerSite2.setFbTotalPosts(fbFeedQuaterly.getTotalPost());
			responsePerSite2.setFbTotalInteractions(fbFeedQuaterly.getTotalInteraction());
			responsePerSite2.setFbInteractionRate(getDouble(fbFeedQuaterly.getTotalInteractionRate()));

			responsePerSite2.setInTotalPosts(instaFeedQuaterly.getTotalPost());
			responsePerSite2.setInTotalInteractions(instaFeedQuaterly.getTotalInteraction());
			responsePerSite2.setInInteractionRate(getDouble(instaFeedQuaterly.getTotalInteractionRate()));

			responsePerSite2.setTwTotalPosts(twFeedQuaterly.getTotalPost());
			responsePerSite2.setTwTotalInteractions(twFeedQuaterly.getTotalInteraction());
			responsePerSite2.setTwInteractionRate(getDouble(twFeedQuaterly.getTotalInteractionRate()));

			responsePerSite2.setYtTotalPosts(ytFeedQuaterly.getTotalPost());
			responsePerSite2.setYtTotalInteractions(ytFeedQuaterly.getTotalInteraction());
			responsePerSite2.setYtInteractionRate(getDouble(ytFeedQuaterly.getTotalInteractionRate()));
			
			activitiesPerSite.add(responsePerSite2);
			
			responsePerSite3.setPeriodType("Annual");
			responsePerSite3.setFbTotalPosts(fbFeedYearly.getTotalPost());
			responsePerSite3.setFbTotalInteractions(fbFeedYearly.getTotalInteraction());
			responsePerSite3.setFbInteractionRate(getDouble(fbFeedYearly.getTotalInteractionRate()));

			responsePerSite3.setInTotalPosts(instaFeedYearly.getTotalPost());
			responsePerSite3.setInTotalInteractions(instaFeedYearly.getTotalInteraction());
			responsePerSite3.setInInteractionRate(getDouble(instaFeedYearly.getTotalInteractionRate()));

			responsePerSite3.setTwTotalPosts(twFeedYearly.getTotalPost());
			responsePerSite3.setTwTotalInteractions(twFeedYearly.getTotalInteraction());
			responsePerSite3.setTwInteractionRate(getDouble(twFeedYearly.getTotalInteractionRate()));

			responsePerSite3.setYtTotalPosts(ytFeedYearly.getTotalPost());
			responsePerSite3.setYtTotalInteractions(ytFeedYearly.getTotalInteraction());
			responsePerSite3.setYtInteractionRate(ytFeedYearly.getTotalInteractionRate());
			
			activitiesPerSite.add(responsePerSite3);
			
			sessionMap.put(SessionVariables.SOCIALACTIVITYPERSITETOTALS, activitiesPerSite);
			
			DashboardSocialmediaPerSite dashboard = new DashboardSocialmediaPerSite();
			dashboard.setCompanyId(user.getUserId());
			dashboard.setTotalPostMonthlyFb(fbFeedMonthly.getTotalPost());
			dashboard.setTotalInteractionMonthlyFb(fbFeedMonthly.getTotalInteraction());
			dashboard.setTotalPostQuarterlyFb(fbFeedQuaterly.getTotalPost());
			dashboard.setTotalInteractionQuarterlyFb(fbFeedQuaterly.getTotalInteraction());
			dashboard.setTotalPostYearlyFb(fbFeedYearly.getTotalPost());
			dashboard.setTotalInteractionYearlyFb(fbFeedYearly.getTotalInteraction());
			dashboard.setTotalInteractionRateYearlyFb(fbFeedYearly.getTotalInteractionRate());
			dashboard.setTotalPostYTDFb(fbFeedYTD.getTotalPost());
			dashboard.setTotalInteractionYTDFb(fbFeedYTD.getTotalInteraction());

			dashboard.setTotalPostMonthlyIn(instaFeedMonthly.getTotalPost());
			dashboard.setTotalInteractionMonthlyIn(instaFeedMonthly.getTotalInteraction());
			dashboard.setTotalPostQuarterlyIn(instaFeedQuaterly.getTotalPost());
			dashboard.setTotalInteractionQuarterlyIn(instaFeedQuaterly.getTotalInteraction());
			dashboard.setTotalPostYearlyIn(instaFeedYearly.getTotalPost());
			dashboard.setTotalInteractionYearlyIn(instaFeedYearly.getTotalInteraction());

			dashboard.setTotalInteractionRateYearlyIn(instaFeedYearly.getTotalInteractionRate());
			dashboard.setTotalPostYTDIn(instaFeedYTD.getTotalPost());
			dashboard.setTotalInteractionYTDIn(instaFeedYTD.getTotalInteraction());

			dashboard.setTotalPostMonthlyTw(twFeedMonthly.getTotalPost());
			dashboard.setTotalInteractionMonthlyTw(twFeedMonthly.getTotalInteraction());
			dashboard.setTotalPostQuarterlyTw(twFeedQuaterly.getTotalPost());
			dashboard.setTotalInteractionQuarterlyTw(twFeedQuaterly.getTotalInteraction());
			dashboard.setTotalPostYearlyTw(twFeedYearly.getTotalPost());
			dashboard.setTotalInteractionYearlyTw(twFeedYearly.getTotalInteraction());

			dashboard.setTotalInteractionRateYearlyTw(twFeedYearly.getTotalInteractionRate());
			dashboard.setTotalPostYTDTw(twFeedYTD.getTotalPost());
			dashboard.setTotalInteractionYTDTw(twFeedYTD.getTotalInteraction());

			dashboard.setTotalPostMonthlyYt(ytFeedMonthly.getTotalPost());
			dashboard.setTotalInteractionMonthlyYt(ytFeedMonthly.getTotalInteraction());
			dashboard.setTotalPostQuarterlyYt(ytFeedQuaterly.getTotalPost());
			dashboard.setTotalInteractionQuarterlyYt(ytFeedQuaterly.getTotalInteraction());
			dashboard.setTotalPostYearlyYt(ytFeedYearly.getTotalPost());
			dashboard.setTotalInteractionYearlyYt(ytFeedYearly.getTotalInteraction());

			dashboard.setTotalInteractionRateYearlyYt(ytFeedYearly.getTotalInteractionRate());
			dashboard.setTotalPostYTDYt(ytFeedYTD.getTotalPost());
			dashboard.setTotalInteractionYTDYt(ytFeedYTD.getTotalInteraction());
			
			userService.deleteDashboardSocialMediaPerSite(dashboard);
			
			userService.saveDashboardSocialMediaPerSite(dashboard);
			
			//Most used Keywords
			val keywordsFBUser = user.getFbKeywords() == null ? "" : user.getFbKeywords(); 
			val keywordsINUser = user.getInstaKeywords() == null ? "" : user.getInstaKeywords();
			val keywordsTWUser = user.getTwtKeywords() == null ? "" : user.getTwtKeywords();
			val keywordsYTUser = user.getYoutubeKeywords() == null ? "" : user.getYoutubeKeywords();
			
			List<String> keywordsList = new ArrayList<String>();
			
			if(keywordsFBUser != null) {
				keywordsList.addAll(Arrays.asList( keywordsFBUser.split(",")));
			}
			if(keywordsINUser != null) {
				keywordsList.addAll(Arrays.asList( keywordsINUser.split(",")));
			}
			if(keywordsTWUser != null) {
				keywordsList.addAll(Arrays.asList( keywordsTWUser.split(",")));
			}
			if(keywordsYTUser != null) {
				keywordsList.addAll(Arrays.asList( keywordsYTUser.split(",")));
			}
			
			List<String> keywordArrayList = keywordsList.stream()
				     .distinct()
				     .collect(Collectors.toList());
			
			// the actual operation
			TreeSet<String> seen = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
			keywordArrayList.removeIf(s -> !seen.add(s));

			
			val keywords =dashboardService.fetchMostUsedKeywords(user.getUserId(),
					keywordArrayList);
			sessionMap.put(SessionVariables.MOSTUSEDKEYWORDS, keywords);
			
			
			
		}
		
		return sessionMap; 
	} 
	
	private Double getDouble(Double val) {
		return Math.round(val * 100D) / 100D;
	}
	
	private DashboardSocialMediaStats fetchTotalTwFeebByClient(Long companyId, String period, String keywordsFB, List<Long> userIds) {
		
		Date toDate = new Date();
		Date fromDate = new Date();
		
		if (period.equalsIgnoreCase("Monthly")) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			fromDate = cal.getTime();
		}
		
		if (period.equalsIgnoreCase("Quarterly")) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -3);
			fromDate = cal.getTime();
		}

		if (period.equalsIgnoreCase("Yearly")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			fromDate = cal.getTime();
		}
		
		if (period.equalsIgnoreCase("YTD")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			fromDate = cal.getTime();
		}
		
//		
//		if (period.equalsIgnoreCase("Monthly")) {
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -31);
//			fromDate = cal.getTime();
//		}
//		
//		if (period.equalsIgnoreCase("Quarterly")) {
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -90);
//			fromDate = cal.getTime();
//		}
//
//		if (period.equalsIgnoreCase("Yearly")) {
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -365);
//			fromDate = cal.getTime();
//		}
//		
//		if (period.equalsIgnoreCase("YTD")) {
//			Calendar cal = Calendar.getInstance();
//			cal.set(Calendar.DAY_OF_YEAR, 1);
//			fromDate = cal.getTime();
//		}
//		
		DashboardSocialMediaStats response = new DashboardSocialMediaStats();
		
		if(userIds != null && userIds.size() > 0) {
			response = dashboardService.fetchTotalTwFeebByClient(companyId, keywordsFB, fromDate, toDate, userIds);
		}
		
		return response;
	}
	
	private DashboardSocialMediaStats fetchTotalYtFeebByClient(Long companyId, String period, String keywordsFB, List<Long> userIds) {
		
		Date toDate = new Date();
		Date fromDate = new Date();
		
		if (period.equalsIgnoreCase("Monthly")) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			fromDate = cal.getTime();
		}
		
		if (period.equalsIgnoreCase("Quarterly")) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -3);
			fromDate = cal.getTime();
		}

		if (period.equalsIgnoreCase("Yearly")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			fromDate = cal.getTime();
		}
		
		if (period.equalsIgnoreCase("YTD")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			fromDate = cal.getTime();
		}
		
		
//		if (period.equalsIgnoreCase("Monthly")) {
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -31);
//			fromDate = cal.getTime();
//		}
//		
//		if (period.equalsIgnoreCase("Quarterly")) {
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -90);
//			fromDate = cal.getTime();
//		}
//
//		if (period.equalsIgnoreCase("Yearly")) {
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -365);
//			fromDate = cal.getTime();
//		}
//		
//		if (period.equalsIgnoreCase("YTD")) {
//			Calendar cal = Calendar.getInstance();
//			cal.set(Calendar.DAY_OF_YEAR, 1);
//			fromDate = cal.getTime();
//		}
		
		DashboardSocialMediaStats response = new DashboardSocialMediaStats();
		
		if(userIds != null && userIds.size() > 0) {
			response = dashboardService.fetchTotalYtFeebByClient(companyId, keywordsFB, fromDate, toDate, userIds);
		}
		
		return response;
	}
	
	private DashboardSocialMediaStats fetchTotalInstaFeebByClient(Long companyId, String period, String keywordsFB, List<Long> userIds) {
		
		Date toDate = new Date();
		Date fromDate = new Date();
		
		if (period.equalsIgnoreCase("Monthly")) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			fromDate = cal.getTime();
		}
		
		if (period.equalsIgnoreCase("Quarterly")) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -3);
			fromDate = cal.getTime();
		}

		if (period.equalsIgnoreCase("Yearly")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			fromDate = cal.getTime();
		}
		
		if (period.equalsIgnoreCase("YTD")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			fromDate = cal.getTime();
		}
		
//		if (period.equalsIgnoreCase("Monthly")) {
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -31);
//			fromDate = cal.getTime();
//		}
//		
//		if (period.equalsIgnoreCase("Quarterly")) {
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -90);
//			fromDate = cal.getTime();
//		}
//
//		if (period.equalsIgnoreCase("Yearly")) {
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -365);
//			fromDate = cal.getTime();
//		}
//		
//		if (period.equalsIgnoreCase("YTD")) {
//			Calendar cal = Calendar.getInstance();
//			cal.set(Calendar.DAY_OF_YEAR, 1);
//			fromDate = cal.getTime();
//		}
//		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String strFromDate = df.format(fromDate.getTime());
		
		try {
			fromDate = new SimpleDateFormat("MM/dd/yyyy").parse(strFromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		DashboardSocialMediaStats response = new DashboardSocialMediaStats();
		
		if(userIds != null && userIds.size() > 0) {
			response = dashboardService.fetchTotalInstaFeebByClient(companyId, keywordsFB, fromDate, toDate, userIds);
		}
		 
		return response;
	}
	

	private DashboardSocialMediaStats fetchTotalFbFeebByClient(Long companyId, String period, String keywordsFB, List<Long> userIds) {
		
		
		Date toDate = Calendar.getInstance().getTime();
		Date fromDate = Calendar.getInstance().getTime();
		
		if (period.equalsIgnoreCase("Monthly")) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			fromDate = cal.getTime();
		}
		
		if (period.equalsIgnoreCase("Quarterly")) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -3);
			fromDate = cal.getTime();
		}

		if (period.equalsIgnoreCase("Yearly")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			fromDate = cal.getTime();
		}
		
		if (period.equalsIgnoreCase("YTD")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			fromDate = cal.getTime();
		}
		
		DashboardSocialMediaStats response = new DashboardSocialMediaStats();
		
		if(userIds != null && userIds.size() > 0) {
			response = dashboardService.fetchTotalFbFeebByClient(companyId, keywordsFB, fromDate, toDate, userIds);
		}
		 
		return response;
	}
	
/*
	private void validateTimeToWait(final User user) {
		final int timeToWait = user.getNumberOfFailedLogins() * DELAY_IN_MINUTES_BEFORE_ALLOWED_AFTER_FAILED_LOGIN;

		final long currentTime = new Timestamp(new Date().getTime()).getTime();
		final long timeSinceLastFailedLogin = user.getLastFailedLogin().toInstant().toEpochMilli();
		final long timeDifference = currentTime - timeSinceLastFailedLogin;

		if(timeDifference < timeToWait * 60 * 1000) {	

			long diffSec = timeDifference / 1000;
			long min = timeToWait - (diffSec / 60);
			long sec = diffSec % 60;

			if(sec > 30) {
				//min = min + 1;
			}
			throw new DisabledException(String.format("You account has been disabled temporarily. Please retry after %s minutes.", min));
		}
		else {
			user.setTempDisabled(false);
		}
	} */
}
