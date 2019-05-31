package com.example.system;

import com.baidu.mobstat.StatService;
import com.csw.csw_desktop_yahao.MainActivity;
import com.zhongqin.tianlai.R;
import com.csw.csw_desktop_yahao.Welcome;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UnlockActivity extends Activity {
	private RelativeLayout layout_zhu;
	private EditText mima;
	private Button but_ok;
	private TextView key_hint;

	private SharedPreferencesUtils preferencesUtils;
	private boolean input_key = true;
	private int unlock_sum = 2;
	LinearLayout layRelativeLayout;
	private static ImageManager imageManager = new ImageManager();
	/** 是否解锁 */
	public static int unLockKey = 0;
	/** 开机启动保存的值变量 */
	private String qidong = "", qidongActivity = "";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.familykey_open);
		/** 开机启动 */
		qidongActivity = preferencesUtils.getData(this, "activityName");
		qidong = preferencesUtils.getData(this, "bao");
		/** 1代表解锁 */
		unLockKey = 1;
		layout_zhu = (RelativeLayout) findViewById(R.id.welcome_key);
		mima = (EditText) findViewById(R.id.wfamily_key_mima);
		but_ok = (Button) findViewById(R.id.but_ok);
		key_hint = (TextView) findViewById(R.id.key_hint);
		// 壁纸
		layRelativeLayout = (LinearLayout) this.findViewById(R.id.fal_r);
		int wallpaperIdCurrent = preferencesUtils.getsum(this, "wallpaperID");
		if (wallpaperIdCurrent != 0) {
			// layRelativeLayout.setBackgroundResource(wallpaperIdCurrent);
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_3));
		}
		but_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String mimaValue = mima.getText().toString();
				if (mimaValue.equals("") || mimaValue == null) {
					mima.setError("密码不能为空");
					return;
				}
				String for_key = preferencesUtils.getData(UnlockActivity.this,
						"family_pwd");
				if (mimaValue.equals(for_key)) {
					/*if (qidongActivity!=null && qidong!=null && !qidongActivity.equals("") && !qidong.equals("")) {

						Intent intent = new Intent();
						intent.setComponent(new ComponentName(qidong,
								qidongActivity));
						startActivity(intent);
					} else {*/
						Intent intent = new Intent(UnlockActivity.this,
								Welcome.class);
						startActivity(intent);
						System.out.println("解锁成功，开机启动");
					/*}*/
					finish();
					System.gc();

				} else {
					if (unlock_sum == 0) {
						Intent but_shezhi_xitongchongqi = new Intent();
						but_shezhi_xitongchongqi
								.setComponent(new ComponentName(
										"com.example.reset",
										"com.example.reset.MainActivity"));
						startActivity(but_shezhi_xitongchongqi);
						finish();
						System.gc();
					}
					key_hint.setText("密码错误!您还有 " + (unlock_sum--) + " 次机会");
					key_hint.setTextColor(Color.RED);
				}
			}
		});
		but_ok.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					but_ok.setTextColor(Color.GREEN);
				} else {
					but_ok.setTextColor(Color.WHITE);
				}
			}
		});
	}

	/*
	 * private void password_info(String value) {
	 * 
	 * String this_key = pwd1 + pwd2 + pwd3 + pwd4; String for_key =
	 * preferencesUtils.getData(this, "family_pwd"); if
	 * (this_key.equals(for_key)) { if (!qidongActivity.equals("") &&
	 * !qidong.equals("")) {
	 * 
	 * Intent intent = new Intent(); intent.setComponent(new
	 * ComponentName(qidong, qidongActivity)); startActivity(intent); } else {
	 * Intent intent = new Intent(this, SimplifyMainUIActivity.class);
	 * startActivity(intent); System.out.println("解锁成功，开机启动"); } finish();
	 * System.gc();
	 * 
	 * } else { if (unlock_sum == 0) { Intent but_shezhi_xitongchongqi = new
	 * Intent(); but_shezhi_xitongchongqi.setComponent(new ComponentName(
	 * "com.example.reset", "com.example.reset.MainActivity"));
	 * startActivity(but_shezhi_xitongchongqi); finish(); System.gc(); }
	 * key_hint.setText("密码错误!您还有 " + (unlock_sum--) + " 次机会");
	 * key_hint.setTextColor(Color.RED); } return; } }
	 */

	@Override
	public boolean onKeyDown(int keycode, KeyEvent arg1) {
		// TODO Auto-generated method stub
		if (keycode == KeyEvent.KEYCODE_BACK) {
			return true;

		}
		return super.onKeyDown(keycode, arg1);
	}

}
