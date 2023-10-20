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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Utility {
	
	public static boolean isValidUUID(String uuid) {
		try {
			UUID.fromString(uuid);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String formatAmt(BigDecimal amount) {
		if (amount == null) {
			amount = BigDecimal.ZERO;
		}

		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(amount);
	}
	
	public static String formatAmtWithoutSign(BigDecimal amount) {
		if (amount == null) {
			amount = BigDecimal.ZERO;
		}

		NumberFormat formatter = NumberFormat.getInstance();
		return formatter.format(amount);
	}
	
	
	
	public static String getReadableTime(long millis) { 
		final long initialValue = millis;

        final long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        millis -= TimeUnit.SECONDS.toMillis(seconds); 

        final StringBuilder sb = new StringBuilder(64);
        
        boolean greater = false;
        if(days > 0) {
	        sb.append(days);
	        sb.append(" Days ");
	        greater = true;
        }
        
        if(hours > 0 || greater) {
	        sb.append(hours);
	        sb.append(" Hours ");
	        greater = true;
        }
        
        if(minutes > 0 || greater) {
	        sb.append(minutes);
	        sb.append(" Minutes ");
	        greater = true;
        }
        
        if(seconds > 0 || greater) {
	        sb.append(seconds);
	        sb.append(" Seconds "); 
        }
        
        sb.append(millis);
        sb.append(" Milliseconds ");
        sb.append(" : Actual total Millis: " + initialValue);

        return(sb.toString());
    }
}
