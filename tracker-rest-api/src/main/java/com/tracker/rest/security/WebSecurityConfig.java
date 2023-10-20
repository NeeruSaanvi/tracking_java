/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tracker.services.utils.AuthenticationManagerExtended;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationManagerExtended authenticationManagerExtended;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		http.csrf().disable()
		.cors().and()
		/*
		 	Apply security to only this URL..any other non-matching url is allowed by default.. 
		 		(i..e /api/v1/xyz will pass through this security and would need Auth Token.. while /swagger will work without any token)

		 	 - To exclude certain specific urls that are sub urls of this i.e (/api/v1/accessWithoutToken)  ..use below web.ignoring() ant matcher to exclude them completely.
		 */
		.antMatcher("/api/v1/**") 

		.authorizeRequests()	// When Request matches above URL..that means user has now authenticated..these following antMatchers will apply authorization and make sure user has proper roles to access certain urls.
		//.antMatchers("/api/v1/usersasfafasdfaasd/").hasAnyRole("admin")
		.anyRequest().permitAll() //anything else that is Authenticated but doesn't match above antMatchers is allowed to access URL without requiring particular role i.e are authorized to access everything as long as they are authenticated


		.and()

		// We filter the api/login requests
		.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),	UsernamePasswordAuthenticationFilter.class)

		// And filter other requests to check the presence of JWT in header
		.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}

	/**
	 * Following URLs are permitted without authentication
	 */
	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.
		ignoring().antMatchers(/*"/swagger*", "/webjars/**", "/v2/api-docs*", "/", */"/api/v1/auth/**", "/api/v1/dashboard1/**")
		//.and().ignoring().antMatchers(HttpMethod.POST, "/api/v1/users/")
		;
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		//auth.authenticationProvider(authProvider);
		auth.parentAuthenticationManager(authenticationManagerExtended);

		// Create a default account
		//auth.inMemoryAuthentication().withUser("admin").password("password").roles("USER");
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public TokenAuthenticationService tokenAuthenticationService() {
		return new TokenAuthenticationService();
	}
	
	//@Bean
	/*
	 * CorsFilter corsFilter() { CorsFilter filter = new CorsFilter(); return
	 * filter; }
	 */

}
