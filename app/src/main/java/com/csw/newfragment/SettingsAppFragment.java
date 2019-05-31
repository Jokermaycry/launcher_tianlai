package com.csw.newfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.zhongqin.tianlai.R;
import com.csw.csw_desktop_yahao.MainActivity;
import com.csw.download.DownManager;
import com.example.system.ShezhiActivity;
import com.example.system.qiActivity;
import com.szy.update.update_main;
import com.util.fragment.FragmentInterfaces;
import com.wyf.allapp.ALLappActivity;
import com.wyf.allapp.AddKJappActivity;
import com.wyf.allapp.GetAppInfo;
import com.wyf.allapp.MusicAppActivity;

import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.NetCheck;
import com.wyf.util.SharedPreferencesUtils;
import com.wyf.util.TimerPingbaoUtil;
import com.wyf.util.Utils;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class SettingsAppFragment extends Fragment implements
		FragmentInterfaces {
	private ImageButton btn_pinbao;
	private ImageButton btn_start_app;
	private ImageButton btn_one_clean;
	private ImageButton btn_rili;
	private ImageButton btn_add_app;
	private ImageButton btn_system_settings;
	private ImageButton btn_system_upgrade;
	
	// 全部应用
	private LinearLayout l_all_1;
	private LinearLayout l_all_2;
	private LinearLayout l_all_3;
	private LinearLayout l_all_4;
	private LinearLayout l_all_5;
	private LinearLayout l_all_6;
	private LinearLayout l_all_7;

	private ImageView l_all_1_image;
	private ImageView l_all_2_image;
	private ImageView l_all_3_image;
	private ImageView l_all_4_image;
	private ImageView l_all_5_image;
	private ImageView l_all_6_image;
	private ImageView l_all_7_image;

	private TextView l_all_1_text;
	private TextView l_all_2_text;
	private TextView l_all_3_text;
	private TextView l_all_4_text;
	private TextView l_all_5_text;
	private TextView l_all_6_text;
	private TextView l_all_7_text;
	
	private TextView l_all_1_text_hide;
	private TextView l_all_2_text_hide;
	private TextView l_all_3_text_hide;
	private TextView l_all_4_text_hide;
	private TextView l_all_5_text_hide;
	private TextView l_all_6_text_hide;
	private TextView l_all_7_text_hide;


	/** */
	private SharedPreferencesUtils preferencesUtils;
	private List<AppInfo> App_List = new ArrayList<AppInfo>();
	private final static int ALL_APP = 1;
	/** */
	private final static int SOUSU_YINGYONG = 2;
	private final static int DOWNLOAD_SUM = 3;
	HashMap<String, String> mHashMap = new HashMap<String, String>();
	private String url;
	private String name;
	/** */
	private ImageView app_whiteBorder;
	private ImageView appxiao_whiteBorder;
	private DownManager manager;
	private GetAppInfo getAppInfo;
	private Context mContext;
	update_main update = new update_main(MainActivity.SetContext);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getAppInfo = new GetAppInfo(getActivity());
		manager = new DownManager(getActivity());
		app_add_all();
	}
	public SettingsAppFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SettingsAppFragment(Context context) {
		super();
	//	this.context = context;
		// TODO Auto-generated constructor stub
	}

	private void app_add_all() {
		if(getActivity() == null){
			return ;
		}
		
		APP_run();
		
	}

	private void app_add_all_init() {
//		getAppInfo.getAllAPK_add();
		if (App_List != null) {
			App_List.clear();

		}
		int sum = preferencesUtils.getsum(getActivity(), "music_app_sum");
		String value = null;
		AppInfo appInfo = null;
		for (int i = 1; i <= 7; i++) {
			value = preferencesUtils.getData(getActivity(), "music_app" + i);
			if (value != null) {
				// appInfo =
				// getAppInfo.quanbuyingyong_saomiao_apps_to_open(value);
				appInfo = pkgName_compare(value);
			} else {
				continue;
			}
			if (appInfo == null) {
				continue;
			}
			App_List.add(appInfo);
		}
		app_handler.sendEmptyMessage(ALL_APP);
	}

	//added by lgj 12.24
    Drawable backDrawable;
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		backDrawable=getResources()
				.getDrawable(R.drawable.taoming_icon);
	}
	
	
	Handler app_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ALL_APP:
				if(getActivity() == null){
					return ;
				}
				int sum = App_List.size();
				if (App_List.size() >= 1) {
					l_all_1_image.setImageDrawable(App_List.get(0)
							.getAppIcon());
					l_all_1_text.setText(App_List.get(0).getAppLabel());
					Log.d(TAG, "App_List.get(0).getPkgName()=="+App_List.get(0).getPkgName());
					l_all_1_text_hide.setText(App_List.get(0).getPkgName());
				} else {
					l_all_1_image.setImageDrawable(backDrawable);
					l_all_1_text.setText("");
					l_all_1_text_hide.setText("");
				}
				if (App_List.size() >= 2) {
					l_all_2_image.setImageDrawable(App_List.get(1)
							.getAppIcon());
					l_all_2_text.setText(App_List.get(1).getAppLabel());
					l_all_2_text_hide.setText(App_List.get(1).getPkgName());
				} else {
					l_all_2_image.setImageDrawable(backDrawable);
					l_all_2_text.setText("");
					l_all_2_text_hide.setText("");
				}
				if (App_List.size() >= 3) {
					l_all_3_image.setImageDrawable(App_List.get(2)
							.getAppIcon());
					l_all_3_text.setText(App_List.get(2).getAppLabel());
					l_all_3_text_hide.setText(App_List.get(2).getPkgName());
				} else {
					l_all_3_image.setImageDrawable(backDrawable);
					l_all_3_text.setText("");
					l_all_3_text_hide.setText("");
				}
				if (App_List.size() >= 4) {
					l_all_4_image.setImageDrawable(App_List.get(3)
							.getAppIcon());
					l_all_4_text.setText(App_List.get(3).getAppLabel());
					l_all_4_text_hide.setText(App_List.get(3).getPkgName());
				} else {
					l_all_4_image.setImageDrawable(backDrawable);
					l_all_4_text.setText("");
					l_all_4_text_hide.setText("");
				}
				if (App_List.size() >= 5) {
					l_all_5_image.setImageDrawable(App_List.get(4)
							.getAppIcon());
					l_all_5_text.setText(App_List.get(4).getAppLabel());
					l_all_5_text_hide.setText(App_List.get(4).getPkgName());
				} else {
					l_all_5_image.setImageDrawable(backDrawable);
					l_all_5_text.setText("");
					l_all_5_text_hide.setText("");
				}
				if (App_List.size() >= 6) {
					l_all_6_image.setImageDrawable(App_List.get(5)
							.getAppIcon());
					l_all_6_text.setText(App_List.get(5).getAppLabel());
					l_all_6_text_hide.setText(App_List.get(5).getPkgName());
				} else {
					l_all_6_image.setImageDrawable(backDrawable);
					l_all_6_text.setText("");
					l_all_6_text_hide.setText("");
				}
				if (App_List.size() >= 7) {
					l_all_7_image.setImageDrawable(App_List.get(6)
							.getAppIcon());
					l_all_7_text.setText(App_List.get(6).getAppLabel());
					l_all_7_text_hide.setText(App_List.get(6).getPkgName());
				} else {
					l_all_7_image.setImageDrawable(backDrawable);
					l_all_7_text.setText("");
					l_all_7_text_hide.setText("");
				}
				break;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		View v4 = inflater.inflate(R.layout.settingsfragment, container, false);
		// 全部应用
		btn_add_app=(ImageButton) v4.findViewById(R.id.btn_settingsadd_app);
		btn_one_clean=(ImageButton) v4.findViewById(R.id.btn_one_clean);
		btn_pinbao=(ImageButton) v4.findViewById(R.id.btn_pingbao);
		btn_rili=(ImageButton) v4.findViewById(R.id.btn_rili);
		btn_start_app=(ImageButton) v4.findViewById(R.id.btn_start_app);
		btn_system_settings=(ImageButton) v4.findViewById(R.id.btn_system_settings);
		btn_system_upgrade=(ImageButton) v4.findViewById(R.id.btn_system_upgrade);
		
		btn_add_app.setOnClickListener(new MyOnClickListener());
		btn_one_clean.setOnClickListener(new MyOnClickListener());
		btn_pinbao.setOnClickListener(new MyOnClickListener());
		btn_rili.setOnClickListener(new MyOnClickListener());
		btn_start_app.setOnClickListener(new MyOnClickListener());
		btn_system_settings.setOnClickListener(new MyOnClickListener());
		btn_system_upgrade.setOnClickListener(new MyOnClickListener());
		
		
		l_all_1 = (LinearLayout) v4.findViewById(R.id.linear_settingsapp1);
		l_all_2 = (LinearLayout) v4.findViewById(R.id.linear_settingsapp2);
		l_all_3 = (LinearLayout) v4.findViewById(R.id.linear_settingsapp3);
		l_all_4 = (LinearLayout) v4.findViewById(R.id.linear_settingsapp4);
		l_all_5 = (LinearLayout) v4.findViewById(R.id.linear_settingsapp5);
		l_all_6 = (LinearLayout) v4.findViewById(R.id.linear_settingsapp6);
		l_all_7 = (LinearLayout) v4.findViewById(R.id.linear_settingsapp7);
		
		l_all_1_image = (ImageView) v4.findViewById(R.id.l_settings_1_image);
		l_all_2_image = (ImageView) v4.findViewById(R.id.l_settings_2_image);
		l_all_3_image = (ImageView) v4.findViewById(R.id.l_settings_3_image);
		l_all_4_image = (ImageView) v4.findViewById(R.id.l_settings_4_image);
		l_all_5_image = (ImageView) v4.findViewById(R.id.l_settings_5_image);
		l_all_6_image = (ImageView) v4.findViewById(R.id.l_settings_6_image);
		l_all_7_image = (ImageView) v4.findViewById(R.id.l_settings_7_image);
	
		l_all_1_text = (TextView) v4.findViewById(R.id.l_settings_1_text);
		l_all_2_text = (TextView) v4.findViewById(R.id.l_settings_2_text);
		l_all_3_text = (TextView) v4.findViewById(R.id.l_settings_3_text);
		l_all_4_text = (TextView) v4.findViewById(R.id.l_settings_4_text);
		l_all_5_text = (TextView) v4.findViewById(R.id.l_settings_5_text);
		l_all_6_text = (TextView) v4.findViewById(R.id.l_settings_6_text);
		l_all_7_text = (TextView) v4.findViewById(R.id.l_settings_7_text);
		
		l_all_1_text_hide = (TextView) v4.findViewById(R.id.l_settings_1_text_hide);
		l_all_2_text_hide = (TextView) v4.findViewById(R.id.l_settings_2_text_hide);
		l_all_3_text_hide = (TextView) v4.findViewById(R.id.l_settings_3_text_hide);
		l_all_4_text_hide = (TextView) v4.findViewById(R.id.l_settings_4_text_hide);
		l_all_5_text_hide = (TextView) v4.findViewById(R.id.l_settings_5_text_hide);
		l_all_6_text_hide = (TextView) v4.findViewById(R.id.l_settings_6_text_hide);
		l_all_7_text_hide = (TextView) v4.findViewById(R.id.l_settings_7_text_hide);
	
		
		
		l_all_1.setOnClickListener(new MyOnClickListener());
		l_all_2.setOnClickListener(new MyOnClickListener());
		l_all_3.setOnClickListener(new MyOnClickListener());
		l_all_4.setOnClickListener(new MyOnClickListener());
		l_all_5.setOnClickListener(new MyOnClickListener());
		l_all_6.setOnClickListener(new MyOnClickListener());
		l_all_7.setOnClickListener(new MyOnClickListener());
	

		

		return v4;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}


	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TimerPingbaoUtil.CancelPingbaoTimer();
			TimerPingbaoUtil.StartPingbaoTimer(getActivity());
			switch (v.getId()) {
			// 全部应用
			case R.id.btn_pingbao:
				
				if(HomeFragment.setDreamPlay.equals("0")){
					   HomeFragment.setDreamPlay = "1";
					}
				Intent dreamIntent = panduan_apps_to_list_all(
						"com.android.setdream");
				if (dreamIntent == null) {
					Toast.makeText(getActivity(), "未安装屏保", 2000).show();
				}else{
					Intent mIntent=new Intent();
					ComponentName comp = new ComponentName("com.android.setdream","com.android.setdream.MainActivity");
					mIntent.setComponent(comp);
					startActivity(mIntent);
				}
				
				break;
			case R.id.btn_start_app:
				Intent qidong = new Intent();
				qidong.setClass(getActivity(), qiActivity.class);
				startActivity(qidong);
				break;
			case R.id.btn_one_clean:
				Intent but_shezhi_clean = new Intent();
				but_shezhi_clean.setComponent(new ComponentName("com.tv.clean",
						"com.tv.clean.HomeAct"));
				Intent but_shezhi_clean_s = panduan_apps_to_list_all(
						"com.tv.clean");
				if (but_shezhi_clean_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "clean";
					name = "clean";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(but_shezhi_clean);
				break;
			 case R.id.btn_rili:
				Intent riliIntent = panduan_apps_to_list_all(
						"cn.etouch.ecalendar.pad");
				if (riliIntent == null) {
					Toast.makeText(getActivity(), "未安装万年历", 2000).show();
					break;
				}
				
				Intent but_all_riliIntent= getActivity().getPackageManager()
    					.getLaunchIntentForPackage("cn.etouch.ecalendar.pad");		
				startActivity(but_all_riliIntent);
				
				break;
            case R.id.btn_system_settings:
            	Intent but_shezhi_4 = new Intent();
				but_shezhi_4.setClass(getActivity(), ShezhiActivity.class);
				startActivity(but_shezhi_4);
				break;
			case R.id.btn_system_upgrade:
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
			case R.id.btn_settingsadd_app:
				Intent but_all_kuaijie = new Intent();
				but_all_kuaijie.setClass(getActivity(), MusicAppActivity.class);
				but_all_kuaijie.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// app_add_all_init();
				preferencesUtils.setsum(getActivity(), "music_app_sum",
						App_List.size());
				
				startActivityForResult(but_all_kuaijie, 1);
				break;
			case R.id.linear_settingsapp1:

				if (l_all_1_text.getText().toString().equals("未添加")) {
					break;
				} else {
					/*
					 * Intent l_all_1 = getAppInfo
					 * .quanbuyingyong_saomiao_apps_to_open_intent(l_all_1_text
					 * .getText().toString());
					 */
					Intent l_all_1 = lableName_compare_intent(getActivity(),l_all_1_text
							.getText().toString(),l_all_1_text_hide.getText().toString());
					
					Log.d(TAG, "   "+l_all_1_text.getText().toString()+"      "+l_all_1_text_hide.getText().toString());
					if (l_all_1 != null)
						startActivity(l_all_1);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
						Log.d(TAG, "  找不到应用");
					}
				}
				break;
			case R.id.linear_settingsapp2:
				if (l_all_2_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_2 = lableName_compare_intent(getActivity(),l_all_2_text
							.getText().toString(),l_all_2_text_hide.getText().toString());
					if (l_all_2 != null)
						startActivity(l_all_2);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.linear_settingsapp3:
				if (l_all_3_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_3 = lableName_compare_intent(getActivity(),l_all_3_text
							.getText().toString(),l_all_3_text_hide.getText().toString());
					if (l_all_3 != null)
						startActivity(l_all_3);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.linear_settingsapp4:
				if (l_all_4_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_4 = lableName_compare_intent(getActivity(),l_all_4_text
							.getText().toString(),l_all_4_text_hide.getText().toString());
					if (l_all_4 != null)
						startActivity(l_all_4);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.linear_settingsapp5:
				if (l_all_5_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_5 = lableName_compare_intent(getActivity(),l_all_5_text
							.getText().toString(),l_all_5_text_hide.getText().toString());
					if (l_all_5 != null)
						startActivity(l_all_5);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
			case R.id.linear_settingsapp6:
				if (l_all_6_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_6 = lableName_compare_intent(getActivity(),l_all_6_text
							.getText().toString(),l_all_6_text_hide.getText().toString());
					if (l_all_6 != null)
						startActivity(l_all_6);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
			case R.id.linear_settingsapp7:
				if (l_all_7_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_7 = lableName_compare_intent(getActivity(),l_all_7_text
							.getText().toString(),l_all_7_text_hide.getText().toString());
					if (l_all_7 != null)
						startActivity(l_all_7);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			default:
				break;
			}
		}

	}

	private ImageView zhibo_whiteBorder;

	/**
	 * @author Robin-wu
	 * @param 平移动画
	 *            全部应用
	 */
	@SuppressLint("NewApi")
	private void flyWhiteBorder_app_scroll(View paramView) {
		if ((this.app_whiteBorder != null) && (paramView != null)) {
			this.app_whiteBorder.setVisibility(0);
			ViewPropertyAnimator localViewPropertyAnimator = this.app_whiteBorder
					.animate();
			localViewPropertyAnimator.setDuration(200L);
			localViewPropertyAnimator.x(paramView.getX() - 9);
			localViewPropertyAnimator.y(paramView.getY() - 13);
			System.out.println(paramView.getX() + " sssssssss "
					+ paramView.getY());
			localViewPropertyAnimator.start();
		}
	}

	/**
	 * @author Robin-wu
	 * @param 平移动画
	 *            全部应用小
	 */
	@SuppressLint("NewApi")
	private void flyWhiteBorder_appxiao_scroll(View paramView) {
		if ((this.appxiao_whiteBorder != null) && (paramView != null)) {
			this.appxiao_whiteBorder.setVisibility(0);
			ViewPropertyAnimator localViewPropertyAnimator = this.appxiao_whiteBorder
					.animate();
			localViewPropertyAnimator.setDuration(200L);
			localViewPropertyAnimator.x(paramView.getX() - 9);
			localViewPropertyAnimator.y(paramView.getY() - 10);
			System.out.println(paramView.getX() + " sssssssss "
					+ paramView.getY());
			localViewPropertyAnimator.start();
		}
	}

	private static final String TAG="SettingsAppFragment";
	/**
	 * 根据apk名称进行比较
	 */
	public Intent lableName_compare_intent(Context context,String lable,String packageName) {
		for (int i = 0; i < GetAppInfo.list.size(); i++) {
			if (GetAppInfo.list.get(i).getAppLabel().equals(lable)) {
				
				Log.d(TAG, "packageName=="+packageName+   "     GetAppInfo.list.get(i).getPkgName()=="+GetAppInfo.list.get(i).getPkgName());
				if(packageName.equals(GetAppInfo.list.get(i).getPkgName()))
				return context.getPackageManager().getLaunchIntentForPackage(GetAppInfo.list.get(i).getPkgName());
			}
		}
		return null;
	}
	
	/**
	 * 根据apk名称进行比较
	 */
	public Intent lableName_compare_intent(String lable) {
		for (int i = 0; i < GetAppInfo.list.size(); i++) {
			if (GetAppInfo.list.get(i).getAppLabel().equals(lable)) {
				return GetAppInfo.list.get(i).getIntent();
			}
		}
		return null;
	}

	/**
	 * 根据包名进行比较
	 */
	public AppInfo pkgName_compare(String pkgname) {
		for (int i = 0; i < GetAppInfo.list.size(); i++) {
			if (GetAppInfo.list.get(i).getPkgName().equals(pkgname)) {
				return GetAppInfo.list.get(i);
			}
		}
		return null;
	}

	/**
	 * 所有的程序，都扫描到列表里。判断有没有这个apk
	 */
	public Intent panduan_apps_to_list_all(String baoName) {
		for (int i = 0; i < GetAppInfo.list.size(); i++) {

			// icon = reInfo.loadIcon(pm); // 获得应用程序图标
			if (GetAppInfo.list.get(i).getPkgName().equals(baoName)
					) {
				// 为应用程序的启动Activity 准备Intent
				return GetAppInfo.list.get(i).getIntent();
			}
		}
		return null;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("Settings App");
		app_add_all();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
	private void APP_run(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				app_add_all_init();
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("全部应用");
	}
	
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
