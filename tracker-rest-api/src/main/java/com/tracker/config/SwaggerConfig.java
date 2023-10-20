package com.tracker.config;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    

	@Bean
	public Docket createRestApi() {

		//Adding Header
		final ParameterBuilder parameterBuilder = new ParameterBuilder();
		//parameterBuilder.name("Authorization").description("Please use Authorization Token provided during the authentication process").
		parameterBuilder.name("Content-Type").defaultValue("application/json").
		modelRef(new ModelRef("string")).parameterType("header").required(true).build();
		
		final ParameterBuilder parameterBuilder2 = new ParameterBuilder();
		//parameterBuilder.name("Authorization").description("Please use Authorization Token provided during the authentication process").
		parameterBuilder.name("Accept").defaultValue("application/json").
		modelRef(new ModelRef("string")).parameterType("header").required(true).build();

		final List<Parameter> globalParams = new ArrayList<Parameter>();
		globalParams.add(parameterBuilder.build());
		//globalParams.add(parameterBuilder2.build());

		return new Docket(DocumentationType.SWAGGER_2) 
				.globalOperationParameters(globalParams)
				.produces(new HashSet<String>(Arrays.asList("application/json")))
				.consumes(new HashSet<String>(Arrays.asList("application/json")))
				.apiInfo(apiInfo())
				.directModelSubstitute(Timestamp.class, Date.class)
				//.pathMapping(WebConfig.BASE_REST_API + "/v1/") //Base URL of the APIs
				.select() 
				.apis(RequestHandlerSelectors.basePackage("com.tracker.rest")) //which package to scan for APIs
				.paths(PathSelectors.any()) 
				.build();

	}  

	private ApiInfo apiInfo() {
		final Contact contact = new Contact("Tracker.com", "https://tracker.com/", "admin@tracker.com");
		return new ApiInfoBuilder()
				.license("License").licenseUrl("https://tracker.com/license")
				.title("Tracker RESTful API Documentation")
				.description("For more information, Please visit https://tracker.com")
				.termsOfServiceUrl("https://tracker.com/TermsOfService")
				.contact(contact).version("1.0").build();
	} 
}