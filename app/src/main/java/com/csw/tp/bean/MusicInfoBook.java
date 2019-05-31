package com.csw.tp.bean;

import java.util.List;

public class MusicInfoBook {
	/**
	 * ����ӵ����
	 */
   private String pathOwner;
   /*
    * ���и���
    */
   private List<MusicInfoEntity> musicInfoEntityList;
   public MusicInfoBook(String pathOwner, List<MusicInfoEntity> musicInfoEntityList) {
	   super();
	   this.pathOwner = pathOwner;
	   this.musicInfoEntityList = musicInfoEntityList;
}
public MusicInfoBook() {
	super();
}
public String getPathOwner() {
	return pathOwner;
}
public void setPathOwner(String pathOwner) {
	this.pathOwner = pathOwner;
}
public List<MusicInfoEntity> getMusicInfoEntityList() {
	return musicInfoEntityList;
}
public void setMusicInfoEntityList(List<MusicInfoEntity> musicInfoEntityList) {
	this.musicInfoEntityList = musicInfoEntityList;
}
   
   
   
   
}
