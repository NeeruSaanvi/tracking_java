package com.tracker.commons.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/*
 * Some info on time zones, Derby the handling of timestamps and what we want.
 * 
 * We want: Timestamps stored as UTC, so we can move the DB where ever and it'll operate the same and 
 * not be "x" hours out of sync with local time
 * 
 * What Derby does: Timestamps are stored in local server time and it is a function of JDBC - JPA cannot override (as at 1/2018) - stupid
 * 
 * So, we are passing UTC to the Timestamp object (The Instant object is in UTC), but JDBC hands it to Derby in the server's local time 
 * and it is persisted.
 * 
 * The only practical way around this is to force Java to use UTC. This will be left implemented for the when this is no longer required.
 */

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	// Convert from local time zone to UTC
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
    	return (locDateTime == null ? null : Timestamp.from(locDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    // Convert from UTC to local time zone
    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dateTime) {
    	return (dateTime == null ? null : LocalDateTime.ofInstant(dateTime.toInstant(),ZoneId.systemDefault()));
    }
}