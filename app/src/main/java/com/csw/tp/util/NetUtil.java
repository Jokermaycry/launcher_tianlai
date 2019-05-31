package com.csw.tp.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.v4.net.ConnectivityManagerCompat;
/*
 * 关于网络的操作类
 */
public class NetUtil {
	public NetUtil(Context context) {
		super();
	}

	/*
	 * 检查当前WIFI是否连接，两层意思——是否连接，连接是不是WIFI
	 */

	public static boolean isWifiConnected(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context

		.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = cm.getActiveNetworkInfo();

		if (info != null && info.isConnected()

		&& ConnectivityManager.TYPE_WIFI == info.getType()) {

			return true;

		}

		return false;

	}

	/**
	 * 检查当前GPRS是否连接，两层意思——是否连接，连接是不是GPRS
	 * 
	 * @param context
	 * @return true表示当前网络处于连接状态，且是GPRS，否则返回false
	 */

	public static boolean isGprsConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()

		&& ConnectivityManager.TYPE_MOBILE == info.getType()) {
			return true;
		}
		return false;
	}

	/**
	 * 检查当前是否连接
	 * 
	 * @param context
	 * @return true表示当前网络处于连接状态，否则返回false
	 */

	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		}

		return false;

	}

	/**
	 * 对大数据传输时，需要调用该方法做出判断，如果流量敏感，应该提示用户
	 * 
	 * @param context
	 * @return true表示流量敏感，false表示不敏感
	 */

	/*public static boolean isActiveNetworkMetered(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context

		.getSystemService(Context.CONNECTIVITY_SERVICE);
		return ConnectivityManagerCompat.isActiveNetworkMetered(cm);

	}*/

	public static Intent registerReceiver(Context context,

	ConnectivityChangeReceiver receiver) {

		return context.registerReceiver(receiver,
				ConnectivityChangeReceiver.FILTER);

	}

	public static void unregisterReceiver(Context context,

	ConnectivityChangeReceiver receiver) {

		context.unregisterReceiver(receiver);

	}

	
	/*
	 * 下面是网络类型广播
	 */
	private final String youxian = "android.net.ethernet.ETHERNET_STATE_CHANGED";//Ethernet物理连接状态广播
	public static abstract class ConnectivityChangeReceiver extends

	BroadcastReceiver {

		public static final IntentFilter FILTER = new IntentFilter(

		ConnectivityManager.CONNECTIVITY_ACTION);

		@Override
		public final void onReceive(Context context, Intent intent) {

			ConnectivityManager cm = (ConnectivityManager) context

			.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo wifiInfo = cm

			.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//是否Wifi连接

			NetworkInfo gprsInfo = cm

			.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);//是否GPRS连接
			
			NetworkInfo networkInfo = cm
					.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);//是否网线连接

			// 判断是否是Connected事件

			boolean wifiConnected = false;

			boolean gprsConnected = false;

			if (wifiInfo != null && wifiInfo.isConnected()) {

				wifiConnected = true;

			}

			if (gprsInfo != null && gprsInfo.isConnected()) {
				gprsConnected = true;

			}

			if (wifiConnected || gprsConnected) {

				onConnected();

				return;

			}

			// 判断是否是Disconnected事件，注意：处于中间状态的事件不上报给应用！上报会影响体验

			boolean wifiDisconnected = false;

			boolean gprsDisconnected = false;

			if (wifiInfo == null || wifiInfo != null

			&& wifiInfo.getState() == State.DISCONNECTED) {

				wifiDisconnected = true;

			}

			if (gprsInfo == null || gprsInfo != null

			&& gprsInfo.getState() == State.DISCONNECTED) {

				gprsDisconnected = true;

			}

			if (wifiDisconnected && gprsDisconnected) {

				onDisconnected();

				return;

			}

		}

		protected abstract void onDisconnected();

		protected abstract void onConnected();
	}
	
	
	/**
	 * 检查 URL 是否有效
	 * @param url
	 * @return
	 */
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
	
	/**
	 * ping某个主机
	 * @param address
	 * @return
	 */
	public static boolean pingHost(String address) {
//		if(System.currentTimeMillis()-lastTime>3000)
//		result = checkSocket(Config.ServerIP, 8989, 2000);
//		lastTime = System.currentTimeMillis();
		return result;
	}
	/**
	 * 检查socket是否可连接
	 * @param ip
	 * @param port
	 * @param timout
	 * @return
	 */
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
