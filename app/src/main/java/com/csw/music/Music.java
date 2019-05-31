package com.csw.music;

/*****
 * music ʵ���� ʵ��MVCģʽ 
 * @author Chopper
 *
 */
public class Music  {
  
	private String title;//���ֱ���
	private String singer;//��������
	private String album;//
	private String url;//path
	private long size;//
	private long time;//ʱ��
	private String name;
	private String httpurl;//path
	private String musicKz;
	
	
	public String getMusicKz() {
		return musicKz;
	}
	public void setMusicKz(String musicKz) {
		this.musicKz = musicKz;
	}
	public String getHttpurl() {
		return httpurl;
	}
	public void setHttpurl(String httpurl) {
		this.httpurl = httpurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
