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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserInstragramFeed;
import com.tracker.commons.models.UserMedia;
import com.tracker.services.dto.StevesieCollectionRequest;
import com.tracker.services.dto.StevesieCollectionResponse;
import com.tracker.services.dto.StevesieUserNameWorkflowResult;
import com.tracker.services.dto.StevesieWorkflowExecutionParametersUserId;
import com.tracker.services.dto.StevesieWorkflowExecutionParametersUserName;
import com.tracker.services.dto.StevesieWorkflowProxy;
import com.tracker.services.dto.StevesieWorkflowRequestForUserId;
import com.tracker.services.dto.StevesieWorkflowRequestForUserName;
import com.tracker.services.dto.WorkflowResponseDTO;
import com.tracker.services.repositories.UserInstagramFeedRepository;
import com.tracker.services.repositories.UserMediaRepository;
import com.tracker.services.repositories.UserRepositoryImpl;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StevesieInstagramService {
	
	@Value("${stevesie.url}")
	private String baseUrl;

	@Value("${stevesie.token}")
	private String token;
	
	@Value("${stevesie.workflowUserProfileId}")
	private String workflowUserProfileId;
	
	@Value("${stevesie.workflowUserPostId}")
	private String workflowUserPostId;
	
	@Value("${stevesie.instaSessionId}")
	private String instaSessionId;
	
	@Autowired
	UserRepositoryImpl userRepositoryImpl;
	
	@Autowired
	UserMediaRepository userMediaRepository;
	
	@Autowired
	UserInstagramFeedRepository userInstagramFeedRepository;
	
	private final CloseableHttpClient httpClient = HttpClients.createDefault();
	
	public static <T> Collection<List<T>> partitionBasedOnSize(List<T> inputList, int size) {
        final AtomicInteger counter = new AtomicInteger(0);
        return inputList.stream()
                    .collect(Collectors.groupingBy(s -> counter.getAndIncrement()/size))
                    .values();
	}
	
	//@Scheduled(initialDelay = 15000, fixedRateString = "86400000") 
	@Scheduled(cron = "0 30 23 * * ?", zone = "America/New_York")
	public void scheduleInstagramFeedData() throws URISyntaxException, InterruptedException {
		
		log.debug("Fetching Instagram data with session Id :: "+instaSessionId);
		
		val userMediaList = userRepositoryImpl.fetchInstagramUser();
		
		Collection<List<UserMedia>> list = partitionBasedOnSize(userMediaList, 200);
		
		String[] instaSessionIds = instaSessionId.split(",");
		log.info("Total IG session ID available size:: "+instaSessionIds.length);
		int i=0;
		for(List<UserMedia> listObj : list) {
			log.info("batch size:: "+listObj.size());
			val instaUserNameMap = new HashMap<String, User>();
			for (UserMedia userMedia : listObj) {
				String name = userMedia.getUsername();
				if( name.contains("@") || name.contains(".") ) {
					continue;
				}
				User user = new User();
				user.setUserId(userMedia.getUserid());
				user.setInstaUserName(userMedia.getUsername());
				instaUserNameMap.put(userMedia.getUsername(), user);
			}
			
			log.info("batch "+(i+1)+" started...UserMap size: " + instaUserNameMap.size() );
			fetchInstagramFeed(instaUserNameMap, instaSessionIds[i]);
			log.info("batch "+(i+1)+" ended...");
			i++;
			if( i >= instaSessionIds.length ) {
				break;
			}
		}
		
	}

	private void fetchInstagramFeed(Map<String, User> instaUserNameMap, String instagramSessionId) 
			throws URISyntaxException, InterruptedException {
		
		log.info("instagramSessionId :: "+instagramSessionId);
		long startTime = System.currentTimeMillis();
		
		val instaUseIds = fetchAndUpdateInstagramID( instaUserNameMap, instagramSessionId );
		log.info("instauserId size {}", instaUseIds.size());
	
		
		val stevesieUserPostWorkflowResultMap = populateUserPostDataWorkFlow(instaUseIds, instagramSessionId );
		
		long endTime = System.currentTimeMillis();
		log.info("######StartTime ::: "+startTime);
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("######endTime :: "+endTime);
		log.info("######Time took for execution instagram data " + (endTime - startTime) + " milliseconds");
		
		startTime = System.currentTimeMillis();
		saveInstaFeedToDB(instaUserNameMap, stevesieUserPostWorkflowResultMap);
		endTime = System.currentTimeMillis();
		log.info("######StartTime ::: "+startTime);
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("#######################################################");
		log.info("######endTime :: "+endTime);
		log.info("######Time took to save data into database " + (endTime - startTime) + " milliseconds");

		log.info("finish with instagramSessionId :: "+instagramSessionId);
	}

	private void saveInstaFeedToDB(final Map<String, User> instaUserNameMap,
			final Map<String, List<WorkflowResponseDTO>> stevesieUserPostWorkflowResultMap) {
		
		log.info("saveInstaFeedToDB called...");
		
		List<UserInstragramFeed> instaFeedData = new ArrayList<UserInstragramFeed>();
		List<String> checkIds = new ArrayList<String>();
		
		val dbExistingMap = new HashMap<String, UserInstragramFeed>();
		
		for (String instaUserName : stevesieUserPostWorkflowResultMap.keySet()) {
			  val workflowResponseDTO = stevesieUserPostWorkflowResultMap.get(instaUserName);
			  User userObj = instaUserNameMap.get(instaUserName);
			  
			  if(userObj != null) {
				  for(WorkflowResponseDTO dto : workflowResponseDTO) {
					  dto = validateWorkflowDto(dto);
					  checkIds.add(dto.getId()+"_"+dto.getOwnerUserId());
				  }
			  }
		}
		
		List<UserInstragramFeed> dbList = userInstagramFeedRepository.findByIdIn(checkIds);
		if(dbList != null && dbList.size() > 0) {
			for(UserInstragramFeed userInstragramFeed : dbList) {
				dbExistingMap.put(userInstragramFeed.getId(), userInstragramFeed);
			}
		}
		
		
		for (String instaUserName : stevesieUserPostWorkflowResultMap.keySet()) {
		  val workflowResponseDTO = stevesieUserPostWorkflowResultMap.get(instaUserName);
		  
		  User userObj = instaUserNameMap.get(instaUserName);
		  if(userObj != null) {
			  
			  for(WorkflowResponseDTO dto : workflowResponseDTO) {
				  
				  dto = validateWorkflowDto(dto);
				  
				  UserInstragramFeed instaFeed = new UserInstragramFeed();
				  instaFeed.setUserId(userObj.getUserId());
				  instaFeed.setId(dto.getId()+"_"+dto.getOwnerUserId());
				  instaFeed.setText(dto.getText());
				  instaFeed.setLink("");
				  instaFeed.setLikes(Integer.parseInt(dto.getLikesCount()));
				  instaFeed.setLikesCount(Integer.parseInt(dto.getLikesCount()));
				  instaFeed.setCommentsCount(Integer.parseInt(dto.getCommentsCount()));
				  instaFeed.setThumbnailImage(dto.getThumbnailImage());
				  instaFeed.setStandardImage(dto.getStandardImage());
				  instaFeed.setCreatedTime(Long.toString(dto.getCreatedTime()));
				  instaFeed.setPostType(dto.getPostType());
				  instaFeed.setVideoViewVount(Integer.parseInt(dto.getVideoViewVount()));
				  instaFeed.setUserProfileThumbnailImage(dto.getUserProfileThumbnailImage());
				  
				  //val existingRecord = userInstagramFeedRepository.findById(dto.getId());
				  val existingRecord = dbExistingMap.get(dto.getId()+"_"+dto.getOwnerUserId());
				  if(existingRecord != null) {
					  log.info("record found for insta id {}", dto.getId()+"_"+dto.getOwnerUserId());
					  existingRecord.setText(dto.getText());
					  existingRecord.setLink("");
					  existingRecord.setLikes(Integer.parseInt(dto.getLikesCount()));
					  existingRecord.setLikesCount(Integer.parseInt(dto.getLikesCount()));
					  existingRecord.setCommentsCount(Integer.parseInt(dto.getCommentsCount()));
					  existingRecord.setThumbnailImage(dto.getThumbnailImage());
					  existingRecord.setStandardImage(dto.getStandardImage());
					  existingRecord.setCreatedTime(Long.toString(dto.getCreatedTime()));
					  existingRecord.setPostType(dto.getPostType());
					  existingRecord.setVideoViewVount(Integer.parseInt(dto.getVideoViewVount()));
					  existingRecord.setUserProfileThumbnailImage(dto.getUserProfileThumbnailImage());
					  
					  //userInstagramFeedRepository.save(existingRecord);
					  instaFeedData.add(existingRecord);
				  }else {
					  instaFeedData.add(instaFeed);
				  }
				  
				  log.info("insta ID {}", dto.getId()+"_"+dto.getOwnerUserId());
			  }
			  
			  log.info("instaUserName:: "+instaUserName+" size:: "+workflowResponseDTO.size());
		  }
		  
		}
		log.info("saving bulk data into database.."+instaFeedData.size());
		userInstagramFeedRepository.saveAll(instaFeedData);		
		log.info("saving data into database done..");
	}
	
	public WorkflowResponseDTO validateWorkflowDto(WorkflowResponseDTO dto) {
		
		if(StringUtils.isEmpty(dto.getId())) {
			dto.setId("");
		}
		
		if(StringUtils.isEmpty(dto.getOwnerUserId())) {
			dto.setOwnerUserId("");
		}
		
		if(StringUtils.isEmpty(dto.getText())) {
			dto.setText("");
		}
		
		if(StringUtils.isEmpty(dto.getLikesCount())) {
			dto.setLikesCount("0");
		}
		
		if(StringUtils.isEmpty(dto.getCommentsCount())) {
			dto.setCommentsCount("0");
		}
		
		if(StringUtils.isEmpty(dto.getThumbnailImage())) {
			dto.setThumbnailImage("");
		}
		
		if(StringUtils.isEmpty(dto.getStandardImage())) {
			dto.setStandardImage("");
		}
		
		if(dto.getCreatedTime() == null) {
			dto.setCreatedTime(0L);
		}
		
		if(StringUtils.isEmpty(dto.getPostType())) {
			dto.setPostType("");
		}
		
		if(StringUtils.isEmpty(dto.getVideoViewVount())) {
			dto.setVideoViewVount("0");
		}
		
		if(StringUtils.isEmpty(dto.getUserProfileThumbnailImage())) {
			dto.setUserProfileThumbnailImage("");
		}
		
		
		return dto;
	}
	
	public Map<String, List<WorkflowResponseDTO>> populateUserPostDataWorkFlow(List<String> userIds , String instagramSessionId ) throws URISyntaxException, InterruptedException {
		
		log.debug("Calling Instagram Public User Posts Wrokflow..");
		
		
		Map<String, List<WorkflowResponseDTO>> stevesieUserPostWorkflowResult = new HashMap<String, List<WorkflowResponseDTO>>();
		//modifyCollection(userIds, userIdCoolectionId);
		if( userIds == null || userIds.size() == 0 ) {
			return stevesieUserPostWorkflowResult;
		}

		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/"+workflowUserPostId+"/executions");
		String url = workFlowUriBuilder.build().toString();
		log.info("user post workflow url {}", url);
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		StringEntity requestEntity = generateWorkflowRequest(userIds, "userid", instagramSessionId);		
		productPost.setEntity(requestEntity);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine of user post workflow {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity != null) {
            	
            	ObjectMapper mapper = new ObjectMapper();
            	String result = EntityUtils.toString(entity);
            	
            	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
            	JsonNode jsondata = jsonNodeName.get("object");
            	JsonNode firstLastnameObj = jsondata.get("id");
                String workFlowId = firstLastnameObj.asText();
                
                log.info("result of user post workflow {}", result);
                log.info("user post workflow Id {}", workFlowId);
                
                boolean isComplete = false;
                while (isComplete == false) {
                	log.info("wait 1.5 minute to complete execution of user post workflow..");
                	Thread.sleep(80000);

					stevesieUserPostWorkflowResult = fetchResultFromUserPostWrokFlow(
							workFlowId);
					if (stevesieUserPostWorkflowResult != null) {
						List<WorkflowResponseDTO> statusList = stevesieUserPostWorkflowResult.get("status");
						if (statusList != null && statusList.size() > 0) {
							isComplete = statusList.get(0).getStatus();
						} else {
							isComplete = true;
						}
					}
					 
                	log.info("isComplete {}", isComplete);
                } 
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stevesieUserPostWorkflowResult;
		
	}
	
	public void modifyCollection(List<String> items, String collectionId) throws URISyntaxException {
		
		log.info("modifying collection data");
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/collections/"+collectionId);
		String url = workFlowUriBuilder.build().toString();
		
		//Fetch existing collection data
		HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("Token", token);
        
        List<String> existingItems = new ArrayList<String>();
        
        try (CloseableHttpResponse response = httpClient.execute(getRequest)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {
            	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            	String result = EntityUtils.toString(entity);
            	log.info("result {}", result);
            	
            	StevesieCollectionResponse collectionResult = mapper.readValue(result, StevesieCollectionResponse.class);
            	
            	if(collectionResult != null && collectionResult.getObject() != null) {
            		if(collectionResult.getObject().getItems() != null && collectionResult.getObject().getItems().size() > 0) {
            			existingItems = collectionResult.getObject().getItems();
            		}
            	}
            	
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Fetch existing collection data end
        
        //Remove existing collection data
        if(existingItems.size() > 0) {
	        HttpPost removePost = new HttpPost(url);
	        removePost.addHeader("Token", token);
			
			StringEntity removeRequestEntity = generateCollectionRequest(existingItems, "remove");
			removePost.setEntity(removeRequestEntity);
			
			try (CloseableHttpResponse response = httpClient.execute(removePost)) {
	            HttpEntity entity = response.getEntity();
	
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity);
	                log.info("result {}", result);
	            }
	
	        } catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		//Remove existing collection data end
		
          
		//Add new collection data
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		StringEntity requestEntity = generateCollectionRequest(items, "add");
		productPost.setEntity(requestEntity);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {
            	String result = EntityUtils.toString(entity);
                log.info("result {}", result);
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Map<String, List<WorkflowResponseDTO>> fetchResultFromUserPostWrokFlow(String workFlowId) throws URISyntaxException {
		boolean status  = false;
		log.info("user post workflow execution start...");
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/executions/"+workFlowId);
		String url = workFlowUriBuilder.build().toString();
		
		Map<String, List<WorkflowResponseDTO>> userNameResultMap = new HashMap<String, List<WorkflowResponseDTO>>();
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine of user post workflow {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity == null) {
            	return userNameResultMap;
            }
            	
        	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	String result = EntityUtils.toString(entity);
        	
        	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
        	JsonNode jsondata = jsonNodeName.get("object");
        	JsonNode completedAtObj = jsondata.get("completed_at");
        	JsonNode resultsObj = jsondata.get("results");
            String completedAt = completedAtObj.asText();
            
            log.info("result of user post workflow {}", result);
            log.info("completedAt {}", completedAt);
            
            if(StringUtils.isNotBlank(completedAt)) {
            	if(completedAt.equalsIgnoreCase("None") || completedAt.equalsIgnoreCase("null")) {
            		status  = false;
            	}else {
            		userNameResultMap = new HashMap<String, List<WorkflowResponseDTO>>();
            		status  = true;
            	}
            }else {
            	status  = false;
            }
            
            if(!status) {
            	List<WorkflowResponseDTO> statusResponse = new ArrayList<WorkflowResponseDTO>();
        		WorkflowResponseDTO request = new WorkflowResponseDTO();
        		request.setStatus(false);
        		statusResponse.add(request);
        		userNameResultMap.put("status", statusResponse);
        		return userNameResultMap;
            }
            System.out.println("resultsObj:: "+resultsObj+" status:: "+status);
            
            if(resultsObj != null && status) {
            	
            	JsonNode resultUrlObj = resultsObj.get(0).get("url");
            	String urlStr = resultUrlObj.asText();
            	log.info("user post workflow result json file url {}", urlStr);
				
				URL jsonUrl = new URL(urlStr);
				
				StevesieUserNameWorkflowResult[] jsonNodeResult = mapper.readValue(jsonUrl, StevesieUserNameWorkflowResult[].class);
				
				for(StevesieUserNameWorkflowResult request : jsonNodeResult) {
					
					val workflowResponseDTO = request.getResponse();						
					String instaUserName = workflowResponseDTO.getOwnerUserName();
					
					List<WorkflowResponseDTO> workflowResponseList = userNameResultMap.get(instaUserName);
					if(workflowResponseList != null && workflowResponseList.size() > 0) {
						workflowResponseList.add(workflowResponseDTO);
					}else {
						workflowResponseList = new ArrayList<WorkflowResponseDTO>();
						workflowResponseList.add(workflowResponseDTO);
					}
					log.info("got resut for username {}, size:: {} ", instaUserName, workflowResponseList.size());
					userNameResultMap.put(instaUserName, workflowResponseList);
				}
            	
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("finished user post workflow..");
		return userNameResultMap;
	}
	
	public Map<String, String> fetchResultFromWrokFlow(String workFlowId) throws URISyntaxException {
		boolean status  = false;
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/executions/"+workFlowId);
		String url = workFlowUriBuilder.build().toString();
		
		Map<String, String> userNameResultMap = new HashMap<String, String>();
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine of user profile workflow {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity == null) {
            	return userNameResultMap;
            }

        	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
        	String result = EntityUtils.toString(entity);
        	
        	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
        	JsonNode jsondata = jsonNodeName.get("object");
        	JsonNode completedAtObj = jsondata.get("completed_at");
        	JsonNode resultsObj = jsondata.get("results");
            String completedAt = completedAtObj.asText();
            
            log.info("result of user profile workflow {}", result);
            log.info("completedAt {}", completedAt);
            
            if(StringUtils.isNotBlank(completedAt)) {
            	if(completedAt.equalsIgnoreCase("None") || completedAt.equalsIgnoreCase("null")) {
            		status  = false;
            	}else {
            		status  = true;
            	}
            }else {
            	status  = false;
            }
            
            if(status == false) {
            	userNameResultMap = new HashMap<String, String>();
        		userNameResultMap.put("status", "false");
        		return userNameResultMap;
            }
            
            
            if(resultsObj != null && status) {
            	
            	JsonNode resultUrlObj = resultsObj.get(0).get("url");
            	String urlStr = resultUrlObj.asText();
				
				URL jsonUrl = new URL(urlStr);
				log.info("jsonUrl of user profile result {}", jsonUrl);
				
				StevesieUserNameWorkflowResult[] jsonNodeResult = mapper.readValue(jsonUrl, StevesieUserNameWorkflowResult[].class);
				
				for(StevesieUserNameWorkflowResult request : jsonNodeResult) {
					String instaUserName = request.getInput().getUsername();
					String instaUserId = request.getResponse().getInstaUserId();
					log.info("instagram username and userid fetch from stevesie api {} {} ", instaUserName, instaUserId );
					if(StringUtils.isNotBlank(instaUserId)) {
						userNameResultMap.put(instaUserName, instaUserId);
					}
				}
            	
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("finished user profile workflow");
		return userNameResultMap;
	}

	private StringEntity generateWorkflowRequest(List<String> lstString, String type, String instagramSessionId) {
		log.info("generating workflow request...");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -30);
		Date previous2 = cal.getTime();
		
		Date today = new Date();
		
		String minDateStr = sdf.format(previous2);
		String maxDateStr = sdf.format(today);

		Date minDate = new Date();
		Date maxDate = new Date();
		
		try {
			minDate = sdf.parse(minDateStr);
			maxDate = sdf.parse(maxDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		long minDateTimeInMillis = minDate.getTime() / 1000;
		long maxDateTimeInMillis = maxDate.getTime() / 1000;
		
		log.info("minDateTimeInMillis:: "+minDateTimeInMillis);
		log.info("maxDateTimeInMillis:: "+maxDateTimeInMillis);
		
		val proxy = new StevesieWorkflowProxy();
		proxy.setLocation("nyc");
		proxy.setType("dedicated");
		
		val executionParameterForUserName = new StevesieWorkflowExecutionParametersUserName();
		executionParameterForUserName.setUsername(lstString);
		executionParameterForUserName.setSession_id(instagramSessionId);
		executionParameterForUserName.setMin_timestamp(Long.toString(maxDateTimeInMillis - 10000));
		executionParameterForUserName.setMax_timestamp(Long.toString(maxDateTimeInMillis));
		
		val executionParameterForUserId = new StevesieWorkflowExecutionParametersUserId();
		executionParameterForUserId.setUser_id(lstString);
		executionParameterForUserId.setSession_id(instagramSessionId);
		executionParameterForUserId.setMin_timestamp(Long.toString(minDateTimeInMillis));
		executionParameterForUserId.setMax_timestamp(Long.toString(maxDateTimeInMillis));
		
		val request = new StevesieWorkflowRequestForUserName();
		request.setFormat("json");
		request.setOutput_aggregation_format("json");
		request.setProxy(proxy);
		request.setExecution_parameters(executionParameterForUserName);
		request.setSend_completion_email(false);
		
		val requestForUserId = new StevesieWorkflowRequestForUserId();
		requestForUserId.setFormat("json");
		requestForUserId.setOutput_aggregation_format("json");
		requestForUserId.setProxy(proxy);
		requestForUserId.setExecution_parameters(executionParameterForUserId);
		requestForUserId.setSend_completion_email(false);
		
		StringEntity requestEntity = null;
		try {
			if(type.equalsIgnoreCase("username")) {
				requestEntity = new StringEntity(request.toJson());
			}else {
				requestEntity = new StringEntity(requestForUserId.toJson());
			}
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return requestEntity;
	}
	
	private StringEntity generateCollectionRequest(List<String> userNames, String method) {

		StevesieCollectionRequest request = new StevesieCollectionRequest();
		request.setMethod(method);
		request.setItems(userNames);

		StringEntity requestEntity = null;
		try {
			requestEntity = new StringEntity(request.toJson());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return requestEntity;
	}
	
	
	public List<String> fetchAndUpdateInstagramID( Map<String, User> instaUserNameMap, String instagramSessionId ) {
		//Get all userid-IGNames mapping from user_instagram_usernames -  A
		//get all Records from user_media where social_type="instagram" - B
		//Extract set of IGnames from A - setA
		//Extract set of IGnames from B - setB1, setB2 (one set with having social_id and one with empty social_id)
		//Find all IGnames which don't have instagram_id by setA-setB (remove all setB data from setA) - emptyIdSet
		//Use stevsie API to fetch IG ID's for IGNames in emptyIdSet
		// Create/update user_media objects with above info and set IG_ID in the objects. Save all.
		// get social_id from user_media where social_type="instagram" and social_id NOT empty -  return this 
		
		///
		Set<String> inputUserNames = clearIGUserNames(instaUserNameMap);
		//Get all IG records from UserMedia
		List<UserMedia> existingUserMediaList = userMediaRepository.findByUsernameInAndSocialType( Arrays.asList( instaUserNameMap.keySet().toArray(new String[0]) ), "instagram");
		Set<String> userWithID = new HashSet<String>();
		Set<String> userWithNOID = new HashSet<String>();
		Map<String, UserMedia> updateMediaMap = new HashMap<String, UserMedia>();
		//Check in UserMedia where socialID already exist and where NOT
		for (UserMedia userMedia : existingUserMediaList) {
			String socialID = userMedia.getSociald();
			String username = userMedia.getUsername();

			if( socialID == null || StringUtils.isAllBlank(socialID) || "null".equals(socialID)) {
				userWithNOID.add( username );
				updateMediaMap.put(username, userMedia);
			}else {
				userWithID.add( username );
			}
		}
		//We need to get IGID only for usernames which don't have ID yet
		Set<String> igUserForAPI = inputUserNames;
		igUserForAPI.removeAll(userWithID); //remove all where ID already exist
		
		
		if( igUserForAPI != null && igUserForAPI.size() > 0 ) {
			log.debug(" IG names for get ID API - " + igUserForAPI);
			
			try {
				//Now get the ID for required IGNames only
				Map<String, String> igNameToIGIDmap = getInstaIDWithAPI( Arrays.asList( igUserForAPI.toArray(new String[0]) ) , instagramSessionId );
				if( igNameToIGIDmap != null && igNameToIGIDmap.size() > 0 ) {
					List<UserMedia> dbSaveList = new ArrayList<UserMedia>();
					
					for ( String igname : igNameToIGIDmap.keySet() ) {
						UserMedia um = new UserMedia();
						if( updateMediaMap.containsKey(igname) ) {
							um = updateMediaMap.get(igname);
						}else {
							
							User usr = instaUserNameMap.get(igname);
							if( usr != null ) {
								um.setUsername(igname);
								um.setSocialType("instagram");
								um.setStatus("Active");		
								um.setUserid(usr.getUserId());
							}
						}
						um.setSociald( igNameToIGIDmap.get(igname) );
						dbSaveList.add(um);
					}
					//Save IGname/ID in UserMedia for future ref
					userMediaRepository.saveAll(dbSaveList);
				}
				
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Data has changed, read table again for latest socialID for IG
			existingUserMediaList = userMediaRepository.findByUsernameInAndSocialType( Arrays.asList( instaUserNameMap.keySet().toArray(new String[0]) ), "instagram");
		}
		
		Set<String> igIDSet = existingUserMediaList.stream().map( UserMedia::getSociald ).collect(Collectors.toCollection(TreeSet::new));
		log.info("Instagram social ID's returned to get posts via API: " + igIDSet);
		return Arrays.asList( igIDSet.toArray( new String[0]) );
		
	}

	private Set<String> clearIGUserNames(Map<String, User> instaUserNameMap) {
		
		Set<String> inputUserNames = new HashSet<String>();
		inputUserNames.addAll(instaUserNameMap.keySet());
		
		inputUserNames.remove(""); inputUserNames.remove("null");
		//cleanup IG username list
		
		for (String igName : instaUserNameMap.keySet() ) {
			//String igName = (String) iterator.next();
			if( igName.contains("@") || igName.contains(".") ||  igName.contains(" ") ) {
				inputUserNames.remove(igName);
			}
			if( igName.startsWith("http:") || igName.startsWith("https:") ) {
				inputUserNames.remove(igName);
			}
		}
		return inputUserNames;
	}
	

	private Map<String, String> getInstaIDWithAPI( List<String> userNames, String instagramSessionId ) throws URISyntaxException, InterruptedException {

		Map<String, String> stevesieUserNameWorkflowResult = new HashMap<String, String>();
		if( userNames == null || userNames.size() == 0 ) {
			return stevesieUserNameWorkflowResult;
		}
		if( userNames.size() > 200 ) {
			userNames = userNames.subList(0, 199);
		}
		
		
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/"+workflowUserProfileId+"/executions");
		
		String url = workFlowUriBuilder.build().toString();
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		StringEntity requestEntity = generateWorkflowRequest(userNames, "username", instagramSessionId);
		
		productPost.setEntity(requestEntity);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity == null) {
            	return stevesieUserNameWorkflowResult;
            }
            	
			ObjectMapper mapper = new ObjectMapper();
        	String result = EntityUtils.toString(entity);
        	
        	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
        	JsonNode jsondata = jsonNodeName.get("object");
        	JsonNode firstLastnameObj = jsondata.get("id");
            String workFlowId = firstLastnameObj.asText();
            
            log.info("result of user profile workflow id {}", result);
            log.info("workFlowId id {}", workFlowId);
            
           
            boolean isComplete = false;
            while (isComplete == false) {
            	log.info("wait for 1 minute to execution user profile workflow");
            	Thread.sleep(70000);
            	stevesieUserNameWorkflowResult  = fetchResultFromWrokFlow(workFlowId);
            	if(stevesieUserNameWorkflowResult != null) {
            		String status = stevesieUserNameWorkflowResult.get("status");
            		if(StringUtils.isNotBlank(status) && status.equalsIgnoreCase("false")) {
            			isComplete = false;
            		}else {
            			isComplete = true;
            		}
            	}
            	
            	log.info("isComplete {}", isComplete);
            }
         	
    		log.info("instauserId size {}", stevesieUserNameWorkflowResult.size());
    		
		}catch( Exception e) {
			log.error(" Exception while getting IG ID's with StevsieAPI - " + e.getMessage());
			e.printStackTrace();
		}
		
		return stevesieUserNameWorkflowResult;
	}
		
}
