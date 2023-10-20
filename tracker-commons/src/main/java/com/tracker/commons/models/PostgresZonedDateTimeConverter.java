/*******************************************************************************
 * Copyright (c) 2018 by RiskMate.                                    
 * All rights reserved.                                                       
 *                                                                              
 * This software is the confidential and proprietary information of 
 * RiskMate ("Confidential Information"). 
 * You shall not disclose such confidential Information and shall 
 * use it only in accordance with  the terms of the license agreement 
 * you entered with RiskMate.
 ******************************************************************************/
package com.tracker.commons.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Attribute converter class that converts Database Timestamp into ZonedDateTime and viceversa
 * @author viral
 *
 */
@Converter(autoApply = false)
public class PostgresZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(final ZonedDateTime attribute) {
		LocalDateTime withoutTimezone = attribute.toLocalDateTime();
		return Timestamp.valueOf(withoutTimezone);
	}

	@Override
	public ZonedDateTime convertToEntityAttribute(Timestamp dbData) {
		LocalDateTime withoutTimezone = dbData.toLocalDateTime();
		return withoutTimezone.atZone(ZoneId.of("UTC"));
	}

}
