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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Query;

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tracker.commons.models.UserImage;
import com.tracker.commons.models.UserInstragramFeed;
import com.tracker.commons.models.UserTeam;
import com.tracker.commons.models.UserYoutubeFeed;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.GalleryRepository;
import com.tracker.services.utils.UserUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GalleryService {

	@Autowired
	private GalleryRepository galleryRepository;
	

	public List<UserInstragramFeed> getInstagramImages(Long companyID, String keyword, List<Long> userList,
			Date fromDate, Date toDate, List<String> finalKeywordsList) {
		log.info("getting instagram images");
	
		
		List<UserInstragramFeed> imageListCopy  = new ArrayList<>();
		List<UserInstragramFeed> imageList = galleryRepository.getInstagramImages(companyID, keyword, userList,
				fromDate, toDate);
		
		if(finalKeywordsList != null && finalKeywordsList.size() > 0 ) {
			for(String word : finalKeywordsList) {
				//System.out.println("word:: "+word);
				for(UserInstragramFeed feed : imageList) {
					String text = feed.getText() == null ? "" : feed.getText();
					//System.out.println("text:: "+text);
					if (text.toLowerCase().contains(word.toLowerCase())) {
						String keywordText = feed.getKeyword() == null ? "" : feed.getKeyword() + ", " + word;
						keywordText = keywordText.replaceFirst(",", "");
						feed.setKeyword(keywordText);
						
			        }
				}
			}
		}
		
//		for(int index =0;index<imageList.size();index++) {
//			
//			if(!TextUtils.isEmpty(imageList.get(index).getKeyword()))
//			
//				imageListCopy.add(imageList.get(index));
//			
//		}

		return imageList;

	}
	
	public List<UserYoutubeFeed> getYoutubeVideo(Long companyID, String keyword, List<Long> userList) {
		
		return galleryRepository.getYoutubeVideo(companyID, keyword, userList);
		
	}
	
	public List<UserImage> getHighResImages(Long companyID) {
		
		return galleryRepository.getHighResImages(companyID);
		
	}
	
	
}
