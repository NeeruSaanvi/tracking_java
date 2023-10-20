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

import java.io.ByteArrayOutputStream;
import java.time.format.*;
import java.time.*;
import java.util.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.commons.Constants;
import com.tracker.commons.dtos.Application;
import com.tracker.commons.dtos.Application1;
import com.tracker.commons.dtos.ParentApplication;
import com.tracker.commons.dtos.SportsUserProAppDetails;
import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.CompanyRef;
import com.tracker.commons.models.DashboardSocialmediaPerSite;
import com.tracker.commons.models.LinkedSocialMedia;
import com.tracker.commons.models.Notification;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserMedia;
import com.tracker.commons.models.UserProAppDetails;
import com.tracker.commons.models.UserProAppDetailsSports;
import com.tracker.commons.models.UserPwdTrack;
import com.tracker.commons.models.UserStore;
import com.tracker.commons.models.UserTeam;
import com.tracker.services.dto.CreateUserDTO;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.emails.ForgotPasswordEmailTemplate;
import com.tracker.services.repositories.ApplicationRepository;
import com.tracker.services.repositories.CompanyRefRepository;
import com.tracker.services.repositories.DashboardSocialmediaPerSiteRepository;
import com.tracker.services.repositories.LinkedSocialMediaRepository;
import com.tracker.services.repositories.NotificationRepository;
import com.tracker.services.repositories.UserMediaRepository;
import com.tracker.services.repositories.UserProAppDetailsRepository;
import com.tracker.services.repositories.UserProAppDetailsSportsRepository;
import com.tracker.services.repositories.UserPwdTrackRepository;
import com.tracker.services.repositories.UserRepository;
import com.tracker.services.repositories.UserTeamRepository;
import com.tracker.services.utils.EnvironmentUtil;
import com.tracker.services.utils.ExcelUtils;
import com.tracker.services.utils.PasswordUtil;
import com.tracker.services.utils.UserUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserTeamRepository userTeamRepository;
	
	@Autowired
	private UserPwdTrackRepository userPwdTrackRepository;
	
	@Autowired
	private CompanyRefRepository companyRefRepository;
	
	@Autowired
	private UserProAppDetailsRepository userProAppDetailsRepository;
	
	@Autowired
	private UserProAppDetailsSportsRepository userProAppDetailsSportsRepository;

	@Autowired
	private ForgotPasswordEmailTemplate forgotPasswordEmail;
	
//	@SuppressWarnings("unused")
//	@Autowired
//	private NewUserValidationEmailTemplate newUserValidationEmailTemplate;

	@Autowired
	private EnvironmentUtil environmentUtil; 
	
	@Autowired
	private SendGridEmailService sendGridEmailService;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private DashboardSocialmediaPerSiteRepository dashboardSocialmediaPerSiteRepository;
	
	@Autowired
    private Environment environment;
	
	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	LinkedSocialMediaRepository mediaRepo;
	
	@Autowired
	UserMediaRepository userMediaRepo;
	
	private final CloseableHttpClient httpClient = HttpClients.createDefault();
	

	public List<User> findAll(final PaginationRequestDTO dto) {
		return userRepository.findAll(dto.getRequest()).getContent();
	} 

	public int findAllCount() {
		return Long.valueOf(userRepository.count()).intValue();
	} 
	
	public List<CompanyRef> getAnnualRankingListByCompnay(Long companyId, Long userId) {
		
		return userRepository.getAnnualRankingListByCompnay(companyId, userId);
	}
	
	public void saveDashboardSocialMediaPerSite(DashboardSocialmediaPerSite dashboard) {
		dashboardSocialmediaPerSiteRepository.save(dashboard);
	}
	
	public void deleteDashboardSocialMediaPerSite(DashboardSocialmediaPerSite dashboard) {
		dashboardSocialmediaPerSiteRepository.deleteByCompanyId(dashboard.getCompanyId());
	}
	
	public boolean activateUser(Long sponserId, Long userId) {
		boolean success = true;
		User user = new User();

		try {
			user = userRepository.findByUserId(userId);
			User sponserUser = userRepository.findByUserId(sponserId);
			if(user == null || sponserUser == null ) {
				return false;
			}
			UserPwdTrack userPwdTrack = userPwdTrackRepository.findByUserId(userId);
			
			final Notification notification = notificationRepository.findByOwnerIdAndType(sponserId, "Welcome");
			String sub = "Welcome To The "+sponserUser.getFirstLastName()+" Team!";
			String subject = ( notification != null && StringUtils.isAllEmpty(notification.getSubject()) ) ? sub : notification.getSubject();
		
			Map<String, String> params = getCustomEmailParams(notification);
	
			params.put("@@CLIENTLOGO@@", sponserUser.getUserLogo());
			params.put("@@SPONSORNAME@@", sponserUser.getFirstLastName());
			
			params.put("@@FIRSTLASTNAMEPRO@@", user.getFirstLastName());
			params.put("@@EMAIL@@",user.getEmail());
			params.put("@@PASSWORD@@",userPwdTrack.getPassword());
			
			boolean sportsman = environmentUtil.containsEnv("sportsman");
			String discountCode = "";
			if(sportsman) {
				UserProAppDetailsSports userProAppDetails = userProAppDetailsSportsRepository.findByUserIdAndCompanyId(userId,sponserId);
				discountCode = userProAppDetails.getDiscountCode();
			
			} else {
				UserProAppDetails userProAppDetails = userProAppDetailsRepository.findByUserIdAndCompanyId(userId,sponserId);
				discountCode = userProAppDetails.getDiscountCode();
			}
			params.put("@@DISCOUNTCODE_STYLE@@","hidden");
			if(StringUtils.isNotBlank(discountCode)) {
				String discountLable = "<h4>Discount Code: "+discountCode+"</h4>";
				params.put("@@DISCOUNTCODE@@", discountLable);
				params.put("@@DISCOUNTCODE_STYLE@@","");
			}
			
			sendGridEmailService.sendEmailUsingTemplate(user.getEmail(), sendGridEmailService.getFromEmail(), subject,
					params, "templates/notify_welcome_emp_new_pro.html", sendGridEmailService.getEmail() );
			user.setStatus("Active");
			userRepository.save(user);
			
		} 
		catch (Exception e) {
			log.error("Cannot send email to {}: {}", user.getEmail(), e.getMessage(), e);
			success=false;
		}
		return success;
	}
	
	public void insertGloveSize() {
		if(UserUtils.getLoggedInUserId() != 428) {
			return;
		}

		String data[][] = new ExcelUtils().readExcel("glovesize.xlsx");
		if( data.length == 0 ) {
			return;
		}
		Map<Object, Object> dataMap = ArrayUtils.toMap(data);
		dataMap.remove("");
		
		log.info("======glove size map size : "+dataMap.size());
		for (Map.Entry<Object, Object> entry : dataMap.entrySet()) {
			try {
				String email = (String) entry.getKey();
				String gloveSize = dataMap.get(email) == null ? "" : (String) dataMap.get(email);
				
	            User user = findUserByEmail(email);
	            if(user != null) {	
	            
		            Long userId = user.getUserId();
		            
		            List<UserProAppDetails> userProAppDetails = userProAppDetailsRepository.findByUserId(userId);
		            if( userProAppDetails != null ) {
		            	for ( UserProAppDetails proApp : userProAppDetails) {
							proApp.setGlove_size( gloveSize );
						}
		            	log.info("======updating glove size for user id : " + userId);
			            userProAppDetailsRepository.saveAll(userProAppDetails);
		            }
	            }
			}
			catch(Exception e) {
				System.out.println(e.getMessage());				
			}
    	} 
	}
	
	public ByteArrayOutputStream getData(int companyId){

		List<Map<String,String>> dataList = new ArrayList<>();
		
		try {
			List<CompanyRef> compRefList = companyRefRepository.findByCompanyId(new Long(companyId));
			List<Long> userIdList = compRefList.stream().map(CompanyRef::getUserid).collect(Collectors.toList());
			List<User> list = userRepository.findByUserIdIn(userIdList); 
			//List<User> list = userRepository.findByUserIdIn(userIdList); //replace from step 3.
//replace from step 3.
			List<UserProAppDetails> appList = userProAppDetailsRepository.findByUserIdIn(userIdList);
			Map<Long, List<UserProAppDetails>> appUserMap = appList.stream().collect(Collectors.groupingBy(UserProAppDetails::getUserId));
			
			for (User user : list) {
				
				if(user.getStatus().equals("Active")) {
					CompanyRef compRef =null;
					try {
					 compRef = companyRefRepository.findByUseridAndCompanyId(user.getUserId(),new Long(companyId));
					}catch(Exception e) {
						e.printStackTrace();	
						}

				Long userId = user.getUserId();
				
				UserProAppDetails userProAppDetails = new UserProAppDetails();
				if(appUserMap.containsKey(userId)) {
					userProAppDetails = appUserMap.get(userId).get(0);
				}
				
				Map<String,String> map = new HashMap<>();
				map.put("name", user.getFirstLastName() );
				map.put("email", user.getEmail() );
				map.put("phone", user.getPhone() );
				map.put("address", user.getStreet() );
				map.put("city", user.getCity() );
				map.put("state", user.getState() );
				map.put("zip", user.getZipcode() );
				if(compRef != null && compRef.getApprovedDate()!= null) {
//					ZoneId z = ZoneId.of( "Asia/Tokyo");
//					ZoneId z = ZoneId.systemDefault();

//					ZonedDateTime zdt = (user.getRegistrationDate()).atZoneSameInstant(z);

//					DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy  z");

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
 					Calendar now = Calendar.getInstance();
					    
					TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
					//ZoneId z = ZoneId.of(timeZone.getDisplayName());
					ZonedDateTime zdt =  (compRef.getApprovedDate()).withZoneSameInstant(timeZone.toZoneId());
					String formatDateTime = zdt.format(formatter);
			        
					map.put("date", formatDateTime);
//					//
				}
				else {
					map.put("date", "" );
				}
				map.put("shirtSize", userProAppDetails.getShirt_size() );
				map.put("gloveSize", userProAppDetails.getGlove_size() );
				dataList.add(map);
				}
			}
//TODO remove
				insertGloveSize();

		}
		catch(Exception e) {
			e.printStackTrace();
		}


		return ExcelUtils.getExcelFile(dataList);
	}

	
	public SportsUserProAppDetails fetchApplicationSports(Long userId, Long companyId) {
		val application = new SportsUserProAppDetails();
		
		String requestUrl = "http://ambassadorlink.com/webservice/index.php/api/prostaff_ref_appprofile/"+userId+"/"+companyId+"/Hunting";
		application.setProfile("Hunting");
		String sports = userRepository.getSports(companyId);
		if(StringUtils.isNotBlank(sports)) {
			application.setProfile(sports);
			if(sports.equalsIgnoreCase("Boating")) {
				requestUrl = "http://ambassadorlink.com/webservice/index.php/api/prostaff_ref_appprofile/"+userId+"/"+companyId+"/Boating";
			}
			
		}
		
		HttpGet request = new HttpGet(requestUrl);
		
		 try (CloseableHttpResponse response = httpClient.execute(request)) {

	            HttpEntity entity = response.getEntity();

	            if (entity != null) {
	                String result = EntityUtils.toString(entity);
	                ObjectMapper mapper = new ObjectMapper();
	                
                	String firstLastname = "";
                	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
                	JsonNode jsondata = jsonNodeName.get("data");
                	for(int i=0; i<jsondata.size(); i++) {
	                	JsonNode userObj = jsondata.get(i);
	                    JsonNode firstLastnameObj = userObj.get("FirstLastname");
	                    firstLastname = firstLastnameObj.asText();
                	}	                
	                
	                ParentApplication jsonNode = mapper.readValue(result, ParentApplication.class);
	                if(jsonNode != null) {
	                	Application1 data = jsonNode.getData().get(0);
	                	
	                	application.setFirstLastName(firstLastname);
	                	application.setEmail(data.getEmail());
	                	try {
	                	LocalDate birthDate = null;
	                    if(StringUtils.isNotBlank(data.getDob())) {
	                    	birthDate = LocalDate.parse(data.getDob());
	                    }
	                    
	                	application.setDob(birthDate);
	                	}catch(Exception e) {
	                		e.printStackTrace();
	                	}
	                	application.setHighestEducation(data.getHighest_education());
	                	application.setStreet(StringUtils.isBlank(data.getStreet()) ? "" : data.getStreet());
	        			application.setCity(StringUtils.isBlank(data.getCity()) ? "" : data.getCity());
	        			application.setState(StringUtils.isBlank(data.getState()) ? "" : data.getState());
	        			application.setZipcode(StringUtils.isBlank(data.getZipcode()) ? "" : data.getZipcode());
	        			
	        			application.setEmail(StringUtils.isBlank(data.getEmail()) ? "" : data.getEmail());	
	        			application.setPhone(StringUtils.isBlank(data.getPhone()) ? "" : data.getPhone());					
	        			application.setSec_phone("");
	        			application.setShirt_size(StringUtils.isBlank(data.getShirt_size()) ? "" : data.getShirt_size());
	        			application.setAttach_resume(StringUtils.isBlank(data.getAttach_resume()) ? "" : data.getAttach_resume());
	        			
	        			application.setHunting_categories(data.getHunting_categories());
	        			application.setTarget_species(data.getTarget_species());
	        			application.setPrimary_hunting_method(data.getPrimary_hunting_method());
	        			application.setNum_days_peryear_field(data.getNum_days_peryear_field());
	        			application.setGuide(data.getGuide());
	        			application.setGuide_service_name(data.getGuide_service_name());
	        			application.setGuide_num_clients(data.getGuide_num_clients());
	        			application.setGuide_service_website(data.getGuide_service_website());
	        			application.setTv_involvement(data.getTv_involvement());
	        			application.setTv_shows_air(data.getTv_shows_air());
	        			application.setTv_shows_list(data.getTv_shows_list());
	        			application.setTv_show_air_quarter(data.getTv_show_air_quarter());
	        			application.setTv_avg_viewers(data.getTv_avg_viewers());
	        			
	        			application.setExp_sponsor_products(data.getExp_sponsor_products());
	        			application.setActivity_on_sponsor_behalf(data.getActivity_on_sponsor_behalf());
	        			
	        			application.setFb_page_link(data.getFb_page_link());
	        			application.setFb_num_followers(data.getFb_num_followers());
	        			application.setTwt_page_link(data.getTwt_page_link());
	        			application.setTwt_num_followers(data.getTwt_num_followers());
	        			application.setInsta_page_link(data.getInsta_page_link());
	        			application.setInsta_num_followers(data.getInsta_num_followers());
	        			application.setYt_channel_link(data.getYt_channel_link());
	        			application.setYt_num_subscribers(data.getYt_num_subscribers());
	        			application.setForum_details(data.getForum_details());
	        			
	        			application.setAdditionals_sponsors(data.getAdditionals_sponsors());
	        			application.setWhy_to_select(data.getWhy_to_select());
	        			application.setPrefer_brands(data.getPrefer_brands());
	        			application.setOrganization_list(data.getOrganization_list());
	        			application.setAttach_resume(data.getAttach_resume());
	        			
	        			application.setBoating_categories(data.getBoating_categories());
	        			application.setBoat_brands(data.getBoat_brands());
	        			application.setNum_days_peryear_field(data.getNum_days_peryear_field());
	        			application.setBoat_freshwaterSaltwater(data.getBoat_freshwaterSaltwater());
	        			application.setBoat_homeBodywater(data.getBoat_homeBodywater());
	        			application.setTv_involvement(data.getTv_involvement());
	        			application.setForum_posting(data.getForum_posting());
	        			application.setPhotoVideoEquipment(data.getPhotoVideoEquipment());
	        			application.setExp_photoVideoEditing_soft(data.getExp_photoVideoEditing_soft());
	                }
	                
	            }

	        } catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		return application;
	}
	
	public Application fetchApplication(Long userId, Long companyId) {
		val application = new Application();
		
		String requestUrl = "http://ambassadorlink.com/webservice/index.php/api/prostaff_ref_appprofile/"+userId+"/"+companyId+"/Fishing";
		
		HttpGet request = new HttpGet(requestUrl);
		
		 try (CloseableHttpResponse response = httpClient.execute(request)) {

	            HttpEntity entity = response.getEntity();

	            if (entity != null) {
	                String result = EntityUtils.toString(entity);
	                ObjectMapper mapper = new ObjectMapper();
	                
	                	String firstLastname = "";
	                	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
	                	JsonNode jsondata = jsonNodeName.get("data");
	                	for(int i=0; i<jsondata.size(); i++) {
		                	JsonNode userObj = jsondata.get(i);
		                    JsonNode firstLastnameObj = userObj.get("FirstLastname");
		                    firstLastname = firstLastnameObj.asText();
	                	}
	                
	                
	                ParentApplication jsonNode = mapper.readValue(result, ParentApplication.class);
	                if(jsonNode != null) {
	                	Application1 data = jsonNode.getData().get(0);
	                	
	                	application.setFirstLastName(firstLastname);
	                	application.setEmail(data.getEmail());
	                	application.setHighestEducation(data.getHighesteducation());
	                	application.setStreet(StringUtils.isBlank(data.getStreet()) ? "" : data.getStreet());
	        			application.setCity(StringUtils.isBlank(data.getCity()) ? "" : data.getCity());
	        			application.setState(StringUtils.isBlank(data.getState()) ? "" : data.getState());
	        			application.setZipcode(StringUtils.isBlank(data.getZipcode()) ? "" : data.getZipcode());
	        			
	        			application.setShip_street(StringUtils.isBlank(data.getZipcode()) ? "" : data.getZipcode());
	        			application.setShip_city("");
	        			application.setShip_state("");
	        			application.setShip_zipcode("");
	        			
	        			application.setEmail(StringUtils.isBlank(data.getEmail()) ? "" : data.getEmail());	
	        			application.setPhone(StringUtils.isBlank(data.getPhone()) ? "" : data.getPhone());					
	        			application.setSec_phone("");
	        			
	        			application.setReferences_1("");
	        			application.setReferences_2("");
	        			application.setReferences_3("");
	        			
	        			application.setShirt_size(StringUtils.isBlank(data.getShirt_size()) ? "" : data.getShirt_size());
	        			application.setGlove_size(StringUtils.isBlank(data.getGlove_size()) ? "" : data.getGlove_size());
	        			application.setPant_size("");
	        			application.setPrefer_hardware("");
	        			
	        			application.setHome_lake_river(StringUtils.isBlank(data.getHome_lake_river()) ? "" : data.getHome_lake_river());
	        			application.setSpecies_type(StringUtils.isBlank(data.getSpecies_type()) ? "" : data.getSpecies_type());
	        			application.setTournament_fish_year(StringUtils.isBlank(data.getTournament_fish_year()) ? "" : data.getTournament_fish_year());
	        			application.setIsfish_tournament_trails(StringUtils.isBlank(data.getIsfish_tournament_trails()) ? "" : data.getIsfish_tournament_trails());
	        			application.setWater_type("");
	        			application.setIsguide(StringUtils.isBlank(data.getIsguide()) ? "" : data.getIsguide());
	        			application.setIsguidelicence(StringUtils.isBlank(data.getIsguide()) ? "" : data.getIsguide());
	        			application.setLicense_number("");
	        			application.setGuideyear("");
	        			application.setIswebsite("");
	        			application.setFlyfishingcareer_1("");
	        			application.setFlyfishingcareer_2("");
	        			
	        			application.setIsusedproducts(StringUtils.isBlank(data.getIsusedproducts()) ? "" : data.getIsusedproducts());
	        			application.setUseproducts(StringUtils.isBlank(data.getUseproducts()) ? "" : data.getUseproducts());
	        			application.setExperiencewithproducts(StringUtils.isBlank(data.getExperiencewithproducts()) ? "" : data.getExperiencewithproducts());
	        			
	        			application.setSocial_media_platform("");
	        			application.setFacebook_personal_page("");
	        			application.setLink_to_facebook_fan_page(StringUtils.isBlank(data.getLink_to_facebook_fan_page()) ? "" : data.getLink_to_facebook_fan_page());
	        			application.setTwitter_personal_page("");
	        			application.setLink_to_twitter_personal_page("");
	        			application.setLink_to_twitter_fan_page(StringUtils.isBlank(data.getTwt_num_personalpage_followers()) ? "" : data.getTwt_num_personalpage_followers());
	        			application.setLink_to_instagram_personal_page(StringUtils.isBlank(data.getInsta_num_personalpage_followers()) ? "" : data.getInsta_num_personalpage_followers());
	        			application.setInstagram_fan_page("");
	        			application.setIsactive_on_forum_blog(StringUtils.isBlank(data.getIsactive_on_forum_blog()) ? "" : data.getIsactive_on_forum_blog());
	        			application.setActive_on_fishing_blog_1("");
	        			application.setActive_on_fishing_blog_2("");
	        			application.setActive_on_fishing_blog_3("");
	        			application.setActive_on_fishing_blog_4("");
	        			application.setActive_on_fishing_blog_5("");
	        			application.setActive_on_fishing_blog_6("");
	        			application.setActive_on_fishing_blog_7("");
	        			application.setActive_on_fishing_blog_8("");
	        			application.setActive_on_fishing_blog_9("");
	        			application.setActive_on_fishing_blog_10("");
	        			application.setActive_on_fishing_blog_11("");
	        			application.setActive_on_fishing_blog_12("");
	        			
	        			application.setOther_sposors_1(StringUtils.isBlank(data.getOther_sposors_1()) ? "" : data.getOther_sposors_1());
	        			application.setOther_sposors_2("");
	        			application.setOther_sposors_3("");
	        			application.setOther_sposors_4("");
	        			application.setOther_sposors_5("");
	        			application.setOther_sposors_6("");
	        			application.setOther_sposors_7("");
	        			application.setOther_sposors_8("");
	        			application.setOther_sposors_9("");
	        			application.setOther_sposors_10("");
	        			application.setOther_sposors_11("");
	        			application.setOther_sposors_12("");
	        			application.setSelect_fishing_pro_staff(StringUtils.isBlank(data.getSelect_fishing_pro_staff()) ? "" : data.getSelect_fishing_pro_staff());
	        			application.setOther_information(StringUtils.isBlank(data.getOther_information()) ? "" : data.getOther_information());
	        			application.setAttach_resume(StringUtils.isBlank(data.getAttach_resume()) ? "" : data.getAttach_resume());
	        			application.setUpload_pictures(StringUtils.isBlank(data.getUpload_pictures()) ? "" : data.getUpload_pictures());
	        			application.setLink_to_facebook_personal_page(StringUtils.isBlank(data.getLink_to_facebook_personal_page()) ? "" : data.getLink_to_facebook_personal_page());
	                }
	                
	            }

	        } catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		return application;
	}
	
	public List<User> getApplicationList(String profile, Long companyId) {
		
		List<User> ambassadorUserList = new ArrayList<User>();
		

		String ambassadorlinkUrl = "http://ambassadorlink.com/webservice/index.php/api/verified_pro_apps_for_sponsor/"+companyId+"/Fishing";
		
		if(profile.equalsIgnoreCase("sports")) {
			String sports = userRepository.getSports(companyId);
			
			if(StringUtils.isNotBlank(sports)) {
				//TODO: get rid of all if's with 2 lines below
				sports = StringUtils.capitalize( sports.toLowerCase() );
				ambassadorlinkUrl = "http://ambassadorlink.com/webservice/index.php/api/verified_pro_apps_for_sponsor/"+companyId+"/" + sports;
				
			}
		}
		
		HttpGet request = new HttpGet(ambassadorlinkUrl);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
           // Header headers = entity.getContentType();
            //System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readValue(result, JsonNode.class);
                
                JsonNode data = jsonNode.get("data");
                
                for(int i=0; i<data.size(); i++) {
                	JsonNode userObj = data.get(i);
                    JsonNode userIdObj = userObj.get("userid");
                    if(userIdObj == null) {
                    	userIdObj = userObj.get("userID");
                    }
                    Long userId = userIdObj.asLong();
                    
                    JsonNode firstLastnameObj = userObj.get("FirstLastname");
                    String firstLastname = firstLastnameObj.asText();
                    
                    JsonNode emailObj = userObj.get("email");
                    String email = emailObj.asText();
                    
                    JsonNode phoneObj = userObj.get("phone");
                    String phone = phoneObj.asText();
                    
                    JsonNode dateObj = userObj.get("posted_date");
                    String posted_date = dateObj.asText();
                    LocalDate registrationDate = null;
                    if(StringUtils.isNotBlank(posted_date)) {
                    	String[] dates = posted_date.split(" ");
                    	posted_date = dates != null ? dates[0] : " " ;
                    	registrationDate = LocalDate.parse(posted_date);
                    }
                    
                    JsonNode statusObj = userObj.get("ref_status");
                    String status = statusObj.asText();
                    
                    
                    User application = new User();
                    application.setUserId(userId);
                    application.setFirstLastName(firstLastname);
                    application.setPhone(phone);
                    application.setRegistrationDate(registrationDate);
                    application.setEmail(email);
                    application.setMemType("ambassadorlink");
                    application.setStatus(status);
                    
                    ambassadorUserList.add(application);
                }
                
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        List<User> applicationList = userRepository.getApplicationList(profile, companyId, null);
        
        applicationList.addAll(ambassadorUserList);
        
//      List<User> finalList = new ArrayList<User>();
//        for(User user : applicationList) {
//    		
//        	if(user.getStatus() != null && user.getStatus().equalsIgnoreCase("Pending")) {
//        		finalList.add(user);
//        	}
//        }
		return applicationList;
	}
	
	public List<User> getApplicationList(String profile, Long companyId, PaginationRequestDTO dto) {
		
		List<User> ambassadorUserList = new ArrayList<User>();
		
		String ambassadorlinkUrl = "http://ambassadorlink.com/webservice/index.php/api/verified_pro_apps_for_sponsor/"+companyId+"/Fishing";
		
		if(profile.equalsIgnoreCase("sports")) {
			String sports = userRepository.getSports(companyId);
			
			if(StringUtils.isNotBlank(sports)) {
				//TODO: get rid of all if's with 2 lines below
				sports = StringUtils.capitalize( sports.toLowerCase() );
				ambassadorlinkUrl = "http://ambassadorlink.com/webservice/index.php/api/verified_pro_apps_for_sponsor/"+companyId+"/" + sports;
			}
		}
		
		HttpGet request = new HttpGet(ambassadorlinkUrl);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            //Header headers = entity.getContentType();
            //System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readValue(result, JsonNode.class);
                
                JsonNode data = jsonNode.get("data");
                
                for(int i=0; i<data.size(); i++) {
                	JsonNode userObj = data.get(i);
                    JsonNode userIdObj = userObj.get("userid");
                    if(userIdObj == null) {
                    	userIdObj = userObj.get("userID");
                    }
                    Long userId = userIdObj.asLong();
                    
                    JsonNode firstLastnameObj = userObj.get("FirstLastname");
                    String firstLastname = firstLastnameObj.asText();
                    
                    JsonNode emailObj = userObj.get("email");
                    String email = emailObj.asText();
                    
                    JsonNode phoneObj = userObj.get("phone");
                    String phone = phoneObj.asText();
                    
                    JsonNode dateObj = userObj.get("posted_date");
                    String posted_date = dateObj.asText();
                    LocalDate registrationDate = null;
                    if(StringUtils.isNotBlank(posted_date)) {
                    	String[] dates = posted_date.split(" ");
                    	posted_date = dates != null ? dates[0] : " " ;
                    	registrationDate = LocalDate.parse(posted_date);
                    }
                    
                    JsonNode statusObj = userObj.get("ref_status");
                    String status = statusObj.asText();
                    
                    
                    User application = new User();
                    application.setUserId(userId);
                    application.setFirstLastName(firstLastname);
                    application.setPhone(phone);
                    application.setRegistrationDate(registrationDate);
                    application.setEmail(email);
                    application.setMemType("ambassadorlink");
                    application.setStatus(status);
                    
                    ambassadorUserList.add(application);
                }
                
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        List<User> finalList = new ArrayList<User>();
        
        //TODO: Use projections with jpa
        // Get all records from user_pro_app_details where status='Pending' OR status='Rejected' and companyId= loggedin userid
        // Get all from users where userid in ( all userids from above query )
        List<User> applicationList = userRepository.getApplicationList(profile, companyId, null);
        
        if(applicationList != null && applicationList.size() > 0) {
        	finalList.addAll(applicationList);
        }
        
        if(ambassadorUserList != null && ambassadorUserList.size() > 0) {
        	finalList.addAll(ambassadorUserList);
        }
        
        for(User user : finalList) {
        	if(user.getStatus() != null && user.getStatus().equalsIgnoreCase("Rejected")) {
        		user.setStatus("ZRejected");
        	}
        }
        
        List<User> sortedList = new ArrayList<User>();
        
        if(dto != null && StringUtils.isNotBlank(dto.getSort())) {
        	
        	if(dto.getSort().equalsIgnoreCase("firstLastName")) {
        		
        		if(dto.getDirection().equalsIgnoreCase("asc")) {        			
        			sortedList = finalList.stream()
        	        		.sorted(Comparator.comparing(User::getFirstLastName))
        	        		.collect(Collectors.toList());
        		}else {
        			sortedList = finalList.stream()
        	        		.sorted(Comparator.comparing(User::getFirstLastName).reversed())
        	        		.collect(Collectors.toList());
        		}
        	}
        	
        	if(dto.getSort().equalsIgnoreCase("email")) {
        		
        		if(dto.getDirection().equalsIgnoreCase("asc")) {        			
        			sortedList = finalList.stream()
        	        		.sorted(Comparator.comparing(User::getEmail))
        	        		.collect(Collectors.toList());
        		}else {
        			sortedList = finalList.stream()
        	        		.sorted(Comparator.comparing(User::getEmail).reversed())
        	        		.collect(Collectors.toList());
        		}
        	}
        	
        	if(dto.getSort().equalsIgnoreCase("phone")) {
        		
        		if(dto.getDirection().equalsIgnoreCase("asc")) {        			
        			sortedList = finalList.stream()
        	        		.sorted(Comparator.comparing(User::getPhone))
        	        		.collect(Collectors.toList());
        		}else {
        			sortedList = finalList.stream()
        	        		.sorted(Comparator.comparing(User::getPhone).reversed())
        	        		.collect(Collectors.toList());
        		}
        	}
        	
        	if(dto.getSort().equalsIgnoreCase("status")) {
        		
        		if(dto.getDirection().equalsIgnoreCase("asc")) {        			
        			sortedList = finalList.stream()
        	        		.sorted(Comparator.comparing(User::getStatus))
        	        		.collect(Collectors.toList());
        		}else {
        			sortedList = finalList.stream()
        	        		.sorted(Comparator.comparing(User::getStatus).reversed())
        	        		.collect(Collectors.toList());
        		}
        	}

			if(dto.getSort().equalsIgnoreCase("registrationDate")) {
				
				if(dto.getDirection().equalsIgnoreCase("asc")) {        			
					sortedList = finalList.stream()
			        		.sorted(Comparator.comparing(User::getRegistrationDate))
			        		.collect(Collectors.toList());
				}else {
					sortedList = finalList.stream()
			        		.sorted(Comparator.comparing(User::getRegistrationDate).reversed())
			        		.collect(Collectors.toList());
				}
			}
			
			
        }
        
        
		/*
		 * for(User application : applicationList) { for(Long userId :
		 * ambassadorUserList) { if (application.getUserId() == userId) {
		 * application.setStatus("Verified"); } } }
		 */
        
		return sortedList;
	}
	
	public int getApplicationListCount(String profile, Long companyId, PaginationRequestDTO dto){
		return userRepository.getApplicationListCount(profile, companyId, dto);
	}
	
	public Application fetchApplicationDetail(Long staffId, Long companyId, String applicationFrom ) {
		Application application = new Application();
		
		if(StringUtils.isNotBlank(applicationFrom) && applicationFrom.equalsIgnoreCase("ambassadorlink")){
    		application = fetchApplication(staffId, companyId);
    	}else {
    		application = applicationRepository.fetchApplication(staffId, companyId);
    	}
		
		if (StringUtils.isNotBlank(application.getLink_to_instagram_personal_page())) {
			if (application.getLink_to_instagram_personal_page().indexOf("https://") == -1) {
				application.setLink_to_instagram_personal_page(
						"http://" + application.getLink_to_instagram_personal_page());
			}
		}
		
		if (StringUtils.isNotBlank(application.getLink_to_facebook_personal_page())) {
			if (application.getLink_to_facebook_personal_page().indexOf("https://") == -1) {
				application.setLink_to_facebook_personal_page(
						"http://" + application.getLink_to_facebook_personal_page());
			}
		}
		
		if (StringUtils.isBlank(application.getInstagram_fan_page())
				|| application.getInstagram_fan_page().indexOf("https://") == -1) {
			application.setInstagram_fan_page("N/A");
		}
		
		return application;
	}
	
	public List<User> findAllUsersByCompany(String name, String email,final PaginationRequestDTO dto, Boolean count) {
		
		List<User> userList = userRepository.findAllUsersByCompany(name, email, dto, count);
		
		if(count) {
			return userList;
		}
		
		List<Long> userIDs = userList.stream().map(User::getUserId).collect(Collectors.toList());
//		List<LinkedSocialMedia> userLinkedMedia = mediaRepo.findByUseridIn(userIDs);
//		Map<Long, List<LinkedSocialMedia>> userMediaMap = userLinkedMedia == null ? null : userLinkedMedia.stream().collect( Collectors.groupingBy( LinkedSocialMedia:: getUserid ) );
		List<UserMedia> userLinkedMedia = userMediaRepo.findByUseridIn(userIDs);
		Map<Long, List<UserMedia>> userMediaMap = userLinkedMedia == null ? null : userLinkedMedia.stream().collect( Collectors.groupingBy( UserMedia:: getUserid ) );

		
		List<Long> sponserTeamList = userRepository.fetchSponserTeamByUserId(UserUtils.getLoggedInUserId());

		for(User user : userList) {
			String[] teams = new String[0];
			String[] teamNames = new String[0];
			List<UserTeam> lstTeam = userRepository.fetchTeamByUserId(user.getUserId(),sponserTeamList);
			if(lstTeam != null && lstTeam.size() > 0) {
				teamNames = new String[lstTeam.size()];
				int index = 0;
				for(UserTeam team : lstTeam) {
					teamNames[index] = team.getTeamName();
					index++;
				}
			}
			///
			try {
			if( userMediaMap != null ) {
				List<UserMedia> userLinkedMedia1  = userMediaMap.get(user.getUserId());
				if(userLinkedMedia1!= null) {
				List<LinkedSocialMedia> duplicateuserLinkedMedia = new ArrayList();
				for(int index= 0;index<userLinkedMedia1.size();index++) {
					LinkedSocialMedia linkedSocialMedia = new LinkedSocialMedia();
					linkedSocialMedia.setSocialId(userLinkedMedia1.get(index).getSociald());
					linkedSocialMedia.setSocialType(userLinkedMedia1.get(index).getSocialType());
					linkedSocialMedia.setUserid(userLinkedMedia1.get(index)	.getUserid());
					duplicateuserLinkedMedia.add(linkedSocialMedia);
					}
				
				
				user.setMediaList( duplicateuserLinkedMedia );
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
				}
			
			user.setUserTeam(teams);
			user.setUserTeamName(teamNames);
			String teamNamesSort = String.join(",", teamNames);
			
			user.setTeamArray(teamNamesSort);
		}
		
		
		
		if(dto != null && StringUtils.isNotBlank(dto.getSort())) {
			if(dto.getSort().equalsIgnoreCase("userTeamName")) {
				List<User> sortedList = new ArrayList<User>();
				if(dto.getDirection().equalsIgnoreCase("asc")) {
					sortedList = userList.stream()
			        		.sorted(Comparator.comparing(User::getTeamArray))
			        		.collect(Collectors.toList());
				}else {
					sortedList = userList.stream()
			        		.sorted(Comparator.comparing(User::getTeamArray)
			        		.reversed())
			        		.collect(Collectors.toList());
				}
				
				return sortedList;
			}
		}
		
		return userList;
	} 
	
	
	public User findUserById(final Long userId) {
		
		log.debug("Finding the user by id:" + userId);
		
		if(userId == null) {
			throw new UserException("Please provide user id");
		}
		
		val user = userRepository.findByUserId(userId);
		user.setFirstLastName(user.getFirstLastName());
		
		if(user == null) {
			throw new UserException("No user found by this id");
		}
		
		user.setFirstLastName(user.getFirstLastName());
		
		/* if(! UserUtils.getLoggedInUserId().equals(userId)) {
			if(! UserUtils.isAdminUser()) {
				throw new UserException("You don't have permission to view this user");
			} 
		} */
		
		return user;
	}
	

	public User findUserByEmail(final String email) {
		if(StringUtils.isBlank(email)) {
			throw new UserException("Invalid user email provided.");
		}

		return userRepository.findByEmail(email);
	} 

	public String getKeywords(User user, String media) {
		
		if(StringUtils.isBlank(media)) {
			throw new UserException("Please provide the valid media type.");
		}
		
		//User user = findUserById(userId);
		
		@SuppressWarnings("unused")
		List<String> keywords  = new ArrayList<>();
		
		String keyword = "";
		
		if(user != null) {
			if(media.equalsIgnoreCase("fbkeywords")) {
				keyword = user.getFbKeywords() == null ? "" : user.getFbKeywords().replaceAll(",", "|");
				
				if(StringUtils.isNotBlank(media)) {
					keywords = Arrays.asList( keyword.split("|"));
				}
			}
			
			if(media.equalsIgnoreCase("instakeywords")) {
				keyword = user.getInstaKeywords() == null ? "" : user.getInstaKeywords().replaceAll(",", "|");
				
				if(StringUtils.isNotBlank(media)) {
					keywords = Arrays.asList( keyword.split(","));
				}
			}
			
			if(media.equalsIgnoreCase("twtkeywords")) {
				keyword = user.getTwtKeywords() == null ? "" : user.getTwtKeywords().replaceAll(",", "|");
				
				if(StringUtils.isNotBlank(media)) {
					keywords = Arrays.asList( keyword.split(","));
				}
			}
			
			if(media.equalsIgnoreCase("youtubekeywords")) {
				keyword = user.getYoutubeKeywords() == null ? "" : user.getYoutubeKeywords().replaceAll(",", "|");
				
				if(StringUtils.isNotBlank(media)) {
					keywords = Arrays.asList( keyword.split(","));
				}
			}
			
		}
		
		return keyword;
	}
	
	
	public User findUserByResetPasswordToken(final String token) {
		if(StringUtils.isBlank(token)) {
			throw new UserException("Please provide the valid token.");
		}

		final User user = userRepository.findByPasswordResetToken(token);

		if(user == null) {
			throw new UserException("The link has expired. Please generate a new link.");
		} 

		if(user.getPasswordResetTokenExpireTime().compareTo(ZonedDateTime.now()) <= 0) {
			throw new UserException("The link has expired. Please generate a new link.");
		}

		return user;
	}  

	public User resetPasswordByToken(final CreateUserDTO resetPasswordUser) {

		User user = findUserByResetPasswordToken(resetPasswordUser.getEmail());

		validatePassword(resetPasswordUser.getPassword(), resetPasswordUser.getConfirmPassword());

		user.setPassword(PasswordUtil.encryptPassword(resetPasswordUser.getPassword()));
		user.setPasswordResetToken(null);
		user.setPasswordResetTokenExpireTime(null);

		user = userRepository.save(user);

		return user;
	} 
	
	private void validateUser(User user) {

		if (StringUtils.isBlank(user.getEmail())) {
			throw new UserException("Email address can not be empty.");
		}

		/*
		 * if(! EmailValidator.getInstance().isValid(user.getEmail())) { throw new
		 * UserException("Invalid email address provided."); }
		 */

		if (StringUtils.isBlank(user.getFirstName())) {
			throw new UserException("First name can not be empty.");
		}

//		if (StringUtils.isBlank(user.getLastName())) {
//			throw new UserException("Last name can not be empty.");
//		}
		
		if (StringUtils.isBlank(user.getPhone())) {
			throw new UserException("Cell Phone can not be empty.");
		}
	}
	
	public void importUsers(String users[][]) {
			List<String> headerList = new ArrayList<>();
			
			for(int i=0; i<users[0].length; i++) {
				headerList.add(users[0][i]);
			}
			
			List<User> userList = new ArrayList<>();
			for(int i = 1; i<users.length; i++) {
				User usr = new User();
				usr.setFirstName(users[i][0]);
				usr.setLastName(users[i][1]);
				usr.setEmail(users[i][2]);
				usr.setPhone(users[i][3]);
				usr.setStreet(users[i][4]);
				usr.setCity(users[i][5]);
				usr.setState(users[i][6]);
				usr.setZipcode(users[i][7]);

				String[] activeProfiles = environment.getActiveProfiles();  
				boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
				userList.add(findUserById(createUser(usr,result).getUserId()));
			}
	}

	public User createUser(User userDTO, boolean result) {
		validateUser(userDTO);
		// Logic
		// Set boolean existingUser = false;
		// -Check if user by same email exist in User table, if yes - means existing
		// user
		// If user DO NOT exist in "user" table
		// set existingUser = true;(false)
		// Create user object with form data and SAVE.
		// UserPwdTrack
		// END IF
		// Get the user_ID from user object.
		// Check in Company_REF table by user_id and company_id to see if user exist
		// IF DOES NOT EXIST in Company_REF table
		// Create new CompanyRef object and insert.
		// END IF
		// Check UserProAppDetails by userID and companyID
		// IF DOES NOT EXIST in UserProAppDetails table
		// Create UserProAppDetails object and insert record.
		// END IF
		// IF existingUser , send existing user email template
		// ELSE send new user welcome email
		// Send back User object always so UI can show success.

		boolean existingUser = false;
		User dbUser = userRepository.findByEmail(userDTO.getEmail());
		if(dbUser != null) {//user exist for other sponser.
			if ( dbUser.getStatus() == "Active") {
				existingUser  = true;
			}
		}
		else {// brand new user.
			String pass = PasswordUtil.generatePassword(5); //RAW password
			userDTO.setPassword(PasswordUtil.encryptPassword(pass));
			userDTO.setPasswordResetToken(null);
			userDTO.setPasswordResetTokenExpireTime(null);
			userDTO.setFirstLastName(userDTO.getFirstName() + " " + userDTO.getLastName());
			userDTO.setCompName("");
			userDTO.setLoginName(userDTO.getEmail());
			userDTO.setMemType("Staff");
			userDTO.setStatus("Active");
			userDTO.setRegistrationDate(LocalDate.now());

			User loggedInUser = UserUtils.getLoggedInUser();
			userDTO.setFbKeywords(loggedInUser.getFbKeywords());
			userDTO.setInstaKeywords(loggedInUser.getInstaKeywords());
			userDTO.setTwtKeywords(loggedInUser.getTwtKeywords());
			userDTO.setYoutubeKeywords(loggedInUser.getYoutubeKeywords());

			dbUser = userRepository.save(userDTO);

			UserPwdTrack upt = new UserPwdTrack();
			upt.setUserId(dbUser.getUserId());
			upt.setEmail(dbUser.getEmail());
			upt.setFromPage("newuser added by sponsor - add user");
			upt.setPassword(pass);
			upt.setDateUpdate(LocalDate.now());
			// Delete any data for this email from password table first
			userPwdTrackRepository.deleteByEmail(dbUser.getEmail());
			userPwdTrackRepository.save(upt);
		}
		Long userId = dbUser.getUserId();
		CompanyRef companyRef =null;
		try {
		 companyRef = companyRefRepository.findByUseridAndCompanyId(userId, UserUtils.getLoggedInUserId());
		}catch(Exception e) {
			e.printStackTrace();
			}
		if (companyRef == null) {// user does not exist for current sponser
			companyRef = getCompanyRef(userId, UserUtils.getLoggedInUser());
		}
//		 Date date = new Date();  
//		    System.out.println("check: "+  date.getHours());
		companyRef.setRefStatus("Active");
		
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.systemDefault());
		companyRef.setApprovedDate(zdt);

		companyRefRepository.save(companyRef);

		Application1 appData = userDTO.getApplicationData();
		if (result) {
			UserProAppDetailsSports proApp = userProAppDetailsSportsRepository.findByUserIdAndCompanyId(userId,
					UserUtils.getLoggedInUserId());
			if (proApp == null) {

				if (appData != null) {
					proApp = populateSportsUserProAppDetails(userDTO.getDiscountCode(), proApp, appData);
				} else {
					proApp = new UserProAppDetailsSports();
				}
			}
			proApp.setUserId(dbUser.getUserId());
			proApp.setCompanyId(UserUtils.getLoggedInUserId());
			proApp.setDiscountCode(userDTO.getDiscountCode());
			proApp.setStatus("Approved");
			proApp.setPosted_date(LocalDate.now());

			userProAppDetailsSportsRepository.save(proApp);

		} else {
			UserProAppDetails userProAppDetails = userProAppDetailsRepository.findByUserIdAndCompanyId(userId,
					UserUtils.getLoggedInUserId());
			if (userProAppDetails == null) {

				if (appData != null) {
					userProAppDetails = populateUserProAppDetails(userDTO.getDiscountCode(), userProAppDetails,
							appData);
				} else {
					userProAppDetails = new UserProAppDetails();
				}
			}
			userProAppDetails.setUserId(dbUser.getUserId());
			userProAppDetails.setCompanyId(UserUtils.getLoggedInUserId());
			userProAppDetails.setDiscountCode(userDTO.getDiscountCode());
			userProAppDetails.setStatus("Approved");
			userProAppDetails.setRegistrationDate(LocalDate.now());

			userProAppDetailsRepository.save(userProAppDetails);

		}

		String teamArray = dbUser.getTeamArray();
		if (StringUtils.isNotBlank(teamArray)) {
			String teams[] = teamArray.split(",");
			for (String team : teams) {
				if (StringUtils.isNotBlank(team)) {
					UserTeam ut = new UserTeam();
					ut.setTeamId(Long.parseLong(team));
					ut.setUserId(dbUser.getUserId());
					userTeamRepository.save(ut);
				}
			}
		}

		dbUser.setDiscountCode(userDTO.getDiscountCode());
		final User usr = dbUser;
		final boolean existing = existingUser;
//		Executors.newSingleThreadExecutor()
//			.submit(() -> sendNotifyEmpAcceptedPendingProEmail(usr, UserUtils.getLoggedInUser(), existing));
		sendNotifyEmpAcceptedPendingProEmail(usr, UserUtils.getLoggedInUser(), existing);
		return dbUser;
	}

	public User updateUser(User user, boolean result) {
		
		if(user.getUserId() == null) {
			throw new UserException("Please provide user id to be updated");
		}

		validateUser(user); 
		
		//Please do not remove ..This will validate if user has proper access to update the user.
		this.findUserById(user.getUserId());
 
		final User dbIdUser = userRepository.getOne(user.getUserId());
		if(dbIdUser == null) {
			throw new UserException("No user found by this id.");
		} 

		final User dbEmailUser = userRepository.findByEmail(user.getEmail());
		if(dbEmailUser != null && !dbEmailUser.getUserId().equals(user.getUserId())) {
			throw new UserException("This email is already taken by another user");
		}
		
		dbIdUser.setFirstLastName(user.getFirstName()+" "+user.getLastName());
		dbIdUser.setState(user.getState());
		dbIdUser.setCompName("");
		dbIdUser.setLoginName(dbIdUser.getEmail());
		dbIdUser.setMemType("Staff");
		dbIdUser.setStatus("Active");
		dbIdUser.setPhone(user.getPhone());
		dbIdUser.setRegistrationDate(LocalDate.now());
		dbIdUser.setDiscountCode(user.getDiscountCode());


		userRepository.save(dbIdUser);
		
		userTeamRepository.deleteByUserId(user.getUserId());
		
		if(dbIdUser.getDiscountCode()!= null && !dbIdUser.getDiscountCode().isEmpty()) {
		if(result) {
			UserProAppDetailsSports userProAppDetails = null;
			try {
			 userProAppDetails = userProAppDetailsSportsRepository.findByUserIdAndCompanyId(dbIdUser.getUserId()
					,UserUtils.getLoggedInUserId());
			}catch(Exception e) {
				e.printStackTrace();		}
			if (userProAppDetails == null) {

				userProAppDetails = new UserProAppDetailsSports();

			}
			userProAppDetails.setUserId(dbIdUser.getUserId());
			userProAppDetails.setCompanyId(UserUtils.getLoggedInUserId());
			userProAppDetails.setDiscountCode(dbIdUser.getDiscountCode());
			userProAppDetails.setStatus("Approved");
			//userProAppDetails.setRegistrationDate(LocalDate.now());

			userProAppDetailsSportsRepository.save(userProAppDetails);
		}else {
		UserProAppDetails userProAppDetails = null;
		try {
		 userProAppDetails = userProAppDetailsRepository.findByUserIdAndCompanyId(dbIdUser.getUserId()
				,UserUtils.getLoggedInUserId());
		}catch(Exception e) {
			e.printStackTrace();		}
		if (userProAppDetails == null) {

			userProAppDetails = new UserProAppDetails();

		}
		userProAppDetails.setUserId(dbIdUser.getUserId());
		userProAppDetails.setCompanyId(UserUtils.getLoggedInUserId());
		userProAppDetails.setDiscountCode(dbIdUser.getDiscountCode());
		userProAppDetails.setStatus("Approved");
		userProAppDetails.setRegistrationDate(LocalDate.now());

		userProAppDetailsRepository.save(userProAppDetails);
		}
		}
		String teamArray = user.getTeamArray();
		if(StringUtils.isNotBlank(teamArray)) {
			String teams[] = teamArray.split(",");
			for(String team : teams) {
				if(StringUtils.isNotBlank(team)) {
					UserTeam ut = new UserTeam();
					ut.setTeamId(Long.parseLong(team));
					ut.setUserId(user.getUserId());
					userTeamRepository.save(ut);
				}
			}
		}
		
		return findUserById(dbIdUser.getUserId());
	}
	
	public void updatePromoCode(List<Long> ids, Long companyId, String promoCode) {
		String[] activeProfiles = environment.getActiveProfiles();  
		boolean sportProfile = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
		
		for(Long id : ids) {
			if( sportProfile ) {
				UserProAppDetailsSports upad = userProAppDetailsSportsRepository.findByUserIdAndCompanyId(id, companyId);
				if(upad != null) {
					upad.setDiscountCode(promoCode);
					userProAppDetailsSportsRepository.save(upad);
				}
			}else {
				UserProAppDetails upad = userProAppDetailsRepository.findByUserIdAndCompanyId(id, companyId);
				if(upad != null) {
					upad.setDiscountCode(promoCode);
					userProAppDetailsRepository.save(upad);
				}	
			}
		}
	}
		
		public void updatePromoCode1(List<Long> ids, Long companyId, String promoCode) {
			String[] activeProfiles = environment.getActiveProfiles();  
			boolean sportProfile = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
			
			for(Long id : ids) {
				if( sportProfile ) {
					UserProAppDetailsSports upad = userProAppDetailsSportsRepository.findByUserIdAndCompanyId(id, companyId);
					if(upad != null) {
						upad.setDiscountCode(promoCode);
						userProAppDetailsSportsRepository.save(upad);
					}
				}else {
					UserProAppDetails upad = userProAppDetailsRepository.findByUserIdAndCompanyId(id, companyId);
					if(upad != null) {
						upad.setDiscountCode(promoCode);
						userProAppDetailsRepository.save(upad);
					}	
				}
			}
	}
	
	public User updateUserLocation(User user) throws IOException {
		
		String address = user.getCity()+"+"+user.getState()+"+"+user.getZipcode();
		
		if(StringUtils.isBlank(address)) {
			return user;
		}
		
		val restTemplate = new RestTemplate();
		val response = restTemplate.getForObject("https://maps.googleapis.com/maps/api/geocode/json?address={address}&sensor=false&key=AIzaSyALr1aJcTzIt5oT1yVZrt4hIu8fL-9-Ng4",
				String.class, address);
		
		val objectMapper = new ObjectMapper();

	    val rootNode = objectMapper.readTree(response);
	    val resultNode = rootNode.get("results");
	    val status = rootNode.get("status").asText();
	    
	    if(StringUtils.isNotBlank(status)) {
	    	if(!status.equalsIgnoreCase("ZERO_RESULTS") && resultNode.size() > 0) {
	    		String lat = resultNode.get(0).get("geometry").get("location").get("lat").asText();
	    		String lng = resultNode.get(0).get("geometry").get("location").get("lng").asText();
	    		//System.out.println("lat:: "+lat+" lng:: "+lng+" userId:: "+user.getUserId());
	    		user.setLatitude(lat);
	    		user.setLongitude(lng);
	    		
	    		userRepository.save(user);
	    	}
	    }
		return user;
	}
	
	private User getUserProAppData( Long userId, String link, String discountCode ) {
		
		User appProUser = null;
		
		if(StringUtils.isBlank(discountCode)) {
			discountCode = "";
		}
		
		if(userId <= 0) {
			throw new UserException("Invalid  application");
		}
		
		String[] activeProfiles = environment.getActiveProfiles();  
		boolean resultProfile = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
			
		String sports = "";
		if(resultProfile) {
			sports = userRepository.getSports(UserUtils.getLoggedInUserId());
			if(StringUtils.isBlank(sports)) {
				sports = "";
			}
		}
		
		String requestUrl = "http://ambassadorlink.com/webservice/index.php/api/application_ref_status/"+userId+"/"+UserUtils.getLoggedInUserId()+"/Active/Fishing";
		
		if(resultProfile) {
			requestUrl = "https://ambassadorlink.com/webservice/index.php/api/application_ref_status/"+userId+"/"+UserUtils.getLoggedInUserId()+"/Active/Hunting";
			if(sports.equalsIgnoreCase("Boating")) {
				requestUrl = "https://ambassadorlink.com/webservice/index.php/api/application_ref_status/"+userId+"/"+UserUtils.getLoggedInUserId()+"/Active/Boating";
			}			
		}
		
		HttpGet request = new HttpGet(requestUrl);
		
		try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readValue(result, JsonNode.class);
                 
                String status = jsonNode.get("status").asText();
                
                if(status.equalsIgnoreCase("success")) {
                	String request1Url = "http://ambassadorlink.com/webservice/index.php/api/prostaff_appprofile/"+userId+"/Fishing";
                	
                	if(resultProfile) {
                		request1Url = "http://ambassadorlink.com/webservice/index.php/api/prostaff_appprofile/"+userId+"/Hunting";
                		if(sports.equalsIgnoreCase("Boating")) {
                			request1Url = "http://ambassadorlink.com/webservice/index.php/api/prostaff_appprofile/"+userId+"/Boating";
    					}
                	}
                	
                	CloseableHttpResponse response1 = httpClient.execute(new HttpGet(request1Url));
                	
                	HttpEntity entity1 = response1.getEntity();
                	
					if (entity1 != null) {
						String result1 = EntityUtils.toString(entity1);

						ObjectMapper mapper1 = new ObjectMapper();

						String firstLastname = "";
						JsonNode jsonNodeName = mapper1.readValue(result1, JsonNode.class);
						JsonNode jsondata = jsonNodeName.get("data");
						for(int i=0; i<jsondata.size(); i++) {
							JsonNode userObj = jsondata.get(i);
							JsonNode firstLastnameObj = userObj.get("FirstLastname");
							firstLastname = firstLastnameObj.asText();
						}
						
						ParentApplication jsonNode1 = mapper1.readValue(result1, ParentApplication.class);

						if (jsonNode1 != null) {
							Application1 data = jsonNode1.getData().get(0);
							if (data != null) {
								
								appProUser = new User();
								
								appProUser.setFirstLastName( firstLastname);
	 		                	appProUser.setEmail(data.getEmail());
	 		                	appProUser.setPhone(data.getPhone());
	 		                	appProUser.setStreet(StringUtils.isBlank(data.getStreet()) ? "" : data.getStreet());
	 		                	appProUser.setCity(StringUtils.isBlank(data.getCity()) ? "" : data.getCity());
	 		                	appProUser.setState(StringUtils.isBlank(data.getState()) ? "" : data.getState());
	 		                	appProUser.setZipcode(StringUtils.isBlank(data.getZipcode()) ? "" : data.getZipcode());
	 		                	appProUser.setDiscountCode(discountCode);
								
								appProUser.setApplicationData(data);
							}
						}
					}
                }
            }
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return appProUser;
	}
	
	public void updateApplications(Long userId, String action, String link, String discountCode) {
		String[] activeProfiles = environment.getActiveProfiles();  
		boolean resultProfile = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
		
		if(StringUtils.isNotBlank(action)) {
			User user = getUserProAppData(userId, link, discountCode);
			if(action.equalsIgnoreCase("Approve")) {
				
				if( user != null) {
					createUser(user, resultProfile);
				}
			}
			
			if(action.equalsIgnoreCase("Reject")) {
				
				if( user != null) {
					Notification notification = notificationRepository.findByOwnerIdAndTypeAndStatus(UserUtils.getLoggedInUserId(), "Rejected", "Active");

					if (notification != null) {
						sendNotifyEmpRejectedProEmail(user, notification, UserUtils.getLoggedInUser());
					}
				}
			}
		}
	}
	
	private UserProAppDetailsSports populateSportsUserProAppDetails(String discountCode,
			UserProAppDetailsSports generalFishingPro, Application1 data) {
		
		if(generalFishingPro == null) {
			generalFishingPro = new UserProAppDetailsSports();
		}
		
		generalFishingPro.setDiscountCode(discountCode);
		generalFishingPro.setShirt_size(
				StringUtils.isBlank(data.getShirt_size()) ? "" : data.getShirt_size());
		
		generalFishingPro.setGlove_size(
				StringUtils.isBlank(data.getGlove_size()) ? "" : data.getGlove_size());
		
		return generalFishingPro;
	}

	private UserProAppDetails populateUserProAppDetails(String discountCode,
			UserProAppDetails generalFishingPro, Application1 data) {
		
		if(generalFishingPro == null ) {
			generalFishingPro = new UserProAppDetails(); 
		}
		generalFishingPro.setDiscountCode(StringUtils.isBlank(discountCode) ? "" : discountCode);
		
		generalFishingPro.setShirt_size(
				StringUtils.isBlank(data.getShirt_size()) ? "" : data.getShirt_size());
		
		generalFishingPro.setGlove_size(
				StringUtils.isBlank(data.getGlove_size()) ? "" : data.getGlove_size());
		
		generalFishingPro
				.setHome_lake_river(StringUtils.isBlank(data.getHome_lake_river()) ? ""
						: data.getHome_lake_river());
		generalFishingPro.setSpecies_type(
				StringUtils.isBlank(data.getSpecies_type()) ? "" : data.getSpecies_type());
		generalFishingPro.setTournament_fish_year(
				StringUtils.isBlank(data.getTournament_fish_year()) ? ""
						: data.getTournament_fish_year());
		generalFishingPro.setIsfish_tournament_trails(
				StringUtils.isBlank(data.getIsfish_tournament_trails()) ? ""
						: data.getIsfish_tournament_trails());
		generalFishingPro
				.setIsusedproducts(StringUtils.isBlank(data.getIsusedproducts()) ? ""
						: data.getIsusedproducts());
		generalFishingPro.setUseproducts(
				StringUtils.isBlank(data.getUseproducts()) ? "" : data.getUseproducts());
		generalFishingPro.setExperiencewithproducts(
				StringUtils.isBlank(data.getExperiencewithproducts()) ? ""
						: data.getExperiencewithproducts());
		generalFishingPro.setLink_to_facebook_fan_page(
				StringUtils.isBlank(data.getLink_to_facebook_fan_page()) ? ""
						: data.getLink_to_facebook_fan_page());
		generalFishingPro.setLink_to_twitter_personal_page("");
		generalFishingPro.setLink_to_instagram_personal_page(
				StringUtils.isBlank(data.getInsta_num_personalpage_followers()) ? ""
						: data.getInsta_num_personalpage_followers());
		generalFishingPro
				.setOther_sposors_1(StringUtils.isBlank(data.getOther_sposors_1()) ? ""
						: data.getOther_sposors_1());
		generalFishingPro.setSelect_fishing_pro_staff(
				StringUtils.isBlank(data.getSelect_fishing_pro_staff()) ? ""
						: data.getSelect_fishing_pro_staff());
		generalFishingPro
				.setOther_information(StringUtils.isBlank(data.getOther_information()) ? ""
						: data.getOther_information());
		generalFishingPro.setLink_to_facebook_personal_page(
				StringUtils.isBlank(data.getLink_to_facebook_personal_page()) ? ""
						: data.getLink_to_facebook_personal_page());
		
		
		return generalFishingPro;
	}
	
	public void deleteApplication(Long userId) {
		userTeamRepository.deleteByUserId(userId);
	}
	
	public void updateApplications(Long userId, String action, String discountCode) {
		
		if(userId <= 0 || findUserById(userId) == null) {
			throw new UserException("Invalid  application");
		}

		String[] activeProfiles = environment.getActiveProfiles();  
    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
    	
		if(StringUtils.isNotBlank(action)) {
			
			User user = findUserById(userId);
			val loggedInUser = UserUtils.getLoggedInUser();
			
			if (action.equalsIgnoreCase("Approve")) {
				createUser(user, result);
			}
			
			if(action.equalsIgnoreCase("Reject")) {
				
				CompanyRef companyRef = getCompanyRef(userId, loggedInUser );
				companyRef.setRefStatus("Rejected");
				companyRefRepository.save(companyRef);
				
				if(result) {
					UserProAppDetailsSports proApp = userProAppDetailsSportsRepository.findByUserIdAndCompanyId(userId, UserUtils.getLoggedInUserId());
					proApp.setStatus("Rejected");		
					proApp.setDiscountCode(discountCode);
					userProAppDetailsSportsRepository.save(proApp);
				}else {
					UserProAppDetails proApp = userProAppDetailsRepository.findByUserIdAndCompanyId(userId, UserUtils.getLoggedInUserId());
					proApp.setStatus("Rejected");		
					proApp.setDiscountCode(discountCode);
					userProAppDetailsRepository.save(proApp);		
				}
				Notification notification = notificationRepository.findByOwnerIdAndTypeAndStatus(UserUtils.getLoggedInUserId(), "Rejected", "Active");
				
				if(notification != null) {
//						Executors.newSingleThreadExecutor()
//							.submit(() -> sendNotifyEmpRejectedProEmail(user, notification, UserUtils.getLoggedInUser()));
					sendNotifyEmpRejectedProEmail(user, notification, UserUtils.getLoggedInUser());
				}
					
			}
			
			if(action.equalsIgnoreCase("Remove")) {
				CompanyRef companyRef = getCompanyRef(userId, loggedInUser );
				companyRef.setRefStatus("Removed");
				companyRefRepository.save(companyRef);
				
				if(result) {
					UserProAppDetailsSports proApp = userProAppDetailsSportsRepository.findByUserIdAndCompanyId(userId, UserUtils.getLoggedInUserId());
					userProAppDetailsSportsRepository.delete(proApp);
				}else {
					UserProAppDetails proApp = userProAppDetailsRepository.findByUserIdAndCompanyId(userId, UserUtils.getLoggedInUserId());
					userProAppDetailsRepository.delete(proApp);
				}
			}
		}

	}
	
	private Map<String, String> getCustomEmailParams(Notification notification) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("@@ATTACHMENT_CLASS@@", "hidden");
		params.put("@@ATTACHMENT_CLASS@@", "innerdiv");
		params.put("@@ATTACHMENT1_CLASS@@","hidden");
		params.put("@@ATTACHMENT2_CLASS@@", "hidden");
		params.put("@@ATTACHMENT3_CLASS@@","hidden");


		if( notification != null) {	
			
			params.put("@@BODYTEXT@@", notification.getBodytext());
			params.put("@@SIGNATURE@@",StringUtils.isAllEmpty(notification.getSignature()) ? "" : notification.getSignature()) ;
			
			String attachment1 = notification.getAttachment();
			String attachment2 = notification.getAttachment1();
			String attachment3 = notification.getAttachment2();

			if(StringUtils.isAllEmpty(attachment1) && StringUtils.isAllEmpty(attachment2) && StringUtils.isAllEmpty(attachment3)) {
				params.put("@@ATTACHMENT_CLASS@@", "hidden");
			}
			else {
				params.put("@@ATTACHMENT1_URL@@", attachment1);
				params.put("@@ATTACHMENT2_URL@@", attachment2);
				params.put("@@ATTACHMENT3_URL@@", attachment3);
				params.put("@@ATTACHMENT_CLASS@@", "innerdiv");
				params.put("@@ATTACHMENT1_CLASS@@",StringUtils.isAllEmpty(attachment1) ? "hidden" : "") ;
				params.put("@@ATTACHMENT2_CLASS@@",StringUtils.isAllEmpty(attachment2) ? "hidden" : "") ;
				params.put("@@ATTACHMENT3_CLASS@@",StringUtils.isAllEmpty(attachment3) ? "hidden" : "") ;
			}
		}
		return params;
	}
	
	private void sendNotifyEmpAcceptedPendingProEmail(User user, User loggedInUser, boolean existingUser) {
		try {
			log.info(" sendNotifyEmpAcceptedPendingProEmail -> sending email to the user id {} with email {}", user.getUserId(), user.getEmail());

			String[] activeProfiles = environment.getActiveProfiles();  
	    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
	    	String from = sendGridEmailService.getFromEmail();
			String cc = sendGridEmailService.getEmail();
			String style = "style='padding: 10px;margin: 0 auto 20px 0;'";
			String empImageLogo = "<img src='https://anglertrack.net/"+loggedInUser.getUserLogo()+"' alt='"+user.getFirstLastName()+"' "+style+" >";
			String siteLogo = "<img src='https://anglertrack.net/assets/img/logoemail.png' />";
	    	String supportEmail = "<a href='mailto:support@anglertrack.net' target='_blank' style='text-decoration: none;padding: 7px 20px;display: inline-block;font-size: 14px;margin-bottom: 30px;'>support@AnglerTrack.net</a>";
			
			String siteName = "AnglerTrack";
			String subject = "Welcome To The "+loggedInUser.getFirstLastName()+" Team!";
			
			String url = "https://anglertrack.net/index.php/home/verifypros/"
					+ PasswordUtil.encryptPassword(Long.toString(user.getUserId())) + "/"
					+ PasswordUtil.encryptPassword(Long.toString(loggedInUser.getUserId()));
			
			if(result) {
				
				url = "https://ambassadortrack.net/index.php/home/verifypros/"
						+ PasswordUtil.encryptPassword(Long.toString(user.getUserId())) + "/"
						+ PasswordUtil.encryptPassword(Long.toString(loggedInUser.getUserId()));
				
				siteLogo = "<img src='https://ambassadortrack.net/assets/images/Ambassador_Track.png'/>";
				supportEmail = "<a href='mailto:support@ambassadortrack.net' target='_blank' style='text-decoration: none;padding: 7px 20px;display: inline-block;font-size: 14px;margin-bottom: 30px;'>support@ambassadortrack.net</a>";

				siteName = "Ambassadortrack";
			}
			
			Notification notification = notificationRepository.findByOwnerIdAndTypeAndStatus( loggedInUser.getUserId(), "Welcome", "Active");
			Map<String, String> params =  getCustomEmailParams(notification);
			params.put("@@EMP_LOGO_URL@@", empImageLogo);
			params.put("@@FIRSTLASTNAMEPRO@@", user.getFirstLastName());
			params.put("@@REFACTION@@", "accepted");
			params.put("@@SPONSORNAME@@", loggedInUser.getFirstLastName());
			params.put("@@LINK@@", url);
			params.put("@@SUPPORT_EMAIL@@", supportEmail);
			params.put("@@SITENAME@@", siteName);
			params.put("@@SITELOGO_URL@@", siteLogo);
			
			String discountCode = user.getDiscountCode(); 
			params.put("@@DISCOUNTCODE_STYLE@@","hidden");
			if(StringUtils.isNotBlank(discountCode)) {
				String discountLable = "<h4>Discount Code: "+discountCode+"</h4>";
				params.put("@@DISCOUNTCODE@@", discountLable);
				params.put("@@DISCOUNTCODE_STYLE@@","");
			}
			
			if(existingUser) {
				sendGridEmailService.sendEmailUsingTemplate(user.getEmail(), from, subject,
						params, "templates/notify_emp_re_accepted_pending.html", cc);
			}
			else {
				sendGridEmailService.sendEmailUsingTemplate(user.getEmail(), from, subject,
						params, "templates/notify_emp_accepted_pending_pro.html", cc); //
			}
		} catch (Exception e) {
			log.error("Cannot send email to {}: {}", user.getEmail(), e.getMessage(), e);
		}
	}
	
//	private void sendNotifyEmpAcceptedActiveProEmail(User user, Notification notification, String discountCode, User loggedInUser) {
//		try {	
//			log.info(" sendNotifyEmpAcceptedActiveProEmail -> sending email to the user id {} with email {}", user.getUserId(), user.getEmail());
//			if(notification != null && notification.getStatus().equalsIgnoreCase("Block")) {
//				return;
//			}
//			String[] activeProfiles = environment.getActiveProfiles();  
//	    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
//	    	String siteLogo = "<img src='https://anglertrack.net/assets/img/logoemail.png' />";
//	    	String supportEmail = "<a href='mailto:support@anglertrack.net' target='_blank' style='text-decoration: none;padding: 7px 20px;display: inline-block;font-size: 14px;margin-bottom: 30px;'>support@AnglerTrack.net</a>";
//			String style = "style='padding: 10px;margin: 0 auto 20px 0;'";
//			String empImageLogo = "<img src='https://anglertrack.net/"+loggedInUser.getUserLogo()+"' alt='"+user.getFirstLastName()+"' "+style+" >";
//			String sub = "Welcome To The "+loggedInUser.getFirstLastName()+" Team!";
//			String subject = ( notification != null && StringUtils.isAllEmpty(notification.getSubject()) ) ? sub : notification.getSubject();
//			if(result) {
//				siteLogo ="<img src='https://sportsmantrack.net/assets/images/Ambassador_Track.png' />"; 
//				supportEmail = "<a href='mailto:support@ambassadortrack.net' target='_blank' style='text-decoration: none;padding: 7px 20px;display: inline-block;font-size: 14px;margin-bottom: 30px;'>support@ambassadortrack.net</a>";
//				empImageLogo ="<img src='https://sportsmantrack.net/"+loggedInUser.getUserLogo()+"' alt='"+user.getFirstLastName()+"' "+style+" >";
//			}
//			
//			Map<String, String> params = getCustomEmailParams(notification);
//			params.put("@@EMP_LOGO_URL@@", empImageLogo);
//			params.put("@@FIRSTLASTNAMEPRO@@", user.getFirstLastName());
//			params.put("@@SUPPORT_EMAIL@@",supportEmail);
//			params.put("@@SITELOGO_URL@@", siteLogo);
//			params.put("@@SPONSORNAME@@", loggedInUser.getFirstLastName());
//			
//			if(StringUtils.isNotBlank(discountCode)) {
//				String discountLable = "<h2>Here's your product discount code "+discountCode+"</h2>";
//				params.put("@@DISCOUNTCODE@@", discountLable);
//				params.put("@@DISCOUNTCODE_STYLE@@","");
//			}else {
//				params.put("@@DISCOUNTCODE_STYLE@@","hidden");
//			}
//		
//			sendGridEmailService.sendEmailUsingTemplate(user.getEmail(), sendGridEmailService.getFromEmail(), subject,
//					params, "templates/notify_emp_accepted_active_pro.html", sendGridEmailService.getEmail() );
//		} catch (Exception e) {
//			log.error("Cannot send email to {}: {}", user.getEmail(), e.getMessage(), e);
//		}
//	}
	
	
	private void sendNotifyEmpRejectedProEmail(User user, Notification notification, User loggedInUser) {
		try {
			log.info(" sendNotifyEmpRejectedProEmail -> Sending email to the user id {} with email {}", user.getUserId(), user.getEmail());
			if(notification != null && notification.getStatus().equalsIgnoreCase("Block")) {
				return;
			}
			String[] activeProfiles = environment.getActiveProfiles();  
	    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
	    	String siteLogo = "<img src='https://anglertrack.net/assets/img/logoemail.png' />";
	    	String supportEmail = "<a href='mailto:support@anglertrack.net' target='_blank' style='text-decoration: none;padding: 7px 20px;display: inline-block;font-size: 14px;margin-bottom: 30px;'>support@AnglerTrack.net</a>";
	    	String style = "style='padding: 10px;margin: 0 auto 20px 0;'";
	    	String empLogoUrl = "<img src='https://anglertrack.net/"+loggedInUser.getUserLogo()+"' alt='"+user.getFirstLastName()+"' "+style+" >";

			if(result) {	
				siteLogo = "<img src='https://ambassadortrack.net/assets/images/Ambassador_Track.png' />";
				supportEmail = "<a href='mailto:ambassadortrack.net' target='_blank' style='text-decoration: none;padding: 7px 20px;display: inline-block;font-size: 14px;margin-bottom: 30px;'>support@ambassadortrack.net</a>";
				empLogoUrl = "<img src='https://ambassadortrack.net/"+loggedInUser.getUserLogo()+"' alt='"+user.getFirstLastName()+"' "+style+" >";

			}
			
			String sub = "Application rejected";
			String subject = ( notification != null && StringUtils.isAllEmpty(notification.getSubject()) ) ? sub : notification.getSubject();
			
			Map<String, String> params = getCustomEmailParams(notification);
			params.put("@@EMP_LOGO_URL@@", empLogoUrl);
			params.put("@@FIRSTLASTNAMEPRO@@", user.getFirstLastName());
			params.put("@@SUPPORT_EMAIL@@",supportEmail);
			params.put("@@SITELOGO_URL@@", siteLogo);
			params.put("@@SPONSORNAME@@", loggedInUser.getFirstLastName());

			sendGridEmailService.sendEmailUsingTemplate(user.getEmail(), sendGridEmailService.getFromEmail(), subject,
					params, "templates/notify_emp_rejected_pro.html", sendGridEmailService.getEmail() );
		} catch (IOException e) {
			log.error("Cannot send email to {}: {}", user.getEmail(), e.getMessage(), e);
		}
	}
	
	public void deleteUser(final Long userId) {
		//This will validate if user has proper access to delete the user.
		this.findUserById(userId);
		//Main delete action - user must be deleted from the sponsor.
		CompanyRef companyRef =  companyRefRepository.findByUseridAndCompanyId(userId, UserUtils.getLoggedInUserId());
		
		if(companyRef == null) {
			log.error("No Company Refrence found by this id.");
			return;
		}
		companyRefRepository.delete(companyRef);
		
		//Related deletions - Any error in this should not stop main action
		try {		
			Long companyId = companyRef.getCompanyId();	
			if(companyId != null){
				userProAppDetailsRepository.deleteByUserIdAndCompanyId(userId,companyId);
			}
			
			Long count = companyRefRepository.countByUserid(userId);
			if(count == 0) {
				//User no longer belongs to any other sponsor.
				userRepository.deleteByUserId(userId);
				//TODO: need to remove user from other tables.
			}
		}catch( Exception e) {
			log.error("Cannot delete related data during DeleteUser : " + e.getMessage());
		}

	} 
	
	public void forgotPassword(String email) { 

		final User user = userRepository.findByEmail(email);
		if(user == null) {
			throw new UserException("No user found by this email.");
		}

		user.setPasswordResetToken(PasswordUtil.generatePassword(30));
		user.setPasswordResetTokenExpireTime(ZonedDateTime.now().plus(15, ChronoUnit.MINUTES));

		forgotPasswordEmail.setUser(user);
		
		sendGridEmailService.sendMail(sendGridEmailService.getFromEmail(), user.getEmail(), "Password reset email", 
				forgotPasswordEmail.formatMessage(), sendGridEmailService.getEmail(), user.getUserId());
		
		/*emailService.sendMail("no-reply@anglertrack.net", user.getEmail(), "AnglerTrack.net - Password reset email", 
				forgotPasswordEmail.formatMessage(), null, user.getUserId());*/

		userRepository.save(user); 
	}

	private void validatePassword(String password, String confirmPassword) {

		if (StringUtils.isBlank(password)) {
			throw new UserException("Password field can not be empty.");
		}

		if (StringUtils.isBlank(confirmPassword)) {
			throw new UserException("Confirm password field can not be empty.");
		}

		if (! password.equals(confirmPassword)) {
			throw new UserException("Confirm password does not match with the password field.");
		}

		if(password.length() < 8) {
			throw new UserException("Password must contain atlesat 8 characters.");
		}

		if(! password.matches(Constants.PASSWORD_VALIDATION_REGEX)) {
			throw new UserException("Your password must contain at least (1) lowercase, (1) uppercase, (1) digit, (1) special character and (8) characters long");
		}

	} 
	
	
	public void sendMailUserStore(UserStore dbUserStore, String email) {
		if(dbUserStore != null && StringUtils.isNotBlank(email)) {
//			Executors.newSingleThreadExecutor()
//				.submit(() -> sendUserStoreEmail(dbUserStore, email));
			sendUserStoreEmail(dbUserStore, email);
		}
		 
	}
	
	private void sendUserStoreEmail(UserStore dbUserStore, String email) {
		try {
			
			log.info(" sendUserStoreEmail -> Sending email to the {}", email);
			
			String subject = "Store check-in form";
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("@@store_name@@", dbUserStore.getStore_name());
			params.put("@@store_date@@", ""+dbUserStore.getStore_date());
			params.put("@@store_time_CheckIN@@", dbUserStore.getStore_time_CheckIN());
			params.put("@@store_chkIn_manager_Duty@@", dbUserStore.getStore_chkIn_manager_Duty());
			params.put("@@store_chkIn_dept_manager@@", dbUserStore.getStore_chkIn_dept_manager());
			String storeLevel = "";
			if(dbUserStore.getStore_inventory_level() == 0 ) {
				storeLevel = "Zero product";
			}
			if(dbUserStore.getStore_inventory_level() == 25 ) {
				storeLevel = "Low Levels (less than 25% full shelf space used)";
			}
			if(dbUserStore.getStore_inventory_level() == 40 ) {
				storeLevel = "Acceptable (40-60% of shelf space used)";
			}
			if(dbUserStore.getStore_inventory_level() == 90 ) {
				storeLevel = "Fully Stocked (shelf space more than 90% full)";
			}
			if(dbUserStore.getStore_inventory_level() == 101 ) {
				storeLevel = "Overstocked (shelf space full with reserve product in the back)";
			}
			params.put("@@store_inventory_level@@", storeLevel);
			params.put("@@store_comp_win_shelf_space@@", dbUserStore.getStore_comp_win_shelf_space());
			
			String store_shelves_conditionStr = "";
			if(dbUserStore.getStore_shelves_condition() == 1 ) {
				store_shelves_conditionStr = "Poor (ex: dusty, poorly organized, half open boxes)";
			}
			if(dbUserStore.getStore_shelves_condition() == 5 ) {
				store_shelves_conditionStr = "Acceptable (generally well organized)";
			}
			if(dbUserStore.getStore_shelves_condition() == 10 ) {
				store_shelves_conditionStr = "Excellent (we looked better than competitors and are well represented)";
			}
			
			params.put("@@store_shelves_condition@@", store_shelves_conditionStr);
			
			String store_end_caps_aisle_lookStr = "";
			if(dbUserStore.getStore_end_caps_aisle_look() == 10 ) {
				store_end_caps_aisle_lookStr = "Fully stocked/Organized well";
			}
			if(dbUserStore.getStore_end_caps_aisle_look() == 8 ) {
				store_end_caps_aisle_lookStr = "Fully stocked/Poorly organized";
			}
			if(dbUserStore.getStore_end_caps_aisle_look() == 7 ) {
				store_end_caps_aisle_lookStr = "Poorly stocked/Organized well";
			}
			if(dbUserStore.getStore_end_caps_aisle_look() == 0 ) {
				store_end_caps_aisle_lookStr = "Poorly stocked/Poorly organized";
			}
			
			params.put("@@store_end_caps_aisle_look@@", store_end_caps_aisle_lookStr);
			params.put("@@store_notes@@", dbUserStore.getStore_notes());
			params.put("@@store_feedback@@", dbUserStore.getStore_feedback());
			params.put("@@store_new_product_trend@@", dbUserStore.getStore_new_product_trend());
			params.put("@@store_brand_observed_discuss@@", dbUserStore.getStore_brand_observed_discuss());
			params.put("@@store_event_name_people@@", dbUserStore.getStore_event_name_people());

//TODO: 	user_store_send_mail.html is used here. Refactor by removing style/html from java code
// updating the template params and use all values from notification table.
//TODO: The notification may be disabled in table, so we need to check that before sending email
			
			sendGridEmailService.sendEmailUsingTemplate(email, sendGridEmailService.getFromEmail(), subject,
					params, "templates/user_store_send_mail.html", sendGridEmailService.getEmail() );
		} catch (IOException e) {
			log.error("Cannot send email to {}: {}", email, e.getMessage(), e);
		}
	}

	private CompanyRef getCompanyRef(long userId, User loggedInUser) {
		CompanyRef companyRef =null;
		try {
		 companyRef = companyRefRepository.findByUseridAndCompanyId(userId, loggedInUser.getUserId() );
		}catch(Exception e) {
			e.printStackTrace();
			}
		if( companyRef == null ) {
			companyRef = new CompanyRef(); 
			companyRef.setUserid(userId);
			companyRef.setCompname( loggedInUser.getFirstLastName() );
			companyRef.setCompanyId( loggedInUser.getUserId() );
			companyRef.setFbkeywords( loggedInUser.getFbKeywords() );
			companyRef.setInstakeywords( loggedInUser.getInstaKeywords() );
			companyRef.setTwitterkeywords( loggedInUser.getTwtKeywords() );
			companyRef.setYoutubekeywords( loggedInUser.getYoutubeKeywords() );
		}
		return companyRef;
	}
	
}
