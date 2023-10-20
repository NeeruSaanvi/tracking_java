package com.tracker.commons.dtos;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings({ "serial" })
@EqualsAndHashCode
@ToString(includeFieldNames = true)
@Data
@AllArgsConstructor
public class AuthResponse implements Serializable {
	private String token; 
	private ZonedDateTime expirationTime;
}
