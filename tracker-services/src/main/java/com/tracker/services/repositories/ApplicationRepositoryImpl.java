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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.dtos.Application;
import com.tracker.commons.dtos.SportsUserProAppDetails;
import com.tracker.commons.models.User;

@Repository
@Transactional
public class ApplicationRepositoryImpl implements ApplicationRepositoryCustom {

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public SportsUserProAppDetails fetchApplicationSports(Long userId, Long companyId) {
		
		String queryStr = "SELECT u.userid, u.FirstLastname, u.dob, u.highesteducation, " + 
				"u.street, u.city,u.state, u.zipcode, u.email, u.phone  " + 
				"FROM users u 								 " + 
				"LEFT JOIN user_pro_app_details uad ON uad.userid=u.userid 								 " + 
				"WHERE (uad.status='Pending' OR uad.status='Rejected') " + 
				"								AND u.userid = :userId AND uad.companyID=:companyID";
		
//		Query nativeQuery = em.createNativeQuery(queryStr, SportsUserProAppDetails.class);
//		nativeQuery.setParameter("userId", userId);
//		nativeQuery.setParameter("companyID", companyId);
//		
//		SportsUserProAppDetails uad = new SportsUserProAppDetails();
//		
//		@SuppressWarnings("unchecked")
//		List<SportsUserProAppDetails> list = nativeQuery.getResultList();
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("userId", userId);
		nativeQuery.setParameter("companyID", companyId);
		
		SportsUserProAppDetails uad = new SportsUserProAppDetails();
		
		@SuppressWarnings("unchecked")
//		List<SportsUserProAppDetails> list = nativeQuery.getResultList();
		List<Object[]> list = nativeQuery.getResultList();

		for(Object[] uapd1 : list) {
			User user = userRepository.findByUserId(Long.parseLong(uapd1[0].toString()));
			
			//uad = uapd;
			try {
				//uad = uapd1;
				uad.setFirstLastName(user.getFirstLastName());
				uad.setDob(user.getDob());
				uad.setHighestEducation(user.getHighestEducation());
				uad.setStreet(user.getStreet());
				uad.setCity(user.getCity());
				uad.setState(user.getState());
				uad.setZipcode(user.getZipcode());
				uad.setEmail(user.getEmail());
				uad.setPhone(user.getPhone());
				uad.setSec_phone("");
			//uad.setFirstLastName(user.getFirstLastName());
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//uad.setDob(user.getDob());
//			try {
//			
//				uad.setHighestEducation(uapd1[2].toString());
//			
//			}catch(Exception e) {
//				e.printStackTrace();
//
//				
//				uad.setHighestEducation("education");
//	
//			}
//			try {
//			
//				uad.setStreet(uapd1[3].toString());
//			
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//			try {
//			
//				uad.setCity(uapd1[4].toString());
//			
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//			try {
//			
//				uad.setState(uapd1[5].toString());
//			
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			try {
//			
//				uad.setZipcode(uapd1[6].toString());
//			
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//			try {
//			
//				uad.setEmail(uapd1[7].toString());
//			
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//			try {
//			
//				uad.setPhone(uapd1[8].toString());
//			
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			try {
//			
//				uad.setSec_phone("");
//			
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
		}
		
		return uad;
		
	}  

	@Override
	public Application fetchApplication(Long userId, Long companyId) {
		
		String queryStr = "SELECT u.userid, u.FirstLastname, u.dob, u.highesteducation as highestEducation, u.street, u.city,u.state, u.zipcode, " + 
				" uad.ship_street, uad.ship_city, uad.ship_state, uad.ship_zipcode, u.email, u.phone, uad.sec_phone, " + 
				" uad.references_1,uad.references_2, uad.references_3, uad.shirt_size, uad.pant_size, uad.prefer_hardware, "+
				" uad.home_lake_river, uad.species_type, uad.tournament_fish_year, uad.isfish_tournament_trails, uad.water_type, " + 
				" uad.isguide, uad.isguidelicence, uad.license_number, uad.guideyear, uad.iswebsite, uad.flyfishingcareer_1, " +
				" uad.flyfishingcareer_2, uad.isusedproducts, uad.useproducts, uad.experiencewithproducts, "+
				" uad.social_media_platform, uad.facebook_personal_page, uad.link_to_facebook_fan_page, uad.twitter_personal_page, " + 
				" uad.link_to_twitter_personal_page, uad.link_to_twitter_fan_page, uad.link_to_instagram_personal_page, " + 
				" uad.instagram_fan_page, uad.isactive_on_forum_blog, uad.active_on_fishing_blog_1,uad.active_on_fishing_blog_2, uad.active_on_fishing_blog_3, " + 
				" uad.active_on_fishing_blog_4, uad.active_on_fishing_blog_5, uad.active_on_fishing_blog_6, uad.active_on_fishing_blog_7, " + 
				" uad.active_on_fishing_blog_8, uad.active_on_fishing_blog_9, uad.active_on_fishing_blog_10, uad.active_on_fishing_blog_11, " + 
				" uad.active_on_fishing_blog_12, uad.other_sposors_1,uad.other_sposors_2, uad.other_sposors_3, uad.other_sposors_4, uad.other_sposors_5," +
				" uad.other_sposors_6, uad.other_sposors_7,uad.other_sposors_8, uad.other_sposors_9, uad.other_sposors_10, uad.other_sposors_11, " +
				" uad.other_sposors_12, uad.select_fishing_pro_staff, uad.other_information, uad.attach_resume, uad.upload_pictures, uad.link_to_facebook_personal_page " + 
				" FROM users u " + 
				"	LEFT JOIN user_pro_app_details uad ON uad.userid=u.userid " + 
				"	WHERE (uad.status='Pending' OR uad.status='Rejected') " + 
				"								AND u.userid = :userId AND uad.companyID=:companyID";
		
		Query nativeQuery = em.createNativeQuery(queryStr);
		nativeQuery.setParameter("userId", userId);
		nativeQuery.setParameter("companyID", companyId);
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = nativeQuery.getResultList();
		
		List<Application> tList = new ArrayList<Application>();
		
		for (Object[] obj : list) {
			
			Application yt = new Application();
			yt.setUserId(Long.parseLong(obj[0].toString()));
			yt.setFirstLastName(obj[1].toString());
			
			if(obj[2] != null) {
				String date = obj[2].toString();
				String dateStr[] = date.split(" ");
		        LocalDate localDate = LocalDate.parse(dateStr[0]);
				yt.setDob(localDate);
			}
			
			yt.setHighestEducation(obj[3] == null ? "" : obj[3].toString());
			yt.setStreet(obj[4] == null ? "" : obj[4].toString());
			yt.setCity(obj[5] == null ? "" : obj[5].toString());
			yt.setState(obj[6] == null ? "" : obj[6].toString());
			yt.setZipcode(obj[7] == null ? "" : obj[7].toString());
			
			yt.setShip_street(obj[8] == null ? "" : obj[8].toString());
			yt.setShip_city(obj[9] == null ? "" : obj[9].toString());
			yt.setShip_state(obj[10] == null ? "" : obj[10].toString());
			yt.setShip_zipcode(obj[11] == null ? "" : obj[11].toString());
			
			yt.setEmail(obj[12] == null ? "" : obj[12].toString());	
			yt.setPhone(obj[13] == null ? "" : obj[13].toString());					
			yt.setSec_phone(obj[14] == null ? "" : obj[14].toString());
			
			yt.setReferences_1(obj[15] == null ? "" : obj[15].toString());
			yt.setReferences_2(obj[16] == null ? "" : obj[16].toString());
			yt.setReferences_3(obj[17] == null ? "" : obj[17].toString());
			
			yt.setShirt_size(obj[18] == null ? "" : obj[18].toString());
			yt.setPant_size(obj[19] == null ? "" : obj[19].toString());
			yt.setPrefer_hardware(obj[20] == null ? "" : obj[20].toString());
			
			yt.setHome_lake_river(obj[21] == null ? "" : obj[21].toString());
			yt.setSpecies_type(obj[22] == null ? "" : obj[22].toString());
			yt.setTournament_fish_year(obj[23] == null ? "" : obj[23].toString());
			yt.setIsfish_tournament_trails(obj[24] == null ? "" : obj[24].toString());
			yt.setWater_type(obj[25] == null ? "" : obj[25].toString());
			yt.setIsguide(obj[26] == null ? "" : obj[26].toString());
			yt.setIsguidelicence(obj[27] == null ? "" : obj[27].toString());
			yt.setLicense_number(obj[28] == null ? "" : obj[28].toString());
			yt.setGuideyear(obj[29] == null ? "" : obj[29].toString());
			yt.setIswebsite(obj[30] == null ? "" : obj[30].toString());
			yt.setFlyfishingcareer_1(obj[31] == null ? "" : obj[31].toString());
			yt.setFlyfishingcareer_2(obj[32] == null ? "" : obj[32].toString());
			
			yt.setIsusedproducts(obj[33] == null ? "" : obj[33].toString());
			yt.setUseproducts(obj[34] == null ? "" : obj[34].toString());
			yt.setExperiencewithproducts(obj[35] == null ? "" : obj[35].toString());
			
			yt.setSocial_media_platform(obj[36] == null ? "" : obj[36].toString());
			yt.setFacebook_personal_page(obj[37] == null ? "" : obj[37].toString());
			yt.setLink_to_facebook_fan_page(obj[38] == null ? "" : obj[38].toString());
			yt.setTwitter_personal_page(obj[39] == null ? "" : obj[39].toString());
			yt.setLink_to_twitter_personal_page(obj[40] == null ? "" : obj[40].toString());
			yt.setLink_to_twitter_fan_page(obj[41] == null ? "" : obj[41].toString());
			yt.setLink_to_instagram_personal_page(obj[42] == null ? "" : obj[42].toString());
			yt.setInstagram_fan_page(obj[43] == null ? "" : obj[43].toString());
			yt.setIsactive_on_forum_blog(obj[44] == null ? "" : obj[44].toString());
			yt.setActive_on_fishing_blog_1(obj[45] == null ? "" : obj[45].toString());
			yt.setActive_on_fishing_blog_2(obj[46] == null ? "" : obj[46].toString());
			yt.setActive_on_fishing_blog_3(obj[47] == null ? "" : obj[47].toString());
			yt.setActive_on_fishing_blog_4(obj[48] == null ? "" : obj[48].toString());
			yt.setActive_on_fishing_blog_5(obj[49] == null ? "" : obj[49].toString());
			yt.setActive_on_fishing_blog_6(obj[50] == null ? "" : obj[50].toString());
			yt.setActive_on_fishing_blog_7(obj[51] == null ? "" : obj[51].toString());
			yt.setActive_on_fishing_blog_8(obj[52] == null ? "" : obj[52].toString());
			yt.setActive_on_fishing_blog_9(obj[53] == null ? "" : obj[53].toString());
			yt.setActive_on_fishing_blog_10(obj[54] == null ? "" : obj[54].toString());
			yt.setActive_on_fishing_blog_11(obj[55] == null ? "" : obj[55].toString());
			yt.setActive_on_fishing_blog_12(obj[56] == null ? "" : obj[56].toString());
			
			yt.setOther_sposors_1(obj[57] == null ? "" : obj[57].toString());
			yt.setOther_sposors_2(obj[58] == null ? "" : obj[58].toString());
			yt.setOther_sposors_3(obj[59] == null ? "" : obj[59].toString());
			yt.setOther_sposors_4(obj[60] == null ? "" : obj[60].toString());
			yt.setOther_sposors_5(obj[61] == null ? "" : obj[61].toString());
			yt.setOther_sposors_6(obj[62] == null ? "" : obj[62].toString());
			yt.setOther_sposors_7(obj[63] == null ? "" : obj[63].toString());
			yt.setOther_sposors_8(obj[64] == null ? "" : obj[64].toString());
			yt.setOther_sposors_9(obj[65] == null ? "" : obj[65].toString());
			yt.setOther_sposors_10(obj[66] == null ? "" : obj[66].toString());
			yt.setOther_sposors_11(obj[67] == null ? "" : obj[67].toString());
			yt.setOther_sposors_12(obj[68] == null ? "" : obj[68].toString());
			yt.setSelect_fishing_pro_staff(obj[69] == null ? "" : obj[69].toString());
			yt.setOther_information(obj[70] == null ? "" : obj[70].toString());
			yt.setAttach_resume(obj[71] == null ? "" : obj[71].toString());
			yt.setUpload_pictures(obj[72] == null ? "" : obj[72].toString());
			yt.setLink_to_facebook_personal_page(obj[73] == null ? "" : obj[73].toString());
			
			tList.add(yt);
			
		}
		
		return tList.get(0);
		
	}  
	

	

}
