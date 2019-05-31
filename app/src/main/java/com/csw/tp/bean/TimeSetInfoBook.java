package com.csw.tp.bean;

import java.util.List;

/**
 * 
 * @author json_data
 * ���ж�ʱ��Ϣʵ����
 */
public class TimeSetInfoBook {
    /**
     * ʱ��ӵ����
     */
	private String timeOwner;
	/**
	 * ���ж�ʱ��Ϣ
	 */
	 private List<MusicSavedInfo> mMusicSavedList;
	 
	 public TimeSetInfoBook() {
		// TODO Auto-generated constructor stub
	}

	public TimeSetInfoBook(String timeOwner,
			List<MusicSavedInfo> mMusicSavedList) {
		super();
		this.timeOwner = timeOwner;
		this.mMusicSavedList = mMusicSavedList;
	}

	public String getTimeOwner() {
		return timeOwner;
	}

	public void setTimeOwner(String timeOwner) {
		this.timeOwner = timeOwner;
	}

	public List<MusicSavedInfo> getmMusicSavedList() {
		return mMusicSavedList;
	}

	public void setmMusicSavedList(List<MusicSavedInfo> mMusicSavedList) {
		this.mMusicSavedList = mMusicSavedList;
	}
	 
	 
	 
}
