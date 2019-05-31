package com.csw.tp.bean;

public class SystemInfo {
	
	private String devicename;
	private String cpu;
	private String memory;
	private String flashmemory;
	private String deviceversion;
   public static boolean isfirst = false;
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getFlashmemory() {
		return flashmemory;
	}
	public void setFlashmemory(String flashmemory) {
		this.flashmemory = flashmemory;
	}
	public String getDeviceversion() {
		return deviceversion;
	}
	public void setDeviceversion(String deviceversion) {
		this.deviceversion = deviceversion;
	}

}
