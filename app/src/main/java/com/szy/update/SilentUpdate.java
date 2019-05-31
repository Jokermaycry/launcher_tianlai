package com.szy.update;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.csw.csw_desktop_yahao.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyf.util.SharedPreferencesUtils;


import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.Context;
/**
 *@author coolszy
 *@date 2012-4-26
 *@blog http://blog.92coding.com
 */
public class SilentUpdate {
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;

	private Context mContext;
	private Handler handler;                  // 声明handler
	

	public  SilentUpdate(Context context)
	{
		mContext = context;
		// 初始化handler
		//在跑线程的时候，对话框不能在线程里跑。不知道为什么，应该理解成
		//对话框只能在主线程里跑吧！
		/*handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
	           if(msg.what == 1) // handler接收到相关的消息后
	           {
	   			UpdateManager manager = new UpdateManager(mContext);
	   			// 检查软件更新
	   			manager.checkUpdate("version.xml");
//	     			Intent launchIntent = new Intent();
//	    			launchIntent.setComponent(new ComponentName("com.example.test","com.example.test.FullscreenActivity"));
//	    			mContext.startActivity(launchIntent);
	   			
	           }
	
			}
		};*/
	}
	
	public  void updateSilent(HashMap mHashMap)
	{
		this.mHashMap = mHashMap;
		//wjz 没有网络，就直接退出
		if (!NetCheck.checkNetWorkStatus(mContext))
		{
			Toast.makeText(mContext,"网络有问题！请检查网络！", Toast.LENGTH_LONG).show();
			return;
		}
	//	xml_parse();
	//	String ver = getAppVersionName(mContext);
		// 获取网站上面的信息,如果没有对应的，就干掉
	//	String url_res = sendGet("http://112.124.43.99/apk/jingmoInstall/q3-ver.txt",null);
	try
	{  
		getIfpush();
		 String url_res =mIfpushValue;
		String result = mIfInstallValue;
		if(url_res == "")
			return;
		 
		int http_dou =  Integer.parseInt(url_res);

		//当网络版本高于本地版本，就要进行更新
		if (http_dou==1) 
		{
			if(result == null || result.equals("true")){
				return ;
			}else{
			downloadApk();
			}
		}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	}
	private String mIfpushValue = "0";
	private String mIfInstallValue = "false";
	private void getIfpush(){
		try{
		String url = "http://112.124.102.254:8080/csw_ui_push/csw_push/getIfpush.action";
		String mac = SharedPreferencesUtils.getData(mContext, "mac");
		String	result = "";
		if(mac == null || mac.equals("")){
			mac = getLocalMacAddress();
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("apkname", "CSWUI_3128")); 
		params.add(new BasicNameValuePair("mac", mac));
	     	result = "1";
		if(result.indexOf("Connection")!=-1 || result.indexOf("Error")!=-1||result.indexOf("Unable to resolve host")!=-1||result.equals("connect_false")){
			return ;
		}
		if(result ==null){
			return ;
		}
		 
			Gson gson = new Gson();
			push_table g = gson.fromJson(result,
					new TypeToken<push_table>() {
					}.getType());
				push_table table = g;
				mIfpushValue = table.getIfpush();
				mIfInstallValue = table.getIfinstall();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		}
	private String	setIfinstall(String ifinstall){
		String url = "http://112.124.102.254:8080/csw_ui_push/csw_push/setIfinstall.action";
		String mac = SharedPreferencesUtils.getData(mContext, "mac");
		String	versionCode = "0";
		try{
		if(mac == null || mac.equals("")){
			mac = getLocalMacAddress();
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("apkname", "CSWUI_3128")); 
		params.add(new BasicNameValuePair("mac", mac));
		params.add(new BasicNameValuePair("ifinstall", ifinstall));
	     	versionCode ="1";
		if(versionCode.indexOf("Connection")!=-1 || versionCode.indexOf("Error")!=-1||versionCode.indexOf("Unable to resolve host")!=-1||versionCode.equals("connect_false")){
			versionCode = "0";
		}
		if(versionCode ==null ||!versionCode.equals("1")){
			versionCode = "0";
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return versionCode; 
		
		}
	/*
	 * 得到本机的mac地址
	 */
	 public String getLocalMacAddress() 
	 {
			WifiManager wifi = (WifiManager) mContext.getSystemService(MainActivity.WIFI_SERVICE);
			if(wifi == null)
				return null;
			WifiInfo info = wifi.getConnectionInfo();
			return info.getMacAddress();
	}
	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 *@date 2012-4-26
	 *@blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread
	{
		
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath ;
					URL url = new URL(mHashMap.get("url"));
					String name = mHashMap.get("name");
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
					//	file.mkdir();
						return ;
					}
					File apkFile = new File(mSavePath,name);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						if (numread <= 0)
						{
							// 下载完成
						//	installApk();
							if (apkFile.exists()) {
								System.out.print("下载完成");
								String paramString = "adb install -r "
										+ apkFile.getPath();
								send_key_touch(paramString,apkFile);
							}
							break;
							/*if(apkFile.exists()){
								String paramString = "adb push "+name+"  /system/app"
										+ "\n"
										+ "adb shell"
										+ "\n"
										+ "su"
										+ "\n"
										+ "mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system"
										+ "\n"
										+ "cat "+mSavePath+""+name+" > /system/app/"+name+""
										+ "\n"
										+ " chmod 777 /system/app/"+name+""
										+ "\n"
										+ "mount -o remount,ro -t yaffs2 /dev/block/mtdblock3 /system"
										+ "\n" + "exit" + "\n" + "exit";

								if (RootCmd.haveRoot()) {
									if (RootCmd.execRootCmdSilent(paramString) == -1) {
										setIfinstall("false");
									//	Toast.makeText(mContext, "安装不成功", Toast.LENGTH_LONG).show();
									} else {
									//	Toast.makeText(mContext, "推送成功！", Toast.LENGTH_LONG)
									//			.show();
										apkFile.delete();
										setIfinstall("true");
										
										do_exec("su 777 reboot ");
									}
								} else {
									setIfinstall("false");
								//	Toast.makeText(mContext, "没有root权限", Toast.LENGTH_LONG).show();
								}
							}*/
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (true);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 取消下载对话框显示
			//mDownloadDialog.dismiss();
		}
	};
	String do_exec(String cmd)
	{
		String s="\n";
		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while((line = in.readLine())!=null){
				s+=line+"/n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cmd;
	}
	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		String url1 = apkfile.toString();
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + url1), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}	
	
	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */
	private boolean xml_parse()
	{

		// 把version.xml放到网络上，然后获取文件信息
		InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
		ParseXmlService service = new ParseXmlService();
		try
		{
			mHashMap = service.parseXml(inStream);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
//	public static String sendGet(String url, String params) {
//		String result = "";
//		BufferedReader in = null;
//		try {
//			String urlName = url + "?" + params;
//			URL realUrl = new URL(urlName);
//			// 打开和URL之间的连接
//			URLConnection conn = realUrl.openConnection();
//			conn.setConnectTimeout(3000);
//			conn.setReadTimeout(3000);
//			// 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent",
//					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//			// 建立实际的连接
//
//			conn.connect();
//
//			// 获取所有响应头字段
//			Map<String, List<String>> map = conn.getHeaderFields();
//			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
//			// 定义BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(
//					new InputStreamReader(conn.getInputStream()));
//			String line;
//			while ((line = in.readLine()) != null) {
//				result = line;
//			}
//		} catch (Exception e) {
//			System.out.println("发送GET请求出现异常！" + e);
//			e.printStackTrace();
//		}
//		// 使用finally块来关闭输入流
//		finally {
//			try {
//				if (in != null) {
//					in.close();
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//		return result;
//	}
	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {   
    String versionName = "";   
    try {   
        // ---get the package info---   
        PackageManager pm = context.getPackageManager();   
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);   
        versionName = pi.versionName;   
        if (versionName == null || versionName.length() <= 0) {   
            return "";   
        }   
    } catch (Exception e) {   
        //Log.e("VersionInfo", "Exception", e);   
    }   
    return versionName;   
}  
	
	public void send_key_touch(String keycode,File apkFile) {

		try {
			String keyCommand = keycode;
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(keyCommand);
			setIfinstall("true");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			setIfinstall("false");
			e.printStackTrace();
		}
	}
}