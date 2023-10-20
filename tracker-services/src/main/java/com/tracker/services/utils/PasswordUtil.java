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
package com.tracker.services.utils;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.text.RandomStringGenerator;

public class PasswordUtil { 

	public static String generatePassword() {
		return generatePassword(20);
	}

	public static String generatePassword(int length) {

		// Using Apache Commons RNG for randomness
		UniformRandomProvider rng = RandomSource.create(RandomSource.MT);
		// Generates a 20 code point string, using only the letters a-z
		RandomStringGenerator generator = new RandomStringGenerator.Builder()
				.withinRange('0', 'z')
				.filteredBy(LETTERS, DIGITS)
				.usingRandom(rng::nextInt) // uses Java 8 syntax
				.build();
		return generator.generate(length);

	} 

	public static boolean matchPassword(String originalPassword, String encryptedPasswordHash) {
		/*try {
			return BCrypt.checkpw(originalPassword, encryptedPasswordHash);
		} catch (Exception e) {
			return false;
		}*/
		try {
		    String md5Hex = DigestUtils.md5Hex(originalPassword);
		    
		    return encryptedPasswordHash.equals(md5Hex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static String encryptPassword(String password) {
		//return BCrypt.hashpw(password, BCrypt.gensalt(4));
		return DigestUtils.md5Hex(password);
	}

/*
	public static void main(String[] args) throws Exception {
		System.out.println(PasswordUtil.matchPassword("Stryde2016!", "68d4dfd11ea64f539d57f9c80a1d8bb5" ));
	}
	
	public static void main(String[] args) throws Exception {
		String salt = PasswordUtil.generatePassword(30);
		System.out.println(salt);
		System.out.println(PasswordUtil.encryptPassword(salt + "password"));
		
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
	}
*/
}
