/******************************************************************************
 *                                                                            *
 * Copyright (c) 2018 by ACI Worldwide Inc.                                   *
 * All rights reserved.                                                       *
 *                                                                            *
 * This software is the confidential and proprietary information of ACI       *
 * Worldwide Inc ("Confidential Information"). You shall not disclose such    *
 * Confidential Information and shall use it only in accordance with the      *
 * terms of the license agreement you entered with ACI Worldwide Inc.         *
 ******************************************************************************/

package com.tracker.config;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class BeanConfiguration {

	/*
	 * @Bean public Validator validator() { return new LocalValidatorFactoryBean();
	 * }
	 */

	@Bean
	public Module module() {
		final SimpleModule module = new SimpleModule("Module", new Version(1, 0, 0, null, null, null));
		module.addSerializer(ZonedDateTime.class, new CustomTimestampSerializer());
		module.addDeserializer(ZonedDateTime.class, new CustomTimestampDeserializer());
		module.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());

		return module;
	}  
	
	@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
