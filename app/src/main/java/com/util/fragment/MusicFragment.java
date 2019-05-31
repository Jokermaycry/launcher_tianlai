package com.util.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhongqin.tianlai.R;
import com.csw.download.DownManager;
import com.csw.newfragment.HomeFragment;
import com.example.system.MobileHelperActivity;
import com.util.fragment.AllApplicationFragment.MyOnClickListener;
import com.util.fragment.AllApplicationFragment.MyOnFocusChangeListener;
import com.wyf.allapp.ALLappActivity;
import com.wyf.allapp.AddKJappActivity;
import com.wyf.allapp.GetAppInfo;
import com.wyf.allapp.MusicAppActivity;
import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.SharedPreferencesUtils;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MusicFragment extends Fragment implements FragmentInterfaces {

	
	// 全部应用
		private FrameLayout music_but_all_app;//音乐
		private FrameLayout music_but_all_kuaijie;
		private FrameLayout music_but_all_tuijian;
		private FrameLayout music_but_all_diantai;//电台
		
//		private LinearLayout music_l_all_1;
		private LinearLayout music_l_all_2;//快捷1
		private LinearLayout music_l_all_3;//快捷2
		private LinearLayout music_l_all_4;//快捷3
		
		
//		private LinearLayout music_l_all_5;
//		private LinearLayout music_l_all_6;
//		private LinearLayout music_l_all_7;
//		private LinearLayout music_l_all_8;
		private FrameLayout music_but_all_dingshi;//定时设置
		private FrameLayout music_but_all_dream;//屏保设置
		private FrameLayout music_but_all_yinyuan;//音源切换
		private FrameLayout music_but_all_mobilehelp;//手机助手
		
		
		
		
//		private ImageView music_l_all_1_image;
		private ImageView music_l_all_2_image;
		private ImageView music_l_all_3_image;
		private ImageView music_l_all_4_image;
//		private ImageView music_l_all_5_image;
//		private ImageView music_l_all_6_image;
//		private ImageView music_l_all_7_image;
//		private ImageView music_l_all_8_image;
//		private TextView music_l_all_1_text;
		private TextView music_l_all_2_text;
		private TextView music_l_all_3_text;
		private TextView music_l_all_4_text;
//		private TextView music_l_all_5_text;
//		private TextView music_l_all_6_text;
//		private TextView music_l_all_7_text;
//		private TextView music_l_all_8_text;
		
		
		/** */
		private SharedPreferencesUtils preferencesUtils;
		private List<AppInfo> App_List_music = new ArrayList<AppInfo>();
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

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			getAppInfo = new GetAppInfo(getActivity());
			manager = new DownManager(getActivity());
			app_add_all();
			System.out.println("执行了..");
		}
		public MusicFragment() {
			super();
			// TODO Auto-generated constructor stub
		}
		public MusicFragment(Context context) {
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
			if (App_List_music != null) {
				App_List_music.clear();
				
			}
			int sum = preferencesUtils.getsum(getActivity(), "music_app_sum");
			String value = null;
			AppInfo appInfo = null;
			for (int i = 1; i <= 8; i++) {
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
				App_List_music.add(appInfo);

			}
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
					int sum = App_List_music.size();
					/*if (App_List.size() >= 1) {
						music_l_all_1_image.setImageDrawable(App_List.get(0)
								.getAppIcon());
						music_l_all_1_text.setText(App_List.get(0).getAppLabel());
					} else {
						music_l_all_1_image.setImageDrawable(backDrawable);
						music_l_all_1_text.setText("");
					}*/
					if (App_List_music.size() >= 1) {
						music_l_all_2_image.setImageDrawable(App_List_music.get(0)
								.getAppIcon());
						music_l_all_2_text.setText(App_List_music.get(0).getAppLabel());
					} else {
						music_l_all_2_image.setImageDrawable(backDrawable);
						music_l_all_2_text.setText("");
					}
					if (App_List_music.size() >= 2) {
						music_l_all_3_image.setImageDrawable(App_List_music.get(1)
								.getAppIcon());
						music_l_all_3_text.setText(App_List_music.get(1).getAppLabel());
					} else {
						music_l_all_3_image.setImageDrawable(backDrawable);
						music_l_all_3_text.setText("");
					}
					if (App_List_music.size() >= 3) {
						music_l_all_4_image.setImageDrawable(App_List_music.get(2)
								.getAppIcon());
						music_l_all_4_text.setText(App_List_music.get(2).getAppLabel());
					} else {
						music_l_all_4_image.setImageDrawable(backDrawable);
						music_l_all_4_text.setText("");
					}
					/*if (App_List.size() >= 5) {
						music_l_all_5_image.setImageDrawable(App_List.get(4)
								.getAppIcon());
						music_l_all_5_text.setText(App_List.get(4).getAppLabel());
					} else {
						music_l_all_5_image.setImageDrawable(backDrawable);
						music_l_all_5_text.setText("");
					}
					if (App_List.size() >= 6) {
						music_l_all_6_image.setImageDrawable(App_List.get(5)
								.getAppIcon());
						music_l_all_6_text.setText(App_List.get(5).getAppLabel());
					} else {
						music_l_all_6_image.setImageDrawable(backDrawable);
						music_l_all_6_text.setText("");
					}
					if (App_List.size() >= 7) {
						music_l_all_7_image.setImageDrawable(App_List.get(6)
								.getAppIcon());
						music_l_all_7_text.setText(App_List.get(6).getAppLabel());
					} else {
						music_l_all_7_image.setImageDrawable(backDrawable);
						music_l_all_7_text.setText("");
					}
					if (App_List.size() >= 8) {
						music_l_all_8_image.setImageDrawable(App_List.get(7)
								.getAppIcon());
						music_l_all_8_text.setText(App_List.get(7).getAppLabel());
					} else {
						music_l_all_8_image.setImageDrawable(backDrawable);
						music_l_all_8_text.setText("");
					}*/
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
			View v4 = inflater.inflate(R.layout.music_fragment, container, false);
			// 全部应用
			music_but_all_app = (FrameLayout) v4.findViewById(R.id.music_but_all_app);
			music_but_all_kuaijie = (FrameLayout) v4.findViewById(R.id.music_but_all_kuaijie);
			music_but_all_tuijian = (FrameLayout) v4.findViewById(R.id.music_but_all_tuijian);
			
			music_but_all_diantai=(FrameLayout)v4.findViewById(R.id.music_but_all_diantai);//电台
			music_but_all_dingshi=(FrameLayout)v4.findViewById(R.id.music_but_all_dingshi);//定时设置
			music_but_all_dream=(FrameLayout)v4.findViewById(R.id.music_but_all_dream);//屏保设置
			music_but_all_yinyuan=(FrameLayout)v4.findViewById(R.id.music_but_all_yinyuan);//音源切换
		    music_but_all_mobilehelp=(FrameLayout)v4.findViewById(R.id.music_but_all_mobilehelp);//手机助手
			
			
//			music_l_all_1 = (LinearLayout) v4.findViewById(R.id.music_l_all_1);
			music_l_all_2 = (LinearLayout) v4.findViewById(R.id.music_l_all_2);
			music_l_all_3 = (LinearLayout) v4.findViewById(R.id.music_l_all_3);
			music_l_all_4 = (LinearLayout) v4.findViewById(R.id.music_l_all_4);
//			music_l_all_5 = (LinearLayout) v4.findViewById(R.id.music_l_all_5);
//			music_l_all_6 = (LinearLayout) v4.findViewById(R.id.music_l_all_6);
//			music_l_all_7 = (LinearLayout) v4.findViewById(R.id.music_l_all_7);
//			music_l_all_8 = (LinearLayout) v4.findViewById(R.id.music_l_all_8);
//			music_l_all_1_image = (ImageView) v4.findViewById(R.id.music_l_all_1_image);
			music_l_all_2_image = (ImageView) v4.findViewById(R.id.music_l_all_2_image);
			music_l_all_3_image = (ImageView) v4.findViewById(R.id.music_l_all_3_image);
			music_l_all_4_image = (ImageView) v4.findViewById(R.id.music_l_all_4_image);
//			music_l_all_5_image = (ImageView) v4.findViewById(R.id.music_l_all_5_image);
//			music_l_all_6_image = (ImageView) v4.findViewById(R.id.music_l_all_6_image);
//			music_l_all_7_image = (ImageView) v4.findViewById(R.id.music_l_all_7_image);
//			music_l_all_8_image = (ImageView) v4.findViewById(R.id.music_l_all_8_image);
//			music_l_all_1_text = (TextView) v4.findViewById(R.id.music_l_all_1_text);
			music_l_all_2_text = (TextView) v4.findViewById(R.id.music_l_all_2_text);
			music_l_all_3_text = (TextView) v4.findViewById(R.id.music_l_all_3_text);
			music_l_all_4_text = (TextView) v4.findViewById(R.id.music_l_all_4_text);
//			music_l_all_5_text = (TextView) v4.findViewById(R.id.music_l_all_5_text);
//			music_l_all_6_text = (TextView) v4.findViewById(R.id.music_l_all_6_text);
//			music_l_all_7_text = (TextView) v4.findViewById(R.id.music_l_all_7_text);
//			music_l_all_8_text = (TextView) v4.findViewById(R.id.music_l_all_8_text);
			/** */
			app_whiteBorder = (ImageView) v4.findViewById(R.id.music_scoll_all_layout);
			appxiao_whiteBorder = (ImageView) v4
					.findViewById(R.id.music_scoll_allxiao_layout);
			music_but_all_app.setOnClickListener(new MyOnClickListener());
			music_but_all_kuaijie.setOnClickListener(new MyOnClickListener());
			music_but_all_tuijian.setOnClickListener(new MyOnClickListener());
			
			music_but_all_diantai.setOnClickListener(new MyOnClickListener());
			music_but_all_dingshi.setOnClickListener(new MyOnClickListener());
			music_but_all_dream.setOnClickListener(new MyOnClickListener());
			music_but_all_yinyuan.setOnClickListener(new MyOnClickListener());
			music_but_all_mobilehelp.setOnClickListener(new MyOnClickListener());
			
			
			
			
//			music_l_all_1.setOnClickListener(new MyOnClickListener());
			music_l_all_2.setOnClickListener(new MyOnClickListener());
			music_l_all_3.setOnClickListener(new MyOnClickListener());
			music_l_all_4.setOnClickListener(new MyOnClickListener());
//			music_l_all_5.setOnClickListener(new MyOnClickListener());
//			music_l_all_6.setOnClickListener(new MyOnClickListener());
//			music_l_all_7.setOnClickListener(new MyOnClickListener());
//			music_l_all_8.setOnClickListener(new MyOnClickListener());

			music_but_all_app.setOnFocusChangeListener(new MyOnFocusChangeListener());
			music_but_all_kuaijie.setOnFocusChangeListener(new MyOnFocusChangeListener());
			music_but_all_tuijian.setOnFocusChangeListener(new MyOnFocusChangeListener());
//			music_l_all_1.setOnFocusChangeListener(new MyOnFocusChangeListener());
			music_l_all_2.setOnFocusChangeListener(new MyOnFocusChangeListener());
			music_l_all_3.setOnFocusChangeListener(new MyOnFocusChangeListener());
			music_l_all_4.setOnFocusChangeListener(new MyOnFocusChangeListener());
//			music_l_all_5.setOnFocusChangeListener(new MyOnFocusChangeListener());
//			music_l_all_6.setOnFocusChangeListener(new MyOnFocusChangeListener());
//			music_l_all_7.setOnFocusChangeListener(new MyOnFocusChangeListener());
//			music_l_all_8.setOnFocusChangeListener(new MyOnFocusChangeListener());

			music_but_all_diantai.setOnFocusChangeListener(new MyOnFocusChangeListener());
			music_but_all_dingshi.setOnFocusChangeListener(new MyOnFocusChangeListener());
			music_but_all_dream.setOnFocusChangeListener(new MyOnFocusChangeListener());
			music_but_all_yinyuan.setOnFocusChangeListener(new MyOnFocusChangeListener());
			music_but_all_mobilehelp.setOnFocusChangeListener(new MyOnFocusChangeListener());
			
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

		class MyOnFocusChangeListener implements OnFocusChangeListener {

			@Override
			public void onFocusChange(View v, boolean arg1) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				// 全部应用
				case R.id.music_but_all_app:
					flyWhiteBorder_app_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						app_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						app_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = true;
					break;
				case R.id.music_but_all_kuaijie:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = true;
					break;
				case R.id.music_but_all_tuijian:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = false;
					break;
				case R.id.music_but_all_diantai:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = true;
					break;
				case R.id.music_l_all_2:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = true;
					break;
				case R.id.music_l_all_3:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = true;
					break;
				case R.id.music_l_all_4:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = true;
					break;
				case R.id.music_but_all_dingshi:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = false;
					break;
				case R.id.music_but_all_dream:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = false;
					break;
				case R.id.music_but_all_yinyuan:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
					}
					Constant.UPWhether = false;
					break;
				case R.id.music_but_all_mobilehelp:
					flyWhiteBorder_appxiao_scroll(v);
					if (arg1) {
						Utils.scaleButton_dada(v);
						appxiao_whiteBorder.setVisibility(View.VISIBLE);
					} else {
						v.clearAnimation();
						appxiao_whiteBorder.setVisibility(View.GONE);
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
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				// 音乐
				case R.id.music_but_all_app:
					
					/*Intent but_all_app = new Intent();
					but_all_app.setClass(getActivity(), ALLappActivity.class);
					but_all_app.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// startActivity(but_all_app);
					startActivityForResult(but_all_app, 0);
					// startActivityForResult(but_all_app,2);
*/				
					Intent musicIntent = panduan_apps_to_list_all(
							"com.example.musicinterface", "com.example.musicinterface.MainActivity");
					if (musicIntent == null) {
						Toast.makeText(getActivity(), "音乐", 2000).show();
						break;
					}
					
					Intent but_all_musicIntent = getActivity().getPackageManager()
	    					.getLaunchIntentForPackage("com.example.musicinterface");		
					startActivity(but_all_musicIntent);
					
					break;
				case R.id.music_but_all_kuaijie:
					Intent but_all_kuaijie = new Intent();
					but_all_kuaijie.setClass(getActivity(), MusicAppActivity.class);
					but_all_kuaijie.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// app_add_all_init();
					preferencesUtils.setsum(getActivity(), "music_app_sum",
							App_List_music.size());
					
					startActivityForResult(but_all_kuaijie, 1);
					break;
				case R.id.music_but_all_tuijian:
//					Intent but_all_tuijian = new Intent();
//					but_all_tuijian.setComponent(new ComponentName(
//							"com.guozi.appstore", "com.guozi.appstore.MainActivity"));
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
//					but_all_tuijian.putExtra("Chuangshiwei", "QIPO201411070001");//这个是必需要填的，不然默认为选中推荐
//					but_all_tuijian.putExtra("interface", 3);//这个需要首选哪一项，就填哪一项的索引值:比如推荐-0,影音-1，游戏-2，应用-3，我的-4，搜索-5.
					
					
					Intent but_all_tuijian_s = panduan_apps_to_list_all(
							"com.guozi.appstore", "com.guozi.appstore.StartActivity");
					if (but_all_tuijian_s == null) {
//						app_handler.sendEmptyMessage(SOUSU_YINGYONG);
//						url = "3128qihoo_mart";
//						name = "3128qihoo_mart";
//						app_handler.sendEmptyMessage(DOWNLOAD_SUM);
						Toast.makeText(getActivity(), "未安装奇珀市场", 2000).show();
						break;
					}
					
					Intent but_all_tuijian = getActivity().getPackageManager()
	    					.getLaunchIntentForPackage("com.guozi.appstore");
				
					startActivity(but_all_tuijian);

					break;
				case R.id.music_but_all_diantai:
//					Toast.makeText(getActivity(), "网络电台", 2000).show();
					
					Intent musicIntentDian = panduan_apps_to_list_all(
							"com.gongjin.cradio", "com.gongjin.cradio.PlayerActivity");
					if (musicIntentDian == null) {
						Toast.makeText(getActivity(), "网络电台", 2000).show();
						break;
					}
					
					Intent but_all_musicIntent22 = getActivity().getPackageManager()
	    					.getLaunchIntentForPackage("com.gongjin.cradio");		
					startActivity(but_all_musicIntent22);
					
					
					/*if (music_l_all_1_text.getText().toString().equals("未添加")) {
						break;
					} else {
						
						 * Intent l_all_1 = getAppInfo
						 * .quanbuyingyong_saomiao_apps_to_open_intent(l_all_1_text
						 * .getText().toString());
						 
						Intent l_all_1 = lableName_compare_intent(music_l_all_1_text
								.getText().toString());
						if (l_all_1 != null)
							startActivity(l_all_1);
						else {
							// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
						}
					}*/
					break;
				case R.id.music_l_all_2:
					if (music_l_all_2_text.getText().toString().equals("未添加")) {
						break;
					} else {
						Intent l_all_2 = lableName_compare_intent(music_l_all_2_text
								.getText().toString());
						if (l_all_2 != null)
							startActivity(l_all_2);
						else {
							// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
						}
					}
					break;
				case R.id.music_l_all_3:
					if (music_l_all_3_text.getText().toString().equals("未添加")) {
						break;
					} else {
						Intent l_all_3 = lableName_compare_intent(music_l_all_3_text
								.getText().toString());
						if (l_all_3 != null)
							startActivity(l_all_3);
						else {
							// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
						}
					}
					break;
				case R.id.music_l_all_4:
					if (music_l_all_4_text.getText().toString().equals("未添加")) {
						break;
					} else {
						Intent l_all_4 = lableName_compare_intent(music_l_all_4_text
								.getText().toString());
						if (l_all_4 != null)
							startActivity(l_all_4);
						else {
							// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
						}
					}
					break;
				case R.id.music_but_all_dingshi:
					
					Intent timeIntent = panduan_apps_to_list_all(
							"com.csw.csw_musictimeset","com.csw.csw_musictimeset.MusicTimeHasBeenSavedActivity");
					if (timeIntent == null) {
						Toast.makeText(getActivity(), "未安装定时模块", 2000).show();
					}else{
						Intent mIntent=new Intent();
						ComponentName comp = new ComponentName("com.csw.csw_musictimeset","com.csw.csw_musictimeset.MusicTimeHasBeenSavedActivity");
						mIntent.setComponent(comp);
						startActivity(mIntent);
					}
					
					
					/*if (music_l_all_5_text.getText().toString().equals("未添加")) {
						break;
					} else {
						Intent l_all_5 = lableName_compare_intent(music_l_all_5_text
								.getText().toString());
						if (l_all_5 != null)
							startActivity(l_all_5);
						else {
							// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
						}
					}*/
					break;
				case R.id.music_but_all_dream:
					
					if(HomeFragment.setDreamPlay.equals("0")){
						   HomeFragment.setDreamPlay = "1";
						}
					Intent dreamIntent = panduan_apps_to_list_all(
							"com.android.setdream","com.android.setdream.MainActivity");
					if (dreamIntent == null) {
						Toast.makeText(getActivity(), "未安装屏保", 2000).show();
					}else{
						Intent mIntent=new Intent();
						ComponentName comp = new ComponentName("com.android.setdream","com.android.setdream.MainActivity");
						mIntent.setComponent(comp);
						startActivity(mIntent);
					}
					
					break;
				case R.id.music_but_all_yinyuan:
					Intent audioIntent = panduan_apps_to_list_all(
							"com.csw.setaudio","com.csw.setaudio.SoundDevicesManager");
							if (audioIntent == null) {
					Toast.makeText(getActivity(), "音源切换未安装", 2000).show();
							}else{
							Intent mIntent=new Intent();
							ComponentName comp = new ComponentName
							("com.csw.setaudio","com.csw.setaudio.SoundDevicesManager");
							mIntent.setComponent(comp);
							startActivity(mIntent);
							}
					
					break;
				case R.id.music_but_all_mobilehelp:
					Toast.makeText(getActivity(), "手机助手", 2000).show();
					/*if (music_l_all_8_text.getText().toString().equals("未添加")) {
						break;
					} else {
						Intent l_all_8 = lableName_compare_intent(music_l_all_8_text
								.getText().toString());
						if (l_all_8 != null) {
							startActivity(l_all_8);
						} else {
							// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
						}
					}*/
					Intent intentMobileHelper=new Intent();
					intentMobileHelper.setClass(getActivity(),MobileHelperActivity.class);
					startActivity(intentMobileHelper);
					
					
					break;
				default:
					break;
				}
			}

		}

		private ImageView music_zhibo_whiteBorder;

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
		public void show() {
			// TODO Auto-generated method stub
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
