package com.csw.csw_desktop_yahao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.zhongqin.tianlai.R;
import com.szy.update.SilentUpdate;
import com.szy.update.update_main;
import com.weather.manager.WeatherManager;
import com.wyf.allapp.GetAppInfo;

import com.wyf.util.FileReadWriteUtil;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Welcome extends Activity implements Runnable {
	/*
	 * @Override protected void onResume() { // TODO Auto-generated method stub
	 * super.onResume();
	 * 
	 * }
	 */

	// 第二次进入时，可以通过handler跳转到桌面主界面
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Message msg1 = new Message();
		msg1.what = START_ACTIVITY;
		mHandler.sendMessage(msg1);
	}

	ImageView ivAnimView;
	private AnimationDrawable animationDrawable;
	private List<PackageInfo> packageInfos;

	/**
	 * �����û���װ����Ϣ
	 */
	public static List<PackageInfo> userPackageInfos;

	/**
	 * E�ֱ�Ӧ�ð���Ϣ
	 */
	public static List<PackageInfo> childPackageInfos;
	/**
	 * ��ϷӦ�ð���Ϣ
	 */
	public static List<PackageInfo> gamePackageInfos;
	/**
	 * ��ƵӦ�ð���Ϣ
	 */
	public static List<PackageInfo> videoPackageInfos;
	/**
	 * �����������Ϣ
	 */
//	public static String welcome_init = null;
	private static final int START_ACTIVITY = 0;
	private static final int START_ANIMATION = 1;
	private static final int START_QIDONG = 2;
	private SharedPreferencesUtils preferencesUtils;
//	private String qidong = "", qidongActivity = "";
	String packageName="";//要启动的应用程序包名
	String activityName="";//要启动应用程序的类名
	Message startAcitiyMsg = new Message();
	private ImageManager imageManager = new ImageManager();
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == START_ACTIVITY) {
				animationDrawable.stop();
				/*
				 * int change_subject = preferencesUtils.getsum(Welcome.this,
				 * "subject_ui");
				 */
				int change_subject = 0;
				Intent in = new Intent();
				if (change_subject == 0) {
					in.setClass(Welcome.this, MainActivity.class);
				}
//				if (change_subject == 1) {
//					in.setClass(Welcome.this, SecondMainUIActivity.class);
//				}
				startActivity(in);
				finish();
				System.gc();

			}
			if (msg.what == START_ANIMATION) {
				animationDrawable.setOneShot(true);
				animationDrawable.start();
			}
			if (msg.what == START_QIDONG) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(packageName, activityName));
				startActivity(intent);
				
			}
			super.handleMessage(msg);
		}

	};
	update_main update = new update_main(Welcome.this);
	public static int whether_updata;
	private GetAppInfo appInfo;
	HashMap<String, String> HashMap = new HashMap<String, String>();
	SilentUpdate silentUpdate = new SilentUpdate(this);
	private final int SILENTINSTALL = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.welcome2);
		appInfo = new GetAppInfo(this);
		ivAnimView = (ImageView) findViewById(R.id.ivAnimView);
		ivAnimView.setBackgroundResource(R.anim.frame_animation);
		Object backgroundObject = ivAnimView.getBackground();

		animationDrawable = (AnimationDrawable) backgroundObject;
		new Thread(this).start();
	}

	public void run() {
		//
		app_init();
		appInfo.getAllAPK_add();
		// image_load();
		Message msg = new Message();
		msg.what = START_ANIMATION;
		mHandler.sendMessage(msg);
		// init start
		MainActivity.welcome_init = "welcome";
		

		String internal_sdPath=FileReadWriteUtil.getSDPath(Welcome.this);//sd卡路径
		String fileDirName="baomingleiming";//文件夹名称
		String fileName="baomingleiming.txt";//文件名称
		
		boolean weatherHasFile=FileReadWriteUtil.isFileExist(internal_sdPath, fileDirName);
		if(weatherHasFile==false){
			System.out.println("文件夹不存在");
			FileReadWriteUtil.creatSDDir(internal_sdPath, fileDirName);
			FileReadWriteUtil.creatSDFile(internal_sdPath+"/"+fileDirName,fileName);
			//向文件中写数据
		}else{
			System.out.println("文件夹存在");
			if(FileReadWriteUtil.isFileExist(internal_sdPath+"/"+fileDirName,fileName)){
				System.out.println("如果文件存在");
//			    boolean deleteFlag=	new File(internal_sdPath+"/"+fileDirName+"/"+fileName).delete();
//				System.out.println("删除"+deleteFlag); 
				String fileContent=FileReadWriteUtil.readFileContent(internal_sdPath+"/"+fileDirName+"/"+fileName);
				if(!fileContent.equals("")&&fileContent!=null){
					try {
						packageName=fileContent.substring(0, fileContent.indexOf(","));//包名
						activityName=fileContent.substring(fileContent.indexOf(",")+1);//类名
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}	
		}
		
		try {
			Thread.sleep(1000);

//			// 获取存储的开机启动主类名
//			qidongActivity = preferencesUtils.getData(Welcome.this,
//					"activityName");
//			qidong = preferencesUtils.getData(Welcome.this, "bao");
			// 判断开机启动主类名是否为空
			if (packageName != null && activityName != null) {
				if (!packageName.equals("") && !activityName.equals("")) {

					startAcitiyMsg.what = START_QIDONG;
				}
			} else {
				startAcitiyMsg.what = START_ACTIVITY;
			}
			Intent intent = panduan_apps_to_list_all(packageName, activityName);
			if(intent==null){
				startAcitiyMsg.what = START_ACTIVITY;
			}
			mHandler.sendMessage(startAcitiyMsg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		/*if (NetCheck.checkNetWorkStatus(this)) {
			try{
		// 	update.getversionRun();
		// 	whether_updata = update.check_update();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			boolean result = SharedPreferencesUtils.getSplash(this, "savakey");
			if(!result){
//	         savaInfo();
			}
//			getrun();
			try {
				Thread.sleep(1000);

//				// 获取存储的开机启动主类名
//				qidongActivity = preferencesUtils.getData(Welcome.this,
//						"activityName");
//				qidong = preferencesUtils.getData(Welcome.this, "bao");
				// 判断开机启动主类名是否为空
				if (packageName != null && activityName != null) {
					if (!packageName.equals("") && !activityName.equals("")) {

						startAcitiyMsg.what = START_QIDONG;
					}
				} else {
					startAcitiyMsg.what = START_ACTIVITY;
				}
				Intent intent = panduan_apps_to_list_all(packageName, activityName);
				if(intent==null){
					startAcitiyMsg.what = START_ACTIVITY;
				}
				mHandler.sendMessage(startAcitiyMsg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			
			 * try { Thread.sleep(1000); Message startAcitiyMsg = new Message();
			 * startAcitiyMsg.what = START_ACTIVITY;
			 * mHandler.sendMessage(startAcitiyMsg); } catch
			 * (InterruptedException e) { e.printStackTrace(); }
			 
			try {
				Thread.sleep(1000);

				// 获取存储的开机启动主类名
				qidongActivity = preferencesUtils.getData(Welcome.this,
						"activityName");
				qidong = preferencesUtils.getData(Welcome.this, "bao");
				// 判断开机启动主类名是否为空
				if (qidongActivity != null && qidong != null) {
					if (!qidongActivity.equals("") && !qidong.equals("")) {

						startAcitiyMsg.what = START_QIDONG;
					}
				} else {
					startAcitiyMsg.what = START_ACTIVITY;
				}
				Intent intent = panduan_apps_to_list_all(qidong, qidongActivity);
				if(intent==null){
					startAcitiyMsg.what = START_ACTIVITY;
				}
				mHandler.sendMessage(startAcitiyMsg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}

	private void app_init() {
		boolean loading = preferencesUtils.getSplash(this, "app_whether");
		if (!loading) {
			// 游戏
			preferencesUtils.setData(this, "youxi_app1", "com.ireadygo.tvplay");
			preferencesUtils
					.setData(this, "youxi_app2", "cn.vszone.tv.gamebox");
			preferencesUtils.setData(this, "youxi_app3",
					"com.youjoy.strugglelandlord");
			preferencesUtils.setData(this, "youxi_app4", "com.youjoy.com");
			preferencesUtils.setData(this, "youxi_app5", "com.putaolab.ptgame");

			//
//			preferencesUtils.setData(this, "add_app1", "com.duomi.androidtv");
//			preferencesUtils.setData(this, "add_app1", "com.tencent.qqmusicpad");//QQ音乐
//			preferencesUtils.setData(this, "add_app2", "com.geniatech.iptv");
//
//			preferencesUtils.setData(this, "add_app3", "com.slanissue.tv.erge");
//			preferencesUtils.setData(this, "add_app4", "com.dangbeimarket");
//
//			preferencesUtils.setData(this, "add_app5", "com.ucbrowser.tv");
//
//			preferencesUtils.setData(this, "add_app6", "com.android.browser");
//			
//			preferencesUtils.setData(this, "add_app7", "com.guozi.mztj");

			// 直播

//			preferencesUtils
//					.setData(this, "zhibo_app1", "com.csw.dianshizhibo");

			preferencesUtils.setData(this, "zhibo_app1", "com.csw.dianshizhiboxiukong");
			preferencesUtils.setData(this, "zhibo_app2", "com.csw.huikanxiukong");
			
			preferencesUtils.setData(this, "zhibo_app3", "com.csw.dianshizhibo");

//			preferencesUtils.setData(this, "zhibo_app3", "com.csw.dianshizhibotaijie");
//			preferencesUtils.setData(this, "zhibo_app3", "com.csw.dianshizhiboxiukong");
//			preferencesUtils.setData(this, "zhibo_app3", "com.csw.huikanxiukong");
			preferencesUtils.setData(this, "zhibo_app4", "hdpfans.com");

			preferencesUtils.setSplash(this, "app_whether", true);
		}
	}



	 /**
		 * 所有的程序，都扫描到列表里。判断有没有这个apk
		 */
		public Intent panduan_apps_to_list_all(String baoName, String zhuleiName) {
			for (int i = 0; i < GetAppInfo.list.size(); i++) {

				String baoTemp=GetAppInfo.list.get(i).getPkgName();
				String activityTemp=GetAppInfo.list.get(i).getActivityName();
				// icon = reInfo.loadIcon(pm); // 获得应用程序图标
				if (baoTemp.equals(baoName)
						&& activityTemp.equals(zhuleiName)) {
					// 为应用程序的启动Activity 准备Intent
					return GetAppInfo.list.get(i).getIntent();
				}
			}
			return null;
		}
}
