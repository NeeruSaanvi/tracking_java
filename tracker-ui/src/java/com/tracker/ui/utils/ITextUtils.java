package com.tracker.ui.utils;

import java.util.List;

/*import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;*/
import com.tracker.commons.dtos.ReportStatResponse;
import com.tracker.commons.models.Reports;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ITextUtils {

	public static void createTotalTable(/* Document document, ReportStatResponse statResponse */)  {
		/*
		 * log.debug("Total Table Creating...");
		 * 
		 * PdfPTable table = new PdfPTable(2); table.setWidthPercentage(50);
		 * table.setWidths(new int[] { 3, 3 });
		 * 
		 * Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD); BaseColor
		 * myColor = WebColors.getRGBColor("#A00000");
		 * 
		 * PdfPCell hcell; hcell = new PdfPCell(new Phrase("Total", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); hcell.setColspan(2);
		 * //hcell.setBackgroundColor(myColor); table.addCell(hcell);
		 * 
		 * table.setHeaderRows(1);
		 * 
		 * hcell = new PdfPCell(new Phrase("Post", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_RIGHT); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new
		 * Phrase(statResponse.getGrandTotalPost().toString()));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase("Interactions", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_RIGHT); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new
		 * Phrase(statResponse.getGrandTotalInteractions().toString()));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase("Effectiveness Rate", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_RIGHT); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new
		 * Phrase(Double.toString(statResponse.getGrandEffectivenessRate())));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * document.add(table);
		 * 
		 */}
	
	public static void createMemberTable(/* Document document, List<Reports> memberList */) /*
																							 * throws DocumentException
																							 */{
		/*
		 * log.debug("Total Table Creating...");
		 * 
		 * PdfPTable table = new PdfPTable(6); table.setWidthPercentage(100);
		 * table.setWidths(new int[] { 3, 3, 3, 3, 3, 3 });
		 * 
		 * Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD); BaseColor
		 * myColor = WebColors.getRGBColor("#2F5496");
		 * 
		 * PdfPCell hcell; hcell = new PdfPCell(new Phrase("Member Data", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); hcell.setColspan(6);
		 * hcell.setBackgroundColor(myColor); table.addCell(hcell);
		 * 
		 * table.setHeaderRows(2);
		 * 
		 * hcell = new PdfPCell(new Phrase("Member Name", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase("Team", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase("Posts", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase("Interactions", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase("Effectiveness Rate", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase("YouTube Views", headFont));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * for(Reports report : memberList) { hcell = new PdfPCell(new
		 * Phrase(report.getName()));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase(report.getName()));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase(report.getName()));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase(report.getName()));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase(report.getName()));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell);
		 * 
		 * hcell = new PdfPCell(new Phrase(report.getName()));
		 * hcell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(hcell); }
		 * 
		 * 
		 * document.add(table);
		 * 
		 */}
}
