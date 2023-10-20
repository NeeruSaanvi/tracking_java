/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.exceptions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;

public class JsonView {

    public static ModelAndView Render(Object model, HttpServletResponse response) {
    	MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();

        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        try {
            jsonConverter.write(model, jsonMimeType, new ServletServerHttpResponse(response));
        } catch (HttpMessageNotWritableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
