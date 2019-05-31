package com.example.system;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import com.baidu.mobstat.StatService;
import com.csw.csw_desktop_yahao.MainActivity;
import com.zhongqin.tianlai.R;

import com.wyf.redian.HotspotActivity;
import com.wyf.setting.SettingTest;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShezhiActivity extends Activity {

	private TextView text;
	private TextView title;
	private Button gengduoshezhi;
	private Button xianshi;
	private Button xitongxinxi;
	private Button luyoushezhi;
	private Button qidongshezhi;
//	private Button fimalyKey;
	private SharedPreferencesUtils preferencesUtils;
	private   ImageManager imageManager = new ImageManager();
	RelativeLayout layRelativeLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.shezhi_item);
		text = (TextView) findViewById(R.id.shezhi_item_text);
		title = (TextView) findViewById(R.id.shezhi_item_title);
		gengduoshezhi = (Button) findViewById(R.id.but_shezhi_item_1);
		xianshi = (Button) findViewById(R.id.but_shezhi_item_3);
		xitongxinxi = (Button) findViewById(R.id.but_shezhi_item_7);
		luyoushezhi = (Button) findViewById(R.id.but_shezhi_item_8);
		qidongshezhi = (Button) findViewById(R.id.but_shezhi_item_9);
//		fimalyKey = (Button) findViewById(R.id.but_shezhi_item_suo);
		// 壁纸
		/*layRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.shezhi_main_r);
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.shezhi_back));*/
		 
		// xianshi.setFocusable(true);
		// xianshi.requestFocus();
		xianshi.setFocusable(true);
		xianshi.requestFocus();

		qidongshezhi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent qidong = new Intent();
				qidong.setClass(ShezhiActivity.this, qiActivity.class);
				startActivity(qidong);
			}
		});
		gengduoshezhi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent shezhi = new Intent();
				shezhi.setComponent(new ComponentName("com.android.settings",
						"com.android.settings.Settings"));
				startActivity(shezhi);
			}
		});
		xianshi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
//				String[] ver = CpuManager.getVersion();
//				String 
				Intent but_shezhi_3 = new Intent();
				/*int hexin = getNumCores();
				if (hexin <= 2) {
					but_shezhi_3.setClass(ShezhiActivity.this,
							ShowSetActivityA20.class);
				}
				if (hexin == 4) {
					but_shezhi_3.setClass(ShezhiActivity.this,
							ShowSetActivity.class);
				}*/
				but_shezhi_3.setClass(ShezhiActivity.this,
						ShowSetActivity3288.class);
				startActivity(but_shezhi_3);
			}
		});
		xitongxinxi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent but_shezhi_7 = new Intent(ShezhiActivity.this,
						sys_xinxi.class);
				but_shezhi_7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				if (but_shezhi_7.getComponent() == null)
					return;
				startActivity(but_shezhi_7);
			}
		});
		luyoushezhi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent but_shezhi_2 = new Intent();
				but_shezhi_2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				but_shezhi_2.setClass(ShezhiActivity.this,
						HotspotActivity.class);
				startActivity(but_shezhi_2);
			}
		});
		/*fimalyKey.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent but_shezhi_2 = new Intent();
				but_shezhi_2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				but_shezhi_2.setClass(ShezhiActivity.this,
						FamilyKeyActivity.class);
				startActivity(but_shezhi_2);
			}
		});*/
	}

	// 判断CPU核心数
	private int getNumCores() {
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}

		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			Log.d("display", "CPU Count: " + files.length);
			return files.length;
		} catch (Exception e) {
			Log.d("display", "CPU Count: Failed.");
			e.printStackTrace();
			return 1;
		}
	}
	  @Override
	  protected void onResume() {
	  	// TODO Auto-generated method stub
	  	super.onResume();
	  	layRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.set_main_r);
	  	int wallpaperIdCurrent = preferencesUtils.getsum(ShezhiActivity.this,
	  			"wallpaperID");
	  	if (wallpaperIdCurrent != 0) {
	  		layRelativeLayout.setBackgroundDrawable(imageManager
	  				.getBitmapFromResources(this, wallpaperIdCurrent));
	  	} else {
	  		layRelativeLayout.setBackgroundDrawable(imageManager
	  				.getBitmapFromResources(this, R.drawable.wallpaper_3));
	  	}
	  }

}
