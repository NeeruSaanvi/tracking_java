package com.tracker.services.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.tracker.commons.dtos.Application;
import com.tracker.commons.dtos.ReportExportDTO;
import com.tracker.commons.dtos.SportsUserProAppDetails;
import com.tracker.commons.models.Lead;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JasperUtils {
	
	private NumberFormat numberFormat = NumberFormat.getInstance();
	
	public String generateSportsManHtml(SportsUserProAppDetails application) {
		
		String HTML_TEMPLATE ="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> " + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\"> " + 
				"<head> " + 
				"<title>pdf</title> " + 
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> " + 
				"</head> " + 
				"<body> " + 
				" " + 
				" <div style=\"background-color:#F6F6F6;\"> " + 
				" 		 <div class=\"col-sm-12\"> " + 
				"           <table width=\"100%\"> " + 
				"             <thead> " + 
				"             <tr style=\"background-color:#D7D7D7;\"> " + 
				"               <th colspan=\"2\" align=\"left\"><i class=\"fa fa-user\"></i> Personal Information</th> " + 
				"             </tr> " + 
				"             </thead> " + 
				"             <tbody> " + 
				"               <tr> " + 
				"                 <td class=\"left\" width=\"40%\"> " + 
				"                   Full Name " + 
				"                 </td> " + 
				"                 <td class=\"right\" width=\"60%\"> " + 
				"                   "+application.getFirstLastName()+" " + 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Date of Birth " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   " +application.getDob()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Highest Education " + 
				"                 </td> " + 
				"                 <td class=\"right\" valign=\"top\"> " + 
				"                   "+application.getHighestEducation()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\" valign=\"top\"> " + 
				"                   Home Address " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getStreet()+"<br/>"+application.getCity()+
				"                   "+application.getState()+"<br/>"+application.getZipcode()+
				"                 </td> " + 
				"               </tr> " + 
				
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Email " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getEmail()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Phone " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getPhone()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Emergency Contact " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getSec_phone()+ 
				"                 </td> " + 
				"               </tr> " + 
				
				"             </tbody> " + 
				"           </table> " + 
				"         </div> " + 
				"         <div class=\"col-sm-12\"> " + 
				"           <table width=\"100%\"> " + 
				"             <thead> " + 
				"               <tr style=\"background-color:#D7D7D7;\"> " + 
				"                 <th colspan=\"2\" align=\"left\"><i class=\"fa fa-briefcase\"></i> Promo Gear</th> " + 
				"               </tr> " + 
				"             </thead> " + 
				"             <tbody> " + 
				"               <tr> " + 
				"                 <td class=\"left\" width=\"40%\"> " + 
				"                   Shirt Size                 </td> " + 
				"                 <td class=\"right\" width=\"60%\"> " + 
				"                  "+application.getShirt_size()+
				"				 </td> " + 
				"               </tr> " + 
				
				"             </tbody> " + 
				"           </table> " + 
				"         </div> " + 
				
				"</div>		 " + 
				"</body> " + 
				"</html>";
		return HTML_TEMPLATE;
	}
	
	public String generateHtml(Application application) {
		
		String HTML_TEMPLATE ="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> " + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\"> " + 
				"<head> " + 
				"<title>pdf</title> " + 
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> " + 
				"</head> " + 
				"<body> " + 
				" " + 
				" <div style=\"background-color:#F6F6F6;\"> " + 
				" 		 <div class=\"col-sm-12\"> " + 
				"           <table width=\"100%\"> " + 
				"             <thead> " + 
				"             <tr style=\"background-color:#D7D7D7;\"> " + 
				"               <th colspan=\"2\" align=\"left\"><i class=\"fa fa-user\"></i> Personal Information</th> " + 
				"             </tr> " + 
				"             </thead> " + 
				"             <tbody> " + 
				"               <tr> " + 
				"                 <td class=\"left\" width=\"40%\"> " + 
				"                   Full Name " + 
				"                 </td> " + 
				"                 <td class=\"right\" width=\"60%\"> " + 
				"                   "+application.getFirstLastName()+" " + 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Date of Birth " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   " +application.getDob()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Highest Education " + 
				"                 </td> " + 
				"                 <td class=\"right\" valign=\"top\"> " + 
				"                   "+application.getHighestEducation()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\" valign=\"top\"> " + 
				"                   Home Address " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getStreet()+"<br/>"+application.getCity()+
				"                   "+application.getState()+"<br/>"+application.getZipcode()+
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\" valign=\"top\"> " + 
				"                   Shipping Address " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getShip_street()+"<br/>"+application.getShip_city()+
				"                   "+application.getShip_state()+"<br/>"+application.getShip_zipcode()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Email " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getEmail()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Phone " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getPhone()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Emergency Contact " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getSec_phone()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\" valign=\"top\"> " + 
				"                   3 References outside of the fishing Industry " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getReferences_1()+"<br/>"+application.getReferences_2()+"<br/>"+application.getReferences_3()+ 
				"                 </td> " + 
				"               </tr> " + 
				"             </tbody> " + 
				"           </table> " + 
				"         </div> " + 
				"         <div class=\"col-sm-12\"> " + 
				"           <table width=\"100%\"> " + 
				"             <thead> " + 
				"               <tr style=\"background-color:#D7D7D7;\"> " + 
				"                 <th colspan=\"2\" align=\"left\"><i class=\"fa fa-briefcase\"></i> Promo Gear</th> " + 
				"               </tr> " + 
				"             </thead> " + 
				"             <tbody> " + 
				"               <tr> " + 
				"                 <td class=\"left\" width=\"40%\"> " + 
				"                   Shirt Size                 </td> " + 
				"                 <td class=\"right\" width=\"60%\"> " + 
				"                  "+application.getShirt_size()+
				"				 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\">Pant Size</td> " + 
				"                 <td class=\"right\">"+application.getPant_size()+"</td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Preferred Head Wear?                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getPrefer_hardware()+"</td> " + 
				"               </tr> " + 
				"             </tbody> " + 
				"           </table> " + 
				"         </div> " + 
				"         <div class=\"col-sm-12\"> " + 
				"           <table width=\"100%\"> " + 
				"             <thead> " + 
				"               <tr style=\"background-color:#D7D7D7;\"> " + 
				"                 <th colspan=\"2\" align=\"left\"><i class=\"fa fa-tint\"></i> Fishing Information</th> " + 
				"               </tr> " + 
				"             </thead> " + 
				"             <tbody> " + 
				"               <tr> " + 
				"                 <td class=\"left\" width=\"40%\"> " + 
				"                   Home Body of Water " + 
				"                 </td> " + 
				"                 <td class=\"right\" width=\"60%\"> " + 
				"				 " +application.getHome_lake_river()+ 
				"				    " + 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Most Targeted Species " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                  "+application.getSpecies_type()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   How Many Tournaments do you Fish Every Year? " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getTournament_fish_year()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Do you Fish any Tournament Trail? " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getIsfish_tournament_trails()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Do you Fish Salt Water or Fresh Water? " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getWater_type()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Are you a Guide? " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                  "+application.getIsguide()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Do you have Captain License " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getIsguidelicence()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   License Number " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getLicense_number()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   How Many days a year do you Guide? " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getGuideyear()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Website for Yourself or guide Service " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getIswebsite()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\" valign=\"top\"> " + 
				"                   Top Moments of Your Fishing Career? " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"					"+application.getFlyfishingcareer_1()+"<br/>"+application.getFlyfishingcareer_2()+
				"                 </td> " + 
				"               </tr> " + 
				"             </tbody> " + 
				"           </table> " + 
				"         </div> " + 
				"         <div class=\"col-sm-12\"> " + 
				"           <table width=\"100%\"> " + 
				"             <thead> " + 
				"               <tr style=\"background-color:#D7D7D7;\"> " + 
				"                 <th colspan=\"2\" align=\"left\"><i class=\"fa fa-anchor\"></i> Product Experience</th> " + 
				"               </tr> " + 
				"             </thead> " + 
				"             <tbody> " + 
				"               <tr> " + 
				"                 <td class=\"left\" width=\"40%\"> " + 
				"                   Have You used our Products? " + 
				"                 </td> " + 
				"                 <td class=\"right\" width=\"60%\"> " + 
				"                   "+application.getIsusedproducts()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Which Product do you use Most? " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getUseproducts()+ 
				"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Please Share Your Experience with our Products? " + 
				"                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getExperiencewithproducts()+
				"                 </td> " + 
				"               </tr> " + 
				"             </tbody> " + 
				"           </table> " + 
				"         </div> " + 
				"         <div class=\"col-sm-12\"> " + 
				"           <table width=\"100%\"> " + 
				"             <thead> " + 
				"               <tr style=\"background-color:#D7D7D7;\"> " + 
				"                 <th colspan=\"2\" align=\"left\"><i class=\"fa fa-share-square\"></i> Social Media / Web Activity</th> " + 
				"               </tr> " + 
				"             </thead> " + 
				"             <tbody> " + 
				"               <tr> " + 
				"                 <td class=\"left\" width=\"40%\"> " + 
				"                   Social Media Platform Used                 </td> " + 
				"                 <td class=\"right\" width=\"60%\"> " + 
				"                   "+application.getSocial_media_platform()+"</td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Facebook Fans (Personal)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getFacebook_personal_page()+"    </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Facebook Link (Personal)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                  "+application.getLink_to_facebook_personal_page()+"</td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Facebook Fans (Fan/Guide)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getLink_to_facebook_fan_page()+" </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Twitter Followers (Personal)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getTwitter_personal_page()+"</td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Twitter Link (Personal)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getLink_to_twitter_personal_page()+"</td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Twitter Followers (Fan/Guide)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getLink_to_twitter_fan_page()+"  </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Twitter Link (Fan/Guide)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getLink_to_twitter_fan_page()+"</td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Instagram Followers (Personal)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getLink_to_instagram_personal_page()+" </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Instagram Link(Personal)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getInstagram_fan_page()+"        </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Instagram Followers (Fan/Guide)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getInstagram_fan_page()+"        </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Instagram Link (Fan/Guide)                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getInstagram_fan_page()+"        </td> " +
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Fishing Blogs used                 </td> " + 
				"                 <td class=\"right\">"+
				"				"+application.getActive_on_fishing_blog_1()+"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Do ever video Fishing Trips                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                                   </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Links to you Fishing Videos                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                                   </td> " + 
				"               </tr> " + 
				"             </tbody> " + 
				"           </table> " + 
				"         </div> " + 
				"         <div class=\"col-sm-12\"> " + 
				"           <table width=\"100%\"> " + 
				"             <thead> " + 
				"               <tr style=\"background-color:#D7D7D7;\"> " + 
				"                 <th colspan=\"2\" align=\"left\"> " + 
				"                   <i class=\"fa fa-info-circle\"></i> Additional Information                 </th> " + 
				"               </tr> " + 
				"             </thead> " + 
				"             <tbody> " + 
				"               <tr> " + 
				"                 <td class=\"left\" width=\"40%\" valign=\"top\"> " + 
				"                   Other Sponsors                 </td> " + 
				"                 <td class=\"right\" width=\"60%\"> " + 
				"				 	"+application.getOther_sposors_1()+"<br/> "+application.getOther_sposors_2()+
				"				 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Why should we select you our pro-Staff?                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getSelect_fishing_pro_staff()+"</td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Other information you would like to share with us?                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getOther_information()+"</td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Resume                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getAttach_resume()+"                 </td> " + 
				"               </tr> " + 
				"               <tr> " + 
				"                 <td class=\"left\"> " + 
				"                   Pictures                 </td> " + 
				"                 <td class=\"right\"> " + 
				"                   "+application.getUpload_pictures()+"</td> " + 
				"               </tr> " + 
				"             </tbody> " + 
				"           </table> " + 
				"		</div> " + 
				"</div>		 " + 
				"</body> " + 
				"</html>";
		return HTML_TEMPLATE;
	}
	
	public ByteArrayOutputStream generateApplicationViewPDF(Application application, String sportsman, SportsUserProAppDetails applicationSport) {
		
		try {
			
			String html = "";
			
			if(StringUtils.isNotBlank(sportsman) && sportsman.equalsIgnoreCase("sportsman")) {
				html = generateSportsManHtml(applicationSport);
			}else {
				html = generateHtml(application);
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			document.open();
			InputStream is = new ByteArrayInputStream(html.getBytes());
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			document.close();
			//log.info("b"+baos.toByteArray());
			return baos;
	      }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public ByteArrayOutputStream generateLeadExcel(String sheetName, Map param, List leadData) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		log.debug("Generating reprot excel");
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);
		
		counter = -1;

		getLeadSheetInformationRow(workbook, sheet, param, leadData);

		workbook.write(out);
		out.close();
		return out;
	}
	
	@SuppressWarnings("rawtypes")
	public ByteArrayOutputStream generateExcel(String sheetName, Map param, List memberData, List teamData, List keywordData) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		log.debug("Generating reprot excel");
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);
		
		counter = -1;

		getSheetInformationRow(workbook, sheet, param, memberData, teamData, keywordData);

		workbook.write(out);
		out.close();
		return out;
	}

	private int counter = -1;

	private int getCount() {
		return ++counter;
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
	
	private Double getDouble(Double val) {
		return Math.round(val * 10D) / 10.0D;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getLeadSheetInformationRow(HSSFWorkbook workbook, HSSFSheet sheet, Map params, List leadData) {
		HSSFRow headerTopRow = sheet.createRow(getCount());
		
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		headerTopRow.setHeight((short) 400);
	    
	    HSSFCellStyle cellMiddleStyle = workbook.createCellStyle();
		cellMiddleStyle.setFont(getFont(workbook,IndexedColors.WHITE.getIndex()));
		setBackGround(cellMiddleStyle, IndexedColors.LIGHT_BLUE.getIndex());		
		cellMiddleStyle.setBorderTop(BorderStyle.MEDIUM);
		cellMiddleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    cellMiddleStyle.setBorderLeft(BorderStyle.MEDIUM); 
	    cellMiddleStyle.setBorderRight(BorderStyle.MEDIUM);
	    cellMiddleStyle.setBorderBottom(BorderStyle.MEDIUM);
	    cellMiddleStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); 
	    
	    HSSFCellStyle columnHeaderName = workbook.createCellStyle();
	    columnHeaderName.setFont(getFont(workbook,IndexedColors.WHITE.getIndex()));
	    setBackGround(columnHeaderName, IndexedColors.BLUE.getIndex()); 
	    columnHeaderName.setAlignment(HorizontalAlignment.LEFT);
	    
	    int temp1;
	    
	    //Setup Headers
	    HSSFCellStyle columnHeader = workbook.createCellStyle();
	    columnHeader.setFont(getFont(workbook,IndexedColors.BLACK.getIndex()));
	    columnHeader.setAlignment(HorizontalAlignment.LEFT);
	    columnHeader.setVerticalAlignment(VerticalAlignment.CENTER); 
	    
	    
	    headerTopRow = sheet.createRow(getCount());	    
	    addCell(headerTopRow, columnHeader, " Date ");
	    addCell(headerTopRow, columnHeader, " Pro Staff Name ");
	    addCell(headerTopRow, columnHeader, " Lead Name ");
	    addCell(headerTopRow, columnHeader, " Lead Email ");
	    addCell(headerTopRow, columnHeader, " Lead Phone ");
	    addCell(headerTopRow, columnHeader, " Lead Address ");
	    addCell(headerTopRow, columnHeader, " Notes given by pro staff ");
		
	    if(leadData != null && leadData.size() > 0) {
	    	List<Lead> data = leadData;
	    	
	    	for (Lead r : data) {
				headerTopRow = sheet.createRow(getCount());

				addCell(headerTopRow, cellStyle1, " " + r.getPostedDateFormatter());
				addCell(headerTopRow, cellStyle1, "" + r.getFirstLastname());
				addCell(headerTopRow, cellStyle1, "" + r.getLeadDealName());
				addCell(headerTopRow, cellStyle1, "" + r.getLeadBuyEmail());
				addCell(headerTopRow, cellStyle1, "" + r.getLeadDelPhone());
				addCell(headerTopRow, cellStyle1, ""+ r.getLeadDelAdd());
				addCell(headerTopRow, cellStyle1, "" + r.getLeadRel());
			}
	    }
	    
	    
	    
		sheet.autoSizeColumn((short)0); 
	    sheet.autoSizeColumn((short)1);
	    sheet.autoSizeColumn((short)2); 
	    sheet.autoSizeColumn((short)3);
	    sheet.autoSizeColumn((short)4); 
	    sheet.autoSizeColumn((short)5);
	    sheet.autoSizeColumn((short)6);
	    
		
	 } 

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSheetInformationRow(HSSFWorkbook workbook, HSSFSheet sheet, Map params, List memberData, List teamData, List keywordData) {
		HSSFRow headerTopRow = sheet.createRow(getCount());
		
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		//cellStyle1.setFont(getFont(workbook,HSSFColor.WHITE.index));
		//setBackGround(cellStyle1, HSSFColor.AUTOMATIC.index);  
		
		headerTopRow.setHeight((short) 400);
	    
	    HSSFCellStyle cellMiddleStyle = workbook.createCellStyle();
		cellMiddleStyle.setFont(getFont(workbook,IndexedColors.WHITE.getIndex()));
		setBackGround(cellMiddleStyle, IndexedColors.LIGHT_BLUE.getIndex());		
		cellMiddleStyle.setBorderTop(BorderStyle.MEDIUM);
		cellMiddleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    cellMiddleStyle.setBorderLeft(BorderStyle.MEDIUM); 
	    cellMiddleStyle.setBorderRight(BorderStyle.MEDIUM);
	    cellMiddleStyle.setBorderBottom(BorderStyle.MEDIUM);
	    cellMiddleStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); 
	    
	    HSSFCellStyle columnHeaderName = workbook.createCellStyle();
	    columnHeaderName.setFont(getFont(workbook,IndexedColors.WHITE.getIndex()));
	    setBackGround(columnHeaderName, IndexedColors.BLUE.getIndex()); 
	    columnHeaderName.setAlignment(HorizontalAlignment.LEFT );
	    
	    int temp1;
	    
	    //Setup Headers
	    HSSFCellStyle columnHeader = workbook.createCellStyle();
	    columnHeader.setFont(getFont(workbook,IndexedColors.BLACK.getIndex()));
	    columnHeader.setAlignment(HorizontalAlignment.CENTER);
	    columnHeader.setVerticalAlignment(VerticalAlignment.CENTER); 
	    
	    addCell(headerTopRow, columnHeaderName, ""+params.get("dateRangeText"));
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
	    
	    headerTopRow = sheet.createRow(getCount());
		addCell(headerTopRow, cellStyle1, " ");
		
		headerTopRow = sheet.createRow(getCount());
	    addCell(headerTopRow, columnHeaderName, "TOTALS");
	    temp1 = counter;
	    sheet.addMergedRegion(new CellRangeAddress(temp1, temp1, 0, 1));
	    headerTopRow = sheet.createRow(getCount());
	    addCell(headerTopRow, cellStyle1, "Posts");
		addCell(headerTopRow, cellStyle1, ""+params.get("grandTotalPost"), "Integer");
		
		headerTopRow = sheet.createRow(getCount());
	    addCell(headerTopRow, cellStyle1, "Interactions");
		addCell(headerTopRow, cellStyle1, ""+numberFormat.format(params.get("grandTotalInteractions")));
		
		headerTopRow = sheet.createRow(getCount());
	    addCell(headerTopRow, cellStyle1, "Effectiveness Rate");
	    addCell(headerTopRow, cellStyle1, ""+numberFormat.format(params.get("grandEffectivenessRate")));
		
		headerTopRow = sheet.createRow(getCount());
	    addCell(headerTopRow, cellStyle1, "Most Used Keyword");
		addCell(headerTopRow, cellStyle1, ""+params.get("mostUsedKeyword"));
		
		headerTopRow = sheet.createRow(getCount());
	    addCell(headerTopRow, cellStyle1, "YouTube Views");
		addCell(headerTopRow, cellStyle1, ""+numberFormat.format(params.get("youtubeView")));
	    
		headerTopRow = sheet.createRow(getCount());
		addCell(headerTopRow, cellStyle1, " ");
		
		headerTopRow = sheet.createRow(getCount());
	    headerTopRow.setHeight((short) 400);
	    addCell(headerTopRow, columnHeaderName, "Team Data");
	    temp1 = counter;
	    sheet.addMergedRegion(new CellRangeAddress(temp1, temp1, 0, 6));
	    
	    headerTopRow = sheet.createRow(getCount());	    
	    addCell(headerTopRow, columnHeader, " Team ");
	    addCell(headerTopRow, columnHeader, " Posts ");
	    addCell(headerTopRow, columnHeader, "Interactions");
	    addCell(headerTopRow, columnHeader, " Effectiveness Rate ");
	    addCell(headerTopRow, columnHeader, " YouTube Views ");
	    addCell(headerTopRow, columnHeader, " Total Social Media Posts ");
	    addCell(headerTopRow, columnHeader, " % Of Posts Branded ");
		
	    if(teamData != null && teamData.size() > 0) {
	    	List<ReportExportDTO> data = teamData;
	    	
	    	for (ReportExportDTO r : data) {
				headerTopRow = sheet.createRow(getCount());

				addCell(headerTopRow, cellStyle1, " " + r.getTeamName());
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getTotalPost()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getTotalInteractions()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(getDouble(r.getTotalEffectivenessRate())));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getTotalYtViews()));
				addCell(headerTopRow, cellStyle1, ""+ numberFormat.format(r.getTotalSocialMediaPost()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(getDouble(r.getPostBrandedPercentange())));
			}
	    }
	    
	    headerTopRow = sheet.createRow(getCount());
		addCell(headerTopRow, cellStyle1, " ");
	    
	    headerTopRow = sheet.createRow(getCount());
	    headerTopRow.setHeight((short) 400);
	    addCell(headerTopRow, columnHeaderName, "Keyword Data");
	    temp1 = counter;
	    sheet.addMergedRegion(new CellRangeAddress(temp1, temp1, 0, 5));
		
	    headerTopRow = sheet.createRow(getCount());	    
	    addCell(headerTopRow, columnHeader, " Keyword");
	    addCell(headerTopRow, columnHeader, " Posts ");
	    addCell(headerTopRow, columnHeader, "Interactions");
	    addCell(headerTopRow, columnHeader, " Effectiveness Rate ");
	    addCell(headerTopRow, columnHeader, " Usage (% of Posts) ");
	    addCell(headerTopRow, columnHeader, " YouTube Views ");
	    
	    if(keywordData != null && keywordData.size() > 0) {
	    	List<ReportExportDTO> data = keywordData;
	    	
	    	for (ReportExportDTO r : data) {
				headerTopRow = sheet.createRow(getCount());

				addCell(headerTopRow, cellStyle1, " " + r.getKeyword());
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getTotalPost()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getTotalInteractions()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(getDouble(r.getTotalEffectivenessRate())));
				addCell(headerTopRow, cellStyle1, " " + r.getPercentagePost()+"%");
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getTotalYtViews()));
			}
	    }
	    
	    headerTopRow = sheet.createRow(getCount());
		addCell(headerTopRow, cellStyle1, " ");
	    
	    headerTopRow = sheet.createRow(getCount());
	    headerTopRow.setHeight((short) 400);
	    addCell(headerTopRow, columnHeaderName, "Member Data");
	    
		temp1 = counter;
		sheet.addMergedRegion(new CellRangeAddress(temp1, temp1, 0, 13));
	    
	    headerTopRow = sheet.createRow(getCount());	    
	    addCell(headerTopRow, columnHeader, " Member Name ");
	    addCell(headerTopRow, columnHeader, " Team ");
	    addCell(headerTopRow, columnHeader, " Posts ");
	    addCell(headerTopRow, columnHeader, "Interactions");
	    addCell(headerTopRow, columnHeader, " Effectiveness Rate ");
	    addCell(headerTopRow, columnHeader, " YouTube Views ");
	    addCell(headerTopRow, columnHeader, " Total Social Media Posts ");
	    addCell(headerTopRow, columnHeader, " % Of Posts Branded ");
	    addCell(headerTopRow, columnHeader, "Tournaments ");
	    addCell(headerTopRow, columnHeader, "Entries ");
	    addCell(headerTopRow, columnHeader, "Events ");
	    addCell(headerTopRow, columnHeader, "Leads ");
	    addCell(headerTopRow, columnHeader, "Print ");
	    addCell(headerTopRow, columnHeader, "AT Score ");
	    
	    
	    if(memberData != null && memberData.size() > 0) {
	    	List<ReportExportDTO> data = memberData;
	    	
	    	for (ReportExportDTO r : data) {
				headerTopRow = sheet.createRow(getCount());				

				addCell(headerTopRow, cellStyle1, " " + r.getMemberName());
				addCell(headerTopRow, cellStyle1, "" + r.getTeamName());
				addCell(headerTopRow, cellStyle1, ""+ numberFormat.format(r.getTotalPost()));
				addCell(headerTopRow, cellStyle1, ""+ numberFormat.format(r.getTotalInteractions()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(getDouble(r.getTotalEffectivenessRate())));
				addCell(headerTopRow, cellStyle1, ""+ numberFormat.format(r.getTotalYtViews()));
				addCell(headerTopRow, cellStyle1, ""+ numberFormat.format(r.getTotalSocialMediaPost()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(getDouble(r.getPostBrandedPercentange())));
				
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getTournamentsCount()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getBlogCount()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getEventCount()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getLeadsCount()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getPrintCount()));
				addCell(headerTopRow, cellStyle1, "" + numberFormat.format(r.getTotalActivity()));
			}
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
	    sheet.autoSizeColumn((short)11);
	    sheet.autoSizeColumn((short)12);
	    sheet.autoSizeColumn((short)13);
	    
		
	 } 
	
	private HSSFFont getFont(HSSFWorkbook workbook, short color) {
		HSSFFont font = workbook.createFont();
		font.setColor(color); 
		font.setBold(true);
		font.setFontHeightInPoints(((short)(font.getFontHeightInPoints() + 2))) ;
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
	
	private HSSFCell addCell(HSSFRow row, HSSFCellStyle cellStyle, String value, String cellType) {
		HSSFCell cell = row.createCell( ((int)row.getLastCellNum() < 0) ? 0 : (int)row.getLastCellNum() ); 
		cell.setCellStyle(cellStyle); 
	    if(StringUtils.isNotBlank(cellType)) {
	    	if(cellType.equalsIgnoreCase("Integer") || cellType.equalsIgnoreCase("Double")) {
	    		if(cellType.equalsIgnoreCase("Double")) {
	    			cell.setCellValue(NumberUtils.toFloat(value));
	    		}else {
	    			cell.setCellValue(Integer.parseInt(value));
	    		}
	    		 
	    		cell.setCellType(CellType.NUMERIC);
	    	}else {
	    		cell.setCellValue(new HSSFRichTextString(value)); 
	    		cell.setCellType(CellType.STRING);
	    	}
	    }else {
	    	cell.setCellType(CellType.STRING);
	    }
	    
	    return cell;
	} 

	/*
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static JasperPrint generateReport(String jrxmlFileName, JasperFormat format, Map params, List data)
			throws Exception {
		

		log.debug("Generating Jasper reports using " + jrxmlFileName + " file and requested formate is "
				+ format.getDescription());

		InputStream inputStream = null, imageStream = null;
		try {

			log.debug("Reading " + jrxmlFileName + " file..");
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(jrxmlFileName);
			imageStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("jrxml/logo-skype-blue.png");

			params.put("imagePath", imageStream);

			log.debug("Compiling " + jrxmlFileName + " file..");
			JasperReport jr = JasperCompileManager.compileReport(inputStream);

			log.debug("Generating Jasper Print for " + jrxmlFileName + " file..");
			JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(data);
			JasperPrint jp = null;
			try {
				jp = JasperFillManager.fillReport(jr, params, beanCollectionDataSource);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (format.getId() == JasperFormat.PDF_FORMAT.getId()) {
				return jp;
			} else {
				throw new UserException("Unknown output format defined for Jasper.");
			}
		} catch (Exception e) {
			log.error("Error while generating Jasper report for file: " + jrxmlFileName + " in format: "
					+ format.getDescription(), e);
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}

		}
	}
*/
}