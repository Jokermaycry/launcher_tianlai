package com.example.system;

import com.zhongqin.tianlai.R;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WallpaperActivity extends Activity {

	FrameLayout firstWallpaperLayout, secondWallpaperLayout,
			thirdWallpaperLayout, fourthWallpaperLayout, fifthWallpaperLayout,
			sixedWallpaperLayout;
	ImageView firstWallpaper, secondWallpaper, ThirdWallpaper, FourthWallpaper,
			fifthWallpaper, sixedWallpaper;
	ImageView firstWallpaper0, secondWallpaper0, ThirdWallpaper0, FourthWallpaper0,
	fifthWallpaper0, sixedWallpaper0;

	/*
	 * 各个桌面布局
	 */
	RelativeLayout  wallpaperLayout;

	/*LayoutInflater layoutInflater;
	LayoutInflater secondLayoutInflater;
	View mainActivityLayoutView;
	View xiezaiActivitylayoutView;
	View HotpotActivityLayoutView;
	View zhiboActivitylayoutView;
	View apkFileLayoutView;
	View AllappActivityLayoutView;
	View YouxiAppActivitylayoutView;
	View ZhiboAppActivitylayoutView;
	View apk_actionLayoutView;*/

	private static int wallpaperId = R.drawable.bg2;

	private SharedPreferencesUtils preferencesUtils;
	private static ImageManager imageManager = new ImageManager();

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.wallpaper);
        
		
		initLayout();
		initWallpaper();
	/*	try {
			initLayout();
			initWallpaper();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println("出现了蛋疼的异常");
		}*/
		
		
		 int wallpaperIdCurrent=preferencesUtils.getsum(WallpaperActivity.this, "wallpaperID");
		 if(wallpaperIdCurrent!=0){
			 //wallpaperLayout.setBackgroundResource(wallpaperIdCurrent);
			 wallpaperLayout.setBackgroundDrawable(imageManager.getBitmapFromResources(this, wallpaperIdCurrent));
			 if(wallpaperIdCurrent==R.drawable.simplify_bg){
				 firstWallpaper.setVisibility(View.VISIBLE);
			 }else if(wallpaperIdCurrent==R.drawable.wallpaper_1){
				 secondWallpaper.setVisibility(View.VISIBLE);
			 }else if(wallpaperIdCurrent== R.drawable.wallpaper_3){
				 ThirdWallpaper.setVisibility(View.VISIBLE);
			 }else if(wallpaperIdCurrent==R.drawable.wallpaper_4){
				 FourthWallpaper.setVisibility(View.VISIBLE);
			 }else if(wallpaperIdCurrent==R.drawable.bg2){
				 fifthWallpaper.setVisibility(View.VISIBLE);
			 }else if(wallpaperIdCurrent==R.drawable.wallpaper_6){
				 sixedWallpaper.setVisibility(View.VISIBLE);
			 }
		 }else{
			 wallpaperLayout.setBackgroundDrawable(imageManager.getBitmapFromResources(this, R.drawable.wallpaper_3));
		 }
		
	}

	/*
	 * 初始化壁纸
	 */
	private void initWallpaper() {

		firstWallpaperLayout = (FrameLayout) this
				.findViewById(R.id.fifthWallpaperLayout);
		secondWallpaperLayout = (FrameLayout) this
				.findViewById(R.id.secondWallpaperLayout);
		thirdWallpaperLayout = (FrameLayout) this
				.findViewById(R.id.thirdWallpaperLayout);
		fourthWallpaperLayout = (FrameLayout) this
				.findViewById(R.id.fourthWallpaperLayout);
		fifthWallpaperLayout = (FrameLayout) this
				.findViewById(R.id.firstWallpaperLayout);
		sixedWallpaperLayout = (FrameLayout) this
				.findViewById(R.id.sixedWallpaperLayout);

		firstWallpaperLayout.setOnClickListener(new WallpaperClickListener());
		secondWallpaperLayout.setOnClickListener(new WallpaperClickListener());
		thirdWallpaperLayout.setOnClickListener(new WallpaperClickListener());
		fourthWallpaperLayout.setOnClickListener(new WallpaperClickListener());
		fifthWallpaperLayout.setOnClickListener(new WallpaperClickListener());
		sixedWallpaperLayout.setOnClickListener(new WallpaperClickListener());

		firstWallpaper = (ImageView) this.findViewById(R.id.firstWallpaper);
		secondWallpaper = (ImageView) this.findViewById(R.id.secondWallpaper);
		ThirdWallpaper = (ImageView) this.findViewById(R.id.thirdWallpaper);
		FourthWallpaper = (ImageView) this.findViewById(R.id.fourthWallpaper);
		fifthWallpaper = (ImageView) this.findViewById(R.id.fifthWallpaper);
		sixedWallpaper = (ImageView) this.findViewById(R.id.sixedWallpaper);
		
		firstWallpaper0 = (ImageView) this.findViewById(R.id.fifthWallpaper_0);
		secondWallpaper0 = (ImageView) this.findViewById(R.id.secondWallpaper_0);
		ThirdWallpaper0 = (ImageView) this.findViewById(R.id.thirdWallpaper_0);
		FourthWallpaper0 = (ImageView) this.findViewById(R.id.fourthWallpaper_0);
		fifthWallpaper0 = (ImageView) this.findViewById(R.id.firstWallpaper_0);
		sixedWallpaper0 = (ImageView) this.findViewById(R.id.sixedWallpaper_0);
		
		firstWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.bg2_00));
		secondWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.wallpaper_1_00));
		ThirdWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.wallpaper_3_00));
		FourthWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.wallpaper_4_00));
		fifthWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.simplify_bg_00));
		sixedWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.wallpaper_6_00));
		//image_run();
	/*	new AsyncTask(){

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			protected void onPostExecute(Object result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				firstWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.bg2));
				secondWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.wallpaper_1));
				ThirdWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.wallpaper_3));
				FourthWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.wallpaper_4));
				fifthWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.simplify_bg));
				sixedWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.wallpaper_6));
			}
		}.execute();*/
	}
/*	private void image_run(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			//	image_load();
				handler.sendEmptyMessage(0);
			}
		}).start();
	}
	Handler handler = new Handler(){
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				firstWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.bg2));
				secondWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.wallpaper_1));
				ThirdWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.wallpaper_3));
				FourthWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.wallpaper_4));
				fifthWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.simplify_bg));
				sixedWallpaper0.setBackgroundDrawable(imageManager.getBitmapFromResources(WallpaperActivity.this,R.drawable.wallpaper_6));
				break;

			default:
				break;
			}
		}
	};*/
/*	public   class ImageAsyncTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

	}*/
	private void initLayout() {
	/*	layoutInflater = LayoutInflater.from(WallpaperActivity.this);
		secondLayoutInflater=LayoutInflater.from(WallpaperActivity.this);
		 

		xiezaiActivitylayoutView = layoutInflater.inflate(R.layout.xiezai_main,
				null);
		HotpotActivityLayoutView = layoutInflater.inflate(R.layout.redian_main,
				null);
		zhiboActivitylayoutView = layoutInflater.inflate(R.layout.browse_app_list, null);

		apkFileLayoutView = layoutInflater.inflate(R.layout.apk_main, null);
		AllappActivityLayoutView = layoutInflater.inflate(R.layout.all_app_item, null);
*/
		 
		/*xiezaiActivitylayout = (RelativeLayout) xiezaiActivitylayoutView
				.findViewById(R.id.xiezaiActivitylayout);
		HotpotActivityLayout = (RelativeLayout) HotpotActivityLayoutView
				.findViewById(R.id.HotpotActivityLayout);
		zhiboActivitylayout = (RelativeLayout) zhiboActivitylayoutView
				.findViewById(R.id.zhiboActivitylayout);
		apkFileLayout = (RelativeLayout) apkFileLayoutView
				.findViewById(R.id.apkFileLayout);
		AllappActivityLayout = (RelativeLayout) AllappActivityLayoutView
				.findViewById(R.id.AllappActivityLayout);
		 */
		wallpaperLayout = (RelativeLayout) this
				.findViewById(R.id.wallpaperLayout);
	}

	/*
	 * 壁纸选中
	 */
	private class WallpaperClickListener implements OnClickListener {

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (view.getId() == R.id.firstWallpaperLayout) {
				firstWallpaper.setVisibility(View.VISIBLE);
				secondWallpaper.setVisibility(View.GONE);
				ThirdWallpaper.setVisibility(View.GONE);
				FourthWallpaper.setVisibility(View.GONE);
				fifthWallpaper.setVisibility(View.GONE);
				sixedWallpaper.setVisibility(View.GONE);
				wallpaperId = R.drawable.simplify_bg;
				preferencesUtils.setsum(WallpaperActivity.this, "wallpaperID",
						wallpaperId);// 存起来
				setWallpaper(wallpaperId);
			} else if (view.getId() == R.id.secondWallpaperLayout) {

				firstWallpaper.setVisibility(View.GONE);
				secondWallpaper.setVisibility(View.VISIBLE);
				ThirdWallpaper.setVisibility(View.GONE);
				FourthWallpaper.setVisibility(View.GONE);
				fifthWallpaper.setVisibility(View.GONE);
				sixedWallpaper.setVisibility(View.GONE);
				wallpaperId = R.drawable.wallpaper_1;
				preferencesUtils.setsum(WallpaperActivity.this, "wallpaperID",
						wallpaperId);// 存起来
				setWallpaper(wallpaperId);
			} else if (view.getId() == R.id.thirdWallpaperLayout) {
				firstWallpaper.setVisibility(View.GONE);
				secondWallpaper.setVisibility(View.GONE);
				ThirdWallpaper.setVisibility(View.VISIBLE);
				FourthWallpaper.setVisibility(View.GONE);
				fifthWallpaper.setVisibility(View.GONE);
				sixedWallpaper.setVisibility(View.GONE);
				wallpaperId = R.drawable.wallpaper_3;
				preferencesUtils.setsum(WallpaperActivity.this, "wallpaperID",
						wallpaperId);// 存起来
				setWallpaper(wallpaperId);
			} else if (view.getId() == R.id.fourthWallpaperLayout) {
				firstWallpaper.setVisibility(View.GONE);
				secondWallpaper.setVisibility(View.GONE);
				ThirdWallpaper.setVisibility(View.GONE);
				FourthWallpaper.setVisibility(View.VISIBLE);
				fifthWallpaper.setVisibility(View.GONE);
				sixedWallpaper.setVisibility(View.GONE);
				wallpaperId = R.drawable.wallpaper_4;
				preferencesUtils.setsum(WallpaperActivity.this, "wallpaperID",
						wallpaperId);// 存起来
				setWallpaper(wallpaperId);
			} else if (view.getId() == R.id.fifthWallpaperLayout) {
				firstWallpaper.setVisibility(View.GONE);
				secondWallpaper.setVisibility(View.GONE);
				ThirdWallpaper.setVisibility(View.GONE);
				FourthWallpaper.setVisibility(View.GONE);
				fifthWallpaper.setVisibility(View.VISIBLE);
				sixedWallpaper.setVisibility(View.GONE);
				wallpaperId = R.drawable.bg2;
				preferencesUtils.setsum(WallpaperActivity.this, "wallpaperID",
						wallpaperId);// 存起来
				setWallpaper(wallpaperId);
			} else if (view.getId() == R.id.sixedWallpaperLayout) {
				firstWallpaper.setVisibility(View.GONE);
				secondWallpaper.setVisibility(View.GONE);
				ThirdWallpaper.setVisibility(View.GONE);
				FourthWallpaper.setVisibility(View.GONE);
				fifthWallpaper.setVisibility(View.GONE);
				sixedWallpaper.setVisibility(View.VISIBLE);
				wallpaperId = R.drawable.wallpaper_6;
				preferencesUtils.setsum(WallpaperActivity.this, "wallpaperID",
						wallpaperId);// 存起来
				setWallpaper(wallpaperId);
			}
		}

	}

	/*
	 * 设置壁纸
	 */
	private void setWallpaper(int wallpaperId) {
//		mainActivityLayout.setBackgroundResource(wallpaperId);
//		xiezaiActivitylayout.setBackgroundResource(wallpaperId);
//		HotpotActivityLayout.setBackgroundResource(wallpaperId);
//		zhiboActivitylayout.setBackgroundResource(wallpaperId);
//		apkFileLayout.setBackgroundResource(wallpaperId);
//		AllappActivityLayout.setBackgroundResource(wallpaperId);
//		YouxiAppActivitylayout.setBackgroundResource(wallpaperId);
//		ZhiboAppActivitylayout.setBackgroundResource(wallpaperId);
//		apk_actionLayout.setBackgroundResource(wallpaperId);
	//	wallpaperLayout.setBackgroundResource(wallpaperId);
		wallpaperLayout.setBackgroundDrawable(imageManager.getBitmapFromResources(this,wallpaperId));
	}
}
