package com.tracker.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.models.UserSponsorScores;
import com.tracker.services.repositories.UserRepository;
import com.tracker.services.repositories.UserSponcerScoreRepository;
import com.tracker.ui.utils.UserUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ScoreSettingsController extends BaseController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserSponcerScoreRepository userSponcerScoreRepository;

	
	@RequestMapping("/scoreSettings")
	public ModelAndView scoreSettings(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

		log.info(" scoreSettings controller has been called...");
		
		Long companyId = UserUtils.getLoggedInUserId();
		
		ModelAndView model = new ModelAndView();
    	model.setViewName("scoreSettings");
    	
    	UserSponsorScores userSponsorScores = userRepository.fetchScoreSettings(companyId);
    	model.addObject("userSponsorScores", userSponsorScores);
    	
		return model;
	}
	
	
	
	@PostMapping("/rest/updateScoreSettings")
	public @ResponseBody String updateScoreSettings(
			@RequestParam(name = "id") String idStr,
			@RequestParam(name = "fbpost") String fbpostStr,
			@RequestParam(name = "fblike") String fblikeStr,
			@RequestParam(name = "fbcomments") String fbcommentsStr,
			@RequestParam(name = "fbshares") String fbsharesStr,
			@RequestParam(name = "twttweets") String twttweetsStr,
			@RequestParam(name = "twtretweets") String twtretweetsStr,
			@RequestParam(name = "twtfavourites") String twtfavouritesStr,
			@RequestParam(name = "instaposts") String instapostsStr,
			@RequestParam(name = "instalikes") String instalikesStr,
			@RequestParam(name = "instacomments") String instacommentsStr,
			@RequestParam(name = "ytposts") String ytpostsStr,
			@RequestParam(name = "ytlikes") String ytlikesStr,
			@RequestParam(name = "ytviews") String ytviewsStr,
			@RequestParam(name = "leadsubmt") String leadsubmtStr,
			@RequestParam(name = "eventworked") String eventworkedStr,
			@RequestParam(name = "tournaments") String tournamentsStr,
			@RequestParam(name = "webhits") String webhitsStr,
			@RequestParam(name = "prints") String printsStr) {
		
		Integer fbpost = StringUtils.isNotBlank(fbpostStr) ? Integer.parseInt(fbpostStr) : 0;
		Integer fblike = StringUtils.isNotBlank(fblikeStr) ? Integer.parseInt(fblikeStr) : 0;
		Integer fbcomments = StringUtils.isNotBlank(fbcommentsStr) ? Integer.parseInt(fbcommentsStr) : 0;
		Integer fbshares = StringUtils.isNotBlank(fbsharesStr) ? Integer.parseInt(fbsharesStr) : 0;
		Integer twttweets = StringUtils.isNotBlank(twttweetsStr) ? Integer.parseInt(twttweetsStr) : 0;
		Integer twtretweets = StringUtils.isNotBlank(twtretweetsStr) ? Integer.parseInt(twtretweetsStr) : 0;
		Integer twtfavourites = StringUtils.isNotBlank(twtfavouritesStr) ? Integer.parseInt(twtfavouritesStr) : 0;
		Integer instaposts = StringUtils.isNotBlank(instapostsStr) ? Integer.parseInt(instapostsStr) : 0;
		Integer instalikes = StringUtils.isNotBlank(instalikesStr) ? Integer.parseInt(instalikesStr) : 0;
		Integer instacomments = StringUtils.isNotBlank(instacommentsStr) ? Integer.parseInt(instacommentsStr) : 0;
		Integer ytposts = StringUtils.isNotBlank(ytpostsStr) ? Integer.parseInt(ytpostsStr) : 0;
		Integer ytlikes = StringUtils.isNotBlank(ytlikesStr) ? Integer.parseInt(ytlikesStr) : 0;
		Integer ytviews = StringUtils.isNotBlank(ytviewsStr) ? Integer.parseInt(ytviewsStr) : 0;
		Integer leadsubmt = StringUtils.isNotBlank(leadsubmtStr) ? Integer.parseInt(leadsubmtStr) : 0;
		Integer eventworked = StringUtils.isNotBlank(eventworkedStr) ? Integer.parseInt(eventworkedStr) : 0;
		Integer tournaments = StringUtils.isNotBlank(tournamentsStr) ? Integer.parseInt(tournamentsStr) : 0;
		Integer webhits = StringUtils.isNotBlank(webhitsStr) ? Integer.parseInt(webhitsStr) : 0;
		Integer prints = StringUtils.isNotBlank(printsStr) ? Integer.parseInt(printsStr) : 0;
		
		if(StringUtils.isNotBlank(idStr)) {
			UserSponsorScores userSponsorScores = userSponcerScoreRepository.getOne(Long.parseLong(idStr));
			
			userSponsorScores.setCompanyID(UserUtils.getLoggedInUserId());
			userSponsorScores.setFbpost(fbpost);
			userSponsorScores.setFblike(fblike);
			userSponsorScores.setFbcomments(fbcomments);
			userSponsorScores.setFbshares(fbshares);
			userSponsorScores.setTwttweets(twttweets);
			userSponsorScores.setTwtretweets(twtretweets);
			userSponsorScores.setTwtfavourites(twtfavourites);
			userSponsorScores.setInstaposts(instaposts);
			userSponsorScores.setInstalikes(instalikes);
			userSponsorScores.setInstacomments(instacomments);
			userSponsorScores.setYtposts(ytposts);
			userSponsorScores.setYtlikes(ytlikes);
			userSponsorScores.setYtviews(ytviews);
			userSponsorScores.setLeadsubmt(leadsubmt);
			userSponsorScores.setEventworked(eventworked);
			userSponsorScores.setTournaments(tournaments);
			userSponsorScores.setWebhits(webhits);
			userSponsorScores.setPrints(prints);
			
			userSponcerScoreRepository.save(userSponsorScores);
			
		}else {
			UserSponsorScores userSponsorScores = new UserSponsorScores();
			
			userSponsorScores.setCompanyID(UserUtils.getLoggedInUserId());
			userSponsorScores.setFbpost(fbpost);
			userSponsorScores.setFblike(fblike);
			userSponsorScores.setFbcomments(fbcomments);
			userSponsorScores.setFbshares(fbshares);
			userSponsorScores.setTwttweets(twttweets);
			userSponsorScores.setTwtretweets(twtretweets);
			userSponsorScores.setTwtfavourites(twtfavourites);
			userSponsorScores.setInstaposts(instaposts);
			userSponsorScores.setInstalikes(instalikes);
			userSponsorScores.setInstacomments(instacomments);
			userSponsorScores.setYtposts(ytposts);
			userSponsorScores.setYtlikes(ytlikes);
			userSponsorScores.setYtviews(ytviews);
			userSponsorScores.setLeadsubmt(leadsubmt);
			userSponsorScores.setEventworked(eventworked);
			userSponsorScores.setTournaments(tournaments);
			userSponsorScores.setWebhits(webhits);
			userSponsorScores.setPrints(prints);
			
			userSponcerScoreRepository.save(userSponsorScores);
		}
		
		return "success";
	}
	 

}
