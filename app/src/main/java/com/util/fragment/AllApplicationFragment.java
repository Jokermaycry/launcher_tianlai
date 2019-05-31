package com.util.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhongqin.tianlai.R;
import com.csw.download.DownManager;
import com.wyf.allapp.ALLappActivity;
import com.wyf.allapp.AddKJappActivity;
import com.wyf.allapp.GetAppInfo;

import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.SharedPreferencesUtils;
import com.wyf.util.Utils;

import android.annotation.SuppressLint;
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

@SuppressLint("ValidFragment")
public class AllApplicationFragment extends Fragment implements
		FragmentInterfaces {
	// 全部应用
	private FrameLayout but_all_app;
	private FrameLayout but_all_kuaijie;
	private FrameLayout but_all_tuijian;
	private LinearLayout l_all_1;
	private LinearLayout l_all_2;
	private LinearLayout l_all_3;
	private LinearLayout l_all_4;
	private LinearLayout l_all_5;
	private LinearLayout l_all_6;
	private LinearLayout l_all_7;
	private LinearLayout l_all_8;
	private ImageView l_all_1_image;
	private ImageView l_all_2_image;
	private ImageView l_all_3_image;
	private ImageView l_all_4_image;
	private ImageView l_all_5_image;
	private ImageView l_all_6_image;
	private ImageView l_all_7_image;
	private ImageView l_all_8_image;
	private TextView l_all_1_text;
	private TextView l_all_2_text;
	private TextView l_all_3_text;
	private TextView l_all_4_text;
	private TextView l_all_5_text;
	private TextView l_all_6_text;
	private TextView l_all_7_text;
	private TextView l_all_8_text;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getAppInfo = new GetAppInfo(getActivity());
		manager = new DownManager(getActivity());
		app_add_all();
	}
	public AllApplicationFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AllApplicationFragment(Context context) {
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
		for (int i = 1; i <= 8; i++) {
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
				} else {
					l_all_1_image.setImageDrawable(backDrawable);
					l_all_1_text.setText("");
				}
				if (App_List.size() >= 2) {
					l_all_2_image.setImageDrawable(App_List.get(1)
							.getAppIcon());
					l_all_2_text.setText(App_List.get(1).getAppLabel());
				} else {
					l_all_2_image.setImageDrawable(backDrawable);
					l_all_2_text.setText("");
				}
				if (App_List.size() >= 3) {
					l_all_3_image.setImageDrawable(App_List.get(2)
							.getAppIcon());
					l_all_3_text.setText(App_List.get(2).getAppLabel());
				} else {
					l_all_3_image.setImageDrawable(backDrawable);
					l_all_3_text.setText("");
				}
				if (App_List.size() >= 4) {
					l_all_4_image.setImageDrawable(App_List.get(3)
							.getAppIcon());
					l_all_4_text.setText(App_List.get(3).getAppLabel());
				} else {
					l_all_4_image.setImageDrawable(backDrawable);
					l_all_4_text.setText("");
				}
				if (App_List.size() >= 5) {
					l_all_5_image.setImageDrawable(App_List.get(4)
							.getAppIcon());
					l_all_5_text.setText(App_List.get(4).getAppLabel());
				} else {
					l_all_5_image.setImageDrawable(backDrawable);
					l_all_5_text.setText("");
				}
				if (App_List.size() >= 6) {
					l_all_6_image.setImageDrawable(App_List.get(5)
							.getAppIcon());
					l_all_6_text.setText(App_List.get(5).getAppLabel());
				} else {
					l_all_6_image.setImageDrawable(backDrawable);
					l_all_6_text.setText("");
				}
				if (App_List.size() >= 7) {
					l_all_7_image.setImageDrawable(App_List.get(6)
							.getAppIcon());
					l_all_7_text.setText(App_List.get(6).getAppLabel());
				} else {
					l_all_7_image.setImageDrawable(backDrawable);
					l_all_7_text.setText("");
				}
				if (App_List.size() >= 8) {
					l_all_8_image.setImageDrawable(App_List.get(7)
							.getAppIcon());
					l_all_8_text.setText(App_List.get(7).getAppLabel());
				} else {
					l_all_8_image.setImageDrawable(backDrawable);
					l_all_8_text.setText("");
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
		View v4 = inflater.inflate(R.layout.all_app_main, container, false);
		// 全部应用
		but_all_app = (FrameLayout) v4.findViewById(R.id.but_all_app);
		but_all_kuaijie = (FrameLayout) v4.findViewById(R.id.but_all_kuaijie);
		but_all_tuijian = (FrameLayout) v4.findViewById(R.id.but_all_tuijian);
		l_all_1 = (LinearLayout) v4.findViewById(R.id.l_all_1);
		l_all_2 = (LinearLayout) v4.findViewById(R.id.l_all_2);
		l_all_3 = (LinearLayout) v4.findViewById(R.id.l_all_3);
		l_all_4 = (LinearLayout) v4.findViewById(R.id.l_all_4);
		l_all_5 = (LinearLayout) v4.findViewById(R.id.l_all_5);
		l_all_6 = (LinearLayout) v4.findViewById(R.id.l_all_6);
		l_all_7 = (LinearLayout) v4.findViewById(R.id.l_all_7);
		l_all_8 = (LinearLayout) v4.findViewById(R.id.l_all_8);
		l_all_1_image = (ImageView) v4.findViewById(R.id.l_all_1_image);
		l_all_2_image = (ImageView) v4.findViewById(R.id.l_all_2_image);
		l_all_3_image = (ImageView) v4.findViewById(R.id.l_all_3_image);
		l_all_4_image = (ImageView) v4.findViewById(R.id.l_all_4_image);
		l_all_5_image = (ImageView) v4.findViewById(R.id.l_all_5_image);
		l_all_6_image = (ImageView) v4.findViewById(R.id.l_all_6_image);
		l_all_7_image = (ImageView) v4.findViewById(R.id.l_all_7_image);
		l_all_8_image = (ImageView) v4.findViewById(R.id.l_all_8_image);
		l_all_1_text = (TextView) v4.findViewById(R.id.l_all_1_text);
		l_all_2_text = (TextView) v4.findViewById(R.id.l_all_2_text);
		l_all_3_text = (TextView) v4.findViewById(R.id.l_all_3_text);
		l_all_4_text = (TextView) v4.findViewById(R.id.l_all_4_text);
		l_all_5_text = (TextView) v4.findViewById(R.id.l_all_5_text);
		l_all_6_text = (TextView) v4.findViewById(R.id.l_all_6_text);
		l_all_7_text = (TextView) v4.findViewById(R.id.l_all_7_text);
		l_all_8_text = (TextView) v4.findViewById(R.id.l_all_8_text);
		/** */
		app_whiteBorder = (ImageView) v4.findViewById(R.id.scoll_all_layout);
		appxiao_whiteBorder = (ImageView) v4
				.findViewById(R.id.scoll_allxiao_layout);
		but_all_app.setOnClickListener(new MyOnClickListener());
		but_all_kuaijie.setOnClickListener(new MyOnClickListener());
		but_all_tuijian.setOnClickListener(new MyOnClickListener());
		l_all_1.setOnClickListener(new MyOnClickListener());
		l_all_2.setOnClickListener(new MyOnClickListener());
		l_all_3.setOnClickListener(new MyOnClickListener());
		l_all_4.setOnClickListener(new MyOnClickListener());
		l_all_5.setOnClickListener(new MyOnClickListener());
		l_all_6.setOnClickListener(new MyOnClickListener());
		l_all_7.setOnClickListener(new MyOnClickListener());
		l_all_8.setOnClickListener(new MyOnClickListener());

		but_all_app.setOnFocusChangeListener(new MyOnFocusChangeListener());
		but_all_kuaijie.setOnFocusChangeListener(new MyOnFocusChangeListener());
		but_all_tuijian.setOnFocusChangeListener(new MyOnFocusChangeListener());
		l_all_1.setOnFocusChangeListener(new MyOnFocusChangeListener());
		l_all_2.setOnFocusChangeListener(new MyOnFocusChangeListener());
		l_all_3.setOnFocusChangeListener(new MyOnFocusChangeListener());
		l_all_4.setOnFocusChangeListener(new MyOnFocusChangeListener());
		l_all_5.setOnFocusChangeListener(new MyOnFocusChangeListener());
		l_all_6.setOnFocusChangeListener(new MyOnFocusChangeListener());
		l_all_7.setOnFocusChangeListener(new MyOnFocusChangeListener());
		l_all_8.setOnFocusChangeListener(new MyOnFocusChangeListener());

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
			case R.id.but_all_app:
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
			case R.id.but_all_kuaijie:
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
			case R.id.but_all_tuijian:
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
			case R.id.l_all_1:
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
			case R.id.l_all_2:
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
			case R.id.l_all_3:
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
			case R.id.l_all_4:
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
			case R.id.l_all_5:
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
			case R.id.l_all_6:
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
			case R.id.l_all_7:
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
			case R.id.l_all_8:
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
			// 全部应用
			case R.id.but_all_app:
				Intent but_all_app = new Intent();
				but_all_app.setClass(getActivity(), ALLappActivity.class);
				but_all_app.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(but_all_app);
				startActivityForResult(but_all_app, 4);
				// startActivityForResult(but_all_app,2);
				break;
			case R.id.but_all_kuaijie:
				Intent but_all_kuaijie = new Intent();
				but_all_kuaijie.setClass(getActivity(), AddKJappActivity.class);
				but_all_kuaijie.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// app_add_all_init();
				preferencesUtils.setsum(getActivity(), "add_app_sum",
						App_List.size());
				startActivityForResult(but_all_kuaijie, 2);
				break;
			case R.id.but_all_tuijian:
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
						"com.guozi.appstore", "com.guozi.appstore.StartActivity");
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
			
				startActivity(but_all_tuijian);

				break;
			case R.id.l_all_1:

				if (l_all_1_text.getText().toString().equals("未添加")) {
					break;
				} else {
					/*
					 * Intent l_all_1 = getAppInfo
					 * .quanbuyingyong_saomiao_apps_to_open_intent(l_all_1_text
					 * .getText().toString());
					 */
					Intent l_all_1 = lableName_compare_intent(l_all_1_text
							.getText().toString());
					if (l_all_1 != null)
						startActivity(l_all_1);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.l_all_2:
				if (l_all_2_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_2 = lableName_compare_intent(l_all_2_text
							.getText().toString());
					if (l_all_2 != null)
						startActivity(l_all_2);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.l_all_3:
				if (l_all_3_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_3 = lableName_compare_intent(l_all_3_text
							.getText().toString());
					if (l_all_3 != null)
						startActivity(l_all_3);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.l_all_4:
				if (l_all_4_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_4 = lableName_compare_intent(l_all_4_text
							.getText().toString());
					if (l_all_4 != null)
						startActivity(l_all_4);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.l_all_5:
				if (l_all_5_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_5 = lableName_compare_intent(l_all_5_text
							.getText().toString());
					if (l_all_5 != null)
						startActivity(l_all_5);
					else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.l_all_6:
				if (l_all_6_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_6 = lableName_compare_intent(l_all_6_text
							.getText().toString());
					if (l_all_6 != null) {
						startActivity(l_all_6);
					} else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.l_all_7:
				if (l_all_7_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_7 = lableName_compare_intent(l_all_7_text
							.getText().toString());
					if (l_all_7 != null) {
						startActivity(l_all_7);
					} else {
						// mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
					}
				}
				break;
			case R.id.l_all_8:
				if (l_all_8_text.getText().toString().equals("未添加")) {
					break;
				} else {
					Intent l_all_8 = lableName_compare_intent(l_all_8_text
							.getText().toString());
					if (l_all_8 != null) {
						startActivity(l_all_8);
					} else {
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
