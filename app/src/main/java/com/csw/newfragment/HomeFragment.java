package com.csw.newfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


import com.baidu.a.a.a.a.a;


import com.cl.service.AuxLineService;
import com.zhongqin.tianlai.R;
import com.csw.download.DownManager;
import com.csw.music.Music;
import com.csw.music.MusicAdapter;
import com.csw.music.MusicList;
import com.csw.music.MusicListActivity;
import com.example.system.MobileHelperActivity;
import com.util.fragment.FragmentInterfaces;
import com.wyf.allapp.GetAppInfo;
import com.wyf.allapp.MusicAppActivity;
import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.SharedPreferencesUtils;
import com.wyf.util.TimerPingbaoUtil;
import com.wyf.util.Utils;

import android.R.raw;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements FragmentInterfaces {

	
	   private ImageButton btn_movie;//电影
	   private ImageButton btn_music;//网络音乐
	   private ImageButton btn_fm;//网络电台
	   private ImageButton btn_in;//
	   private ImageButton btn_phone;//手机软件
	   private ImageButton btn_system_onff;//系统重启
	   
	   private RelativeLayout rl_music_root;//音乐播放器主界面
	   private RelativeLayout gongban_player;  //公版播放器
	   private ImageView img_singer_photo;
	   private ImageButton btn_music_play;
	   private ImageButton btn_music_up;
	   private ImageButton btn_music_down;
	   private ImageButton btn_music_shunxu;
	   private ImageButton btn_music_yinxiao;
	   private ImageButton btn_music_liebiao;
	   private TextView txt_music_title ;
	   private ImageView img_music_go;
		/**下载APP */
		private final static int SOUSU_YINGYONG = 2;
		private final static int DOWNLOAD_SUM = 3;
		HashMap<String, String> mHashMap = new HashMap<String, String>();
		private String url;
		private String name;
		/** */
		
		private DownManager manager;
		private GetAppInfo getAppInfo;
		private Context mContext;

		private Equalizer mEqualizer;
		
		/** MediaPlayer */
		public static MediaPlayer mMediaPlayer = null;
		
		
		/** 计算当前播放列表的位置 */
		public static int music_id = -1;
		private Music music;
		
		private static final int MUSICINFOUPDATA_NUM=99;
		private AnimationDrawable musicAnimationDrawable;
		//播放模式
		private static String playModel = "playModel_fullCirculationPlay";
		private Button NormalModel_Btn;// normal
		private Button ClassicalModel_Btn;
		private Button HipHopModel_Btn;
		private Button JazzModel_Btn;
		private Button PopModel_Btn;
		private Button RockModel_Btn;
		private Button HeavyMetalModel_Btn;
		
		//均衡器
		private PopupWindow whichModelPopupWindow;
		private View whichModelView = null;
		LayoutInflater mInfater;
	
		//音乐目录
		private PopupWindow whichPathPopupWindow;
		private View whichPathOwnView = null;
		private Button whichPathOwn_sd;
		private Button whichPathOwn_external_sd;
		private Button whichPathOwn_usb;
		private static String whichPathOwn = "internal_sd";// 在哪个目录下，U盘或者SD卡
		private TextView txt_path;
		private TextView txt_music_sum;
		
		private ListView music_listview;
		public static List<Music> mMusiclist = new ArrayList<Music>();
		private MusicAdapter musicAdapter;
		private int music_progress;
		private boolean music_stop;
		
		public static final String SERIAL_ACTION_NAME = "infomation_from_serialport";
		public static final String PRESS_SD_PLAY_BUTTON = "com.yahao.press.sd.play.button";
		public static final String PRESS_USB_PLAY_BUTTON = "com.yahao.press.usb.play.button";
		public static final String OPEN_AUX_BROADCAST = "com.yahao.open.aux.broadcast";
		
		public static String setDreamPlay = "0";
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			getAppInfo = new GetAppInfo(getActivity());
			manager = new DownManager(getActivity());
			System.out.println("执行了..");
		}
		public HomeFragment() {
			super();
			// TODO Auto-generated constructor stub
		}
		public HomeFragment(Context context) {
			super();
		//	this.context = context;
			// TODO Auto-generated constructor stub
		}

		
		@Override
		public void onDestroyView() {
			// TODO Auto-generated method stub
			super.onDestroyView();
			if (broadcastReceiver!=null) {
				getActivity().unregisterReceiver(broadcastReceiver);
			}
			
		}
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(SERIAL_ACTION_NAME);
			intentFilter.addAction(PRESS_SD_PLAY_BUTTON);
			intentFilter.addAction(PRESS_USB_PLAY_BUTTON);
			intentFilter.addAction(OPEN_AUX_BROADCAST);
			getActivity().registerReceiver(broadcastReceiver, intentFilter);
			
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


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			mContext = getActivity();
			View v4 = inflater.inflate(R.layout.homefragment, container, false);
			mInfater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			rl_music_root = (RelativeLayout) v4.findViewById(R.id.rl_music_root);
			
			//音乐播放
			gongban_player =(RelativeLayout) v4.findViewById(R.id.gongban_player);
			img_music_go=(ImageView) v4.findViewById(R.id.img_music_dongtai);
			txt_music_title=(TextView) v4.findViewById(R.id.txt_music_title);
			img_singer_photo=(ImageView) v4.findViewById(R.id.img_singer_photo);
			btn_music_down=(ImageButton) v4.findViewById(R.id.btn_music_down);
			btn_music_up=(ImageButton) v4.findViewById(R.id.btn_music_up);
			btn_music_shunxu=(ImageButton) v4.findViewById(R.id.btn_music_shunxu);
			btn_music_yinxiao=(ImageButton) v4.findViewById(R.id.btn_music_yinxiao);
			btn_music_play=(ImageButton) v4.findViewById(R.id.btn_music_play);
			btn_music_liebiao=(ImageButton) v4.findViewById(R.id.btn_music_liebiao);
			
			img_music_go.setBackgroundResource(R.anim.music_animation);
			Object backgroundObject = img_music_go.getBackground();
			musicAnimationDrawable = (AnimationDrawable) backgroundObject;
			
			whichModelView = mInfater.inflate(R.layout.whichmodel_item, null);
			whichModelPopupWindow = new PopupWindow(whichModelView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			whichModelPopupWindow.setFocusable(true);
			whichModelPopupWindow.setOutsideTouchable(true);
			whichModelPopupWindow.update();
			whichModelPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			NormalModel_Btn = (Button) whichModelView
					.findViewById(R.id.normalModelBtn);
			ClassicalModel_Btn = (Button) whichModelView
					.findViewById(R.id.ClassicalModel_Btn);
			HipHopModel_Btn = (Button) whichModelView
					.findViewById(R.id.HipHopModel_Btn);
			JazzModel_Btn = (Button) whichModelView
					.findViewById(R.id.JazzModel_Btn);
			PopModel_Btn = (Button) whichModelView.findViewById(R.id.PopModel_Btn);
			RockModel_Btn = (Button) whichModelView
					.findViewById(R.id.RockModel_Btn);
			HeavyMetalModel_Btn = (Button) whichModelView
					.findViewById(R.id.HeavyMetalModel_Btn);
			
			whichPathOwnView = mInfater.inflate(R.layout.whichpath_item, null);
			whichPathPopupWindow = new PopupWindow(whichPathOwnView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			whichPathPopupWindow.setFocusable(true);
			whichPathPopupWindow.setOutsideTouchable(true);
			whichPathPopupWindow.update();
			whichPathPopupWindow.setBackgroundDrawable(new BitmapDrawable());

			whichPathOwn_sd = (Button) whichPathOwnView
					.findViewById(R.id.whichpath_sd);
			whichPathOwn_external_sd = (Button) whichPathOwnView
					.findViewById(R.id.whichpath_external_sd);
			whichPathOwn_usb = (Button) whichPathOwnView
					.findViewById(R.id.whichpath_usb);
			whichPathOwn_sd.setOnClickListener(new MyOnClickListener());
			whichPathOwn_external_sd.setOnClickListener(new MyOnClickListener());
			whichPathOwn_usb.setOnClickListener(new MyOnClickListener());
			
			
			btn_fm=(ImageButton) v4.findViewById(R.id.btn_fm);
			btn_movie=(ImageButton) v4.findViewById(R.id.btn_movie);
			btn_music=(ImageButton) v4.findViewById(R.id.btn_music);
			btn_in=(ImageButton) v4.findViewById(R.id.btn_in);
			btn_system_onff=(ImageButton) v4.findViewById(R.id.btn_system_onff);
			btn_phone=(ImageButton) v4.findViewById(R.id.btn_phone);
			
			rl_music_root.setOnClickListener(new MyOnClickListener());
			
			//音乐播放
			img_singer_photo.setOnClickListener(new MyOnClickListener());
			btn_music_down.setOnClickListener(new MyOnClickListener());
			btn_music_up.setOnClickListener(new MyOnClickListener());
			btn_music_shunxu.setOnClickListener(new MyOnClickListener());
			btn_music_yinxiao.setOnClickListener(new MyOnClickListener());
			btn_music_play.setOnClickListener(new MyOnClickListener());
			btn_music_liebiao.setOnClickListener(new MyOnClickListener());
			
			btn_fm.setOnClickListener(new MyOnClickListener());
			btn_movie.setOnClickListener(new MyOnClickListener());
			btn_music.setOnClickListener(new MyOnClickListener());
			btn_in.setOnClickListener(new MyOnClickListener());
			btn_system_onff.setOnClickListener(new MyOnClickListener());
			btn_phone.setOnClickListener(new MyOnClickListener());
			
			//
			NormalModel_Btn.setOnClickListener(new MyOnClickListener());
			ClassicalModel_Btn.setOnClickListener(new MyOnClickListener());
			HipHopModel_Btn.setOnClickListener(new MyOnClickListener());
			JazzModel_Btn.setOnClickListener(new MyOnClickListener());
			PopModel_Btn.setOnClickListener(new MyOnClickListener());
			RockModel_Btn.setOnClickListener(new MyOnClickListener());
			HeavyMetalModel_Btn.setOnClickListener(new MyOnClickListener());
					
		//公版与卓居的区别就是播放器的区别。
		if (!TextUtils.isEmpty(Build.DISPLAY) && (Build.DISPLAY).contains("zhuoju")) {
			rl_music_root.setVisibility(View.VISIBLE);
			gongban_player.setVisibility(View.GONE);
		}else{
			rl_music_root.setVisibility(View.GONE);
			gongban_player.setVisibility(View.VISIBLE);
		}
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

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);

			if (requestCode==1000){
				  Bundle bundle = data.getExtras();
		          String str_music_url = bundle.getString("music_url");
		          String str_music_title=bundle.getString("music_title");
		          System.out.println("接受到music"+"url:"+str_music_url+"title:"+str_music_title);
			}
		}


		class MyOnClickListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimerPingbaoUtil.CancelPingbaoTimer();
				TimerPingbaoUtil.StartPingbaoTimer(getActivity());
				switch (v.getId()) {
	            case R.id.btn_music:
	            	Intent musicIntent = panduan_apps_to_list_all(
							"com.tencent.qqmusictv");
					if (musicIntent == null) {
						Toast.makeText(getActivity(), "未安装QQ音乐", 2000).show();
						break;
					}
					
					Intent but_all_musicIntent= getActivity().getPackageManager()
	    					.getLaunchIntentForPackage("com.tencent.qqmusictv");		
					startActivity(but_all_musicIntent);
	            	
	            	
	            	break;
	            case R.id.btn_system_onff:
//	            	send_key(KeyEvent.KEYCODE_F12);
	            	Intent but_shezhi_xitongchongqi = new Intent();
					but_shezhi_xitongchongqi.setComponent(new ComponentName(
							"com.example.reset", "com.example.reset.MainActivity"));

					Intent but_shezhi_xitongchongqi_s = panduan_apps_to_list_all(
							"com.example.reset");
					if (but_shezhi_xitongchongqi_s == null) {
						app_handler.sendEmptyMessage(SOUSU_YINGYONG);
						url = "3288chongqi";
						name = "3288chongqi";
						app_handler.sendEmptyMessage(DOWNLOAD_SUM);
						break;
					}
					startActivity(but_shezhi_xitongchongqi);

	            	break;
	            case R.id.btn_movie:

					Intent musicIntent2 = panduan_apps_to_list_all(
							"com.ktcp.tvvideo");
					if (musicIntent2 == null) {
						Toast.makeText(getActivity(), "未安装腾讯视频", 2000).show();
						break;
					}
					
					Intent but_all_musicIntent2= getActivity().getPackageManager()
	    					.getLaunchIntentForPackage("com.ktcp.tvvideo");		
					startActivity(but_all_musicIntent2);
	            	
	            	
	            	break;
	               
				case R.id.btn_fm:
//					Toast.makeText(getActivity(), "网络电台", 2000).show();
					
//					Intent musicIntentDian = panduan_apps_to_list_all(
//							"com.gongjin.cradio");
//					if (musicIntentDian == null) {
//						Toast.makeText(getActivity(), "网络电台未安装", 2000).show();
//						break;
//					}
//					
//					Intent but_all_musicIntent22 = getActivity().getPackageManager()
//	    					.getLaunchIntentForPackage("com.gongjin.cradio");		
//					startActivity(but_all_musicIntent22);
					
					Intent launchIntent_but_bendi_555 = new Intent();
					launchIntent_but_bendi_555.setComponent(new ComponentName(
							"com.android.rk",
							"com.android.rk.RockExplorer"));
					ComponentName intent555 = launchIntent_but_bendi_555.getComponent();
					if (intent555 == null)
						return;
					Intent but_bendi_555_s = panduan_apps_to_list_all(
							"com.android.rk");
					if (but_bendi_555_s == null) {
						app_handler.sendEmptyMessage(SOUSU_YINGYONG);
						break;
					}
					startActivity(launchIntent_but_bendi_555);
					
					
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
				case R.id.rl_music_root:
					Intent rl_music_rootIntent= getActivity().getPackageManager().getLaunchIntentForPackage("com.lkj.musicinterface");	
					if (rl_music_rootIntent == null) {
						Toast.makeText(getActivity(), "卓居播放器未安装", 2000).show();
						break;
					}
					startActivity(rl_music_rootIntent);
					break;
				case R.id.btn_in:
					Intent audioIntent = panduan_apps_to_list_all(
							"com.csw.setaudio");
							if (audioIntent == null) {
					Toast.makeText(getActivity(), "音源切换未安装", 2000).show();
							}else{
							Intent mIntent=new Intent();
							ComponentName comp = new ComponentName
							("com.csw.setaudio","com.csw.setaudio.SoundDevicesManager");
							mIntent.setComponent(comp);
							startActivity(mIntent);
							Intent wifiMusicService = new Intent(getActivity(),AuxLineService.class);
							getActivity().startService(wifiMusicService);
							}
					
					break;
				case R.id.btn_phone:
				//	Toast.makeText(getActivity(), "手机助手", 2000).show();
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
					/*Intent intentMobileHelper=new Intent();
					intentMobileHelper.setClass(getActivity(),MobileHelperActivity.class);
					startActivity(intentMobileHelper);*/
					Intent intent  = getActivity().getPackageManager().getLaunchIntentForPackage("com.ecloud.eshare.server");
					if (intent==null) {
						Toast.makeText(getActivity(), "手机软件未安装", 2000).show();
						return;
					}
					startActivity(intent);
					break;
				case R.id.img_singer_photo:
					//Intent intent = new Intent(getActivity(),MusicListActivity.class);
					//HomeFragment.this.startActivityForResult(intent, 1000);
					break;
				case R.id.btn_music_play:
					if (mMediaPlayer == null) {
						return;
					}
					// mMediaPlayer.setSo
					if (mMediaPlayer.isPlaying()) {
						mMediaPlayer.pause();
						// but_play.setImageResource(R.drawable.play1);
						try {
							
							txt_music_title.setText(music.getTitle());
							btn_music_play.setBackground(getResources().getDrawable(
									R.drawable.music_play_selector));
							musicAnimationDrawable.stop();
						} catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						try {
							// player.prepare();
							System.out.println("准备播放了么"+music_stop);
							if (music_stop==true) {
								music_stop=false;
								mMediaPlayer.seekTo(music_progress);
								System.out.println("给歌曲设置进度:"+music_progress);
							}
							mMediaPlayer.start();
							// but_play.setImageResource(R.drawable.pause1);
							try {
								
								txt_music_title.setText(music.getTitle());
								btn_music_play.setBackground(getResources().getDrawable(
										R.drawable.music_pause_selector));
								musicAnimationDrawable.start();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					break;
				case R.id.btn_music_up:
					try {
						rewind();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case R.id.btn_music_down:
					try {
						if (playModel.equals("playModel_randomPlay")) {
							forward("playModel_randomPlay");
						} else {
							forward("playModel_turnPlay");
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case R.id.btn_music_shunxu:
					if (playModel.equals("playModel_fullCirculationPlay")) {
						playModel = "playModel_singalPlay";
						btn_music_shunxu.setBackground(getResources().getDrawable(R.drawable.music_danqu));
						Toast.makeText(getActivity(), "单曲循环", Toast.LENGTH_SHORT).show();
					}else if (playModel.equals("playModel_singalPlay")) {
						playModel="playModel_turnPlay";
						Toast.makeText(getActivity(), "顺序播放", Toast.LENGTH_SHORT).show();
						btn_music_shunxu.setBackground(getResources().getDrawable(R.drawable.music_leibiao));
					}else if (playModel.equals("playModel_turnPlay")) {
						playModel = "playModel_randomPlay";
						Toast.makeText(getActivity(), "随机播放", Toast.LENGTH_SHORT).show();
						btn_music_shunxu.setBackground(getResources().getDrawable(R.drawable.music_suiji));
					}else if (playModel.equals("playModel_randomPlay")) {
						playModel = "playModel_fullCirculationPlay";
						Toast.makeText(getActivity(), "全部循环", Toast.LENGTH_SHORT).show();
						btn_music_shunxu.setBackground(getResources().getDrawable(R.drawable.music_shunxu));
					}
					break;
				case R.id.btn_music_liebiao:
					Music_Dialog();
					break;
				case R.id.whichpath_sd:// 内置SD卡
					whichPathOwn = "internal_sd";
					txt_path.setText("本地音乐列表");
					mMusiclist = MusicList.getMusicData(getActivity(), whichPathOwn);
					musicAdapter = new MusicAdapter(getActivity(), mMusiclist);
					music_listview.setAdapter(musicAdapter);
					txt_music_sum.setText("歌曲数目"+String.valueOf(mMusiclist.size()));
					break;
				case R.id.whichpath_external_sd://外置SD卡
					whichPathOwn = "external_sd";
					txt_path.setText("SD卡音乐列表");
					mMusiclist = MusicList.getMusicData(getActivity(), whichPathOwn);
					musicAdapter = new MusicAdapter(getActivity(), mMusiclist);
					music_listview.setAdapter(musicAdapter);
					txt_music_sum.setText("歌曲数目"+String.valueOf(mMusiclist.size()));
					break;
				case R.id.whichpath_usb:// U盘
					whichPathOwn = "usb_storage";
					txt_path.setText("U盘音乐列表");
					mMusiclist = MusicList.getMusicData(getActivity(), whichPathOwn);
					musicAdapter = new MusicAdapter(getActivity(), mMusiclist);
					music_listview.setAdapter(musicAdapter);
					txt_music_sum.setText("歌曲数目"+String.valueOf(mMusiclist.size()));
					break;
				case R.id.btn_music_yinxiao:
					whichModelPopupWindow.showAsDropDown(v, 50, -158);
					break;
				case R.id.normalModelBtn:
					if (mMediaPlayer != null)
						setupEqualizeFxAndUi(1800, 1500, 1500, 1500, 1800);
					whichModelPopupWindow.dismiss();
					break;
				case R.id.ClassicalModel_Btn:
					if (mMediaPlayer != null)
						setupEqualizeFxAndUi(2100, 1700, 1400, 1750, 2000);
					whichModelPopupWindow.dismiss();
					break;
				case R.id.HipHopModel_Btn:
					if (mMediaPlayer != null)
						setupEqualizeFxAndUi(2100, 1900, 1500, 1600, 1800);
					whichModelPopupWindow.dismiss();
					break;
				case R.id.JazzModel_Btn:
					if (mMediaPlayer != null)
						setupEqualizeFxAndUi(1850, 1700, 1300, 1700, 2100);
					whichModelPopupWindow.dismiss();
					break;
				case R.id.PopModel_Btn:
					if (mMediaPlayer != null)
						setupEqualizeFxAndUi(1400, 1700, 2100, 1600, 1300);
					whichModelPopupWindow.dismiss();
					break;
				case R.id.HeavyMetalModel_Btn:
					if (mMediaPlayer != null)
						setupEqualizeFxAndUi(1800, 1600, 2500, 1700, 1500);
					whichModelPopupWindow.dismiss();
					break;
				case R.id.RockModel_Btn:
					if (mMediaPlayer != null)
						setupEqualizeFxAndUi(2000, 1700, 1400, 1800, 2100);
					whichModelPopupWindow.dismiss();
					break;
				default:
					break;
				}
			}

		}

		
		public void send_key(int keycode) {

			sendKeyCode(keycode);

		}

		/**
		 * 传入需要的键值即可
		 * 
		 * @param keyCode
		 */
		private void sendKeyCode(final int keyCode) {
			new Thread() {
				public void run() {
					try {
						System.out.println("模拟按键");
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(keyCode);
					} catch (Exception e) {
						Log.e("Exception when sendPointerSync", e.toString());
					}
				}
			}.start();
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
		/**
		 * @author Chopper 判断play为空时停止重置
		 */
		private void ReleaseResourcePlay() {
			// if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.reset(); // 重置播放器
			// player.release(); //释放play资源
			// player = null;
			// nowPlayingPosition = -1;
			// }
		}
		
		/**
		 * @author Chopper
		 * @param 初始化play对象
		 */
		private void initPlay() {
			/** 初始化play对象 */
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// player.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
			/** 播放完成 */
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				public void onCompletion(MediaPlayer mp) {
					// 下一首
					mMediaPlayer.reset();
					// if(playModel.equals("playModel_turnPlay")){
					// if (music_id > mMusiclist.size() - 1) {
					forward(playModel);
					// }
					// }

				}
			});
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					mMediaPlayer.reset();
					try {
						PlayMusic(music_id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return false;
				}
			});
		}

		
		private void PlayMusic(int id) {

			music = mMusiclist.get(id);
			if (music == null) {
				return;
			}
			Log.e("Id", String.valueOf(id));
			music_id = id;
			if (mMediaPlayer != null) {
				ReleaseResourcePlay();
			} else {
				initPlay();
			}
			String url = music.getUrl();
			Uri myUri = Uri.parse(url);
			try {
				mMediaPlayer.setDataSource(getActivity(), myUri);
				mMediaPlayer.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMediaPlayer.start();
			// if(mMediaPlayer.isPlaying()){
			musicAnimationDrawable.start();
			// }
			mHandler.sendEmptyMessage(MUSICINFOUPDATA_NUM);
			/** 实行线程监听 */
		//	Play_Thread_run();
			setupEqualizeFxAndUi(1500, 1500, 1500, 1500, 1500);
			// mHandler.sendEmptyMessage(100);//音量软启测试
		}
		
		/**
		 * 通过mMediaPlayer返回的AudioSessionId创建一个优先级为0均衡器对象 并且通过频谱生成相应的UI和对应的事件
		 */
		private void setupEqualizeFxAndUi(int progress1, int progress2,
				int progress3, int progress4, int progress5) {
			mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
			mEqualizer.setEnabled(true);// 启用均衡器

			// 通过均衡器得到其支持的频谱引擎
			short bands = mEqualizer.getNumberOfBands();

			// getBandLevelRange 是一个数组，返回一组频谱等级数组，
			// 第一个下标为最低的限度范围
			// 第二个下标为最大的上限,依次取出
			final short minEqualizer = mEqualizer.getBandLevelRange()[0];
			// final short maxEqualizer = mEqualizer.getBandLevelRange()[1];

			for (short i = 0; i < bands; i++) {
				final short band = i;
				if (band == 0) {
					mEqualizer.setBandLevel(band,
							(short) (progress1 + minEqualizer));
				} else if (band == 1) {
					mEqualizer.setBandLevel(band,
							(short) (progress2 + minEqualizer));
				} else if (band == 2) {
					mEqualizer.setBandLevel(band,
							(short) (progress3 + minEqualizer));
				} else if (band == 3) {
					mEqualizer.setBandLevel(band,
							(short) (progress4 + minEqualizer));
				} else if (band == 4) {
					mEqualizer.setBandLevel(band,
							(short) (progress5 + minEqualizer));
				}
			}
		}
		
		@SuppressLint("ShowToast")
		private void rewind() {
			if (music_id <= 0) {
				music_id = 0;
				Toast.makeText(getActivity(), "已经是第一首", 1).show();
			} else {
				music_id = music_id - 1;
			}

			if (mMediaPlayer != null) {
				ReleaseResourcePlay();
			} else {
				initPlay();
			}
			music = mMusiclist.get(music_id);
			// SongsActivity.listMusic.add(m);
			mHandler.sendEmptyMessage(MUSICINFOUPDATA_NUM);

			String url = music.getUrl();
			Uri myUri = Uri.parse(url);// 匹配当前地址
			try {
				mMediaPlayer.setDataSource(mContext, myUri);
				mMediaPlayer.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}

			mMediaPlayer.start();

		//	Play_Thread_run();
		}

		private void forward(String playModel) {

			if (playModel.equals("playModel_turnPlay")) {
				if (music_id >= mMusiclist.size() - 1) {
					music_id = mMusiclist.size() - 1;
					Toast.makeText(getActivity(), "已经是最后一首", 1).show();

				} else {
					music_id = music_id + 1;
				}
			} else if (playModel.equals("playModel_randomPlay")) {
				Random random = new Random();
				if (mMusiclist.size() > 0)
					music_id = Math.abs(random.nextInt(mMusiclist.size() - 1))
							% (mMusiclist.size());
			} else if (playModel.equals("playModel_singalPlay")) {

			} else if (playModel.equals("playModel_fullCirculationPlay")) {
				if (music_id >= mMusiclist.size() - 1) {
					music_id = 0;

				} else {
					music_id = music_id + 1;
				}
			}

			if (playModel.equals("playModel_turnPlay")
					&& (music_id >mMusiclist.size() - 1)) {

			}

			System.out.println("这个id是" + music_id);
			if (mMediaPlayer != null) {
				ReleaseResourcePlay();
			} else {
				initPlay();
			}
			music = mMusiclist.get(music_id);
			mHandler.sendEmptyMessage(MUSICINFOUPDATA_NUM);
			String url = music.getUrl();
			Uri myUri = Uri.parse(url);
			try {
				mMediaPlayer.setDataSource(mContext, myUri);
				mMediaPlayer.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMediaPlayer.start();

			System.out.println("这个url是" + url);
			musicAnimationDrawable.start();

			//Play_Thread_run();
		}
		
		
		 private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case MUSICINFOUPDATA_NUM:
					txt_music_title.setText(music.getTitle());
					// String totalTime=MusicUtil.toTime((long) music.getTime());
				/*	String totalTime = MusicUtil.toTime((long) mMediaPlayer
							.getDuration());
					textEndTime.setText(totalTime);*/
					btn_music_play.setBackgroundDrawable(getActivity().getResources().getDrawable(
							R.drawable.music_pause_selector));
					break;
			
				default:
					break;
				}
			}

		};

		
		private void Music_Dialog(){
			final Dialog dialog = new Dialog(getActivity(),R.style.Dialog);
			View view =mInfater.inflate(R.layout.musiclist, null);
			dialog.setContentView(view);
		    txt_path=(TextView) view.findViewById(R.id.txt_path);
		    txt_music_sum=(TextView) view.findViewById(R.id.txt_music_sum);
			Button txtButton=(Button)view.findViewById(R.id.btn_path);
			txtButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					whichPathPopupWindow.showAsDropDown(v, -30, 0);
				}
			});
			music_listview=(ListView) view.findViewById(R.id.music_listview);
			mMusiclist = MusicList.getMusicData(getActivity(), whichPathOwn);
			musicAdapter = new MusicAdapter(getActivity(), mMusiclist);
			music_listview.setAdapter(musicAdapter);
			txt_path.setText("本地音乐列表");
			txt_music_sum.setText("歌曲数目"+String.valueOf(mMusiclist.size()));
			dialog.show();
			music_listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					PlayMusic(arg2);
					dialog.dismiss();
					
				}
			}); 
		
		}
		
		@Override
		public void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			System.out.println("chopper --- > onStop() : " + setDreamPlay);
			if(setDreamPlay.equals("1")){
				System.out.println("chopper --- > setDreamPlay ");
				setDreamPlay = "2";
				if(musicAnimationDrawable != null){
				musicAnimationDrawable.stop();
				}
				return ;
			}

			if (mMediaPlayer != null) {
				    music_stop=true;
				if (mMediaPlayer.isPlaying()) {
					music_progress=mMediaPlayer.getCurrentPosition();
					mMediaPlayer.pause();
					musicAnimationDrawable.stop();
					mMediaPlayer.stop();

				} else {
					mMediaPlayer.stop();

				}
			}

		}
		
		
		private BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction().equals(PRESS_SD_PLAY_BUTTON)) {
					//Toast.makeText(mContext, "333", Toast.LENGTH_SHORT).show();
					//sendKeyCode(KeyEvent.KEYCODE_HOME);
					//send_key(KeyEvent.KEYCODE_HOME);
					Intent home_intent= getActivity().getPackageManager()
	    					.getLaunchIntentForPackage("com.csw.csw_desktop");
					startActivity(home_intent);
					btn_music_liebiao.callOnClick();
					whichPathOwn_external_sd.callOnClick();
				}else if(intent.getAction().equals(PRESS_USB_PLAY_BUTTON)) {
					//Toast.makeText(mContext, "444", Toast.LENGTH_SHORT).show();
					//sendKeyCode(KeyEvent.KEYCODE_HOME);
					//send_key(KeyEvent.KEYCODE_HOME);
					Intent home_intent= getActivity().getPackageManager()
	    					.getLaunchIntentForPackage("com.csw.csw_desktop");
					startActivity(home_intent);
					btn_music_liebiao.callOnClick();
					whichPathOwn_usb.callOnClick();
				}else if(intent.getAction().equals(OPEN_AUX_BROADCAST)) {
					Intent home_intent= getActivity().getPackageManager()
	    					.getLaunchIntentForPackage("com.csw.csw_desktop");
					startActivity(home_intent);
					btn_in.callOnClick();
				}else if(intent.getAction().equals(SERIAL_ACTION_NAME)){//串口发过来的广播
					 Log.e("SERIAL_ACTION_NAME:", "接收到串口广播");
					String infomationSerial = intent.getStringExtra("serial_action");
					 Log.e("infomationSerial:", infomationSerial);
					 if(infomationSerial.equals("serial_play_pause")){//播放、暂停
						
					//	if (weatherPlayMusic == true) {
							btn_music_play.callOnClick();
							Toast.makeText(getActivity(), "play/pause",
									Toast.LENGTH_SHORT).show();
					//	}
						
					}else if(infomationSerial.equals("serial_previous")){//上一曲
						
					//	if (weatherPlayMusic == true) {
							btn_music_up.callOnClick();
							Toast.makeText(getActivity(), "上一曲",
									Toast.LENGTH_SHORT).show();
					//	}
					
					}else if(infomationSerial.equals("serial_next")){//下一曲
						
					//	if (weatherPlayMusic == true) {
							btn_music_down.callOnClick();
							Toast.makeText(getActivity(), "下一曲",
									Toast.LENGTH_SHORT).show();
					//	}
						
					}else if(infomationSerial.equals("serial_all_cycle")){//全部循环
						playModel="playModel_randomPlay";
						btn_music_shunxu.callOnClick();
						Toast.makeText(getActivity(), "全部循环",
								Toast.LENGTH_SHORT).show();
					}else if(infomationSerial.equals("serial_one_cycle")){//单曲循环
						playModel="playModel_fullCirculationPlay";
						btn_music_shunxu.callOnClick();
						Toast.makeText(getActivity(), "单曲循环",
								Toast.LENGTH_SHORT).show();
					}else if(infomationSerial.equals("serial_turn_paly")){//顺序播放
						playModel="playModel_singalPlay";
						btn_music_shunxu.callOnClick();
						Toast.makeText(getActivity(), "顺序播放",
								Toast.LENGTH_SHORT).show();
					}else if(infomationSerial.equals("serial_random_paly")){//随机播放
						playModel="playModel_turnPlay";
						btn_music_shunxu.callOnClick();
						Toast.makeText(getActivity(), "随机播放",
								Toast.LENGTH_SHORT).show();
					}else if(infomationSerial.equals("musicModel_normal")){//自然
						
						NormalModel_Btn.callOnClick();
						Toast.makeText(getActivity(), "自然", Toast.LENGTH_SHORT)
								.show();
						
					}else if(infomationSerial.equals("musicModel_heavy")){//重低音
						
						HeavyMetalModel_Btn.callOnClick();
						Toast.makeText(getActivity(), "重低音", Toast.LENGTH_SHORT)
								.show();
						
					}else if(infomationSerial.equals("musicModel_rock")){//摇滚
						
						RockModel_Btn.callOnClick();
						Toast.makeText(getActivity(), "摇滚", Toast.LENGTH_SHORT)
								.show();
						
					}else if(infomationSerial.equals("musicModel_classical")){//古典
						
						ClassicalModel_Btn.callOnClick();
						Toast.makeText(getActivity(), "古典", Toast.LENGTH_SHORT)
								.show();
						
					}else if(infomationSerial.equals("musicModel_jazz")){//爵士
						JazzModel_Btn.callOnClick();
						Toast.makeText(getActivity(), "爵士", Toast.LENGTH_SHORT)
								.show();
						
					}else if(infomationSerial.equals("musicModel_Pop")){//语言
						
						PopModel_Btn.callOnClick();
						Toast.makeText(getActivity(), "Pop", Toast.LENGTH_SHORT)
								.show();
						
					}else if(infomationSerial.equals("musicModel_Hip_Hop")){//舞曲
						
						HipHopModel_Btn.callOnClick();
						Toast.makeText(getActivity(), "Hip Hop",
								Toast.LENGTH_SHORT).show();
						
					}
				}
			}
			
		};
		
		
		@Override
		public void show() {
			// TODO Auto-generated method stub
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
