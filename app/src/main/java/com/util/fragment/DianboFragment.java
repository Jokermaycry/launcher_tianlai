package com.util.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhongqin.tianlai.R;
import com.csw.download.DownManager;
import com.wyf.allapp.GetAppInfo;
import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
public class DianboFragment extends Fragment implements FragmentInterfaces{
	/** 网络点播 */
	private FrameLayout more_dianbo_layout;
	private FrameLayout dianying_dianbo_layout;
	private FrameLayout dianshiju_dianbo_layout;
	private FrameLayout zongyi_dianbo_layout;
	private FrameLayout dongman_dianbo_layout;
	private FrameLayout soushuo_dianbo_layout;
	private FrameLayout history_dianbo_layout;
	private ImageView dianbo_whiteBorder;
	private ImageView dianboxiao_whiteBorder;
	/** */
	/** */
	private final static int SOUSU_YINGYONG = 2;
	private final static int DOWNLOAD_SUM = 3;
	HashMap<String, String> mHashMap = new HashMap<String, String>();
	private String url;
	private String name;
	DownManager manager ;
	private GetAppInfo getAppInfo;
	// 泰洁视频2.7.6.1
	private String dianbo_bao = "com.togic.livevideo";
	private String dianbo_lei = "com.togic.launcher.SplashActivity";
//	private Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getAppInfo = new GetAppInfo(getActivity());
		manager = new DownManager(getActivity());
	}

	public DianboFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DianboFragment(Context context) {
		super();
	//	this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v0 = inflater.inflate(R.layout.dianbo_main, container, false);
		/** 网络点播 */
		more_dianbo_layout = (FrameLayout) v0.findViewById(R.id.more_dianbo_layout);
		dianying_dianbo_layout = (FrameLayout) v0.findViewById(R.id.dianying_dianbo_layout);
		dianshiju_dianbo_layout = (FrameLayout) v0.findViewById(R.id.dianshiju_dianbo_layout);
		zongyi_dianbo_layout = (FrameLayout) v0.findViewById(R.id.zongyi_dianbo_layout);
		dongman_dianbo_layout = (FrameLayout) v0.findViewById(R.id.dongman_dianbo_layout);
		soushuo_dianbo_layout = (FrameLayout) v0.findViewById(R.id.shousuo_dianbo_layout);
		history_dianbo_layout = (FrameLayout) v0.findViewById(R.id.history_dianbo_layout);

		more_dianbo_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());
		dianying_dianbo_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());
		dianshiju_dianbo_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());
		zongyi_dianbo_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());
		dongman_dianbo_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());
		soushuo_dianbo_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());
		history_dianbo_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());

		more_dianbo_layout.setOnClickListener(new MyOnClickListener());
		dianying_dianbo_layout.setOnClickListener(new MyOnClickListener());
		dianshiju_dianbo_layout.setOnClickListener(new MyOnClickListener());
		zongyi_dianbo_layout.setOnClickListener(new MyOnClickListener());
		dongman_dianbo_layout.setOnClickListener(new MyOnClickListener());
		soushuo_dianbo_layout.setOnClickListener(new MyOnClickListener());
		history_dianbo_layout.setOnClickListener(new MyOnClickListener());
		/** */
		dianbo_whiteBorder = (ImageView) v0.findViewById(R.id.scoll_dianbo_layout);
		dianboxiao_whiteBorder = (ImageView) v0.findViewById(R.id.scoll_dianboxiao_layout);
		return v0;
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
	 * @param 平移动画
	 *            点播
	 */
	@SuppressLint("NewApi")
	private void flyWhiteBorder_dianbo_scroll(View paramView) {
		if ((this.dianbo_whiteBorder != null) && (paramView != null)) {
			this.dianbo_whiteBorder.setVisibility(0);
			ViewPropertyAnimator localViewPropertyAnimator = this.dianbo_whiteBorder
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
	 *            点播搜索和播放历史
	 */
	@SuppressLint("NewApi")
	private void flyWhiteBorder_dianboxiao_scroll(View paramView) {
		if ((this.dianboxiao_whiteBorder != null) && (paramView != null)) {
			this.dianboxiao_whiteBorder.setVisibility(0);
			ViewPropertyAnimator localViewPropertyAnimator = this.dianboxiao_whiteBorder
					.animate();
			localViewPropertyAnimator.setDuration(200L);
			localViewPropertyAnimator.x(paramView.getX() - 9);
			localViewPropertyAnimator.y(paramView.getY() - 10);
			System.out.println(paramView.getX() + " sssssssss "
					+ paramView.getY());
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
				break;
			//	AllAPK_List = getAppInfo.getAllAPK();
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
			// 点播
			case R.id.more_dianbo_layout:
				flyWhiteBorder_dianbo_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					dianbo_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					dianbo_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.dianying_dianbo_layout:
				flyWhiteBorder_dianbo_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					dianbo_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					dianbo_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			// case R.id.but_dianbo_3:
			case R.id.dianshiju_dianbo_layout:
				flyWhiteBorder_dianbo_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					dianbo_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					dianbo_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.zongyi_dianbo_layout:
				flyWhiteBorder_dianbo_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					dianbo_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					dianbo_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.dongman_dianbo_layout:
				flyWhiteBorder_dianbo_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					dianbo_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					dianbo_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.shousuo_dianbo_layout:
				flyWhiteBorder_dianboxiao_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					dianboxiao_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					dianboxiao_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.history_dianbo_layout:
				flyWhiteBorder_dianboxiao_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					dianboxiao_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					dianboxiao_whiteBorder.setVisibility(View.GONE);
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
			// 点播
			case R.id.more_dianbo_layout:

				Intent but_dianbo_1 = new Intent();
				but_dianbo_1.setClass(getActivity(),
						com.csw.apps.zhibo.ZhiboActivity.class);
				but_dianbo_1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ArrayList<String> playlist = new ArrayList<String>();
				playlist.add("com.qiyi.video net.myvst.v2  com.tencent.qqlivehd com.youku.tv "
						+ "com.moretv.android com.lanxu.vod tv.pps.tpad com.pplive.androidtv com.togic.livevideo com.zbmv");
				playlist.add("csw.dianbo");
				but_dianbo_1.putExtra("selected", 0);
				but_dianbo_1.putExtra("playlist", playlist);
				startActivity(but_dianbo_1);
				break;
			case R.id.dianshiju_dianbo_layout:

				/*Intent intent_but_dianbo_2 = new Intent(
						"togic.intent.action.LIVE_VIDEO.PROGRAM_LIST");
				intent_but_dianbo_2.putExtra("android.intent.extra.UID", 1);
				intent_but_dianbo_2.putExtra("intent.extra.LALEB", "电视剧");

				Intent intent_but_dianbo_2_s = panduan_apps_to_list_all(
						dianbo_bao, dianbo_lei);
				if (intent_but_dianbo_2_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "taijieshiping";
					name = "taijieshiping";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(intent_but_dianbo_2);*/
				
//				"com.molitv.android", "com.molitv.android.activity.LauncherActivity"
//				Intent musicIntent = panduan_apps_to_list_all(
//						"net.myvst.v2", "com.vst.itv52.v1.LancherActivity");
				Intent musicIntent = panduan_apps_to_list_all(
						"com.molitv.android", "com.molitv.android.activity.LauncherActivity");
				if (musicIntent == null) {
					Toast.makeText(getActivity(), "未安装魔力视频", 2000).show();
					break;
				}
				
				Intent but_all_musicIntent = getActivity().getPackageManager()
    					.getLaunchIntentForPackage("com.molitv.android");		
				startActivity(but_all_musicIntent);
				
				
				
				break;
			case R.id.dongman_dianbo_layout:
				/*Intent intent_but_dianbo_3 = new Intent(
						"togic.intent.action.LIVE_VIDEO.PROGRAM_LIST");
				intent_but_dianbo_3.putExtra("android.intent.extra.UID", 4);
				intent_but_dianbo_3.putExtra("intent.extra.LALEB", "动漫");

				Intent intent_but_dianbo_3_s = panduan_apps_to_list_all(
						dianbo_bao, dianbo_lei);
				if (intent_but_dianbo_3_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "taijieshiping";
					name = "taijieshiping";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(intent_but_dianbo_3);*/
				
				Intent musicIntent2 = panduan_apps_to_list_all(
						"com.molitv.android", "com.molitv.android.activity.LauncherActivity");
				if (musicIntent2 == null) {
					Toast.makeText(getActivity(), "未安装魔力视频", 2000).show();
					break;
				}
				
				Intent but_all_musicIntent2= getActivity().getPackageManager()
    					.getLaunchIntentForPackage("com.molitv.android");		
				startActivity(but_all_musicIntent2);
				break;
			case R.id.zongyi_dianbo_layout:
				/*Intent intent_but_dianbo_4 = new Intent(
						"togic.intent.action.LIVE_VIDEO.PROGRAM_LIST");
				intent_but_dianbo_4.putExtra("android.intent.extra.UID", 3);
				intent_but_dianbo_4.putExtra("intent.extra.LALEB", "综艺");

				Intent intent_but_dianbo_4_s = panduan_apps_to_list_all(
						dianbo_bao, dianbo_lei);
				if (intent_but_dianbo_4_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "taijieshiping";
					name = "taijieshiping";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(intent_but_dianbo_4);*/
				
				Intent musicIntent3 = panduan_apps_to_list_all(
						"com.molitv.android", "com.molitv.android.activity.LauncherActivity");
				if (musicIntent3 == null) {
					Toast.makeText(getActivity(), "未安装魔力视频", 2000).show();
					break;
				}
				
				Intent but_all_musicIntent3 = getActivity().getPackageManager()
    					.getLaunchIntentForPackage("com.molitv.android");		
				startActivity(but_all_musicIntent3);
				
				break;
			case R.id.shousuo_dianbo_layout:

				/*Intent intent_but_dianbo_5 = new Intent(
						"togic.intent.action.MY_BOX.SEARCH");
				intent_but_dianbo_5.addCategory(Intent.CATEGORY_DEFAULT);

				Intent intent_but_dianbo_5_s = panduan_apps_to_list_all(
						dianbo_bao, dianbo_lei);
				if (intent_but_dianbo_5_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "taijieshiping";
					name = "taijieshiping";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(intent_but_dianbo_5);*/
				
				Intent musicIntent4 = panduan_apps_to_list_all(
						"com.molitv.android", "com.molitv.android.activity.LauncherActivity");
				if (musicIntent4 == null) {
					Toast.makeText(getActivity(), "未安装魔力视频", 2000).show();
					break;
				}
				
				Intent but_all_musicIntent4 = getActivity().getPackageManager()
    					.getLaunchIntentForPackage("com.molitv.android");		
				startActivity(but_all_musicIntent4);
				
				break;
			case R.id.history_dianbo_layout:
				/*Intent intent_but_dianbo_6 = new Intent(
						"togic.intent.action.LIVE_VIDEO_PROGRAM_MY_FAVOR");
				intent_but_dianbo_6.addCategory(Intent.CATEGORY_DEFAULT);

				Intent intent_but_dianbo_6_s = panduan_apps_to_list_all(
						dianbo_bao, dianbo_lei);
				if (intent_but_dianbo_6_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "taijieshiping";
					name = "taijieshiping";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(intent_but_dianbo_6);*/
				
				Intent musicIntent5 = panduan_apps_to_list_all(
						"com.molitv.android", "com.molitv.android.activity.LauncherActivity");
				if (musicIntent5 == null) {
					Toast.makeText(getActivity(), "未安装魔力视频", 2000).show();
					break;
				}
				
				Intent but_all_musicIntent5 = getActivity().getPackageManager()
    					.getLaunchIntentForPackage("com.molitv.android");		
				startActivity(but_all_musicIntent5);
				
				break;
			case R.id.dianying_dianbo_layout:
				/*Intent intent_but_dianbo_1 = new Intent(
						"togic.intent.action.LIVE_VIDEO.PROGRAM_LIST");
				intent_but_dianbo_1.putExtra("android.intent.extra.UID", 2);
				intent_but_dianbo_1.putExtra("intent.extra.LALEB", "电影");

				Intent intent_but_dianbo_1_s = panduan_apps_to_list_all(
						dianbo_bao, dianbo_lei);
				if (intent_but_dianbo_1_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "taijieshiping";
					name = "taijieshiping";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(intent_but_dianbo_1);*/
				
				Intent musicIntent6 = panduan_apps_to_list_all(
						"com.molitv.android", "com.molitv.android.activity.LauncherActivity");
				if (musicIntent6 == null) {
					Toast.makeText(getActivity(), "未安装魔力视频", 2000).show();
					break;
				}
				Intent but_all_musicIntent6 = getActivity().getPackageManager()
    					.getLaunchIntentForPackage("com.molitv.android");		
				startActivity(but_all_musicIntent6);
				
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

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
