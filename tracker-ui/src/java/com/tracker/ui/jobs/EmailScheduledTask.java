package com.tracker.ui.jobs;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tracker.commons.email.EmailNotifications;
import com.tracker.services.impls.EmailService;
import com.tracker.services.repositories.EmailNotificationsRepository;
import com.tracker.services.utils.Utility;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailScheduledTask {

	@Autowired
	private EmailService emailService;

	@Autowired
	private  EmailNotificationsRepository emailNotificationsRepository; 

	/**
	 * Options:
	 *  fixedDelay = the duration between end of last execution and the start of the next execution is fixed. The task always wait until the previous one is finished.
	 *  fixedRate = the beginning of the task execution doesn't wait for the last one to finish..it just executes at fixed rate
	 */
//	@Scheduled(fixedDelayString = "${schedules.emailNotificationService.fixedDelay}", 
//			initialDelayString = "${schedules.emailNotificationService.initialDelay}")
	public void fetchMarketPrices() { 

		final Date startTime = new Date();
/*
		log.debug("     ");
		log.debug("     ");
		log.debug("     ");
		log.debug("##################################################################################");
		log.debug("##################################################################################");
		log.debug("##################################################################################");
		log.debug("#############   Executing the Email Notification Service Job #####################");
		log.debug("##################################################################################");
		log.debug("##################################################################################");
		log.debug("##################################################################################");
		log.debug("     ");
		log.debug("     ");
		log.debug("     "); */

		Date tempStartTime = new Date();

		final List<EmailNotifications> notifications = emailNotificationsRepository.findByStatusFalseAndRetryCountLessThan(200);

		tempStartTime = new Date();

		for(EmailNotifications notification : notifications) {
			try {
				emailService.retryEmail(notification);
			} 
			catch (Exception e) {
			}
		}

/*

		log.debug("     ");
		log.debug("     ");
		log.debug("     ");
		log.debug("##################################################################################");
		log.debug("########  Finished Executing the Email Notification Service Job ##################");
		log.debug("##################################################################################");
		log.debug("     ");
		log.debug("     ");
		log.debug("     ");*/

	}

}