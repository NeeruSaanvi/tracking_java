


package com.tracker.services.impls;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tracker.commons.models.CompanyRef;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserProAppDetails;
import com.tracker.commons.models.UserProAppDetailsSports;
import com.tracker.services.repositories.CompanyRefRepository;
import com.tracker.services.repositories.UserProAppDetailsRepository;
import com.tracker.services.repositories.UserProAppDetailsSportsRepository;
import com.tracker.services.repositories.UserRepository;
import com.tracker.services.utils.EnvironmentUtil;

@Service
public class ApplicationService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserProAppDetailsRepository userProAppDetailsRepository;

	@Autowired
	private CompanyRefRepository companyRefRepository;

	@Autowired
	private UserProAppDetailsSportsRepository userProAppDetailsSportsRepository;
	
	@Autowired
	private EnvironmentUtil environmentUtil; 
	

	private final CloseableHttpClient httpClient = HttpClients.createDefault();

	public void deleteApplication_AmbassadorLink(Long userId, Long companyId) throws ClientProtocolException, IOException {
		String sports = "Fishing";  //userRepository.getSports(companyId);
		
		String requestURL = "http://ambassadorlink.com/webservice/index.php/api/application_ref_status/"+userId+"/"+companyId+"/Deleted/"+sports;
		
		HttpGet request = new HttpGet(requestURL);
		
		httpClient.execute(request);		
		
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void deleteApplication(Long userId, Long companyId) throws ClientProtocolException, IOException {
	
		User user = userRepository.findByCompanyIdAndUserId(companyId.intValue(), userId);
		if(user != null) {
			user.setStatus("Deleted");
			userRepository.save(user);
		}
		  
		boolean sportsman = environmentUtil.containsEnv("sportsman"); //Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
		if(sportsman) {
			UserProAppDetailsSports userProAppDetails = userProAppDetailsSportsRepository.findByUserIdAndCompanyId(userId, companyId);
			if(userProAppDetails != null) {
				userProAppDetails.setStatus("Deleted");
				userProAppDetailsSportsRepository.save(userProAppDetails);
			}
		}else {
			UserProAppDetails userProAppDetails = userProAppDetailsRepository.findByUserIdAndCompanyId(userId, companyId);
			if(userProAppDetails != null) {
				userProAppDetails.setStatus("Deleted");
				userProAppDetailsRepository.save(userProAppDetails);
			}
		}

		CompanyRef companyRef =  companyRefRepository.findByUseridAndCompanyId(userId, companyId);
		if(companyRef != null) {
			companyRef.setRefStatus("Deleted");
			companyRefRepository.save(companyRef);
		}
	}
}
