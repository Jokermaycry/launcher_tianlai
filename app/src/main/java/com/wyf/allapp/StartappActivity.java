package com.wyf.allapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zhongqin.tianlai.R;
import com.csw.tp.cc_server.SocketServer;
import com.example.apkfilejiazai.MainActivity;
import com.example.system.qidongActivity;
import com.wyf.app.AppInfo;
import com.wyf.util.FileReadWriteUtil;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;
import com.wyf.xiezai.xiezaiActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartappActivity extends Activity implements OnFocusChangeListener, OnClickListener {

	private ArrayList<View> pageViews;
	private ViewPager viewPager;
	private int sum = 0;
	private List<AppInfo> mlistAppInfo_quanbuyingyong = new ArrayList<AppInfo>();
	private TextView all_app_sum;
	private TextView all_app_pages_sum;
	
	RelativeLayout allappActivity;
	private SharedPreferencesUtils preferencesUtils;
	private   ImageManager imageManager = new ImageManager();
	private GetAppInfo getAppInfo = new GetAppInfo(this);
	
	private String bao;
	private String activityname;
	private String name ;
	public static int code = 10000;// 记录点击了第几个Item
	public static int code1 = 10001;// 是否按对话框标示
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
		setContentView(R.layout.all_app_item);
		/*
		 * 壁纸
		 */
		allappActivity=(RelativeLayout)this.findViewById(R.id.AllappActivityLayout);
		int wallpaperIdCurrent=preferencesUtils.getsum(StartappActivity.this, "wallpaperID");
		 if(wallpaperIdCurrent!=0){
		//	 allappActivity.setBackgroundResource(wallpaperIdCurrent);
			 allappActivity.setBackgroundDrawable(imageManager.getBitmapFromResources(this,wallpaperIdCurrent));
		 }else{
			 allappActivity.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.wallpaper_3));
		 }
		
		this.viewPager = (ViewPager) findViewById(R.id.app_item_viewPager);
		all_app_sum = (TextView) findViewById(R.id.all_app_sum);
		all_app_pages_sum = (TextView) findViewById(R.id.all_app_pages_sum);
		init_run();
		viewPager.setFocusable(true);
		viewPager.requestFocus();

	}
	private final static int BACK_SUM = 0;
	private void init_run() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			//	invidata();
				quanbuyingyong();
				handler.sendEmptyMessage(BACK_SUM);
			}
		}).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BACK_SUM:
				invidata();
				break;

			default:
				break;
			}

		}
	}; 
	
	@SuppressLint("NewApi")
	public void invidata() {
	 
		pageViews = new ArrayList<View>();
		int summer = mlistAppInfo_quanbuyingyong.size()%10;
		int count = mlistAppInfo_quanbuyingyong.size()/10;
		
		
		/*
		 * added by lgj 4.17
		 */
		DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int widthPixels=dm.widthPixels;
        int heightPixels=dm.heightPixels;
       
        int addItemSize;
        if(widthPixels>1060||heightPixels>600){
        	
        	addItemSize=192;
        }else{
        	
        	addItemSize=146;
        }
		
		
		
		for(int i=0;;i++){
			LinearLayout  layout_parment = new LinearLayout(StartappActivity.this);
			  layout_parment.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			  layout_parment.setOrientation(LinearLayout.VERTICAL);
			  layout_parment.setGravity(Gravity.CENTER);
			  if(sum == mlistAppInfo_quanbuyingyong.size()){
					break;
				}
		for(int j = i*3;;j++)
		{
			LinearLayout layout_child  = new LinearLayout(StartappActivity.this);
			layout_child.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, addItemSize));
//			layout_child.setLayoutParams(new LinearLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, 282));
			layout_child.setOrientation(LinearLayout.HORIZONTAL);
			layout_child.setGravity(Gravity.CENTER);
			/*View view_parent = LayoutInflater.from(this).inflate(
					R.layout.linelayout_app, null);*/
		for (int k = j*5; k < mlistAppInfo_quanbuyingyong.size(); k++) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.linelayout_item, null);
			LinearLayout layout_child2 = (LinearLayout) view.findViewById(R.id.l_all);
			//layout_child2.setId(k);
			ImageView imageView = (ImageView) view
					.findViewById(R.id.l_all_image);
			imageView.setBackground(mlistAppInfo_quanbuyingyong.get(k).getAppIcon());
			TextView textView = (TextView) view
					.findViewById(R.id.l_all_text);
			textView.setText(mlistAppInfo_quanbuyingyong.get(k).getAppLabel());
		    textView.setTextColor(getResources().getColor(R.color.white));
		    
		    TextView  textViewHide = (TextView) view.findViewById(R.id.l_all_text_hide);
			textViewHide.setText(mlistAppInfo_quanbuyingyong.get(k).getPkgName());
			
			view.setOnFocusChangeListener(this);
			view.setOnClickListener(this);
			view.setId(k);
			layout_child.addView(view);
			if(sum == mlistAppInfo_quanbuyingyong.size()){
			 	
				if(sum<(j*5)+5){
					for(int m = sum;m<(j*5)+5;m++){
						View view1 = LayoutInflater.from(this).inflate(
								R.layout.linelayout_item, null);
						view1.setId(m);
						RelativeLayout layout = (RelativeLayout) view1.findViewById(R.id.r_all);
						LinearLayout layout2 = (LinearLayout) view1.findViewById(R.id.l_all);
						//layout2.setId(m);
						layout.setFocusable(false);
						layout2.setBackground(getResources().getDrawable(R.drawable.all_app_item_image_taoming));
						layout2.setVisibility(View.GONE);
						view1.setId(m);
						layout_child.addView(view1);
	 				}
				}
				layout_parment.addView(layout_child);
				break;
			}
			sum++;
			if((j*5)+4==k){
				break;
			}
		}
		if(sum == mlistAppInfo_quanbuyingyong.size()){
			if(sum<(j*5)+5){
				for(int m = sum;m<(j*5)+5;m++){
					View view1 = LayoutInflater.from(this).inflate(
							R.layout.linelayout_item, null);
					
					RelativeLayout layout = (RelativeLayout) view1.findViewById(R.id.r_all);
					LinearLayout layout2 = (LinearLayout) view1.findViewById(R.id.l_all);
					//layout2.setId(m);
					layout.setFocusable(false);
					layout2.setBackground(getResources().getDrawable(R.drawable.all_app_item_image_taoming));
					layout2.setVisibility(View.GONE);
					view1.setId(m);
					layout_child.addView(view1);
 				}
			}
			layout_parment.addView(layout_child);
			pageViews.add(layout_parment);
			break;
		}
		layout_parment.addView(layout_child);
		if((j+1)%3 == 0){
			pageViews.add(layout_parment);
			 break;
		}
		}
		}
		  //all_app_sum.setText("全部应用"+ sum +"个");
		all_app_sum.setText("添加启动应用");
		  int sum = mlistAppInfo_quanbuyingyong.size()/15;
		  if(mlistAppInfo_quanbuyingyong.size()%15!=0)
		  {
			  sum ++ ;
		  }
		  all_app_pages_sum.setText( 1+"/"+sum+"页");
		viewPager.setAdapter(new myPagerView());
	 	viewPager.clearAnimation();
	    
		  this.viewPager.setOnPageChangeListener(new OnPageChangeListener() {
		  
		  @Override public void onPageSelected(int position) { 
			  // TODOAuto-generated method stub 
			  int sum = mlistAppInfo_quanbuyingyong.size()/15;
			  if(mlistAppInfo_quanbuyingyong.size()%15!=0)
			  {
				  sum ++ ;
			  }
			  all_app_pages_sum.setText(position+1+"/"+sum+"页");
		  
		  }
		  
		  @Override public void onPageScrolled(int arg0, float arg1, int arg2)
		  { // TODO Auto-generated method stub
		  
		  }
		  
		  @Override public void onPageScrollStateChanged(int position) {
			  //TODO Auto-generated method stub
		  
		  } });
			}
	AnimationSet animationSet = new AnimationSet(true);
	private void scaleButton(View v) {
		AnimationSet animationSet = new AnimationSet(true);
		// ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f,
		// 1.0f, AnimationSet.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0.5f);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f,
				1.06f, AnimationSet.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		animationSet.addAnimation(scaleAnimation);
		// animationSet.setStartOffset(1000);
		animationSet.setFillAfter(true);
		animationSet.setFillBefore(false);
		animationSet.setDuration(100);
		v.startAnimation(animationSet);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	 	LinearLayout layout = (LinearLayout) v.findViewById(R.id.l_all);
	 	int ii = v.getId();
	//	RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.r_all);
		if (hasFocus) {
			scaleButton(v);
			//v.bringToFront();
	   	  	layout.setBackground(getResources().getDrawable(R.drawable.all_app_item_image01));
		} else {
			
		 	v.clearAnimation();
		//	v.invalidate();
	  	  	layout.setBackground(getResources().getDrawable(R.drawable.all_app_item_image));
		
	  	 
		}
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//code1 = arg2;
		//code = arg2;
		TextView  textView = (TextView) v.findViewById(R.id.l_all_text);
		String value = textView.getText().toString();
		
		TextView  textViewHide = (TextView) v.findViewById(R.id.l_all_text_hide);
		String packageName=textViewHide.getText().toString();
			
		if(value.equals("")||value==null){
			return ;
		}
	    if (!lableName_compare_bao(value,packageName)) {
			return;
		}
		AlertDialog.Builder builder = new Builder(StartappActivity.this);
		builder.setTitle("提示");
		builder.setMessage("是否把当前应用设为开机启动");
		// 更新
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						code1 = 100;
						SharedPreferencesUtils.setData(
								StartappActivity.this, "bao", bao);
						SharedPreferencesUtils.setData(
								StartappActivity.this, "activityName",
								activityname);
						SharedPreferencesUtils.setData(StartappActivity.this,
								"SetAppStart", name);
						String jsonResult ="setappstart:"+name+":setappstart";
						SocketServer.SendDataClient(jsonResult);
						System.out.println("执行保存开机apk信息操作");
					/*	String internal_sdPath=FileReadWriteUtil.getSDPath(StartappActivity.this);//sd卡路径
						String fileDirName="baomingleiming";//文件夹名称
						String fileName="baomingleiming.txt";//文件名称
						boolean weatherHasFile=FileReadWriteUtil.isFileExist(internal_sdPath, fileDirName);
						if(weatherHasFile==false){
							System.out.println("文件夹不存在");
							FileReadWriteUtil.creatSDDir(internal_sdPath, fileDirName);
							//向文件中写数据
						}else{
							System.out.println("文件夹存在");
							if(FileReadWriteUtil.isFileExist(internal_sdPath+"/"+fileDirName,fileName)){
								System.out.println("如果文件存在，删除");
							    boolean deleteFlag=	new File(internal_sdPath+"/"+fileDirName+"/"+fileName).delete();
								System.out.println("删除"+deleteFlag); 
							}	
						}
					    File finalFileName=	FileReadWriteUtil.creatSDFile(internal_sdPath+"/"+fileDirName,fileName);
						String packageAndActivityName=bao+","+activityname;
						byte [] content=packageAndActivityName.getBytes();
						try {
							FileReadWriteUtil.writeFile(content, finalFileName.getPath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("开机要启动的apk信息保存失败");
							e.printStackTrace();
						}
						System.out.println("开机要启动的apk信息保存成功");*/

						// SharedPreferencesUtils.setsum(qidongActivity.this,"code",code);
						Intent resultValue = new Intent();
						resultValue.putExtra("code", code1);
						setResult(RESULT_OK, resultValue);
						dialog.dismiss();
						StartappActivity.this.finish();
					}
				});
		// 稍后更新
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						dialog.dismiss();
					}
				});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
		
		
		
		
		
		
		
	}
	class myPagerView extends PagerAdapter {
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent but_all_kuaijie = new Intent();
			setResult(4, but_all_kuaijie);
			//结束当前的Activity
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}  
	
	
	private void quanbuyingyong(){
		for(int i = 0;i < GetAppInfo.list.size(); i++){
			if (GetAppInfo.list.get(i).getAppLabel().equals("录音机")||GetAppInfo.list.get(i).getAppLabel().equals("一键清理")) {
				continue;
			}
			mlistAppInfo_quanbuyingyong.add(GetAppInfo.list.get(i));
		}
	}
	/*
	 * 根据apk名称进行比较
	 */
	public Intent lableName_compare_intent(String lable){
		for(int i = 0;i < GetAppInfo.list.size(); i++){
		if(	GetAppInfo.list.get(i).getAppLabel().equals(lable) ){
			return GetAppInfo.list.get(i).getIntent();
		}
		}
		return null;
	}
	
	/**/
	public boolean lableName_compare_bao(String lable,String packageName){
		for(int i = 0;i < GetAppInfo.list.size(); i++){
		if(	GetAppInfo.list.get(i).getAppLabel().equals(lable)&&GetAppInfo.list.get(i).getPkgName().equals(packageName) ){
			code = i;
			bao = GetAppInfo.list.get(i).getPkgName();
			activityname = GetAppInfo.list.get(i).getActivityName();
			name= GetAppInfo.list.get(i).getAppLabel();
			return true;
		}
		}
		return false;
	}
	
	
	
}
