/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(includeFieldNames=true) 
public class AccountCredentials {

  private String username;
  private String password;
  // getters & setters
}
