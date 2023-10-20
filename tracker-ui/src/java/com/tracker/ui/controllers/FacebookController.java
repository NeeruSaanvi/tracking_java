package com.tracker.ui.controllers;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
 
@Controller
public class FacebookController {
 
//	@Autowired
//    private Facebook facebook;
//     
//
//
//
//   
// 
//	@GetMapping
//    public String helloFacebook(Model model) {
//        if (!facebook.isAuthorized()) {
//            return "redirect:/connect/facebook";
//        }
// 
//        model.addAttribute(facebook.userOperations().getUserProfile());
//        PagedList homeFeed = facebook.feedOperations().getHomeFeed();
//        model.addAttribute("feed", homeFeed);
//        return "feeds";
//    }
}