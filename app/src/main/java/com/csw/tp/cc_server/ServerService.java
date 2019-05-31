package com.csw.tp.cc_server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import com.csw.tp.bean.DataEntity;
import com.csw.tp.bean.GetAppInfo;
import com.csw.tp.util.GetIp_MacUtil;
import com.csw.tp.util.NetUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

public class ServerService extends Service {

	String serverIp = null;
	private final int serverPort = 10087;
	AboutServer aboutServer;

	private Context mContext;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("这里是ServerService的onCreate方法");
		Toast.makeText(this, "Server端建立", 2000).show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		System.out.println("这里是ServerService的onStart方法");
		GetAppInfo appInfo = new GetAppInfo(this);
		appInfo.getAllAPK_add();
		DataEntity.currentContext = this;
		ServerThread.Maincontext = this;
		ServerThread.IntentMaincontext = this;
		mContext=this;
		if (NetUtil.isConnected(ServerService.this) == true) {// 如果有网络
			serverIp = GetIp_MacUtil.getNetIPAddress(true, ServerService.this);
			myHandler.sendEmptyMessage(CREATE_TCP_SERVER);
			System.out.println("有网络，将会创建一个TCPServer");
		} else {
			serverIp = GetIp_MacUtil.getLocalHostIp();
			myHandler.sendEmptyMessage(CREATE_TCP_SERVER);
			Log.d("局域网或没有网络", "局域网或没有网络");
			System.out.println("没网络，将会创建一个TCPServer");
		}
		acquireWakeLock();
//		myHandler.sendEmptyMessage(1);
		
		sendip();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private final int CREATE_TCP_SERVER = 0;
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				try {
					aboutServer = new AboutServer(serverPort);
					if (SocketServer.serverSocket == null) {
						aboutServer.creatTcpServer();// 开始创建TCP SERVER
						Log.d("创建TCP Server连接", "以前没有连接存在");
					} else {
						if (SocketServer.serverSocket.isClosed()) {
							aboutServer.creatTcpServer();// 开始创建TCP SERVER
							Log.d("创建TCP Server连接", "以前有连接存在，但已经关闭");
						} else {
							Log.d("创建TCP Server连接", "连接已经存在");
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case 1:
				weatherMoLiAppRun();
				System.out.println("检测魔力视频的线程开启");
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 检测当前运行的app是否是魔力视频
	 */
	private void weatherMoLiAppRun() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					isAppRunning("com.molitv.android");
				}
			}
		});
		thread.start();
	}

	// "com.molitv.android;com.molitv.android.activity.LauncherActivity"魔力视频包名类名

	private boolean isAppRunning(String packageName) {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		if (list == null)
			return false;
		for (RunningTaskInfo info : list) {
			if (info == null)
				continue;
			Log.d("aa",
					"info.topActivity.getPackageName()="+ info.topActivity.getPackageName());
			
			String currentAppPackageName=info.topActivity.getPackageName();
			String currentBasePackageName=info.baseActivity.getPackageName();
			
			if (currentAppPackageName.equals(packageName)&& currentBasePackageName.equals(packageName)) {
				//stopMusic();
				
				Log.i("Order from 魔力视频打开了","魔力视频打开了");
				return true;
			}
			
			if(currentAppPackageName.equals("com.iflytek.xiri")&&currentBasePackageName.equals("com.iflytek.xiri")){
				
				//stopMusic();
				System.out.println("讯飞语音打开了");
			}
			
          if(currentAppPackageName.equals("com.gongjin.cradio")&&currentBasePackageName.equals("com.gongjin.cradio")){
				
				//stopMusic();
				System.out.println("龙卷风收音机打开了");
			}
		}
		return false;
	}
	
//	private void stopMusic(){
//		if (MainActivity.mMediaPlayer != null) {
//			if (MainActivity.mMediaPlayer.isPlaying()) {
//				MainActivity.mMediaPlayer.pause();
////				MainActivity.mMediaPlayer.release();
//				System.out.println("暂停音乐播放器的音乐");
//			}
//		}
				
//		Intent mIntent=new Intent(MainActivity.CLIENT_ACTION_NAME);
//		mIntent.putExtra("action_action", "finish_app"); 
//		mContext.sendBroadcast(mIntent); 

	//}

	WakeLock wakeLock = null;  
    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行  
    private void acquireWakeLock()  
    {  
        if (null == wakeLock)  
        {  
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);  
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");  
            if (null != wakeLock)  
            {  
            	System.out.println("执行CPU唤醒");
                wakeLock.acquire();  
            }  
        }  
    }  
      
    //释放设备电源锁  
    private void releaseWakeLock()  
    {  
        if (null != wakeLock)  
        {  
            wakeLock.release();  
            wakeLock = null;  
        }  
    }  
	
	
     /**
      * UDP 向服务端发送IP
      * **/
     private void sendip(){
		System.out.println("向服务端发送IP");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DatagramSocket socket = null;
				//创建DatagramSocket对象并指定一个端口号，注意，如果客户端需要接收服务器的返回数据,
				//还需要使用这个端口号来receive，所以一定要记住
				if (NetUtil.isConnected(mContext)) {
				try {
					socket = new DatagramSocket(43708);
				} catch (SocketException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				//使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址  
				InetAddress serverAddress = null;
				try {
					serverAddress = InetAddress.getByName("255.255.255.255");
				} catch (UnknownHostException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//String str = GetIp_MacUtil.getNetIPAddress(true,ServerService.this);//设置要发送的报文  
				byte data[] = serverIp.getBytes();//把字符串str字符串转换为字节数组  
				//创建一个DatagramPacket对象，用于发送数据。  
				//参数一：要发送的数据  参数二：数据的长度  参数三：服务端的网络地址  参数四：服务器端端口号 
				DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,43708);  
				
				while (true) {
				
					try {
						if (NetUtil.isConnected(mContext)) {
							if (socket!=null) {
								socket.send(packet);
							}
							
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}//把数据发送到服务端。  
					
                 try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
				}
			}
		}).start();
		
	}
	
	
    
    
}
