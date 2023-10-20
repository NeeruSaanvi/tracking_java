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

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.val;

@SuppressWarnings("serial")
public class CustomTimestampDeserializer extends StdDeserializer<ZonedDateTime> {
	/*
	 * private static final String DATE_TIME_WITH_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	 * private static final String DATE_TIME_WITH_TIMEZONE_FORMAT =
	 * "yyyy-MM-dd'T'HH:mm:ssXXX";
	 */
	private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
			// date/time
			.append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
			// offset (hh:mm - "+00:00" when it's zero)
			.optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
			// offset (hhmm - "+0000" when it's zero)
			.optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
			// offset (hh - "Z" when it's zero)
			.optionalStart().appendOffset("+HH", "Z").optionalEnd()
			// create formatter
			.toFormatter();

	public CustomTimestampDeserializer() {
		this(null);
	}

	public CustomTimestampDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public ZonedDateTime deserialize(final JsonParser jsonparser, final DeserializationContext context)
			throws IOException {
		val date = jsonparser.getText();
		return ZonedDateTime.parse(date, ISO_DATE_TIME_FORMATTER);
	}
}