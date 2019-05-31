package com.example.system;

import com.baidu.mobstat.StatService;
import com.zhongqin.tianlai.R;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FamilyKeyActivity extends Activity {

	private Button key;
	private Button key_ok;
	private SharedPreferencesUtils preferencesUtils;
	private int sum;
	private boolean input_key;
	private RelativeLayout layout_mima;
	private RelativeLayout layout_zhu;
	private EditText mima;
	/*private TextView mima1;
	private TextView mima2;
	private TextView mima3;
	private TextView mima4;*/
	private static ImageManager imageManager = new ImageManager();
	RelativeLayout layRelativeLayout;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.familykey_main);
		key = (Button) findViewById(R.id.but_key);
		key_ok = (Button) findViewById(R.id.key_ok);
		layout_mima = (RelativeLayout) findViewById(R.id.family_mima);
		layout_zhu = (RelativeLayout) findViewById(R.id.family_zhu);
		mima = (EditText) findViewById(R.id.family_key_mima);
		/*mima1 = (TextView) findViewById(R.id.family_key_mima1);
		mima2 = (TextView) findViewById(R.id.family_key_mima2);
		mima3 = (TextView) findViewById(R.id.family_key_mima3);
		mima4 = (TextView) findViewById(R.id.family_key_mima4);*/
		// 壁纸

		layRelativeLayout = (RelativeLayout) this.findViewById(R.id.fal_main_r);
		int wallpaperIdCurrent = preferencesUtils.getsum(FamilyKeyActivity.this,
				"wallpaperID");
		if (wallpaperIdCurrent != 0) {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_3));
		}
		sum = preferencesUtils.getsum(this, "family_key");
		if (sum == 0) {
			key.setBackground(getResources().getDrawable(
					R.drawable.family_close));
		}
		if (sum == 1) {
			key.setBackground(getResources()
					.getDrawable(R.drawable.family_open));
		}
		key.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sum = preferencesUtils.getsum(FamilyKeyActivity.this,
						"family_key");
				if (sum == 1) {
					key.setBackground(getResources().getDrawable(
							R.drawable.family_close));
					preferencesUtils.setsum(FamilyKeyActivity.this,
							"family_key", 0);
				}
				if (sum == 0) {
					key.setBackground(getResources().getDrawable(
							R.drawable.family_open));
					preferencesUtils.setsum(FamilyKeyActivity.this,
							"family_key", 1);
					layout_zhu.setVisibility(View.GONE);
					layout_mima.setVisibility(View.VISIBLE);
					mima.setText("");
					mima.setFocusable(true);
					mima.requestFocus();
					/*mima2.setText("");
					mima3.setText("");
					mima4.setText("");*/
					input_key = true;
				}
			}
		});
		key_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String mimaValue = mima.getText().toString();
				if(mimaValue.equals("")||mimaValue==null){
					mima.setError("密码不能为空");
					return ;
				}
				preferencesUtils.setData(FamilyKeyActivity.this, "family_pwd",mimaValue);
				input_key = false;
				layout_zhu.setVisibility(View.VISIBLE);
				layout_mima.setVisibility(View.GONE);
			}
		});
		key_ok.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					key_ok.setTextColor(Color.RED);
				}else{
					key_ok.setTextColor(Color.WHITE);
				}
			}
		});
	}

	/*private void password_info(String value) {
		String pwd1 = mima1.getText().toString();
		String pwd2 = mima2.getText().toString();
		String pwd3 = mima3.getText().toString();
		String pwd4 = mima4.getText().toString();
		if (pwd1 == null || pwd1.equals("")) {
			mima1.setText(value);
			return;
		}
		if (pwd2 == null || pwd2.equals("")) {
			mima2.setText(value);
			return;
		}
		if (pwd3 == null || pwd3.equals("")) {
			mima3.setText(value);
			return;
		}
		if (pwd4 == null || pwd4.equals("")) {
			mima4.setText(value);
			preferencesUtils.setData(this, "family_pwd", pwd1 + pwd2 + pwd3
					+ pwd4);
			input_key = false;
			layout_zhu.setVisibility(View.VISIBLE);
			layout_mima.setVisibility(View.GONE);
			return;
		}
	}*/

	@Override
	public boolean onKeyDown(int keycode, KeyEvent arg1) {
		// TODO Auto-generated method stub
		/*if (keycode == KeyEvent.KEYCODE_0) {
			if (input_key) {
				password_info("0");
			}
			return true;
		}
		if (keycode == KeyEvent.KEYCODE_1) {
			if (input_key) {
				password_info("1");
			}
			return true;
		}
		if (keycode == KeyEvent.KEYCODE_2) {
			if (input_key) {
				password_info("2");
			}
			return true;
		}
		if (keycode == KeyEvent.KEYCODE_3) {
			if (input_key) {
				password_info("3");
			}
			return true;
		}
		if (keycode == KeyEvent.KEYCODE_4) {
			if (input_key) {
				password_info("4");
			}
			return true;
		}
		if (keycode == KeyEvent.KEYCODE_5) {
			if (input_key) {
				password_info("5");
			}
			return true;
		}
		if (keycode == KeyEvent.KEYCODE_6) {
			if (input_key) {
				password_info("6");
			}
			return true;
		}
		if (keycode == KeyEvent.KEYCODE_7) {
			if (input_key) {
				password_info("7");
			}
			return true;
		}
		if (keycode == KeyEvent.KEYCODE_8) {
			if (input_key) {
				password_info("8");
			}
			return true;
		}
		if (keycode == KeyEvent.KEYCODE_9) {
			if (input_key) {
				password_info("9");
			}
			return true;
		}*/

		if (keycode == KeyEvent.KEYCODE_BACK) {
			if (input_key) {
				preferencesUtils
						.setsum(FamilyKeyActivity.this, "family_key", 0);
			}

		}

		return super.onKeyDown(keycode, arg1);
	}

}
