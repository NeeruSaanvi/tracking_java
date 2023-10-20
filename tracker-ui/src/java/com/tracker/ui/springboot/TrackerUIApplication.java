package com.tracker.ui.springboot;

import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.apache.catalina.connector.Connector;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.hibernate4.encryptor.HibernatePBEEncryptorRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.context.request.RequestContextListener;

import com.tracker.ui.hibernate.CustomDataSource;
import com.tracker.ui.security.CustomAccessDeniedHandler;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zaxxer.hikari.HikariConfig;

@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
@ComponentScan("com.tracker.*")
@EnableJpaRepositories(basePackages = "com.tracker")
@EntityScan(basePackages = "com.tracker.commons")
@Configuration
@EnableEncryptableProperties
@EnableAsync
@EnableScheduling
public class TrackerUIApplication implements AsyncConfigurer {

	@Value("${server.ajpPort}")
	private int ajpPort = 6073;
	
	@Value("${server.ajpEnabled}")
	private Boolean tomcatAjpEnabled = true;

	@Value("${jasypt.encryptor.password}")
	private String jasyptMasterPassword;

	@Value("${spring.datasource.url}")
	private String jdbcUrl;

	@Value("${spring.datasource.driver-class-name}")
	private String jdbcDriverClass;

	@Value("${spring.datasource.username}")
	private String jdbcUsername;

	@Value("${spring.datasource.password}")
	private String jdbcPassword;

	@Value("${spring.datasource.hikari.maximum-pool-size}")
	private Integer jdbcMaximumPoolsize;

	@Value("${spring.datasource.hikari.connection-timeout}")
	private Integer jdbcConnectionTimeout;

	@Value("${spring.datasource.hikari.idle-timeout}")
	private Integer jdbcIdleTimeout;

	@Value("${spring.datasource.hikari.connection-test-query}")
	private String jdbcConnectionTestQuery;

	@Value("${spring.datasource.hikari.pool-name}")
	private String jdbcPoolname;

	public static void main(final String[] args) {
//		String[]  args1 = new String[1];
//		args1[0] = "sportsman";
//		SpringApplication application = 
//			      new SpringApplication(TrackerUIApplication.class);
//			    application.setAdditionalProfiles("sportsman");
//	
//			    application.run(args);
//			  
		SpringApplication.run(TrackerUIApplication.class, args);
		
	}
	
	
	  @Bean 
	  public TomcatServletWebServerFactory servletContainer() {
	  
		  TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		  if (tomcatAjpEnabled) { Connector ajpConnector = new Connector("AJP/1.3");
			  ajpConnector.setPort(ajpPort); ajpConnector.setSecure(false);
			  ajpConnector.setAllowTrace(false); ajpConnector.setScheme("https");
			  tomcat.addAdditionalTomcatConnectors(ajpConnector); 
		  }
		  
		  return tomcat; 
	  }

	// Jasypt Encryption
	@Bean(name = "encryptorBean")
	public StringEncryptor stringEncryptor() {
		final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

		final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(jasyptMasterPassword);
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("10");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);

		HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
		registry.registerPBEStringEncryptor("STRING_ENCRYPTOR", encryptor);

		return encryptor;
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	@Primary // this will override the datasource autoconfiguration and use your own everywhere
	public DataSource dataSource() { 

		HikariConfig config = new HikariConfig();

		config.setJdbcUrl(jdbcUrl);
		config.setDriverClassName(jdbcDriverClass);
		config.setUsername(jdbcUsername);
		config.setPassword(jdbcPassword);
		config.setMaximumPoolSize(jdbcMaximumPoolsize);
		config.setConnectionTimeout(jdbcConnectionTimeout);
		config.setConnectionTestQuery(jdbcConnectionTestQuery);
		config.setIdleTimeout(jdbcIdleTimeout);
		config.setPoolName(jdbcPoolname);

		return new CustomDataSource(config);
	}
	
	@Value("${schedules.poolsize}")
	private int schedulePoolSize;

	@Bean() //this pool is for schedules threads to execute
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(schedulePoolSize); 
		taskScheduler.setThreadNamePrefix("Task Scheduler Thread");
		taskScheduler.setAwaitTerminationSeconds(20);
		taskScheduler.initialize();

		return  taskScheduler;
	}

	@Override //This pool is for individual threads within the schedule that get spawned i.e 1 for each exchange services
	public Executor getAsyncExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(schedulePoolSize);
		executor.setMaxPoolSize(schedulePoolSize);
		executor.initialize();

		return executor;
	}
	 
}
