/** 
 * @author 作者 Chopper 
 * @version 创建时间：2017-4-12 上午10:07:14 
 * 类说明 
 */
package com.cl.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.csw.csw_desktop_yahao.RootCmd;


import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AuxLineService extends Service {

	private String TAG = "AuxLineService";
	private Context mContext;
	private   Timer timer = new Timer();  
	private TimerTask task;  
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		mContext = this;
		Log.d(TAG, "Aux服务开始");
		WifiMusicThread();
	}

	private void WifiMusicThread() {
		if(timer!=null){
			timer.cancel();
			timer = null ;
			timer = new Timer();  
		}
		if(task != null ){
			task.cancel();
			task = null;
		}
		task = new TimerTask() {  
		    @Override  
		    public void run() {  
		        // TODO Auto-generated method stub  
		    	if (!isTopActivy("com.csw.setaudio")) {
					Log.d(TAG, "AuxLineService关闭");
					new Thread(openAuxRunnable).start();
					task.cancel();
				}else{
					Log.d(TAG, "Aux已打开");
				}
		    }  
		};  
		timer.schedule(task, 2000, 1000); 
	}

	/**
	 * 打开aux的线程
	 */  
	Runnable openAuxRunnable = new Runnable() {

		@Override
		public void run() {   
			// TODO Auto-generated method stub
			
			
			String paramStringOpen = "adb root" + "\n" + "su" + "\n"
					+ "echo 'n' > /sys/codec-spk-ctl/spk-ctl" + "\n" + "exit"
					+ "\n" + "exit";
			if (RootCmd.haveRoot()) {
				if (RootCmd.execRootCmdSilent(paramStringOpen) == -1) {
					Log.d(TAG, "打开失败");
				} else {
					Log.d(TAG, "输出成功");
				}
			} else {
				Log.d("AuxBoot", "root失败");
			}
			/*Intent intent = new Intent(AUX_OPEN_ACTION);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.sendBroadcast(intent);*/
			Log.d(TAG, "openAuxRunnable 打开aux的线程");
		}

	};

	public boolean isTopActivy(String cmdName) {
		ActivityManager manager = (ActivityManager) mContext
				.getSystemService(mContext.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
		String cmpNameTemp = null;
		if (null != runningTaskInfos) {
			cmpNameTemp = (runningTaskInfos.get(0).topActivity.getPackageName())
					.toString();
			Log.e(TAG, "cmpname:" + cmdName);
		}
		if (null == cmpNameTemp)
			return false;
		return cmpNameTemp.equals(cmdName);
	}
}
