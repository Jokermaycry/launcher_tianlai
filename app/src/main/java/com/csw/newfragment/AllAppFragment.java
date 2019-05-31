package com.csw.newfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.zhongqin.tianlai.R;
import com.csw.download.DownManager;
import com.util.fragment.FragmentInterfaces;
import com.wyf.allapp.ALLappActivity;
import com.wyf.allapp.AddKJappActivity;
import com.wyf.allapp.GetAppInfo;

import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.SharedPreferencesUtils;
import com.wyf.util.TimerPingbaoUtil;
import com.wyf.util.Utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class AllAppFragment extends Fragment implements
		FragmentInterfaces {
	private ImageButton btn_time_onoff;
	private ImageButton btn_photo;
	private ImageButton btn_file_manager;
	private ImageButton btn_wanpan;
	private ImageButton btn_add_app;
	private ImageButton btn_all_app;
	private ImageButton btn_android_market;
	
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
		private String bendi_bao = "com.csw.wj";
		private String bendi_lei = "com.csw.wj.MainActivity";
	/** */
	private ImageView app_whiteBorder;
	private ImageView appxiao_whiteBorder;
	private DownManager manager;
	private GetAppInfo getAppInfo;
	private Context mContext;
	
	
	public static final String ADD_APPLICATION_BUTTON = "com.yahao.broadcast.add.app";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getAppInfo = new GetAppInfo(getActivity());
		manager = new DownManager(getActivity());
		app_add_all();
	}
	public AllAppFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AllAppFragment(Context context) {
		super();
	//	this.context = context;
		// TODO Auto-generated constructor stub
	}

	private void app_add_all() {
		if(getActivity() == null){
			return ;
		}
		app_add_all_init();
		app_handler.sendEmptyMessage(ALL_APP);
	}

	private void app_add_all_init() {
		if (App_List != null) {
			App_List.clear();

		}
		int sum = preferencesUtils.getsum(getActivity(), "add_app_sum");
		String value = null;
		AppInfo appInfo = null;
		for (int i = 1; i <= 7; i++) {
			value = preferencesUtils.getData(getActivity(), "add_app" + i);
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
	}

	//added by lgj 12.24
    Drawable backDrawable;
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter intentFilter1 = new IntentFilter();
		intentFilter1.addAction(ADD_APPLICATION_BUTTON);
		getActivity().registerReceiver(mAddApproadcastReceiver, intentFilter1);
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
		View v4 = inflater.inflate(R.layout.myappfragment, container, false);
		// 全部应用
		btn_add_app=(ImageButton) v4.findViewById(R.id.btn_add_app);
		btn_all_app=(ImageButton) v4.findViewById(R.id.btn_all_app);
		btn_android_market=(ImageButton) v4.findViewById(R.id.btn_android_market);
		btn_time_onoff=(ImageButton) v4.findViewById(R.id.btn_time_onoff);
		btn_wanpan=(ImageButton) v4.findViewById(R.id.btn_wangpan);
		btn_file_manager=(ImageButton) v4.findViewById(R.id.btn_file_manager);
		btn_photo=(ImageButton) v4.findViewById(R.id.btn_photo);
		
		btn_add_app.setOnClickListener(new MyOnClickListener());
		btn_all_app.setOnClickListener(new MyOnClickListener());
		btn_android_market.setOnClickListener(new MyOnClickListener());
		btn_time_onoff.setOnClickListener(new MyOnClickListener());
		btn_wanpan.setOnClickListener(new MyOnClickListener());
		btn_file_manager.setOnClickListener(new MyOnClickListener());
		btn_photo.setOnClickListener(new MyOnClickListener());
		
		
		l_all_1 = (LinearLayout) v4.findViewById(R.id.linear_myapp1);
		l_all_2 = (LinearLayout) v4.findViewById(R.id.linear_myapp2);
		l_all_3 = (LinearLayout) v4.findViewById(R.id.linear_myapp3);
		l_all_4 = (LinearLayout) v4.findViewById(R.id.linear_myapp4);
		l_all_5 = (LinearLayout) v4.findViewById(R.id.linear_myapp5);
		l_all_6 = (LinearLayout) v4.findViewById(R.id.linear_myapp6);
		l_all_7 = (LinearLayout) v4.findViewById(R.id.linear_myapp7);
		
		l_all_1_image = (ImageView) v4.findViewById(R.id.l_all_1_image);
		l_all_2_image = (ImageView) v4.findViewById(R.id.l_all_2_image);
		l_all_3_image = (ImageView) v4.findViewById(R.id.l_all_3_image);
		l_all_4_image = (ImageView) v4.findViewById(R.id.l_all_4_image);
		l_all_5_image = (ImageView) v4.findViewById(R.id.l_all_5_image);
		l_all_6_image = (ImageView) v4.findViewById(R.id.l_all_6_image);
		l_all_7_image = (ImageView) v4.findViewById(R.id.l_all_7_image);
	
		l_all_1_text = (TextView) v4.findViewById(R.id.l_all_1_text);
		l_all_2_text = (TextView) v4.findViewById(R.id.l_all_2_text);
		l_all_3_text = (TextView) v4.findViewById(R.id.l_all_3_text);
		l_all_4_text = (TextView) v4.findViewById(R.id.l_all_4_text);
		l_all_5_text = (TextView) v4.findViewById(R.id.l_all_5_text);
		l_all_6_text = (TextView) v4.findViewById(R.id.l_all_6_text);
		l_all_7_text = (TextView) v4.findViewById(R.id.l_all_7_text);
		
		l_all_1_text_hide = (TextView) v4.findViewById(R.id.l_all_1_text_hide);
		l_all_2_text_hide = (TextView) v4.findViewById(R.id.l_all_2_text_hide);
		l_all_3_text_hide = (TextView) v4.findViewById(R.id.l_all_3_text_hide);
		l_all_4_text_hide = (TextView) v4.findViewById(R.id.l_all_4_text_hide);
		l_all_5_text_hide = (TextView) v4.findViewById(R.id.l_all_5_text_hide);
		l_all_6_text_hide = (TextView) v4.findViewById(R.id.l_all_6_text_hide);
		l_all_7_text_hide = (TextView) v4.findViewById(R.id.l_all_7_text_hide);
	
		
		
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
			case R.id.btn_time_onoff:
				
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
				
//				Intent timeIntent = panduan_apps_to_list_all(
//						"com.csw.csw_musictimeset");
//				if (timeIntent == null) {
//					Toast.makeText(getActivity(), "未安装定时模块", 2000).show();
//				}else{
//					Intent mIntent=new Intent();
//					ComponentName comp = new ComponentName("com.csw.csw_musictimeset","com.csw.csw_musictimeset.MusicTimeHasBeenSavedActivity");
//					mIntent.setComponent(comp);
//					startActivity(mIntent);
//				}
				break;
			case R.id.btn_file_manager:
				
				Intent timeIntent = panduan_apps_to_list_all(
						"com.csw.csw_musictimeset");
				if (timeIntent == null) {
					Toast.makeText(getActivity(), "未安装定时模块", 2000).show();
				}else{
					Intent mIntent=new Intent();
					ComponentName comp = new ComponentName("com.csw.csw_musictimeset","com.csw.csw_musictimeset.MusicTimeHasBeenSavedActivity");
					mIntent.setComponent(comp);
					startActivity(mIntent);
				}
				
//				Intent launchIntent_but_bendi_555 = new Intent();
//				launchIntent_but_bendi_555.setComponent(new ComponentName(
//						"com.android.rk",
//						"com.android.rk.RockExplorer"));
//				ComponentName intent555 = launchIntent_but_bendi_555.getComponent();
//				if (intent555 == null)
//					return;
//				Intent but_bendi_555_s = panduan_apps_to_list_all(
//						"com.android.rk");
//				if (but_bendi_555_s == null) {
//					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
//					break;
//				}
//				startActivity(launchIntent_but_bendi_555);
				
				break;
			case R.id.btn_photo:
				Intent but_bendi_4 = new Intent();
				but_bendi_4.setComponent(new ComponentName("com.csw.wj",
						"com.csw.wj.MainActivity"));
				ComponentName intent4 = but_bendi_4.getComponent();
				if (intent4 == null)
					return; //
				but_bendi_4.putExtra("name", "图片");

				Intent but_bendi_4_s = panduan_apps_to_list_all(bendi_bao);
				if (but_bendi_4_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "wenjianguanli";
					name = "wenjianguanli";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(but_bendi_4);
				break;
			case R.id.btn_wangpan:
				/*Intent launchIntent_but_bendi_guanggao = new Intent();
				launchIntent_but_bendi_guanggao.setComponent(new ComponentName(
						"com.letv.lecloud.disk",
						"com.letv.lecloud.disk.activity.SplashActivity"));

				Intent launchIntent_but_bendi_guanggao_s = panduan_apps_to_list_all(
						"com.letv.lecloud.disk",
						"com.letv.lecloud.disk.activity.SplashActivity");
				if (launchIntent_but_bendi_guanggao_s == null) {
//					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
//					url = "huaweiwangpan";
//					name = "huaweiwangpan";
//					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}else{
					startActivity(launchIntent_but_bendi_guanggao);
				}*/
				//Utils.writeSys("/sys/class/hdmi_gpio/hdmi_gpio",1);  //切换lcd显示。
				Utils.writeSys("/sys/class/hdmi_gpio/hdmi_gpio",0); //切换hdmi显示。
			    break;
			case R.id.btn_all_app:
				Intent but_all_app = new Intent();
				but_all_app.setClass(getActivity(), ALLappActivity.class);
				but_all_app.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(but_all_app);
				startActivityForResult(but_all_app, 4);
				// startActivityForResult(but_all_app,2);
				break;
			case R.id.btn_add_app:
				Intent but_all_kuaijie = new Intent();
				but_all_kuaijie.setClass(getActivity(), AddKJappActivity.class);
				but_all_kuaijie.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// app_add_all_init();
				preferencesUtils.setsum(getActivity(), "add_app_sum",
						App_List.size());
				AllAppFragment.this.startActivityForResult(but_all_kuaijie, 2);
				break;
			case R.id.btn_android_market:
//				Intent but_all_tuijian = new Intent();
//				but_all_tuijian.setComponent(new ComponentName(
//						"com.guozi.appstore", "com.guozi.appstore.MainActivity"));
				/*Intent but_all_tuijian_s = panduan_apps_to_list_all(
						"com.dangbeimarket", "com.dangbeimarket.WelcomeActivity");
				if (but_all_tuijian_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "3128qihoo_mart";
					name = "3128qihoo_mart";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				
				Intent but_all_tuijian = getActivity().getPackageManager()
    					.getLaunchIntentForPackage("com.dangbeimarket");*/
//				but_all_tuijian.putExtra("Chuangshiwei", "QIPO201411070001");//这个是必需要填的，不然默认为选中推荐
//				but_all_tuijian.putExtra("interface", 3);//这个需要首选哪一项，就填哪一项的索引值:比如推荐-0,影音-1，游戏-2，应用-3，我的-4，搜索-5.
				Intent but_all_tuijian_s = panduan_apps_to_list_all(
						"com.guozi.appstore");
				if (but_all_tuijian_s == null) {
//					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
//					url = "3128qihoo_mart";
//					name = "3128qihoo_mart";
//					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					Toast.makeText(getActivity(), "未安装奇珀市场", 2000).show();
					break;
				}
				
				Intent but_all_tuijian = getActivity().getPackageManager()
    					.getLaunchIntentForPackage("com.guozi.appstore");
			
				startActivity(but_all_tuijian);
			
//				startActivity(but_all_tuijian);

				break;
			case R.id.linear_myapp1:

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
					if (l_all_1 != null)
						startActivity(l_all_1);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.linear_myapp2:
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
			case R.id.linear_myapp3:
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
			case R.id.linear_myapp4:
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
			case R.id.linear_myapp5:
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
			case R.id.linear_myapp6:
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
			case R.id.linear_myapp7:
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
	 * 根据apk名称进行比较
	 */
	public Intent lableName_compare_intent(Context context,String lable,String PackageName) {
		for (int i = 0; i < GetAppInfo.list.size(); i++) {
			if (GetAppInfo.list.get(i).getAppLabel().equals(lable)) {
				if(PackageName.equals(GetAppInfo.list.get(i).getPkgName()))
				return context.getPackageManager().getLaunchIntentForPackage(GetAppInfo.list.get(i).getPkgName());
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
			if (GetAppInfo.list.get(i).getPkgName().equals(baoName)) {
				// 为应用程序的启动Activity 准备Intent
				return GetAppInfo.list.get(i).getIntent();
			}
		}
		return null;
	}

	private BroadcastReceiver mAddApproadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(ADD_APPLICATION_BUTTON)) {
				//Toast.makeText(context, "555", Toast.LENGTH_SHORT).show();
				btn_add_app.callOnClick();
			}
		}
		
	};

	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("ALL App");
		app_add_all();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("全部应用");
	}
}
