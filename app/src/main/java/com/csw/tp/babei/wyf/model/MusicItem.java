package com.csw.tp.babei.wyf.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicItem implements Parcelable {

	public String id;
	public String musicid;
	public String filename;
	public String musicname;
	public String type;
	public String url;
	public String lrc;
	public String profile;
	public String author;
	public String playtime;
	public String contentsrc;
	public String hasLyric;	
	
//	hurley 
	public String cover;
	public String announcer;
	//��������½ڻ������½�����
	public String sections;
	//��Զ��������½�����
	public String sectionCount;
	
	//** ��Դ����  2����1��Ŀ0�鼮*/
	public String datatype;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMusicid() {
		return musicid;
	}

	public void setMusicid(String musicid) {
		this.musicid = musicid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMusicname() {
		return musicname;
	}

	public void setMusicname(String musicname) {
		this.musicname = musicname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLrc() {
		return lrc;
	}

	public void setLrc(String lrc) {
		this.lrc = lrc;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPlaytime() {
		return playtime;
	}

	public void setPlaytime(String playtime) {
		this.playtime = playtime;
	}

	public String getContentsrc() {
		return contentsrc;
	}

	public void setContentsrc(String contentsrc) {
		this.contentsrc = contentsrc;
	}
	
	public String getHasLyric() {
		return hasLyric;
	}

	public void setHasLyric(String hasLyric) {
		this.hasLyric = hasLyric;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public String getCover() {
		return cover;
	}

	
	public void setCover(String cover) {
		this.cover = cover;
	}

	
	public String getAnnouncer() {
		return announcer;
	}

	
	public void setAnnouncer(String announcer) {
		this.announcer = announcer;
	}
	
	public String getSections() {
		return sections;
	}

	
	public void setSections(String sections) {
		this.sections = sections;
	}
	
	public String getSectionCount() {
		return sectionCount;
	}

	public void setSectionCount(String sectionCount) {
		this.sectionCount = sectionCount;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(musicid);
		out.writeString(filename);
		out.writeString(musicname);
		out.writeString(type);
		out.writeString(url);
		out.writeString(lrc);
		out.writeString(profile);
		out.writeString(author);
		out.writeString(playtime);
		out.writeString(contentsrc);
		out.writeString(cover);
		out.writeString(announcer);
		out.writeString(sections);
	}

	@SuppressWarnings("unchecked")
	public static final Parcelable.Creator CREATOR = new MusicItem.Creator() {
		public MusicItem createFromParcel(Parcel in) {
			return new MusicItem(in);
		}

		public MusicItem[] newArray(int size) {
			return new MusicItem[size];
		}
	};

	private MusicItem(Parcel in) {
		id = in.readString();
		musicid = in.readString();
		filename = in.readString();
		musicname = in.readString();
		type = in.readString();
		url = in.readString();
		lrc = in.readString();
		profile = in.readString();
		author = in.readString();
		playtime = in.readString();
		contentsrc = in.readString();
		hasLyric = in.readString();
		cover = in.readString();
		announcer = in.readString();
		sections= in.readString();
	}

	public MusicItem() {
		id = "";
		musicid = "";
		filename = "";
		musicname = "";
		type = "";
		url = "";
		lrc = "";
		profile = "";
		author = "";
		playtime = "";
		contentsrc = "";
		hasLyric = "";
		cover = "";
		announcer = "";
		sections="";
	}

	public MusicItem(String id, String musicid, String filename,
			String musicname, String type, String url, String lrc,
			String profile, String author, String playtme, String contentsrc, String hasLyric, String cover, String announcer, String sections,
			String datatype) {
		this.id = id;
		this.musicid = musicid;
		this.filename = filename;
		this.musicname = musicname;
		this.type = type;
		this.url = url;
		this.lrc = lrc;
		this.profile = profile;
		this.author = author;
		this.playtime = playtme;
		this.contentsrc = contentsrc;
		this.hasLyric = hasLyric;
		
		this.cover = cover;
		this.announcer = announcer;
		this.sections=sections;
		
		this.datatype = datatype;
	}
	
}
