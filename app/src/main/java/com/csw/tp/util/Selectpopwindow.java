package com.csw.tp.util;






import com.zhongqin.tianlai.R;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Selectpopwindow  {
	WindowManager wManager;
	private WindowManager.LayoutParams mParams;
	 private Selectpopwindow(Context context){
		 wManager = (WindowManager) context.getApplicationContext().getSystemService(
			        Context.WINDOW_SERVICE);
			    mParams = new WindowManager.LayoutParams();
			    mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示window
			    mParams.format = PixelFormat.TRANSLUCENT;// 支持透明
			    //mParams.format = PixelFormat.RGBA_8888;
			    mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
			    mParams.width = 490;//窗口的宽和高
			    mParams.height = 160;
			    mParams.x = 534;//窗口位置的偏移量
			    mParams.y = 560;
			    View myview = View.inflate(context, R.layout.popwindow,
						null);
			    wManager.addView(myview, mParams);
	         
	 }
}
