/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.converters;
import org.springframework.core.convert.converter.Converter;

/*
 * These converters are used to convert incoming 'Form Data' values into Java Objects. 
 * These are not the same as JSON to JAVA. These are Form submission values to Java converters
 */
public class StringToIntegerConverter implements Converter<String, Integer> {
	
	/**
	 * The input string value is guaranteed not to be null by Spring so no need to check for nullsafe
	 */
    @Override
    public Integer convert (String strVal) {
        try {
            return Integer.parseInt(strVal);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
