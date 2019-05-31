/** 
 * @author 作者 Chopper 
 * @version 创建时间：2017-7-11 下午3:47:25 
 * 类说明 
 */
package com.wyf.util;

import java.util.Timer;
import java.util.TimerTask;

import com.csw.newfragment.HomeFragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class TimerPingbaoUtil {

	public static Timer mDreamTimer = new Timer();
	public static TimerTask mDreamTimerTask;
	/**
	 * 判断屏保是否执行
	 */
	public boolean wetherDreamStart = false;

	public static void CancelPingbaoTimer() {
		if (mDreamTimer != null) {
			mDreamTimer.cancel();
			mDreamTimer = null;
		}
		if (mDreamTimerTask != null) {
			mDreamTimerTask.cancel();
			mDreamTimerTask = null;
		}
		Log.i("onStart()", "cancel------------------------");
	}

	public static void StartPingbaoTimer(final Context mContext) {
		if (mContext == null) {
			return;
		}
		if (mDreamTimer == null) {
			mDreamTimer = new Timer();
		}
		if (mDreamTimerTask == null) {
			mDreamTimerTask = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if(HomeFragment.setDreamPlay.equals("0")){
							HomeFragment.setDreamPlay="1";
						}
						Intent _Intent = new Intent();
						ComponentName _ComponentName = new ComponentName(
								"com.android.setdream",
								"com.android.setdream.MainActivity");
						_Intent.setComponent(_ComponentName);
						_Intent.putExtra("which_come", "music_palyer");
						_Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(_Intent);
						Log.i("onStart()", "执行屏保------------");
						
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.i("onStart()", "Exception------------");
						e.printStackTrace();
					}
				}

			};
		}
		if (mDreamTimerTask != null && mDreamTimer != null) {
			mDreamTimer.schedule(mDreamTimerTask, 6000*10*20);
		}
	}

}
