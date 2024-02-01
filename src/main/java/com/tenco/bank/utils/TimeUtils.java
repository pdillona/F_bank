package com.tenco.bank.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtils {

	public static String timestampToString(Timestamp timestamp) {
		
		// yyyy-mm-dd HH:mm:ss
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sdf.format(timestamp);
	}
}
