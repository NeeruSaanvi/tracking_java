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
package com.tracker.services.repositories;

import java.util.Date;
import java.util.List;

import com.tracker.commons.models.UserImage;
import com.tracker.commons.models.UserInstragramFeed;
import com.tracker.commons.models.UserYoutubeFeed;

public interface GalleryRepositoryCustom {

	public List<UserInstragramFeed> getInstagramImages(Long companyID, String keyword, List<Long> userList, Date fromDate, Date toDate);

	public List<UserYoutubeFeed> getYoutubeVideo(Long companyID, String keyword, List<Long> userList);
	
	public List<UserImage> getHighResImages (Long companyID);

}
