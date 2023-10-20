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
public class DashboardSocialMediaStats implements Serializable {
	private Integer totalPost = 0; 
	private Integer totalInteraction = 0; 
	private Double totalInteractionRate = 0d;
}
