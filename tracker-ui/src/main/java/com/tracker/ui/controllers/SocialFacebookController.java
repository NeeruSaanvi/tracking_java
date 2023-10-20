package com.tracker.ui.controllers;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.models.UserMedia;
import com.tracker.services.repositories.UserMediaRepository;
import com.tracker.services.repositories.UserRepository;
import com.tracker.services.utils.SessionVariables;
import com.tracker.services.utils.UserUtils;

import antlr.collections.List;


@Controller
public class SocialFacebookController {
	
	@Value("${server.host}")
	String url;
	@Value("${server.port}")
	String port;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserMediaRepository userMediaRepository;
	 @Autowired
	    private ServletContext servletContext;

	private FacebookConnectionFactory factory = new FacebookConnectionFactory("410761562312101",
			"af3b1e569a37c23308940a3733c094a5");

//	private FacebookConnectionFactory factory = new FacebookConnectionFactory("345056937206187",
//			"593a873a2617b0c13ca81737c9d304a1");

	@RequestMapping("/link")
	public ModelAndView launch() {
	//UserMedia userMedia = userMediaRepository.findByUserid(UserUtils.getLoggedInUserId());
	UserMedia userMedia = userMediaRepository.getAccessToken(UserUtils.getLoggedInUserId(),"facebook");

		if(userMedia!= null && !TextUtils.isEmpty(userMedia.getAccessToken())) {	
			com.tracker.commons.models.User userObject = userRepository.findByUserId(UserUtils.getLoggedInUserId());
			ModelAndView model = new ModelAndView("link");

//			userMedia.setUserid(userObject.getUserId());
//			userMedia.setSocialType("facebook");
//			userMedia.setAccess_token(token);
//			userMedia.setUsername(userObject.getFirstName());
//			
//			userMedia.setEmail(userObject.getEmail());
//
//			userMediaRepository.save(userMedia);
			model.addObject("user", userMedia);

			return model;

		}
		
		return new ModelAndView("link");
	}
	
	@RequestMapping("/")
	public ModelAndView firstPage() {
		return new ModelAndView("link_account");
	}

	@GetMapping(value = "/useApplication")
	public String Link() {
		//ModelAndView model = new ModelAndView("link");

		
		OAuth2Operations operations = factory.getOAuthOperations();
		OAuth2Parameters params = new OAuth2Parameters();
		
		String base_url = 		url;
		if(base_url.contains("localhost")) {
			base_url+=":"+port;
		}
		//params.setRedirectUri("http://"+basepath +"/forwardLogin");
	// params.setRedirectUri("http://localhost:8080/forwardLogin");
	 params.setRedirectUri("https://"+base_url+ "/forwardLogin");

		
		params.setScope("email,public_profile");
		String url = operations.buildAuthenticateUrl(params);
		System.out.println("The URL is" + url);
		
		return "redirect:" + url;

	}
	@GetMapping(value = "/relink")
	public ModelAndView relink() {

		userMediaRepository.deleteAccessToken(UserUtils.getLoggedInUserId(),"facebook","");

		UserMedia userMedia = new UserMedia();
		userMedia.setEmail("");
		userMedia.setAccessToken("");

		userMedia.setUsername("");
		ModelAndView model = new ModelAndView("link");

//		userMedia.setUserid(userObject.getUserId());
//		userMedia.setSocialType("facebook");
//		userMedia.setAccess_token(token);
//		userMedia.setUsername(userObject.getFirstName());
//		
//		userMedia.setEmail(userObject.getEmail());
//
//		userMediaRepository.save(userMedia);
		model.addObject("user", userMedia);

		return model;

	}

	@RequestMapping(value = "/forwardLogin")
	public ModelAndView prodducer(@RequestParam("code") String authorizationCode) {
		try {
		OAuth2Operations operations = factory.getOAuthOperations();
		String base_url = 		url;
		if(base_url.contains("localhost")) {
			base_url+=":"+port;
		}
		AccessGrant accessToken = operations.exchangeForAccess(authorizationCode,"https://"+base_url+ "/forwardLogin",
				null);
		
		
		com.tracker.commons.models.User userObject = userRepository.findByUserId(UserUtils.getLoggedInUserId());
		
		UserMedia userMedia = new UserMedia();
		
		
		

		//userMediaRepository.saveAccessToken(UserUtils.getLoggedInUserId(),"facebook", accessToken.getAccessToken());
		
	    //userRepository.saveAccessToken(UserUtils.getLoggedInUser(),"facebook",accessToken.getAccessToken());
		Connection<Facebook> connection = factory.createConnection(accessToken);
		Facebook facebook = connection.getApi();
		String[] fields = { "id", "email", "first_name", "last_name" };
		User userProfile = facebook.fetchObject("me", User.class, fields);
		
		UserMedia userMedia1 = userMediaRepository.getAccessToken(UserUtils.getLoggedInUserId(),"facebook");
		ModelAndView model = new ModelAndView("link");

		if(userMedia1 == null ) {	
		
		userMedia.setUserid(userObject.getUserId());
		userMedia.setSocialType("facebook");
		userMedia.setAccessToken(accessToken.getAccessToken());
		userMedia.setSociald(userProfile.getId());
		userMedia.setUsername(userProfile.getFirstName() + userProfile.getLastName());
		
		userMedia.setEmail(userProfile.getEmail());

		userMediaRepository.save(userMedia);
		
		model.addObject("user", userMedia);
		}else {
			userMediaRepository.updateAccessToken(UserUtils.getLoggedInUserId(),"facebook",accessToken.getAccessToken());
			userMedia1.setAccessToken(accessToken.getAccessToken());
			model.addObject("user", userMedia1);

		}
		return model;
		}catch(Exception e) {
			e.printStackTrace();
		}
 return null ;
	}
	@RequestMapping(value = "/afterlogin")
	public String authenticated() {
		
		return "authenticate";

	}


}
	



	