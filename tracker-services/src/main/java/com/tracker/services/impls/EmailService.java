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
package com.tracker.services.impls;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tracker.commons.Constants;
import com.tracker.commons.email.EmailNotifications;
import com.tracker.services.repositories.EmailNotificationsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
	
	@Value("${default.email}")
	private String defaultEmail;
	
	@Value("${default.ccEmail}")
	private String defaultCCEmail;
	
	@Autowired
	private Environment environment;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private  EmailNotificationsRepository emailNotificationsRepository; 

	public void sendMail(String from, String to, String subject, String text, String cc) {
		sendMail(from, to, subject, text, cc, null);
	}

	public void sendMail(String from, String to, String subject, String text, String cc, Long userId) {
		sendMail(from, to, subject, text, cc, null, null, userId);
	}

	public void sendMail(String from, String to, String subject, String text, String cc, String bcc, List<File> fileAttachments, Long userId) {
		
		if (this.environment.getActiveProfiles()[0].equals("default")) {
			to = defaultEmail;
			cc = defaultCCEmail;
		}
		
		sendMail(this.getEmailNotification(subject, text, from, to, cc, bcc, from, fileAttachments, userId));
	}
	
	public void sendMail1(String from, String to, String subject, String text, byte[] filedata, Long userId) {
		MimeMessage mail = emailSender.createMimeMessage();

		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mail, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);

			helper.setFrom(from);

			helper.addAttachment("attachemtn1.pdf", new ByteArrayDataSource(filedata, "text/html"));
			emailSender.send(mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void retryEmail(EmailNotifications notification) {

		try {

			MimeMessage mail = emailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			helper.setTo(notification.getRecipients());
			helper.setSubject(notification.getSubject());
			helper.setText(notification.getBody(), true);

			helper.setFrom(notification.getSender());

			if (StringUtils.isNotBlank(notification.getCC())) {
				helper.setCc(notification.getCC());
			}

			//Add attachments if any
			if(notification.getAttachment() != null) {
				helper.addAttachment("attachemtn1", new ByteArrayDataSource(notification.getAttachment(), "text/html"));
			} 

			emailSender.send(mail);

			notification.setStatus(true);
		} 
		catch (Exception e) {
			notification.setStatus(false);
			notification.setRetryCount(notification.getRetryCount() + 1);
			log.error("Error while sending email to: {} ", notification.getRecipients(), e);
		}
		finally {
			emailNotificationsRepository.save(notification);
		}
	}

	public void sendMail(EmailNotifications notification) {
		 
		notification.setStatus(false);
		notification.setRetryCount(notification.getRetryCount() + 1);
		log.debug("Scheduling an email to: {} ", notification.getRecipients());
		log.trace("Scheduled email info: {}", notification);
	 
		emailNotificationsRepository.save(notification);
	}

	private EmailNotifications getEmailNotification(String subject,
			String body, String replyTo, String recipients, String cc, String bcc, String from, List<File> attachment, Long userId) {

		EmailNotifications notification = new EmailNotifications();
		notification.setSubject(subject);
		notification.setSender(from);
		notification.setReplyTo(replyTo);
		notification.setRecipients(recipients);
		notification.setCC(cc);
		notification.setBCC(bcc);
		notification.setBody(body);

		Long loggedInUser = userId == null ? Constants.SYSTEM_DEFAULT_USER_ID : userId;

		notification.setCreatedBy(loggedInUser);
		notification.setModifiedBy(loggedInUser);

		if (null != attachment && attachment.size() > 0) {
			notification.setAttachment(attachment.get(0).toString().getBytes());
		}

		return notification;
	} 

}
