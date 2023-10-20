package com.tracker.services.emails;

import org.springframework.beans.factory.annotation.Autowired;

import com.tracker.services.utils.EnvironmentUtil;

public abstract class EmailTemplate {

	@Autowired
	protected EnvironmentUtil environmentUtil;

	public abstract String formatMessage();	

	public String getBaseURL() {
		if(environmentUtil.isDefault()) {
			return "http://localhost:8080";
		}
		else if(environmentUtil.isDev()) {
			return "http://localhost:8080";
		}
		else if(environmentUtil.isTest()) {
			return "http://localhost:8080";
		}
		else if(environmentUtil.isUnitTest()) {
			return "http://localhost:8080";
		}
		else if(environmentUtil.isStaging()) {
			return "http://localhost:8080";
		}
		else if(environmentUtil.isProd()) {
			return "https://anglertrack.net";
		}

		return "http://localhost:8080";
	} 

	private void footerMessage(StringBuffer sb) {

		/*sb.append("<br><br><br> If you experience any difficulties please contact our support team via helpdesk or email : " +
				"<br> <a target=\"_blank\" href=\"https://anglertrack.freshdesk.com\">Support Request / Knowledge Base</a>" +
				"<br> support@anglertrack.net " +
				"<br><br> Sincerely," +
				"<br>AngerlTrack Customer Care" + 
				"<br><br><br>");
*/
		sb.append("<style>"); 

		sb.append(".footer-region-display-wrapper  {"); 
		sb.append(" cursor: pointer;"); 
		sb.append("display: inline-block;"); 
		sb.append("height: inherit;"); 
		sb.append(" margin-right: 7px;"); 
		sb.append("width: 28px;"); 
		sb.append("color: #383838;"); 
		sb.append("text-decoration: none;"); 
		sb.append("} "); 
		sb.append("a {"); 
		sb.append(" background1: rgba(0, 0, 0, 0) none repeat scroll 0 0;"); 
		sb.append(" font-size: 100%;"); 
		sb.append(" margin: 0;"); 
		sb.append("vertical-align: baseline;"); 
		sb.append("}"); 
		sb.append("</style>"); 

/*
		sb.append("<table style=\"margin-top: 0px; width:100%;\">");
		sb.append("<tr>");
		sb.append("<td width=\"30%\" style=\"font-size:14px; text-align: center; font-family: Roboto,Arial; color: #383838;\">");
		sb.append("27th Ventura Drive, Suite C<br>");
		sb.append("Duluth, GA 30009<br>");
		sb.append("(770)-752-5356 ");
		sb.append("</td>");
		sb.append("<td style=\"vertical-align:middle; text-align: center;\">");
		sb.append("<div style=\"padding: 0px; border-left: 1px solid #4DD6B5; border-right: 1px solid #4DD6B5; font-size:14px; text-align: center; font-family: Roboto,Arial; color: #383838;\">");

		sb.append("<div style=\"padding-bottom: 10px;\">Visit Us Via: </div>");

		sb.append("<a href=\"https://www.facebook.com/M and Mmedtrans\" class=\"footer-region-display-wrapper\">");
		sb.append("		<img src=\"https://www.M and Mmedtrans.com/resources/img/footer-view-black-f.png\"/>");
		sb.append("</a>");
		sb.append("<a href=\"https://twitter.com/M and Mmedtrans\" class=\"footer-region-display-wrapper\">");
		sb.append("		<img src=\"https://www.M and Mmedtrans.com/resources/img/footer-view-black-t.png\"/>");
		sb.append("</a>");
		sb.append("<a href=\"https://www.pinterest.com/M and Mmedtrans/\" class=\"footer-region-display-wrapper\">");
		sb.append("		<img src=\"https://www.M and Mmedtrans.com/resources/img/footer-view-black-p.png\"/>");
		sb.append("</a>");
		sb.append("</div>");*/
		sb.append("</td>");
		sb.append("<td width=\"37%\" style=\"vertical-align:middle; text-align: center;\">");
		sb.append("		<div style=\"padding: 0px; font-size:14px; text-align: center; font-family: Roboto,Arial; color: #383838;\">"); 

		sb.append("<a target=\"_blank\" class=\"ng-binding\" href=\"http://www.anglertrack.net\" style=\"color: #383838;\">");
		sb.append(" www.anglertrack.net");
		sb.append("	 </a>");

		sb.append("<a class=\"footer-region-display-wrapper\" target=\"_blank\" href=\"http://www.anglertrack.net\">");
		sb.append("				<img style=\"vertical-align: bottom; \" src=\"https://www.anglertrack.net/resources/img/footer-view-site-title-arrow.png\"/>");
		sb.append(" </a>");
		sb.append("</div>");
		sb.append("</td>			");			

		sb.append("</tr>");
		sb.append("</table><br><br><br><br>");
	} 

	protected String addStandardHeaderFooters(String body) {

		//String baseUrl = getBaseURL(); //"https://www.coinxoom.com"; 
		//baseUrl = "http://localhost:8080/cso-ui/";

		StringBuffer sb = new StringBuffer();
		footerMessage(sb);	   

		String headerFooter =  "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
				"<html xmlns=\"http://www.w3.org/1999/xhtml\"> <head> <title>Untitled Document</title> <style type=\"text/css\">\n" + 
				"<!-- .bodyText { 	font-family: Arial, Helvetica, sans-serif; 	font-size: 14px; } .darkblue_heading { 	color: #0068A9; 	font-weight: bold; 	font-family: Arial, Helvetica, sans-serif; 	font-size: 18px; } -->\n" + 
				"</style>\n" +
				"</head>\n" +
				"<html> " + 
				"<body style='font-family:Arial, Helvetica, sans-serif; font-size:14px'>\n" +
				"<table width='100' border='0' align='center' cellpadding='0'>\n" +
				"<tr>\n" +
				"<td width='870'>" +

						     	//"<img src=\"" + baseUrl + "/resources/img/email_header.png\" width=\"870\" style='display: block' />\n" +
						     	"<table width='870' cellpadding='0' cellspacing='0' style='border:1px solid #4DD6B5; background-color: #fff;' bordercolor='#4DD6B5'><tr><td>\n" +
						     	"<table width='867' cellpadding='0' cellspacing='0' >\n" +
						     	"<tr>\n" +
						     	"<td width='20'></td><td colspan='2' height='350'>\n" +
						     	"<div style='color:#333333; font-fsby:Arial, Helvetica, sans-serif; font-size:15px; '>" +
						     	body + sb.toString() +
						     	"</div>" +
						     	"</td><td width='20'></td>\n" +
						     	"</tr>\n" +
						     	"</table>\n" +
						     	"</td></tr></table>\n" +
						     	//"<img src=\"" + baseUrl + "/resources/img/email_footer.png\" width=\"870\" style='display: block' />\n" +

						   	 "</td>\n" +
						   	 "</tr>\n" +				   	 
						   	 "</table\n>" +	    
						   	 "</body>\n" +
						   	 "</html>\n"; 	 
		return headerFooter;

	}

}
