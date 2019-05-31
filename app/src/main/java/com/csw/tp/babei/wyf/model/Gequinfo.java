package com.csw.tp.babei.wyf.model;

import java.util.ArrayList;
import java.util.List;

public class Gequinfo {
	
	public static List<Gequinfo> music_list = new ArrayList<Gequinfo>();
	private boolean ischeck;
	public boolean isIscheck() {
		return ischeck;
	}
	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String url;
	
}
