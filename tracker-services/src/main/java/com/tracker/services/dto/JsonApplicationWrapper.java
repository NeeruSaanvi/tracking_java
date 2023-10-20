package com.tracker.services.dto;

import java.util.List;

import com.tracker.commons.models.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(includeFieldNames = true)
public class JsonApplicationWrapper {

	private String message = "";

	private boolean success = true;
	private List<User> data;

	public JsonApplicationWrapper() { 
	}


}
