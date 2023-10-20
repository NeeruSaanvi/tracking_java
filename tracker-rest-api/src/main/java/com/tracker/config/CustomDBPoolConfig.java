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

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
 
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data 
public class CustomDBPoolConfig {

	private String url;
	private String driverClassName;
	private String username;
	private String password;
	private Hikari hikari;

	@Getter
	@Setter
	public static class Hikari {
		private Integer maximumPoolSize;
		private Integer connectionTimeout;
		private Integer idleTimeout;
		private String connectionTestQuery;
		private String poolname;
	}

	@Bean
	@Primary  
	public DataSource dataSource() {

		val config = new HikariConfig();

		config.setJdbcUrl(url);
		config.setDriverClassName(driverClassName);
		config.setUsername(username);
		config.setPassword(password);
		config.setMaximumPoolSize(hikari.getMaximumPoolSize());
		config.setConnectionTimeout(hikari.getConnectionTimeout());
		config.setConnectionTestQuery(hikari.getConnectionTestQuery());
		config.setIdleTimeout(hikari.getIdleTimeout());
		config.setPoolName(hikari.getPoolname());

		return new CustomDataSource(config);
	}

}



