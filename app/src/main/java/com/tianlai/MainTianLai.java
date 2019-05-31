package com.tianlai;



import android.annotation.NonNull;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
//import android.support.annotation.NonNull;
//import android.support.v4.widget.DrawerLayout;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
//import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.content.res.Resources;


import com.cl.service.AuxLineService;
import com.zhongqin.tianlai.R;
import com.csw.music.Music;
import com.csw.music.MusicAdapter;
import com.csw.music.MusicList;
import com.csw.newfragment.AllAppFragment;
import com.weather.manager.DBHelper;
import com.weather.manager.TianqiActivity;
import com.weather.manager.WeatherManager;
import com.wyf.allapp.AddKJappActivity;
import com.wyf.allapp.GetAppInfo;
import com.wyf.app.AppInfo;
import com.wyf.util.Constant;
import com.wyf.util.SharedPreferencesUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.csw.newfragment.HomeFragment.OPEN_AUX_BROADCAST;
import static com.csw.newfragment.HomeFragment.PRESS_SD_PLAY_BUTTON;
import static com.csw.newfragment.HomeFragment.PRESS_USB_PLAY_BUTTON;
import static com.csw.newfragment.HomeFragment.SERIAL_ACTION_NAME;
import static com.csw.newfragment.HomeFragment.mMediaPlayer;
import static com.csw.newfragment.HomeFragment.mMusiclist;
import static com.csw.newfragment.HomeFragment.music_id;
import static com.csw.newfragment.HomeFragment.setDreamPlay;
import static com.csw.tp.wyf.util.MusicConstant.MUSICINFOUPDATA_NUM;




public class MainTianLai extends FragmentActivity implements OnGetCurrentDateTimeListener {
    private AnimationDrawable animationDrawable;
  //  private DrawerLayout sideMenu;
  private SharedPreferencesUtils preferencesUtils;
    private List<AppInfo> App_List = new ArrayList<AppInfo>();
    private GetAppInfo getAppInfo;
    private Button btn_bottom_1;
    private Button btn_bottom_2;
    private Button btn_bottom_3;
    private Button btn_bottom_4;
    private Button btn_bottom_5;
    private Button btn_bottom_6;

    private TextView chengshi,weathers,temp1,temp2;
    private String weathercityname;
    private String weathermin;
    private String weathermax;
    private String weather;
    private String address;
    private ImageView currentimage;

    private DateTimeUtil dateTimeUtil;
    private TextView date;
    private TextView time;
    private TextView week;

    LayoutInflater mInfater;
    private PopupWindow whichPathPopupWindow;
    private static String whichPathOwn = "internal_sd";// 在哪个目录下，U盘或者SD卡
    private TextView txt_path;
    private TextView txt_music_sum;
    private ListView music_listview;
    public static List<Music> mMusiclist = new ArrayList<Music>();
    private MusicAdapter musicAdapter;
    private View whichPathOwnView = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintianlai);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);}
        getAppInfo = new GetAppInfo(getApplicationContext());


        mInfater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        whichPathOwnView = mInfater.inflate(R.layout.whichpath_item, null);
        whichPathPopupWindow = new PopupWindow(whichPathOwnView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        whichPathPopupWindow.setFocusable(true);
        whichPathPopupWindow.setOutsideTouchable(true);
        whichPathPopupWindow.update();
        whichPathPopupWindow.setBackgroundDrawable(new BitmapDrawable());


        initView();

        date = (TextView) findViewById(R.id.date);
        time = (TextView)findViewById(R.id.time);
        week = (TextView)findViewById(R.id.week);


        dateTimeUtil = DateTimeUtil.getInstance();
         new TimeThreadUtil(this).start();



    }
     @Override
 public void onGetDateTime() {
        time.setText(dateTimeUtil.getCurrentTime());//显示时间
         date.setText(dateTimeUtil.getCurrentDate());//显示年月日
         week.setText(dateTimeUtil.getCurrentWeekDay(0));//显示星期几
         }


    private void initView() {
//        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_sider);
//        final ImageView sider = findViewById(R.id.iv_anim_sider);
//        sider.setBackground(animationDrawable);
//        animationDrawable.start();

//        sideMenu = findViewById(R.id.layout_drawer);

        chengshi=(TextView)findViewById(R.id.chengshi);
        weathers=(TextView)findViewById(R.id.weathers);
        temp1=(TextView)findViewById(R.id.temp1);
        temp2=(TextView)findViewById(R.id.temp2);
        currentimage=(ImageView) findViewById(R.id.weather_icon02);

        String x = WeatherManager.GetNetIp();
        if (!(x == null)) {
            String city = WeatherManager.getCityByIp(x);
            address = WeatherManager.getAdd(city);
            SharedPreferencesUtils.setData(MainTianLai.this,
                    "weathercityname", address);
        }
        Bundle bundle = new Bundle();
        weathercityname =	SharedPreferencesUtils.getData(getApplicationContext(),"weathercityname");
        weathermin = SharedPreferencesUtils.getData(getApplicationContext(),"weathermin");
        weathermax = SharedPreferencesUtils.getData(getApplicationContext(),"weathermax");
        weather = SharedPreferencesUtils.getData(getApplicationContext(),"weather");

//        if(weathercityname ==null || weathercityname.equals("")) {
//            weathercityname = "北京";
//        }
//        if(weather ==null || weather.equals("")){
//            weather = "晴";
//          }
//        if(weathermin ==null || weathermin.equals("")){
//            weathermin = "20" ;
//        }if(weathermax ==null || weathermax.equals("")){
//            weathermax = "25" ;
//        }
        bundle.putString("cityname", weathercityname);
        bundle.putString("weathermin", weathermin);
        bundle.putString("weathermax", weathermax);
        bundle.putString("weather", weather);
        weathers.setText(bundle.getString("weather"));
        temp1.setText(bundle.getString("weathermin"));
        temp2.setText(bundle.getString("weathermax"));
        chengshi.setText(bundle.getString("cityname"));
        //  map = WeatherManager.getTianqi1(address);



        Constant.map.put(Constant.nodes[0],"多云");
        Constant.map.put(Constant.nodes[1],"10");
        Constant.map.put(Constant.nodes[2],"20");
        weathers.setText(Constant.map.get(Constant.nodes[0]));
        temp1.setText((Constant.map.get(Constant.nodes[1]) + "℃"));
        temp2.setText(Constant.map.get(Constant.nodes[2]) + "℃");
        getcurrentimage();


        btn_bottom_1=(Button)findViewById(R.id.btn_bottom_1);
        btn_bottom_2=(Button)findViewById(R.id.btn_bottom_2);
        btn_bottom_3=(Button)findViewById(R.id.btn_bottom_3);
        btn_bottom_4=(Button)findViewById(R.id.btn_bottom_4);
        btn_bottom_5=(Button)findViewById(R.id.btn_bottom_5);
        btn_bottom_6=(Button)findViewById(R.id.btn_bottom_6);

        btn_bottom_1.setOnClickListener(new MyOnClickListener());
        btn_bottom_2.setOnClickListener(new MyOnClickListener());
        btn_bottom_3.setOnClickListener(new MyOnClickListener());
        btn_bottom_4.setOnClickListener(new MyOnClickListener());
        btn_bottom_5.setOnClickListener(new MyOnClickListener());
        btn_bottom_6.setOnClickListener(new MyOnClickListener());


//        sideMenu.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(@NonNull View view, float v) {
//                sider.setAlpha(1-v);
//            }
//
//            @Override
//            public void onDrawerOpened(@NonNull View view) {
//
//            }
//
//            @Override
//            public void onDrawerClosed(@NonNull View view) {
//
//            }
//
//            @Override
//            public void onDrawerStateChanged(int i) {
//
//            }
//        });

    }
    public void getcurrentimage() {
        if (Constant.map.get(Constant.nodes[0]).equals("晴")) {
            currentimage
                    .setImageResource(R.drawable.weathericon_condition_01);
        } else if (Constant.map.get(Constant.nodes[0]).equals("多云")) {
            currentimage
                    .setImageResource(R.drawable.weathericon_condition_02);
        } else if (Constant.map.get(Constant.nodes[0]).equals("阴")) {
            currentimage
                    .setImageResource(R.drawable.weathericon_condition_04);
        } else if (Constant.map.get(Constant.nodes[0]).equals("雾")) {
            currentimage
                    .setImageResource(R.drawable.weathericon_condition_05);
        } else if (Constant.map.get(Constant.nodes[0]).equals("沙尘暴")) {
            currentimage
                    .setImageResource(R.drawable.weathericon_condition_06);
        } else if (Constant.map.get(Constant.nodes[0]).equals("阵雨")) {
            currentimage
                    .setBackgroundResource(R.drawable.weathericon_condition_07);
        } else if (Constant.map.get(Constant.nodes[0]).equals("小雨")
                || Constant.map.get(Constant.nodes[0]).equals("中雨")) {
            currentimage
                    .setBackgroundResource(R.drawable.weathericon_condition_08);
        } else if (Constant.map.get(Constant.nodes[0]).equals("大雨")) {
            currentimage
                    .setBackgroundResource(R.drawable.weathericon_condition_09);
        } else if (Constant.map.get(Constant.nodes[0]).equals("雷阵雨")) {
            currentimage
                    .setBackgroundResource(R.drawable.weathericon_condition_10);
        } else if (Constant.map.get(Constant.nodes[0]).equals("小雪")) {
            currentimage
                    .setBackgroundResource(R.drawable.weathericon_condition_11);
        } else if (Constant.map.get(Constant.nodes[0]).equals("大雪")) {
            currentimage
                    .setBackgroundResource(R.drawable.weathericon_condition_12);
        } else if (Constant.map.get(Constant.nodes[0]).equals("雨夹雪")) {
            currentimage
                    .setBackgroundResource(R.drawable.weathericon_condition_13);
        } else {
            currentimage
                    .setBackgroundResource(R.drawable.weathericon_condition_17);
        }
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

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SERIAL_ACTION_NAME);
        intentFilter.addAction(PRESS_SD_PLAY_BUTTON);
        intentFilter.addAction(PRESS_USB_PLAY_BUTTON);
        intentFilter.addAction(OPEN_AUX_BROADCAST);
        getApplication().registerReceiver(broadcastReceiver, intentFilter);

    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(OPEN_AUX_BROADCAST)) {
                Intent home_intent= getApplication().getPackageManager()
                        .getLaunchIntentForPackage("com.csw.csw_desktop");
                startActivity(home_intent);
                btn_bottom_5.callOnClick();
            }
        }
    };
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_bottom_1:
                    Music_Dialog();
                    break;

                case R.id.btn_bottom_5:
                    Intent audioIntent = panduan_apps_to_list_all(
                            "com.csw.setaudio");
                    if (audioIntent == null) {
                        Toast.makeText(getApplicationContext(), "音源切换未安装", 2000).show();
                    }else{
                        Intent mIntent=new Intent();
                        ComponentName comp = new ComponentName
                                ("com.csw.setaudio","com.csw.setaudio.SoundDevicesManager");
                        mIntent.setComponent(comp);
                        startActivity(mIntent);
                        Intent wifiMusicService = new Intent(getApplicationContext(), AuxLineService.class);
                        getApplicationContext().startService(wifiMusicService);
                    }
                    break;
                case R.id.btn_bottom_6:
                    Intent but_all_kuaijie = new Intent();
                    but_all_kuaijie.setClass(getApplicationContext(), AddKJappActivity.class);
                    but_all_kuaijie.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // app_add_all_init();
                    preferencesUtils.setsum(getApplicationContext(), "add_app_sum",
                            App_List.size());
                    MainTianLai.this.startActivityForResult(but_all_kuaijie, 2);
                    break;
            }
        }
    }
    private void Music_Dialog(){
        final Dialog dialog = new Dialog(MainTianLai.this,R.style.Dialog);
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
        mMusiclist = MusicList.getMusicData(getApplicationContext(), whichPathOwn);
        musicAdapter = new MusicAdapter(getApplicationContext(), mMusiclist);
        music_listview.setAdapter(musicAdapter);
        txt_path.setText("本地音乐列表");
        txt_music_sum.setText("歌曲数目"+String.valueOf(mMusiclist.size()));
        dialog.show();
//        music_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int arg2, long arg3) {
//                // TODO Auto-generated method stub
//                PlayMusic(arg2);
//                dialog.dismiss();
//
//            }
//        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != animationDrawable && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }

    }
}
