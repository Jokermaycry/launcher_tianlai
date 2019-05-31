package com.csw.tp.bean;
/**
 * ������Ϣ
 * @author Administrator
 *
 */
public class MusicInfoEntity {
    
	/**
	 * ���ֱ���
	 */
	private String musicTitle;
	/**
	 * ����·��
	 */
	private String musicPath;
	/**
	 * ����ʱ��
	 */
	private long musicDuration;
	public MusicInfoEntity(String musicTitle, String musicPath,
			long musicDuration) {
		super();
		this.musicTitle = musicTitle;
		this.musicPath = musicPath;
		this.musicDuration = musicDuration;
	}
	public MusicInfoEntity() {
		super();
	}
	public String getMusicTitle() {
		return musicTitle;
	}
	public void setMusicTitle(String musicTitle) {
		this.musicTitle = musicTitle;
	}
	public String getMusicPath() {
		return musicPath;
	}
	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}
	public long getMusicDuration() {
		return musicDuration;
	}
	public void setMusicDuration(long musicDuration) {
		this.musicDuration = musicDuration;
	}
	
	
	
	
}
