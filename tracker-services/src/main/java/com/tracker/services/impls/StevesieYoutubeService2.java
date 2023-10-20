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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserMedia;
import com.tracker.commons.models.UserYoutubeFeed1;
import com.tracker.services.dto.StevesieWorkflowChannelIdInputs;
import com.tracker.services.dto.StevesieWorkflowProxy;
import com.tracker.services.dto.StevesieYTChannelWorkflowResult;
import com.tracker.services.dto.StevesieYTVideoWorkflowResult;
import com.tracker.services.dto.StevesieYoutubeWorkflowRequest;
import com.tracker.services.dto.StevesieYoutubeWorkflowRequestForChannelId;
import com.tracker.services.dto.StevesieYoutubeWorkflowRequestForUsername;
import com.tracker.services.dto.YTVideoWorkflowResponseDTO;
import com.tracker.services.repositories.UserRepositoryImpl;
import com.tracker.services.repositories.UserYoutubeFeedRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StevesieYoutubeService2 {
	
	@Value("${stevesie.url}")
	private String baseUrl;

	@Value("${stevesie.token}")
	private String token;
	
	@Value("${google.credentials.token}")
	private String gogleToken;
	
	@Value("${stevesie.workflowUserProfileId}")
	private String workflowUserProfileId;
	
	@Value("${stevesie.workflowUserPostId}")
	private String workflowUserPostId;
	
	
	@Autowired
	UserRepositoryImpl userRepositoryImpl;
	
	@Autowired
	UserYoutubeFeedRepository userYoutubeFeedRepository;
	
	private final CloseableHttpClient httpClient = HttpClients.createDefault();
	
	
	//@Scheduled(initialDelay = 15000, fixedRateString = "86400000") 
	public void scheduleYoutubeFeedData() throws URISyntaxException, InterruptedException {
		
		log.debug("Fetching Youtube data..");
		
		val userMediaList = userRepositoryImpl.fetchYoutubeUserMedia();
		
		for (UserMedia userMedia : userMediaList) {
			User user = new User();
			user.setUserId(userMedia.getUserid());
			user.setYoutubeUserName(userMedia.getUsername());
			
			//if (user.getUserId() == 11117) {
				val userNames = new ArrayList<String>();
				userNames.add(userMedia.getUsername());
				processYoutubeFeed(userNames, userMedia.getUserid());
			//}
		}
		
		//fetchYoutubeDataByChannelId("UCDGYnmDohqC8V4ga98AV0Ag", 203L);
		
	}
	
	private String launchProxie() {
		String proxy_id = "";
		
		String url = "https://stevesie.com/cloud/api/v1/proxies";
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            HttpEntity entity = response.getEntity();
            if (entity == null) {
            	return proxy_id;
            }
            	
			ObjectMapper mapper = new ObjectMapper();
        	String result = EntityUtils.toString(entity);
        	
        	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
        	JsonNode object = jsonNodeName.get("object");
        	JsonNode id = object.get("id");
        	proxy_id = id.asText();
            
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return proxy_id;
	}
	
	private void processYoutubeFeed(List<String> userNames, Long userId)
			throws URISyntaxException, InterruptedException {
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/"+workflowUserProfileId+"/executions");
		//String url = workFlowUriBuilder.build().toString();
		String url = "https://stevesie.com/cloud/api/v1/endpoints/17fbad2a-c597-4fcd-81c0-675921a95811/executions";
		
		
		/*
		 * String proxieId = launchProxie(); System.out.println("proxieId:: "+proxieId);
		 */
		
		String proxieId = "";
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		log.info("generate workflow request for youtube usernames{} "+userNames);
		StringEntity requestEntity = generateWorkflowRequest(userNames, "username", "", proxieId);
		
		
		productPost.setEntity(requestEntity);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            HttpEntity entity = response.getEntity();
            if (entity == null) {
            	return;
            }
            	
			ObjectMapper mapper = new ObjectMapper();
        	String result = EntityUtils.toString(entity);
        	
        	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
        	JsonNode object = jsonNodeName.get("object");
        	log.info("object:: "+object);
        	if(object != null) {
	        	JsonNode responseObj = object.get("response");
	        	JsonNode response_json = responseObj.get("response_json");
	        	JsonNode items = response_json.get("items");
	        	if(items == null) {
	        		log.info("Youtube channel not found for user  {}", userNames);
	        	}else {
		        	JsonNode idObj = items.get(0).get("id");
		        	String channelId = idObj.asText();
		            
		            log.info("result of youtube user channel id by username {}", result);
		            log.info("channel id {}", channelId);
		            fetchYoutubeDataByChannelId(channelId, userId, proxieId);
	        	}
        	}else {
        		log.info("Youtube channel not found for user  {}", userNames);
        	}
            
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void fetchYoutubeDataByChannelId(String channelId, Long userId, String proxieId) throws URISyntaxException, InterruptedException {
		//channelId = "UCDGYnmDohqC8V4ga98AV0Ag";
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/5658616d-9f21-47c4-ba87-74dcd3eea77d/executions");
		String url = workFlowUriBuilder.build().toString();
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		StringEntity requestEntity = generateWorkflowRequest(new ArrayList<String>(), "channel", channelId, proxieId);		
		productPost.setEntity(requestEntity);
		
		Map<String, String> channelResultMap = new HashMap<String, String>();
		Map<String, List<YTVideoWorkflowResponseDTO>> videoResultMap = new HashMap<String, List<YTVideoWorkflowResponseDTO>>();
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine of youtube uploaded video by channelId workflow {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity != null) {
            	
            	ObjectMapper mapper = new ObjectMapper();
            	String result = EntityUtils.toString(entity);
            	
            	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
            	JsonNode jsondata = jsonNodeName.get("object");
            	JsonNode firstLastnameObj = jsondata.get("id");
                String workFlowId = firstLastnameObj.asText();
                
                log.info("result of youtube uploaded video by channelId workflow {}", result);
                log.info("youtube uploaded video by channelId workflow Id {}", workFlowId);
                
                boolean isComplete = false;
                while (isComplete == false) {
                	log.info("wait 1 minute to complete execution of youtube channelId workflow..");
                	Thread.sleep(10000);

                	channelResultMap = fetchResultChannelVideoDetail1(workFlowId);
					
					if (channelResultMap != null) {
						String statusList = channelResultMap.get("status");
						if (StringUtils.isNotBlank(statusList)) {
							isComplete = statusList.equalsIgnoreCase("false")?false:true;
						} else {
							isComplete = true;
						}
					}
					 
                	log.info("1st workflow isComplete {}", isComplete);
                }
                
                log.info("2nd wordkflow calling start!!");
                URIBuilder workFlowUriBuilder2 = new URIBuilder(baseUrl + "/workflows/524b8949-5d57-4bcf-abdb-c23ef1dd66ab/executions");
        		workFlowUriBuilder2.setParameter("status", "active");
        		String url2 = workFlowUriBuilder2.build().toString();
        		String workFlowId2 = "";
        		HttpGet getRequest = new HttpGet(url2);
                getRequest.addHeader("Token", token);
                
                try (CloseableHttpResponse response2 = httpClient.execute(getRequest)) {

                    HttpEntity entity2 = response2.getEntity();

                    if (entity2 != null) {
                    	ObjectMapper mapper2 = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    	String result2 = EntityUtils.toString(entity2);
                    	log.info("result {}", result2);
                    	
                    	JsonNode jsonNodeName2 = mapper2.readValue(result2, JsonNode.class);
                    	JsonNode jsondata2 = jsonNodeName2.get("objects");
                    	JsonNode jsonObj2 = jsondata2.get(0);
                    	JsonNode id2 = jsonObj2.get("id");
                        workFlowId2 = id2.asText();
                    }

                } catch (ClientProtocolException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
                
                log.info("2nd wordkflow calling start!!");
                Map<String, String> playListResultMap = new HashMap<String, String>();
                isComplete = false;
                while (isComplete == false) {
                	log.info("wait 1 minute to complete execution of youtube channelId workflow..");
                	Thread.sleep(10000);

                	playListResultMap = fetchResultChannelVideoDetails2(workFlowId2);
					
                	if (playListResultMap != null) {
						String statusList = playListResultMap.get("status");
						if (StringUtils.isNotBlank(statusList)) {
							isComplete = statusList.equalsIgnoreCase("false")?false:true;
						} else {
							isComplete = true;
						}
					}
					 
                	log.info("2nd workflow isComplete {}", isComplete);
                }
                
                String isError = playListResultMap.get("isError");
                if (StringUtils.isNotBlank(isError) && isError.equalsIgnoreCase("true")) {
                	log.info("3rd wordkflow will not call!!");
                }else {
                
	                log.info("3rd wordkflow calling start!!");
	                
	                URIBuilder workFlowUriBuilder3 = new URIBuilder(baseUrl + "/workflows/2122af32-f2d0-4681-93f2-25ee4fe510c3/executions");
	        		workFlowUriBuilder3.setParameter("status", "active");
	        		String url3 = workFlowUriBuilder3.build().toString();
	        		String workFlowId3 = "";
	        		HttpGet getRequest3 = new HttpGet(url3);
	        		getRequest3.addHeader("Token", token);
	                
	                try (CloseableHttpResponse response3 = httpClient.execute(getRequest3)) {
	
	                    HttpEntity entity3 = response3.getEntity();
	
	                    if (entity3 != null) {
	                    	ObjectMapper mapper3 = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	                    	String result3 = EntityUtils.toString(entity3);
	                    	log.info("result {}", result3);
	                    	
	                    	JsonNode jsonNodeName3 = mapper3.readValue(result3, JsonNode.class);
	                    	JsonNode jsondata3 = jsonNodeName3.get("objects");
	                    	JsonNode firstLastnameObj3 = jsondata3.get(0).get("id");
	                        workFlowId3 = firstLastnameObj3.asText();
	                    }
	
	                } catch (ClientProtocolException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		} catch (IOException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	                
	                isComplete = false;
	                while (isComplete == false) {
	                	log.info("wait 1 minute to complete execution of user post workflow..");
	                	Thread.sleep(10000);
	
	                	videoResultMap = fetchResultChannelVideoDetails3(workFlowId3);
						
						if (videoResultMap != null) {
							List<YTVideoWorkflowResponseDTO> statusList = videoResultMap.get("status");
							if (statusList != null && statusList.size() > 0) {
								isComplete = statusList.get(0).getStatus().equalsIgnoreCase("false") ? false : true;
							} else {
								isComplete = true;
							}
						}
						 
	                	log.info("3rd workflow isComplete {}", isComplete);
	                }
                }
                
                System.out.println("videoResultMap:: "+videoResultMap);
               
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<YTVideoWorkflowResponseDTO> videoList = videoResultMap.get("status");
		if(videoList != null) {
			saveYoutubeFeedToDB(videoList, userId);
		}
	}

	private void saveYoutubeFeedToDB(List<YTVideoWorkflowResponseDTO> resultList, Long userId ) {
		
		log.info("saveYoutubeFeedToDB called..."+userId);
		
		List<UserYoutubeFeed1> youtubeFeedData = new ArrayList<UserYoutubeFeed1>();
		List<String> videoIds = new ArrayList<String>();
		
		val dbExistingMap = new HashMap<String, UserYoutubeFeed1>();
		
		for (YTVideoWorkflowResponseDTO videoDTO: resultList) {
			videoIds.add(videoDTO.getVideo_id());
		}
		
		List<UserYoutubeFeed1> dbList = userYoutubeFeedRepository.findByVideoIdIn(videoIds);
		if(dbList != null && dbList.size() > 0) {
			for(UserYoutubeFeed1 userYoutubeFeed : dbList) {
				dbExistingMap.put(userYoutubeFeed.getVideoId(), userYoutubeFeed);
			}
		}
		
		for (YTVideoWorkflowResponseDTO videoDTO: resultList) {
			videoDTO = validateWorkflowDto(videoDTO);
			
			UserYoutubeFeed1 youtubeFeed = new UserYoutubeFeed1();
			youtubeFeed.setUserId(userId);
			youtubeFeed.setVideoId(videoDTO.getVideo_id());
			youtubeFeed.setChannel_id(videoDTO.getChannel_id());
			youtubeFeed.setTitle(videoDTO.getTitle());
			youtubeFeed.setText(videoDTO.getText());
			youtubeFeed.setTotal_views(StringUtils.isBlank(videoDTO.getViewCount()) ? 0 : Integer.parseInt(videoDTO.getViewCount()));
			youtubeFeed.setTotal_likes(StringUtils.isBlank(videoDTO.getLikeCount()) ? 0 : Integer.parseInt(videoDTO.getLikeCount()));
			youtubeFeed.setThumb(videoDTO.getThumb());
			youtubeFeed.setVideo_url("https://www.youtube.com/watch?v="+videoDTO.getVideo_id());
			youtubeFeed.setPrivacy(videoDTO.getPrivacyStatus());
			youtubeFeed.setDownloadDate(LocalDate.now());
			
			String date = videoDTO.getPublishedAt() == null ? "" : videoDTO.getPublishedAt();
			if(StringUtils.isNotBlank(date)) {
				date = date.substring(0,10);
				LocalDate localDate = LocalDate.parse(date);
				youtubeFeed.setCreatedDate(localDate);
			}
			
			val existingRecord = dbExistingMap.get(videoDTO.getVideo_id());
			 if(existingRecord != null) {
				 
				 existingRecord.setTitle(videoDTO.getTitle());
				 existingRecord.setText(videoDTO.getText());
				 existingRecord.setTotal_views(StringUtils.isBlank(videoDTO.getViewCount()) ? 0 : Integer.parseInt(videoDTO.getViewCount()));
				 existingRecord.setTotal_likes(StringUtils.isBlank(videoDTO.getLikeCount()) ? 0 : Integer.parseInt(videoDTO.getLikeCount()));
				 existingRecord.setThumb(videoDTO.getThumb());
				 existingRecord.setVideo_url("https://www.youtube.com/watch?v="+videoDTO.getVideo_id());
				 existingRecord.setPrivacy(videoDTO.getPrivacyStatus());
				
				 youtubeFeedData.add(existingRecord);
			 }else {
				 youtubeFeedData.add(youtubeFeed);
			 }
			 log.info("youtube Video ID {}", videoDTO.getVideo_id());
			
		}
		
		log.info("saving bulk data into database {}.."+youtubeFeedData.size());
		log.info("saving bulk data into database {}.."+youtubeFeedData);
		userYoutubeFeedRepository.saveAll(youtubeFeedData);		
		log.info("saving data into database done..");
		
	}
	
	public YTVideoWorkflowResponseDTO validateWorkflowDto(YTVideoWorkflowResponseDTO dto) {
		
		if(StringUtils.isEmpty(dto.getVideo_id())) {
			dto.setVideo_id("");
		}
		
		if(StringUtils.isEmpty(dto.getText())) {
			dto.setText("");
		}
		
		if(StringUtils.isEmpty(dto.getViewCount())) {
			dto.setViewCount("0");
		}
		
		if(StringUtils.isEmpty(dto.getLikeCount())) {
			dto.setLikeCount("0");
		}
		
		if(StringUtils.isEmpty(dto.getPrivacyStatus())) {
			dto.setPrivacyStatus("");
		}
		
		if(StringUtils.isEmpty(dto.getPublishedAt())) {
			dto.setPublishedAt("");
		}
		
		if(StringUtils.isEmpty(dto.getThumb())) {
			dto.setThumb("");
		}
		
		if(StringUtils.isEmpty(dto.getTitle())) {
			dto.setTitle("");
		}
		
		return dto;
	}
	
	public Map<String, String> fetchResultFromChannelWrokFlow(String workFlowId) throws URISyntaxException {
		boolean status  = false;
		log.info("youtube channelId workflow execution start...");
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/executions/"+workFlowId);
		String url = workFlowUriBuilder.build().toString();
		
		Map<String, String> channelResultMap = new HashMap<String, String>();
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine of youtube channelId workflow {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity == null) {
            	return channelResultMap;
            }
            	
        	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	String result = EntityUtils.toString(entity);
        	
        	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
        	JsonNode jsondata = jsonNodeName.get("object");
        	JsonNode completedAtObj = jsondata.get("completed_at");
        	JsonNode resultsObj = jsondata.get("results");
            String completedAt = completedAtObj.asText();
            
            log.info("result of youtube channelId workflow {}", result);
            log.info("completedAt {}", completedAt);
            
            if(StringUtils.isNotBlank(completedAt)) {
            	if(completedAt.equalsIgnoreCase("None") || completedAt.equalsIgnoreCase("null")) {
            		status  = false;
            	}else {
            		channelResultMap = new HashMap<String, String>();
            		status  = true;
            	}
            }else {
            	status  = false;
            }
            
            if(status == false) {
            	channelResultMap = new HashMap<String, String>();
        		channelResultMap.put("status", "false");
        		return channelResultMap;
            }
            System.out.println("resultsObj:: "+resultsObj+" status:: "+status);
            
            if(resultsObj != null && status) {
            	
            	JsonNode resultUrlObj = resultsObj.get(0).get("url");
            	String urlStr = resultUrlObj.asText();
				
				URL jsonUrl = new URL(urlStr);
				log.info("jsonUrl of youtube channelId result {}", jsonUrl);
				
				StevesieYTChannelWorkflowResult[] jsonNodeResult = mapper.readValue(jsonUrl, StevesieYTChannelWorkflowResult[].class);
				
				for(StevesieYTChannelWorkflowResult request : jsonNodeResult) {

					String channelIdStr = request.getInput().getChannel_id();
					String uploadId = request.getResponse().getUploads();
					log.info("upload Id {}", uploadId);
					if (StringUtils.isNotBlank(uploadId)) {
						channelResultMap.put(channelIdStr, uploadId);
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
		log.info("finished youtube channel uploaded video workflow..");
		return channelResultMap;
	}
	
	public Map<String, String> fetchResultChannelVideoDetail1(String workFlowId) throws URISyntaxException {
		boolean status  = false;
		log.info("youtube channelId workflow execution start...");
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/executions/"+workFlowId);
		String url = workFlowUriBuilder.build().toString();
		
		Map<String, String> channelResultMap = new HashMap<String, String>();
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine of youtube channelId workflow {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity == null) {
            	return channelResultMap;
            }
            	
        	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	String result = EntityUtils.toString(entity);
        	
        	JsonNode jsonNodeName = mapper.readValue(result, JsonNode.class);
        	JsonNode jsondata = jsonNodeName.get("object");
        	JsonNode completedAtObj = jsondata.get("completed_at");
        	JsonNode resultsObj = jsondata.get("results");
            String completedAt = completedAtObj.asText();
            
            log.info("result of youtube channelId workflow {}", result);
            log.info("completedAt {}", completedAt);
            
            if(StringUtils.isNotBlank(completedAt)) {
            	if(completedAt.equalsIgnoreCase("None") || completedAt.equalsIgnoreCase("null")) {
            		status  = false;
            	}else {
            		channelResultMap = new HashMap<String, String>();
            		status  = true;
            	}
            }else {
            	status  = false;
            }
            
            if(status == false) {
            	channelResultMap = new HashMap<String, String>();
        		channelResultMap.put("status", "false");
        		return channelResultMap;
            }
            System.out.println("resultsObj:: "+resultsObj+" status:: "+status);
            
            if(resultsObj != null && status) {
            	
            	JsonNode resultUrlObj = resultsObj.get(0).get("url");
            	String urlStr = resultUrlObj.asText();
				
				URL jsonUrl = new URL(urlStr);
				log.info("jsonUrl of youtube channelId result {}", jsonUrl);
				
				StevesieYTChannelWorkflowResult[] jsonNodeResult = mapper.readValue(jsonUrl, StevesieYTChannelWorkflowResult[].class);
				
				for(StevesieYTChannelWorkflowResult request : jsonNodeResult) {

					String channelIdStr = request.getInput().getChannel_id();
					String uploadId = request.getResponse().getUploads();
					log.info("upload Id {}", uploadId);
					if (StringUtils.isNotBlank(uploadId)) {
						channelResultMap.put(channelIdStr, uploadId);
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
		log.info("finished youtube channel uploaded video workflow..");
		return channelResultMap;
	}
	
	public Map<String, List<YTVideoWorkflowResponseDTO>> fetchResultChannelVideoDetails3(String workFlowId) throws URISyntaxException {
		boolean status  = false;
		log.info("3 fetchResultFromVideoListWrokFlow workflow execution start...");
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/executions/"+workFlowId);
		String url = workFlowUriBuilder.build().toString();
		
		Map<String, List<YTVideoWorkflowResponseDTO>> videoResultMap = new HashMap<String, List<YTVideoWorkflowResponseDTO>>();
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine of user post workflow {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity == null) {
            	return videoResultMap;
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
            		videoResultMap = new HashMap<String, List<YTVideoWorkflowResponseDTO>>();
            		status  = true;
            	}
            }else {
            	status  = false;
            }
            
            if(status == false) {
            	videoResultMap = new HashMap<String, List<YTVideoWorkflowResponseDTO>>();
            	val emptyList = new ArrayList<YTVideoWorkflowResponseDTO>();
            	val ytVideoWorkflowResponseDTO = new YTVideoWorkflowResponseDTO();
            	ytVideoWorkflowResponseDTO.setStatus("false");
            	emptyList.add(ytVideoWorkflowResponseDTO);
            	videoResultMap.put("status", emptyList);
        		return videoResultMap;
            }
            System.out.println("resultsObj:: "+resultsObj+" status:: "+status);
            
            if(resultsObj != null && status) {
            	
            	JsonNode resultUrlObj = resultsObj.get(0).get("url");
            	String urlStr = resultUrlObj.asText();
				
				URL jsonUrl = new URL(urlStr);
				log.info("jsonUrl of user profile result {}", jsonUrl);
				
				StevesieYTVideoWorkflowResult[] jsonNodeResult = mapper.readValue(jsonUrl, StevesieYTVideoWorkflowResult[].class);
				val videoList = new ArrayList<YTVideoWorkflowResponseDTO>();
				for(StevesieYTVideoWorkflowResult request : jsonNodeResult) {
					YTVideoWorkflowResponseDTO videoObj = new YTVideoWorkflowResponseDTO();
					videoObj = request.getResponse();
					videoObj.setVideo_id(request.getInput().getVideo_id());
					videoObj.setStatus("true");
					videoList.add(request.getResponse());
				}
				
				videoResultMap.put("status", videoList);
            	
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("finished user post workflow..");
		return videoResultMap;
	}
	
	public Map<String, String> fetchResultChannelVideoDetails2(String workFlowId) throws URISyntaxException {
		boolean status  = false;
		log.info("Fetch Result Channel VideoDetails2 workflow execution start...");
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/executions/"+workFlowId);
		String url = workFlowUriBuilder.build().toString();
		
		Map<String, String> playListResultMap = new HashMap<String, String>();
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine of user post workflow {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity == null) {
            	return playListResultMap;
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
            		playListResultMap = new HashMap<String, String>();
            		status  = true;
            	}
            }else {
            	status  = false;
            }
            
            if(status == false) {
            	playListResultMap = new HashMap<String, String>();
            	playListResultMap.put("status", "false");
        		return playListResultMap;
            }
            System.out.println("resultsObj:: "+resultsObj+" status:: "+status);
            
            if(resultsObj != null && status) {
            	playListResultMap.put("status", "true");
            	
            	JsonNode resultUrlObj = resultsObj.get(0).get("url");
            	String urlStr = resultUrlObj.asText();
				
				URL jsonUrl = new URL(urlStr);
				log.info("jsonUrl of user profile result {}", jsonUrl);
				String outputUrl = jsonUrl.toString();
				if(StringUtils.isNotBlank(outputUrl)) {
					if(outputUrl.contains("execution_1.json")) {
						JsonNode jsonNodeName1 = mapper.readValue(jsonUrl, JsonNode.class);
			        	JsonNode inputs = jsonNodeName1.get("inputs");
			        	JsonNode channelId = inputs.get("channel_id");
			            String channel_id = channelId.asText();
						log.info("Playlist not found for channel Id {}", channel_id);
						playListResultMap.put("isError", "true");
					}else {
						StevesieYTChannelWorkflowResult[] jsonNodeResult = mapper.readValue(jsonUrl, StevesieYTChannelWorkflowResult[].class);
						
						for(StevesieYTChannelWorkflowResult request : jsonNodeResult) {
							String id = request.getResponse().getId();
							if(StringUtils.isNotBlank(id)) {
								String videoId = request.getResponse().getPlaylistId();
								if(StringUtils.isNotBlank(videoId)) {
									playListResultMap.put(videoId, videoId);
									playListResultMap.put("isError", "false");
								}
							}else {
								log.info("Playlist not found for channel Id {}", request.getInput().getChannel_id());
							}
							/*
							 * String videoId = request.getResponse().getVideoId();
							 * System.out.println("videoId:: "+videoId); if
							 * (StringUtils.isNotBlank(videoId)) { playListResultMap.put(videoId, videoId);
							 * }
							 */

						}
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
		log.info("finished user post workflow..");
		return playListResultMap;
	}
	
	public Map<String, String> fetchResultBychannelId(String channelId) throws URISyntaxException {
		boolean status  = false;
		
		URIBuilder workFlowUriBuilder = new URIBuilder(baseUrl + "/workflows/4fceec2c-7fc6-4460-85fe-bd700ed29af8/executions");
		String url = workFlowUriBuilder.build().toString();
		
		StringEntity requestEntity = generateWorkflowRequest(new ArrayList<String>(), "channel", channelId, "");
		
		Map<String, String> channelResultMap = new HashMap<String, String>();
		
		HttpPost productPost = new HttpPost(url);
		productPost.addHeader("Token", token);
		productPost.setEntity(requestEntity);
		
		try (CloseableHttpResponse response = httpClient.execute(productPost)) {

            log.info("statusLine of user profile workflow {} "+response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity == null) {
            	return channelResultMap;
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
            	channelResultMap = new HashMap<String, String>();
        		channelResultMap.put("status", "false");
        		return channelResultMap;
            }
            
            
            if(resultsObj != null && status) {
            	
            	JsonNode resultUrlObj = resultsObj.get(0).get("url");
            	String urlStr = resultUrlObj.asText();
				
				URL jsonUrl = new URL(urlStr);
				log.info("jsonUrl of user profile result {}", jsonUrl);
				
				StevesieYTChannelWorkflowResult[] jsonNodeResult = mapper.readValue(jsonUrl, StevesieYTChannelWorkflowResult[].class);
				
				for(StevesieYTChannelWorkflowResult request : jsonNodeResult) {

					String channelIdStr = request.getInput().getChannel_id();
					String playListId = request.getResponse().getUploads();
					if (StringUtils.isNotBlank(playListId)) {
						channelResultMap.put(channelIdStr, playListId);
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
		return channelResultMap;
	}

	private StringEntity generateWorkflowRequest(List<String> lstString, String type, String channelId, String proxieId) {
		log.info("generating workflow request...");
		
		val proxy = new StevesieWorkflowProxy();
		proxy.setLocation("nyc");
		proxy.setType("dedicated");
		//proxy.setId(proxieId);
		
		val input = new StevesieYoutubeWorkflowRequestForUsername();
		val inputChannelId = new StevesieWorkflowChannelIdInputs();
		
		if (type.equalsIgnoreCase("username")) {
			input.setUsername(lstString.get(0));
			input.setPart("id,snippet,contentDetails,statistics");
		} else {
			inputChannelId.setChannel_id(channelId);
			//inputChannelId.setPart("id,snippet,contentDetails,statistics");
			inputChannelId.setAuth_token(gogleToken);
		}
		
		input.setAuth_token(gogleToken);

		StringEntity requestEntity = null;
		try {
			if (type.equalsIgnoreCase("username")) {
				val request = new StevesieYoutubeWorkflowRequest();
				request.setFormat("json");
				request.setProxy(proxy);
				request.setInputs(input);
				requestEntity = new StringEntity(request.toJson());
			}
			if (type.equalsIgnoreCase("channel")) {
				val requestChannelId = new StevesieYoutubeWorkflowRequestForChannelId();
				requestChannelId.setOutput_aggregation_format("json");
				requestChannelId.setProxy(proxy);
				requestChannelId.setSend_completion_email(false);
				requestChannelId.setExecution_parameters(inputChannelId);
				requestEntity = new StringEntity(requestChannelId.toJson());
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
	

}
