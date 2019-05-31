package com.wyf.allapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.wyf.app.AppInfo;

public class GetAppInfo {

	private Context context;

	public GetAppInfo(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/*
	 * 将app里，所有的程序
	 */
	public static List<AppInfo> list = new ArrayList<AppInfo>();

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
	public List<AppInfo> getAllAPK_add() {
		Log.i("GetAppInfo.list.size=======", list.size()+"" );
		if (list.size() > 0) {
			list.clear();
//			list=null;
			list = new ArrayList<AppInfo>();
			Log.i("GetAppInfo.list.size==reset=====", list.size()+"" );
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
				Intent launchIntent = new Intent();
				launchIntent.setComponent(new ComponentName(pkgName,
						activityName));
				// 创建一个AppInfo对象，并赋值
				AppInfo appInfo = new AppInfo();
				appInfo.setActivityName(activityName);
				appInfo.setPkgName(pkgName);
				appInfo.setIntent(launchIntent);
				appInfo.setAppIcon(reInfo.loadIcon(pm));
				appInfo.setAppLabel(appLabel);
				appInfo.setAppIcon(icon);
				list.add(appInfo);
			}
		}
		pm = null;
		resolveInfos.clear();
		return list;

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
		}

		return 0;
	}

	/*
	 * 根据包名进行比较
	 */
	public AppInfo pkgName_compare(String pkgname) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPkgName().equals(pkgname)) {
				return list.get(i);
			}
		}
		return null;
	}
}
