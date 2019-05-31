package com.loaddown.test;



import com.zhongqin.tianlai.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SpeedActivity extends Activity {
	TextView fileLength = null;
	TextView speed = null;
	TextView hasDown = null;
	TextView percent = null;
	String url = "";

	// ImageView imageView = null;
	byte[] imageData = null;

	NetWorkSpeedInfo netWorkSpeedInfo = null;
	private final int UPDATE_SPEED = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.wangsu_test);
		hasDown = (TextView) findViewById(R.id.hasDown);
		fileLength = (TextView) findViewById(R.id.fileLength);
		speed = (TextView) findViewById(R.id.speed);
		percent = (TextView) findViewById(R.id.percent);
		// imageView = ( ImageView ) findViewById( R.id.ImageView01 );
		url = getString(R.string.image_url);
		netWorkSpeedInfo = new NetWorkSpeedInfo();
				new Thread() {
					@Override
					public void run() {
						imageData = ReadFile.getFileFromUrl(url,
								netWorkSpeedInfo);
						// stop();
					}
				}.start();

				// get the speed , down load bytes ,update the view thread
				new Thread() {
					@Override
					public void run() {

						while (netWorkSpeedInfo.hadFinishedBytes < netWorkSpeedInfo.totalBytes) {
							netWorkSpeedInfo.downloadPercent = (int) (((double) netWorkSpeedInfo.hadFinishedBytes / (double) netWorkSpeedInfo.totalBytes) * 100);
							try {
								sleep(1500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							Log.e("update,send the message to update", "");
							// update view
							handler.sendEmptyMessage(UPDATE_SPEED);
						}

						// finished
						if (netWorkSpeedInfo.hadFinishedBytes == netWorkSpeedInfo.totalBytes) {

							netWorkSpeedInfo.downloadPercent = (int) (((double) netWorkSpeedInfo.hadFinishedBytes / (double) netWorkSpeedInfo.totalBytes) * 100);
							handler.sendEmptyMessage(UPDATE_SPEED);
							Log.e("update",
									",send the message to update and stop");
							// stop();
						}

					}
				}.start();
	}

	/**
	 * Handler for post message into OS
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int value = msg.what;
			switch (value) {
			case UPDATE_SPEED:
				updateView();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * Update the view method
	 */
	private void updateView() {

		if (netWorkSpeedInfo.speed != 0) {
			
			speed.setText("连接成功\n当前速度 :" + netWorkSpeedInfo.speed / 1024
					+ "Kbytes/s ");
		}else{
			speed.setText("     连接失败\n" + "请检查你的路由器设置"
					+ "\n1、设置你的路由器为自动拨号上网，在路由器设置页面找到网络设置》WAN口参数\n"
					+ "选择当断电时自动拨号，并保存重启路由器。\n"
					+ "2.设置你的路由器的无线信道为1或6，在路由器设置页面找到无线设置》选为信道为1\n"
					+ "或6，并保存重启路由器");
	//	hasDown.setText(netWorkSpeedInfo.hadFinishedBytes + "bytes");
	//	fileLength.setText(netWorkSpeedInfo.totalBytes + "");

	//	percent.setText(netWorkSpeedInfo.downloadPercent + "%");
		}
		if (imageData != null) {
			Bitmap b = BitmapFactory.decodeByteArray(imageData, 0,
					imageData.length);
			// imageView.setImageBitmap( b );
		}
	}
}
