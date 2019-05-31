package com.csw.tp.bean;

import android.graphics.drawable.Drawable;

/**
 * 已经保存的定时信息
 * 
 * @author lgj
 * 
 */
public class MusicSavedInfo {
	
	/**
	 * id
	 */
	private int musicId;
	/**
	 * 时，分
	 */
	private String hourAndMinute;
	/**
	 * 开机还是关机，1是开机，0是关机
	 */
	private String openOrCloseState;
	/**
	 * 星期的哪几天,如“12”表示星期一和星期二
	 */
	private String whichDays;
	/**
	 * 音乐图标
	 */
	private Drawable musicIcon;
	/**
	 * 到时间了播放哪首音乐
	 */
	private String onTimeMusicTitle;
	/**
	 * 音乐的路径
	 */
	private String onTimeMusicPath;
	/**
	 * 定时开关打开or关闭，1是打开，0是关闭
	 */
	private String on_off;

	
	
	public MusicSavedInfo() {
		super();
	}

	

	public int getMusicId() {
		return musicId;
	}



	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}



	public MusicSavedInfo(String hourAndMinute, String openOrCloseState,
			String whichDays, String onTimeMusicTitle, String onTimeMusicPath,
			String on_off) {
		super();
		this.hourAndMinute = hourAndMinute;
		this.openOrCloseState = openOrCloseState;
		this.whichDays = whichDays;
		this.onTimeMusicTitle = onTimeMusicTitle;
		this.onTimeMusicPath = onTimeMusicPath;
		this.on_off = on_off;
	}

   

	


	public Drawable getMusicIcon() {
		return musicIcon;
	}

	public void setMusicIcon(Drawable musicIcon) {
		this.musicIcon = musicIcon;
	}



	
	
	public String getOnTimeMusicPath() {
		return onTimeMusicPath;
	}

	public void setOnTimeMusicPath(String onTimeMusicPath) {
		this.onTimeMusicPath = onTimeMusicPath;
	}

	public String getHourAndMinute() {
		return hourAndMinute;
	}

	public void setHourAndMinute(String hourAndMinute) {
		this.hourAndMinute = hourAndMinute;
	}

	public String getOpenOrCloseState() {
		return openOrCloseState;
	}

	public void setOpenOrCloseState(String openOrCloseState) {
		this.openOrCloseState = openOrCloseState;
	}

	public String getWhichDays() {
		return whichDays;
	}

	public void setWhichDays(String whichDays) {
		this.whichDays = whichDays;
	}

	public String getOnTimeMusicTitle() {
		return onTimeMusicTitle;
	}

	public void setOnTimeMusicTitle(String onTimeMusicTitle) {
		this.onTimeMusicTitle = onTimeMusicTitle;
	}

	public String getOn_off() {
		return on_off;
	}

	public void setOn_off(String on_off) {
		this.on_off = on_off;
	}

}
