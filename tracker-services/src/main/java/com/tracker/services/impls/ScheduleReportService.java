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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.tracker.commons.dtos.ReportExportDTO;
import com.tracker.commons.dtos.ReportStatResponse;
import com.tracker.commons.exceptions.UserException;
import com.tracker.commons.models.Reports;
import com.tracker.commons.models.ScheduleReport;
import com.tracker.commons.models.UserTeam;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.repositories.ReportsRepository;
import com.tracker.services.repositories.ScheduleReportRepository;
import com.tracker.services.repositories.UserRepository;
import com.tracker.services.repositories.UserTeamRepository;
import com.tracker.services.utils.JasperUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScheduleReportService {

	@Autowired
	private ScheduleReportRepository scheduleReportRepository;
	
	@Autowired
	private ReportsRepository reportsRepository;
	
	@Autowired
	private ReportsService reportsService;
	
	@Autowired
	private JasperUtils jasperUtils;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private Environment environment;
	
	@Autowired
	private UserTeamRepository userTeamRepository;
	
	private String SENDGRID_API_KEY = "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";


	public List<ScheduleReport> findAll(final PaginationRequestDTO dto, Long companyId) {
		return scheduleReportRepository.findByCreatedBy(companyId);
	} 

	public int findAllCount() {
		return Long.valueOf(scheduleReportRepository.count()).intValue();
	} 
	
	public ScheduleReport findByScheduleId(final Long scheduleId) {
		
		log.debug("Finding the scheduleReport by id:" + scheduleId);
		
		if(scheduleId == null) {
			throw new UserException("Please provide web id");
		}
		
		val scheduleReport = scheduleReportRepository.findByScheduleId(scheduleId);
		
		if(scheduleReport == null) {
			throw new UserException("No scheduleReport found by this id");
		}
		
		
		return scheduleReport;
	}
	
	
	public ScheduleReport saveScheduleReport(ScheduleReport scheduleReport) {
		return scheduleReportRepository.save(scheduleReport);
	}
	 
	
	@Scheduled(initialDelay = 15000, fixedRateString = "3600000") 
	public void scheduleReport() {
		
		log.debug("Finding All Schedule Report:");
		
		List<ScheduleReport> lstReport = scheduleReportRepository.findAll();
		
		/*
		 * Date toDate = Calendar.getInstance().getTime(); Date fromDate =
		 * Calendar.getInstance().getTime(); Date nextScheduleDate =
		 * Calendar.getInstance().getTime();
		 */
		
		LocalDate toDate1 = LocalDate.now(); 
		LocalDate fromDate1 = LocalDate.now();
		
		
		for(ScheduleReport report : lstReport) {
			
			LocalDate nextScheduleDate = LocalDate.now();
			if(report.getStartDate().toLocalDate().equals(toDate1)) {

				log.debug("Schedule Report start:" + report.getScheduleId());
				
				String teams = report.getTeam();
				String members = report.getMembers();
				String finalTeamMembers = "";
				
				if (members != null && StringUtils.isNoneBlank(members) && members.equalsIgnoreCase("All")) {
					members = "";
				}
				
				if (teams != null && StringUtils.isNoneBlank(teams) && teams.equalsIgnoreCase("All")) {
					teams = "";
				}
				
				if(teams != null && StringUtils.isNotBlank(teams)) {
					List<String> teamArray = Arrays.asList( teams.split(","));
					
					if(teamArray != null && teamArray.size() > 0) {
						for(String teamIds : teamArray) {
							List<UserTeam> userTeam = userTeamRepository.findByTeamId(Long.parseLong(teamIds));
							String[] teamMembers = new String[userTeam.size()];
							int index = 0;
							for(UserTeam uTeam : userTeam) {
								teamMembers[index] = String.valueOf(uTeam.getUserId());
								index++;
							}
							finalTeamMembers = finalTeamMembers +","+ String.join(",", teamMembers);
						}
					}
				}
				
				
				List<String> userList = new ArrayList<String>();
				
				userList.addAll(Arrays.asList( finalTeamMembers.split(",")));
				userList.addAll(Arrays.asList( members.split(",")));
				
				List<String> users = userList.stream()
					     .distinct()
					     .collect(Collectors.toList());
								
				if(report.getFrequency().equalsIgnoreCase("Weekly")) {
					fromDate1 = fromDate1.minus(Period.ofDays(7)); 
				}
				
				if(report.getFrequency().equalsIgnoreCase("Monthly")) {
					fromDate1 = fromDate1.minus(Period.ofMonths(1)); 
				}
				
				if(report.getFrequency().equalsIgnoreCase("Quarterly")) {
					fromDate1 = fromDate1.minus(Period.ofMonths(3)); 
				}
				
				Date fromDate = Date.from(fromDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
				Date toDate = Date.from(toDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
				
				String staffs = String.join(",", users); 
				if (staffs != null && StringUtils.isNoneBlank(staffs) && staffs.equalsIgnoreCase("All")) {
					staffs = "";
				}
				
				if (staffs != null && StringUtils.isNoneBlank(staffs)){
					staffs = staffs.replaceFirst(",", "");
				}
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					out = generateExcelReport("", staffs, fromDate, toDate, report.getCreatedBy());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				/*List<Reports> reportList = reportsRepository.generateSchduleReport(fromDate, toDate, users, report.getCreatedBy());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					generateExcel(out, reportList);
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				
				InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
				
				sendScheduleMail("no-reply@anglertrack.net", report, "Scheduled Report", "Please download attached report",inputStream, report.getCreatedBy());
				
				if(report.getFrequency().equalsIgnoreCase("Weekly")) {
					nextScheduleDate = nextScheduleDate.plus(Period.ofDays(7)); 
				}
				
				if(report.getFrequency().equalsIgnoreCase("Monthly")) {
					nextScheduleDate = nextScheduleDate.plus(Period.ofMonths(1)); 
				}
				
				if(report.getFrequency().equalsIgnoreCase("Quartely")) {
					nextScheduleDate = nextScheduleDate.plus(Period.ofMonths(3)); 
				}
				
				report.setStartDate(nextScheduleDate.atTime(0,0));
				
				scheduleReportRepository.save(report);
				
			}
		}
		
		
	}
	
	private Double getDouble(Double val) {
		return Math.round(val * 100D) / 100D;
	}
	
	public ByteArrayOutputStream generateExcelReport(String keyword, String staff, Date date1, Date date2, Long companyId)
			throws Exception {
		PaginationRequestDTO dto = null;
		Map<String, Object> properties = new HashMap<String, Object>();
		
		//val companyUser = userRepository.getOne(companyId);
		val companyUser = userRepository.findByUserId(companyId);
		
		log.info("step1..");
		
		// start Main Report Grand Totals
		ReportStatResponse statResponse = reportsRepository.getReportsStat(dto, date1, date2, staff, keyword, true, companyId);
		
		if(statResponse != null) {
			
			statResponse.setGrandTotalPost(statResponse.getTotalPostFb() + statResponse.getTotalPostIn() + statResponse.getTotalPostTw());
			statResponse.setGrandTotalInteractions(statResponse.getTotalInteractionsFb() + statResponse.getTotalInteractionsIn() + statResponse.getTotalInteractionsTw());
			Double effectivenessRate = Double.valueOf(statResponse.getGrandTotalInteractions()) / Double.valueOf(statResponse.getGrandTotalPost());
			//effectivenessRate = effectivenessRate / 10;
			
			statResponse.setGrandEffectivenessRate(getDouble(effectivenessRate));
		}
		
		properties.put("grandTotalPost", statResponse.getGrandTotalPost());
		properties.put("grandTotalInteractions", statResponse.getGrandTotalInteractions());
		properties.put("grandEffectivenessRate", statResponse.getGrandEffectivenessRate());
		
		// End here
		
		log.info("step2..");
		
		//Start Main report filling data
		Integer totalMemberyoutubeView = 0;
		List<Reports> memberList = reportsService.fetchReports(dto, date1, date2, staff, keyword, true, false, true, companyId);
		List<Reports> totalSocialMediaMemberList = reportsService.fetchReports(dto, date1, date2, staff, "-1", true, false, true, companyId);
		Map<String, ReportExportDTO> memberMap = new HashMap<String, ReportExportDTO>();
		Map<String, ReportExportDTO> totalSocialMediaMemberMap = new HashMap<String, ReportExportDTO>();
		Map<String, ReportExportDTO> teamMap = new HashMap<String, ReportExportDTO>();
		
		for(Reports report : totalSocialMediaMemberList) {
			ReportExportDTO rObj = new ReportExportDTO();
			rObj.setTotalSocialMediaPost(report.getFbFeedCount()+report.getInFeedCount()+report.getTwFeedCount()+report.getYtFeedCount());
			totalSocialMediaMemberMap.put(report.getName(), rObj);		
		}
		
		log.info("step3..");
		
		for(Reports report : memberList) {
			ReportExportDTO rObj = new ReportExportDTO();
			rObj.setTotalPost(report.getFbFeedCount()+report.getInFeedCount()+report.getTwFeedCount()+report.getYtFeedCount());
			
			Integer fbInteractions = report.getFbLikeCount()+report.getFbShareCount()+report.getFbCommentCount();
			Integer inInteractions = report.getInLikeCount()+report.getInCommentCount();
			Integer twInteractions = report.getTwFavCount()+report.getTwReTweetCount();
			
			rObj.setTotalInteractions(fbInteractions+inInteractions+twInteractions);
			Double effectivenessRate = Double.valueOf(rObj.getTotalInteractions()) / Double.valueOf(rObj.getTotalPost());
			//effectivenessRate = effectivenessRate / 10;
			rObj.setTotalEffectivenessRate(effectivenessRate);
			rObj.setMemberName(report.getName());
			rObj.setTeamName(report.getTeamName());
			rObj.setTotalYtViews(report.getYtViewCount());
			totalMemberyoutubeView += report.getYtViewCount() == null ? 0 : report.getYtViewCount();
			
			if (totalSocialMediaMemberMap != null) {
				ReportExportDTO totalSocialMediaPostObj = totalSocialMediaMemberMap.get(report.getName());
				
				if(totalSocialMediaPostObj != null) {
					rObj.setTotalSocialMediaPost(totalSocialMediaPostObj.getTotalSocialMediaPost());
					Double postBrandedPercentange = Double.valueOf(rObj.getTotalPost()*100) / Double.valueOf(rObj.getTotalSocialMediaPost());
					rObj.setPostBrandedPercentange(postBrandedPercentange);
				}
			}
			
			rObj.setTournamentsCount(report.getTournamentsCount());
			rObj.setBlogCount(report.getBlogCount());
			rObj.setEventCount(report.getEventCount());
			rObj.setLeadsCount(report.getLeadsCount());
			rObj.setPrintCount(report.getPrintCount());
			rObj.setTotalActivity(report.getTotalActivity());
			
			memberMap.put(report.getName(), rObj);			
		}
		properties.put("youtubeView", totalMemberyoutubeView);
		
		log.info("step4..");
		
		List<ReportExportDTO> resultMemberList = memberMap.values().stream().collect(Collectors.toList());
		
		for(Reports report : memberList) {
			if(StringUtils.isNotBlank(report.getTeamName())) {
				
				ReportExportDTO teamObj = teamMap.get(report.getTeamName());
				Integer totalPost = 0, totalInteractions = 0, totalYtViews = 0;
				Double effectivenessRate1 = 0d;
				if(teamObj != null) {
					totalPost += teamObj.getTotalPost() + report.getFbFeedCount()+report.getInFeedCount()+report.getTwFeedCount()+report.getYtFeedCount();
					
					Integer fbInteractions = report.getFbLikeCount()+report.getFbShareCount()+report.getFbCommentCount();
					Integer inInteractions = report.getInLikeCount()+report.getInCommentCount();
					Integer twInteractions = report.getTwFavCount()+report.getTwReTweetCount();
					
					totalInteractions += teamObj.getTotalInteractions() + fbInteractions + inInteractions + twInteractions;					
					totalYtViews += teamObj.getTotalYtViews();
					
					effectivenessRate1 = Double.valueOf(totalInteractions) / Double.valueOf(totalPost);
					//effectivenessRate1 = effectivenessRate1 / 10;
					
					teamObj.setTotalPost(totalPost);
					teamObj.setTotalInteractions(totalInteractions);
					teamObj.setTotalEffectivenessRate(effectivenessRate1);
					
					if (totalSocialMediaMemberMap != null) {
						ReportExportDTO totalSocialMediaPostObj = totalSocialMediaMemberMap.get(report.getName());
						
						if(totalSocialMediaPostObj != null) {
							teamObj.setTotalSocialMediaPost(teamObj.getTotalSocialMediaPost() + totalSocialMediaPostObj.getTotalSocialMediaPost());
							Double postBrandedPercentange = Double.valueOf(teamObj.getTotalPost()*100) / Double.valueOf(teamObj.getTotalSocialMediaPost());
							teamObj.setPostBrandedPercentange(postBrandedPercentange);
						}
					}
					
					teamMap.put(report.getTeamName(), teamObj);					
				}else {
					ReportExportDTO rObj = new ReportExportDTO();
					rObj.setTotalPost(report.getFbFeedCount()+report.getInFeedCount()+report.getTwFeedCount()+report.getYtFeedCount());
					
					Integer fbInteractions = report.getFbLikeCount()+report.getFbShareCount()+report.getFbCommentCount();
					Integer inInteractions = report.getInLikeCount()+report.getInCommentCount();
					Integer twInteractions = report.getTwFavCount()+report.getTwReTweetCount();
					
					rObj.setTotalInteractions(fbInteractions+inInteractions+twInteractions);
					Double effectivenessRate = Double.valueOf(rObj.getTotalInteractions()) / Double.valueOf(rObj.getTotalPost());
					//effectivenessRate = effectivenessRate / 10;
					rObj.setTotalEffectivenessRate(effectivenessRate);
					rObj.setTeamName(report.getTeamName());
					rObj.setTotalYtViews(report.getYtViewCount());
					
					if (totalSocialMediaMemberMap != null) {
						ReportExportDTO totalSocialMediaPostObj = totalSocialMediaMemberMap.get(report.getName());
						
						if(totalSocialMediaPostObj != null) {
							rObj.setTotalSocialMediaPost(totalSocialMediaPostObj.getTotalSocialMediaPost());
							Double postBrandedPercentange = Double.valueOf(rObj.getTotalPost()*100) / Double.valueOf(rObj.getTotalSocialMediaPost());
							rObj.setPostBrandedPercentange(postBrandedPercentange);
						}
					}
					
					teamMap.put(report.getTeamName(), rObj);
				}								
			}
		}
		// End here 
		
		log.info("step5..");
		
		// Start Team data
		List<HashMap<String, Object>> subReportData1 = new ArrayList<HashMap<String, Object>>();
		List<ReportExportDTO> resultTeamList = teamMap.values().stream().collect(Collectors.toList());
		for(ReportExportDTO exportDto : resultTeamList) {
			HashMap<String, Object> row = new HashMap<String, Object>();
			row.put("teamName", exportDto.getTeamName());
			row.put("totalPost",exportDto.getTotalPost());
			row.put("totalInteractions",exportDto.getTotalInteractions());
			row.put("totalEffectivenessRate",exportDto.getTotalEffectivenessRate());
			row.put("totalYtViews",exportDto.getTotalYtViews());
			subReportData1.add(row);
		}
		// End here
		
		log.info("step6..");
		
		List<String> keywordsList = new ArrayList<String>();
		
//Fix for keyword filter in report		
		if( StringUtils.isAllBlank(keyword ) )  {
			val keywordsFBUser = companyUser.getFbKeywords() == null ? "" : companyUser.getFbKeywords(); 
			val keywordsINUser = companyUser.getInstaKeywords() == null ? "" : companyUser.getInstaKeywords();
			val keywordsTWUser = companyUser.getTwtKeywords() == null ? "" : companyUser.getTwtKeywords();
			val keywordsYTUser = companyUser.getYoutubeKeywords() == null ? "" : companyUser.getYoutubeKeywords();
		
	
			if(keywordsFBUser != null) {
				keywordsList.addAll(Arrays.asList( keywordsFBUser.split(",")));
			}
			if(keywordsINUser != null) {
				keywordsList.addAll(Arrays.asList( keywordsINUser.split(",")));
			}
			if(keywordsTWUser != null) {
				keywordsList.addAll(Arrays.asList( keywordsTWUser.split(",")));
			}
			if(keywordsYTUser != null) {
				keywordsList.addAll(Arrays.asList( keywordsYTUser.split(",")));
			}
		
		
		} else {
			String tmp = keyword.replace("|", ",");
			keywordsList.addAll(Arrays.asList( tmp.split(",")));
		}
		List<String> keywordArrayList = keywordsList.stream()
			     .distinct()
			     .collect(Collectors.toList());
		log.info("step7..");
		
		List<ReportExportDTO> resultKeywordList = reportsRepository.fetchReportsByKeyWords(date1, date2, staff, keywordArrayList, 
				statResponse.getGrandTotalPost(), companyId);
	
		Collections.sort(resultKeywordList, new Comparator<ReportExportDTO>() {
			@Override
	        public int compare(ReportExportDTO o1, ReportExportDTO o2) {
	            return o2.getTotalPost().compareTo(o1.getTotalPost());
	        }
	    });
		
		if(resultKeywordList != null && resultKeywordList.size() > 0) {
			properties.put("mostUsedKeyword", resultKeywordList.get(0).getKeyword());
		}else {
			properties.put("mostUsedKeyword", "");
		}
		log.info("step8..");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String date1Str = sdf.format(date1);
		String date2Str = sdf.format(date2);
		
		properties.put("dateRangeText", "Report generated date between "+date1Str+" to "+date2Str);
		
		ByteArrayOutputStream out = jasperUtils.generateExcel("Reports", properties, resultMemberList, resultTeamList, resultKeywordList);
		return out;
	}

	private void sendScheduleMail(String from1, ScheduleReport report, String subject1, String text, InputStream inputStream, Long userId) {
		
		
		String[] activeProfiles = environment.getActiveProfiles();  
    	boolean result = Arrays.stream(activeProfiles).anyMatch("sportsman"::equals);
    	
    	if(result) {
    		from1 = "no-reply@ambassadortrack.net";
    	}
		
		Email from = new Email(from1);
		//Email to = new Email(to1);
		Content content = new Content("text/html", text);
		
		Personalization personalization = new Personalization();
		
		//Mail mail = new Mail(from, subject1, to, content);
		
		Mail mail = new Mail();
		mail.setFrom(from);
		mail.addContent(content);
		
		personalization.setSubject(subject1);
		
		if(StringUtils.isNotBlank(report.getRecipient1())) {
			Email to1 = new Email(report.getRecipient1());
			personalization.addTo(to1);
			mail.addPersonalization(personalization);
		}
		
		if(StringUtils.isNotBlank(report.getRecipient2())) {
			Email to2 = new Email(report.getRecipient2());
			personalization.addTo(to2);
			mail.addPersonalization(personalization);
		}
		
		if(StringUtils.isNotBlank(report.getRecipient3())) {
			Email to3 = new Email(report.getRecipient3());
			personalization.addTo(to3);
			mail.addPersonalization(personalization);
		}
		
		if(StringUtils.isNotBlank(report.getRecipient4())) {
			Email to4 = new Email(report.getRecipient4());
			personalization.addTo(to4);
			mail.addPersonalization(personalization);
		}
		
		if(StringUtils.isNotBlank(report.getRecipient5())) {
			Email to5 = new Email(report.getRecipient5());
			personalization.addTo(to5);
			mail.addPersonalization(personalization);
		}
		
		//Email bcc = new Email("debugmycode80@gmail.com");
		//personalization.addBcc(bcc);
		
		byte[] filedata = null;
		try {
			filedata = org.apache.commons.io.IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Base64 x = new Base64();
		
		String imageDataString = x.encodeAsString(filedata);
		Attachments attachments3 = new Attachments();
		attachments3.setContent(imageDataString);
		attachments3.setType("application/vnd.ms-excel");
		attachments3.setFilename("attachment.xls");
		attachments3.setDisposition("attachment");
		attachments3.setContentId("Banner");
		mail.addAttachments(attachments3);
		
		
		SendGrid sg = new SendGrid(SENDGRID_API_KEY);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			/*
			 * System.out.println(response); System.out.println(response.getStatusCode());
			 * System.out.println(response.getBody());
			 * System.out.println(response.getHeaders());
			 */
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void generateExcel(OutputStream out, List<Reports> reportList) throws Exception {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Schedule Report");

		getSheet1TopHeaderRow(workbook, sheet, reportList);

		workbook.write(out);
	}
	
	private int counter = -1;

	private int getCount() {
		return ++counter;
	}
	
	private HSSFFont getFont(HSSFWorkbook workbook, short color) {
		HSSFFont font = workbook.createFont();
		font.setColor(color); 
		font.setBold(true);
		font.setFontHeightInPoints(((short)(font.getFontHeightInPoints() - 2))) ;
		return font;
	}
	
	private void setBackGround(HSSFCellStyle cellStyle, short color) {
		cellStyle.setFillBackgroundColor(color);
	    cellStyle.setFillForegroundColor(color);
	    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    
	    cellStyle.setAlignment(HorizontalAlignment.CENTER);
	    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); 
	} 
	
	private HSSFCell addCell(HSSFRow row, HSSFCellStyle cellStyle, String value) {
		HSSFCell cell = row.createCell( ((int)row.getLastCellNum() < 0) ? 0 : (int)row.getLastCellNum() ); 
		cell.setCellStyle(cellStyle); 
	    cell.setCellValue(new HSSFRichTextString(value)); 
	    cell.setCellType(CellType.STRING);
	    
	    return cell;
	} 
	
	public void setColor(HSSFWorkbook workbook, HSSFCellStyle cellStyle, byte r, byte g, byte b) {
		
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor hssfColor = null;
		 
		hssfColor= palette.findColor(r, g, b); 
		if (hssfColor == null ) {
		    palette.setColorAtIndex(IndexedColors.LAVENDER.getIndex(), r, g, b);
		    hssfColor = palette.getColor(IndexedColors.LAVENDER.getIndex());
		} 
		
		cellStyle.setFillForegroundColor(hssfColor.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); 
	}
	
	@SuppressWarnings("deprecation")
	public void getSheet1TopHeaderRow(HSSFWorkbook workbook, HSSFSheet sheet, List<Reports> reportList) {
		HSSFRow headerTopRow = sheet.createRow(getCount());
		
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		
		headerTopRow.setHeight((short) 400);
		 
		cellStyle1.setBorderLeft(BorderStyle.MEDIUM);
		cellStyle1.setLeftBorderColor(IndexedColors.BLACK.getIndex()); 
	    cellStyle1.setBorderRight(BorderStyle.MEDIUM);
	    cellStyle1.setRightBorderColor(IndexedColors.BLACK.getIndex()); 
	    cellStyle1.setBorderTop(BorderStyle.MEDIUM);
	    cellStyle1.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    
	    HSSFCellStyle cellMiddleStyle = workbook.createCellStyle();
		cellMiddleStyle.setFont(getFont(workbook,IndexedColors.WHITE.getIndex()));
		setBackGround(cellMiddleStyle, IndexedColors.LIGHT_BLUE.getIndex()); 
		
		cellMiddleStyle.setBorderTop(BorderStyle.MEDIUM);
		cellMiddleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    cellMiddleStyle.setBorderLeft(BorderStyle.MEDIUM); 
	    cellMiddleStyle.setBorderRight(BorderStyle.MEDIUM);
	    cellMiddleStyle.setBorderBottom(BorderStyle.MEDIUM);
	    cellMiddleStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); 
	    
	  //Setup Headers
	    HSSFCellStyle columnHeader = workbook.createCellStyle();
	    columnHeader.setFont(getFont(workbook,IndexedColors.WHITE.getIndex()));
	    setColor(workbook,  columnHeader, (byte) 255, (byte) 190, (byte) 124); //light orange   
	    columnHeader.setBorderLeft(BorderStyle.MEDIUM);
	    columnHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex()); 	    
	    columnHeader.setBorderTop(BorderStyle.MEDIUM);
	    columnHeader.setTopBorderColor(IndexedColors.BLACK.getIndex()); 	    
	    columnHeader.setBorderBottom(BorderStyle.MEDIUM);
	    columnHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex()); 	    
	    columnHeader.setBorderRight(BorderStyle.MEDIUM);
	    columnHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    columnHeader.setAlignment(HorizontalAlignment.CENTER);
	    columnHeader.setVerticalAlignment(VerticalAlignment.CENTER); 
	    
	    //First Line 
	   
	    addCell(headerTopRow, cellStyle1, "Schedule Report");
	    
	    headerTopRow = sheet.createRow(getCount());
	    
	    addCell(headerTopRow, columnHeader, " Name ");
	    addCell(headerTopRow, columnHeader, " Facebook ");
	    addCell(headerTopRow, columnHeader, " Twitter ");
	    addCell(headerTopRow, columnHeader, "Instagram");
	    addCell(headerTopRow, columnHeader, " Youtube ");
	    addCell(headerTopRow, columnHeader, " Tournament ");
	    addCell(headerTopRow, columnHeader, " Entries ");
	    addCell(headerTopRow, columnHeader, " Events ");
	    addCell(headerTopRow, columnHeader, " Leads");
	    addCell(headerTopRow, columnHeader, " Print ");
	    addCell(headerTopRow, columnHeader, "AT Score ");
	    
		for (Reports d : reportList) {
			headerTopRow = sheet.createRow(getCount());
			
			addCell(headerTopRow, cellStyle1, " " + d.getName());
			addCell(headerTopRow, cellStyle1, " " + d.getFbFeedCount());
			addCell(headerTopRow, cellStyle1, " " + d.getTwFeedCount());
			addCell(headerTopRow, cellStyle1, " " + d.getInFeedCount());
			addCell(headerTopRow, cellStyle1, " " + d.getYtFeedCount());
			addCell(headerTopRow, cellStyle1, " " + d.getTournamentsCount());
			addCell(headerTopRow, cellStyle1, " " + d.getBlogCount());
			addCell(headerTopRow, cellStyle1, " " + d.getEventCount());
			addCell(headerTopRow, cellStyle1, " " + d.getLeadsCount());
			addCell(headerTopRow, cellStyle1, " " + d.getPrintCount());
			addCell(headerTopRow, cellStyle1, " " + d.getTotalActivity());

		}
		
		sheet.autoSizeColumn((short)0); 
	    sheet.autoSizeColumn((short)1);
	    sheet.autoSizeColumn((short)2); 
	    sheet.autoSizeColumn((short)3);
	    sheet.autoSizeColumn((short)4); 
	    sheet.autoSizeColumn((short)5);
	    sheet.autoSizeColumn((short)6); 
	    sheet.autoSizeColumn((short)7);
	    sheet.autoSizeColumn((short)8); 
	    sheet.autoSizeColumn((short)9);
	    sheet.autoSizeColumn((short)10);
	    
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));
		
	 }

}
