package com.szy.update;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetCheck {
	/**
	 * �����������״̬��Monitor network connections (Wi-Vi, GPRS, UMTS, etc.)
	 * @param context
	 * @return
	 */
	public static boolean checkNetWorkStatus(Context context) {
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(null==cm)
			return false;
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public static boolean checkURL(String url) {
		boolean value = false;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			int code = conn.getResponseCode();
			if (code != 200) {
				value = false;
			} else {
				value = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
//	/**
//	 * ���ĳ�������Ƿ��������
//	 * @return
//	 */
//	public static boolean checkNetwokStatus(String address){
//		String host = address;
//		int timeOut = 1500; //��ʱӦ����3������
//		try {
//			return InetAddress.getByName(host).isReachable(timeOut);
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//			return false;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

	public static boolean pingHost(String address) {
//		if(System.currentTimeMillis()-lastTime>3000)
//		result = checkSocket(Config.ServerIP, 8989, 2000);
//		lastTime = System.currentTimeMillis();
		return result;
	}

	public static boolean checkSocket(String ip,int port,int timout){
		boolean result  =false;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), timout);
			if(socket!=null)
				result =  true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	public static boolean result = false;
	public static long lastTime = 0;
}
