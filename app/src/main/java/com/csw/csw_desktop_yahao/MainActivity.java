package com.csw.csw_desktop_yahao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import com.baidu.mobstat.StatService;


import com.zhongqin.tianlai.R;
import com.csw.download.DownManager;
import com.csw.newfragment.AllAppFragment;
import com.csw.newfragment.HomeFragment;
import com.csw.newfragment.SettingsAppFragment;
import com.csw.tp.cc_server.ServerService;

import com.example.system.UnlockActivity;

import com.szy.update.update_main;
import com.util.fragment.AllApplicationFragment;
import com.util.fragment.DianboFragment;
import com.util.fragment.FragmentInterfaces;
import com.util.fragment.LocalFragment;
import com.util.fragment.MusicFragment;
import com.util.fragment.SetFragment;
import com.weather.manager.TianqiActivity;
import com.weather.manager.WeatherManager;
import com.wyf.allapp.GetAppInfo;
import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.ImageManager;
import com.wyf.util.NetCheck;
import com.wyf.util.SharedPreferencesUtils;
import com.wyf.util.TimerPingbaoUtil;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

@SuppressLint({ "ValidFragment", "NewApi", "InlinedApi", "SimpleDateFormat" })
public class MainActivity extends FragmentActivity implements OnClickListener,
		OnFocusChangeListener {

	private int viewFlipper_sum;
	private boolean viewFlipper_wether = true;
//	private SharedPreferencesUtils preferencesUtils;

	// wifi状态
	ConnectivityManager connManager;
	NetworkInfo mWifi;
	// 天气
	private static TextView currentTemp;
	public static TextView currentWeather;
	public static TextView currenttop;
	public static TextView cityField;
	private String address;
	private String city;
	public static Map<String, String> map = new HashMap<String, String>();
	// 变量
	public static boolean whether = false;
	public static boolean whether_zhanshi;
	public static final String TAG = "MainActivity";
	public Button but_view;
	public View pop_view;
	// 用于保存上一个按钮的view用来放大
	public View but_fd;
	private boolean downWhether;
	private int DOWN_NUM;
	private int UP_TIANQI = 0;
	// 播放
	private VideoView vv_video;
	private boolean isPlaying;
	// 二级菜单
	RelativeLayout rel_menu = null;
	private List<AppInfo> left_mlistAppInfo = null;
	// //////////////////////////////增加应用程序///////
	private final static int init_kuaijie_apps_run = 0;
	private final static int UPDATE_UI = 1;
	private final static int DIALOG__TIANQI_OFF = 11;
	private final static int DIALOG__TIANQI_ON = 12;
	private final static int SOUSU_YINGYONG = 13;
	private final static int DOWNLOAD_SUM = 19;
	private RelativeLayout apps_rel;
	
	private ViewPager viewPager;
	private ArrayList<Button> textViews;
	private LinearLayout titleLinearLayout;
	private int oldPosition = 0;
	private HorizontalScrollView horizontalScrollView;
	/*
	 * View v0; View v1; View v3; View v4; View v5;
	 */
	// View mZhiboView;
	// View mAllView;
	private Button tianqi_touming_Button;

	// 泰洁视频2.7.6.1

	HashMap<String, String> mHashMap = new HashMap<String, String>();
	private String url;
	private String name;
	private static final int CMNET = 3;
	private static final int CMWAP = 2;
	private static final int WIFI = 1;
	private ImageView currentimage;
	private static String PM = "优";

	DownManager manager = new DownManager(MainActivity.this);
	RelativeLayout mainRelativeLayout;
	private ImageManager imageManager = new ImageManager();
//	private com.wyf.util.gundong_Suspend gundong_Suspend;

	public static String welcome_init = null;
	public static int whether_updata;

	private static int currentWhichPage = 0;// 当前是哪个页面
	public static Context SetContext;//为了设置界面升级中心
	
	
	 /*
     * added  by lgj
     */
    private static   Timer recordTimer=new Timer();
    private  static TimerTask mTimerTask;
	
    private List<Button> mButton;
	private Button homeBtn, settingsBtn, myappBtn;
	public static final String SERIAL_UPDATE = "com.cl.update";
	//开机启动
    private String qidong = "", qidongActivity;
    
    private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SERIAL_UPDATE);
		registerReceiver(broadcastReceiver, intentFilter);
		// added 11.20,隐藏3288下面的状态栏
		// getWindow().getDecorView().setSystemUiVisibility(
		// View.SYSTEM_UI_FLAG_LOW_PROFILE);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
		setContentView(R.layout.activity_main);
//		this.isResumed();  //注释于5.15
		
		SetContext=MainActivity.this;
		//currentimage = (ImageView) findViewById(R.id.currentimage);

		// wjz：进行数据初始化
		// if(jihuo_sum==1){
		if (welcome_init != null) {
			// 由于数据在welcom里初始化，但是界面显示肯定要在这个activity里。那么将要初始化的
			// 界面，就在这里。
			/** TCP 手机助手服务 */
	//		Intent intenttcp= new Intent(MainActivity.this,ServerService.class);
	//		startService(intenttcp);
			init_ui();
//			System.out.println("Welcome.whether_updata== "
//					+ Welcome.whether_updata);
			if (Welcome.whether_updata == 1) {
				// ui_update();
			} else {
//				Check_ui_update();
//				ui_update();
				
				
				if(recordTimer!=null){
		    		recordTimer.cancel();
		    		recordTimer=null;
		    		System.out.println("recordTimer=null;");
		    	}
		    	if(mTimerTask!=null){
		    		mTimerTask.cancel();
		    		mTimerTask=null;
		    		System.out.println("mTimerTask=null;");
		    	}
		    	
		    	 if(recordTimer==null){
		  			recordTimer = new Timer();//注意这里，不然
		  		 }
		          if (mTimerTask == null) {
		  			mTimerTask = new TimerTask() {
		  				@Override
		  				public void run() {
		  				// 获取存储的开机启动主类名
		  					qidongActivity = SharedPreferencesUtils.getData(MainActivity.this,
		  							"activityName");
		  					qidong = SharedPreferencesUtils.getData(MainActivity.this, "bao");
		  				// 判断开机启动主类名是否为空
		  					if (qidongActivity != null && qidong != null) {
		  						if (!qidongActivity.equals("") && !qidong.equals("")) {

		  							Intent intent = panduan_apps_to_list_all(qidong, qidongActivity);
		  							if (intent != null) {
		  								// startAcitiyMsg.what = 9;
		  								handler.sendEmptyMessage(8);
		  							}
		  						}
		  					} else {
		  						Log.d(TAG, "没有开机启动项");
		  					}
		  					
		  					ui_update();
//		  					tianqijiazai();
		  					openYaHaoMusicPlayer();
		  					
		  				}
		  			};
		  		}
		  		if (recordTimer != null && mTimerTask != null) {
		  			recordTimer.schedule(mTimerTask,2*1000);
		  		}
		  		TimerPingbaoUtil.CancelPingbaoTimer();
				TimerPingbaoUtil.StartPingbaoTimer(this);

			}
     
		} else {
			// 将要数据初始化的，都放在welcome里面，比如版本更新、天气管理等等。
			// 4月8号，王建忠和吴余锋，好好讨论。

//			int family_key = preferencesUtils.getsum(this, "family_key");
//			if (family_key == 1) {
//				Intent intent = new Intent(this, UnlockActivity.class);
//				this.startActivity(intent);
//				this.finish();
//			} else {
				Intent intent = new Intent(this, Welcome.class);
				this.startActivity(intent);
				this.finish();
//			}
		}
		/*
		 * }else{ Intent intent = new Intent(this,
		 * com.example.apk_activation.MainActivity.class);
		 * this.startActivity(intent); this.finish(); } }
		 */
		
//		Intent mIntent=new Intent();
//		mIntent.setClass(packageContext, cls)
//		openYaHaoMusicPlayer();
	}

	 /**
	 * 执行打开播放器apk的操作
	 */
	
	 private void openYaHaoMusicPlayer(){
		 Intent intent =MainActivity.this.getPackageManager().getLaunchIntentForPackage("com.example.musicinterface");
		if (intent==null) {
			return;
		}
		Intent k12intent = new Intent();
		k12intent.setAction("com.csw.StartServiceActivity");
		k12intent.setComponent(new ComponentName(
				"com.example.musicinterface",
				"com.example.musicinterface.StartServiceActivity"));
		startActivity(k12intent);
		
		/* String paramString="adb shell"+"\n"+"su"+"\n"+"am start -n com.example.musicinterface/com.example.musicinterface.StartServiceActivity"+"\n"+ "exit" + "\n" + "exit";
		  if (RootCmd.haveRoot()) {
				if (RootCmd.execRootCmdSilent(paramString) == -1) {
					System.out.println("执行不成功");
				} else {
					System.out.println("执行成功");
				}
			} else {
				System.out. println("没有root权限");
			}*/
	}
	
	 void init_ui() {
		 //下划线指示
		homeBtn = (Button) findViewById(R.id.btn_sound);
		settingsBtn = (Button) findViewById(R.id.btn_settings);
		myappBtn = (Button) findViewById(R.id.btn_myapp);

		homeBtn.setOnClickListener(this);
		settingsBtn.setOnClickListener(this);
		myappBtn.setOnClickListener(this);
		mButton = new ArrayList<Button>();
       
		mButton.add(homeBtn);
		mButton.add(settingsBtn);
		mButton.add(myappBtn);
		
		// welcome();
		this.viewPager = (ViewPager) findViewById(R.id.viewPager);
		this.titleLinearLayout = (LinearLayout) findViewById(R.id.titleLinearLayout);
		this.horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		/** 布局加载 */
		//initTitle();
		initActivity();
		// 天气
//		currentTemp = (TextView) findViewById(R.id.currentTemp);
//		currentWeather = (TextView) findViewById(R.id.currentWeather);
//		currenttop = (TextView) findViewById(R.id.currenttop);
		cityField = (TextView) findViewById(R.id.cityField);
		// 播放
		// vv_video = (VideoView) findViewById(R.id.vv_videoview);
		// play(0);
		tianqi_touming_Button = (Button) findViewById(R.id.tianqi_touming);
		// final Intent intent10 = new Intent(this,
		// com.csw.apps.intent.IntentActivity.class);
		tianqi_touming_Button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Intent intent11 = new Intent(MainActivity.this,
				// SetCityActivity.class);
				// startActivityForResult(intent11, 0);
//				TimerPingbaoUtil.CancelPingbaoTimer();
//				TimerPingbaoUtil.StartPingbaoTimer(MainActivity.this);
//				
//				Intent intent11 = new Intent(MainActivity.this,
//						TianqiActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("cityname", cityField.getText().toString()
//						.trim());
//				bundle.putString("min", currentTemp.getText().toString().trim());
//				bundle.putString("max", currenttop.getText().toString().trim());
//				bundle.putString("weather", currentWeather.getText().toString()
//						.trim());
//				bundle.putString("pm", PM.trim());
//				intent11.putExtras(bundle);
//				startActivityForResult(intent11, 0);
				Intent intent3 = panduan_apps_to_list_all("com.moji.tvweather", "com.moji.tvweather.activity.WelcomeActivity");
				if (intent3 == null) {
					Toast.makeText(mContext, "没有找到天气应用", Toast.LENGTH_SHORT).show();
				}else {
					//intent3.setClassName("com.moji.tvweather", "com.moji.tvweather.activity.WelcomeActivity");
					startActivity(intent3);
				}
			}
		});
		tianqi_touming_Button
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if(arg1){
							TimerPingbaoUtil.CancelPingbaoTimer();
							TimerPingbaoUtil.StartPingbaoTimer(MainActivity.this);
						}
						downWhether = true;
					}
				});
		/** 字幕滚动 */
		/*
		 * gundong_Suspend = new
		 * com.wyf.util.gundong_Suspend(MainActivity.this); if (gundong_Suspend
		 * != null) gundong_Suspend.showAdvertDialog(3);
		 */
		/** wifi检测 */
		wifi_check();
		/** 天气管理 */
		
		
		
		/** 静默安装 */
		//tianqijiazai();

		// Check_ui_update();
	}

	// 做窗口的动画效果
	private AlphaAnimation alpha;
	private AnimationDrawable animationDrawable;

	/*
	 * private void welcome(){ new Thread(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * 
	 * 
	 * app_add_all(); app_add_zhibo(); app_add_youxi(); } }).start(); }
	 */
	//更新
	   update_main update = new update_main(MainActivity.this);
		
		private void ui_update() {
			Thread thread = new Thread(runnableup);
			thread.start();
		}
		Runnable runnableup = new Runnable() {
			public void run() {
				try {
					Thread.sleep(10);
					update.update();
					// mUIHandler.sendEmptyMessage(UPDATE_VER);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
	
	
	
	// 初始化handler
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.HAND_START:
				openYaHaoMusicPlayer();
				break;
			case Constant.HAND_WIFI:
				if (isWifiConnect()) {
					WifiManager mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
					WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
					int wifi = mWifiInfo.getRssi();// 获取wifi信号强度
					if (wifi > -50 && wifi < 0)
						image_button_switch(R.id.wifi_icon,
								R.drawable.ic_wifi_signal_4);
					if (wifi > -70 && wifi < -50)
						image_button_switch(R.id.wifi_icon,
								R.drawable.ic_wifi_signal_3);
					if (wifi > -80 && wifi < -70)
						image_button_switch(R.id.wifi_icon,
								R.drawable.ic_wifi_signal_2);
					if (wifi > -100 && wifi < -80)
						image_button_switch(R.id.wifi_icon,
								R.drawable.ic_wifi_signal_1);
				} else {
					ConnectivityManager conn = (ConnectivityManager) MainActivity.this
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo networkInfo = conn
							.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
					if (networkInfo.isConnected()) {
						image_button_switch(R.id.wifi_icon,
								R.drawable.mynetwork2);
					} else {
						image_button_switch(R.id.wifi_icon,
								R.drawable.connect_no);
					}

				}
				break;
			case Constant.HAND_WEATHER:
				Log.d(TAG, "HAND_WEATHER --   ---  ");
				if (map == null || map.size() == 0) {
					Log.d(TAG, "HAND_WEATHER --   ---  map == null || map.size() == 0");
					break;
				}
				String wendu = map.get(Constant.nodes[0]);
				if (wendu != null) {
					Log.d(TAG, "wendu != null   "+wendu);
					currentTemp.setText(map.get(Constant.nodes[2]) + "℃");

					currenttop.setText(map.get(Constant.nodes[1]) + "℃");

					currentWeather.setText(map.get(Constant.nodes[0]));
					cityField.setText(address);
					PM = map.get(Constant.nodes[3]);
					getcurrentimage();
					SharedPreferencesUtils.setData(MainActivity.this,
							"weathercityname", cityField.getText().toString()
									.trim());
					SharedPreferencesUtils.setData(MainActivity.this,
							"weathermin", currenttop.getText().toString()
									.trim());
					SharedPreferencesUtils.setData(MainActivity.this,
							"weathermax", currentTemp.getText().toString()
									.trim());
					SharedPreferencesUtils.setData(MainActivity.this,
							"weather", currentWeather.getText().toString()
									.trim());
					SharedPreferencesUtils.setData(MainActivity.this,
							"weatherpm", PM.trim());
				}else{
					Log.d(TAG, "wendu == null");
				}
				break;
			case Constant.HAND_WEATHER_UP:
				if (!Constant.map.get(Constant.nodes[0]).equals(null)) {
					Log.d(TAG, "HAND_WEATHER_UP --   ---  "+Constant.map.get(Constant.nodes[2]));
					currentTemp.setText(Constant.map.get(Constant.nodes[2])
							+ "℃");

					currenttop.setText(Constant.map.get(Constant.nodes[1])
							+ "℃");

					currentWeather.setText(Constant.map.get(Constant.nodes[0]));
					PM = map.get(Constant.nodes[3]);
					getcurrentimage();
					SharedPreferencesUtils.setData(MainActivity.this,
							"weathercityname", cityField.getText().toString()
									.trim());
					SharedPreferencesUtils.setData(MainActivity.this,
							"weathermin", currentTemp.getText().toString()
									.trim());
					SharedPreferencesUtils.setData(MainActivity.this,
							"weathermax", currenttop.getText().toString()
									.trim());
					SharedPreferencesUtils.setData(MainActivity.this,
							"weather", currentWeather.getText().toString()
									.trim());
					SharedPreferencesUtils.setData(MainActivity.this,
							"weatherpm", PM.trim());
				}else{
					Log.d(TAG, "HAND_WEATHER_UP --   --- NULL ");
				}
				break;
			case 8:
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(qidong, qidongActivity));
				startActivity(intent);
				break;
			default:
				break;
			}

		}
	};

	AnimationSet animationSet = new AnimationSet(true);

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.configure_menu, menu); return true; }
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 得到城市的编码
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			FragmentInterfaces fragmentInterfaces = mFragmentInterfaces.get(2);
			fragmentInterfaces.show();
			// Kuaijie_ini();
			return;
		}
		/*if (resultCode == 4) {
			FragmentInterfaces fragmentInterfaces = mFragmentInterfaces.get(2);
			fragmentInterfaces.show();
			// Kuaijie_ini();
			return;
		}*/
		if (resultCode == 2) {
			FragmentInterfaces fragmentInterfaces = mFragmentInterfaces.get(1);
			fragmentInterfaces.show();
			return;
		}

		/*
		 * SharedPreferences sp = getSharedPreferences(
		 * TianqiActivity.CITY_CODE_FILE, MODE_PRIVATE);
		 * 
		 * String cityCode = sp.getString("code", ""); String cityName =
		 * sp.getString("city", "");
		 */
		String cityName = null;
		try {
			cityName = data.getStringExtra("city");
		} catch (Exception e) {

		}
		Log.d(TAG, "onActivityResult cityName="+cityName);
		if (NetCheck.checkNetWorkStatus(MainActivity.this)) {
			if (cityName == null || cityName.equals("")) {
				cityName = SharedPreferencesUtils.getData(MainActivity.this, "weathercityname");
				if (cityName == null || cityName.equals("")) {
					cityName = cityField.getText().toString().trim();
				}
			}
			city = WeatherManager.getAdd1(cityName);
			
//			cityField.setText(city);
//			mUIHandler.sendEmptyMessage(20);
			Log.d(TAG, "定位天氣,onActivityResult");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						map = WeatherManager.getTianqi1(city);
						if (map != null || map.size() != 0) {
//							if (!Constant.map.get(Constant.nodes[0]).equals(
//									null))
							Log.d(TAG, "map != null || map.size() != 0=======");
							
								if (Constant.map.get(Constant.nodes[0])!=
										null){
									Log.d(TAG, "map != null || map.size() != 0");
								handler.sendEmptyMessage(Constant.HAND_WEATHER_UP);
								}else{
									Log.d(TAG, "map == null || map.size() == 0");
								}
						}else{
							Log.d(TAG, "map == null || map.size() == 0====================");
						}
						mUIHandler.sendEmptyMessage(DIALOG__TIANQI_OFF);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}).start();
		} else {
			Toast.makeText(getApplicationContext(), "网络异常！请检查网络",
					Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * wifi信号检查
	 */
	void wifi_check() {

		// 新建一个线程，检查wifi的状态
		Runnable runnable1 = new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);// 等待系统调度，显示欢迎界面

					while (true) {
						handler.sendEmptyMessage(Constant.HAND_WIFI);
						Thread.sleep(1000);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		Thread thread1 = new Thread(runnable1);
		thread1.start();
	}

	/**
	 * 判断wifi是否连接成功
	 * 
	 * @return
	 */
	public boolean isWifiConnect() {
		connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		WifiManager mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
		int wifi = mWifiInfo.getRssi();// 获取wifi信号强度

		return mWifi.isConnected();
	}

	/**
	 * 放横幅的image，不过这个函数，可以适用所有的imagebuttom放图像
	 * 
	 * @param id
	 *            要换的位置
	 * @param id2
	 *            要更换的图片
	 */
	private void image_button_switch(int id, int id2) {
		final ImageView image_shoucang = (ImageView) findViewById(id);
		image_shoucang.setImageBitmap(BitmapFactory.decodeResource(
				getResources(), id2));
	}

	/**
	 * @param 天气加载
	 */
	public void tianqijiazai() {
		
		String cityName = null;
		try {
			cityName = SharedPreferencesUtils.getData(MainActivity.this, "weathercityname");
		} catch (Exception e) {
            e.printStackTrace();
		}
		Log.d(TAG, "tian qi jia zai cityName=="+cityName);
		if(cityName!=null){
			Log.d(TAG, "tian qi jia zai cityName!=null");
			if (NetCheck.checkNetWorkStatus(MainActivity.this)) {
				if (cityName == null || cityName.equals("")) {
					cityName = SharedPreferencesUtils.getData(MainActivity.this, "weathercityname");
					if (cityName == null || cityName.equals("")) {
						cityName = cityField.getText().toString().trim();
					}
				}
				city = WeatherManager.getAdd1(cityName);
//				cityField.setText(city);
				mUIHandler.sendEmptyMessage(20);
                Log.d(TAG, "tian qi jia zai update didian");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							map = WeatherManager.getTianqi1(city);
							if (map != null || map.size() != 0) {
								Log.d(TAG, "map != null || map.size() != 0=====================");
//								if (!Constant.map.get(Constant.nodes[0]).equals(
//										null))
								
									if (Constant.map.get(Constant.nodes[0])!=
											null){
									handler.sendEmptyMessage(Constant.HAND_WEATHER_UP);
									Log.d(TAG, "Constant.map.get(Constant.nodes[0])!=null");
									}
							}else{
								Log.d(TAG, "map == null || map.size() == 0");
							}
							mUIHandler.sendEmptyMessage(DIALOG__TIANQI_OFF);
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}
				}).start();
			} else {
				Toast.makeText(getApplicationContext(), "网络异常！请检查网络",
						Toast.LENGTH_LONG).show();
			}

		}else{
			Log.d(TAG, "tian qi jia zai cityName==null");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						
							if (NetCheck.checkNetWorkStatus(MainActivity.this)) {
								String x = WeatherManager.GetNetIp();
								if (!(x == null)) {
									String city = WeatherManager.getCityByIp(x);
									address = WeatherManager.getAdd(city);
									SharedPreferencesUtils.setData(MainActivity.this,
											"weathercityname", address);
									map = WeatherManager.getTianqi1(address);
									
									handler.sendEmptyMessage(Constant.HAND_WEATHER);
									Log.d(TAG, "tian qi jia zai has net");
									/*
									 * Toast.makeText(MainActivity.this, "城市:" +
									 * address + "更新成功", 1).show();
									 */
									// mUIHandler.sendEmptyMessage(DIALOG__TIANQI_OFF);
								} else {
									// Toast.makeText(MainActivity.this,
									// "请检查网络是否连接成功！", 1).show();
									mUIHandler.sendEmptyMessage(DIALOG__TIANQI_ON);
								}
							} else {
								mUIHandler.sendEmptyMessage(DIALOG__TIANQI_ON);
							}
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}).start();
		}
		
		
		
		
		
		
		/*new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						if (NetCheck.checkNetWorkStatus(MainActivity.this)) {
							String x = WeatherManager.GetNetIp();
							if (!(x == null)) {
								String city = WeatherManager.getCityByIp(x);
								address = WeatherManager.getAdd(city);
								preferencesUtils.setData(MainActivity.this,
										"city", address);
								map = WeatherManager.getTianqi1(address);
								handler.sendEmptyMessage(Constant.HAND_WEATHER);

								
								 * Toast.makeText(MainActivity.this, "城市:" +
								 * address + "更新成功", 1).show();
								 
								// mUIHandler.sendEmptyMessage(DIALOG__TIANQI_OFF);
							} else {
								// Toast.makeText(MainActivity.this,
								// "请检查网络是否连接成功！", 1).show();
								mUIHandler.sendEmptyMessage(DIALOG__TIANQI_ON);
							}
						} else {
							mUIHandler.sendEmptyMessage(DIALOG__TIANQI_ON);
						}
						Thread.sleep(3000000);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}).start();*/
	}

	/**
	 * 得到本地天气
	 */
	public void dedaobenditianqi() {
		if (NetCheck.checkNetWorkStatus(MainActivity.this)) {
			String x = WeatherManager.GetNetIp();
			if (!(x == null)) {
				String city = WeatherManager.getCityByIp(x);
				address = WeatherManager.getAdd(city);
				map = WeatherManager.getTianqi1(address);
				handler.sendEmptyMessage(Constant.HAND_WEATHER);

			}
		}
	}

	Handler mUIHandler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/*
			 * case init_kuaijie_apps_run: kuaijie_apps_init(); //
			 * listview.setFocusable(true); // listview.requestFocus(); break;
			 */
			case UPDATE_UI:
				apps_rel.setBackgroundResource(R.drawable.bg);
				break;
			case DIALOG__TIANQI_OFF:
				// mpDialog_tianqi.dismiss();
				Toast.makeText(MainActivity.this, "城市:" + city + " 更新成功", 1)
						.show();
				break;
			case DIALOG__TIANQI_ON:
				Toast.makeText(MainActivity.this, "请检查网络是否连接成功！", 1).show();
				// mpDialog_tianqi.dismiss();
				break;
			case SOUSU_YINGYONG:
				Toast.makeText(MainActivity.this, "找不到该应用", Toast.LENGTH_LONG)
						.show();
				break;
			case DOWNLOAD_SUM:
				mHashMap.put("url", "http://112.124.43.99/apk/xiazaiDownload/"
						+ url + ".apk");
				mHashMap.put("name", name);
				// 检查软件下载
				manager.checkUpdate(mHashMap);
				
			case 20:
				Log.d(TAG, "更新桌面地点");
				cityField.setText(city);
				break;
			default:
				break;
			}
		}
	};

	/*
	 * 重新刷新一遍
	 */
	public void send_key() {
		try {
			String keyCommand = "input keyevent " + KeyEvent.KEYCODE_DPAD_UP;
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(keyCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private long getAvailMemory(Context context) {
		// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		// return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
		return mi.availMem / (1024 * 1024);
	}

	// 从系统内存信息文件中读取总的内存：
	private long getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
		}
		// return Formatter.formatFileSize(context, initial_memory);//
		// Byte转换为KB或者MB，内存大小规格化
		return initial_memory / (1024 * 1024);
	}

	// 清理Android系统后台没有用到的内存：
	private void clear(Context context) {
		ActivityManager activityManger = (ActivityManager) context
				.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = activityManger
				.getRunningAppProcesses();
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				ActivityManager.RunningAppProcessInfo apinfo = list.get(i);

				// System.out.println("pid---->>>>>>>" + apinfo.pid);
				// System.out.println("processName->> " + apinfo.processName);
				// System.out.println("importance-->>" + apinfo.importance);
				String[] pkgList = apinfo.pkgList;
				if (apinfo.processName.equals("com.example.houtaiupdate")
						|| apinfo.processName.equals("com.csw.cc_server")) {
					System.out.println("22222222222222222222222222222222222");
					continue;
				}
				if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
					// Process.killProcess(apinfo.pid);
					for (int j = 0; j < pkgList.length; j++) {
						// 2.2以上是过时的,请用killBackgroundProcesses代替
						/** 清理不可用的内容空间 **/
						// activityManger.restartPackage(pkgList[j]);
						activityManger.killBackgroundProcesses(pkgList[j]);
					}
				}
			}
	}

	void yijian_clear() {
		ActivityManager activityManger = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = activityManger
				.getRunningAppProcesses();
		for (int i = 0; i < list.size(); i++) {
			ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
			// if(apinfo.processName.equals(new String("包名")))
			{
				String[] pkgList = apinfo.pkgList;
				for (int j = 0; j < pkgList.length; j++) {
					// 2.2以上是过时的,请用killBackgroundProcesses代替
					activityManger.killBackgroundProcesses(pkgList[j]);

				}
			}
		}
	}

	public void initTitle() {
		textViews = new ArrayList<Button>();
		/*
		 * int width = (getWindowManager().getDefaultDisplay().getWidth()) /
		 * Constant.HOME_TITLE_COUNT;
		 */
		// int width = 790 / Constant.HOME_TITLE_COUNT;
		int width = 632 / Constant.HOME_TITLE_COUNT;
		for (int i = 0; i < Constant.HOME_TITLE.length; i++) {
			Button textView = new Button(this);
			textView.setText(Constant.HOME_TITLE[i]);
			textView.setTextSize(30);
			textView.setTextColor(Color.BLACK);
			textView.setWidth(width);
			textView.setGravity(Gravity.CENTER);
			textView.setId(i);
			textView.setOnClickListener(this);
			textView.setOnFocusChangeListener(this);
			textViews.add(textView);
			titleLinearLayout.addView(textView);
		}
		setSelector(0);
		textViews.get(0).setTextColor(Color.GREEN);
	}

	private FragAdapter adapter;
	public static List<FragmentInterfaces> mFragmentInterfaces = new ArrayList<FragmentInterfaces>();

	
	
	public void initActivity() {
		 HomeFragment homeFragment = new HomeFragment();
		//DianboFragment dianboFragment = new DianboFragment();
		//LocalFragment localFragment = new LocalFragment();
		AllAppFragment allAppFragment = new AllAppFragment();
		SettingsAppFragment setFragment = new SettingsAppFragment();

		mFragmentInterfaces.add(homeFragment);
		//mFragmentInterfaces.add(dianboFragment);
		//mFragmentInterfaces.add(localFragment);
		mFragmentInterfaces.add(allAppFragment);
		mFragmentInterfaces.add(setFragment);
		
		
		
		adapter = new FragAdapter(getSupportFragmentManager());

		this.viewPager.setAdapter(adapter);
		this.viewPager.clearAnimation();
		this.viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if(position>=0&&position<3){
				/*setSelector(position);// 头部切换
				
				if (position > oldPosition
						&& position >= Constant.HOME_TITLE_COUNT
						|| position < oldPosition
						&& position <= Constant.HOME_TITLE_COUNT) {// 判断标题要显示的位置是否看不见了
					int singleWidth = titleLinearLayout.getWidth()
							/ Constant.HOME_TITLE.length;// 获取当个标题的宽度
					horizontalScrollView.smoothScrollTo(singleWidth * position,
							0);// 头部滚动到X,Y的位置
				}*/
					
				selected(position);	
				oldPosition = position;
				FragmentInterfaces fragmentInterfaces = mFragmentInterfaces
						.get(position);
				currentWhichPage = position;
				fragmentInterfaces.show();// 显示处理
				// fragmentInterfaces.hide();// 隐藏处理
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int position) {
				// TODO Auto-generated method stub

			}
		});
		viewPager.setOffscreenPageLimit(2);// 预加载，0或1都默认预加载1
		 selected(0);
	}
	
	private void selected(int position) {
		for (int i = 0; i < mButton.size(); i++) {
			if (position == i) {
				mButton.get(i).setBackground(
						getResources().getDrawable(R.drawable.pagetrue));
			} else {
				mButton.get(i).setBackground(
						getResources().getDrawable(R.drawable.pagefalse));
			}
		}
	}
	
	public void setSelector(int id) {
		for (int i = 0; i < Constant.HOME_TITLE.length; i++) {
			if (id == i) {
				if (id == 0) {
					textViews.get(i).setBackgroundResource(
							R.drawable.top_title_left);
				} else if (id == Constant.HOME_TITLE.length - 1) {
					textViews.get(i).setBackgroundResource(
							R.drawable.top_title_right);
				} else {
					textViews.get(i).setBackgroundResource(
							R.drawable.top_title_on);
				}

				if (DOWN_NUM == 1) {
					but_view.setTextColor(Color.WHITE);
					DOWN_NUM = 0;
				} else {
					textViews.get(id).setTextColor(Color.WHITE);

				}
				viewPager.setCurrentItem(i);
				textViews.get(id).setTextColor(Color.GREEN);
				// 字体加粗
				TextPaint tp = textViews.get(i).getPaint();
				tp.setFakeBoldText(true);
				but_view = textViews.get(id);
			} else {
				textViews.get(i).setTextColor(Color.WHITE);
				textViews.get(i).setBackgroundResource(
						R.drawable.top_title_center);
				textViews.get(i).setTextColor(
						getResources().getColor(R.color.fron_classify));
				// 字体加粗
				TextPaint tp = textViews.get(i).getPaint();
				tp.setFakeBoldText(false);
			}
		}
	}

	/*
	 * public void setSelector2(int id) { for (int i = 0; i <
	 * Constant.HOME_TITLE.length; i++) {
	 * 
	 * if (id == i) {
	 *//** */
	/*
	 * 
	 * textViews.get(id).setBackgroundResource( R.drawable.top_title_center);
	 * textViews.get(id).setTextColor(Color.WHITE); but_view =
	 * textViews.get(id); TextPaint tp = textViews.get(i).getPaint();
	 * tp.setFakeBoldText(true); break; } else { textViews.get(i).setTextColor(
	 * getResources().getColor(R.color.fron_classify)); // 字体加粗 TextPaint tp =
	 * textViews.get(i).getPaint(); tp.setFakeBoldText(false); } } }
	 */

	public void onClick(View v) {
		TimerPingbaoUtil.CancelPingbaoTimer();
		TimerPingbaoUtil.StartPingbaoTimer(MainActivity.this);
		if (v.getId() == 0) {
			setSelector(0);
		} else if (v.getId() == 1) {
			setSelector(1);
		} else if (v.getId() == 2) {
			setSelector(2);
		} else if (v.getId() == 3) {
			setSelector(3);
		} else if (v.getId() == 4) {
			setSelector(4);
		}
	}

	@Override
	public void onFocusChange(View v, boolean arg1) {
		/*
		 * MediaPlayer shakeRuning = MediaPlayer.create(MainActivity.this,
		 * R.raw.move_left);
		 */
		if(arg1){
			TimerPingbaoUtil.CancelPingbaoTimer();
			TimerPingbaoUtil.StartPingbaoTimer(MainActivity.this);
		}
		if (arg1) {
			adapter.notifyDataSetChanged();
			setSelector(v.getId());
			/*
			 * shakeRuning.start(); shakeRuning.stop();
			 */
			UP_TIANQI = 1;
			downWhether = false;
		} else {
			// setSelector2(v.getId());
			UP_TIANQI = 0;
		}

	}

	class FragAdapter extends FragmentStatePagerAdapter {

		public FragAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;

			switch (position) {
			case 0:
				HomeFragment homeFragment = new HomeFragment();
				mFragmentInterfaces.set(position, homeFragment);
				fragment = homeFragment;
				break;
			/*case 1:
				DianboFragment dianboFragment = new DianboFragment();
				mFragmentInterfaces.set(position, dianboFragment);
				fragment = dianboFragment;
				break;
			case 2:
				LocalFragment localFragment = new LocalFragment();
				mFragmentInterfaces.set(position, localFragment);
				fragment = localFragment;
				break;*/
			case 1:
				AllAppFragment allAppFragment = new AllAppFragment();
				mFragmentInterfaces.set(position, allAppFragment);
				fragment = allAppFragment;
				break;
			case 2:
				SettingsAppFragment setFragment = new SettingsAppFragment();
				mFragmentInterfaces.set(position, setFragment);
				fragment = setFragment;
				break;
			
			default:
				break;
			}
			return fragment;

		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
//			return mFragmentInterfaces.size();  无奈之举啊，盒子不报错，平板就是报错，可能数目突变了，望后来人找到原因
			return 3;
		}

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
		// added by lgj,解决全部应用进推荐应用返回时偶尔快捷加载不完全
		FragmentInterfaces fragmentInterfaces = mFragmentInterfaces
				.get(currentWhichPage);
		fragmentInterfaces.show();

		StatService.onResume(this);
        Log.d(TAG, "onResume().........");
		/*
		 * 壁纸
		 */
		mainRelativeLayout = (RelativeLayout) this.findViewById(R.id.main_r);

		int wallpaperIdCurrent = SharedPreferencesUtils.getsum(MainActivity.this,
				"wallpaperID");
		if (wallpaperIdCurrent != 0) {
			// mainRelativeLayout.setBackgroundResource(wallpaperIdCurrent);
			mainRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			mainRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_1));
		}

//		System.out.println("可用内存" + getAvailMemory(this));
//		System.out.println("总内存" + getTotalMemory(this));
//		// clear(this);
//		System.out.println("清理空间,可用内存" + getAvailMemory(this));

		// added 11.20,隐藏3288下面状态栏
		// getWindow().getDecorView().setSystemUiVisibility(
		// View.GONE);

		/**
		 * 设置为横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (downWhether) {
				downWhether = false;
				viewFlipper_wether = false;
				DOWN_NUM = 1;
				but_view.setFocusable(true);
				but_view.requestFocus();
				return true;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (UP_TIANQI == 1) {
				tianqi_touming_Button.setFocusable(true);
				tianqi_touming_Button.requestFocus();
				return true;
			}
			if (Constant.UPWhether) {
				Constant.UPWhether = false;
				viewFlipper_wether = false;
				DOWN_NUM = 1;
				but_view.setFocusable(true);
				but_view.requestFocus();
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
	 * 
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		System.out.println("networkInfo.getExtraInfo() is "
				+ networkInfo.getExtraInfo());
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = CMNET;
			} else {
				netType = CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		}
		return netType;

	}

	public int getData_day() {
		int day, month, year, sum, leap;
		sum = 1000;
		String str;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String data = df.format(new Date());
		year = new Date().getYear() + 1900;
		month = new Date().getMonth() + 1;
		day = new Date().getDate();
		System.out.println(year + " " + month + " " + day);
		switch (month)// 先计算某月以前月份的总天数
		{
		case 1:
			sum = 0;
			break;
		case 2:
			sum = 31;
			break;
		case 3:
			sum = 59;
			break;
		case 4:
			sum = 90;
			break;
		case 5:
			sum = 120;
			break;
		case 6:
			sum = 151;
			break;
		case 7:
			sum = 181;
			break;
		case 8:
			sum = 212;
			break;
		case 9:
			sum = 243;
			break;
		case 10:
			sum = 273;
			break;
		case 11:
			sum = 304;
			break;
		case 12:
			sum = 334;
			break;
		default:
			System.out.println("data error");
			break;
		}
		sum = sum + day;// eg:假如为3月5日 那么sum表示第“sum=59+5”天　　
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))// 判断是不是闰年　
			leap = 1;
		else
			leap = 0;
		if (leap == 1 && month > 2)// 如果是闰年且月份大于2，总天数自加一天　
			sum++;
		if (sum > 365)
			System.out.println("data error");
		else
			System.out.println("It is the" + " " + sum + "th" + " " + "day");
		return sum;
	}

	public int getDataDeskt() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String data = df.format(new Date());
		int yean = new Date().getYear() + 1900;
		return yean;
	}

	public void getcurrentimage() {
		if (Constant.map.get(Constant.nodes[0]).equals("晴")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_01);
		} else if (Constant.map.get(Constant.nodes[0]).equals("多云")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_02);
		} else if (Constant.map.get(Constant.nodes[0]).equals("阴")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_04);
		} else if (Constant.map.get(Constant.nodes[0]).equals("雾")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_05);
		} else if (Constant.map.get(Constant.nodes[0]).equals("沙尘暴")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_06);
		} else if (Constant.map.get(Constant.nodes[0]).equals("阵雨")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_07);
		} else if (Constant.map.get(Constant.nodes[0]).equals("小雨")
				|| Constant.map.get(Constant.nodes[0]).equals("中雨")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_08);
		} else if (Constant.map.get(Constant.nodes[0]).equals("大雨")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_09);
		} else if (Constant.map.get(Constant.nodes[0]).equals("雷阵雨")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_10);
		} else if (Constant.map.get(Constant.nodes[0]).equals("小雪")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_11);
		} else if (Constant.map.get(Constant.nodes[0]).equals("大雪")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_12);
		} else if (Constant.map.get(Constant.nodes[0]).equals("雨夹雪")) {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_13);
		} else {
			currentimage
					.setBackgroundResource(R.drawable.weathericon_condition_17);
		}
	}

	/**
	 * 所有的程序，都扫描到列表里。判断有没有这个apk
	 */
	public Intent panduan_apps_to_list_all(String baoName, String zhuleiName) {
		for (int i = 0; i < GetAppInfo.list.size(); i++) {

			// icon = reInfo.loadIcon(pm); // 获得应用程序图标
			if (GetAppInfo.list.get(i).getPkgName().equals(baoName)
					&& GetAppInfo.list.get(i).getActivityName()
							.equals(zhuleiName)) {
				// 为应用程序的启动Activity 准备Intent
				return GetAppInfo.list.get(i).getIntent();
			}
		}
		return null;
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();

	}

	
	
	/**
	 * 监听客户端消息广播，更新界面
	 */
	public static final String CHECKNET_ACTION_NAME = "infomation_from_net";
	
	/**
	 * 判斷有沒註冊廣播
	 */
	private static boolean weatherUpdateFlag=false;
	/**
	 * 被通知要更新天氣的廣播
	 */
	private BroadcastReceiver updateWeatherReceiver=new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			String action = intent.getAction();

	//		if(action.equals(CHECKNET_ACTION_NAME)){
			if(false){
				Log.d(TAG, "定位天氣,先检测本地是否有保存");
				
				
				
				String cityName = null;
				try {
					cityName = SharedPreferencesUtils.getData(MainActivity.this, "weathercityname");
				} catch (Exception e) {

				}
				
				if(cityName!=null){
					if (NetCheck.checkNetWorkStatus(MainActivity.this)) {
						if (cityName == null || cityName.equals("")) {
							cityName = SharedPreferencesUtils.getData(MainActivity.this, "weathercityname");
							if (cityName == null || cityName.equals("")) {
								cityName = cityField.getText().toString().trim();
							}
						}
						city = WeatherManager.getAdd1(cityName);
//						cityField.setText(city);
						mUIHandler.sendEmptyMessage(20);
						Log.d(TAG, "定位天氣,onReceive");
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									map = WeatherManager.getTianqi1(city);
									if (map != null || map.size() != 0) {
//										if (!Constant.map.get(Constant.nodes[0]).equals(
//												null))
											if (Constant.map.get(Constant.nodes[0])!=
													null)
											handler.sendEmptyMessage(Constant.HAND_WEATHER_UP);
									}
									mUIHandler.sendEmptyMessage(DIALOG__TIANQI_OFF);
								} catch (Exception ex) {
									ex.printStackTrace();
								}

							}
						}).start();
					} else {
						Toast.makeText(getApplicationContext(), "网络异常！请检查网络",
								Toast.LENGTH_LONG).show();
					}

				}else{
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
//								while (true) {
									if (NetCheck.checkNetWorkStatus(MainActivity.this)) {
										String x = WeatherManager.GetNetIp();
										if (!(x == null)) {
											String city = WeatherManager.getCityByIp(x);
											address = WeatherManager.getAdd(city);
											SharedPreferencesUtils.setData(MainActivity.this,
													"weathercityname", address);
											map = WeatherManager.getTianqi1(address);
											handler.sendEmptyMessage(Constant.HAND_WEATHER);

											/*
											 * Toast.makeText(MainActivity.this, "城市:" +
											 * address + "更新成功", 1).show();
											 */
											// mUIHandler.sendEmptyMessage(DIALOG__TIANQI_OFF);
										} else {
											// Toast.makeText(MainActivity.this,
											// "请检查网络是否连接成功！", 1).show();
											mUIHandler.sendEmptyMessage(DIALOG__TIANQI_ON);
										}
									} else {
										mUIHandler.sendEmptyMessage(DIALOG__TIANQI_ON);
									}
//								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}

						}
					}).start();
				}
			}
		}
	};
	
	/**
	 * 註冊廣播
	 */
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(CHECKNET_ACTION_NAME);
		
		// 注册广播
		registerReceiver(updateWeatherReceiver, myIntentFilter);
		weatherUpdateFlag=true;
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(weatherUpdateFlag==false){
		  registerBoradcastReceiver();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(weatherUpdateFlag==true){
			unregisterReceiver(updateWeatherReceiver);
		}
		
		weatherUpdateFlag=false;
		if (broadcastReceiver!=null) {
			unregisterReceiver(broadcastReceiver);
		}
	}
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(SERIAL_UPDATE)) {
				if (!NetCheck.checkNetWorkStatus(MainActivity.this)) {
					Toast.makeText(MainActivity.this, "网络未连接", 2000).show();
				}else{
//					getVersionCodeHttp();
					String ver = update.getAppVersionName(MainActivity.this);
					double ver_dou = Double.parseDouble(ver);
					double http_dou = Double.parseDouble(update_main.versionCode);
					
					//当网络版本高于本地版本，就要进行更新
					if (http_dou>ver_dou) {
						Toast.makeText(MainActivity.this, "需要升级", Toast.LENGTH_SHORT).show();
//						Check_ui_update();
						update = new update_main(MainActivity.SetContext);
						try {
							ui_update();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else{
						Toast.makeText(MainActivity.this, "当前已经是最新版本", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
		
	};
	
	  @Override
		protected void onRestart() {
			// TODO Auto-generated method stub
			super.onRestart();
			TimerPingbaoUtil.CancelPingbaoTimer();
			TimerPingbaoUtil.StartPingbaoTimer(this);
			Log.i("onRestart()", "onRestartonRestartonRestartonRestartonRestartonRestart");
		}
		 @Override
		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			Log.i("onStop()", "onStoponStoponStoponStoponStoponStoponStop");
			TimerPingbaoUtil.CancelPingbaoTimer();
		}
		 
		
	
}
