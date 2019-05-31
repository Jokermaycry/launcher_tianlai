package com.csw.tp.bean;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;


public class GetAppInfo {

	private static Context context;

	public GetAppInfo(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/*
	 * 将app里，所有的程序
	 */
	public   List<AppInfo> list = new ArrayList<AppInfo>();

	/*
	 * public List<AppInfo> getAllAPK() { if(list.size()>0){ list.clear(); } int
	 * saomiao = 0; Drawable icon = null; PackageManager pm =
	 * context.getPackageManager(); // 获得PackageManager对象 Intent mainIntent =
	 * new Intent(Intent.ACTION_MAIN, null);
	 * mainIntent.addCategory(Intent.CATEGORY_LAUNCHER); //
	 * 通过查询，获得所有ResolveInfo对象. // wjz GET_UNINSTALLED_PACKAGES:所有的程序吧，包括SD卡 //
	 * MATCH_DEFAULT_ONLY：只是系统程序 List<ResolveInfo> resolveInfos =
	 * pm.queryIntentActivities(mainIntent,
	 * PackageManager.GET_UNINSTALLED_PACKAGES); // 调用系统排序 ， 根据name排序 //
	 * 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序 Collections.sort(resolveInfos, new
	 * ResolveInfo.DisplayNameComparator(pm)); int len = resolveInfos.size();
	 * for (ResolveInfo reInfo : resolveInfos) { saomiao++; String activityName
	 * = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name String pkgName =
	 * reInfo.activityInfo.packageName; // 获得应用程序的包名 String appLabel = (String)
	 * reInfo.loadLabel(pm); // 获得应用程序的Label icon = reInfo.loadIcon(pm); //
	 * 获得应用程序图标 // 为应用程序的启动Activity 准备Intent Intent launchIntent = new Intent();
	 * launchIntent.setComponent(new ComponentName(pkgName, activityName)); //
	 * 创建一个AppInfo对象，并赋值 AppInfo appInfo = new AppInfo();
	 * appInfo.setActivityName(activityName); appInfo.setPkgName(pkgName);
	 * appInfo.setIntent(launchIntent); appInfo.setAppIcon(reInfo.loadIcon(pm));
	 * appInfo.setAppLabel(appLabel); appInfo.setAppIcon(icon);
	 * list.add(appInfo); } pm = null; resolveInfos.clear(); return list;
	 * 
	 * }
	 */
	/*
	 * 查询出了需要隐藏的apk之外的所有apk
	 */
	public  List<AppInfo> getAllAPK_add() {
		if (list.size() > 0) {
			list.clear();
		}
		int saomiao = 0;
		Drawable icon = null;
		PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 通过查询，获得所有ResolveInfo对象.
		// wjz GET_UNINSTALLED_PACKAGES:所有的程序吧，包括SD卡
		// MATCH_DEFAULT_ONLY：只是系统程序
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);
		// 调用系统排序 ， 根据name排序
		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));
		int len = resolveInfos.size();
		for (ResolveInfo reInfo : resolveInfos) {
			saomiao++;
			String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
			String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
			String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
			if (del_kuaijie_sel(appLabel) == 0) {
				// 为应用程序的启动Activity 准备Intent
				icon = reInfo.loadIcon(pm); // 获得应用程序图标
				BitmapDrawable db = (BitmapDrawable) icon;
			    String iconString = convertIconToString(db.getBitmap());
				Intent launchIntent = new Intent();
				launchIntent.setComponent(new ComponentName(pkgName,
						activityName));
				// 创建一个AppInfo对象，并赋值
				
				AppInfo appInfo = new AppInfo();
				appInfo.setAppLabel(appLabel);
				appInfo.setAppIcon(iconString);
				appInfo.setActivityname(activityName);
				appInfo.setPagename(pkgName);
				list.add(appInfo);
			}
		}
		pm = null;
		resolveInfos.clear();
		return list;

	}
	
	public  List<AppInfo> getAllAPK_add1(){
		if (list.size() > 0) {
			list.clear();
		}
		int saomiao = 0;
		Drawable icon = null;
		PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 通过查询，获得所有ResolveInfo对象.
		// wjz GET_UNINSTALLED_PACKAGES:所有的程序吧，包括SD卡
		// MATCH_DEFAULT_ONLY：只是系统程序
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);
		// 调用系统排序 ， 根据name排序
		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));
		int len = resolveInfos.size();
		for (ResolveInfo reInfo : resolveInfos) {
			saomiao++;
			String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
			String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
			String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
			if (del_kuaijie_sel(appLabel) == 0) {
				// 为应用程序的启动Activity 准备Intent
				icon = reInfo.loadIcon(pm); // 获得应用程序图标
			//	BitmapDrawable db = (BitmapDrawable) icon;
			//    String iconString = convertIconToString(db.getBitmap());
				Intent launchIntent = new Intent();
				launchIntent.setComponent(new ComponentName(pkgName,
						activityName));
				// 创建一个AppInfo对象，并赋值
				
				AppInfo appInfo = new AppInfo();
				appInfo.setAppLabel(appLabel);
			//	appInfo.setAppIcon(iconString);
				appInfo.setActivityname(activityName);
				appInfo.setPagename(pkgName);
				list.add(appInfo);
			}
		}
		pm = null;
		resolveInfos.clear();
		return list;

	}
	public static Intent pkgLaber_compare(String laber) {
		int saomiao = 0;
		Drawable icon = null;
		PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 通过查询，获得所有ResolveInfo对象.
		// wjz GET_UNINSTALLED_PACKAGES:所有的程序吧，包括SD卡
		// MATCH_DEFAULT_ONLY：只是系统程序
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);
		// 调用系统排序 ， 根据name排序
		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));
		int len = resolveInfos.size();
		for (ResolveInfo reInfo : resolveInfos) {
			saomiao++;
			String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
			String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
			String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
			if (laber.equals(appLabel)) {
				// 为应用程序的启动Activity 准备Intent
				Intent launchIntent = new Intent();
				launchIntent.setComponent(new ComponentName(pkgName,
						activityName));
				 return launchIntent;
			}
		}
		pm = null;
		resolveInfos.clear();
		return null;

	}
	/**
	 * 获取应用程序的包名
	 * @param laber
	 * @return
	 */
	public static String pkgLaber_compareName(String laber) {
		int saomiao = 0;
		Drawable icon = null;
		PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 通过查询，获得所有ResolveInfo对象.
		// wjz GET_UNINSTALLED_PACKAGES:所有的程序吧，包括SD卡
		// MATCH_DEFAULT_ONLY：只是系统程序
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);
		// 调用系统排序 ， 根据name排序
		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));
		int len = resolveInfos.size();
		for (ResolveInfo reInfo : resolveInfos) {
			saomiao++;
			String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
			String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
			String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
			if (laber.equals(appLabel)) {
				// 为应用程序的启动Activity 准备Intent
				 
				return pkgName;
			}
		}
		pm = null;
		resolveInfos.clear();
		return null;

	}

	/**
	 * 获取应用程序的包名类名
	 * @param laber
	 * @return
	 */
	public static String pkgLaber_compareName_activityName(String laber) {
		int saomiao = 0;
		Drawable icon = null;
		PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 通过查询，获得所有ResolveInfo对象.
		// wjz GET_UNINSTALLED_PACKAGES:所有的程序吧，包括SD卡
		// MATCH_DEFAULT_ONLY：只是系统程序
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);
		// 调用系统排序 ， 根据name排序
		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));
		int len = resolveInfos.size();
		for (ResolveInfo reInfo : resolveInfos) {
			saomiao++;
			String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
			String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
			String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
			if (laber.equals(appLabel)) {
				// 为应用程序的启动Activity 准备Intent
				 
				return pkgName+","+activityName;
			}
		}
		pm = null;
		resolveInfos.clear();
		return null;

	}
	
	  int del_kuaijie_sel(String str) {
		  if (str.equals("电视回看")) {
				return 1;
			} else if (str.equals("影视点播")) {
				return 1;
			} else if (str.equals("多屏互动")) {
				return 1;
			} else if (str.equals("电视桌面")) {
				return 1;
			} else if (str.equals("设置")) {
				return 1;
			} else if (str.indexOf("CSWUI") != -1) {
				return 1;
			}else if(str.indexOf("YaHaoUI") != -1){
				return 1;
			}else if (str.indexOf("SoundBarUi")!=-1){
				return 1;
			}

		return 0;
	}
	/**
	 * 图片转成string
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.PNG, 70, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);

	}
 
}
