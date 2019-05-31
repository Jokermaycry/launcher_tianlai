package com.csw.csw_desktop_yahao;



import java.io.IOException;

import com.csw.tp.cc_server.ServerService;
import com.csw.tp.cc_server.SocketServer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class CheckNetReceiver extends BroadcastReceiver {

	
	private Context mContext;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		 String action = intent.getAction();
         if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
        	 
    
        	 this.mContext=context;
    		 ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	     NetworkInfo networkinfo = cm.getActiveNetworkInfo();
    	     if(networkinfo!=null&&networkinfo.isConnected()){//如果联网状态
    	    	 checkNetHandler.sendEmptyMessage(CONNECT);
    	     }else{//如果断网状态
    	    	 checkNetHandler.sendEmptyMessage(BREAK);
    	     }
    	     Log.d("检测网络状态广播", "广播开启");
//    	               return;
         }else{
        	 return;
         }
		
		
	}
	
	private final int CONNECT=1;
	private final int BREAK=2;
	
	Handler checkNetHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch(msg.what){
			case CONNECT:
				
				Intent mIntent = new Intent(
						MainActivity.CHECKNET_ACTION_NAME);
				if (mContext != null) {
					Log.d("检测网络狀態", "網絡已連接，發定位天氣廣播");
					mContext.sendBroadcast(mIntent);
				}
				/*Intent netintent=new Intent(mContext,ServerService.class);
 				netintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 				mContext.startService(netintent);*/
				
				break;
			case BREAK:
				
				Log.d("检测网络狀態", "網絡已斷開");
				//关闭TCP Server连接
				/*if (SocketServer.serverSocket != null) {
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
*/				break;
			default:
				break;
			
			}
		};
	};

}
