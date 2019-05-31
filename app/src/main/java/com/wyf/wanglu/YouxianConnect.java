package com.wyf.wanglu;

import com.zhongqin.tianlai.R;
import com.loaddown.test.SpeedActivity;
import com.wyf.util.NetCheck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

public class YouxianConnect extends Activity {
	private static final int CMNET = 3;
	private static final int CMWAP = 2;
	private static final int WIFI = 1;
	WifiManager wifiManager = null;
	String book_text_str = null;
	TextView book_text;
	Button wangsu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youxian_main);
		book_text = (TextView) findViewById(R.id.yx_text);
		wangsu = (Button) findViewById(R.id.wanglu_wangsu);
		wangsu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(YouxianConnect.this,
						SpeedActivity.class);
				// intent.setClass(MainActivity.this, SpeedActivity.class);
				startActivity(intent);
			}
		});
		wangsu.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					scaleButton(v);
					wangsu.bringToFront();

				} else {
					scaleButton2(v);
					// animationSet.setFillAfter(false);
					// btn_dianying.setBackgroundResource(R.drawable.dianshitai_0);
				}
			}
		});

		String ip = null;

		int net_leixing = getAPNType(YouxianConnect.this);

		if (net_leixing == WIFI) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			ip = intToIp(ipAddress);
			book_text_str = "网络IP：" + ip + "\n\n网络状态：wifi网络";
		} else {

			if (!NetCheck.checkNetWorkStatus(YouxianConnect.this)) {
				book_text_str = "网络IP：" + ip + "\n\n网络状态：没有网络";
			} else
				book_text_str = "网络状态：有线网络";
		}

		book_text.setText(book_text_str);
	}

	private void scaleButton(View v) {
		AnimationSet animationSet = new AnimationSet(true);
		// ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f,
		// 1.0f, AnimationSet.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0.5f);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.1f, 1, 1.1f,
				AnimationSet.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		animationSet.addAnimation(scaleAnimation);
		// animationSet.setStartOffset(1000);
		animationSet.setFillAfter(true);
		animationSet.setFillBefore(false);
		animationSet.setDuration(1);
		v.startAnimation(animationSet);
	}

	AnimationSet animationSet2 = new AnimationSet(true);

	private void scaleButton2(View v) {
		AnimationSet animationSet = new AnimationSet(true);
		// ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f,
		// 1.0f, AnimationSet.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0.5f);
		ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.0f, 1.0f, 1.0f,
				1.0f, AnimationSet.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		animationSet2.addAnimation(scaleAnimation2);
		// animationSet.setStartOffset(1000);
		animationSet2.setFillAfter(false);
		animationSet2.setFillBefore(false);
		animationSet2.setDuration(1);
		v.startAnimation(animationSet2);
	}

	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
	 * 
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		System.out.println("networkInfo.getExtraInfo() is "
				+ networkInfo.getExtraInfo());
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = CMNET;
			} else {
				netType = CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		}
		return netType;

	}

	private String intToIp(int i) {

		return (i & 0xFF) + "." +

		((i >> 8) & 0xFF) + "." +

		((i >> 16) & 0xFF) + "." +

		(i >> 24 & 0xFF);

	}
}
