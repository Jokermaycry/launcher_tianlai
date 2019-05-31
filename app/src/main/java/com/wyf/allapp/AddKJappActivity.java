package com.wyf.allapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.csw.csw_desktop_yahao.MainActivity;
import com.zhongqin.tianlai.R;
import com.wyf.allapp.ALLappActivity.myPagerView;
import com.wyf.app.AppInfo;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddKJappActivity extends Activity implements OnFocusChangeListener, OnClickListener {

	
	private ArrayList<View> pageViews;
	private ViewPager viewPager;
	private int sum = 0;
	private TextView all_app_sum;
	private TextView all_app_pages_sum;
	private TextView all_app_explain;
	private String str_app = null;
	private SharedPreferencesUtils preferencesUtils ;
	private int summer;
	RelativeLayout addKAppActivityLayout;
	private   ImageManager imageManager = new ImageManager();
	private GetAppInfo getAppInfo = new GetAppInfo(this);
	
	private List<AppInfo> mlistAppInfo_quanbuyingyong = new ArrayList<AppInfo>();
	
	private static String packageFlag="lgjistc";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
		setContentView(R.layout.all_app_item);
		/*
		 * 壁纸
		 */
		addKAppActivityLayout=(RelativeLayout)this.findViewById(R.id.AllappActivityLayout);
		
		int wallpaperIdCurrent=preferencesUtils.getsum(AddKJappActivity.this, "wallpaperID");
		 if(wallpaperIdCurrent!=0){
		//	 addKAppActivityLayout.setBackgroundResource(wallpaperIdCurrent);
			 addKAppActivityLayout.setBackgroundDrawable(imageManager.getBitmapFromResources(this,wallpaperIdCurrent));
		 }else{
			 addKAppActivityLayout.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.wallpaper_3));
		 }
		 
		
		this.viewPager = (ViewPager) findViewById(R.id.app_item_viewPager);
		all_app_sum = (TextView) findViewById(R.id.all_app_sum);
		all_app_pages_sum = (TextView) findViewById(R.id.all_app_pages_sum);
		all_app_explain = (TextView) findViewById(R.id.all_app_explain);
		all_app_explain.setVisibility(View.VISIBLE);
	 
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
				quanbuyingyong();
				inti();
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
   public void inti(){
	   str_app = "";
	  int sum  =  preferencesUtils.getsum(this, "add_app_sum");
	  for(int i = 1;i<=7 ;i++){
		  String str = preferencesUtils.getData(this, "add_app"+i);
		  AppInfo appinfo = pkgName_compare(str);
			if(appinfo == null ){
			  continue;
		  }
		  str_app += " "+str+packageFlag;
	  }
	  for(int i = 1;i<= 7 ;i++){
			 String str = preferencesUtils.getData(this, "add_app"+i);
			 if(str==null){
				 continue;
			 }
			 if(str_app.indexOf(str)==-1)
			 {
				 preferencesUtils.setData(this, "add_app"+i, null);
			 }
		 }
   }
	 
	@SuppressLint("NewApi")
	public void invidata() {
	 
		pageViews = new ArrayList<View>();
		int summer = mlistAppInfo_quanbuyingyong.size()%10;
		int count = mlistAppInfo_quanbuyingyong.size()/10;
		
		
		/*
		 * added by lgj 4.14
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
			LinearLayout  layout_parment = new LinearLayout(this);
			 layout_parment = new LinearLayout(this);
			  layout_parment.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			  layout_parment.setOrientation(LinearLayout.VERTICAL);
			  layout_parment.setGravity(Gravity.CENTER);
			  if(sum == mlistAppInfo_quanbuyingyong.size()){
					break;
				}
		for(int j = i*3;;j++)
		{
			LinearLayout layout_child  = new LinearLayout(this);
			layout_child = new LinearLayout(this);
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
			ImageView imageView = (ImageView) view
					.findViewById(R.id.l_all_image);
			imageView.setBackground(mlistAppInfo_quanbuyingyong.get(k).getAppIcon());
			TextView textView = (TextView) view
					.findViewById(R.id.l_all_text);
			textView.setText(mlistAppInfo_quanbuyingyong.get(k).getAppLabel());
			textView.setTextColor(getResources().getColor(R.color.white));
			
			 TextView textViewHide = (TextView) view
						.findViewById(R.id.l_all_text_hide);
			    textViewHide.setText(mlistAppInfo_quanbuyingyong.get(k).getPkgName());
			
			LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.l_all);
			if(str_app!=null)
			if(str_app.indexOf(mlistAppInfo_quanbuyingyong.get(k).getPkgName()+packageFlag)!=-1)
			{
				layout2.setBackground(getResources().getDrawable(R.drawable.all_app_item_image_selector));
			}
			view.setOnFocusChangeListener(this);
			view.setOnClickListener(this);
			layout_child.addView(view);
			if(sum == mlistAppInfo_quanbuyingyong.size()){
			 	
				if(sum<(j*5)+5){
					for(int m = sum;m<(j*5)+5;m++){
						View view1 = LayoutInflater.from(this).inflate(
								R.layout.linelayout_item, null);
						 
						RelativeLayout layout = (RelativeLayout) view1.findViewById(R.id.r_all);
						LinearLayout layout3 = (LinearLayout) view1.findViewById(R.id.l_all);
						layout.setFocusable(false);
						layout3.setBackground(getResources().getDrawable(R.drawable.all_app_item_image_taoming));
						layout3.setVisibility(View.GONE);
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
					layout.setFocusable(false);
					layout2.setBackground(getResources().getDrawable(R.drawable.all_app_item_image_taoming));
					layout2.setVisibility(View.GONE);
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
		all_app_sum.setText("全部应用"+ sum +"个");
		  summer = mlistAppInfo_quanbuyingyong.size()/15;
		  if(mlistAppInfo_quanbuyingyong.size()%15!=0)
		  {
			  summer ++ ;
		  }
		  all_app_pages_sum.setText(1+"/"+summer+"页");
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
		TextView textView = (TextView) v.findViewById(R.id.l_all_text);
		String value = textView.getText().toString();
		
		 TextView textViewHide = (TextView) v
					.findViewById(R.id.l_all_text_hide);
		String packageName=textViewHide.getText().toString();
		
		AppInfo appinfo = check_apkLable(value,packageName);
		if (hasFocus) {
			scaleButton(v);
			if (!str_app.equals("")) {
			
				if (str_app.indexOf(appinfo.getPkgName()) != -1) {
					layout.setBackground(getResources().getDrawable(
							R.drawable.all_app_item_image_lv01));
				} else {
					layout.setBackground(getResources().getDrawable(
							R.drawable.all_app_item_image01));
				}
			} else {
				layout.setBackground(getResources().getDrawable(
						R.drawable.all_app_item_image01));
			}
		} else {
			v.clearAnimation();
			v.invalidate();
			if (!str_app.equals("")) {
				if (str_app.indexOf(appinfo.getPkgName()) != -1) {
					layout.setBackground(getResources().getDrawable(
							R.drawable.all_app_item_image_lv));
				} else {
					layout.setBackground(getResources().getDrawable(
							R.drawable.all_app_item_image));
				}
			} else {
				layout.setBackground(getResources().getDrawable(
						R.drawable.all_app_item_image));
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.l_all);
		TextView  textView = (TextView) v.findViewById(R.id.l_all_text);
		String value = textView.getText().toString();
		 TextView textViewHide = (TextView) v
					.findViewById(R.id.l_all_text_hide);
		String packageName=textViewHide.getText().toString();
		
		if(value.equals("")||value==null){
			return ;
		}
		AppInfo appinfo = check_apkLable(value,packageName);
		if(appinfo == null ){
			Toast.makeText(this, "找不到该应用", Toast.LENGTH_SHORT).show();
			return ;
		}
	//	inti();
		if(str_app!=null)
		if(str_app.indexOf(appinfo.getPkgName()+packageFlag)!=-1){
			for(int i = 1;i<= 7 ;i++){
				 String str = preferencesUtils.getData(this, "add_app"+i);
				 if(str==null){
					 continue;
				 }
				 if(appinfo.getPkgName().indexOf(str)!=-1)
				 {
					 preferencesUtils.setData(this, "add_app"+i, null);
					 layout.setBackground(getResources().getDrawable(R.drawable.all_app_item_image01));
					 int sum =  preferencesUtils.getsum(this, "add_app_sum");
					 SharedPreferencesUtils.setsum(this, "add_app_sum", sum-1);
					 v.clearAnimation();
						v.invalidate();
						scaleButton(v);
						int len_index = str_app.indexOf(appinfo.getPkgName()+packageFlag);
						if(len_index != -1)
						{
							int len = (appinfo.getPkgName()+packageFlag).length();
							String str_app_dianbo1 = str_app.substring(0, len_index);
							String str_app_dianbo2 = str_app.substring(len_index+len, str_app.length());
							str_app = str_app_dianbo1 + str_app_dianbo2;
							
						}
					 return ;
				 }
			 }
			Toast.makeText(this, "该应用已添加过", Toast.LENGTH_SHORT).show();
			return ;
		}
		if(appinfo.getPkgName().equals("")||appinfo.getPkgName()!=null){
		 int add_sum =  preferencesUtils.getsum(this, "add_app_sum");
		 if(add_sum == 0){
			 preferencesUtils.setsum(this, "add_app_sum", 1);
			 int sum =  preferencesUtils.getsum(this, "add_app_sum");
			 preferencesUtils.setData(this, "add_app"+sum, appinfo.getPkgName());
			 layout.setBackground(getResources().getDrawable(R.drawable.all_app_item_image_lv01));
			 str_app += " "+appinfo.getPkgName()+packageFlag;
		 }else if(add_sum >= 7){
			 Toast.makeText(this, "应用最多只能添加7个", Toast.LENGTH_SHORT).show();
			 return ;
		 }else{
			
			 SharedPreferencesUtils.setsum(this, "add_app_sum", add_sum+1);
			 int sum =  preferencesUtils.getsum(this, "add_app_sum");
			 layout.setBackground(getResources().getDrawable(R.drawable.all_app_item_image_lv01));
			 str_app += " "+appinfo.getPkgName()+packageFlag;
			 if(preferencesUtils.getData(this, "add_app1")==null){
				 preferencesUtils.setData(this, "add_app1", appinfo.getPkgName());
				 return ;
			 }
			 if(preferencesUtils.getData(this, "add_app2")==null){
				 preferencesUtils.setData(this, "add_app2", appinfo.getPkgName());
				 return ;
			 }
			 if(preferencesUtils.getData(this, "add_app3")==null){
				 preferencesUtils.setData(this, "add_app3", appinfo.getPkgName());
				 return ;
			 }
			 if(preferencesUtils.getData(this, "add_app4")==null){
				 preferencesUtils.setData(this, "add_app4", appinfo.getPkgName());
				 return ;
			 }
			 if(preferencesUtils.getData(this, "add_app5")==null){
				 preferencesUtils.setData(this, "add_app5", appinfo.getPkgName());
				 return ;
			 } 
			 if(preferencesUtils.getData(this, "add_app6")==null){
				 preferencesUtils.setData(this, "add_app6", appinfo.getPkgName());
				 return ;
			 } 
			 if(preferencesUtils.getData(this, "add_app7")==null){
				 preferencesUtils.setData(this, "add_app7", appinfo.getPkgName());
				 return ;
			 }
			/* if(preferencesUtils.getData(this, "add_app8")==null){
				 preferencesUtils.setData(this, "add_app8", appinfo.getPkgName());
				 return ;
			 }*/
			 
		 }
		}
		System.out.println(textView.getText()+"------------");
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
	 /*
	  * 比较APK是否能找到
	  */
		public AppInfo check_apkLable(String value,String packageName){
			Log.i(TAG, "mlistAppInfo_quanbuyingyong==="+mlistAppInfo_quanbuyingyong.size());
			for(int i = 0;i<mlistAppInfo_quanbuyingyong.size();i++){
				if(value.equals(mlistAppInfo_quanbuyingyong.get(i).getAppLabel())&&packageName.equals(mlistAppInfo_quanbuyingyong.get(i).getPkgName())){
					return mlistAppInfo_quanbuyingyong.get(i);
				}
			}
			return null;
		}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent but_all_kuaijie = new Intent();
			setResult(2, but_all_kuaijie);
			//结束当前的Activity
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}  

	private void quanbuyingyong(){
		if(mlistAppInfo_quanbuyingyong!=null&&mlistAppInfo_quanbuyingyong.size()>0){
			mlistAppInfo_quanbuyingyong.clear();
			mlistAppInfo_quanbuyingyong=null;
			mlistAppInfo_quanbuyingyong = new ArrayList<AppInfo>();
		}
		Log.i(TAG, "en  mlistAppInfo_quanbuyingyong.size=="+mlistAppInfo_quanbuyingyong.size());
		Log.i(TAG, "GetAppInfo.list.size()=="+GetAppInfo.list.size());
		for(int i = 0;i < GetAppInfo.list.size(); i++){
			mlistAppInfo_quanbuyingyong.add(GetAppInfo.list.get(i));
		}
		Log.i(TAG, "mlistAppInfo_quanbuyingyong.size=="+mlistAppInfo_quanbuyingyong.size());
	}
	private static String TAG="AddKJappActivity";
	
	/*
	 * 根据包名进行比较
	 */
	public AppInfo pkgName_compare(String pkgname){
		Log.i(TAG, "==mlistAppInfo_quanbuyingyong=="+mlistAppInfo_quanbuyingyong.size());
		for(int i = 0;i < mlistAppInfo_quanbuyingyong.size(); i++){
		if(	mlistAppInfo_quanbuyingyong.get(i).getPkgName().equals(pkgname) ){
			return mlistAppInfo_quanbuyingyong.get(i);
		}
		}
		return null;
	}
	/*
	 * 根据apk名称进行比较
	 */
	public AppInfo lableName_compare(String lable){
		for(int i = 0;i < mlistAppInfo_quanbuyingyong.size(); i++){
		if(	mlistAppInfo_quanbuyingyong.get(i).getAppLabel().equals(lable) ){
			return mlistAppInfo_quanbuyingyong.get(i);
		}
		}
		return null;
	}
}
