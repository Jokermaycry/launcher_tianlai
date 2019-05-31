package com.wyf.util;
/**
 * ���ʵ����
 * @author Administrator
 *
 */
public class Advert {
	private String id;//���ID
	private String imgUrl;//ͼƬurl
	private String adUrl;//Ŀ���ַurl
	private String imgType;//���ͣ�ռ������Ԫ��eg:2��ʾռ������Ԫ

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
