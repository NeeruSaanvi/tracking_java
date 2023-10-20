/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication//(exclude = RepositoryRestMvcAutoConfiguration.class)
//@EnableScheduling
@ComponentScan("com.tracker.*")
@EnableJpaRepositories(basePackages = "com.tracker")
@EntityScan(basePackages = "com.tracker.commons")
@EnableAutoConfiguration
//@EnableAsync
// @EnableWebMvc
//@EnableEncryptableProperties
public class Application {

	 
	public static void main(final String[] args) {
		/* final ApplicationContext ctx = */ SpringApplication.run(Application.class, args);
		 
	}
 
}
