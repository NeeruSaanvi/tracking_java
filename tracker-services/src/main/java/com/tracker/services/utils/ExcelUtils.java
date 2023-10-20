package com.tracker.services.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.WordUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtils {
	
	public String[][] readExcel( String fileName ) {
		String[][] excelData = null;
        Workbook workbook =  null;
		try {
			File file = new File(fileName);
			workbook = WorkbookFactory.create(file);
			 Sheet sheet = workbook.getSheetAt( 0 );

			// Find number of rows in excel file
			int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
			excelData = new String[ rowCount ] [ sheet.getRow(0).getLastCellNum() ];
			// Create a loop over all the rows of excel file to read it
			for (int rowIndex = 1; rowIndex < rowCount ; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				// Create a loop to print cell values in a row
				for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
					excelData[ rowIndex ] [ colIndex ] = row.getCell(colIndex).getStringCellValue().trim();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("====== Exception caught:"+ e.getMessage() + " ========");
		} finally {

			try {
				if (workbook != null)
					workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("====== Exception caught:"+ e.getMessage() + " ========");
			}
		}
		return excelData;
	}
	
	public String[][] readExcel( InputStream fileName ) {
		String[][] excelData = null;
        Workbook workbook =  null;
		try {
//			File file = new File(fileName);
			workbook = WorkbookFactory.create(fileName);
			 Sheet sheet = workbook.getSheetAt( 0 );

			// Find number of rows in excel file
			int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
			excelData = new String[ rowCount ] [ sheet.getRow(0).getLastCellNum() ];
			// Create a loop over all the rows of excel file to read it
			for (int rowIndex = 1; rowIndex < rowCount ; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				// Create a loop to print cell values in a row
				for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
					excelData[ rowIndex ] [ colIndex ] = row.getCell(colIndex).getStringCellValue().trim();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("====== Exception caught:"+ e.getMessage() + " ========");
		} finally {

			try {
				if (workbook != null)
					workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("====== Exception caught:"+ e.getMessage() + " ========");
			}
		}
		return excelData;
	}

	private HSSFFont getFont(HSSFWorkbook workbook, short color) {
		HSSFFont font = workbook.createFont();
		font.setColor(color); 
		font.setBold(true);
		font.setFontHeightInPoints(((short)(font.getFontHeightInPoints() + 2))) ;
		return font;
	}
	
	public static ByteArrayOutputStream getExcelFile(List<Map<String,String>> dataList){
		
		ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();

		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("UserDatail");
        sheet.setDefaultColumnWidth(25);

        Set<String> keyset = dataList.get(0).keySet();
        int cols = keyset.size();
        
        try {
        	CellStyle styleX = workbook.createCellStyle();
        	styleX.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        	styleX.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
    	    styleX.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        	styleX.setWrapText(true);
        	int headCellCount = 0;
            
            Row headerRow = sheet.createRow(0);
            Cell headerCell[] = new Cell[cols];

            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("Name");
            
            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("Email");

            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("Phone");
            
            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("Date Added");
            
            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("Address");

            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("City");
            
            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("State");
            
            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("Zip");
            
            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("Shirt Size");
            
            headerCell[headCellCount] = headerRow.createCell(headCellCount);
            headerCell[headCellCount].setCellStyle(styleX);
            headerCell[headCellCount++].setCellValue("Glove Size");

	        for (int r=0; r<dataList.size();r++) {
	            Map<String,String> map = dataList.get(r);
	            Row row = sheet.createRow(r+1);
	            int col = 0;
	                Cell cell[] = new Cell[cols];
	                
	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(WordUtils.capitalize(map.get("name")));
	            	
	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(map.get("email"));

	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(map.get("phone"));
	            	
	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(map.get("date"));

	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(WordUtils.capitalize(map.get("address")));

	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(WordUtils.capitalize(map.get("city")));

	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(WordUtils.capitalize(map.get("state")));
	            	
	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(map.get("zip"));

	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(map.get("shirtSize"));

	            	cell[col] = row.createCell(col);
	            	cell[col++].setCellValue(map.get("gloveSize"));
	        }
        }
        catch(NullPointerException e) {
        	e.printStackTrace();
        }
        try {
            workbook.write(bufferOut);
            bufferOut.flush();
            bufferOut.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bufferOut;
	}
}
//H(2003) and X(2007+)