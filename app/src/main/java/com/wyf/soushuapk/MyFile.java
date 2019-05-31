package com.wyf.soushuapk;

import java.io.File;

import android.graphics.drawable.Drawable;

public class MyFile {
   private Drawable apk_icon;
   private String packageName;
   private String filePath;
   private String versionName;
   private String label;
   private int versionCode;
   private int installed;
   
public String getLabel() {
	return label;
}
public void setLabel(String label) {
	this.label = label;
}
public Drawable getApk_icon() {
	return apk_icon;
}
public void setApk_icon(Drawable apk_icon) {
	this.apk_icon = apk_icon;
}
public String getPackageName() {
	return packageName;
}
public void setPackageName(String packageName) {
	this.packageName = packageName;
}
public String getFilePath() {
	return filePath;
}
public void setFilePath(String filePath) {
	this.filePath = filePath;
}
public String getVersionName() {
	return versionName;
}
public void setVersionName(String versionName) {
	this.versionName = versionName;
}
public int getVersionCode() {
	return versionCode;
}
public void setVersionCode(int versionCode) {
	this.versionCode = versionCode;
}
public int getInstalled() {
	return installed;
}
public void setInstalled(int installed) {
	this.installed = installed;
}
   
}
