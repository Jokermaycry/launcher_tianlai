package com.csw.apps.zhibo;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhongqin.tianlai.R;
import com.wyf.allapp.GetAppInfo;
import com.wyf.app.AppInfo;
import com.wyf.util.ImageManager;
import com.wyf.util.SharedPreferencesUtils;

public class ZhiboActivity extends Activity implements OnItemClickListener,
		OnClickListener {

	private GridView left_listview = null;
	private GridView right_listview = null;
	RelativeLayout rel_menu = null;
	private List<AppInfo> left_mlistAppInfo = null;
	Handler handler;
	private ArrayList<String> mPlayListArray = null;
	private int mPlayListSelected = -1;
	private GetAppInfo getAppInfo = new GetAppInfo(this);
	private List<AppInfo> AllAPK_List = new ArrayList<AppInfo>();
	protected void initializeData() {
		Intent intent = getIntent();
		String action = intent.getAction();
		if (action != null && action.equals(Intent.ACTION_VIEW)) {
			String one = intent.getDataString();
			mPlayListSelected = 0;
			mPlayListArray = new ArrayList<String>();
			mPlayListArray.add(one);
		} else {
			mPlayListSelected = intent.getIntExtra("selected", 0);
			mPlayListArray = intent.getStringArrayListExtra("playlist");
		}
		if (mPlayListArray == null || mPlayListArray.size() == 0) {
			finish();
			return;
		}
	}

	String file_path = null;
	private SharedPreferencesUtils preferencesUtils;
	RelativeLayout zhiboLayout;
	private static ImageManager imageManager = new ImageManager();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_app_list);
		initializeData();
		StatService.setOn(this, StatService.EXCEPTION_LOG);
		/**
		 * 壁纸
		 */
		zhiboLayout = (RelativeLayout) this
				.findViewById(R.id.zhiboActivitylayout);

		int wallpaperIdCurrent = preferencesUtils.getsum(ZhiboActivity.this,
				"wallpaperID");
		if (wallpaperIdCurrent != 0) {
			zhiboLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, wallpaperIdCurrent));
		} else {
			zhiboLayout.setBackgroundDrawable(imageManager
					.getBitmapFromResources(this, R.drawable.wallpaper_3));
		}

		AnimationSet set = new AnimationSet(false);
		Animation animation = new AlphaAnimation(0, 1); // AlphaAnimation
														// 控制渐变透明的动画效果
		animation.setDuration(100); // 动画时间毫秒数
		set.addAnimation(animation); // 加入动画集合

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 1);
		listview = (GridView) findViewById(R.id.grid);
		mlistAppInfo = new ArrayList<AppInfo>();
		ImageButton home_button = (ImageButton) findViewById(R.id.home_result);
		home_button.setOnClickListener(this);
		listview = (GridView) findViewById(R.id.grid_add_apk_zhibo);
		listview_sel = (GridView) findViewById(R.id.grid_add_a_apk_zhibo);
		listview_sel.setLayoutAnimation(controller); // GridView 设置动画效果
		AllAPK_List = GetAppInfo.list;
		init_fenlei_date();
		init_kuaijie_apps_run();

	}

	/**
	 * 
	 */
	public void send_key() {
		try {
			String keyCommand = "input keyevent " + KeyEvent.KEYCODE_DPAD_UP;
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(keyCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���°�ť
		case R.id.home_result:
			this.finish();
			break;
		}
	}

	// �����ת����Ӧ�ó���
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Intent intent = mlistAppInfo.get(position).getIntent();
		startActivity(intent);
	}

	int visible = 1;
	int miaoshu = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (miaoshu == 1) {
				compare_apkName_getList();
				// 识别之后，分析出，如果带有add的，就是继续增加软件。
				AppInfo appInfo = new AppInfo();
				appInfo.setAppLabel("添加");
				// appInfo.setPkgName(pkgName);
				Drawable icon = getResources().getDrawable(R.drawable.add_icon);
				appInfo.setAppIcon(icon);
				appInfo.setIntent(null);
				mlistAppInfo.add(appInfo); // 添加至列表中
				AppInfo appInfo1 = new AppInfo();
				appInfo1.setAppLabel("删除");
				// appInfo.setPkgName(pkgName);
				Drawable icon1 = getResources()
						.getDrawable(R.drawable.del_icon);
				appInfo1.setAppIcon(icon1);
				appInfo1.setIntent(null);
				mlistAppInfo.add(appInfo1); // 添加至列表中
				// 更新列表
				browseAppAdapter.notifyDataSetChanged();
				send_key();

				listview_sel.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				miaoshu = 0;
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	public String app_read(String file_path) {

		SQLiteDatabase my_DataBase = this.openOrCreateDatabase(file_path
				+ ".db", MODE_PRIVATE, null);
		boolean exist = false;
		Cursor cursor = null;
		cursor = my_DataBase.rawQuery(
				"select count(*) as c from Sqlite_master  where type ='table' and name ='"
						+ "t" + "' ", null);
		if (cursor.moveToNext()) {
			int count = cursor.getInt(0);
			if (count > 0) {
				exist = true;
			}
		}
		cursor.close();
		if (!exist)
			my_DataBase
					.execSQL("CREATE TABLE t (_id INTEGER PRIMARY KEY,somestring string);");
		Cursor cur = my_DataBase.rawQuery("SELECT * FROM t", null);
		if (cur != null) {// 游标不为空
			// 返回给定名称的列的基于0开始的index，如果该属性列不存在则返回-1
			// 通过它们的index来检索属性值
			int numColumn = cur.getColumnIndex("somestring");
			if (cur.moveToFirst()) {
				// cur.moveToFirst()让游标指向第一行，如果游标指向第一行，则返回true
				do {
					String str = cur.getString(numColumn);// 获得当前行该属性的值
					/*
					 * Cursor提供了不同的方法来回索不同的数据类型 例如getInt(int
					 * index)/getString(int index)等等
					 */
					/* 做一些事情 */
					System.out.print("\n!!!!" + str + "\n");

					cur.close();
					my_DataBase.close();
					return str;
				} while (cur.moveToNext());
				/*
				 * 游标移动到下一行，如果游标已经通过了结果集中的最后， 即没有行可以移动时，则返回false
				 */
				// 其他可能移动的是 previous() 和first()方法
			}
		}
		cur.close();
		my_DataBase.close();
		return null;
		/*
		 * try { // 打开文件输入流 FileInputStream fis = openFileInput(file_path);
		 * byte[] buff = new byte[1024]; int hasRead = 0; StringBuilder sb = new
		 * StringBuilder(""); while ((hasRead = fis.read(buff)) > 0) {
		 * sb.append(new String(buff, 0, hasRead)); } return sb.toString(); }
		 * catch (Exception e) { e.printStackTrace(); } return null;
		 */
	}

	// 写入的时候，会增加一个"\n"，为什么呢？以后比较的时候，是不是不能用相等的比较。
	// 而是直接搜索
	public void app_write(String content, String file_path) {

		SQLiteDatabase my_DataBase = this.openOrCreateDatabase(file_path
				+ ".db", MODE_PRIVATE, null);
		// 创建表
		my_DataBase.execSQL("DROP TABLE t");
		my_DataBase
				.execSQL("CREATE TABLE t (_id INTEGER PRIMARY KEY,somestring string);");
		my_DataBase.execSQL("INSERT INTO t (_id,somestring) values(1,'"
				+ content + "');");
		// my_DataBase.execSQL("update  t set somestring='abcdefg' where t._id=1;");
		my_DataBase.close();

		/*
		 * try { // 以追加模式打开文件输出流 FileOutputStream fos =
		 * openFileOutput(file_path, MODE_WORLD_WRITEABLE); //
		 * 将FileOutputStream包装成PrintStream PrintStream ps = new
		 * PrintStream(fos); // 输出文件内容
		 * 
		 * ps.println(content); ps.close(); fos.close();
		 * 
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	// //////////////////////////////增加应用程序///////

	private final int init_kuaijie_apps_run = 0;
	private final int UPDATE_UI = 1;
	private final int SOUSU_YINGYONG = 2;
	private final int TISHI_YIJING_APP = 3;
	private final int TISHI_JIXU_APP = 4;
	private final int TISHI_JIAZAIAPP = 5;
	private GridView listview = null;
	private GridView listview_sel = null;

	RelativeLayout apps_rel;
	private List<AppInfo> mlistAppInfo = new ArrayList<AppInfo>();
	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case init_kuaijie_apps_run:
				kuaijie_apps_init();
				break;
			case UPDATE_UI:
				apps_rel.setBackgroundResource(R.drawable.bg);
				break;
			case SOUSU_YINGYONG:
				Toast.makeText(ZhiboActivity.this, "找不到该应用", Toast.LENGTH_LONG)
						.show();
				break;
			case TISHI_YIJING_APP:
				Toast.makeText(ZhiboActivity.this, "此应用已添加过",
						Toast.LENGTH_SHORT).show();
				break;
			case TISHI_JIXU_APP:
				Toast.makeText(ZhiboActivity.this, "添加应用成功，按返回键表示添加完毕",
						Toast.LENGTH_SHORT).show();
				break;
			case TISHI_JIAZAIAPP:
				Toast.makeText(ZhiboActivity.this, "点击应用快捷方式既能添加，按返回键表示添加完毕",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	// 新建一个线程，扫描apk，应付一些apk未装到软件扫描区里
	// 以后系统快速启动，可以通过这个来进行！
	Runnable runnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(10);// 等待系统调度，显示欢迎界面
				
				compare_apkName_getList();
				
				
				// 识别之后，分析出，如果带有add的，就是继续增加软件。
				AppInfo appInfo = new AppInfo();
				appInfo.setAppLabel("添加");
				// appInfo.setPkgName(pkgName);
				Drawable icon = getResources().getDrawable(R.drawable.add_icon);
				appInfo.setAppIcon(icon);
				appInfo.setIntent(null);
				mlistAppInfo.add(appInfo); // 添加至列表中

				AppInfo appInfo1 = new AppInfo();
				appInfo1.setAppLabel("删除");
				// appInfo.setPkgName(pkgName);
				Drawable icon1 = getResources()
						.getDrawable(R.drawable.del_icon);
				appInfo1.setAppIcon(icon1);
				appInfo1.setIntent(null);
				mlistAppInfo.add(appInfo1); // 添加至列表中
				// kuaijie_apps_init();

				mUIHandler.sendEmptyMessage(init_kuaijie_apps_run);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	};

	void init_kuaijie_apps_run() {
		Thread thread = new Thread(runnable);
		thread.start();
	}

	BrowseApplicationInfoAdapter browseAppAdapter;

	public void kuaijie_apps_init() {
		// 到时候在启动的时候初始化，通过获取，拿到数据。其实也还好啦！这样才能获取最新的数据
		// 将程序装入到列表里
		browseAppAdapter = new BrowseApplicationInfoAdapter(this, mlistAppInfo);
		listview.setAdapter(browseAppAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				String name = mlistAppInfo.get(arg2).getAppLabel();
				if (name.indexOf("添加") != -1) {

					// 进行添加数据
					mUIHandler.sendEmptyMessage(TISHI_JIAZAIAPP);
					add_apps_list();
					visible = 0;
					miaoshu = 1;
					send_key();
					listview_sel.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);

				} else if (name.indexOf("删除") != -1) {
					// 进行添加数据
					del_apps_list();
					visible = 0;
					miaoshu = 1;
					send_key();
					listview_sel.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
				} else {

					
					String bao = mlistAppInfo.get(arg2).getPkgName();
					String lei = mlistAppInfo.get(arg2).getActivityName();
					Intent intent = panduan_apps_to_list_all(bao, lei);
					if (intent == null) {
						intent = panduan_apps_to_list_lei(bao);
						if(intent == null){
							mUIHandler.sendEmptyMessage(SOUSU_YINGYONG);
							return;
						}
					} 
					startActivity(intent);
				}
			}

		});
		send_key();
	}

	/**
	 * 增加快捷方式的图标
	 */
	public void add_apps_list() {

		// 到时候在启动的时候初始化，通过获取，拿到数据。其实也还好啦！这样才能获取最新的数据
	//	saomiao_apps_to_list_all(); // 查询所有应用程序信息
		// 将程序装入到列表里
		BrowseApplicationInfoAdapter browseAppAdapter1 = new BrowseApplicationInfoAdapter(
				this, AllAPK_List);
		listview_sel.setAdapter(browseAppAdapter1);
		listview_sel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {

				String name = AllAPK_List.get(arg2).getAppLabel();
				if (name.indexOf("添加") == -1) {
					String str_dianbo = AllAPK_List.get(arg2).getPkgName();
					if (str_app_dianbo.indexOf(str_dianbo) == -1) {
						str_app_dianbo = str_app_dianbo+" "+ str_dianbo;
						app_write(str_app_dianbo, file_path);

					} else {
						mUIHandler.sendEmptyMessage(TISHI_YIJING_APP);
						return;
					}

					// mUIHandler.sendEmptyMessage(TISHI_JIXU_APP);
					compare_apkName_getList();
					// 识别之后，分析出，如果带有add的，就是继续增加软件。
					AppInfo appInfo = new AppInfo();
					appInfo.setAppLabel("添加");
					// appInfo.setPkgName(pkgName);
					Drawable icon = getResources().getDrawable(R.drawable.add_icon);
					appInfo.setAppIcon(icon);
					appInfo.setIntent(null);
					mlistAppInfo.add(appInfo); // 添加至列表中
					AppInfo appInfo1 = new AppInfo();
					appInfo1.setAppLabel("删除");
					// appInfo.setPkgName(pkgName);
					Drawable icon1 = getResources()
							.getDrawable(R.drawable.del_icon);
					appInfo1.setAppIcon(icon1);
					appInfo1.setIntent(null);
					mlistAppInfo.add(appInfo1); // 添加至列表中
					// 更新列表
					browseAppAdapter.notifyDataSetChanged();
					send_key();

					listview_sel.setVisibility(View.GONE);
					listview.setVisibility(View.VISIBLE);
					miaoshu = 0;
					
				} else {
					// 进行添加数据
				}
			}

		});
		listview_sel.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				/*
				 * Animation anim ; anim =
				 * AnimationUtils.loadAnimation(ZhiboActivity.this,
				 * R.anim.dialog_enter); v.setAnimation(anim);
				 */
				// scaleButton2(v.findViewById(R.id.zhibo_l));

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void scaleButton2(View v) {
		AnimationSet set = new AnimationSet(false);

		Animation animation = new TranslateAnimation(1, 13, 10, 50); // ScaleAnimation
																		// 控制尺寸伸缩的动画效果
		animation.setDuration(300);
		set.addAnimation(animation);
		v.startAnimation(animation);
	}

	/**
	 * 删除列表里的数据。
	 */
	public void del_apps_list() {
		// 到时候在启动的时候初始化，通过获取，拿到数据。其实也还好啦！这样才能获取最新的数据
		compare_apkName_getList(); // 查询所有应用程序信息
		// 将程序装入到列表里
		BrowseApplicationInfoAdapter browseAppAdapter1 = new BrowseApplicationInfoAdapter(
				this, mlistAppInfo);
		listview_sel.setAdapter(browseAppAdapter1);
		listview_sel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				String name = mlistAppInfo.get(arg2).getAppLabel();
				if (name.indexOf("删除") == -1) {
					String str_dianbo = mlistAppInfo.get(arg2).getPkgName();

					int len_index = str_app_dianbo.indexOf(str_dianbo);
					if (len_index != -1) {
						int len = str_dianbo.length();
						String str_app_dianbo1 = str_app_dianbo.substring(0,
								len_index);
						String str_app_dianbo2 = str_app_dianbo.substring(
								len_index + len, str_app_dianbo.length());
						str_app_dianbo = str_app_dianbo1 + str_app_dianbo2;
						app_write(str_app_dianbo, file_path);

					}

					del_apps_list();
				} else {
					// 进行添加数据
				}
			}

		});
	}

	/*
	 * 初始化分类的数据。
	 */
	void init_fenlei_date() {
		str_app = mPlayListArray.get(mPlayListSelected);
		file_path = mPlayListArray.get(mPlayListSelected + 1);
		SQLiteDatabase my_DataBase = this.openOrCreateDatabase(file_path
				+ ".db", MODE_PRIVATE, null);
		boolean exist = false;
		Cursor cursor = null;
		cursor = my_DataBase.rawQuery(
				"select count(*) as c from Sqlite_master  where type ='table' and name ='"
						+ "t" + "' ", null);
		if (cursor.moveToNext()) {
			int count = cursor.getInt(0);
			if (count > 0) {
				exist = true;
			}
		}
		if (!exist)
			my_DataBase
					.execSQL("CREATE TABLE t (_id INTEGER PRIMARY KEY,somestring string);");
		my_DataBase.close();
		cursor.close();
		str_app_dianbo = app_read(file_path);
		if (str_app_dianbo == null) {
			app_write(str_app, file_path);
			str_app_dianbo = str_app;
		}
	}

	String str_app_dianbo = null;
	String str_app = null;

 
	// 获得所有启动Activity的信息，类似于Launch界面
	/*
	 * 将app里，所有的程序，都扫描到列表里。
	 */
	public void saomiao_apps_to_list_all() {
		if (mlistAppInfo != null) {
			mlistAppInfo.clear();
		}
		mlistAppInfo = AllAPK_List;
		 
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
		System.gc();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}

	/**
	 * 得到字符串里面的所有包名，再得到apk
	 */
	public void  compare_apkName_getList(){
		if (str_app_dianbo == null)
			return ;
		if (mlistAppInfo.size()>0||mlistAppInfo != null) {
			mlistAppInfo.clear();
		}
		for(int i = 0;i < AllAPK_List.size(); i++){
			
			if (str_app_dianbo.indexOf(AllAPK_List.get(i).getPkgName()) != -1) {
				mlistAppInfo.add(AllAPK_List.get(i));
			}
		 
		} 
	}
	/**
	 * 所有的程序，都扫描到列表里。判断有没有这个apk
	 */
	public Intent panduan_apps_to_list_all(String baoName, String zhuleiName) {
		for (int i = 0; i < GetAppInfo.list.size(); i++) {

			// icon = reInfo.loadIcon(pm); // 获得应用程序图标
			if (GetAppInfo.list.get(i).getPkgName().equals(baoName)
					&& GetAppInfo.list.get(i).getActivityName().equals(zhuleiName)) {
				// 为应用程序的启动Activity 准备Intent
				return GetAppInfo.list.get(i).getIntent();
			}
		}
		return null;
	}
	/**
	 * 所有的程序，都扫描到列表里。判断有没有这个apk
	 */
	public Intent panduan_apps_to_list_lei(String baoName) {
		for (int i = 0; i < GetAppInfo.list.size(); i++) {

			// icon = reInfo.loadIcon(pm); // 获得应用程序图标
			if (GetAppInfo.list.get(i).getPkgName().equals(baoName)) {
				// 为应用程序的启动Activity 准备Intent
				return GetAppInfo.list.get(i).getIntent();
			}
		}
		return null;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("销毁了");
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		getAppInfo.getAllAPK_add();
		super.onRestart();
	}
}