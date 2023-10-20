package com.tracker.ui.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tracker.commons.models.User;
import com.tracker.services.utils.TimeZoneEnum;
import com.tracker.ui.utils.UserUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class CustomDataSource extends HikariDataSource {

	private final Logger log = LoggerFactory.getLogger(CustomDataSource.class);

	public CustomDataSource(HikariConfig configuration) {
		super(configuration);
	}

	@Override
	public Connection getConnection() throws SQLException {
		Connection con = super.getConnection();

		try {
			if(UserUtils.isUserLoggedIn()) {
				@SuppressWarnings("unused")
				final User user = UserUtils.getLoggedInUser();
				final TimeZoneEnum userTimeZoneEnum = TimeZoneEnum.findById(/*user.getTimeZone()*/TimeZoneEnum._1.getId());

				final String query = "SET time_zone = '" + userTimeZoneEnum.getOffset() + "'";

				PreparedStatement ps = con.prepareStatement(query);
				ps.executeQuery();
			}
		}
		catch(Exception e) {
			log.error("Error while setting user timezone in database" + e.getMessage(), e);
		}

		return con;
	}

}
