package com.csw.tp.activity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongqin.tianlai.R;
import com.csw.tp.alladapter.Music_adapter;
import com.csw.tp.babei.wyf.model.Gequinfo;
import com.csw.tp.babei.wyf.model.Getallmusicinfo;
import com.csw.tp.babei.wyf.model.Music;
import com.csw.tp.bean.DataEntity;
import com.csw.tp.bean.SystemInfo;
import com.csw.tp.music.MusicAdapter;
import com.csw.tp.music.MusicList;
import com.csw.tp.util.SharedPreferencesUtils;
import com.csw.tp.wyf.util.MusicConstant;
import com.csw.tp.wyf.util.MusicUtil;

public class MusicplayAct extends Activity {
   
	private ListView musicListView;
	private List<Music> mMusiclist = new ArrayList<Music>();
	MusicAdapter musicAdapter;
	Music_adapter adapter;
	private boolean istvmusic=false;
	/** 设置声音 */
	private AudioManager mAudioManager;
    private TextView nametv = null;
	private TextView music_play_infoText;
	private TextView textStartTime, textEndTime;
	private String infomation3,inforname,inforindex;
	private SeekBar mAudioProgressSeekbar;
	private long mLastSeekEventTime;
	private long mPlayProgress = -1;
	private Socket toClientSocket;
	private int index=0;
	OutputStream socketOutputStream;
	/** 上一曲、播放、下一曲 */
	private Button previousSongBtn, playBtn, nextSongBtn;
	/** 全部循环、单曲循环、随机播放、顺序播放、静音 */
	private Button  playSingleBtn, playRandomBtn, playTurnBtn,
			muteBtn;
	private static int  playModel = 0;//0代表的是顺序播放，1代表随机播放，2代表单曲循环

	/** MediaPlayer */
	public static MediaPlayer mMediaPlayer = null;
	private int nowPlayingPosition = -1;
	private boolean isLoop = false;
	private boolean isSlience = false;
	/** 按静音之前保存的音量值 */
	private static int volumeTemp = 0;// 按静音之前的值

	/** 计算当前播放列表的位置 */
	public static int music_id = -1;
	private Music music;
	private Context mContext;

	public static double witch;
	public static double height;

	private static String whichPathOwn = "internal_sd";// 在哪个目录下，U盘或者SD卡
	private PopupWindow whichPathPopupWindow;
	private View whichPathOwnView = null;
	private Button whichPathOwn_sd;
	//private Button whichPathOwn_external_sd;
	private Button whichPathOwn_usb;

	LayoutInflater mInfater;

	private PopupWindow whichModelPopupWindow;
	private View whichModelView = null;

	private Button NormalModel_Btn;// normal
	private Button ClassicalModel_Btn;
	private Button HipHopModel_Btn;
	private Button JazzModel_Btn;
	private Button PopModel_Btn;
	private Button RockModel_Btn;
	private Button HeavyMetalModel_Btn;

	private Button whichPathMusiclistBtn;
	private Button musicJunHengQiBtn;
        String phone_index;
	private Equalizer mEqualizer;
	private Button goBackBtn;
	private Timer mDreamTimer = new Timer();
	private TimerTask mDreamTimerTask;

	private ImageView musicGoImageView;
	private AnimationDrawable musicAnimationDrawable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musicplay_main);
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 给常量类中的屏幕高和宽赋值
//		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
//		Window window = getWindow();
//		witch = display.getWidth();
//		height = display.getHeight();
		istvmusic = getIntent().getBooleanExtra("istvmusic", false);
		 if (istvmusic) {
			 index = Integer.parseInt(getIntent().getStringExtra("action_index"));
		}
		nametv = (TextView) findViewById(R.id.nametv);
		initWidget();
		infomation3 = getIntent().getStringExtra("url");
		 inforname = getIntent().getStringExtra("name");
		 phone_index = getIntent().getStringExtra("phone_index");
		 if (!istvmusic) {
			 if (null!=infomation3) {
				   index = Integer.parseInt(phone_index);
				   mHandler.sendEmptyMessage(0x50);
					PlayMusic(infomation3);
					//nametv.setText(inforname);
					music_play_infoText.setText(inforname);
				}
		}
		 if (istvmusic) {
			 PlayMusic(Getallmusicinfo.data.get(index).getUrl());
			 music_play_infoText.setText(Getallmusicinfo.data.get(index).getName());
		}
		
		 musicListView.setFocusable(true);
		 musicListView.requestFocus();
		SystemInfo.isfirst=true;
	}
	
	/** 初始化控件 */
	private void initWidget() {

		previousSongBtn = (Button) findViewById(R.id.but_up);
		playBtn = (Button) findViewById(R.id.but_play);
		nextSongBtn = (Button) findViewById(R.id.but_down);
		muteBtn = (Button) findViewById(R.id.muteBtn);
		playTurnBtn = (Button) findViewById(R.id.but_return);
		playRandomBtn = (Button) findViewById(R.id.but_random_broadcast);
		playSingleBtn = (Button) findViewById(R.id.but_single_cycle);
		previousSongBtn.setOnClickListener(new mBtnOnClickListener());
		playBtn.setOnClickListener(new mBtnOnClickListener());
		nextSongBtn.setOnClickListener(new mBtnOnClickListener());
		muteBtn.setOnClickListener(new mBtnOnClickListener());
		playTurnBtn.setOnClickListener(new mBtnOnClickListener());
		playRandomBtn.setOnClickListener(new mBtnOnClickListener());
		playSingleBtn.setOnClickListener(new mBtnOnClickListener());
		music_play_infoText = (TextView) this
				.findViewById(R.id.music_play_info);
		textEndTime = (TextView) this.findViewById(R.id.txt_music_stop_time);
		textStartTime = (TextView) this.findViewById(R.id.txt_music_start_time);
		mAudioProgressSeekbar = (SeekBar) findViewById(R.id.music_play_seek);
		mAudioProgressSeekbar.setOnSeekBarChangeListener(mOnSeekListener);
		musicListView = (ListView) findViewById(R.id.music_listview);
		mMusiclist = MusicList.getMusicData(this, whichPathOwn);
		musicAdapter = new MusicAdapter(this, mMusiclist);
		if (!istvmusic) {
			if (Gequinfo.music_list.size()>0) {
				adapter = new Music_adapter(Gequinfo.music_list, MusicplayAct.this);
				musicListView.setAdapter(adapter);
				
			}
		}
		else if(istvmusic){
			if (Getallmusicinfo.data.size()>0) {
				Getallmusicinfo.data.clear();
			}
			Getallmusicinfo alltvgequinfo = new Getallmusicinfo(MusicplayAct.this);
			adapter = new Music_adapter(alltvgequinfo.data, MusicplayAct.this);
			musicListView.setAdapter(adapter);
		}
		
		musicListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!istvmusic) {
					for (int i = 0; i < Gequinfo.music_list.size(); i++) {
						Gequinfo.music_list.get(i).setIscheck(false);
					}
					Gequinfo.music_list.get(arg2).setIscheck(true);
					adapter.notifyDataSetChanged();
					try {
						sendindex(arg2+"", DataEntity.currentos);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}  
				else if(istvmusic){
					index=arg2;
					for (int i = 0; i < Getallmusicinfo.data.size(); i++) {
						Getallmusicinfo.data.get(i).setIscheck(false);
					}
					 Getallmusicinfo.data.get(index).setIscheck(true);
					adapter.notifyDataSetChanged();
			        PlayMusic(Getallmusicinfo.data.get(arg2).getUrl());
					music_play_infoText.setText(Getallmusicinfo.data.get(arg2).getName());
					try {
						sendindex(index+"", DataEntity.currentos);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		});
		whichPathMusiclistBtn = (Button) this.findViewById(R.id.but_leibiao);
		whichPathMusiclistBtn.setOnClickListener(new mBtnOnClickListener());

		mInfater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*
		 * ======================================================================
		 * ===
		 */
		whichPathOwnView = mInfater.inflate(R.layout.whichpath_item, null);
		whichPathPopupWindow = new PopupWindow(whichPathOwnView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		whichPathPopupWindow.setFocusable(true);
		whichPathPopupWindow.setOutsideTouchable(true);
		whichPathPopupWindow.update();
		whichPathPopupWindow.setBackgroundDrawable(new BitmapDrawable());

		whichPathOwn_sd = (Button) whichPathOwnView
				.findViewById(R.id.whichpath_sd);
		whichPathOwn_usb = (Button) whichPathOwnView
				.findViewById(R.id.whichpath_usb);
		whichPathOwn_sd.setOnClickListener(new mBtnOnClickListener());
		whichPathOwn_usb.setOnClickListener(new mBtnOnClickListener());
		/*
		 * ======================================================================
		 * ===
		 */
		musicJunHengQiBtn = (Button) this.findViewById(R.id.but_equalizer);
		musicJunHengQiBtn.setOnClickListener(new mBtnOnClickListener());
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
		NormalModel_Btn.setOnClickListener(new mBtnOnClickListener());
		ClassicalModel_Btn.setOnClickListener(new mBtnOnClickListener());
		HipHopModel_Btn.setOnClickListener(new mBtnOnClickListener());
		JazzModel_Btn.setOnClickListener(new mBtnOnClickListener());
		PopModel_Btn.setOnClickListener(new mBtnOnClickListener());
		RockModel_Btn.setOnClickListener(new mBtnOnClickListener());
		HeavyMetalModel_Btn.setOnClickListener(new mBtnOnClickListener());
		/*
		 * ======================================================================
		 * =
		 */
		goBackBtn = (Button) this.findViewById(R.id.goBackBtn);
		goBackBtn.setOnClickListener(new mBtnOnClickListener());

		musicGoImageView = (ImageView) this.findViewById(R.id.img_music_rhythm);
		musicGoImageView.setBackgroundResource(R.anim.music_animation);
		Object backgroundObject = musicGoImageView.getBackground();
		musicAnimationDrawable = (AnimationDrawable) backgroundObject;
		// musicAnimationDrawable.start();

		// toClientSocket=DataEntity.currentSocket;

	}
	/**
	 * @author Chopper按钮功能区的监听
	 */
	private class mBtnOnClickListener implements OnClickListener {

		@SuppressLint("NewApi")
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.muteBtn:// 静音
				if (isSlience == false) {

					muteBtn.setBackground(getResources().getDrawable(
							R.drawable.music_volume_close_selector));
					isSlience = true;
					volumeTemp = mAudioManager
							.getStreamVolume(AudioManager.STREAM_MUSIC);
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
							0);

					sendWhatToClient = "client_mute_1";// 静音
					mHandler.sendEmptyMessage(MusicConstant.SEND_WHAT_TO_CLIENT);

				} else {
					muteBtn.setBackground(getResources().getDrawable(
							R.drawable.music_volemu_open_selector));
					isSlience = false;
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							volumeTemp,// 这里控制音量的直接大小
							0);

					sendWhatToClient = "client_mute_0";// 取消静音
					mHandler.sendEmptyMessage(MusicConstant.SEND_WHAT_TO_CLIENT);
				}
				// first();
				break;
			case R.id.but_up:// 上一曲
				//Toast.makeText(mContext, "正在准备播放上一曲", Toast.LENGTH_SHORT).show();
				if (!istvmusic) {
				if (mMediaPlayer==null) {
					return;
				}
				mMediaPlayer.pause();
				try {
					sendcount("last",DataEntity.currentos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}else if (istvmusic) {
					if (index==0) {
						index=Getallmusicinfo.data.size()-1;
					}else {
						index--;
					}
					try {
						sendindex(index+"", DataEntity.currentos);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					PlayMusic(Getallmusicinfo.data.get(index).getUrl());
					music_play_infoText.setText(Getallmusicinfo.data.get(index).getName());
				}
				mHandler.sendEmptyMessage(0x50);
//				try {
//					rewind();
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}

				break;
			case R.id.but_play:// 播放

				if (mMediaPlayer == null) {
					return;
				}
				// mMediaPlayer.setSo
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.pause();
					// but_play.setImageResource(R.drawable.play1);
					try {
						if (music_play_infoText == null) {
							music_play_infoText = (TextView) MusicplayAct.this
									.findViewById(R.id.music_play_info);
						}
						music_play_infoText.setText(inforname);
						playBtn.setBackground(getResources().getDrawable(
								R.drawable.music_player_selector));
						musicAnimationDrawable.stop();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						// mMediaPlayer.prepare();
						mMediaPlayer.start();
					 //but_play.setImageResource(R.drawable.pause1);
						try {
							if (music_play_infoText == null) {
								music_play_infoText = (TextView)  MusicplayAct.this
										.findViewById(R.id.music_play_info);
							}

							music_play_infoText.setText(inforname);
							playBtn.setBackground(getResources().getDrawable(
									R.drawable.music_tp_pause_selector));
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
			case R.id.but_down:// 下一曲
				//Toast.makeText(mContext, "正在准备播放下一曲", Toast.LENGTH_SHORT).show();
				if (!istvmusic) {
					if (mMediaPlayer==null) {
						return;
					}
					mMediaPlayer.pause();
					try {
						sendcount("next",DataEntity.currentos);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (istvmusic) {
					if (index==Getallmusicinfo.data.size()-1) {
						index=0;
					}else {
						index++;
					}
					try {
						sendindex(index+"", DataEntity.currentos);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					PlayMusic(Getallmusicinfo.data.get(index).getUrl());
					music_play_infoText.setText(Getallmusicinfo.data.get(index).getName());
				}
				mHandler.sendEmptyMessage(0x50);

//				try {
//					if (playModel.equals("playModel_randomPlay")) {
//						forward("playModel_randomPlay");
//					} else {
//						forward("playModel_turnPlay");
//					}
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				break;
//			case R.id.but_Orde_play:// 全部循环
//				/*
//				 * if (isLoop == false) { // but_Orde_play //
//				 * .setBackgroundResource(R.drawable.music_loop_selector);
//				 * isLoop = true; } else { // but_Orde_play //
//				 * .setBackgroundResource(R.drawable.music_xh_selector); isLoop
//				 * = false; }
//				 */
//				playModel = "playModel_fullCirculationPlay";
//
//				sendWhatToClient = "client_model_full";// 全部循环
//				mHandler.sendEmptyMessage(MusicConstant.SEND_WHAT_TO_CLIENT);
//
//				playAllCycleBtn
//						.setBackgroundResource(R.drawable.music_station_allcycle1);
//				playSingleBtn
//						.setBackgroundResource(R.drawable.music_singlecycle_selector);
//				playRandomBtn
//						.setBackgroundResource(R.drawable.music_random_selector);
//				playTurnBtn
//						.setBackgroundResource(R.drawable.music_turn_selector);
//
//				break;
			case R.id.but_single_cycle:// 单曲循环
			  playModel = 2;
			  Toast.makeText(MusicplayAct.this, "单曲循环",
						Toast.LENGTH_SHORT).show();
//
//				sendWhatToClient = "client_model_single";// 单曲循环
//				mHandler.sendEmptyMessage(MusicConstant.SEND_WHAT_TO_CLIENT);
//
				playSingleBtn
						.setBackgroundResource(R.drawable.music_station_singlecycle1);
				playRandomBtn
						.setBackgroundResource(R.drawable.music_random_selector);
				playTurnBtn
						.setBackgroundResource(R.drawable.music_turn_selector);
				try {
					sendmusicmodle(2+"",DataEntity.currentos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case R.id.but_random_broadcast:// 随机播放
				playModel = 1;
				 Toast.makeText(MusicplayAct.this, "随机播放",
							Toast.LENGTH_SHORT).show();
//
//				sendWhatToClient = "client_model_random";// 单曲循环
//				mHandler.sendEmptyMessage(MusicConstant.SEND_WHAT_TO_CLIENT);
//
				playSingleBtn
						.setBackgroundResource(R.drawable.music_singlecycle_selector);
				playRandomBtn
						.setBackgroundResource(R.drawable.music_station_random1);
				playTurnBtn
						.setBackgroundResource(R.drawable.music_turn_selector);
				try {
					sendmusicmodle(1+"",DataEntity.currentos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//
				break;
			case R.id.but_return:// 顺序播放
				playModel = 0;
				playSingleBtn
						.setBackgroundResource(R.drawable.music_singlecycle_selector);
				playRandomBtn
						.setBackgroundResource(R.drawable.music_random_selector);
				playTurnBtn
						.setBackgroundResource(R.drawable.music_station_turn1);
				try {
					sendmusicmodle(0+"",DataEntity.currentos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 Toast.makeText(MusicplayAct.this, "顺序播放",
							Toast.LENGTH_SHORT).show();
				break;

			case R.id.but_leibiao:
				//whichPathPopupWindow.showAsDropDown(v, 0, 0);

				break;
			case R.id.whichpath_sd:// 内置SD卡
//				if (Gequinfo.music_list.size()>0) {
//					adapter = new Music_adapter(Gequinfo.music_list, MusicplayAct.this);
//					musicListView.setAdapter(adapter);
//				}
				break;
//			case R.id.whichpath_external_sd:
//				whichPathOwn = "external_sd";
//				mMusiclist = MusicList.getMusicData( MusicplayAct.this,
//						whichPathOwn);
//				musicAdapter = new MusicAdapter( MusicplayAct.this, mMusiclist);
//				musicListView.setAdapter(musicAdapter);
//				musicListView.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1,
//							int arg2, long arg3) {
//						//PlayMusic(arg2);
//					}
//				});
//				break;
			case R.id.whichpath_usb:// U盘
				
				 
				break;

			case R.id.but_equalizer:
				whichModelPopupWindow.showAsDropDown(v, 0, 0);
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
			case R.id.goBackBtn:
				//  Musicplay.this.finish();

				if (mMediaPlayer != null) {
					if (mMediaPlayer.isPlaying()) {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						startActivity(intent);
					} else {
						 MusicplayAct.this.finish();

					}
				} else {
					 MusicplayAct.this.finish();

				}

				break;
			default:
				break;
			}
		}
	}
	
	/** 进度条监听 */
	private OnSeekBarChangeListener mOnSeekListener = new OnSeekBarChangeListener() {
		public void onStartTrackingTouch(SeekBar bar) {
			mLastSeekEventTime = 0;
		}

		public void onProgressChanged(SeekBar bar, int progress,
				boolean fromtouch) {
			if (mMediaPlayer == null)
				return;
			if (fromtouch) {
				long now = SystemClock.elapsedRealtime();
				if ((now - mLastSeekEventTime) > 250) {
					mLastSeekEventTime = now;
					mPlayProgress = mMediaPlayer.getDuration() * progress / 100;
					mMediaPlayer.seekTo((int) mPlayProgress);
					mMediaPlayer.start();
					// if(mMediaPlayer.isPlaying())
					musicAnimationDrawable.start();
				}
			}
			/*
			 * int proTo = mAudioProgressSeekbar.getProgress() *
			 * mMediaPlayer.getDuration() / 100;
			 * 
			 * if(proTo>mMediaPlayer.getCurrentPosition()){
			 * mAudioProgressSeekbar.setProgress(mMediaPlayer.getCurrentPosition());
			 * }else{ mMediaPlayer.seekTo((int) proTo); mMediaPlayer.start(); }
			 */
		}

		public void onStopTrackingTouch(SeekBar bar) {
			if (mMediaPlayer == null)
				return;
			int proTo = mAudioProgressSeekbar.getProgress()
					* mMediaPlayer.getDuration() / 100;

			if (proTo > mMediaPlayer.getCurrentPosition()) {
				mAudioProgressSeekbar.setProgress(mMediaPlayer
						.getCurrentPosition());
				// musicAnimationDrawable.start();
			} else {
				mMediaPlayer.seekTo((int) proTo);
				mMediaPlayer.start();
				// musicAnimationDrawable.start();
			}
			musicAnimationDrawable.start();
		}
	};

	/**
	 * @author Chopper 判断play为空时停止重置
	 */
	private void ReleaseResourcePlay() {
		// if (mMediaPlayer.isPlaying()) {
		mMediaPlayer.stop();
		mMediaPlayer.reset(); // 重置播放器
		// mMediaPlayer.release(); //释放play资源
		// mMediaPlayer = null;
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
		// mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
		/** 播放完成 */
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@SuppressLint("NewApi")
			public void onCompletion(MediaPlayer mp) {
				// 下一首
				//mMediaPlayer.reset();
				// if(playModel.equals("playModel_turnPlay")){
				// if (music_id > mMusiclist.size() - 1) {
				//PlayMusic(infomation3);
				// }
				// }
				if (playModel==0) {//顺序播放
					nextSongBtn.callOnClick();
					System.out.println("播放完了播放完了播放完了播放完了");
				}else if(playModel==1){//随机播放
					Random r = new Random();  
					if (!istvmusic) {
						int suiji_index = r.nextInt(Gequinfo.music_list.size()-1);
						index=suiji_index;
						for (int i = 0; i < Gequinfo.music_list.size(); i++) {
							Gequinfo.music_list.get(i).setIscheck(false);
						}
						Gequinfo.music_list.get(suiji_index).setIscheck(true);
						adapter.notifyDataSetChanged();
						try {
							sendindex(suiji_index+"", DataEntity.currentos);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(istvmusic) {
						int suiji_index = r.nextInt(Getallmusicinfo.data.size()-1);
						index=suiji_index;
						for (int i = 0; i < Getallmusicinfo.data.size(); i++) {
							Getallmusicinfo.data.get(i).setIscheck(false);
						}
						Getallmusicinfo.data.get(suiji_index).setIscheck(true);
						adapter.notifyDataSetChanged();
						 PlayMusic(Getallmusicinfo.data.get(suiji_index).getUrl());
							music_play_infoText.setText(Getallmusicinfo.data.get(suiji_index).getName());
							try {
								sendindex(index+"", DataEntity.currentos);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
					}
				}else if(playModel==2){//单曲循环
					if (istvmusic) {
						PlayMusic(Getallmusicinfo.data.get(index).getUrl());
						music_play_infoText.setText(Getallmusicinfo.data.get(index).getName());
					}else if(!istvmusic&&null!=infomation3&&null!=inforname){
						PlayMusic(infomation3);
						music_play_infoText.setText(inforname);
					}
					
				}
				

			}
		});
		mMediaPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				mMediaPlayer.reset();
				try {
					//PlayMusic(music_id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		});
	}
	private void PlayMusic(String url) {

//		music = mMusiclist.get(id);
//		if (music == null) {
//			return;
//		}
//		Log.e("Id", String.valueOf(id));
//		music_id = id;
		if (mMediaPlayer != null) {
			ReleaseResourcePlay();
		} else {
			initPlay();
		}
		//String url = music.getUrl();
		Uri myUri = Uri.parse(url);
		try {
			mMediaPlayer.setDataSource( MusicplayAct.this, myUri);
			mMediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mMediaPlayer.start();
		// if(mMediaPlayer.isPlaying()){
		musicAnimationDrawable.start();
		// }
		mHandler.sendEmptyMessage(MusicConstant.MUSICINFOUPDATA_NUM);
		
		/** 实行线程监听 */
		Play_Thread_run();
		setupEqualizeFxAndUi(1500, 1500, 1500, 1500, 1500);
		// mHandler.sendEmptyMessage(100);//音量软启测试
	}
	private void Play_Thread_run() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean isTrue = true;
				while (isTrue == true) {
					try {
						Thread.sleep(100);
						if (mMediaPlayer == null) {
							isTrue = false;
						} else if (mMediaPlayer.isPlaying()) {
							nowPlayingPosition = mMediaPlayer
									.getCurrentPosition();
							Message msg = seekBarUpdateHandler.obtainMessage();
							msg.what = nowPlayingPosition;
							seekBarUpdateHandler
									.sendEmptyMessage(nowPlayingPosition);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});
		if (thread.isAlive()) {

		} else {
			thread.start();
		}
	}
	/**
	 * @author Chopper 更新歌曲进度条
	 */
	private Handler seekBarUpdateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int position = msg.what;// 获取handler 跳转位置
			if (null!=mMediaPlayer) {
				int total = mMediaPlayer.getDuration();
				int progress = position * 100 / total;
				textStartTime.setText(MusicUtil.toTime(position));
				mAudioProgressSeekbar.setProgress(progress);
			}
			
		
			super.handleMessage(msg);
		}
	};
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MusicConstant.MUSICINFOUPDATA_NUM:
				//music_play_infoText.setText(music.getTitle());
				// String totalTime=MusicUtil.toTime((long) music.getTime());
				String totalTime = MusicUtil.toTime((long) mMediaPlayer
						.getDuration());
				textEndTime.setText(totalTime);
				playBtn.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.music_tp_pause_selector));
				break;

			case 100:// 音量软启
				// volumeSoftUpThread.start();

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						for (int i = 0; i < 10; i++) {
							try {
								Thread.sleep(600);
								mAudioManager.setStreamVolume(
										AudioManager.STREAM_MUSIC, i + 1, 0);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}).start();

				break;
			case MusicConstant.SEND_WHAT_TO_CLIENT:
				// sendActionThread.start();
				if (sendWhatToClient.equals("client_mute_1")) {
					sendDataToClient("client_mute_1");
				} else if (sendWhatToClient.equals("client_mute_0")) {
					sendDataToClient("client_mute_0");
				} else if (sendWhatToClient.equals("client_model_full")) {
					sendDataToClient("client_model_full");
				} else if (sendWhatToClient.equals("client_model_single")) {
					sendDataToClient("client_model_single");
				} else if (sendWhatToClient.equals("client_model_random")) {
					sendDataToClient("client_model_random");
				} else if (sendWhatToClient.equals("client_model_turn")) {
					sendDataToClient("client_model_turn");
				}

				break;
			case 12:
				mHandler.sendEmptyMessage(0x50);
				PlayMusic(infomation3);
				music_play_infoText.setText(inforname);
				if(istvmusic){
					mHandler.sendEmptyMessage(0x30);
				}
				break;
			case 0x10:
				 index=0;
				if (Getallmusicinfo.data.size()>0) {
					adapter=null;
					adapter = new Music_adapter(Getallmusicinfo.data, MusicplayAct.this);
					musicListView.setAdapter(adapter);
					istvmusic = true;
				}
				
				
				break;
			case 0x20:
		        index = Integer.parseInt(inforindex);
		        String surl =Getallmusicinfo.data.get(index).getUrl(); 
		        mHandler.sendEmptyMessage(0x50);
		        PlayMusic(Getallmusicinfo.data.get(index).getUrl());
				music_play_infoText.setText(Getallmusicinfo.data.get(index).getName());
				break;
			case 0x30:
				istvmusic = false;
				if (Gequinfo.music_list.size()>0) {
					adapter = new Music_adapter(Gequinfo.music_list, MusicplayAct.this);
					musicListView.setAdapter(adapter);
				}
				break;
			case 0x50:
				if(istvmusic){
					for (int i = 0; i < Getallmusicinfo.data.size(); i++) {
						Getallmusicinfo.data.get(i).setIscheck(false);
					}
					Getallmusicinfo.data.get(index).setIscheck(true);
				}else if(!istvmusic){
					for (int i = 0; i <Gequinfo.music_list.size(); i++) {
						Gequinfo.music_list.get(i).setIscheck(false);
					}
					Gequinfo.music_list.get(index).setIscheck(true);
					
				}
				musicListView.setSelection(index);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

	};

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

//	@SuppressLint("ShowToast")
//	private void rewind() {
//		if (music_id <= 0) {
//			music_id = 0;
//			Toast.makeText(this, "已经是第一首", 1).show();
//		} else {
//			music_id = music_id - 1;
//		}
//
//		if (mMediaPlayer != null) {
//			ReleaseResourcePlay();
//		} else {
//			initPlay();
//		}
//		music = mMusiclist.get(music_id);
//		// SongsActivity.listMusic.add(m);
//		mHandler.sendEmptyMessage(MusicConstant.MUSICINFOUPDATA_NUM);
//
//		String url = music.getUrl();
//		Uri myUri = Uri.parse(url);// 匹配当前地址
//		try {
//			mMediaPlayer.setDataSource(mContext, myUri);
//			mMediaPlayer.prepare();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		mMediaPlayer.start();
//
//		Play_Thread_run();
//	}

//	private void forward(String playModel) {
//
//		if (playModel.equals("playModel_turnPlay")) {
//			if (music_id >= mMusiclist.size() - 1) {
//				music_id = mMusiclist.size() - 1;
//				Toast.makeText(this, "已经是最后一首", 1).show();
//
//			} else {
//				music_id = music_id + 1;
//			}
//		} else if (playModel.equals("playModel_randomPlay")) {
//			Random random = new Random();
//			if (mMusiclist.size() > 0)
//				
//				music_id = Math.abs(random.nextInt(mMusiclist.size() - 1))
//						% (mMusiclist.size());
//		} else if (playModel.equals("playModel_singalPlay")) {
//
//		} else if (playModel.equals("playModel_fullCirculationPlay")) {
//			if (music_id >= mMusiclist.size() - 1) {
//				music_id = 0;
//
//			} else {
//				music_id = music_id + 1;
//			}
//		}
//
//		if (playModel.equals("playModel_turnPlay")
//				&& (music_id > mMusiclist.size() - 1)) {
//
//		}
//
//		System.out.println("这个id是" + music_id);
//		if (mMediaPlayer != null) {
//			ReleaseResourcePlay();
//		} else {
//			initPlay();
//		}
//		music = mMusiclist.get(music_id);
//		mHandler.sendEmptyMessage(MusicConstant.MUSICINFOUPDATA_NUM);
//		String url = music.getUrl();
//		Uri myUri = Uri.parse(url);
//		try {
//			mMediaPlayer.setDataSource(mContext, myUri);
//			mMediaPlayer.prepare();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		mMediaPlayer.start();
//
//		System.out.println("这个url是" + url);
//		musicAnimationDrawable.start();
//
//		Play_Thread_run();
//	}

	public String jiequString(String x) {

		int c = x.indexOf("/", 5);
		String z = x.substring(5, c);
		return z;
	}

	public String jiequ(String x) {
		int c = x.indexOf("/", 5);
		String z = x.substring(c + 1, x.length());
		return z;
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */


	private void registerClientBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(CLIENT_ACTION_NAME);
		 //注册广播
		registerReceiver(clientBroadcastReceiver, myIntentFilter);
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRunningInBack = false;

		unregisterReceiver(clientBroadcastReceiver);
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
         SystemInfo.isfirst = false;
//		mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
		weatherPlayMusic=false;
	}
	public static boolean dlnaMusicFlag = false;// 默认dlna音乐没有播放
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		Log.i("onRestart()", "------------");
		// playBtn.callOnClick();
		dlnaMusicFlag = false;
		weatherPlayMusic=true;

	}

	public static boolean isRunningInBack = false;// 判断播放器是否在后台运行，默认没

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("onResume()", "------------");

		isRunningInBack = false;
		super.onResume();
        weatherPlayMusic=true;
        dlnaMusicFlag=false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("onPause()", "------------");
		// playBtn.callOnClick();
		isRunningInBack = true;

		if (dlnaMusicFlag == true) {
			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.pause();
					musicAnimationDrawable.stop();
					 mMediaPlayer.stop();
//					 mMediaPlayer.release();
				}
			}
		}
//		weatherPlayMusic=false;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("onStop()", "------------");
		if (mDreamTimer != null) {
			mDreamTimer.cancel();
			mDreamTimer = null;
		}
		if (mDreamTimerTask != null) {
			mDreamTimerTask.cancel();
			mDreamTimerTask = null;
		}

		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				musicAnimationDrawable.stop();
				mMediaPlayer.stop();
				
			}else{
				mMediaPlayer.stop();
			}
		}
		weatherPlayMusic=false;
		SystemInfo.isfirst = false;
	}
	/**
	 * 判断播放器是否按播放键播放,false不播放
	 */
	public static boolean weatherPlayMusic=false;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		weatherPlayMusic=true;
		SharedPreferencesUtils.setData(MusicplayAct.this, "isplaying", "1");
		registerClientBoradcastReceiver();
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		System.out.println("开始初始化一些音频信息");
//		int result = mAudioManager.requestAudioFocus(
//				mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
//				AudioManager.AUDIOFOCUS_GAIN);
//
//		System.out.println("初始化音频焦点" + result);
		//initTimeUpData();

		Log.i("onStart()", "------------");
		if (mDreamTimer == null) {
			mDreamTimer = new Timer();
		}
		if (mDreamTimerTask == null) {
			mDreamTimerTask = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Intent _Intent = new Intent();
						ComponentName _ComponentName = new ComponentName(
								"com.android.setdream",
								"com.android.setdream.MainActivity");
						_Intent.setComponent(_ComponentName);
						 MusicplayAct.this.startActivity(_Intent);
						Log.i("onStart()", "执行屏保------------");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.i("onStart()", "Exception------------");
						e.printStackTrace();
					}
				}

			};
		}
		if (mDreamTimerTask != null && mDreamTimer != null) {
			mDreamTimer.schedule(mDreamTimerTask, 6000*10*2, 6000*10*2);
		}

	}
	
	/**
	 * 返回主页
	 */
	private void goHome() {
		PackageManager pm = getPackageManager();
		ResolveInfo homeInfo = pm.resolveActivity(
				new Intent(Intent.ACTION_MAIN)
						.addCategory(Intent.CATEGORY_HOME), 0);

		ActivityInfo ai = homeInfo.activityInfo;
		Intent startIntent = new Intent(Intent.ACTION_MAIN);
		startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
		startActivitySafely(startIntent);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		PackageManager pm = getPackageManager();
//		ResolveInfo homeInfo = pm.resolveActivity(
//				new Intent(Intent.ACTION_MAIN)
//						.addCategory(Intent.CATEGORY_HOME), 0);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//if (mMediaPlayer != null) {
//				if (mMediaPlayer.isPlaying()) {
//					ActivityInfo ai = homeInfo.activityInfo;
//					Intent startIntent = new Intent(Intent.ACTION_MAIN);
//					startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//					startIntent.setComponent(new ComponentName(ai.packageName,
//							ai.name));
//					startActivitySafely(startIntent);
//					return true;
//				} else {
//					 MusicplayAct.this.finish();
//					return true;
//				}
			//} else {
			SystemInfo.isfirst = false;
				 MusicplayAct.this.finish();
				//return true;
			//}
//		} else {
			//return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void startActivitySafely(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
		} catch (SecurityException e) {
			Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * 处理其他Activity返回的数据
	 */
	/**
	 * 
	 */
	private static String sendWhatToClient = "";

	/**
	 * 
	 */
	Thread sendActionThread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (sendWhatToClient.equals("client_mute_1")) {
				sendDataToClient("client_mute_1");
			} else if (sendWhatToClient.equals("client_mute_0")) {
				sendDataToClient("client_mute_0");
			} else if (sendWhatToClient.equals("client_model_full")) {
				sendDataToClient("client_model_full");
			} else if (sendWhatToClient.equals("client_model_single")) {
				sendDataToClient("client_model_single");
			} else if (sendWhatToClient.equals("client_model_random")) {
				sendDataToClient("client_model_random");
			} else if (sendWhatToClient.equals("client_model_turn")) {
				sendDataToClient("client_model_turn");
			}
		}

	});

	/**
	 * 发送数据到客户端
	 * 
	 * @param connectFlag
	 */
	private void sendDataToClient(String connectFlag) {
		try {
			toClientSocket = DataEntity.currentSocket;
			if (toClientSocket != null) {
				socketOutputStream = toClientSocket.getOutputStream();
				byte[] conByte = connectFlag.getBytes("GBK");
				socketOutputStream.write(conByte);
				Log.d("发送的什么", connectFlag);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 音频焦点监听
	 */
	OnAudioFocusChangeListener mOnAudioFocusChangeListener = new OnAudioFocusChangeListener() {

		@Override
		public void onAudioFocusChange(int focusChange) {
			switch (focusChange) {
			case AudioManager.AUDIOFOCUS_GAIN:
				System.out.println("获得音频焦点");
				// 获得音频焦点
				if (!mMediaPlayer.isPlaying()) {
					mMediaPlayer.start();
				}
				// 还原音量
				mMediaPlayer.setVolume(1.0f, 1.0f);
				break;

			case AudioManager.AUDIOFOCUS_LOSS:
				// 长久的失去音频焦点，释放MediaPlayer
				System.out.println("长久的失去音频焦点，释放MediaPlayer");
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
				break;

			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
				// 展示失去音频焦点，暂停播放等待重新获得音频焦点
				System.out.println("展示失去音频焦点，暂停播放等待重新获得音频焦点");
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.pause();
				break;
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
				System.out.println("失去音频焦点，无需停止播放，降低声音即可");
				// 失去音频焦点，无需停止播放，降低声音即可
				if (mMediaPlayer.isPlaying()) {
					// mMediaPlayer.setVolume(0.1f, 0.1f);
					mMediaPlayer.setVolume(0.0f, 0.0f);
					System.out.println("播放器失去焦点");
				}
				break;
			}
		}
	};
	
	/**
	 * 监听客户端消息广播，更新界面
	 */
	 
	public static final String CLIENT_ACTION_NAME = "infomation_from_client";
	BroadcastReceiver clientBroadcastReceiver = new BroadcastReceiver() {

		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			String infomation = intent.getStringExtra("action_action");
			String infomation1 = intent.getStringExtra("whichPath");
			String infomation2 = intent.getStringExtra("musicPath");
			String infomation4 = intent.getStringExtra("action_phone");
			infomation3 = intent.getStringExtra("music_url");
			String phone_index = intent.getStringExtra("phone_index");
			if (null==intent.getStringExtra("name")) {
			}else {
				inforname = intent.getStringExtra("name");
			}
			inforindex = intent.getStringExtra("action_index");
			// System.out.println(infomation);
			// System.out.println(infomation1);
			// System.out.println(infomation2);

			if (action.equals(CLIENT_ACTION_NAME)) {
				System.out.println("收到广播");
				if (infomation.equals("music_path")) {//点击 以及下一曲，上一曲操作
					if (null!=infomation3&&!"".equals(infomation3) ) {
						index = Integer.parseInt(phone_index);
						mHandler.sendEmptyMessage(12);
						
					}
					
				}else if(infomation.equals("index")){ //手机端点击music
					if (null!=inforindex&&!"".equals(inforindex)) {
					mHandler.sendEmptyMessage(0x20);
				    }
			  }else if(infomation.equals("action_phone")){ //由tv端music切换为手机端music
						if (null!=infomation4&&!"".equals(infomation4)) {
							index=0;
							mHandler.sendEmptyMessage(0x30);
				 }
			 }else if (infomation.equals("server_mute")) {// 静音
					muteBtn.callOnClick();
				} 
//				else if (infomation.equals("server_previousSong")) {// 上一首
//					if(weatherPlayMusic==true){
//						previousSongBtn.callOnClick();
//						Toast.makeText(MusicplayAct.this, "上一曲", Toast.LENGTH_SHORT)
//						.show();
//					}
//					
//					action_index
//				} 
				
				else if (infomation.equals("gettvmusic")) {//拿到tv端歌曲
					  
					   mHandler.sendEmptyMessage(0x10);
				}
				else if (infomation.equals("server_play")) {// 播放
					if(weatherPlayMusic==true){
						playBtn.callOnClick();
						Toast.makeText(MusicplayAct.this, "play/pause",
								Toast.LENGTH_SHORT).show();
					}

				} 
//				else if (infomation.equals("server_nextSong")) {// 下一首
//					
//					if(weatherPlayMusic==true){
//						nextSongBtn.callOnClick();
//						Toast.makeText(MusicplayAct.this, "下一曲", Toast.LENGTH_SHORT)
//								.show();
//					}
//				} else if (infomation.equals("server_fullCirculation")) {// 全部循环
//					playAllCycleBtn.callOnClick();
//					Toast.makeText(MusicplayAct.this, "全部循环",
//							Toast.LENGTH_SHORT).show();
//				} 
				else if (infomation.equals("server_singleCycle")) {// 单曲循环
					playSingleBtn.callOnClick();
					Toast.makeText(MusicplayAct.this, "单曲循环",
							Toast.LENGTH_SHORT).show();
				} else if (infomation.equals("server_turnPlay")) {// 顺序播放
					playTurnBtn.callOnClick();
					Toast.makeText(MusicplayAct.this, "顺序播放",
							Toast.LENGTH_SHORT).show();
				} else if (infomation.equals("server_randomPlay")) {// 随机播放
					playRandomBtn.callOnClick();
					Toast.makeText(MusicplayAct.this, "随机播放",
							Toast.LENGTH_SHORT).show();
				} 
//					else if (infomation.equals("client_come_path")) {
//					if (!infomation1.equals("")) {
//						whichPathMusiclistBtn.callOnClick();
//                        System.out.println(infomation1+"whichPathMusiclistBtn.callOnClick();");
//						if (infomation1.equals("internal_sd")) {
//							whichPathOwn_sd.callOnClick();
//							System.out.println(infomation1+".callOnClick();");
//						} else if (infomation1.equals("usb_storage")) {
//							whichPathOwn_usb.callOnClick();
//							System.out.println(infomation1+".callOnClick();");
//						} else if (infomation1.equals("external_sd")) {
//							whichPathOwn_external_sd.callOnClick();
//							System.out.println(infomation1+"callOnClick();");
//						}
//						whichPathPopupWindow.dismiss();
//					}
//
//				} else if (infomation.equals("server_clientMusicToServer")) {
//
//					if (!infomation2.equals("")) {
//
//						for (int i = 0; i < mMusiclist.size(); i++) {
//
//							String musicPath = mMusiclist.get(i).getUrl();
//							if (infomation2.equals(musicPath)) {
//								music_id = i;
//								System.out.println(musicPath);
//								break;
//							}
//
//						}
//
//						//PlayMusic(music_id);
//						System.out.println(infomation2);
//					}
//				} 
				else if (infomation.equals("timeISup")) {
					if (!infomation2.equals("")) {
						System.out
								.println("timeISup..............................");

						for (int i = 0; i < mMusiclist.size(); i++) {

							String musicPath = mMusiclist.get(i).getUrl();
							if (infomation2.equals(musicPath)) {
								music_id = i;
								System.out.println(musicPath);
								break;
							}

						}
						//PlayMusic(music_id);
						mHandler.sendEmptyMessage(100);// 音量软启测试

					}
				} else if (infomation.equals("musicModel_normal")) {
					NormalModel_Btn.callOnClick();
					Toast.makeText(MusicplayAct.this, "自然", Toast.LENGTH_SHORT)
							.show();
				} else if (infomation.equals("musicModel_classical")) {
					ClassicalModel_Btn.callOnClick();
					Toast.makeText(MusicplayAct.this, "古典", Toast.LENGTH_SHORT)
							.show();
				} else if (infomation.equals("musicModel_Hip_Hop")) {
					HipHopModel_Btn.callOnClick();
					Toast.makeText(MusicplayAct.this, "Hip Hop",
							Toast.LENGTH_SHORT).show();
				} else if (infomation.equals("musicModel_jazz")) {
					JazzModel_Btn.callOnClick();
					Toast.makeText(MusicplayAct.this, "爵士", Toast.LENGTH_SHORT)
							.show();
				} else if (infomation.equals("musicModel_Pop")) {
					PopModel_Btn.callOnClick();
					Toast.makeText(MusicplayAct.this, "Pop", Toast.LENGTH_SHORT)
							.show();
				} else if (infomation.equals("musicModel_rock")) {
					RockModel_Btn.callOnClick();
					Toast.makeText(MusicplayAct.this, "摇滚", Toast.LENGTH_SHORT)
							.show();
				} else if (infomation.equals("musicModel_heavy")) {
					HeavyMetalModel_Btn.callOnClick();
					Toast.makeText(MusicplayAct.this, "重低音", Toast.LENGTH_SHORT)
							.show();
				} else if (infomation.equals("server_play_stop")) {// 定时关机时广播
					System.out.println("收到停止播放音乐的广播");
					if (mMediaPlayer != null) {
						if (mMediaPlayer.isPlaying()) {
							mMediaPlayer.pause();
							// MainActivity.mMediaPlayer.release();
							// MainActivity.mMediaPlayer = null;
							System.out.println("暂停音乐播放器的音乐");
						}
					}
				} else if (infomation.equals("finish_app")) {
					System.out.println("收到finish——app广播");
					MusicplayAct.this.finish();
				}
			}
		}
	};
	
	/**
	 * 
	 * @param dwb
	 * @param 向手机端发送上下区的指令
	 * 
	 */
	private void sendcount(String str,OutputStream os) throws IOException{
		if (null!=os) {
			String jsonResult =  str;
			System.out.println(">>>>>>>jsonresult:"+jsonResult);
			// Log.e(null, jsonResult);
			byte[] dataByte = null;
			dataByte = jsonResult.getBytes("GBK");
			os.write(dataByte, 0, dataByte.length);
			os.flush();
			System.out.println("开始下一曲开始下一曲");
		}

	}
	/**
	 * 
	 * @param dwb
	 * @param 向手机端发送点击的歌曲的信息
	 * 
	 */
	private void sendindex(String str,OutputStream os) throws IOException{
		if (null!=os) {
			String jsonResult = "index_start:" +str+":index_end";
			System.out.println(">>>>>>>jsonresult:"+jsonResult);
			// Log.e(null, jsonResult);
			byte[] dataByte = null;
			dataByte = jsonResult.getBytes("GBK");
			os.write(dataByte, 0, dataByte.length);
			os.flush();
		}

	}
	/**
	 * 
	 * @param dwb
	 * @param 向手机端发送播放的模式
	 * 
	 */
	private void sendmusicmodle(String str,OutputStream os) throws IOException{
		if (null!=os) {
			String jsonResult = "modle_start:" +str+":modle_end";
			System.out.println(">>>>>>>jsonresult:"+jsonResult);
			// Log.e(null, jsonResult);
			byte[] dataByte = null;
			dataByte = jsonResult.getBytes("GBK");
			os.write(dataByte, 0, dataByte.length);
			os.flush();
		}

	}
	private void getFileName(File[] files) {  
        if (files != null) {// 先判断目录是否为空，否则会报空指针  
            for (File file : files) {  
                if (file.isDirectory()) {  
                    Log.i("zeng", "若是文件目录。继续读1" + file.getName().toString()  
                            + file.getPath().toString());  
  
                    getFileName(file.listFiles());  
                    Log.i("zeng", "若是文件目录。继续读2" + file.getName().toString()  
                            + file.getPath().toString());  
                } else {  
                    String fileName = file.getName();  
                    if (fileName.endsWith(".mp3")||fileName.endsWith(".wav")) {  
                    	Gequinfo  fi = new Gequinfo();
	        			fi.setName(file.getName());
	        			fi.setUrl(file.getPath());
                    }  
                }  
            }  
        }  
    }  
	
	
}
