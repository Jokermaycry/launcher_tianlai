package com.csw.music;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;

import com.wyf.httpservice.MediaServer;

public class MusicList {
	/** */
	public static MediaServer mediaServer;
	private static String whichPathOwn;

	public static List<Music> getMusicData(Context context, String whichPathOw) {
		whichPathOwn = whichPathOw;
	/*	*//** *//*
		try {
			if (mediaServer == null) {
				mediaServer = new MediaServer(getLocalIpAddress(context));
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		List<Music> Musiclist = new ArrayList<Music>();
		/** 扫描手机文件 */
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		while (cursor.moveToNext()) {
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
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));// 从文件夹中返回一个字符串数组保存所有的名字中的列的结果集，以便在他们的结果列。
			String name = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			if (url.contains(whichPathOwn)) {

				m.setTitle(title);
				m.setSinger(singer);
				m.setAlbum(album);
				m.setSize(size);
				m.setTime(time);
				//m.setHttpurl("http://" + mediaServer.getAddress());
				m.setHttpurl(null);
				m.setUrl(url);
				m.setName(name);
				int dianIndex = name.lastIndexOf(".");// 点的索引
				String musicKz;// 扩展名，如.mp3
				if (dianIndex != -1) {
					musicKz = name.substring(dianIndex);// 扩展名，如.mp3
				} else {
					musicKz = "";// 可能存在没扩展名的文件
				}
				/*
				 * if (!musicKz.equals(".mp3") && !musicKz.equals(".MP3")) {
				 * continue; }
				 */
				m.setMusicKz(musicKz);
				//System.out.println("http://" + mediaServer.getAddress());
				Musiclist.add(m);
//				System.out.println(m.getTitle().toString());
			} else {
				continue;
			}
		}
		try {
			cursor.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Musiclist;
	}

	/**
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	private static InetAddress getLocalIpAddress(Context context)
			throws UnknownHostException {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService("wifi");
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return InetAddress.getByName(String.format("%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
				(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
	}
}
