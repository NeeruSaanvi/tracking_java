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
package com.tracker.services.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString(includeFieldNames = true)
public class WorkflowResponseDTO implements Serializable {
	
	@JsonProperty("graphql.user.id")
	private String instaUserId;
	
	private Long dbUserId;
	
	private Boolean status;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.id")
	private String id;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.__typename")
	private String postType;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.owner.username")
	private String ownerUserName;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.owner.id")
	private String ownerUserId;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.display_url")
	private String standardImage;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.thumbnail_src")
	private String thumbnailImage;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.edge_media_to_caption.edges[0].node.text")
	private String text;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.edge_media_to_comment.count")
	private String commentsCount;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.edge_media_preview_like.count")
	private String likesCount;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.video_view_count")
	private String videoViewVount;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.taken_at_timestamp")
	private Long createdTime;
	
	@JsonProperty("data.user.edge_owner_to_timeline_media.edges.node.thumbnail_resources[0].src")
	private String userProfileThumbnailImage;
	

}
