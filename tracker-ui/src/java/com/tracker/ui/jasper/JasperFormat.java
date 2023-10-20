package com.tracker.ui.jasper;

public enum JasperFormat {
	
	PDF_FORMAT (1, "PDF", "pdf", "application/octet-stream"),
	EXCEL_FORMAT (2, "EXCEL", "xls", "application/vnd.ms-excel"),
	CSV_FORMAT (3, "CSV", "csv", "text/csv"),
	HTML_FORMAT (4, "HTML", "html", "text/html");

    private int id; 
    private String description;
    private String extention;
    private String contentType;
    
    JasperFormat(int id, String description, String extention, String contentType) {
    	this.id = id;
    	this.description = description;
    	this.extention = extention;
    	this.contentType = contentType;
    }
    
	public int getId() {
		return id;
	} 
	
	public String getDescription() {
		return description;
	}
	
	public String getExtention() {
		return extention;
	}

	public String getContentType() {
		return contentType;
	}

	public static JasperFormat findById(int id) {
		for(JasperFormat jf : JasperFormat.values()) {
			if(jf.getId() == id) {
				return jf;
			}
		}
		
		return PDF_FORMAT;
	}

}
