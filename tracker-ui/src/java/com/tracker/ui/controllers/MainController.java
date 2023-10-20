package com.tracker.ui.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import  org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.social.facebook.api.Post;
import org.springframework.web.bind.annotation.RequestMethod;



@Configuration
@Controller
@Slf4j
@RequestMapping("/facebook123")
public class MainController {
    private Facebook facebook;

    public MainController() {
      
    }
    public MainController(Facebook facebook) {
        this.facebook = facebook;
        //this.connectionRepository = connectionRepository;
    }

    
//    public String feed(Model model) {
//
//        if(!this.facebook.isAuthorized()) {
//            return "redirect:/connect/facebook";
//        }
//
//        User userProfile = facebook.userOperations().getUserProfile();
//        model.addAttribute("userProfile", userProfile);
//        PagedList<Post> userFeed = facebook.feedOperations().getFeed();
//        model.addAttribute("userFeed", userFeed);
//        return "feed";
//    }
}