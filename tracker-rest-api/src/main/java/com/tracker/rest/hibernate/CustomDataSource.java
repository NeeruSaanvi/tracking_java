/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tracker.commons.models.User;
import com.tracker.services.utils.TimeZoneEnum;
import com.tracker.services.utils.UserUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomDataSource extends HikariDataSource {

	public CustomDataSource(HikariConfig configuration) {
		super(configuration);
	}

	@Override
	public Connection getConnection() throws SQLException {
		Connection con = super.getConnection();
/*
		try {
			if(UserUtils.isUserLoggedIn()) {
				final User user = UserUtils.getLoggedInUser();
				final TimeZoneEnum userTimeZoneEnum = TimeZoneEnum.findById(user.getTimeZone());

				final String query = "SET time_zone = '" + userTimeZoneEnum.getOffset() + "'";

				PreparedStatement ps = con.prepareStatement(query);
				ps.executeQuery();
			}
		}
		catch(Exception e) {
			log.error("Error while setting user timezone in database" + e.getMessage(), e);
		}
		*/

		return con;
	}

}
