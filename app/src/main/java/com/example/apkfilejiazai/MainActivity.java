package com.example.apkfilejiazai;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import com.zhongqin.tianlai.R;
import com.wyf.app.BrowseApplicationInfoAdapter_apk;
import com.wyf.soushuapk.ApkSearchUtils;
import com.wyf.soushuapk.MyFile;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
   
	
	private ListView listView; 
	private  List<MyFile> myfile = new ArrayList<MyFile>();
	private  List<MyFile> myfile_u = new ArrayList<MyFile>();
	private  List<MyFile> myfile_u1 = new ArrayList<MyFile>();
	private  List<MyFile> myfile_u2 = new ArrayList<MyFile>();
	private  List<MyFile> myfile_s = new ArrayList<MyFile>();
	private  List<MyFile> myfile_q = new ArrayList<MyFile>();
	private GridView listview_apk ;
	private BrowseApplicationInfoAdapter_apk browseAppAdapter_zhanshi;
	
	
	RelativeLayout apkFileLayout;
	private SharedPreferencesUtils preferencesUtils;
	private static ImageManager imageManager = new ImageManager();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.apk_main);
		
		/*
		 * 壁纸
		 */
		apkFileLayout=(RelativeLayout)this.findViewById(R.id.apkFileLayout);
		
		int wallpaperIdCurrent=preferencesUtils.getsum(MainActivity.this, "wallpaperID");
		 if(wallpaperIdCurrent!=0){
			// apkFileLayout.setBackgroundResource(wallpaperIdCurrent);
			 apkFileLayout.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.bg2));
		 }else{
			 apkFileLayout.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.bg2));
		 }
		
		
		
		 ApkSearchUtils aa = new ApkSearchUtils(MainActivity.this); 
		 File file = new File("/mnt/sdcard/Download/");
		 File file2 = new File( "/mnt/extsd/");
    	 File file3 = new File("/mnt/usbhost0/");
    	 File file4 = new File("/mnt/usbhost1/");
    	 File file5 = new File("/mnt/usbhost2/");
    //	 file2 = Environment.getDataDirectory();
    	  System.out.println(file2);
    	  myfile_q =  aa.FindAllAPKFile(file);
    	  myfile_s =  aa.FindAllAPKFile(file2);
    	  myfile_u = aa.FindAllAPKFile(file3);
    	  myfile_u1 = aa.FindAllAPKFile(file4);
    	  myfile_u2 = aa.FindAllAPKFile(file5);
    	  System.out.println(myfile_u.size());
    	  System.out.println(myfile_u1.size());
    	  System.out.println(myfile_u2.size());
    	  System.out.println(myfile_s.size());
    	   for(int i = 0;i<myfile_s.size();i++){ 
    		   myfile.add(myfile_s.get(i));
    	   }
    	  System.out.println(myfile.size());
    	  listview_apk = (GridView) findViewById(R.id.apk_grid);
    	  browseAppAdapter_zhanshi = new BrowseApplicationInfoAdapter_apk(
  				MainActivity.this, myfile);
  		  listview_apk.setAdapter(browseAppAdapter_zhanshi);
    	  listview_apk.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						"android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(new File(myfile.get(arg2).getFilePath().toString()));
				File file = new File(myfile.get(arg2).getFilePath());
				intent.setDataAndType(uri, "application/vnd.android.package-archive");
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 
}
