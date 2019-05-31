package com.cl.rkm_hdmi_display;

import java.util.List;

import com.zhongqin.tianlai.R;
import com.example.system.ShowSetActivity3288;



import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.DisplayOutputManager;
import android.os.Handler;
import android.os.RemoteException;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class hdmi_Activity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ////����Ϊtrue�����������ʧ
		setFinishOnTouchOutside(true);//
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.display_main);
		/*
		 * WindowManager m = getWindowManager(); Display d =
		 * m.getDefaultDisplay(); //Ϊ��ȡ��Ļ�?�� LayoutParams p =
		 * getWindow().getAttributes(); //��ȡ�Ի���ǰ�Ĳ���ֵ p.height = (int)
		 * (d.getHeight() * 1.0); //�߶�����Ϊ��Ļ��1.0 p.width = (int) (d.getWidth() *
		 * 0.7); //�������Ϊ��Ļ��0.8 p.alpha = 1.0f; //���ñ���͸���� p.dimAmount = 0.0f;
		 * //���úڰ��� getWindow().setAttributes(p);
		 */
		android.app.FragmentManager fragmentManager = getFragmentManager();
		android.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		DisplaySettings dreamsettings = new DisplaySettings();

		fragmentTransaction.replace(android.R.id.content, dreamsettings);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		// this.finish();
	}

	/*
	 * @Override public void onBuildHeaders(List<Header> headers) {
	 * loadHeadersFromResource(R.xml.settings_headers, headers);
	 * 
	 * }
	 */

	/*
	 * @Override protected boolean isValidFragment(String fragmentName) { return
	 * true; }
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
			Intent intent = new Intent(hdmi_Activity.this,
					ShowSetActivity3288.class);
			setResult(RESULT_OK, intent);
			hdmi_Activity.this.finish();
		}
		return super.onKeyDown(keyCode, keyEvent);
	}
}
