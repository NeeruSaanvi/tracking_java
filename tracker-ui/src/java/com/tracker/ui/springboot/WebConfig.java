package com.tracker.ui.springboot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tracker.ui.converters.CustomJsonConverter;
import com.tracker.ui.converters.StringToIntegerConverter;
import com.tracker.ui.converters.StringToLongConverter;
import com.tracker.ui.interceptors.LoginInterceptor;

@Configuration 
@ComponentScan(basePackages = "com.tracker")  
public class WebConfig implements WebMvcConfigurer { 


	/*
	 * Register REST converters that will convert JSON to JAVA and JAVA to JSON.
	 * We are using gson but can use anything
	 */
	@Override
	public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
		converters.add(byteArrayHttpMessageConverter());
		converters.add(new CustomJsonConverter());
		converters.add(new MappingJackson2HttpMessageConverter());
	} 

	/*
	 * Register Form Submission converters that will convert Request Parameters (submitted via Form Submission) to JAVA.
	 */
	@Override
	public void addFormatters (final FormatterRegistry registry) {
		registry.addConverter(new StringToIntegerConverter());
		registry.addConverter(new StringToLongConverter());
	} 

	@Bean
	public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
		final ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
		arrayHttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());
		return arrayHttpMessageConverter;
	}

	private List<MediaType> getSupportedMediaTypes() {
		final List<MediaType> list = new ArrayList<MediaType>();
		list.add(MediaType.IMAGE_JPEG);
		list.add(MediaType.IMAGE_PNG);
		list.add(MediaType.APPLICATION_OCTET_STREAM);

		return list;
	} 

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/");
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
	    registry.jsp();
	    // OR
	    registry.jsp("/",".jsp");
	}

}