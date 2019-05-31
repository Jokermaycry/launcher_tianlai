package com.csw.tp.bean;

import android.content.Intent;
import android.graphics.drawable.Drawable;

//Model�� �������洢Ӧ�ó�����Ϣ
public class AppInfo {

	private String appLabel; // Ӧ�ó�����?
	private String appIcon; // Ӧ�ó���ͼ��
	private String activityname;
	private String pagename;
	public String getActivityname() {
		return activityname;
	}
	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}
	public String getPagename() {
		return pagename;
	}
	public void setPagename(String pagename) {
		this.pagename = pagename;
	}
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}
	public String getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}
 
}
