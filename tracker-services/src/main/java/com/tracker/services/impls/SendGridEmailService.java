package com.tracker.services.impls;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.tracker.commons.Constants;
import com.tracker.commons.email.EmailNotifications;
import com.tracker.services.repositories.EmailNotificationsRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class SendGridEmailService {

	@Autowired
	private  EmailNotificationsRepository emailNotificationsRepository; 
	
//	@Autowired
//    private Environment environment;
	
	@Value("${default.fromEmail}")
	private String fromEmail;
	
	@Value("${sendgrid.apikey}")
	private String SENDGRID_API_KEY;
	
	@Value("${default.email}")
	private String email;
	
	@Value("${default.ccEmail}")
	private String ccEmail;
	

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCcEmail() {
		return ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

	public void sendMail(String from, String to, String subject, String text, String cc) {
		sendMail(from, to, subject, text, cc, null);
	}

	public void sendMail(String from, String to, String subject, String text, String cc, Long userId) {
		sendMail(from, to, subject, text, cc, null, null, userId);
	}

	public void sendMail(String from, String to, String subject, String text, String cc, String bcc, List<File> fileAttachments, Long userId) {
		sendMail(this.getEmailNotification(subject, text, from, to, cc, bcc, from, fileAttachments, userId));
	}
	
	public void sendEmailUsingTemplate(String to, String from, String subject, Map<String,String> params, String templateName, String cc) throws IOException {
		String template = getEmailTemplate(templateName);
		String body = createEmailFromTemplate(params,template);

		try {
			sendMail(from, to, subject, body, cc, null);
		} catch (Exception e) {
			log.error("Failed to send email with subject {} to '{}'",subject, to,e);
		}
		
	}
	
	public static String createEmailFromTemplate(Map<String,String> params, String template) {
		String email = template;
		for (Map.Entry<String,String> entry : params.entrySet()) {
			email = email.replace(entry.getKey(),entry.getValue() != null ? entry.getValue() : "");
		}
		return email;
	}
	
	public static String getEmailTemplate(String name) throws IOException {
		final char[] buffer = new char[1024];
		final StringBuilder template = new StringBuilder();
		try {
			InputStream templateIn = SendGridEmailService.class.getClassLoader().getResourceAsStream(name);
			if (templateIn != null) {
				Reader templateReader = new InputStreamReader(templateIn,"UTF-8");
				int resultSize;
				resultSize = templateReader.read(buffer,0,buffer.length);
				while (resultSize >= 0) {
					if (resultSize > 0)
						template.append(buffer,0,resultSize);
					resultSize = templateReader.read(buffer,0,buffer.length);
				}
			}
			else
				throw new IOException("Cannot find template at '" + name + "'");
		} catch (IOException e) {
			log.error("Cannot read email template at '{}'",name,e);
			throw e;
		}
		return template.toString();
	}
	
	public void sendDashboardQuickMail(String from1, String to1, String subject1, String text, byte[] filedata,
			String fileName, Long userId) throws IOException {
		
		log.info("sending Dashboard Quick Mail to "+to1);
		
//		String[] activeProfiles = environment.getActiveProfiles();  
//    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
//    	
//    	if(result) {
//    		from1 = "no-reply@ambassadortrack.net";
//    	}
		
		Email from = new Email(fromEmail);
		Email to = new Email(to1);
		Content content = new Content("text/html", text);
		
		Personalization personalization = new Personalization();
		
		Mail mail = new Mail(from, subject1, to, content);
		
		//Email bcc = new Email("debugmycode80@gmail.com");
		//personalization.addBcc(bcc);
		personalization.addTo(to);
		personalization.setSubject(subject1);
		mail.addPersonalization(personalization);

		Base64 x = new Base64();

		if (filedata != null && filedata.length > 0) {
			String imageDataString = x.encodeAsString(filedata);
			Attachments attachments3 = new Attachments();
			attachments3.setContent(imageDataString);
			attachments3.setFilename(fileName);
			attachments3.setDisposition("attachment");
			attachments3.setContentId("Banner");
			mail.addAttachments(attachments3);
		}

		SendGrid sg = new SendGrid(SENDGRID_API_KEY);
		Request request = new Request();
		request.setMethod(Method.POST);
		request.setEndpoint("mail/send");
		request.setBody(mail.build());
		Response response = sg.api(request);
		log.debug("Status code :: {}", response.getStatusCode());
		log.info("Dashboard Quick Mail sent sucessfully for .."+to1);
	
	}

	public void sendMail(EmailNotifications notification) {

		try {
			
//			String[] activeProfiles = environment.getActiveProfiles();  
//	    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
//	    	
//	    	if(result) {
//	    		notification.setSender("no-reply@ambassadortrack.net");
//	    	}
			notification.setSender(fromEmail);
			
			Email from = new Email(notification.getSender());
			Email to = new Email(notification.getRecipients());
			Content content = new Content("text/html", notification.getBody());
			
			Personalization personalization = new Personalization();
			
			Mail mail = new Mail(from, notification.getSubject(), to, content);
			
			if(StringUtils.isNotBlank(notification.getCC())) {
				Email cc = new Email(notification.getCC());
				personalization.addCc(cc);
				personalization.addTo(to);
				personalization.setSubject(notification.getSubject());
				mail.addPersonalization(personalization);
			}
			
			
			if(notification.getAttachment() != null) {
				Base64 x = new Base64();
				
				String imageDataString = x.encodeAsString(notification.getAttachment());
				Attachments attachments3 = new Attachments();
				attachments3.setContent(imageDataString);
				//attachments3.setType("application/pdf");
				attachments3.setFilename(notification.getAttachmentName());
				attachments3.setDisposition("attachment");
				attachments3.setContentId("Banner");
				mail.addAttachments(attachments3);
			}
			
			SendGrid sg = new SendGrid(SENDGRID_API_KEY);
			Request request = new Request();
			try {
				request.setMethod(Method.POST);
				request.setEndpoint("mail/send");
				request.setBody(mail.build());
				try {
					Response response = sg.api(request);
					log.debug("Status code :: {}", response.getStatusCode());
				}catch(Exception e) {
					e.printStackTrace();
				}
				notification.setStatus(true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
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

		long loggedInUser = userId == null ? Constants.SYSTEM_DEFAULT_USER_ID : userId;

		notification.setCreatedBy(loggedInUser);
		notification.setModifiedBy(loggedInUser);

		if (null != attachment && attachment.size() > 0) {
			notification.setAttachment(attachment.get(0).toString().getBytes());
			notification.setAttachmentName(attachment.get(0).getName());
		}

		return notification;
	} 

}