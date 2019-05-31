package com.wyf.util;

public class pushTable {
	
	private int Id;
	private String apkName;
	private String pushWhether;
	private String pushInfo;
	private String pushImagePath;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getPushWhether() {
		return pushWhether;
	}
	public void setPushWhether(String pushWhether) {
		this.pushWhether = pushWhether;
	}
	public String getPushInfo() {
		return pushInfo;
	}
	public void setPushInfo(String pushInfo) {
		this.pushInfo = pushInfo;
	}
	public String getPushImagePath() {
		return pushImagePath;
	}
	public void setPushImagePath(String pushImagePath) {
		this.pushImagePath = pushImagePath;
	}
	

}
