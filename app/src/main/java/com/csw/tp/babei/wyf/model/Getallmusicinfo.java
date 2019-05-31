package com.csw.tp.babei.wyf.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;


public class Getallmusicinfo {
	public static List<Gequinfo> data = new ArrayList<Gequinfo>();//存放本地歌曲
	public Getallmusicinfo(Context context) {
		Cursor mAudioCursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
				null, // 查询条件
				null, // 条件的对应?的参数
				MediaStore.Audio.AudioColumns.TITLE);// 排序方式
		// 循环输出歌曲的信息
		//List<String> mListData = new ArrayList<String>();
		
		for (int i = 0; i < mAudioCursor.getCount(); i++) {
			mAudioCursor.moveToNext();
			// 找到歌曲标题和总时间对应的列索引
			int indexTitle = mAudioCursor
					.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);//歌名
			int indexARTIST = mAudioCursor
					.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);//艺术家
			int indexALBUM = mAudioCursor
					.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);//专辑
			String url = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));//文件路径
			String strTitle = mAudioCursor.getString(indexTitle);
			String strARTIST = mAudioCursor.getString(indexARTIST);
			Gequinfo getinfo = new Gequinfo();
			String strALBUM = mAudioCursor.getString(indexALBUM);
			getinfo.setName(strTitle);
			getinfo.setUrl(url);
	       data.add(getinfo);
			
		}	
	}
	
	
}
