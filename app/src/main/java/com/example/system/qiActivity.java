package com.example.system;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.baidu.mobstat.StatService;
import com.zhongqin.tianlai.R;
import com.csw.tp.cc_server.SocketServer;

import com.wyf.allapp.GetAppInfo;
import com.wyf.allapp.StartappActivity;
import com.wyf.util.FileReadWriteUtil;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/*
 * 修改开机启动的Activity
 */
public class qiActivity extends Activity {

	ImageView imv1;
	TextView qitext1;
	Button qibtn;
	Button qibtn1;
	StartappActivity qidong;
	static final private int GET_CODE = 0;
	SharedPreferencesUtils sha;
	RelativeLayout layRelativeLayout;
	private static ImageManager imageManager = new ImageManager();

	
	//added by lgj
	LinearLayout addBgLv;//显示添加应用图标和名称的布局
	/////////////////////////////////////////////////////////////////
	updateReceiver uReceiver;
	public static final String SET_APP="com.qiactivity.app";
	public static final String SET_APP_DELETE="com.qiactivity.app.delete";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
		setContentView(R.layout.qi);
		uReceiver=new updateReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.qiactivity.app");
		intentFilter.addAction("com.qiactivity.app.delete");
		registerReceiver(uReceiver, intentFilter);
		qidong = new StartappActivity();
		imv1 = (ImageView) findViewById(R.id.qiimv1);
		qitext1 = (TextView) findViewById(R.id.qitext1);
		//added by lgj
		addBgLv=(LinearLayout)this.findViewById(R.id.qilin1);
		///////////////////////////////////////////////////////////////////////////////////
		
		/*String internal_sdPath=FileReadWriteUtil.getSDPath(qiActivity.this);//sd卡路径
		String fileDirName="baomingleiming";//文件夹名称
		String fileName="baomingleiming.txt";//文件名称
		String packageName="";//要启动的应用程序包名
		boolean weatherHasFile=FileReadWriteUtil.isFileExist(internal_sdPath, fileDirName);
		if(weatherHasFile==false){
			System.out.println("文件夹不存在");
			FileReadWriteUtil.creatSDDir(internal_sdPath, fileDirName);
			FileReadWriteUtil.creatSDFile(internal_sdPath+"/"+fileDirName,fileName);
			//向文件中写数据
		}else{
			System.out.println("文件夹存在");
			if(FileReadWriteUtil.isFileExist(internal_sdPath+"/"+fileDirName,fileName)){
				System.out.println("如果文件存在");
//			    boolean deleteFlag=	new File(internal_sdPath+"/"+fileDirName+"/"+fileName).delete();
//				System.out.println("删除"+deleteFlag); 
				String fileContent=FileReadWriteUtil.readFileContent(internal_sdPath+"/"+fileDirName+"/"+fileName);
				if(!fileContent.equals("")&&fileContent!=null){
					try {
						packageName=fileContent.substring(0, fileContent.indexOf(","));//包名
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}	
		}

		if (!packageName.equals("")&&packageName != null) {
			queryAppInfo2(packageName);
		}
*/
		String co = sha.getData(qiActivity.this, "bao");
		if (co != null) {
			queryAppInfo2(co);
		}
		qibtn = (Button) findViewById(R.id.qibtn1);
		qibtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(qiActivity.this, StartappActivity.class);
				startActivityForResult(intent, GET_CODE);
			}
		});
		qibtn1 = (Button) findViewById(R.id.qibtn2);
		qibtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				qitext1.setText("");
				qidong.code1 = 10001;
				
				//modified by lgj
				//imv1.setBackgroundResource(R.drawable.tianjia);
				addBgLv.setBackgroundResource(R.drawable.tianjia);
				imv1.setVisibility(View.GONE);
				//////////////////////////////////////////////////////////////////////
				sha.setData(qiActivity.this, "bao", "");
				sha.setData(qiActivity.this, "activityName", "");
				sha.setData(qiActivity.this, "SetAppStart", "");
				String jsonResult ="setappdelete";
				SocketServer.SendDataClient(jsonResult);
				
				/*
				String internal_sdPath=FileReadWriteUtil.getSDPath(qiActivity.this);//sd卡路径
				String fileDirName="baomingleiming";//文件夹名称
				String fileName="baomingleiming.txt";//文件名称
				boolean weatherHasFile=FileReadWriteUtil.isFileExist(internal_sdPath, fileDirName);
				if(weatherHasFile==false){
					System.out.println("文件夹不存在");
					FileReadWriteUtil.creatSDDir(internal_sdPath, fileDirName);
					//向文件中写数据
				}else{
					System.out.println("文件夹存在");
					if(FileReadWriteUtil.isFileExist(internal_sdPath+"/"+fileDirName,fileName)){
						System.out.println("如果文件存在，删除");
					    boolean deleteFlag=	new File(internal_sdPath+"/"+fileDirName+"/"+fileName).delete();
						System.out.println("删除"+deleteFlag); 
					}	
				}
			    FileReadWriteUtil.creatSDFile(internal_sdPath+"/"+fileDirName,fileName);
				*/
				
			}
		});
		/*
		 * 壁纸
		 */
		layRelativeLayout = (RelativeLayout) this.findViewById(R.id.qi_r);
		int wallpaperIdCurrent = sha.getsum(qiActivity.this,
				"wallpaperID");
		if (wallpaperIdCurrent != 0) {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			layRelativeLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_3));
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GET_CODE) {
			if (qidong.code1 == 100) {
				try {
					qitext1.setText(GetAppInfo.list.get(qidong.code)
							.getAppLabel());
					//modified by lgj
//					imv1.setBackground(GetAppInfo.list.get(qidong.code)
//							.getAppIcon());
					imv1.setVisibility(View.VISIBLE);
					imv1.setImageDrawable(GetAppInfo.list.get(qidong.code)
							.getAppIcon());
					addBgLv.setBackgroundResource(R.drawable.xing);
					///////////////////////////////////////////
				} catch (Exception e) {
					System.out.println(e);
				}
			}

		}
	}

	@SuppressLint("NewApi")
	public void queryAppInfo2(String co) {

		int saomiao = 0;
		PackageManager pm = this.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);

		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));

		// mlistAppInfo.clear();
		int len = resolveInfos.size();

		for (ResolveInfo reInfo : resolveInfos) {
			saomiao++;

			String pkgName = reInfo.activityInfo.packageName;

			if (co.equals(pkgName)) {
				String applabel = (String) reInfo.loadLabel(pm);
				Drawable icon = reInfo.loadIcon(pm);
				//modified by lgj
//				imv1.setBackground(icon);
				imv1.setVisibility(View.VISIBLE);
				imv1.setImageDrawable(icon);
				addBgLv.setBackgroundResource(R.drawable.xing);
				///////////////////////////////////////////////////
				qitext1.setText(applabel);
				break;
			}

		}

	}
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String co = sha.getData(qiActivity.this, "bao");
				if (co != null) {
					queryAppInfo2(co);
				}
				break;
            case 1:
            	qitext1.setText("");
				qidong.code1 = 10001;
				addBgLv.setBackgroundResource(R.drawable.xing);
				imv1.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};
	 @Override
	    protected void onDestroy() {
	    	// TODO Auto-generated method stub
	    	super.onDestroy();
	    	unregisterReceiver(uReceiver);
	    }
	class updateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getAction().equals(SET_APP)) {
				System.out.println("保存");
				handler.sendEmptyMessage(0);
			}
			if (arg1.getAction().equals(SET_APP_DELETE)) {
				System.out.println("删除");
				handler.sendEmptyMessage(1);
			}
		}
    	
    }
	

}
