package com.util.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.csw.csw_desktop_yahao.MainActivity;
import com.zhongqin.tianlai.R;
import com.csw.download.DownManager;
import com.wyf.allapp.GetAppInfo;
import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.SharedPreferencesUtils;
import com.wyf.util.Utils;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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
public class LocalFragment extends Fragment implements FragmentInterfaces{
	/********************** lgj 资源管理 *************************************************/
	private FrameLayout fileManager_layout;
	private FrameLayout huawei_layout;
	private FrameLayout installPackage_layout;
	private FrameLayout video_layout;
	private FrameLayout music_layout;
	private FrameLayout picture_layout;
	private ImageView ziyun_whiteBorder;
	/** */
	private SharedPreferencesUtils preferencesUtils;
	private List<AppInfo> App_List_zhibo = new ArrayList<AppInfo>();
	private final static int ZHIBO_APP = 1;
	private GetAppInfo getAppInfo;
	/** */
	private final static int SOUSU_YINGYONG = 2;
	private final static int DOWNLOAD_SUM = 3;
	HashMap<String, String> mHashMap = new HashMap<String, String>();
	private String url;
	private String name;
	DownManager manager;
    /** */
	private String bendi_bao = "com.csw.wj";
	private String bendi_lei = "com.csw.wj.MainActivity";
//	private Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getAppInfo = new GetAppInfo(getActivity());
		manager = new DownManager(getActivity());
	}
	public LocalFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LocalFragment(Context context) {
		super();
//		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v3 = inflater.inflate(R.layout.local_main, container, false);
		// 本地
		fileManager_layout = (FrameLayout) v3
				.findViewById(R.id.fileManager_ziyuan_layout);
		huawei_layout = (FrameLayout) v3
				.findViewById(R.id.huawei_ziyuan_layout);
		installPackage_layout = (FrameLayout) v3
				.findViewById(R.id.installPackage_ziyuan_layout);
		video_layout = (FrameLayout) v3.findViewById(R.id.video_ziyuan_layout);
		music_layout = (FrameLayout) v3.findViewById(R.id.music_ziyuan_layout);
		picture_layout = (FrameLayout) v3
				.findViewById(R.id.picture_ziyuan_layout);
		/** */
		ziyun_whiteBorder = (ImageView) v3.findViewById(R.id.all_ziyuan_layout);
		fileManager_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		huawei_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());
		installPackage_layout
				.setOnFocusChangeListener(new MyOnFocusChangeListener());
		video_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());
		music_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());
		picture_layout.setOnFocusChangeListener(new MyOnFocusChangeListener());

		fileManager_layout.setOnClickListener(new MyOnClickListener());
		huawei_layout.setOnClickListener(new MyOnClickListener());
		installPackage_layout.setOnClickListener(new MyOnClickListener());
		video_layout.setOnClickListener(new MyOnClickListener());
		music_layout.setOnClickListener(new MyOnClickListener());
		picture_layout.setOnClickListener(new MyOnClickListener());
		return v3;
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
	 *            资源管理
	 */
	@SuppressLint("NewApi")
	private void flyWhiteBorder_ziyun_scroll(View paramView) {
		if ((this.ziyun_whiteBorder != null) && (paramView != null)) {
			this.ziyun_whiteBorder.setVisibility(0);
			ViewPropertyAnimator localViewPropertyAnimator = this.ziyun_whiteBorder
					.animate();
			localViewPropertyAnimator.setDuration(200L);
			localViewPropertyAnimator.x(paramView.getX() - 9);
			localViewPropertyAnimator.y(paramView.getY() - 13);
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
			// 资源管理
			case R.id.fileManager_ziyuan_layout:
				flyWhiteBorder_ziyun_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					ziyun_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					ziyun_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.huawei_ziyuan_layout:
				flyWhiteBorder_ziyun_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					ziyun_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					ziyun_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.installPackage_ziyuan_layout:
				flyWhiteBorder_ziyun_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					ziyun_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					ziyun_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.video_ziyuan_layout:
				flyWhiteBorder_ziyun_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					ziyun_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					ziyun_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.music_ziyuan_layout:
				flyWhiteBorder_ziyun_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					ziyun_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					ziyun_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
				break;
			case R.id.picture_ziyuan_layout:
				flyWhiteBorder_ziyun_scroll(v);
				if (arg1) {
					Utils.scaleButton_dada(v);
					ziyun_whiteBorder.setVisibility(View.VISIBLE);
				} else {
					v.clearAnimation();
					ziyun_whiteBorder.setVisibility(View.GONE);
				}
				Constant.UPWhether = true;
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
			// 本地按钮集
			// 视频
			case R.id.video_ziyuan_layout:
				Intent but_bendi_1 = new Intent();
				but_bendi_1.setComponent(new ComponentName("com.csw.wj",
						"com.csw.wj.MainActivity"));
				ComponentName intent1 = but_bendi_1.getComponent();
				if (intent1 == null)
					return; //
				but_bendi_1.putExtra("name", "视频");

				Intent but_bendi_1_s = panduan_apps_to_list_all(bendi_bao,
						bendi_lei);
				if (but_bendi_1_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "wenjianguanli";
					name = "wenjianguanli";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}

				startActivity(but_bendi_1);
				break;
			// 音乐
			case R.id.music_ziyuan_layout:
				Intent but_bendi_2 = new Intent();
				but_bendi_2.setComponent(new ComponentName("com.csw.wj",
						"com.csw.wj.MainActivity"));
				ComponentName intent2 = but_bendi_2.getComponent();
				if (intent2 == null)
					return; //

				Intent but_bendi_2_s = panduan_apps_to_list_all(bendi_bao,
						bendi_lei);
				if (but_bendi_2_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "wenjianguanli";
					name = "wenjianguanli";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}

				but_bendi_2.putExtra("name", "音乐");
				startActivity(but_bendi_2);
				break;
			// 回看
			case R.id.installPackage_ziyuan_layout:
				Intent launchIntent_but_bendi_3 = new Intent();
//				launchIntent_but_bendi_3.setClass(getActivity(),
//						com.example.apkfilejiazai.MainActivity.class);
//				launchIntent_but_bendi_3
//						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				launchIntent_but_bendi_3.setComponent(new ComponentName("com.android.apkinstaller",
						"com.android.apkinstaller.apkinstaller"));
				ComponentName intent3 = launchIntent_but_bendi_3.getComponent();
				if (intent3 == null)
					return; //
				startActivity(launchIntent_but_bendi_3);
				break;
			case R.id.picture_ziyuan_layout:
				Intent but_bendi_4 = new Intent();
				but_bendi_4.setComponent(new ComponentName("com.csw.wj",
						"com.csw.wj.MainActivity"));
				ComponentName intent4 = but_bendi_4.getComponent();
				if (intent4 == null)
					return; //
				but_bendi_4.putExtra("name", "图片");

				Intent but_bendi_4_s = panduan_apps_to_list_all(bendi_bao,
						bendi_lei);
				if (but_bendi_4_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					url = "wenjianguanli";
					name = "wenjianguanli";
					app_handler.sendEmptyMessage(DOWNLOAD_SUM);
					break;
				}
				startActivity(but_bendi_4);
				break;
			case R.id.fileManager_ziyuan_layout:
				/*Intent launchIntent_but_bendi_5 = new Intent();
				launchIntent_but_bendi_5.setComponent(new ComponentName(
						"com.softwinner.TvdFileManager",
						"com.softwinner.TvdFileManager.MainUI"));
				ComponentName intent5 = launchIntent_but_bendi_5.getComponent();
				if (intent5 == null)
					return;
				Intent but_bendi_5_s = panduan_apps_to_list_all(
						"com.softwinner.TvdFileManager",
						"com.softwinner.TvdFileManager.MainUI");
				if (but_bendi_5_s == null) {
					app_handler.sendEmptyMessage(SOUSU_YINGYONG);
					break;
				}
				startActivity(launchIntent_but_bendi_5);*/
				
				//3128平板
//				 "com.android.rk","com.android.rk.RockExplorer"
				
				 /*
					 * added by lgj 4.14
					 */
				/*	DisplayMetrics dm = new DisplayMetrics();  
			        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);  
			        int widthPixels=dm.widthPixels;
			        int heightPixels=dm.heightPixels;
			       
			        int addItemSize;
			        if(widthPixels>1060||heightPixels>600){//电视
			        	
			        	Intent launchIntent_but_bendi_5 = new Intent();
						launchIntent_but_bendi_5.setComponent(new ComponentName(
								"com.android.rockchip",
								"com.android.rockchip.RockExplorer"));
						ComponentName intent5 = launchIntent_but_bendi_5.getComponent();
						if (intent5 == null)
							return;
						Intent but_bendi_5_s = panduan_apps_to_list_all(
								"com.android.rockchip",
								"com.android.rockchip.RockExplorer");
						if (but_bendi_5_s == null) {
							app_handler.sendEmptyMessage(SOUSU_YINGYONG);
							break;
						}
						startActivity(launchIntent_but_bendi_5);
			        }else{//平板电脑
*/			        	
			        	Intent launchIntent_but_bendi_555 = new Intent();
						launchIntent_but_bendi_555.setComponent(new ComponentName(
								"com.android.rk",
								"com.android.rk.RockExplorer"));
						ComponentName intent555 = launchIntent_but_bendi_555.getComponent();
						if (intent555 == null)
							return;
						Intent but_bendi_555_s = panduan_apps_to_list_all(
								"com.android.rk",
								"com.android.rk.RockExplorer");
						if (but_bendi_555_s == null) {
							app_handler.sendEmptyMessage(SOUSU_YINGYONG);
							break;
						}
						startActivity(launchIntent_but_bendi_555);
//			        }
			        
				
				break;
			case R.id.huawei_ziyuan_layout:
				Intent launchIntent_but_bendi_guanggao = new Intent();
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
				}
				
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
		for(int i = 0;i < GetAppInfo.list.size(); i++){

			// icon = reInfo.loadIcon(pm); // 获得应用程序图标
			if (GetAppInfo.list.get(i).getPkgName().equals(baoName) && GetAppInfo.list.get(i).getActivityName().equals(zhuleiName)) {
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
