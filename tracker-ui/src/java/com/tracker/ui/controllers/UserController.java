package com.tracker.ui.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.time.*;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tracker.commons.dtos.Application;
import com.tracker.commons.dtos.KeywordsDTO;
import com.tracker.commons.dtos.SportsUserProAppDetails;
import com.tracker.commons.dtos.UserProfileDTO;
import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.CompanyRef;
import com.tracker.commons.models.Team;
import com.tracker.commons.models.User;
import com.tracker.commons.models.UserFeed;
import com.tracker.commons.models.UserInstragramFeed;
import com.tracker.commons.models.UserTeam;
import com.tracker.commons.models.UserTweets;
import com.tracker.commons.models.UserYoutubeFeed;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.dto.UpdateUserDTO;
import com.tracker.services.impls.ApplicationService;
import com.tracker.services.impls.UserService;
import com.tracker.services.repositories.ApplicationRepository;
import com.tracker.services.repositories.CompanyRefRepository;
import com.tracker.services.repositories.GalleryRepository;
import com.tracker.services.repositories.UserProAppDetailsRepository;
import com.tracker.services.repositories.UserProAppDetailsSportsRepository;
import com.tracker.services.repositories.UserRepository;
import com.tracker.services.repositories.UserTeamRepository;
import com.tracker.services.utils.ExcelUtils;
import com.tracker.services.utils.GoogleCloudService;
import com.tracker.services.utils.JasperUtils;
import com.tracker.services.utils.SessionVariables;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

import lombok.val;

@Controller
public class UserController extends BaseController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	UserTeamRepository userTeamRepository;
	
	@Autowired
	private GalleryRepository galleryRepository;
	
	@Autowired
	private CompanyRefRepository companyRefRepository;
	
	@Autowired
	private UserProAppDetailsRepository userProAppDetailsRepository;
	
	@Autowired
	private UserProAppDetailsSportsRepository userProAppDetailsSportsRepository;
	
	@Autowired
	private GoogleCloudService googleCloudService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
    private Environment environment;
	
	@Autowired
	private JasperUtils jasperUtils;
	
	private final Logger log = LoggerFactory.getLogger(UserController.class);
	
	private NumberFormat numberFormat = NumberFormat.getInstance();

	
	@PutMapping("/updateProfile")
	public @ResponseBody String updateProfile(@ModelAttribute("updateUser") UpdateUserDTO updateUser) {

		log.info("Update User Profile controller called...");

		return "success";
	}
	
	@GetMapping("/rest/activateuser/{sponserId}/{userId}")
	public @ResponseBody String getActivationEmail(@PathVariable Long sponserId, @PathVariable Long userId) {
		boolean success= userService.activateUser(sponserId, userId);
		
		return success? HttpStatus.OK.toString(): HttpStatus.NOT_FOUND.toString() ;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/users")
	public ModelAndView users(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

		log.info("Manage User controller has been called...");
		
		ModelAndView model = new ModelAndView();
    	model.setViewName("users");
    	
		val teamList = (List<Team>) UserUtils.getSessionsMap().get(SessionVariables.TEAM);
		model.addObject("teamList", teamList);
		
		val state = UserUtils.getState();
		model.addObject("stateMap", state);

		return model;
	}
	
	@RequestMapping("/userProfile")
	public ModelAndView userProfile(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			@RequestParam(name = "userId") String userIdStr) throws IOException {

		log.info("Manage User controller has been called..."+userIdStr);
		
		ModelAndView model = new ModelAndView();
    	model.setViewName("userProfile");
    	
    	String keywordsFB = (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSFB);
    	String keywordsTW = (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSTW);
    	String keywordsIN = (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSIN);
    	String keywordsYT = (String)UserUtils.getSessionsMap().get(SessionVariables.KEYWORDSYT);
    	
    	Long userId = 0l;
    	List<Long> userIdList = new ArrayList<Long>();
    	
    	if(StringUtils.isNotBlank(userIdStr)) {
    		userId = Long.parseLong(userIdStr);
    		userIdList.add(userId);
    	}
    	
    	val dbUser = userService.findUserById(userId);
    	
    	String[] activeProfiles = environment.getActiveProfiles();  
    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
    	
    	List<User> userDetailsList = userService.findAllUsersByCompany(dbUser.getFirstLastName(), dbUser.getEmail(), null, true);
    	for(User user : userDetailsList) {
			String pic = user.getAtProfilePic() == null ? "" : user.getAtProfilePic();
			String profilepicURL = "assets/img/noimage.jpg";
			
			Long fb = user.getFB() == null ? 0 : user.getFB();
			Long twt = user.getTwt() == null ? 0 : user.getTwt();
			Long in = user.getInsta() == null ? 0 : user.getInsta();
			
			if(pic.equals("None") && (fb+ twt+ in) > 0) {
				
				pic = userRepository.getProfilePicMS(user.getUserId(),"facebook");
				
				if(StringUtils.isNotBlank(pic)){
					profilepicURL = pic;
				}
				else if(pic.equals("")){
					pic = userRepository.getProfilePicMS(user.getUserId(),"instagram");
					if(StringUtils.isNotBlank(pic)){
						profilepicURL = pic;
					}
				} else { 
					profilepicURL = "assets/img/noimage.jpg";
				}
			}else if(pic.equals("None") && (fb+ twt+ in) <= 0){
				profilepicURL = "assets/img/user-no-image.png";
			}else{
				profilepicURL = user.getAtProfilePic();
				profilepicURL = StringUtils.substringAfterLast(profilepicURL, "/");
				
				String bucket = "user_profilepics";
				
				if(result) {
					bucket = "user_profilepics_sports";
				}
				
				URL profilePic = googleCloudService.getBucketImageUrl(bucket,profilepicURL);
				
				if(profilePic != null) {
					profilepicURL = profilePic.toString();
				}else {
					profilepicURL = "assets/img/noimage.jpg";
				}
				
			}
			user.setProfilePic(profilepicURL);
		}
    	
    	val detailUser = userDetailsList != null ? userDetailsList.get(0) : dbUser;
    	String dobFormatter = detailUser.getDob() == null ? "" : detailUser.getDob().format(DateTimeFormatter.ofPattern("dd, MMM yyyy"));
		detailUser.setDobFormatter(dobFormatter);
		
		if (result) {
			val userProAppDetails = userProAppDetailsSportsRepository.findByUserIdAndCompanyId(userId,
					UserUtils.getLoggedInUserId());
			if (userProAppDetails != null) {
				detailUser.setShirtSize(userProAppDetails.getShirt_size());
				detailUser.setGloveSize(userProAppDetails.getGlove_size() == null ? "" : userProAppDetails.getGlove_size());
				detailUser.setDiscountCode(userProAppDetails.getDiscountCode());
			}
		} else {
			val userProAppDetails = userProAppDetailsRepository.findByUserIdAndCompanyId(userId,
					UserUtils.getLoggedInUserId());
			if (userProAppDetails != null) {
				detailUser.setShirtSize(userProAppDetails.getShirt_size());
				detailUser.setGloveSize(userProAppDetails.getGlove_size() == null ? "" : userProAppDetails.getGlove_size());
				detailUser.setDiscountCode(userProAppDetails.getDiscountCode());
			}
		}
    	
    	List<UserFeed> userFeed = userRepository.fetchFBData(UserUtils.getLoggedInUserId(), userId, keywordsFB);
    	List<UserTweets> userTweets = userRepository.fetchTWData(UserUtils.getLoggedInUserId(), userId, keywordsTW);
    	List<UserInstragramFeed> userInstragramFeed = userRepository.fetchInstaData(UserUtils.getLoggedInUserId(), userId, keywordsIN);
    	List<UserYoutubeFeed> userYoutubeFeed = galleryRepository.getYoutubeVideo(UserUtils.getLoggedInUserId(), keywordsYT, userIdList);
    	
    	Integer fbFeedCount = 0, fbLikeCount = 0, fbCommentCount = 0, fbShareCount = 0;
    	Integer twFeedCount = 0, twReTweetCount = 0, twFavCount = 0;
    	Integer inFeedCount = 0, inLikeCount = 0, inCommentCount = 0;
    	
    	if(userFeed != null) {
    		fbFeedCount = userFeed.size();
	    	for(UserFeed uf : userFeed) {
	    		fbLikeCount += uf.getLikes_count();
	    		fbCommentCount += uf.getComment_count();
	    		fbShareCount += uf.getShare_count();
	    	}
    	}
    	
    	if(userTweets != null) {
    		twFeedCount = userTweets.size();
	    	for(UserTweets ut : userTweets) {
	    		twReTweetCount += ut.getRetweet_count();
	    		twFavCount += ut.getFavorite_count();
	    	}
    	}
    	
    	if(userInstragramFeed != null) {
    		inFeedCount = userInstragramFeed.size();
	    	for(UserInstragramFeed uif : userInstragramFeed) {
	    		inLikeCount += uif.getLikesCount();
	    		inCommentCount += uif.getCommentsCount();
	    	}
    	}
    	
    	Integer fbInteractions = fbLikeCount+fbCommentCount+fbShareCount;
		Integer inInteractions = inLikeCount+inCommentCount;
		Integer twInteractions = twReTweetCount+twFavCount;
		
		Integer totalPost = fbFeedCount + twFeedCount + inFeedCount;
		Integer totalInteractions = fbInteractions + inInteractions + twInteractions;
		Double effectivenessRate = Double.valueOf(totalInteractions) / Double.valueOf(totalPost);
		//effectivenessRate = getDouble(effectivenessRate / 10);
		effectivenessRate = getDouble(effectivenessRate);
		
		List<CompanyRef> annualRanking = userService.getAnnualRankingListByCompnay(UserUtils.getLoggedInUserId(), userId);
		Integer annualRankScore = 0;
		if(annualRanking != null && annualRanking.size() > 0) {
			CompanyRef ref = annualRanking.get(0);
			annualRankScore = ref.getRankScore();
		}
		
		UserProfileDTO userProfileTotal = new UserProfileDTO();
		userProfileTotal.setTotalPosts(numberFormat.format(totalPost));
		userProfileTotal.setTotalInteractions(numberFormat.format(totalInteractions));
		userProfileTotal.setTotalEffectivenessRate(numberFormat.format(effectivenessRate));
		userProfileTotal.setAnnualRankScore(numberFormat.format(annualRankScore));
		
    	model.addObject("userFeed", userFeed);
    	model.addObject("userTweets", userTweets);
    	model.addObject("userInstragramFeed", userInstragramFeed);
    	model.addObject("userYoutubeFeed",userYoutubeFeed);
    	model.addObject("detailUser", detailUser);
    	model.addObject("userProfileTotal", userProfileTotal);

		return model;
	}
	
	private Double getDouble(Double val) {
		return Math.round(val * 100D) / 100D;
	}
	
	@GetMapping("/rest/userList")
	public @ResponseBody JsonWrapper<User> fetchUsers(@ModelAttribute final PaginationRequestDTO dto,
			@RequestParam(name = "name") String name,
			@RequestParam(name = "email") String email) {
		
		//final String sort = dto.getSort();

		final List<User> list = userService.findAllUsersByCompany(name, email, dto, false);
		final List<User> countList = userService.findAllUsersByCompany(name, email, dto, true);
		
		int count = 0;
		
		if(countList != null) {
			count = countList.size();
		}
		
		return new JsonWrapper<User>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}
	
	
	@PostMapping("/rest/saveUser")
	public @ResponseBody String update(@RequestBody final User user) {
		
		String[] activeProfiles = environment.getActiveProfiles();  
    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
		if(user.getUserId() != null && user.getUserId() > 0) {
			userService.updateUser(user, result);
		}else {
			
			userService.createUser(user, result);
//			CompanyRef companyRef = companyRefRepository.findByUseridAndCompanyId(user.getUserId(), UserUtils.getLoggedInUserId());
//			if (companyRef == null) {// user does not exist for current sponser
//				companyRef = getCompanyRef(user.getUserId(), UserUtils.getLoggedInUser());
//			}
//			companyRef.setRefStatus("Active");
//		    Date date = new Date();  
//		    System.out.println("check: "+  date.getDate());
//
//			ZoneId z = ZoneId.systemDefault();// Or get the JVM’s current default time zone: ZoneId.systemDefault() 
//			ZonedDateTime zdt = ZonedDateTime.now(z);
//			companyRef.setApprovedDate(zdt);
//
//			companyRefRepository.save(companyRef);

		}
		
		return "success";
	}
private CompanyRef getCompanyRef(long userId, User loggedInUser) {
		
		CompanyRef companyRef = companyRefRepository.findByUseridAndCompanyId(userId, loggedInUser.getUserId() );
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
	
	
	@PostMapping("/rest/deleteUser")
	public @ResponseBody String deleteUser(@RequestParam("userId") Long userId) {
		userService.deleteUser(userId);
		return "success";
	}
	
	
	@RequestMapping("/embedcode")
	public String embedcode() {
		return "embedcode";
	}
	
	@RequestMapping("/applications")
	public String applications() {

		return "applications";
	}
	
	@RequestMapping("/annualrankings")
	public String annualrankings() {
		return "annualrankings";
	}
	
	@GetMapping("/rest/rankList")
	public @ResponseBody JsonWrapper<CompanyRef> getAnnualRankingListByCompnay() {
		
		final List<CompanyRef> list = userService.getAnnualRankingListByCompnay(UserUtils.getLoggedInUserId(), 0l);

		return new JsonWrapper<CompanyRef>(list, list.size(), list.size(), list.size());
	}
	
	@GetMapping("/rest/applicationList")
	public @ResponseBody JsonWrapper<User> getApplications(@ModelAttribute final PaginationRequestDTO dto) {
		
		String[] activeProfiles = environment.getActiveProfiles();  
    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
    	//result = true;
    	
    	String profile = "angelTrack";
    	if(result) {
    		profile = "sports";
    	}

		dto.setPage((dto.getPage() / dto.getSize()) + 1);
		
		final List<User> list = userService.getApplicationList(profile, UserUtils.getLoggedInUserId());
		final int count = userService.getApplicationListCount(profile, UserUtils.getLoggedInUserId(), dto);

		return new JsonWrapper<User>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}

	@DeleteMapping("/rest/deleteApplication/{cid}/{id}/{link}")
	public @ResponseBody String deleteApplication(@PathVariable("cid") Long companyId, @PathVariable("id") Long id, @PathVariable("link") String link) throws ClientProtocolException, IOException {
		if( StringUtils.isAllBlank(link)) {
			applicationService.deleteApplication(id,companyId);
		}else {
			applicationService.deleteApplication_AmbassadorLink(id,companyId);
		}
		
		return "success";
     }
	
	@PostMapping("/rest/updateApplications")
	public @ResponseBody String updateApplications(
			@QueryParam(value = "ids") String ids,
			@QueryParam(value = "action") String action,
			@QueryParam(value = "link") String link,
			@QueryParam(value = "discount_code") String discount_code) {
		
		Long id = 0l;
		
		String discountCode = "";
		
		if(StringUtils.isBlank(discount_code)) {
			discountCode = "";
		}else {
			discountCode = discount_code;
		}
		
		if(StringUtils.isNotBlank(ids)) {
			id = Long.parseLong(ids);
		}
		
		if(StringUtils.isNotBlank(link) && link.equalsIgnoreCase("ambassadorlink")) {
			userService.updateApplications(id,action, link, discountCode);
		}else {
			userService.updateApplications(id,action, discountCode);
		}
		
		
		return "success";
	}
	
	@PostMapping("/rest/uploadUser")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,RedirectAttributes redirectAttributes) {
		try {
			String[][] users = new ExcelUtils().readExcel(file.getInputStream());
			userService.importUsers(users);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return "redirect:/users";
	}

	@GetMapping(value = "/userexporttoexcel")
	public ResponseEntity<InputStreamResource> userExport(){
		
		ByteArrayOutputStream data = userService.getData(UserUtils.getLoggedInUserId().intValue());
		ByteArrayInputStream in = new ByteArrayInputStream(data.toByteArray());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/octet-stream");
		headers.add("Content-Disposition", "attachment; filename=users.xls");
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}
	
	@GetMapping(value = "/viewApplicationExportPdf")
	public ResponseEntity<InputStreamResource> excelReport(
			@RequestParam(name = "staffId") String staffIdStr,
			@RequestParam(name = "applicationFrom") String applicationFrom) throws Exception {
		Long staffId = 0l;
		Long companyId = UserUtils.getLoggedInUserId();
		
		if(StringUtils.isNotBlank(staffIdStr)) {
			staffId = Long.parseLong(staffIdStr);
		}
		
		String[] activeProfiles = environment.getActiveProfiles();  
    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
    	
    	Application application = new Application();
    	SportsUserProAppDetails applicationSport = new SportsUserProAppDetails();
    	
    	if(result) {
    		
    		if(StringUtils.isNotBlank(applicationFrom) && applicationFrom.equalsIgnoreCase("ambassadorlink")){
        		application = userService.fetchApplication(staffId, companyId);
        	}else {
        		applicationSport = applicationRepository.fetchApplicationSports(staffId, companyId);
        	}
        	
        	
    		if (StringUtils.isBlank(applicationSport.getInsta_page_link())
    				|| applicationSport.getInsta_page_link().indexOf("https://") == -1) {
    			applicationSport.setInsta_page_link("N/A");
    		}
    		
    	}else {
    		
    		application = userService.fetchApplicationDetail(staffId, companyId, applicationFrom);
    		
    		if(StringUtils.isNotBlank(application.getUpload_pictures())) {
    	    	
    	    	String picURL = StringUtils.substringAfterLast(application.getUpload_pictures(), "/");
    			
    			URL uploadPicture = googleCloudService.getBucketImageUrl(result?"emp_document_sports":"emp_document",picURL);
    			
    			String pictureString = "";
    			if(uploadPicture != null) {
    				pictureString = "<a href="+uploadPicture.toString()+" target=\"_blank\">View </a>";
    			}
    			application.setUpload_pictures(pictureString);
    		}
    		
    		if(StringUtils.isNotBlank(application.getAttach_resume())) {
    			System.out.println("application.getAttach_resume():: "+application.getAttach_resume());
    			String resumeURL = StringUtils.substringAfterLast(application.getAttach_resume(), "/");
    			String resumeString = "";
    			
    			if(StringUtils.isNotBlank(resumeURL) && !resumeURL.equalsIgnoreCase("NA")) {
    				URL uploadResume = googleCloudService.getBucketImageUrl(result?"emp_document_sports":"emp_document",resumeURL);
    				if(uploadResume != null) {
        				resumeString = "<a href="+uploadResume.toString()+" target=\"_blank\">Download </a>";
        			}
    			}
    			
    			application.setAttach_resume(resumeString);
    		}
    	}
		
    	
    	ByteArrayOutputStream out = null;
    	
    	if(result) {
    		if(StringUtils.isNotBlank(applicationFrom) && applicationFrom.equalsIgnoreCase("ambassadorlink")){
    			out = jasperUtils.generateApplicationViewPDF(application, "", null);
    		}else {
    			out = jasperUtils.generateApplicationViewPDF(null, "sportsman", applicationSport);
    		}
    		
    	}else {
    		out = jasperUtils.generateApplicationViewPDF(application, "", null);
    	}
    	
    	ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/octet-stream");
		headers.add("Content-Disposition", "attachment; filename=application.pdf");
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		
	}
	
	
	
	@RequestMapping("/viewApplication")
	public ModelAndView viewApplication(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

		log.info("View Application controller has been called..."+request.getParameter("id"));
		
		Long staffId = Long.parseLong(request.getParameter("id"));
		Long companyId = UserUtils.getLoggedInUserId();
    	
		ModelAndView model = new ModelAndView();
		
    	String applicationFrom = request.getParameter("link");
    	Application application = null;
    	SportsUserProAppDetails applicationSport = null;
    	
    	String[] activeProfiles = environment.getActiveProfiles();  
    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
    	//result=true;
    	
    	if(result) {
    		
        	model.setViewName("viewApplicationSports");
    		
    		if(StringUtils.isNotBlank(applicationFrom) && applicationFrom.equalsIgnoreCase("ambassadorlink")){
    			applicationSport = userService.fetchApplicationSports(staffId, companyId);
        		//model.addObject("application", application);
        	}else {
        		applicationSport = applicationRepository.fetchApplicationSports(staffId, companyId);
            	

        	}
    		
    		if(applicationSport != null) {
    			if (StringUtils.isBlank(applicationSport.getInsta_page_link())
        				|| applicationSport.getInsta_page_link().indexOf("https://") == -1) {
        			applicationSport.setInsta_page_link("N/A");
        		}

    		}
    		model.addObject("application", applicationSport);

        	
    	}else {
    		
        	model.setViewName("viewApplication");
    		
        	application = userService.fetchApplicationDetail(staffId, companyId, applicationFrom);
        	
    		try {
	    		if(StringUtils.isNotBlank(application.getUpload_pictures())) {
	    	    	
	    	    	String picURL = StringUtils.substringAfterLast(application.getUpload_pictures(), "/");
	    			
	    			URL uploadPicture = googleCloudService.getBucketImageUrl(result?"emp_document_sports":"emp_document",picURL);
	    			
	    			String pictureString = "";
	    			if(uploadPicture != null) {
	    				pictureString = "<a href="+uploadPicture.toString()+" target=\"_blank\">View </a>";
	    			}
	    			application.setUpload_pictures(pictureString);
	    		}
	    		
	    		if(StringUtils.isNotBlank(application.getAttach_resume())) {
	    			String resumeURL = StringUtils.substringAfterLast(application.getAttach_resume(), "/");
	    			String resumeString = "";
	    			
	    			if(StringUtils.isNotBlank(resumeURL) && !resumeURL.equalsIgnoreCase("NA")) {
	    				URL uploadResume = googleCloudService.getBucketImageUrl(result?"emp_document_sports":"emp_document",resumeURL);
	    				if(uploadResume != null) {
	        				resumeString = "<a href="+uploadResume.toString()+" target=\"_blank\">Download </a>";
	        			}
	    			}
	    			
	    			application.setAttach_resume(resumeString);
	    		}
    		}catch(Exception ex) {
    			log.error("Error getting files from cloud bucket. :"+ex.getMessage());
    		}
        	
        	model.addObject("application", application);
    	}
    	
    	

		return model;
	}
	
	@RequestMapping("/keywords")
	public ModelAndView keywords(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		
		ModelAndView model = new ModelAndView();
    	model.setViewName("keywords");
    	
		Long companyId = UserUtils.getLoggedInUserId();
		
		User user  = userService.findUserById(companyId);
		
		val keywordsFB = user.getFbKeywords();
		val keywordsIN = user.getInstaKeywords();
		val keywordsTW = user.getTwtKeywords();
		val keywordsYT = user.getYoutubeKeywords();
		
		List<String> keywordsList = new ArrayList<String>();
		
		if(keywordsFB != null) {
			keywordsList.addAll(Arrays.asList( keywordsFB.split(",")));
		}
		if(keywordsIN != null) {
			keywordsList.addAll(Arrays.asList( keywordsIN.split(",")));
		}
		if(keywordsTW != null) {
			keywordsList.addAll(Arrays.asList( keywordsTW.split(",")));
		}
		if(keywordsYT != null) {
			keywordsList.addAll(Arrays.asList( keywordsYT.split(",")));
		}
		
		List<String> keywords = keywordsList.stream()
			     .distinct()
			     .collect(Collectors.toList());
		
		String keywordStr = "";
		for(String str : keywords) {
			keywordStr = keywordStr+","+str;
		}
		
		keywordStr = keywordStr.replaceFirst(",", "");
		
		model.addObject("keywordsList", keywordStr);

		return model;
	}
	
	@PostMapping("/rest/updateKeyWords")
	public @ResponseBody String updateKeyWords(@RequestBody final KeywordsDTO txtKeywords) {
		User user  = userService.findUserById(UserUtils.getLoggedInUserId());		
		user.setFbKeywords(txtKeywords.getTxtKeywords());
		userRepository.save(user);
		return "success";
	}
	
	@RequestMapping("/promoCode")
	public ModelAndView promoCode(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		
		ModelAndView model = new ModelAndView();
    	model.setViewName("promoCode");
    	
    	@SuppressWarnings("unchecked")
		val membersList = (List<User>) UserUtils.getSessionsMap().get(SessionVariables.MEMBERS);
		model.addObject("membersList", membersList);
		
		@SuppressWarnings("unchecked")
		val teamList = (List<Team>) UserUtils.getSessionsMap().get(SessionVariables.TEAM);
		model.addObject("teamList", teamList);

		return model;
	}
	
	@PostMapping("/rest/updatePromoCode")
	public @ResponseBody String update(
			@RequestParam(name = "promoCode") String promoCode,
			@RequestParam(name = "members[]", required = false) final String[] membersArray,
			@RequestParam(name = "team[]", required = false) final String[] teamArray
			) {
		
		
		List<Long> ids = new ArrayList<Long>();
		
		if(StringUtils.isBlank(promoCode)) {
			throw new UserException("Please enter Promo Code.");
		}
		
		if(membersArray != null && membersArray.length > 0) {
			for(String str : membersArray) {
				ids.add(Long.parseLong(str));
			}
		}
		
		if(teamArray != null && teamArray.length > 0) {
			for(String str : teamArray) {
				List<UserTeam> uTeam = userTeamRepository.findByTeamId(Long.parseLong(str));
				if(uTeam != null) {
					for(UserTeam usrteam : uTeam) {
						ids.add(usrteam.getUserId());
					}					
				}
			}
		}
		
		userService.updatePromoCode(ids, UserUtils.getLoggedInUserId(), promoCode);
		
		return "success";
	}
	
	
}
