package com.example.system;


import java.util.ArrayList;
import java.util.HashMap;

import com.cl.rkm_hdmi_display.hdmi_Activity;
import com.zhongqin.tianlai.R;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.DisplayOutputManager;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@SuppressLint("NewApi")
public class ShowSetActivity3288 extends Activity {
    
	
/*	private Button resolution_ratio;
	private Button screen;
	private TextView resolution_text;
	private TextView screen_text;
	*/
	
	AlertDialog.Builder builder;
	private View mGameView;
//	private DisplayManagerAw mDisplayManagerAw;
	/*************************************************************************************************************/

	
	private SharedPreferencesUtils preferencesUtils;
	private   ImageManager imageManager = new ImageManager();
	Button selectWallpaperBtn;// 选择壁纸
	RelativeLayout layRelativeLayout;
/***********************************************************************3288****************/
    private DisplayOutputManager mDisplayManagement = null;
	
	private int mMainDisplay_last = -1;
	private int mMainDisplay_set = -1;
	private String mMainMode_last = null;
	private String mMainMode_set = null;
	private static final String TAG = "ShowSetActivity3288";
	CharSequence[] ModeEntries;//显示的分辩率
	CharSequence[] ModeEntryValues;// 索引，没用上
	private static int whichItem = 0;// 第一次对话框默认选的
    private static int whichItemtemp=0;//临时存放对话框默认选中项

	

	
	private static final int FALLBACK_DISPLAY_MODE_TIMEOUT = 10;
	/******************************************************************3288*********************/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.showset_main);
		/*resolution_ratio = (Button) findViewById(R.id.but_show_item_1);
		screen = (Button) findViewById(R.id.but_show_item_3);

		resolution_text = (TextView) findViewById(R.id.show_item_text1);
		screen_text = (TextView) findViewById(R.id.show_item_text2);*/
		// 壁纸
				layRelativeLayout = (RelativeLayout) this.findViewById(R.id.showset_r2);
		
		if (witch == 1280) {

		} else {
			get_resolution();
		}
	
		/***************************************************************************/
		

		/**************************************************************************/
		// 屏幕大小


//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		// resolution_text.setText("屏幕分辨率:" + displayMetrics.widthPixels + "X"
//		// + displayMetrics.heightPixels);
//
//		String mHdmiValue = preferencesUtils.getData(this, "hdmi_last");
//		Log.d("ShowSetActivity3288", mHdmiValue);
//		if (mHdmiValue == null) {
//			resolution_text.setText("HDMI " + displayMetrics.widthPixels + " "
//					+ displayMetrics.heightPixels + "P");
//		} else {
//			resolution_text.setText("HDMI "+mHdmiValue);
//		}
//		int screen_value = preferencesUtils.getsum(this, "screen_valuea20");
//		if (screen_value == 0) {
		//	screen_text.setText("设置屏幕大小");
	//		screen_text.setText("设置屏幕大小");
			// mSeekBar.setProgress(95);
//		} else {
//			screen_text.setText(screen_value + "%");
//		}
/***************************************************************************************3288*********/
		/*
		try {
			mDisplayManagement = new DisplayOutputManager();
		} catch (RemoteException doe) {

		}
		int[] main_display = mDisplayManagement
				.getIfaceList(mDisplayManagement.MAIN_DISPLAY);
		if (main_display == null) {
			Log.e(TAG, "Can not get main display interface list");
			return;
		}
		
		int curIface = mDisplayManagement
				.getCurrentInterface(mDisplayManagement.MAIN_DISPLAY);
		mMainDisplay_last = curIface;

		SetModeList(mDisplayManagement.MAIN_DISPLAY, curIface);
		String mode = mDisplayManagement.getCurrentMode(
				mDisplayManagement.MAIN_DISPLAY, curIface);*/

		//added 11.21
//		String mHdmiValue = preferencesUtils.getData(this, "hdmi_last");
//		Log.d("ShowSetActivity3288", mHdmiValue);
//		if (mHdmiValue == null) {
	//		resolution_text.setText("HDMI " + mode);
		//	resolution_text.setText("HDMI " + mode);
//		} else {
//			resolution_text.setText("HDMI "+mHdmiValue);
//		}
		
	/*	
		/*
		if (savedInstanceState != null) {
			String saved_mode_last = savedInstanceState.getString(
					"main_mode_last", null);
			String saved_mode_set = savedInstanceState.getString(
					"main_mode_set", null);
			Log.d(TAG, "get savedInstanceState mainmodelast=" + saved_mode_last
					+ ",mainmodeset=" + saved_mode_set);
			if (saved_mode_last != null && saved_mode_set != null) {
				// mMainModeList.setValue(saved_mode_last);
				mMainMode_last = saved_mode_last;
				mMainDisplay_set = mMainDisplay_last;
				mMainMode_set = saved_mode_set;
			}
		} else if (mode != null) {
			// mMainModeList.setValue(mode);
			mMainMode_last = mode;
			mMainDisplay_set = mMainDisplay_last;
			mMainMode_set = mMainMode_last;
		}*/
			
/***************************************************************3288******************************8/
		/*
		 * 分辨率设置
		 */
		/*resolution_ratio.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Builder b = new AlertDialog.Builder(ShowSetActivity3288.this);
				b.setTitle("分辨率设置");
				b.setSingleChoiceItems(ModeEntries, whichItem,
						new DialogInterface.OnClickListener() {

							@SuppressWarnings("unchecked")
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								whichItem = which;
								String mode = (String) ModeEntries[whichItem];
								// mMainModeList.setValue(mode);
								mMainMode_set = mode;
								mMainDisplay_last = mDisplayManagement
										.getCurrentInterface(mDisplayManagement.MAIN_DISPLAY);
								Log.d("display", String.valueOf(mMainDisplay_last));
								if ((mMainDisplay_set != mMainDisplay_last)
										|| (mMainMode_last
												.equals(mMainMode_set) == false)) {
									if (mMainDisplay_set != mMainDisplay_last) {
										mDisplayManagement
												.setInterface(
														mDisplayManagement.MAIN_DISPLAY,
														mMainDisplay_last,
														false);
										
									} else{
										
									}
										
									mDisplayManagement.setMode(
											mDisplayManagement.MAIN_DISPLAY,
											mMainDisplay_set, mMainMode_set);
									mDisplayManagement.setInterface(
											mDisplayManagement.MAIN_DISPLAY,
											mMainDisplay_set, true);
									// showDialog(DIALOG_ID_RECOVER);
								}
								
								
								String str = "在 10 秒之后将会回到原来的设置\n请问是否保存当前的修改";
								final AlertDialog dialog2 = new AlertDialog.Builder(
										ShowSetActivity3288.this)
										.setTitle("修改确认")
										.setMessage(
												String.format(
														str,
														Integer.toString(FALLBACK_DISPLAY_MODE_TIMEOUT)))
										.setPositiveButton("保存", listener)
										.setNegativeButton("取消", listener)
										.create();
								dialog2.show();

								new AsyncTask() {
									@Override
									protected Object doInBackground(
											Object... arg0) {
										int time = FALLBACK_DISPLAY_MODE_TIMEOUT;
										while (time >= 0 && dialog2.isShowing()) {
											publishProgress(time);
											try {
												Thread.sleep(1000);
											} catch (Exception e) {
											}
											time--;
										}
										return null;
									}

									@Override
									protected void onPostExecute(Object result) {
										super.onPostExecute(result);
										if (dialog2.isShowing()) {
											// DispList.DispFormat item = null;
											if (mMainDisplay_last == -1) {
					

											} else {
												RestoreDisplaySetting();
											}
											dialog2.dismiss();
											
										}
									}

									@Override
									protected void onProgressUpdate(
											Object... values) {
										super.onProgressUpdate(values);
										int time = (Integer) values[0];
										String str = "在 " + time
												+ " 之后将会回到原来的设置\n请问是否保存当前的修改";
										dialog2.setMessage(String.format(str,
												Integer.toString(time)));
									}

								}.execute();

								// currentDisplayMetricsTemp = entries[which];

							}
							//
						});
				b.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				//
				Dialog dialog = b.create();
				dialog.show();

				Intent intentqidong = new Intent(ShowSetActivity3288.this,
						hdmi_Activity.class);
				//startActivity(intentqidong);
				startActivityForResult(intentqidong, 1);
				
			}
		});
		//屏幕大小
		screen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ShowSetActivity3288.this,ScreenScaleActivity.class);
				startActivity(intent);
			}
		});
*/
		selectWallpaperBtn = (Button) this.findViewById(R.id.wallpaperBtn);
		selectWallpaperBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ShowSetActivity3288.this,
						WallpaperActivity.class);
				startActivity(intent);
			}
		});
	}

	
	
	
	@SuppressLint("NewApi")
	private void get_resolution() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																		// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		Log.i(TAG, "手机屏幕分辨率:" + displayMetrics.widthPixels + "X"
				+ displayMetrics.heightPixels);
		Log.i(TAG, "密度:" + displayMetrics.density);
		Log.i(TAG, "使用每英寸的像素点来显示密度:" + displayMetrics.densityDpi);
		Log.i(TAG, displayMetrics.xdpi + "," + displayMetrics.ydpi);
		Log.i(TAG, displayMetrics.scaledDensity + "");
		// 获取分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 给常量类中的屏幕高和宽赋值
		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		Window window = getWindow();
		LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
		windowLayoutParams.width = (int) witch; // 宽度设置为屏幕的0.95
		windowLayoutParams.height = (int) height; // 高度设置为屏幕的0.6
		// windowLayoutParams.alpha = 0.5f;// 设置透明度
		Log.i(TAG, windowLayoutParams.width + "");
		Log.i(TAG, windowLayoutParams.height + "");
		// myHandler.sendEmptyMessage(1);
		// new Thread(new GameThread()).start();
	}

	

	private static int witch = 1280;
	private static int height = 720;

	private void setDisplayPercent(int value) {
//		mDisplayManagerAw.setDisplayAreaPercent(0, value);
	}

	Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mGameView = new View(ShowSetActivity3288.this);
				mGameView.invalidate(); // 刷新界面
				break;
			case 2:
				try {
					mDisplayManagement = new DisplayOutputManager();
				} catch (RemoteException doe) {

				}
				int[] main_display = mDisplayManagement
						.getIfaceList(mDisplayManagement.MAIN_DISPLAY);
				if (main_display == null) {
					Log.e(TAG, "Can not get main display interface list");
					return;
				}

				int curIface = mDisplayManagement
						.getCurrentInterface(mDisplayManagement.MAIN_DISPLAY);
				mMainDisplay_last = curIface;

				//SetModeList(mDisplayManagement.MAIN_DISPLAY, curIface);
				String mode = mDisplayManagement.getCurrentMode(
						mDisplayManagement.MAIN_DISPLAY, curIface);
				
			//	resolution_text.setText("HDMI " + mode);
				
				break;
			}
			super.handleMessage(msg);
		}
	};

	class GameThread implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				Message message = new Message();
				message.what = 1;
				// 发送消息
				ShowSetActivity3288.this.myHandler.sendMessage(message);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int wallpaperIdCurrent = preferencesUtils.getsum(ShowSetActivity3288.this,
				"wallpaperID");
		if (wallpaperIdCurrent != 0) {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_1));
		}
	}
	
	
	/****************************************3288********down****************************************/
	private void SetModeList(int display, int iface) {

		Log.d(TAG, "SetModeList display " + display + " iface " + iface);

		String[] modelist = mDisplayManagement.getModeList(display, iface);
		String cuMode = mDisplayManagement.getCurrentMode(
				display, iface);
		
		ModeEntries = new CharSequence[modelist.length];
		ModeEntryValues = new CharSequence[modelist.length];

		for (int i = 0; i < modelist.length; i++) {
			ModeEntries[i] = modelist[i];
			String modeEn=ModeEntries[i].toString();
			if(cuMode.equals(modeEn)){
				whichItem=i;//列表默认选中项
				whichItemtemp=i;//save it
			}
			if (iface == mDisplayManagement.DISPLAY_IFACE_TV) {
				String mode = modelist[i];
				if (mode.equals("720x576i-50")) {
					ModeEntries[i] = "CVBS: PAL";
				} else if (mode.equals("720x480i-60")) {
					ModeEntries[i] = "CVBS: NTSC";
				} else {
					ModeEntries[i] = "YPbPr: " + modelist[i];
				}

			}

			ModeEntryValues[i] = modelist[i];
		}

	}

	private void RestoreDisplaySetting() {
		if ((mMainDisplay_set != mMainDisplay_last)
				|| (mMainMode_last.equals(mMainMode_set) == false)) {
			if (mMainDisplay_set != mMainDisplay_last) {
				mDisplayManagement.setInterface(
						mDisplayManagement.MAIN_DISPLAY, mMainDisplay_set,
						false);
				SetModeList(mDisplayManagement.MAIN_DISPLAY, mMainDisplay_last);
			}
			// mMainModeList.setValue(mMainMode_last);
			mDisplayManagement.setMode(mDisplayManagement.MAIN_DISPLAY,
					mMainDisplay_last, mMainMode_last);
			mDisplayManagement.setInterface(mDisplayManagement.MAIN_DISPLAY,
					mMainDisplay_last, true);
			mMainDisplay_set = mMainDisplay_last;
			mMainMode_set = mMainMode_last;
			whichItem=whichItemtemp;
		}

	}

	
	// 倒计时点击事件
	 
	android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int btn) {
			if (btn == AlertDialog.BUTTON_POSITIVE) {
				 mMainMode_last=mMainMode_set;
				 mMainDisplay_last=mMainDisplay_set;
//				 preferencesUtils.setData(ShowSetActivity3288.this, "hdmi_last", mMainMode_last);//save it
			} else if (btn == AlertDialog.BUTTON_NEGATIVE) {
				whichItem=whichItemtemp;
				if (mMainDisplay_last == -1) {
                    
				} else {
					dialog.dismiss();
					dialog = null;
					RestoreDisplaySetting();

				}
			}
		}

	};
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	
	    	if(resultCode == RESULT_OK){
	    	//	mMainMode_last = SharedPreferencesUtils.getData(ShowSetActivity3288.this, "modeValue");
	    		Message message = new Message();
				message.what = 2;
				ShowSetActivity3288.this.myHandler.sendMessage(message);
	    	}
	    };
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "store onSaveInstanceState mainmodelast="+mMainMode_last
				+",mainmodeset="+mMainMode_set);
		super.onSaveInstanceState(outState);
		outState.putString("main_mode_last", mMainMode_last);
		outState.putString("main_mode_set", mMainMode_set);
		
	}
	
	
	
	/***************************************3288************up*************************************/
	
	
	
	
	
	
	
}
