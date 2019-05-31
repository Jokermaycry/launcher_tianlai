package com.wyf.allapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppInstallReceiver extends BroadcastReceiver {

	private GetAppInfo appInfo;
	
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		appInfo = new GetAppInfo(context);
		appInfo.getAllAPK_add();
     System.out.println("安装，卸载广播");
	}

}
