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
public class DashboardSocialActivityPerSiteResponse implements Serializable {
	private String periodType;
	
	private Integer fbTotalInteractions;
	private Integer fbTotalPosts;
	private Double fbInteractionRate;
	
	private Integer inTotalInteractions;
	private Integer inTotalPosts;
	private Double inInteractionRate;
	
	private Integer twTotalInteractions;
	private Integer twTotalPosts;
	private Double twInteractionRate;
	
	private Integer ytTotalInteractions;
	private Integer ytTotalPosts;
	private Double ytInteractionRate;
	
	//Query purpose
	private Integer ytRecTotal;
	private Integer ytRecViewTotal;
}

