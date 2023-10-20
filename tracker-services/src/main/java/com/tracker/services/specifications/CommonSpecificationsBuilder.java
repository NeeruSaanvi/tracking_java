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
package com.tracker.services.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

@SuppressWarnings("deprecation")
public class CommonSpecificationsBuilder {

	private final List<SearchCriteria> params;

	public CommonSpecificationsBuilder() {
		params = new ArrayList<SearchCriteria>();
	}

	public CommonSpecificationsBuilder with(String key, String operation, Object value) {
		params.add(new SearchCriteria(key, operation, value));
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Specification<?> build() {
		if (params.size() == 0) {
			return null;
		}

		List<Specification> specs = new ArrayList<Specification>();
		for (SearchCriteria param : params) {
			specs.add(new CommonSpecification(param));
		}

		Specification<?> result = specs.get(0);
		for (int i = 1; i < specs.size(); i++) {
			result = Specifications.where(result).and(specs.get(i));
		}
		return result;
	}
}
