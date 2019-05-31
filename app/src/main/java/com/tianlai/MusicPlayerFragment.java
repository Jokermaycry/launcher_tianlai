package com.tianlai;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongqin.tianlai.R;
import com.csw.download.DownManager;
import com.csw.music.Music;
import com.csw.music.MusicAdapter;
import com.csw.music.MusicList;
import com.wyf.allapp.GetAppInfo;
import com.wyf.util.TimerPingbaoUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.csw.newfragment.HomeFragment.OPEN_AUX_BROADCAST;
import static com.csw.newfragment.HomeFragment.PRESS_SD_PLAY_BUTTON;
import static com.csw.newfragment.HomeFragment.PRESS_USB_PLAY_BUTTON;
import static com.csw.newfragment.HomeFragment.SERIAL_ACTION_NAME;
import static com.csw.newfragment.HomeFragment.mMediaPlayer;
import static com.csw.newfragment.HomeFragment.mMusiclist;
import static com.csw.newfragment.HomeFragment.music_id;
import static com.csw.newfragment.HomeFragment.setDreamPlay;
import static com.csw.tp.wyf.util.MusicConstant.MUSICINFOUPDATA_NUM;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicPlayerFragment extends Fragment {
//    public static MediaPlayer mMediaPlayer = null;
//    public static List<Music> mMusiclist = new ArrayList<Music>();
//    public static int music_id = -1;
//    public static String setDreamPlay = "0";
//    private static final int MUSICINFOUPDATA_NUM=99;
//    private final static int SOUSU_YINGYONG = 2;
//    private final static int DOWNLOAD_SUM = 3;
//    HashMap<String, String> mHashMap = new HashMap<String, String>();
    private String url;
    private String name;
    private static String playModel = "playModel_fullCirculationPlay";
    private Equalizer mEqualizer;
    private Button whichPathOwn_external_sd;
    private Button whichPathOwn_usb;
    private Button whichPathOwn_sd;

    private Button btn_play_type;
    private Button btn_play_pre;
    private Button btn_play_play;
    private Button btn_play_next;
    private Button btn_xz;
    private Button btn_bflb;
    private Button btn_sc;
    private Button btn_fx;
    private Button btn_gd;

    private Music music;
    private TextView txt_music_titles ;
    private DownManager manager;
    private boolean music_stop;
    private int music_progress;
    private Context mContext;
    LayoutInflater mInfater;
  //  private AnimationDrawable musicAnimationDrawable;

    private TextView txt_path;
    private TextView txt_music_sum;
    private PopupWindow whichPathPopupWindow;
    private View whichPathOwnView = null;
    private ListView music_listview;
    private static String whichPathOwn = "internal_sd";// 在哪个目录下，U盘或者SD卡
    private MusicAdapter musicAdapter;


    public MusicPlayerFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        manager = new DownManager(getActivity());
        System.out.println("执行了..");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        mInfater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        whichPathOwnView = mInfater.inflate(R.layout.whichpath_item, null);
        whichPathPopupWindow = new PopupWindow(whichPathOwnView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        whichPathPopupWindow.setFocusable(true);
        whichPathPopupWindow.setOutsideTouchable(true);
        whichPathPopupWindow.update();
        whichPathPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        btn_play_type= (Button)view.findViewById(R.id.btn_play_type);
        btn_play_pre= (Button)view.findViewById(R.id.btn_play_pre);
        btn_play_play= (Button)view.findViewById(R.id.btn_play_play);
        btn_play_next= (Button)view.findViewById(R.id.btn_play_next);
        btn_bflb= (Button)view.findViewById(R.id.btn_bflb);
        txt_music_titles=(TextView) view.findViewById(R.id.txt_music_titles);

        whichPathOwn_sd = (Button) whichPathOwnView.findViewById(R.id.whichpath_sd);
        whichPathOwn_external_sd = (Button) whichPathOwnView.findViewById(R.id.whichpath_external_sd);
        whichPathOwn_usb = (Button) whichPathOwnView.findViewById(R.id.whichpath_usb);

        btn_play_type.setOnClickListener(new MyOnClickListeners());
        btn_play_next.setOnClickListener(new MyOnClickListeners());
        btn_play_pre.setOnClickListener(new MyOnClickListeners());
        btn_play_play.setOnClickListener(new MyOnClickListeners());
        btn_bflb.setOnClickListener(new MyOnClickListeners());
        whichPathOwn_sd.setOnClickListener(new MyOnClickListeners());
        whichPathOwn_external_sd.setOnClickListener(new MyOnClickListeners());
        whichPathOwn_usb.setOnClickListener(new MyOnClickListeners());



        return view;

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
    private void Music_Dialog(){
        final Dialog dialog = new Dialog(getActivity(),R.style.Dialog);
        View view =mInfater.inflate(R.layout.musiclist, null);
        dialog.setContentView(view);
        txt_path=(TextView) view.findViewById(R.id.txt_path);
        txt_music_sum=(TextView) view.findViewById(R.id.txt_music_sum);
        Button txtButton=(Button)view.findViewById(R.id.btn_path);
        txtButton.setOnClickListener(new View.OnClickListener() {

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
        music_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(PRESS_SD_PLAY_BUTTON)) {
                //Toast.makeText(mContext, "333", Toast.LENGTH_SHORT).show();
                //sendKeyCode(KeyEvent.KEYCODE_HOME);
                //send_key(KeyEvent.KEYCODE_HOME);
                Intent home_intent= getActivity().getPackageManager()
                        .getLaunchIntentForPackage("com.csw.csw_desktop");
                startActivity(home_intent);
                btn_bflb.callOnClick();
                whichPathOwn_external_sd.callOnClick();
            }else if(intent.getAction().equals(PRESS_USB_PLAY_BUTTON)) {
                //Toast.makeText(mContext, "444", Toast.LENGTH_SHORT).show();
                //sendKeyCode(KeyEvent.KEYCODE_HOME);
                //send_key(KeyEvent.KEYCODE_HOME);
                Intent home_intent= getActivity().getPackageManager()
                        .getLaunchIntentForPackage("com.csw.csw_desktop");
                startActivity(home_intent);
                btn_bflb.callOnClick();
                whichPathOwn_usb.callOnClick();
            }
//            else if(intent.getAction().equals(OPEN_AUX_BROADCAST)) {
//                Intent home_intent= getActivity().getPackageManager()
//                        .getLaunchIntentForPackage("com.csw.csw_desktop");
//                startActivity(home_intent);
//                btn_in.callOnClick();
//            }
            else if (intent.getAction().equals(SERIAL_ACTION_NAME)) {//串口发过来的广播
                Log.e("SERIAL_ACTION_NAME:", "接收到串口广播");
                String infomationSerial = intent.getStringExtra("serial_action");
                Log.e("infomationSerial:", infomationSerial);
                if (infomationSerial.equals("serial_play_pause")) {//播放、暂停

                    //	if (weatherPlayMusic == true) {
                    btn_play_play.callOnClick();
                    Toast.makeText(getActivity(), "play/pause",
                            Toast.LENGTH_SHORT).show();
                    //	}

                } else
                    if (infomationSerial.equals("serial_previous")) {//上一曲

                    //	if (weatherPlayMusic == true) {
                    btn_play_pre.callOnClick();
                    Toast.makeText(getActivity(), "上一曲",
                            Toast.LENGTH_SHORT).show();
                    //	}

                } else if (infomationSerial.equals("serial_next")) {//下一曲

                    //	if (weatherPlayMusic == true) {
                    btn_play_next.callOnClick();
                    Toast.makeText(getActivity(), "下一曲",
                            Toast.LENGTH_SHORT).show();
                    //	}
                }
            }
        }
        };
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
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

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
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

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
    class MyOnClickListeners implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            TimerPingbaoUtil.CancelPingbaoTimer();
            TimerPingbaoUtil.StartPingbaoTimer(getActivity());
            switch (v.getId()) {
                case R.id.btn_play_type:
                    if (playModel.equals("playModel_fullCirculationPlay")) {
                        playModel = "playModel_singalPlay";
                        btn_play_type.setBackground(getResources().getDrawable(R.drawable.music_danqu));
                        Toast.makeText(getActivity(), "单曲循环", Toast.LENGTH_SHORT).show();
                    }else if (playModel.equals("playModel_singalPlay")) {
                        playModel="playModel_turnPlay";
                        Toast.makeText(getActivity(), "顺序播放", Toast.LENGTH_SHORT).show();
                        btn_play_type.setBackground(getResources().getDrawable(R.drawable.music_leibiao));
                    }else if (playModel.equals("playModel_turnPlay")) {
                        playModel = "playModel_randomPlay";
                        Toast.makeText(getActivity(), "随机播放", Toast.LENGTH_SHORT).show();
                        btn_play_type.setBackground(getResources().getDrawable(R.drawable.music_suiji));
                    }else if (playModel.equals("playModel_randomPlay")) {
                        playModel = "playModel_fullCirculationPlay";
                        Toast.makeText(getActivity(), "全部循环", Toast.LENGTH_SHORT).show();
                        btn_play_type.setBackground(getResources().getDrawable(R.drawable.music_shunxu));
                    }
                    break;
                case R.id.btn_play_next:
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
                case R.id.btn_play_pre:
                    try {
                        rewind();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                case R.id.btn_play_play:
                    if (mMediaPlayer == null) {
                        return;
                    }
                    // mMediaPlayer.setSo
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        // but_play.setImageResource(R.drawable.play1);
                        try {

                               txt_music_titles.setText(music.getTitle());
                            btn_play_play.setBackground(getResources().getDrawable(
                                    R.drawable.music_play_selector));
                           // musicAnimationDrawable.stop();
                        } catch (Resources.NotFoundException e) {
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

                                     txt_music_titles.setText(music.getTitle());
                                btn_play_play.setBackground(getResources().getDrawable(
                                        R.drawable.music_pause_selector));
                               // musicAnimationDrawable.start();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.btn_bflb:
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


            }
        }
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
         if(mMediaPlayer.isPlaying()){
       // musicAnimationDrawable.start();
         }
        mHandler.sendEmptyMessage(MUSICINFOUPDATA_NUM);
        /** 实行线程监听 */
        //	Play_Thread_run();
//        setupEqualizeFxAndUi(1500, 1500, 1500, 1500, 1500);
        // mHandler.sendEmptyMessage(100);//音量软启测试
    }
//    /**
//     * 通过mMediaPlayer返回的AudioSessionId创建一个优先级为0均衡器对象 并且通过频谱生成相应的UI和对应的事件
//     */
//    private void setupEqualizeFxAndUi(int progress1, int progress2,
//                                      int progress3, int progress4, int progress5) {
//        mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
//        mEqualizer.setEnabled(true);// 启用均衡器
//
//        // 通过均衡器得到其支持的频谱引擎
//        short bands = mEqualizer.getNumberOfBands();
//
//        // getBandLevelRange 是一个数组，返回一组频谱等级数组，
//        // 第一个下标为最低的限度范围
//        // 第二个下标为最大的上限,依次取出
//        final short minEqualizer = mEqualizer.getBandLevelRange()[0];
//        // final short maxEqualizer = mEqualizer.getBandLevelRange()[1];
//
//        for (short i = 0; i < bands; i++) {
//            final short band = i;
//            if (band == 0) {
//                mEqualizer.setBandLevel(band,
//                        (short) (progress1 + minEqualizer));
//            } else if (band == 1) {
//                mEqualizer.setBandLevel(band,
//                        (short) (progress2 + minEqualizer));
//            } else if (band == 2) {
//                mEqualizer.setBandLevel(band,
//                        (short) (progress3 + minEqualizer));
//            } else if (band == 3) {
//                mEqualizer.setBandLevel(band,
//                        (short) (progress4 + minEqualizer));
//            } else if (band == 4) {
//                mEqualizer.setBandLevel(band,
//                        (short) (progress5 + minEqualizer));
//            }
//        }
//    }
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
       // musicAnimationDrawable.start();

        //Play_Thread_run();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        System.out.println("chopper --- > onStop() : " + setDreamPlay);
        if (setDreamPlay.equals("1")) {
            System.out.println("chopper --- > setDreamPlay ");
            setDreamPlay = "2";
//            if (musicAnimationDrawable != null) {
//             //   musicAnimationDrawable.stop();
//            }
            return;
        }
            if (mMediaPlayer != null) {
                music_stop = true;
                if (mMediaPlayer.isPlaying()) {
                    music_progress = mMediaPlayer.getCurrentPosition();
                    mMediaPlayer.pause();
                 //   musicAnimationDrawable.stop();
                    mMediaPlayer.stop();

                } else {
                    mMediaPlayer.stop();
                }
            }
        }



    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case MUSICINFOUPDATA_NUM:
                    txt_music_titles.setText(music.getTitle());
                    // String totalTime=MusicUtil.toTime((long) music.getTime());
				/*	String totalTime = MusicUtil.toTime((long) mMediaPlayer
							.getDuration());
					textEndTime.setText(totalTime);*/
                    btn_play_play.setBackgroundDrawable(getActivity().getResources().getDrawable(
                            R.drawable.music_pause_selector));
                    break;

                default:
                    break;
            }
        }

    };

}