package com.csw.tp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiConfiguration;


public class SharedPreferencesUtils {
	//��ݵ�һ���洢��ʽ����SQLite���������ݿ⣬������������������ں���Ŀһ��?
	private static SharedPreferences preferences;
	private static final String PREFS_NAME = "com.screenui.main";
	private static int MODE = Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE;
	public SharedPreferencesUtils(Context context) {

	}




	public static void save(Context context, String key, String value) {
		preferences = context
				.getSharedPreferences(PREFS_NAME, MODE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
   
	public static String get(Context context, String key) {
		preferences = context
				.getSharedPreferences(PREFS_NAME,MODE);
		return preferences.getString(key, null);
	}
	//��һ���ǲ鿴��û�����Keyû�еĻ�������Ĭ��false�� ��ִ������������set�����󣬵������ִ���������������ҵ�����ص�Key�ͻ�õ����Key��ֵ��
	public static boolean getSplash(Context context, String key) {
		preferences = context
				.getSharedPreferences(PREFS_NAME,MODE);
		return preferences.getBoolean(key, false);
	}
	//��һ��key���ٸ�key����һ��ֵ��Ϊ������Ĳ���key��ֵ����д�ġ�
	public static void setSplash(Context context, String key, boolean value) {
		preferences = context
				.getSharedPreferences(PREFS_NAME, MODE);
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static void setData(Context context,String key,String value){
		preferences = context
				.getSharedPreferences(PREFS_NAME,MODE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static String getData(Context context,String key){
		preferences = context.
				getSharedPreferences(PREFS_NAME, MODE);
		return preferences.getString(key, null);
	}
	public static void setsum(Context context,String key,int value){
		preferences = context
				.getSharedPreferences(PREFS_NAME,MODE);
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getsum(Context context,String key){
		preferences = context
				.getSharedPreferences(PREFS_NAME, MODE);
		return preferences.getInt(key, 0);
	}
	public static void ClearData(Context context){
		preferences = context
				.getSharedPreferences(PREFS_NAME,MODE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}
}