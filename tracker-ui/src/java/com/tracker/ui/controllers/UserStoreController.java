package com.tracker.ui.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.UserStore;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.UserService;
import com.tracker.services.impls.UserStoreService;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserStoreController extends BaseController {
	
	@Autowired
	private UserStoreService userStoreService;
	
	@Autowired
	private UserService userService;

	@RequestMapping("/userStore")
	public String userStore(HttpServletRequest request, HttpServletResponse response) {

		log.info("userStore controller has been called...");

		return "userStore";
	}
	
	@GetMapping("/rest/userStoreList")
	public @ResponseBody JsonWrapper<UserStore> getWebPostList(@ModelAttribute final PaginationRequestDTO dto) {

		dto.setPage((dto.getPage() / dto.getSize()) + 1);
		
		final List<UserStore> list = userStoreService.getUserStoreList(UserUtils.getLoggedInUserId(), dto);
		final int count = userStoreService.getUserStoreListCount(UserUtils.getLoggedInUserId(), dto);
		
		return new JsonWrapper<UserStore>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}
	
	@PostMapping("/rest/saveUserStore")
	public @ResponseBody String update(@RequestBody final UserStore userStore) {

		if(StringUtils.isBlank(userStore.getEmail())) {
			throw new UserException("Email field can not be empty.");
		}
		
		UserStore dbUserStore = userStoreService.findUserStoreById(userStore.getStoreId());
		
		if(dbUserStore != null) {
			userService.sendMailUserStore(dbUserStore, userStore.getEmail());
		}
		
		return "success";
	}
	
	@RequestMapping("/viewUserStore")
	public ModelAndView viewUserStore(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

		log.info("View UserStore controller has been called..."+request.getParameter("storeId"));
		
		Long storeId = Long.parseLong(request.getParameter("storeId"));
    	
		ModelAndView model = new ModelAndView();
        model.setViewName("viewUserStore");
    		
        UserStore dbUserStore = userStoreService.findUserStoreById(storeId);	
        
        String store_inventory_levelStr = "";
        
		if (dbUserStore.getStore_inventory_level() != null) {
			if (dbUserStore.getStore_inventory_level() == 0) {
				store_inventory_levelStr = "Zero product";
			}
			if (dbUserStore.getStore_inventory_level() == 25) {
				store_inventory_levelStr = "Low Levels (less than 25% full shelf space used)";
			}
			if (dbUserStore.getStore_inventory_level() == 40) {
				store_inventory_levelStr = "Acceptable (40-60% of shelf space used)";
			}
			if (dbUserStore.getStore_inventory_level() == 90) {
				store_inventory_levelStr = "Fully Stocked (shelf space more than 90% full)";
			}
			if (dbUserStore.getStore_inventory_level() == 101) {
				store_inventory_levelStr = "Overstocked (shelf space full with reserve product in the back)";
			}
		}
		dbUserStore.setStore_inventory_levelStr(store_inventory_levelStr);
		
		String store_shelves_conditionStr = "";
		if(dbUserStore.getStore_shelves_condition() != null ) {
			if(dbUserStore.getStore_shelves_condition() == 1 ) {
				store_shelves_conditionStr = "Poor (ex: dusty, poorly organized, half open boxes)";
			}
			if(dbUserStore.getStore_shelves_condition() == 5 ) {
				store_shelves_conditionStr = "Acceptable (generally well organized)";
			}
			if(dbUserStore.getStore_shelves_condition() == 10 ) {
				store_shelves_conditionStr = "Excellent (we looked better than competitors and are well represented)";
			}
		}
		dbUserStore.setStore_shelves_conditionStr(store_shelves_conditionStr);
		
		String store_end_caps_aisle_lookStr = "";
		if(dbUserStore.getStore_end_caps_aisle_look() != null ) {
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
		}
		dbUserStore.setStore_end_caps_aisle_lookStr(store_end_caps_aisle_lookStr);
		        	
        model.addObject("userStore", dbUserStore);

		return model;
	}

}
