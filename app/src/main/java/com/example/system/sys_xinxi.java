package com.example.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhongqin.tianlai.R;
import com.szy.update.update_main;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

public class sys_xinxi extends Activity {
	private static final String TAG = null;
	TextView book_text;
	TextView book_text_2;
	String jishenneicun = "";
	String neicundaxiao = "";
	String hexinshu = "";
	int flashSum = 0;
	int flashSize = 0;
	float flashSize_2;
	RelativeLayout layRelativeLayout;
	private static ImageManager imageManager = new ImageManager();
	private SharedPreferencesUtils preferencesUtils;
	private ProgressBar bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
		setContentView(R.layout.sys_xinxi);
		book_text = (TextView) findViewById(R.id.csw_text);
		book_text_2 = (TextView) findViewById(R.id.csw_text2);
		bar = (ProgressBar) findViewById(R.id.bar);
		
		layRelativeLayout = (RelativeLayout) this.findViewById(R.id.xinxi_r);
		int wallpaperIdCurrent = preferencesUtils.getsum(sys_xinxi.this,
				"wallpaperID");
		if (wallpaperIdCurrent != 0) {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_3));
		}


		String[] ver = CpuManager.getVersion();

		int hexin = getNumCores();

		if (hexin == 1) {
			hexinshu = "1.5Ghz";
		} else if (hexin == 2) {
			hexinshu = "双核1.2Ghz";
		} else if (hexin == 4) {
			hexinshu = "四核1.6Ghz";
		} else if (hexin == 8) {
			hexinshu = "八核1.6Ghz";
		} else {
			hexinshu = "unknow Ghz";
		}

		float sizeFlash = readSDCard();
		if (sizeFlash < 2 && sizeFlash > 0) {
			jishenneicun = "4G Byte";
			flashSum = 4;
		} else if (sizeFlash < 8 && sizeFlash > 2) {
			jishenneicun = "8G Byte";
			flashSum = 8;
		} else if (sizeFlash < 16 && sizeFlash > 8) {
			jishenneicun = "16G Byte";
			flashSum = 16;
		} else if (sizeFlash < 32 && sizeFlash > 16) {
			jishenneicun = "32G Byte";
			flashSum = 32;
		} else {
			jishenneicun = "unknow Byte";
		}

		long sizeSystem = getmem_TOLAL();

		if (sizeSystem < 256 && sizeSystem > 0) {
			neicundaxiao = "256M Byte";
		} else if (sizeSystem < 512 && sizeSystem > 256) {
			neicundaxiao = "512M Byte";
		} else if (sizeSystem < 1024 && sizeSystem > 512) {
			neicundaxiao = "1G Byte";
		} else if (sizeSystem < 2048 && sizeSystem > 1024) {
			neicundaxiao = "2G Byte";
		} else {
			neicundaxiao = "unknow Byte";
		}

		String book_text_str = "CPU主频：" + hexinshu + " \n机身内存：" + jishenneicun
				+ " \n安卓版本：android" + ver[1] + "\n" + "设备名称：" + ver[2] + "\n"
				+ "系统版本：" + ver[3];
		book_text.setText(book_text_str);
		flashSize =	Math.round(flashSize_2);
		bar.setMax(flashSum);
		bar.setProgress(flashSize);
		book_text_2.setText("机身内存:" + jishenneicun + "\n可用空间:" + flashSize_2
				+ "G Byte");
	}

	public String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(sys_xinxi.WIFI_SERVICE);
		if (wifi == null)
			return null;
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public float readSDCard() {
		try {
			String state = Environment.getExternalStorageState();
			float sizeFlash = 0;
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				File sdcardDir = Environment.getExternalStorageDirectory();
				StatFs sf = new StatFs(sdcardDir.getPath());
				long blockSize = sf.getBlockSize();
				long blockCount = sf.getBlockCount();
				long availCount = sf.getAvailableBlocks();
				sizeFlash = (float) (blockSize * blockCount)
						/ (1024 * 1024 * 1024);
				Log.d("", "block大小:" + blockSize + ",block数目:" + blockCount
						+ ",总大小:" + (float) (blockSize * blockCount)
						/ (1024 * 1024 * 1024) + "GB");
				Log.d("", "可用的block数目：:" + availCount + ",剩余空间:"
						+ (float) (availCount * blockSize)
						/ (1024 * 1024 * 1024) + "KB");
				flashSize = (int) ((availCount * blockSize) / (1024 * 1024 * 1024));
				flashSize_2 = (float) (availCount * blockSize)
						/ (1024 * 1024 * 1024);
			}
			return sizeFlash;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
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
			Log.d(TAG, "CPU Count: " + files.length);
			return files.length;
		} catch (Exception e) {
			Log.d(TAG, "CPU Count: Failed.");
			e.printStackTrace();
			return 1;
		}
	}

	// 获取内存大小
	public static long getmem_TOLAL() {
		long mTotal;
		// /proc/meminfo读出的内核信息进行解释
		String path = "/proc/meminfo";
		String content = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path), 8);
			String line;
			if ((line = br.readLine()) != null) {
				content = line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// beginIndex
		int begin = content.indexOf(':');
		// endIndex
		int end = content.indexOf('k');
		// 截取字符串信息

		content = content.substring(begin + 1, end).trim();
		mTotal = Integer.parseInt(content);
		long x = mTotal / 1024;
		return x;
	}

}
