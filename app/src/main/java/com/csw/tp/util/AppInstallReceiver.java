package com.csw.tp.util;

import java.util.ArrayList;
import java.util.List;

import com.csw.tp.bean.AppInfo;
import com.csw.tp.bean.GetAppInfo;
import com.csw.tp.cc_server.SocketServer;
import com.google.gson.Gson;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppInstallReceiver extends BroadcastReceiver {

	private final String ADD_APP = "android.intent.action.PACKAGE_ADDED";
	private final String REMOVE_APP = "android.intent.action.PACKAGE_REMOVED";
    private final String REPLACED_APP = "android.intent.action.PACKAGE_REPLACED";
	List<AppInfo> appInfos =new ArrayList<AppInfo>();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (ADD_APP.equals(action)) {
			String packageName = intent.getDataString();
			Log.i("AddApp", "安装了:" + packageName);
			getaddAPP(packageName, context);
		} else if (REMOVE_APP.equals(action)) {
			String packageName = intent.getDataString();
			Log.i("whw", "卸载了:" + packageName);
		}else if (REPLACED_APP.equals(action)) {
			String packageName = intent.getDataString();
			Log.i("whw", "替换了:" + packageName);
		}
	}
	
	private void getaddAPP(final String packageName,final Context context){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetAppInfo getappinfo = new GetAppInfo(context);
				appInfos= getappinfo.getAllAPK_add();
				for (int i = 0; i < appInfos.size(); i++) {
					if (packageName.substring(packageName.indexOf("package:")+8, packageName.length()).equals(appInfos.get(i).getPagename())){
						String jsonResult = "appname:"+appInfos.get(i).getAppLabel()+":appicon:"+appInfos.get(i).getAppIcon();
						jsonResult=jsonResult+":addapp";
						SocketServer.SendDataClient(jsonResult);
						Log.i("AddApp", "发送了安装的APP到手机助手" );
					}
				}
			}
		}).start();
	}

}
