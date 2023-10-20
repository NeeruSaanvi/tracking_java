package com.tracker.ui.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.firewall.HttpFirewall;
//import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.tracker.services.utils.AuthenticationManagerExtended;
import com.tracker.ui.springboot.HttpsRedirectionFilter;

@Configuration
@EnableWebSecurity 
//@EnableGlobalMethodSecurity 
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String[] NOT_SECURED = {"/register", "/registerUser", "/login*",  "/facebookAuth", "/twitterAuth", 
			"/connect/**", "/accessDenied*", "/error", "/resetForgottenPassword", "/resetForgottenPasswordSubmit", "/forgotPassword",
			"/contactUsEmailSend", "/forgotPasswordSendEmail", "/", "/emailValidation","/rest/activateuser/**" };

	@Autowired
	private AuthenticationManagerExtended authManager; 

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		//.requiresChannel().anyRequest().requiresSecure()
//		.and()
		.addFilterAfter(new HttpsRedirectionFilter(), BasicAuthenticationFilter.class)
		.csrf().ignoringAntMatchers("/facebookAuth", "/rest/**","/twitterAuth", "/connect/**")
		.and()
			.authorizeRequests()
			.antMatchers(NOT_SECURED).permitAll() //.hasAnyRole("ADMIN")
			.anyRequest().authenticated() //Anything else must be authenticated
		.and()
			.formLogin() 
			.loginPage("/")
			.loginProcessingUrl("/doLogin")
			.failureUrl("/loginFailure")
			.authenticationDetailsSource(new CustomWebAuthenticationDetailsSource())
			//.failureHandler(authenticationFailureHandler)
			.defaultSuccessUrl("/dashboard", true)
			.usernameParameter("username").passwordParameter("password") 
			.permitAll()
		//.and().rememberMe().rememberMeServices(rememberMeServices())
			//.tokenRepository(persistentTokenRepository())
			//.key("CoinxoomRememberMeKey")
			// .alwaysRemember(false)
			// .rememberMeParameter("rememberMe")
			//.rememberMeCookieName("coinxoom-remember-me")
			//  .tokenValiditySeconds(365 * 24 * 60 * 60)
		.and()
			.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/")
		.and() 
			.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()) //handle access denied errors to it's specific page
		.and()
			.sessionManagement()
			.invalidSessionUrl("/")
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
			

	} 

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/custom_resources/**");
	}

	

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.parentAuthenticationManager(authManager); 
		auth.eraseCredentials(false);
	}
	
	/*
	 * @SuppressWarnings("unused")
	 * 
	 * @Autowired private MyUserDetailsService myUserDetailsService;
	 */

	/*@Bean
	public AbstractRememberMeServices rememberMeServices() {

		CustomRememberMeService rememberMeServices = new CustomRememberMeService("CoinxoomRememberMeKey", myUserDetailsService);
		rememberMeServices.setAlwaysRemember(false);
		//rememberMeServices.rememberMeParameter("rememberMe");
		rememberMeServices.setCookieName("coinxoom-remember-me");
		rememberMeServices.setTokenValiditySeconds(365 * 24 * 60 * 60);
		return rememberMeServices;
	}*/

}