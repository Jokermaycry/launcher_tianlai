package com.wyf.setting;




import com.zhongqin.tianlai.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingTest extends PreferenceActivity implements OnPreferenceChangeListener,   
    OnPreferenceClickListener ,
	SeekBar.OnSeekBarChangeListener{
	
	 //定义相关变量  
    String p1Key;  
    String p2Key;  
    String p3Key; 
    CheckBoxPreference updateSwitchCheckPref;  
    ListPreference updateFrequencyListPref; 
    PreferenceScreen preferenceScreen;
    
    
    SeekBar mSeekBar;
	TextView mProgressText;
	TextView mTrackingText;
	private PopupWindow mPopupWindow;
	private Button mButton;
	View popupView;
	private static final String TAG = "DisplayMetricsDemo";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		
		
		addPreferencesFromResource(R.xml.preferencesii);
	   

		
		p1Key = getResources().getString(R.string.auto_p1_key);
		p2Key = getResources().getString(R.string.auto_p2_key);
		p3Key = getResources().getString(R.string.auto_p3_key);
		updateSwitchCheckPref = (CheckBoxPreference) findPreference(p1Key);
		updateFrequencyListPref = (ListPreference) findPreference(p2Key);
		preferenceScreen = (PreferenceScreen) findPreference(p3Key);
		
		 updateSwitchCheckPref.setOnPreferenceChangeListener(this);  
	        updateSwitchCheckPref.setOnPreferenceClickListener(this);  
	        updateFrequencyListPref.setOnPreferenceChangeListener(this);  
	        updateFrequencyListPref.setOnPreferenceClickListener(this);  
	        preferenceScreen.setOnPreferenceChangeListener(SettingTest.this);
	        preferenceScreen.setOnPreferenceClickListener(SettingTest.this);
	        
	          
	        // Set up a listener whenever a key changes  
	       
			/*
			 * final android.app.AlertDialog.Builder b = new
			 * AlertDialog.Builder(class_SeekBar.this);
			 * b.setIcon(R.drawable.settings_alt); b.setTitle("屏幕大小");
			 * b.setItems(R.id.seekBar,
			 * mSeekBar.setOnSeekBarChangeListener(class_SeekBar.this));
			 */
		//	mPopupWindow = new PopupWindow(popupView, 300, 200, true);
			// 下面三行代码的作用是点击空白处的时候PopupWindow会消失
		//	mPopupWindow.setTouchable(true);
		//	mPopupWindow.setOutsideTouchable(true);
			// mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
			// (Bitmap) null));
		//	mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.ys_dianyin));
	} 
	 @Override  
	    public boolean onPreferenceChange(Preference preference, Object newValue) {  
	        // TODO Auto-generated method stub  
	        Log.v("SystemSetting", "preference is changed");  
	        Log.v("Key_SystemSetting", preference.getKey());  
	        //判断是哪个Preference改变了  
	        if(preference.getKey().equals(p1Key))  
	        {  
	      //      Log.v("SystemSetting", "checkbox preference is changed");  
	        }  
	        else if(preference.getKey().equals(p2Key))  
	        {  
	            Log.v("SystemSetting", "list preference is changed");  
	        }  
	        else  
	        {  
	            //如果返回false表示不允许被改变  
	            return false;  
	        }  
	        //返回true表示允许改变  
	        return true;  
	    }  
	    @SuppressLint("NewApi")
		@Override  
	    public boolean onPreferenceClick(Preference preference) {  
	        // TODO Auto-generated method stub  
	        Log.v("SystemSetting", "preference is clicked");  
	        Log.v("Key_SystemSetting", preference.getKey());  
	        //判断是哪个Preference被点击了  
	        if(preference.getKey().equals(p1Key))  
	        {  
	        	preference.setIcon(getResources().getDrawable(R.drawable.ic_launcher_settings));
	        //    Log.v("SystemSetting", "checkbox preference is clicked");  
	            DisplayMetrics displayMetrics = new DisplayMetrics();  
	            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);  
	            Log.i(TAG, "手机屏幕分辨率:" + displayMetrics.widthPixels + "X"  
	                    + displayMetrics.heightPixels);  
	            Log.i(TAG, "密度:" + displayMetrics.density);  
	            Log.i(TAG, "使用每英寸的像素点来显示密度:" + displayMetrics.densityDpi);  
	            Log.i(TAG, displayMetrics.xdpi + "," + displayMetrics.ydpi);  
	            Log.i(TAG, displayMetrics.scaledDensity + "");  
	            
	          //获取分辨率
	            DisplayMetrics dm=new DisplayMetrics();
	            getWindowManager().getDefaultDisplay().getMetrics(dm);
	            //给常量类中的屏幕高和宽赋值
	            Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高 
	            Window window = getWindow(); 
	           LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值 
	           windowLayoutParams.width = (int) (display.getWidth() * 0.5); // 宽度设置为屏幕的0.95  
	           windowLayoutParams.height = (int) (display.getHeight() * 0.1); // 高度设置为屏幕的0.6 
	           windowLayoutParams.alpha = 0.5f;// 设置透明度
	            return true;
	        }  
	        else if(preference.getKey().equals(p2Key))  
	        {  
	        	
	            Log.v("SystemSetting", "list preference is clicked");  
	           
	        }else if(preference.getKey().equals(p3Key))  {
	        	preference.setIcon(getResources().getDrawable(R.drawable.ic_launcher_settings));
	        	   popupView = getLayoutInflater().inflate(
	   					R.layout.gundong_jingdutiao, null);

	   			mSeekBar = (SeekBar) popupView.findViewById(R.id.seekBar);
	   			// setOnSeekBarChangeListener() - 响应拖动进度条事件
	   			mSeekBar.setOnSeekBarChangeListener(this);
	   			mProgressText = (TextView) popupView.findViewById(R.id.progress);
	   			mTrackingText = (TextView) popupView.findViewById(R.id.tracking);
	        	AlertDialog.Builder builder = new Builder(this);
	    		builder.setTitle(R.string.auto_p2_title);
	    		builder.setView(popupView);
	    		builder.setIcon(R.drawable.liangguang);
	    	//	builder.setMessage(msg);
	    		// 更新
	    		builder.setPositiveButton(R.string.queding,  new OnClickListener()
	    		{
	    			@Override
	    			public void onClick(DialogInterface dialog, int which)
	    			{
	    				dialog.dismiss();
	    			}
	    		});
	    		// 稍后更新
	    		builder.setNegativeButton(R.string.cancel, new OnClickListener()
	    		{
	    			@Override
	    			public void onClick(DialogInterface dialog, int which)
	    			{
	    				
	    				dialog.dismiss();
	    			}
	    		});
	    		Dialog noticeDialog = builder.create();
	    		noticeDialog.show();
	    		return true;
	        }
	        else  
	        {  
	            return false;   
	        }  
	        return true;  
	    }  
	 // 拖动进度条后，进度发生改变时的回调事件
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromTouch) {
			mProgressText.setText(progress + "%");
		}

		// 拖动进度条前开始跟踪触摸
		public void onStartTrackingTouch(SeekBar seekBar) {
			mTrackingText.setText("开始跟踪触摸");
		}

		// 拖动进度条后停止跟踪触摸
		public void onStopTrackingTouch(SeekBar seekBar) {
			mTrackingText.setText("停止跟踪触摸");
		}

}
