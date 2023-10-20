package com.tracker.commons.dtos;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings({ "serial" })
@EqualsAndHashCode
@ToString(includeFieldNames = true, exclude= {"password"})
@Data
public class AuthRequest implements Serializable {
	private String email;
	private String password; 
}
