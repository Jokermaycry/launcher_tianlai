package com.wyf.redian;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.csw.csw_desktop_yahao.MainActivity;
import com.zhongqin.tianlai.R;
import com.example.system.ShezhiActivity;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HotspotActivity extends Activity {
	private WifiManager wifiManager;
	private Button open;
	private Button fanghui;
	private boolean flag = false;
	private EditText zhanghao;
	private EditText mima;
	private String name;
	private String pwd;
	private static ImageManager imageManager = new ImageManager();
	RelativeLayout hotRelativelayout;
	private SharedPreferencesUtils preferencesUtils;
	private ProgressDialog mProgress ;
	private ProgressDialog mProgress2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
		setContentView(R.layout.redian_main);
		
		
		// 壁纸
		hotRelativelayout=(RelativeLayout)this.findViewById(R.id.HotpotActivityLayout);
		int wallpaperIdCurrent = preferencesUtils.getsum(HotspotActivity.this,
				"wallpaperID");
		if (wallpaperIdCurrent != 0) {
			hotRelativelayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			hotRelativelayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_3));
		}
		
		// 获取wifi管理服务
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		open = (Button) findViewById(R.id.open_hotspot);
		fanghui = (Button) findViewById(R.id.hotspot_fanghui);
		zhanghao = (EditText) findViewById(R.id.zhanghao);
		mima = (EditText) findViewById(R.id.mima);
		// 通过按钮事件设置热点
		fanghui.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HotspotActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		fanghui.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
				fanghui.setTextColor(Color.RED);
				}else{ 
					fanghui.setTextColor(Color.WHITE);
				}
			}
		});
		open.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					    open.setTextColor(Color.RED);
					}else{
						open.setTextColor(Color.WHITE);
					}
			}
		});
		open.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 如果是打开状态就关闭，如果是关闭就打开
				flag = !flag;
				name = zhanghao.getText().toString();
				pwd = mima.getText().toString();
				if (name == null || name.equals("")) {
					zhanghao.setError("热点名称不能为空！");
					return;
				} else {
					if (vd(name)) {
						zhanghao.setError("热点名称不能为中文！");
						return; 
					}
					if (!check(name)) {
						zhanghao.setError("热点名称第一位必须为字母！");
						return;
					}
				}
				if (pwd == null || pwd.equals("")) {
					mima.setError("密码不能为空！");
					return;
				} else {
					if (vd(pwd)) {
						mima.setError("密码不能为中文！");
						return;
					}
					if (pwd.length()<8) {
						mima.setError("密码不能少于8位！");
						return;
					}
					if (IsConfig(name)) {
						Toast.makeText(HotspotActivity.this, "当前热点已经配置过！", 1)
								.show();
						return;
					}
						if (!setWifiApEnabled(false)) {
							 mProgress = ProgressDialog.show(HotspotActivity.this,"" , "新建wifi热点发生错误！");
							 mProgress.setButton("确定", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										mProgress.dismiss(); 
									}
								});
							
							/*AlertDialog.Builder builder = new AlertDialog.Builder(HotspotActivity.this)  
					        .setMessage("新建wifi热点发生错误！")//设置对话框内容  
					        .setTitle("提示")//设置对话框标题  
					        .setPositiveButton("确定", new OnClickListener() {//设置对话框[肯定]按钮  
					            @Override  
					            public void onClick(DialogInterface dialog, int which) {  
					                dialog.dismiss();//关闭对话框  
					                finish();//结束当前Activity  
					            }  
					        })  
					        .setNegativeButton("取消", new OnClickListener() {//设置对话框[否定]按钮  
					            @Override  
					            public void onClick(DialogInterface dialog, int which) {  
					                dialog.dismiss();//关闭对话框  
					            }  
					        });  
					        builder.create().show();//创建对话框并且显示该对话框  
*/							return ;
						}
						new Timer().schedule(new TimerTask() {
							public void run() {
								// 这里是10S后执行的代码

								if (!setWifiApEnabled(true)) {
									 mProgress = ProgressDialog.show(HotspotActivity.this,"" , "新建wifi热点发生错误！");
									 mProgress.setButton("确定", new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												mProgress.dismiss(); 
											}
										});
									 return ;
								}
								handler.sendEmptyMessage(2);
								new Timer().schedule(new TimerTask() {
									public void run() {
										// 这里是10S后执行的代码

										handler.sendEmptyMessage(1);
										/*Intent intent = new Intent();
										intent.setClass(HotspotActivity.this, MainActivity.class);
										startActivity(intent);*/
										
									}
								}, 7000);
							}
						}, 3000);
					}
				}
		//	}
		});
	}

	// wifi热点开关
	public boolean setWifiApEnabled(boolean enabled) {
	//	if (enabled) { // disable WiFi in any case
			// wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
			wifiManager.setWifiEnabled(false);
	//	}
		try {
			// 热点的配置类
			WifiConfiguration apConfig = new WifiConfiguration();
			// 配置热点的名称(可以在名字后面加点随机数什么的)
			apConfig.SSID=name;
			// 配置热点的密码
			apConfig.preSharedKey = pwd;
			SharedPreferencesUtils.setData(HotspotActivity.this, "SSID", name);
			SharedPreferencesUtils.setData(HotspotActivity.this, "preSharedKey", pwd);
			apConfig.hiddenSSID = true;
			apConfig.status = WifiConfiguration.Status.ENABLED;
			apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			// 通过反射调用设置热点
			Method method = wifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			// 返回热点打开状态
			System.out.println(wifiManager.getWifiState()+"132121");
			return (Boolean) method.invoke(wifiManager, apConfig, enabled);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				Toast.makeText(HotspotActivity.this, "新建wifi热点"+name+"成功!", 1).show();
				mProgress2.dismiss();
				break;
			case 2:
				mProgress2 = ProgressDialog.show(HotspotActivity.this,"" , "正在为您配置wifi热点，请稍后...");
				
				break;
			default:
				break;
			}
		}
		
		
	};

	public static boolean check(String fstrData) {
		char c = fstrData.charAt(0);
		if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean vd(String str) {

		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;
				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}

	public boolean IsConfig(String SSID) {
		List<WifiConfiguration> existingConfigs = wifiManager
				.getConfiguredNetworks();
		if (existingConfigs != null && existingConfigs.size() > 0) {
			for (WifiConfiguration existingConfig : existingConfigs) {
				if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
}