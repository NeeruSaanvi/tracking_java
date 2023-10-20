package com.tracker.config;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tracker.rest.converters.CustomJsonConverter;
import com.tracker.rest.converters.StringToIntegerConverter;
import com.tracker.rest.converters.StringToLongConverter;
import com.tracker.rest.interceptors.LoginInterceptor;

@Configuration 
@ComponentScan(basePackages = "com.tracker")  
public class WebConfig implements WebMvcConfigurer { 

	public final static String BASE_REST_API = "api";
	public final static String BASE_VERSION = "/v1";
	public final static String BASE_REST_API_V1 = "/" + BASE_REST_API + BASE_VERSION;
	
	/*@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		//This should make the url case insensitive
		AntPathMatcher matcher = new AntPathMatcher();
		matcher.setCaseSensitive(false);
		configurer.setPathMatcher(matcher);
	}

	*//**
	 * Following method/bean will set the base path for the all REST API Controllers automatically
	 * @return
	 *//*
	@Bean
	public WebMvcRegistrationsAdapter webMvcRegistrationsHandlerMapping() {
		return new WebMvcRegistrationsAdapter() {
			@Override
			public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
				return new RequestMappingHandlerMapping() {


					@Override
					protected void registerHandlerMethod(final Object handler, final Method method, RequestMappingInfo mapping) {
						final Class<?> beanType = method.getDeclaringClass();
						if (AnnotationUtils.findAnnotation(beanType, RestController.class) != null) {
							final PatternsRequestCondition apiPattern = new PatternsRequestCondition(BASE_REST_API)
									.combine(mapping.getPatternsCondition());

							mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
									mapping.getMethodsCondition(), mapping.getParamsCondition(),
									mapping.getHeadersCondition(), mapping.getConsumesCondition(),
									mapping.getProducesCondition(), mapping.getCustomCondition());
						}

						super.registerHandlerMethod(handler, method, mapping);
					}
				};
			}
		};
	}*/

	/*
	 * Register REST converters that will convert JSON to JAVA and JAVA to JSON.
	 * We are using gson but can use anything
	 */
	@Override
	public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
		converters.add(byteArrayHttpMessageConverter());
		converters.add(new CustomJsonConverter());
		converters.add(new MappingJackson2HttpMessageConverter());

		//super.configureMessageConverters(converters);
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

	/*
	 * Allow CORS request from All Origins
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/*").allowedOrigins(CorsConfiguration.ALL);
	}

}