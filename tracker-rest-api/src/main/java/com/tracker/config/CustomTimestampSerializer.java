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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tracker.services.utils.TimeZoneEnum;
import com.tracker.services.utils.UserUtils;

@SuppressWarnings("serial")
public class CustomTimestampSerializer extends StdSerializer<ZonedDateTime> {
  private static final String DATE_TIME_WITH_TIMEZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

  public CustomTimestampSerializer() {
    this(null);
  }

  @SuppressWarnings("unchecked")
  public CustomTimestampSerializer(@SuppressWarnings("rawtypes") final Class t) {
    super(t);
  }

  @Override
  public void serialize(final ZonedDateTime value, final JsonGenerator gen,  final SerializerProvider arg2) throws IOException {
	 /* if(UserUtils.isUserLoggedIn()) {
		  ZoneId timezone = ZoneId.of(TimeZoneEnum.findById(UserUtils.getLoggedInUser().getTimeZone()).getTzName());
		  ZonedDateTime timezonedTime = value.withZoneSameInstant(timezone);
		  String valueStr = timezonedTime.format(DateTimeFormatter.ofPattern(DATE_TIME_WITH_TIMEZONE_FORMAT));
		  gen.writeString(valueStr);
	  }
	  else {*/
		  String valueStr = value.format(DateTimeFormatter.ofPattern(DATE_TIME_WITH_TIMEZONE_FORMAT));
		  gen.writeString(valueStr);
	 //}
  }
}