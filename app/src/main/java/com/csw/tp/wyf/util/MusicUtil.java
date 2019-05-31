package com.csw.tp.wyf.util;

public class MusicUtil {
	
	
	/**
	 * 时间格式转换
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String toTime(long time) {

		time /= 1000;
		long minute = time / 60;
		long hour = minute / 60;
		long second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}


}
