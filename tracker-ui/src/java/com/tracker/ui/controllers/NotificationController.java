package com.tracker.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tracker.commons.models.Notification;
import com.tracker.services.impls.NotificationService;
import com.tracker.services.utils.GoogleCloudService;
import com.tracker.ui.utils.UserUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class NotificationController extends BaseController {
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	GoogleCloudService googleCloudService;
	
	@Autowired
    private Environment environment;
	

	@RequestMapping("/notifications")
	public ModelAndView notifications(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		
		ModelAndView model = new ModelAndView();
    	model.setViewName("notifications");
    	
    	val notificationList = notificationService.getNotificationByOwner(UserUtils.getLoggedInUserId());
		model.addObject("notificationList", notificationList);
    	
    	return model;
	}
	
	@RequestMapping("/updateTemplates")
	public ModelAndView updateTemplates(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		
		ModelAndView model = new ModelAndView();
    	model.setViewName("updateTemplates");
    	
    	String idStr = request.getParameter("id");
    	
    	String[] activeProfiles = environment.getActiveProfiles();  
    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
    	//result = true;
    	if(StringUtils.isNotBlank(idStr)) {
    		Long id = Long.parseLong(idStr);
    		
    		val notification = notificationService.findNotificationById(id);
    		
    		if (StringUtils.isNotBlank(notification.getAttachment())
    				&& notification.getAttachment().indexOf("https://") == -1) {
    			
    			String picURL = StringUtils.substringAfterLast(notification.getAttachment(), "/");
    			
    			URL uploadPicture = googleCloudService.getBucketImageUrl(result?"emp_document_sports":"emp_document",picURL);
    			if(uploadPicture != null) {
    				notification.setAttachment(uploadPicture.toString());
    			}
    			
    		}
    		
    		if (StringUtils.isNotBlank(notification.getAttachment1())
    				&& notification.getAttachment1().indexOf("https://") == -1) {
    			
    			String picURL = StringUtils.substringAfterLast(notification.getAttachment1(), "/");
    			
    			URL uploadPicture = googleCloudService.getBucketImageUrl(result?"emp_document_sports":"emp_document",picURL);
    			if(uploadPicture != null) {
    				notification.setAttachment1(uploadPicture.toString());
    			}
    		}
    		
    		if (StringUtils.isNotBlank(notification.getAttachment2())
    				&& notification.getAttachment2().indexOf("https://") == -1) {
    			
    			String picURL = StringUtils.substringAfterLast(notification.getAttachment2(), "/");
    			
    			URL uploadPicture = googleCloudService.getBucketImageUrl(result?"emp_document_sports":"emp_document",picURL);
    			if(uploadPicture != null) {
    				notification.setAttachment2(uploadPicture.toString());
    			}
    		}
    		
    		
    		model.addObject("notification", notification);
    	}
    	
    	return model;
	}
	
	private static final int READ_BUFFER_SIZE = 1024;
	
	private byte[] getFileContent(MultipartFile file) throws IOException {
		byte[] fileContentBuffer = new byte[READ_BUFFER_SIZE];
		ByteArrayOutputStream fileContentStore = null;
		if (file != null) {
			fileContentStore = new ByteArrayOutputStream();
			try (InputStream fileInputStream = file.getInputStream()) {
				int readCount = fileInputStream.read(fileContentBuffer);
				while (readCount > 0) {
					fileContentStore.write(fileContentBuffer,0,readCount);
					readCount = fileInputStream.read(fileContentBuffer);
				}
				log.info("Read {} bytes from {}",fileContentStore.toByteArray().length,file.getOriginalFilename());
			}
		}
		return fileContentStore != null ? fileContentStore.toByteArray() : null;
	}
	
	@PostMapping("/rest/updateTemplateMsg")
	public @ResponseBody String updateTemplateMsg(@ModelAttribute Notification notification) throws IOException {
		
		String[] activeProfiles = environment.getActiveProfiles();
		boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);

		String bucketName = "emp_document";
		if (result) {
			bucketName = "emp_document_sports";
		}

		if (notification.getId() != null && notification.getId() > 0) {

			byte[] inputSchema = getFileContent(notification.getAttachmentFile());
			byte[] inputSchema1 = getFileContent(notification.getAttachmentFile1());
			byte[] inputSchema2 = getFileContent(notification.getAttachmentFile2());

			if (inputSchema != null && inputSchema.length > 0) {
				String attachment = googleCloudService.uploadFileToBucket(bucketName, inputSchema, notification.getId()
						+ "_" + "attachment_" + notification.getAttachmentFile().getOriginalFilename());

				if (attachment != null) {
					notification.setAttachment(attachment);
				}
			}

			if (inputSchema1 != null && inputSchema1.length > 0) {
				String attachment1 = googleCloudService.uploadFileToBucket(bucketName, inputSchema1, notification.getId()
						+ "_" + "attachment1_" + notification.getAttachmentFile1().getOriginalFilename());

				if (attachment1 != null) {
					notification.setAttachment1(attachment1);
				}
			}

			if (inputSchema2 != null && inputSchema2.length > 0) {
				String attachment2 = googleCloudService.uploadFileToBucket(bucketName, inputSchema2, notification.getId()
						+ "_" + "attachment2_" + notification.getAttachmentFile2().getOriginalFilename());

				if (attachment2 != null) {
					notification.setAttachment2(attachment2);
				}
			}

			notificationService.updateTemplate(notification);
		}

		return "success";
	}
	
	 
	

}
