package com.wyf.util;

import java.util.HashMap;
import java.util.Map;


/**
 * 常量类
 * 
 * @author Robin-wu
 * 
 */
public final class Constant {
	public static final int HAND_WIFI = 0;
	public static final int HAND_WEATHER = 1;
	public static final int HAND_WEATHER_UP = 2;
	public static final int HAND_UPDATE = 3;
	public static final int ADD_UPDATE = 4;
	public static final int HAND_START = 5;
	public static  boolean UPWhether;
	public static String ServerIP = "www.jltvbox.com";
	public static final int HOME_TITLE_COUNT = 4;// 主页标题显示个数
	public static final String HOME_TITLE[] = { "音乐 ","影视",
		"资源管理","全部应用","我的设置"};
	/*
	 * public static final Integer[] GALLERY_IMAGES = { R.drawable.center,
	 * R.drawable.center, R.drawable.center, R.drawable.center };
	 */
	public static final String[] GALLERY_NAMES = { "长沙地铁", "钢铁王国", "移动OA",
			"智慧旅游" };
	public static final String[] nodes = {"status1","temperature1","temperature2","pollution_l"};
	public static Map<String, String>  map = new HashMap<String,String>();

}
