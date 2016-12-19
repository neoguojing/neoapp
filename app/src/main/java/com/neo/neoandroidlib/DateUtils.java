package com.neo.neoandroidlib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Context;

public class DateUtils {

	@SuppressLint("SimpleDateFormat")
	public static Date getDate(String time) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Date getDate(String time, String sFormat) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat(sFormat);
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String formatDate(Context context, long date) {
		@SuppressWarnings("deprecation")
		int format_flags = android.text.format.DateUtils.FORMAT_NO_NOON_MIDNIGHT
				| android.text.format.DateUtils.FORMAT_ABBREV_ALL
				| android.text.format.DateUtils.FORMAT_CAP_AMPM
				| android.text.format.DateUtils.FORMAT_SHOW_DATE
				| android.text.format.DateUtils.FORMAT_SHOW_DATE
				| android.text.format.DateUtils.FORMAT_SHOW_TIME;
		return android.text.format.DateUtils.formatDateTime(context, date,
				format_flags);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String dateToString(Date date,String format) {
		SimpleDateFormat sdf=new SimpleDateFormat(format);  
		String str=sdf.format(date);  
		return str;
	}
}
