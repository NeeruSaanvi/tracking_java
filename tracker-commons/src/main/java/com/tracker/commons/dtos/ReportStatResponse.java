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
public class ReportStatResponse implements Serializable {
	private Integer totalPostFb; 
	private Integer totalInteractionsFb;	
	private Double effectivenessRateFb;
	private Double avgPostsPerMemberFb;
	private Double avgInteractionsPerMemberFb;
	private Double postsPerWeekFb;
	
	private String totalPostFbFormatter; 
	private String totalInteractionsFbFormatter;
	
	private Integer totalPostTw; 
	private Integer totalInteractionsTw;	
	private Double effectivenessRateTw;
	private Double avgPostsPerMemberTw;
	private Double avgInteractionsPerMemberTw;
	private Double postsPerWeekTw;
	
	private String totalPostTwFormatter; 
	private String totalInteractionsTwFormatter;
	
	private Integer totalPostIn; 
	private Integer totalInteractionsIn;	
	private Double effectivenessRateIn;
	private Double avgPostsPerMemberIn;
	private Double avgInteractionsPerMemberIn;
	private Double postsPerWeekIn;
	
	private String totalPostInFormatter; 
	private String totalInteractionsInFormatter;
	
	private Integer grandTotalPost; 
	private Integer grandTotalInteractions;	
	private Double grandEffectivenessRate;
}
