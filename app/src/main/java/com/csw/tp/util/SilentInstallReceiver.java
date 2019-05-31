package com.csw.tp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SilentInstallReceiver extends BroadcastReceiver{

	public static String apkSileninstall;
	
	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		apkSileninstall = intent.getStringExtra("apkWhether");
	}

}
