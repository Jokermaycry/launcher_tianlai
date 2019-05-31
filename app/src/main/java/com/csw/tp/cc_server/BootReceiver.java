package com.csw.tp.cc_server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


/*
 * 开机自启动广播
 */
public class BootReceiver extends BroadcastReceiver {

	private static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";  //开机启动广播


	
	
	@Override
	public void onReceive(Context context, Intent intent) {	
			if (intent.getAction().equals(BOOT_ACTION)) {
				
				
//				Intent mBootIntent = new Intent(context, MainActivity.class);
//				mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(mBootIntent);
				System.out.println("接收到广播，准备启动");
				
			}
	}

}
