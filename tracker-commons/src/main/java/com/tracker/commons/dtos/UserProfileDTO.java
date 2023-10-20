package com.tracker.commons.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings({ "serial" })
@EqualsAndHashCode
@ToString(includeFieldNames = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO implements Serializable {
	
	private String totalInteractions;
	private String totalPosts;
	private String totalEffectivenessRate;
	
	private String annualRankScore;
}

