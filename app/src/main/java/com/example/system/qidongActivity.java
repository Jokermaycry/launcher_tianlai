package com.example.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.baidu.mobstat.StatService;
import com.zhongqin.tianlai.R;

import com.wyf.allapp.GetAppInfo;
import com.wyf.app.AppInfo;
import com.wyf.util.FileReadWriteUtil;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.appwidget.AppWidgetManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class qidongActivity extends Activity {
	private String bao;
	private String activityname;
	private Drawable mDrawable;
	public static int code = 10000;// 记录点击了第几个Item
	public static int code1 = 10001;// 是否按对话框标示
	private GridView listview = null;
	RelativeLayout apps_rel;
	private final int INIT_APPS = 0;
	private static ImageManager imageManager = new ImageManager();
	private GetAppInfo getAppInfo = new GetAppInfo(this);
	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INIT_APPS:

				main_app_init2();

				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.apps_list);
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_BOOT_COMPLETED);
		listview = (GridView) findViewById(R.id.grid);

		// 壁纸

		layRelativeLayout = (RelativeLayout) this.findViewById(R.id.apps_rel);

		int wallpaperIdCurrent = preferencesUtils.getsum(this, "wallpaperID");
		if (wallpaperIdCurrent != 0) {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_3));
		}

		init_apps();
		// 定位到全部应用第一去
		send_key();
	}

	Runnable runnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(10);
				send_key();
				mUIHandler.sendEmptyMessage(INIT_APPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	};

	void init_apps() {
		Thread thread = new Thread(runnable);
		thread.start();
	}


	int del_kuaijie_sel(String str) {
		if (str.indexOf("LXUI") != -1) {
			return 1;
		} else if (str.indexOf("多屏互动") != -1) {
			return 1;
		} else if (str.indexOf("test") != -1) {
			return 1;
		} else if (str.indexOf("电视桌面") != -1) {
			return 1;
		} else if (str.indexOf("LYUI") != -1) {
			return 1;
		}

		return 0;
	}

	public void send_key() {

		try {
			String keyCommand = "input keyevent " + KeyEvent.KEYCODE_DPAD_LEFT;
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(keyCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void main_app_init2() {

		BrowseApplicationInfoAdapter browseAppAdapter = new BrowseApplicationInfoAdapter(
				this, GetAppInfo.list);
		listview.setAdapter(browseAppAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				code1 = arg2;
				code = arg2;
				bao = GetAppInfo.list.get(arg2).getPkgName();
				activityname = GetAppInfo.list.get(arg2).getActivityName();
				mDrawable=GetAppInfo.list.get(arg2).getAppIcon();
				
				
				
				AlertDialog.Builder builder = new Builder(qidongActivity.this);
				builder.setTitle("提示");
				builder.setMessage("是否把当前应用设为开机启动");
				// 更新
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								code1 = 100;
//								SharedPreferencesUtils.setData(
//										qidongActivity.this, "bao", bao);
//								SharedPreferencesUtils.setData(
//										qidongActivity.this, "activityName",
//										activityname);
								System.out.println("执行保存开机apk信息操作");
								String internal_sdPath=FileReadWriteUtil.getSDPath(qidongActivity.this);//sd卡路径
								String fileDirName="baomingleiming";//文件夹名称
								String fileName="baomingleiming.txt";//文件名称
								boolean weatherHasFile=FileReadWriteUtil.isFileExist(internal_sdPath, fileDirName);
								if(weatherHasFile==false){
									System.out.println("文件夹不存在");
									FileReadWriteUtil.creatSDDir(internal_sdPath, fileDirName);
									//向文件中写数据
								}else{
									System.out.println("文件夹存在");
									if(FileReadWriteUtil.isFileExist(internal_sdPath+"/"+fileDirName,fileName)){
										System.out.println("如果文件存在，删除");
									    boolean deleteFlag=	new File(internal_sdPath+"/"+fileDirName+"/"+fileName).delete();
										System.out.println("删除"+deleteFlag); 
									}	
								}
							    File finalFileName=	FileReadWriteUtil.creatSDFile(internal_sdPath+"/"+fileDirName,fileName);
								String packageAndActivityName=bao+","+activityname;
								byte [] content=packageAndActivityName.getBytes();
								try {
									FileReadWriteUtil.writeFile(content, finalFileName.getPath());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									System.out.println("开机要启动的apk信息保存失败");
									e.printStackTrace();
								}
								System.out.println("开机要启动的apk信息保存成功");

								// SharedPreferencesUtils.setsum(qidongActivity.this,"code",code);
								Intent resultValue = new Intent();
								resultValue.putExtra("code", code1);
								setResult(RESULT_OK, resultValue);

								dialog.dismiss();
								qidongActivity.this.finish();
							}
						});
				// 稍后更新
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				Dialog noticeDialog = builder.create();
				noticeDialog.show();
			}
		});
	}

	RelativeLayout layRelativeLayout;
	SharedPreferencesUtils preferencesUtils;
}
