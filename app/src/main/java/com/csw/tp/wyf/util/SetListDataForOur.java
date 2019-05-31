package com.csw.tp.wyf.util;

import java.util.List;

import com.csw.tp.babei.wyf.model.Music;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;



public class SetListDataForOur {

	private List<Music> lists;
	private Cursor cursor;
	private ContentResolver cr;

	//有参构造函数
	public SetListDataForOur(List<Music> lists, Cursor cursor,
			ContentResolver cr) {
		this.lists = lists;
		this.cursor = cursor;
		this.cr = cr;
	}

	public void setListDataFor() {

		cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
				null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (null == cursor) {
			return;
		}
		if (cursor.moveToFirst()) {
			do {
				Music m = new Music();
				String title = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.TITLE));
				String singer = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				if ("<unknown>".equals(singer)) {
					singer = "未知艺术家";
				}
				String album = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM));
				long size = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.SIZE));
				long time = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.DURATION));
				String url = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DATA));// 从文件夹中返回一个字符串数组保存所有的名字中的列的结果集，以便在他们的结果列。
				String name = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
				m.setTitle(title);
				m.setSinger(singer);
				m.setAlbum(album);
				m.setSize(size);
				m.setTime(time);
				m.setUrl(url);
				m.setName(name);
				lists.add(m);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}
}
