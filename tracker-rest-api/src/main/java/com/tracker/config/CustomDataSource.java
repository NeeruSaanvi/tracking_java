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
package com.tracker.config;

import java.sql.Connection;
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

		try {
			if(UserUtils.isUserLoggedIn()) {
				/*final User user = UserUtils.getLoggedInUser();
				final TimeZoneEnum userTimeZoneEnum = TimeZoneEnum.findById(user.getTimeZone());

				final String query = "set time zone '" + userTimeZoneEnum.getTzName() + "'";

				con.createStatement().execute(query);
				*/
			}
		}
		catch(Exception e) {
			log.error("Error while setting user timezone in database" + e.getMessage(), e);
		}

		return con;
	}

}
