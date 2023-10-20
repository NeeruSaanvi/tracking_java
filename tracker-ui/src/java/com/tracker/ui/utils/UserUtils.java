package com.tracker.ui.utils;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.security.core.context.SecurityContextHolder;

import com.tracker.commons.models.User;
import com.tracker.ui.security.SessionVariables;

@SuppressWarnings("unchecked")
public class UserUtils {

	public static Map<String, Object> getSessionsMap() {
		return (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static User getLoggedInUser() {
		return (User) getSessionsMap().get(SessionVariables.USER);
	}

	public static Long getLoggedInUserId() {
		return getLoggedInUser().getUserId();
	}
	
	public static Integer getLoggedInCompanyID() {
		return getLoggedInUser().getCompanyId();
	}

	public static boolean isUserLoggedIn() {
		try {
			getLoggedInUserId();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Map<String, String> getState() {
		Map<String,String> map = new TreeMap<String,String>();
		
		map.put("all","All States");
				map.put("AL","Alabama");
				map.put("AK","Alaska"); 
				map.put("AZ","Arizona"); 
				map.put("AR","Arkansas"); 
				map.put("CA","California"); 
				map.put("CO","Colorado"); 
				map.put("CT","Connecticut"); 
				map.put("DE","Delaware"); 
				map.put("DC","District Of Columbia"); 
				map.put("FL","Florida"); 
				map.put("GA","Georgia"); 
				map.put("HI","Hawaii"); 
				map.put("ID","Idaho"); 
				map.put("IL","Illinois"); 
				map.put("IN","Indiana"); 
				map.put("IA","Iowa"); 
				map.put("KS","Kansas"); 
				map.put("KY","Kentucky"); 
				map.put("LA","Louisiana"); 
				map.put("ME","Maine"); 
				map.put("MD","Maryland"); 
				map.put("MA","Massachusetts"); 
				map.put("MI","Michigan"); 
				map.put("MN","Minnesota"); 
				map.put("MS","Mississippi"); 
				map.put("MO","Missouri"); 
				map.put("MT","Montana");
				map.put("NE","Nebraska");
				map.put("NV","Nevada");
				map.put("NH","New Hampshire");
				map.put("NJ","New Jersey");
				map.put("NM","New Mexico");
				map.put("NY","New York");
				map.put("NC","North Carolina");
				map.put("ND","North Dakota");
				map.put("OH","Ohio"); 
				map.put("OK","Oklahoma"); 
				map.put("OR","Oregon"); 
				map.put("PA","Pennsylvania"); 
				map.put("RI","Rhode Island"); 
				map.put("SC","South Carolina"); 
				map.put("SD","South Dakota");
				map.put("TN","Tennessee"); 
				map.put("TX","Texas"); 
				map.put("UT","Utah"); 
				map.put("VT","Vermont"); 
				map.put("VA","Virginia"); 
				map.put("WA","Washington"); 
				map.put("WV","West Virginia"); 
				map.put("WI","Wisconsin"); 
				map.put("WY","Wyoming");
				map.put("AB","Alberta");
				map.put("BC","British Columbia");
				map.put("MB","Manitoba");
				map.put("NB","New Brunswick");
				map.put("NL","Newfoundland and Labrador");
				map.put("NT","Northwest Territories");
				map.put("NS","Nova Scotia");
				map.put("NU","Nunavut");
				map.put("ON","Ontario");
				map.put("PE","Prince Edward Island");
				map.put("QC","Quebec");
				map.put("SK","Saskatchewan");
				map.put("YT","Yukon");
		return map;
	}
	
}