package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Test {
	public static void main(String[] args) {
		SimpleDateFormat sdf1 = new SimpleDateFormat();
		sdf1.setTimeZone(TimeZone.getDefault());
		/**
		 * 生成ISO-8601 规范的时间格式
		 *
		 * @param date
		 * @return
		 */
		//String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
		//return DateFormatUtils.format(date, pattern);
		System.out.println(new Date().getSeconds());
	}
}
