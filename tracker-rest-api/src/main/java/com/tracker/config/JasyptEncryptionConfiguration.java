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

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.hibernate4.encryptor.HibernatePBEEncryptorRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tracker.commons.Constants;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "jasypt.encryptor")
@Data
public class JasyptEncryptionConfiguration {
	
	private String password;
	private String poolSize;
	private String keyObtentionIterations;
	private String algorithm;

	@Bean(name="encryptorBean")
	public StringEncryptor stringEncryptor() {
		final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

		final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(password);
		config.setAlgorithm(algorithm);
		config.setKeyObtentionIterations(keyObtentionIterations);
		config.setPoolSize(poolSize);
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);

		// strongEncryptor.setPassword(password);
		HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
		registry.registerPBEStringEncryptor(Constants.JASYPT_ENCRYPTOR_ID, encryptor);

		return encryptor;
	} 
	
}
