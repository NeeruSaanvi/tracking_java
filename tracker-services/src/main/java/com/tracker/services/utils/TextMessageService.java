package com.tracker.services.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.commons.dtos.SMSMessageResponse;
import com.tracker.commons.exceptions.UserException;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Sample response: 
 * {"code":-3,"message":"Required parameter is not specified"}
 * {"code":1,"message":"The request succeeded","smsid":"5cf88fbe0a975a117dd777c4"}
 * 
 * For More Details: https://simpletexting.com/api/docs/
 * 
 * @author viral
 *
 */

@Service
@Slf4j
public class TextMessageService {

	@Value("${sms.url}")
	private String smsUrl;

	@Value("${sms.token}")
	private String token;
	
	@Value("${default.phone}")
	private String defaultPhone;
	
	@Value("${tiny.url}")
	private String tinyUrl;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private GoogleCloudService googleCloudService;
	
	private final CloseableHttpClient httpClient = HttpClients.createDefault();

	public void sendTextMessage(String phone, String message) {
		
		if (this.environment.getActiveProfiles()[0].equals("default")) {
			phone = defaultPhone;
		}
		
		log.info("Sending SMS Message to {} with message {}", phone, message);
		
		phone = validatePhone(phone);
		validateMessage(message, phone, "sms");

		val restTemplate = new RestTemplate();
		val response = restTemplate.getForObject(smsUrl + "/send?token={token}&phone={phone}&message={message}",
				SMSMessageResponse.class, token, phone, message);

		validateResponse(response, phone);

		log.info("SMS Response status {}", response);
	}
	
	public void sendTextMessage(String phone, String message, String mediaUrl) throws IOException {
		
		if (this.environment.getActiveProfiles()[0].equals("default")) {
			phone = defaultPhone;
		}
		//phone = phone.replaceAll("-", "");
		log.info("Sending SMS Message to {} with message {}", phone, message);

		phone = validatePhone(phone);
		validateMessage(message, phone, "mms");
		
		String smsUrl = builtSmsUrl(phone, message, mediaUrl);
		
		HttpPost request = new HttpPost(smsUrl);
		CloseableHttpResponse response = httpClient.execute(request);

        log.info("statusLine {} "+response.getStatusLine().toString());

        HttpEntity entity = response.getEntity();
        Header headers = entity.getContentType();
        log.info("header {} ", headers);

        if (entity != null) {
            String result = EntityUtils.toString(entity);           
            log.info("result {}", result);
            log.info("MMS send successfully to phone "+phone);
        }
		
	}

	public String getMediaUrl(byte[] inputSchema, String inputSchemaFileName) throws IOException {
		URL url = googleCloudService.getMMSBucketImageUrl("user_mms", inputSchema, inputSchemaFileName);
		
		String mediaUrl = "";
		if(url != null) {
			mediaUrl  = url.toString();
			
			mediaUrl = tinyUrl(mediaUrl);
		}
		return mediaUrl;
	}
	
	private String tinyUrl(String url) throws IOException {
		String shortUrl = "";
		
		String token = getShortUrlToken();
	    
		val restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		Map<String, String> urlParameters = new HashMap<>();
	    urlParameters.put("url", url);
	    
	    val requestBody = new org.springframework.http.HttpEntity<Map<String, String>>(
	    		urlParameters, headers);
	    
	    val response = restTemplate.postForEntity("https://app.bl.ink/api/v4/95806/links", requestBody, String.class).getBody();
	    
	    val objectMapper = new ObjectMapper();

	    val rootNode = objectMapper.readTree(response);
	    val objectNode = rootNode.get("objects");
	    shortUrl = objectNode.get(0).get("short_link").asText();
	    
		return shortUrl;
	}

	private String getShortUrlToken() throws IOException {
		val restTemplate = new RestTemplate();
		val headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	    Map<String, String> urlParameters = new HashMap<>();
	    urlParameters.put("email", "graham@strydeconcepts.com");
	    urlParameters.put("password", "StrydeTXT2020");
	    
	    val response = restTemplate.postForEntity("https://app.bl.ink/api/v4/access_token", urlParameters, String.class).getBody();
	    
	    val objectMapper = new ObjectMapper();

	    val rootNode = objectMapper.readTree(response);
	    val token = rootNode.get("access_token").asText();
	    //System.out.println("token== "+token);
	    return token;
	}
	
	/*private String shorterUrl(String url){
		String tinyUrlLookup = tinyUrl + url;
		BufferedReader reader;
		String tinyUrl = "";
		try {
			reader = new BufferedReader(new InputStreamReader(new URL(tinyUrlLookup).openStream()));
			tinyUrl = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tinyUrl;
	}*/

	private String builtSmsUrl(String phone, String message, String mediaUrl) {
		URIBuilder smsUriBuilder = null;
		try {
			smsUriBuilder = new URIBuilder(smsUrl+"/sendmms");
			smsUriBuilder.addParameter("token", token);
			smsUriBuilder.addParameter("phone", phone);
			smsUriBuilder.addParameter("message", message);
			smsUriBuilder.addParameter("mediaUrl", mediaUrl);

		} catch (URISyntaxException e1) {
			log.error("SMS URL build call failed {} " +e1.getMessage());
		}

		String smsUrl = "";
		try {
			smsUrl = smsUriBuilder.build().toString();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			log.error("SMS URL build call failed {} " +e1.getMessage());
		}
		
		if (StringUtils.isBlank(smsUrl)) {
			throw new UserException("Invalid SMS request");
		}
		
		return smsUrl;
	}

	private void validateResponse(final SMSMessageResponse response, String phone) {
		if (response.getCode() != 1) {
			log.error("SMS call failed with the error code {} and a message from the provider {} for phone {}", response.getCode(),
					response.getMessage(), phone);

			if (response.getCode() == -5) {
				throw new UserException("Invalid phone number "+phone);
			}

			if (response.getCode() == 0) {
				throw new UserException("This phone number has unsubscribed globally "+phone);
			}

			if (response.getCode() == -304) {
				throw new UserException("Message length can not exceed 160 characters "+phone);
			}

			throw new UserException("System could not send sms message "+phone);
		}else if(response.getCode() == 1){
			log.info("Text message sent successfully to phone "+phone);
		}
	}

	private String validatePhone(String phone) {
		if (StringUtils.isBlank(phone)) {
			throw new UserException("Invalid "+phone+" number, it must be 10 digits");
		}
		
		phone = phone.replaceAll("-", "");
		phone = phone.replaceAll("[()]", "");
		phone = phone.replaceAll("\\s", "");

		if (phone.length() == 11) {
			phone = phone.substring(1);
		}

		if (phone.length() != 10) {
			throw new UserException("Invalid "+phone+" number, it must be 10 digits");
		}
		return phone;
	}

	private void validateMessage(String message, String phone, String messageType) {
		if (StringUtils.isBlank(message)) {
			throw new UserException(" Message field can not be empty while sending SMS to phone number "+phone);
		}
		
		if(messageType.equalsIgnoreCase("sms")) {
			if (message.length() > 160) {
				throw new UserException(" Message length can not exceed 160 characters while sending SMS to phone number "+phone);
			}
		}else {

			if (message.length() > 1500) {
				throw new UserException(" Message length can not exceed 1500 characters while sending SMS to phone number "+phone);
			}
		}
	} 
}
