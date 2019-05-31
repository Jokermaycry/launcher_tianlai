package com.csw.tp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csw.tp.bean.DataEntity;

import com.csw.tp.cc_server.ServerService;
import com.csw.tp.cc_server.SocketServer;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

/*
 * 检测网络状态广播
 */
public class CheckNetReceiver extends BroadcastReceiver {
	
	private Context context;
	
	public static Context UpdateContext=null;//用于更新的context
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 String action = intent.getAction();
         if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
        	 
    
        	 this.context=context;
    		 ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	     NetworkInfo networkinfo = cm.getActiveNetworkInfo();
    	     if(networkinfo!=null&&networkinfo.isConnected()){//如果联网状态
    	    	 
//    	    	 checkNetHandler.sendEmptyMessage(CONNECT);
    	     }else{//如果断网状态
    	//    	 checkNetHandler.sendEmptyMessage(BREAK);
    	     }
    	     Log.d("检测网络状态广播", "广播开启");
//    	               return;
         }else{
        	 return;
         }
		
		
	}
   
	private final int CONNECT=1;
	private final int BREAK=2;
	private Handler checkNetHandler=new Handler(){

		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 1:		
				Log.d("检测到网络打开", "检测到网络打开");
	 				Intent netintent=new Intent(context,ServerService.class);
	 				netintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 				context.startService(netintent);
	 				
				break;
			case 2:
				//关闭TCP Server连接
				if (SocketServer.serverSocket != null) {
					try {
						SocketServer.serverSocket.close();
						SocketServer.serverSocket=null;
						
						Log.d("检测到网络断开", "TcpServer关闭");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.d("检测到网络断开，关闭TCP Server", "未知错误，关闭失败");
					}
				}else{
					Log.d("检测到网络断开", "TcpServer已经关闭");
				}
				
				SocketServer.socketList.clear();//清除当前连接的客户端
				break;
			default:
					break;
			}
			super.handleMessage(msg);
		}
		
	};

}
