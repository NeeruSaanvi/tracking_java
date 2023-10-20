package com.tracker.ui.exceptions;

import lombok.Data;

@Data
public class RestErrorResponse {
	private String status = "success";
	private String message = ""; 
}

