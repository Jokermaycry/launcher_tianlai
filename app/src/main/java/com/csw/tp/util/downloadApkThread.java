package com.csw.tp.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import com.csw.tp.cc_server.ServerThread;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;

/***
 * 下载静默安装
 * 
 * */

public class downloadApkThread extends Thread {

	
  private String downloadUrl;
  private String downloadName;
  private Context mContext;
  private ServerThread mserverThread;
   public downloadApkThread(String downloadUrl, String downloadName,Context context,ServerThread serverThread) {
		super();
		this.downloadUrl = downloadUrl;
		this.downloadName = downloadName;
		this.mContext=context;
		this.mserverThread=serverThread;
	}
  
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try { 
			  System.out.println("进入下载中,完成之后静默安装");
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String sdpath = Environment.getExternalStorageDirectory()
						+ "/";
				String mSavePath = sdpath + "download";
				URL url = new URL(downloadUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				File file = new File(mSavePath);
				if (!file.exists()) {
					file.mkdir();
				}
				File apkFile = new File(mSavePath,downloadName);
				FileOutputStream fos = new FileOutputStream(apkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					if (numread <= 0) {
						if (apkFile.exists()) {
							if(mContext!=null){
							System.out.println("后台下载升级用的APK成功");
							}
							boolean value = slientInstall(apkFile);
							if (value) {
								System.out.println("111111后台下载升级用的APK成功");
							//	mserverThread.sendappupdate();
							} else {
								System.out.println("2222222后台下载升级用的APK成功");
							}
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (true);
				fos.close();
				is.close();
				mserverThread.downflag=false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//downflag=false;
		//	android.os.Process.killProcess(android.os.Process.myPid());
		}
		mserverThread.downflag=false;
	}
		
	
	/**
	 * 静默安装
	 * 
	 * @param file
	 * @return
	 */
	public static boolean slientInstall(File file) {
		boolean result = false;
		Process process = null;
		OutputStream out = null;
		try {
			process = Runtime.getRuntime().exec("su");
			out = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			dataOutputStream.writeBytes("chmod 777 " + file.getPath() + "\n");
			dataOutputStream
					.writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r "
							+ file.getPath());
			// 提交命令
			dataOutputStream.flush();
			// 关闭流操作
			dataOutputStream.close();
			out.close();
			int value = process.waitFor();

			// 代表成功
			if (value == 0) {
				result = true;
			} else if (value == 1) { // 失败
				result = false;
			} else { // 未知情况
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	
	
}
