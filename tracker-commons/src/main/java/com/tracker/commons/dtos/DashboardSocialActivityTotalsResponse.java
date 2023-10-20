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
public class DashboardSocialActivityTotalsResponse implements Serializable {
	private Integer id;
	private String totalType; 
	private Integer totalMembers;
	private Integer totalPosts;
	private Integer totalInteractions; 
	private Double interactionRate;
	private Double percentageChange;
}
