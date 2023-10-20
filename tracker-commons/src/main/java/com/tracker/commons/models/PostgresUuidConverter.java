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
package com.tracker.commons.models;

import java.util.UUID;

import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;

//@Converter(autoApply = false)
public class PostgresUuidConverter implements AttributeConverter<UUID, String> {

	@Override
	public String convertToDatabaseColumn(final UUID attribute) {
		return attribute.toString();
	}

	@Override
	public UUID convertToEntityAttribute(String dbData) {
		return UUID.fromString(dbData);
	}

}
