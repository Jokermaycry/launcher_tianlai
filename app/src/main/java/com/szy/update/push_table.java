package com.szy.update;


public class push_table {
	private int Id;
	private String apkname;
	private String mac;
	private String address;
	private String ifpush;
	private String pushAddress;
	private String ifinstall;
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getApkname() {
		return apkname;
	}

	public void setApkname(String apkname) {
		this.apkname = apkname;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIfpush() {
		return ifpush;
	}

	public void setIfpush(String ifpush) {
		this.ifpush = ifpush;
	}

	public String getPushAddress() {
		return pushAddress;
	}

	public void setPushAddress(String pushAddress) {
		this.pushAddress = pushAddress;
	}

	public String getIfinstall() {
		return ifinstall;
	}

	public void setIfinstall(String ifinstall) {
		this.ifinstall = ifinstall;
	}

}
