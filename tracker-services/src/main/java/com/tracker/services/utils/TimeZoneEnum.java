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
package com.tracker.services.utils;

public enum TimeZoneEnum {

	_9(9, "-12:00", "", "(GMT -12:00) Eniwetok, Kwajalein"),
	_2(2, "-11:00", "", "(GMT -11:00) Midway Island, Samoa"),
	_3(3, "-10:00", "US/Hawaii", "(GMT -10:00) Hawaii"),
	_4(4, "-09:50", "US/Taiohae", "(GMT -9:30) Taiohae"),
	_5(5, "-09:00", "US/Alaska", "(GMT -9:00) Alaska"),
	_6(6, "-08:00", "US/Pacific", "(GMT -8:00) Pacific Time (US & Canada)"),
	_7(7, "-07:00", "US/Mountain", "(GMT -7:00) Mountain Time (US & Canada)"),
	_8(8, "-06:00", "US/Central", "(GMT -6:00) Central Time (US & Canada), Mexico City"),
	_1(1, "-05:00", "US/Eastern", "(GMT -5:00) Eastern Time (US & Canada), Bogota, Lima");
	 

	int id; 
	String offset;
	String tzName;
	String displayName;

	TimeZoneEnum( int id, String offset, String tzName, String name) {
		this.id=id;
		this.offset = offset;
		this.tzName = tzName;
		this.displayName = name;
	}

	public int getId() {
		return id;
	} 

	public String getDisplayName() {
		return displayName;
	} 

	public String getOffset() {
		return offset;
	} 
	
	public String getTzName() {
		return tzName;
	}

	public static TimeZoneEnum findById(int id) {
		for(TimeZoneEnum en : TimeZoneEnum.values()) {
			if(en.getId() == id) {
				return en;
			}
		}

		return TimeZoneEnum._1;
	}

}
