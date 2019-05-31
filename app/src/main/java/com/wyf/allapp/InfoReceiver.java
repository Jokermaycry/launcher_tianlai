package com.wyf.allapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InfoReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(null,"intent" + intent);   
        String message = intent.getStringExtra("author");   
        System.out.println(message + "88888888888888888888888888888888888888");
        Log.i(null,message);   
	}

}
