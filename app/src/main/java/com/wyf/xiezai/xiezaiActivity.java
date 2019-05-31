package com.wyf.xiezai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wyf.app.AppInfo;
import com.wyf.app.BrowseApplicationInfoAdapter_zhanshi;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import com.baidu.mobstat.StatService;
import com.csw.csw_desktop_yahao.MainActivity;
import com.zhongqin.tianlai.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

public class xiezaiActivity extends Activity implements OnItemClickListener {

	private GridView listview = null;

	private List<AppInfo> mlistAppInfo = null;

	private Handler handler;

	RelativeLayout xiezaiActivitylayout;
	private SharedPreferencesUtils preferencesUtils;
	private   ImageManager imageManager = new ImageManager();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.xiezai_main);
		/*
		 * 壁纸
		 */
		xiezaiActivitylayout = (RelativeLayout) this
				.findViewById(R.id.xiezaiActivitylayout);
		int wallpaperIdCurrent = preferencesUtils.getsum(xiezaiActivity.this,
				"wallpaperID");
		if (wallpaperIdCurrent != 0) {
			// allappActivity.setBackgroundResource(wallpaperIdCurrent);
			xiezaiActivitylayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			xiezaiActivitylayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_3));
		}

		listview = (GridView) findViewById(R.id.grid);
		// 初始化handler
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) // handler接收到相关的消息后
				{

					main_app();
				} else if (msg.what == 2) {

				}

			}
		};

		main_app();
	}

	public void main_app() {

		mlistAppInfo = new ArrayList<AppInfo>();
		// 到时候在启动的时候初始化，通过获取，拿到数据。其实也还好啦！这样才能获取最新的数据
		queryAppInfo(); // 查询所有应用程序信息
		// 将程序装入到列表里
		BrowseApplicationInfoAdapter_quanbuyingyong browseAppAdapter = new BrowseApplicationInfoAdapter_quanbuyingyong(
				this, mlistAppInfo);
		listview.setAdapter(browseAppAdapter);
		listview.setOnItemClickListener(this);
	}

	// 获得所有启动Activity的信息，类似于Launch界面
	public void queryAppInfo() {
		Drawable icon = null;
		PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 通过查询，获得所有ResolveInfo对象.
		// wjz GET_UNINSTALLED_PACKAGES:所有的程序吧，包括SD卡
		// MATCH_DEFAULT_ONLY：只是系统程序
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);
		// 调用系统排序 ， 根据name排序
		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));
		if (mlistAppInfo != null) {
			mlistAppInfo.clear();
			for (ResolveInfo reInfo : resolveInfos) {
				String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
				String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
				String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
				if (del_kuaijie_sel(appLabel) == 0)// 去掉泰云桌面
				{
					icon = reInfo.loadIcon(pm); // 获得应用程序图标
					// 为应用程序的启动Activity 准备Intent
					Intent launchIntent = new Intent();
					launchIntent.setComponent(new ComponentName(pkgName,
							activityName));
					// 创建一个AppInfo对象，并赋值
					AppInfo appInfo = new AppInfo();
					appInfo.setAppLabel(appLabel);
					appInfo.setPkgName(pkgName);
					appInfo.setAppIcon(icon);
					appInfo.setIntent(launchIntent);
					mlistAppInfo.add(appInfo); // 添加至列表中
				}
			}
		}
		pm = null;
		resolveInfos.clear();
	}

	int del_kuaijie_sel(String str) {
		  if (str.equals("LXUI")) {
			return 1;
		} else if (str.equals("电视回看")) {
			return 1;
		} else if (str.equals("影视点播")) {
			return 1;
		} else if (str.equals("多屏互动")) {
			return 1;
		} else if (str.indexOf("test") != -1) {
			return 1;
		} else if (str.equals("电视桌面")) {
			return 1;
		} else if (str.equals("wifi")) {
			return 1;
		} else if (str.indexOf("CSWUI") != -1) {
			return 1;
		} else if (str.indexOf("系统重启") != -1) {
			return 1;
		} else if (str.indexOf("泰捷视频") != -1) {
			return 1;
		} else if (str.equals("本地多媒体")) {
			return 1;
		} else if (str.equals("应用市场")) {
			return 1;
		} else if (str.equals("本地多媒体")) {
			return 1;
		} else if (str.equals("悟空遥控")) {
			return 1;
		} else if (str.equals("电视回播")) {
			return 1;
		} else if (str.equals("升级中心")) {
			return 1;
		}

		return 0;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// Intent intent = mlistAppInfo.get(arg2).getIntent();
		// startActivity(intent);

		String pkg_name1 = mlistAppInfo.get(arg2).getPkgName();
		if (pkg_name1 != null) {
			pkg_name1 = "package:" + pkg_name1;
			Uri packageURI = Uri.parse(pkg_name1);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
					packageURI);
			startActivity(uninstallIntent);
			runnable_init();
		}
	}

	void runnable_init() {

		// 新建一个线程，检查wifi的状态
		Runnable runnable1 = new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);// 等待系统调度，显示欢迎界面
					handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		Thread thread1 = new Thread(runnable1);
		thread1.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}

}