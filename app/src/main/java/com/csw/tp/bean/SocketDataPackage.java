package com.csw.tp.bean;

public class SocketDataPackage {
	private String BitmapStr;
	private int key_code;

	public SocketDataPackage(String bitmapStr, int key_code) {
		super();
		BitmapStr = bitmapStr;
		this.key_code = key_code;
	}

	public SocketDataPackage() {
		super();
	}

	public String getBitmapStr() {
		return BitmapStr;
	}

	public void setBitmapStr(String bitmapStr) {
		BitmapStr = bitmapStr;
	}

	public int getKey_code() {
		return key_code;
	}

	public void setKey_code(int key_code) {
		this.key_code = key_code;
	}

}
