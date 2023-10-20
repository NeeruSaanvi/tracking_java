package com.tracker.commons.dtos;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

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
public class DashboardQuickMessage implements Serializable {
	private Long memberId; 
	private Long teamId;
	private String subject;
	private String message;
	private String messageFormat;
	private String messageText;
	private String isText;
	
	private MultipartFile attachmentFile;
}
