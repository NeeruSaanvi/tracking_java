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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.UserMedia;



@Repository
@Transactional
public class UserMediaRepositoryImpl implements UserMediaRepositoryCustom {

	@PersistenceContext
	
	
	EntityManager em;


	@SuppressWarnings("unchecked")
	public UserMedia getAccessToken(Long userid, String mediasource) {

		String queryStr = "Select access_token , username,email from user_media where userid=" + userid + " and social_type='"
				+ mediasource + "'";
		Query nativeQuery = em.createNativeQuery(queryStr);
		String access_token = "";
		List<Object[]> objectList = nativeQuery.getResultList();
		for (Object[] obj : objectList) {
			
			UserMedia user = new UserMedia();
			
			if (obj != null && obj[0] != null) {

				String access_token1 = obj[0].toString();

				user.setAccessToken(access_token1);
				
				
			}
			if (obj != null && obj[1] != null) {


				user.setUsername(obj[1].toString());
				
				
			}
			if (obj != null && obj[2] != null) {


				user.setEmail(obj[2].toString());
				
				
			}
			
			return user;
		}
		return null;
	}
	
	
	public void saveAccessToken1(Long userid, String mediasource,String access_token) {
	 
		
		String queryStr = "INSERT INTO user_media ( " + "access_token, "
				+ "				social_type " + "	) " + "		VALUES "
				+ "			( " + "				" + access_token + ", " + "				"
				+ mediasource + ") where userid=" + userid ;


		
		Query nativeQuery = em.createNativeQuery(queryStr);
		
		nativeQuery.executeUpdate();
		
	}
	
	public void deleteAccessToken(Long userid, String mediasource,String access_token) {
		 
		String queryStr = "DELETE from user_media "
				+ "	 where userid=" + userid 
				+" AND social_type="+ "'"+mediasource+"'";

		
		Query nativeQuery = em.createNativeQuery(queryStr);
		
		nativeQuery.executeUpdate();
	}
	
	public void updateAccessToken(Long userid, String mediasource,String access_token) {
		 
		String queryStr = "UPDATE  user_media SET access_token=" + "'"+access_token+"'" 
				+ "	 where userid=" + userid ;

		
		Query nativeQuery = em.createNativeQuery(queryStr);
		
		nativeQuery.executeUpdate();
	}
	
}
