package com.tracker.ui.converters;
import org.springframework.core.convert.converter.Converter;

/*
 * These converters are used to convert incoming 'Form Data' values into Java Objects. 
 * These are not the same as JSON to JAVA. These are Form submission values to Java converters
 */
public class StringToLongConverter implements Converter<String, Long> {
  
	/**
	 * The input string value is guaranteed not to be null by Spring so no need to check for nullsafe
	 */
    @Override
    public Long convert (String strVal) {
        try {
            return Long.parseLong(strVal);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}