package com.util.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.csw.csw_desktop_yahao.MainActivity;
import com.zhongqin.tianlai.R;
import com.csw.download.DownManager;
import com.example.system.AppleMainActivity_kong;
import com.example.system.ShezhiActivity;
import com.example.system.UserMainActivity_kong;
import com.szy.update.update_main;
import com.weather.manager.TianqiActivity;
import com.wyf.allapp.GetAppInfo;
import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.NetCheck;
import com.wyf.util.SharedPreferencesUtils;
import com.wyf.util.Utils;
import com.wyf.wanglu.YouxianConnect;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class SetFragment extends Fragment implements FragmentInterfaces{

	/** 网络点播 */
	private FrameLayout first_net_set_layout;
	private FrameLayout second_user_set_layout;
	private FrameLayout third_more_layout;
	private FrameLayout fourth_duoping_set_layout;
	private FrameLayout fifth_file_set_layout;
	private FrameLayout sixed_clean_set_layout;
	private FrameLayout seventh_update_set_layout;
	private FrameLayout eighth_restart_set_layout;
	private ImageView set_whiteBorder;
	private ImageView setxiao_whiteBorder;
	/** */
	private GetAppInfo getAppInfo ;
	/** */
	private final static int SOUSU_YINGYONG = 2;
	private final static int DOWNLOAD_SUM = 3;
	HashMap<String, String> mHashMap = new HashMap<String, String>();
	private String url;
	private String name;
	DownManager manager;
	// 泰洁视频2.7.6.1
	private String dianbo_bao = "com.togic.livevideo";
	private String dianbo_lei = "com.togic.launcher.SplashActivity";
	private static final int CMNET = 3;
	private static final int CMWAP = 2;
	private static final int WIFI = 1;
//	private Context context;
	private String weathercityname;
	private String weathermin;
	private String weathermax;
	private String weather;
	private String weatherpm;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getAppInfo = new GetAppInfo(getActivity());
		manager = new DownManager(getActivity());
	}
	public SetFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SetFragment(Context context) {
		super();
//		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v5 = inflater.inflate(R.layout.shezhi_main, container, false);
		first_net_set_layout = (FrameLayout) v5
				.findViewById(R.id.first_net_set_layout);
		second_user_set_layout = (FrameLayout) v5
				.findViewById(R.id.second_user_set_layout);
		third_more_layout = (FrameLayout) v5
				.findViewById(R.id.third_more_set_layout);
		fourth_duoping_set_layout = (FrameLayout) v5
				.findViewById(R.id.fourth_duoping_set_layout);
		fifth_file_set_layout = (FrameLayout) v5
				.findViewById(R.id.fifth_file_set_layout);
		sixed_clean_set_layout = (FrameLayout) v5
				.findViewById(R.id.sixed_clean_set_layout);
		seventh_update_set_layout = (FrameLayout) v5
				.findViewById(R.id.seventh_update_set_layout);
		eighth_restart_set_layout = (FrameLayout) v5
				.findViewById(R.id.eighth_restart_set_layout);
		
		first_net_set_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		second_user_set_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		third_more_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		fourth_duoping_set_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		// shezhi_3.setOnFocusChangeListener(new MyOnFocusChangeListener());
		fifth_file_set_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		sixed_clean_set_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		seventh_update_set_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		// shezhi_7.setOnFocusChangeListener(new MyOnFocusChangeListener());
		eighth_restart_set_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		first_net_set_layout.setOnClickListener(new MyOnClickListener());
		second_user_set_layout.setOnClickListener(new MyOnClickListener());
		third_more_layout.setOnClickListener(new MyOnClickListener());
		fourth_duoping_set_layout.setOnClickListener(new MyOnClickListener());
		fifth_file_set_layout.setOnClickListener(new MyOnClickListener());
		// shezhi_3.setOnClickListener(new MyOnClickListener());
		sixed_clean_set_layout.setOnClickListener(new MyOnClickListener());
		seventh_update_set_layout.setOnClickListener(new MyOnClickListener());
		eighth_restart_set_layout.setOnClickListener(new MyOnClickListener());
		/** */
		set_whiteBorder = (ImageView) v5.findViewById(R.id.scoll_set_layout);
		setxiao_whiteBorder = (ImageView) v5.findViewById(R.id.scoll_setxiao_layout);
		return v5;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * @author Robin-wu
	 * @param 平移动画 设置
	 */
	@SuppressLint("NewApi")
	private void flyWhiteBorder_set_scroll(View paramView) {
		if ((this.set_whiteBorder != null) && (paramView != null)) {
			this.set_whiteBorder.setVisibility(0);
			ViewPropertyAnimator localViewPropertyAnimator = this.set_whiteBorder
					.animate();
			localViewPropertyAnimator.setDuration(200L);
			localViewPropertyAnimator.x(paramView.getX() -9);
			localViewPropertyAnimator.y(paramView.getY() -13);
			localViewPropertyAnimator.start();
		}
	}
	/**
	 * @author Robin-wu
	 * @param 平移动画 设置小
	 */
	@SuppressLint("NewApi")
	private void flyWhiteBorder_setxiao_scroll(View paramView) {
		if ((this.setxiao_whiteBorder != null) && (paramView != null)) {
			this.setxiao_whiteBorder.setVisibility(0);
			ViewPropertyAnimator localViewPropertyAnimator = this.setxiao_whiteBorder
					.animate();
			localViewPropertyAnimator.setDuration(200L);
			localViewPropertyAnimator.x(paramView.getX() -9);
			localViewPropertyAnimator.y(paramView.getY() -10);
			localViewPropertyAnimator.start();
		}
	}

	Handler app_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SOUSU_YINGYONG:
				Toast.makeText(getActivity(), "找不到该应用", Toast.LENGTH_LONG)
						.show();
				break;
			case DOWNLOAD_SUM:
				mHashMap.put("url", "http://112.124.43.99/apk/xiazaiDownload/"
						+ url + ".apk");
				mHashMap.put("name", name);
				// 检查软件下载
				manager.checkUpdate(mHashMap);
			default:
				break;
			}
		}
	};

	class MyOnFocusChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean arg1) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			// 设置
			case R.id.first_net_set_layout:
				flyWhiteBorder_set_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					set_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					set_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.second_user_set_layout:
				flyWhiteBorder_set_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					set_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					set_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.third_more_set_layout:
				flyWhiteBorder_setxiao_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					setxiao_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					setxiao_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.fourth_duoping_set_layout:
				flyWhiteBorder_set_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					set_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					set_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.fifth_file_set_layout:
				flyWhiteBorder_set_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					set_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					set_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.sixed_clean_set_layout:
				flyWhiteBorder_setxiao_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					setxiao_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					setxiao_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.seventh_update_set_layout:
				flyWhiteBorder_setxiao_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					setxiao_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					setxiao_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = false;
				break;
			case R.id.eighth_restart_set_layout:

				flyWhiteBorder_setxiao_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					setxiao_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					setxiao_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = false;
				break;

			default:
				break;
			}
		}

	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.first_net_set_layout:
				/*Intent but_shezhi_1 = new Intent();
				but_shezhi_1.setComponent(new ComponentName("com.csw.new_wifi",
						"com.csw.new_wifi.MainActivity"));

				Intent but_shezhi_1_s = panduan_apps_to_list_all(
						"com.csw.new_wifi", "com.csw.new_wifi.MainActivity");
				if (but_shezhi_1_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "csw_wifi";
					name = "csw_wifi";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				int net_leixing = getAPNType(getActivity());
				if (net_leixing == WIFI) {
					startActivity(but_shezhi_1);
				} else {

					if (!NetCheck.checkNetWorkStatus(getActivity())) {
						startActivity(but_shezhi_1);
					} else {
						Intent but_shezhi_1_youxian = new Intent();
						but_shezhi_1_youxian.setClass(getActivity(),
								YouxianConnect.class);
						startActivity(but_shezhi_1_youxian);
					}
				}*/
				//雅豪改成系统设置
				Intent shezhi = new Intent();
				shezhi.setComponent(new ComponentName("com.android.settings",
						"com.android.settings.Settings"));
				startActivity(shezhi);
				break;
			case R.id.second_user_set_layout:
				Intent but_shezhi_2 = new Intent();
				but_shezhi_2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				but_shezhi_2.setClass(getActivity(),
						UserMainActivity_kong.class);
				startActivity(but_shezhi_2);
				break;
			// 显示设置
			/*
			 * case R.id.but_shezhi_3: Intent but_shezhi_3 = new Intent();
			 * but_shezhi_3.setComponent(new ComponentName(
			 * "com.android.settings", "com.android.settings.DisplaySettings"));
			 * but_shezhi_3.setClass(MainActivity.this, SettingTest.class);
			 * startActivity(but_shezhi_3); break;
			 */
			// 更多设置
			case R.id.third_more_set_layout:
				/*
				 * Intent shezhi = new Intent(); shezhi.setComponent(new
				 * ComponentName("com.android.settings",
				 * "com.android.settings.Settings"));
				 * 
				 * Intent but_shezhi_4_s =
				 * panduan_apps_to_list_all("com.android.settings",
				 * "com.android.settings.Settings"); if(but_shezhi_4_s == null){
				 * app_handler.sendEmptyMessage(SOUSU_YINGYONG); break; }
				 * startActivity(shezhi);
				 */
				Intent but_shezhi_4 = new Intent();
				but_shezhi_4.setClass(getActivity(), ShezhiActivity.class);
				startActivity(but_shezhi_4);
				break;

			case R.id.fourth_duoping_set_layout:
				/*Intent but_shezhi_5 = new Intent();
				but_shezhi_5.setComponent(new ComponentName(
						"com.wukongtv.wkhelper",
						"com.wukongtv.wkhelper.MainActivity"));
				ComponentName intent_5 = but_shezhi_5.getComponent();
				if (intent_5 == null)
					return;
				Intent but_shezhi_5_s = panduan_apps_to_list_all(
						"com.wukongtv.wkhelper",
						"com.wukongtv.wkhelper.MainActivity");
				if (but_shezhi_5_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "wukongyaokong";
					name = "wukongyaokong";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(but_shezhi_5);*/
				
				Intent but_shezhi_5 = new Intent();
				but_shezhi_5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				but_shezhi_5.setClass(getActivity(),
						AppleMainActivity_kong.class);
				startActivity(but_shezhi_5);
				
				break;
			// 本地天气
			case R.id.fifth_file_set_layout:
				weathercityname =	SharedPreferencesUtils.getData(getActivity(),"weathercityname");
				weathermin = SharedPreferencesUtils.getData(getActivity(),"weathermin");
				weathermax = SharedPreferencesUtils.getData(getActivity(),"weathermax");
				weather = SharedPreferencesUtils.getData(getActivity(),"weather");
				weatherpm = SharedPreferencesUtils.getData(getActivity(),"weatherpm");
				if(weathercityname ==null || weathercityname.equals("")){
					weathercityname = "北京" ;
				}if(weathermin ==null || weathermin.equals("")){
					weathermin = "20" ;
				}if(weathermax ==null || weathermax.equals("")){
					weathermax = "25" ;
				}if(weather ==null || weather.equals("")){
					weather = "晴" ;
				}if(weatherpm ==null || weatherpm.equals("")){
					weatherpm = "优" ;
				}
				Intent but_shezhi_6 = new Intent(getActivity(),
						TianqiActivity.class);
				Bundle bundle_weather = new Bundle();
				bundle_weather.putString("cityname", weathercityname);
				bundle_weather.putString("min", weathermin);
				bundle_weather.putString("max", weathermax);
				bundle_weather.putString("weather", weather);
				bundle_weather.putString("pm", weatherpm);
				but_shezhi_6.putExtras(bundle_weather);
				startActivityForResult(but_shezhi_6, 0);
				break;
			case R.id.sixed_clean_set_layout:
				Intent but_shezhi_clean = new Intent();
				but_shezhi_clean.setComponent(new ComponentName("com.tv.clean",
						"com.tv.clean.HomeAct"));
				Intent but_shezhi_clean_s = panduan_apps_to_list_all(
						"com.tv.clean", "com.tv.clean.HomeAct");
				if (but_shezhi_clean_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "clean";
					name = "clean";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(but_shezhi_clean);

				break;
			case R.id.seventh_update_set_layout:
				/*Intent but_shezhi_zaixianshenji = new Intent();
				but_shezhi_zaixianshenji.setComponent(new ComponentName(
						"com.csw.apkupdata_gb", "com.csw.apkupdata_gb.MainActivity"));
				Intent but_shezhi_zaixianshenji_s = panduan_apps_to_list_all(
						"com.csw.apkupdata_gb", "com.csw.apkupdata_gb.MainActivity");
				if (but_shezhi_zaixianshenji_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "apkupdate_gb";
					name = "apkupdate_gb";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(but_shezhi_zaixianshenji);*/
				// jiance_shengji();
				
				if (!NetCheck.checkNetWorkStatus(getActivity())) {
					Toast.makeText(getActivity(), "网络未连接", 2000).show();
				}else{
//					getVersionCodeHttp();
					String ver = update.getAppVersionName(getActivity());
					double ver_dou = Double.parseDouble(ver);
					double http_dou = Double.parseDouble(update_main.versionCode);
					
					//当网络版本高于本地版本，就要进行更新
					if (http_dou>ver_dou) {
						Toast.makeText(getActivity(), "需要升级", Toast.LENGTH_SHORT).show();
//						Check_ui_update();
						update = new update_main(MainActivity.SetContext);
						try {
							ui_update();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else{
						Toast.makeText(getActivity(), "当前已经是最新版本", Toast.LENGTH_LONG).show();
					}
				}
				
				
				
				
				break;
			case R.id.eighth_restart_set_layout:
				Intent but_shezhi_xitongchongqi = new Intent();
				but_shezhi_xitongchongqi.setComponent(new ComponentName(
						"com.example.reset", "com.example.reset.MainActivity"));

				Intent but_shezhi_xitongchongqi_s = panduan_apps_to_list_all(
						"com.example.reset", "com.example.reset.MainActivity");
				if (but_shezhi_xitongchongqi_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "3288chongqi";
					name = "3288chongqi";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(but_shezhi_xitongchongqi);

				break;
			default:
				break;
			}
		}

	}

	/**
	 * 所有的程序，都扫描到列表里。判断有没有这个apk
	 */
	public Intent panduan_apps_to_list_all(String baoName, String zhuleiName) {
		for (int i = 0; i < GetAppInfo.list.size(); i++) {

			// icon = reInfo.loadIcon(pm); // 获得应用程序图标
			if (GetAppInfo.list.get(i).getPkgName().equals(baoName)
					&& GetAppInfo.list.get(i).getActivityName().equals(zhuleiName)) {
				// 为应用程序的启动Activity 准备Intent
				return GetAppInfo.list.get(i).getIntent();
			}
		}
		return null;
	}
	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
	 * 
	 * @param getActivity()
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

	
	 @Override
	    public void setMenuVisibility(boolean menuVisible) {
	        super.setMenuVisibility(menuVisible);
	        if (this.getView() != null)
	            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	    }
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	update_main update = new update_main(MainActivity.SetContext);
	private static String versionCode = "0";
	/*
	 * added 2015.1.6
	 */
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
	
}
