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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class CustomLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

  private static final String DATE_TIME_WITH_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"; 

  public CustomLocalDateTimeDeserializer() {
    this(null);
  }

  public CustomLocalDateTimeDeserializer(Class<?> vc) {
    super(vc);
  }

	@Override
	public LocalDateTime deserialize(final JsonParser jsonparser, final DeserializationContext context)
			throws IOException {
		final String date = jsonparser.getText();
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_TIME_WITH_FORMAT));
	}
}