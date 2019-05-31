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
import java.util.HashMap;
import java.util.List;
import java.util.Map;






import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.Context;
/**
 *@author coolszy
 *@date 2012-4-26
 *@blog http://blog.92coding.com
 */
public class update_main 
{
	
	/* ���������XML��Ϣ */
	HashMap<String, String> mHashMap;
	/* ���ر���·�� */
	private String mSavePath;

	private Context mContext;
	private Handler handler;                  // ����handler
	
	public static String versionCode="0";

	public  update_main(Context context)
	{
		mContext = context;
		// ��ʼ��handler
		//�����̵߳�ʱ�򣬶Ի��������߳����ܡ���֪��Ϊʲô��Ӧ������
		//�Ի���ֻ�������߳����ܰɣ�
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
	           if(msg.what == 1) // handler���յ���ص���Ϣ��
	           {
	   			UpdateManager manager = new UpdateManager(mContext);
	   			// ����������
	   			manager.checkUpdate("version.xml");
	           }
	           if(msg.what == 2) // handler���յ���ص���Ϣ��
	           {
	   			UpdateManager manager = new UpdateManager(mContext);
	   			// ����������
	   			manager.checkUpdate("tengxun_version.xml");
	           }
	           
	           if(msg.what == 3) // handler���յ���ص���Ϣ��
	           {
	   			UpdateManager manager = new UpdateManager(mContext);
	   			// ����������
	   			manager.checkUpdate("taijie_version.xml");
	           }
	           
	           if(msg.what == 4) // handler���յ���ص���Ϣ��
	           {
	   			UpdateManager manager = new UpdateManager(mContext);
	   			// ����������
	   			manager.checkUpdate("shichang_version.xml");
	           }
	
			}
		};
	}
	public void update()
	{
		//wjz û�����磬��ֱ���˳�
		if (!NetCheck.checkNetWorkStatus(mContext))
			return;
		xml_parse();
		String ver = getAppVersionName(mContext);
		// ��ȡ��վ�������Ϣ,���û�ж�Ӧ�ģ��͸ɵ�
		String url_res = sendGet("http://112.124.43.99/apk/yahao/tianlaiyouyang_7cun_ver.txt",null);
		if(url_res == "")
			return;
		 else
		{
			int len = url_res.indexOf("."); 
			
//			url_res= url_res.substring(len-1, url_res.length());
			versionCode=url_res.substring(len-1, url_res.length());
		}
		//stringתdouble
		double ver_dou = Double.parseDouble(ver);
		double http_dou =  Double.parseDouble(versionCode);

		//������汾���ڱ��ذ汾����Ҫ���и���
		if (http_dou>ver_dou) 
		{
			handler.sendEmptyMessage(1);
		}
	}
	
	
	public  void update_tengxun()
	{
		// ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ
		InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("tengxun_version.xml");
		// ����XML�ļ��� ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
		ParseXmlService service = new ParseXmlService();
		try
		{
			mHashMap = service.parseXml(inStream);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		handler.sendEmptyMessage(2);
		
	}
	public  void update_taijie()
	{
		// ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ
		InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("taijie_version.xml");
		// ����XML�ļ��� ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
		ParseXmlService service = new ParseXmlService();
		try
		{
			mHashMap = service.parseXml(inStream);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		handler.sendEmptyMessage(3);
		
	}
	public  void update_anzhuoshichang()
	{
		// ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ
		InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("shichang_version.xml");
		// ����XML�ļ��� ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
		ParseXmlService service = new ParseXmlService();
		try
		{
			mHashMap = service.parseXml(inStream);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		handler.sendEmptyMessage(4);
		
	}
	/**
	 * ����apk�ļ�
	 */
	private void downloadApk()
	{
		// �������߳��������
		new downloadApkThread().start();
	}

	/**
	 * �����ļ��߳�
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
				// �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// ��ô洢����·��
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// ��������
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// ��ȡ�ļ���С
					int length = conn.getContentLength();
					// ����������
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// �ж��ļ�Ŀ¼�Ƿ����
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// ����
					byte buf[] = new byte[1024];
					// д�뵽�ļ���
					do
					{
						int numread = is.read(buf);
						count += numread;
						if (numread <= 0)
						{
							// �������
							installApk();
							break;
						}
						// д���ļ�
						fos.write(buf, 0, numread);
					} while (true);// ���ȡ���ֹͣ����.
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
			// ȡ�����ضԻ�����ʾ
			//mDownloadDialog.dismiss();
		}
	};

	/**
	 * ��װAPK�ļ�
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		String url1 = apkfile.toString();
		if (!apkfile.exists())
		{
			return;
		}
		// ͨ��Intent��װAPK�ļ�
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + url1), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}	
	
	/**
	 * �������Ƿ��и��°汾
	 * 
	 * @return
	 */
	private boolean xml_parse()
	{

		// ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ
		InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
		// ����XML�ļ��� ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
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
	 * ��ָ��URL����GET����������
	 * 
	 * @param url
	 *            ���������URL
	 * @param params
	 *            ��������������Ӧ����name1=value1&name2=value2����ʽ��
	 * @return URL����Զ����Դ����Ӧ
	 */
	public static String sendGet(String url, String params) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + params;
			URL realUrl = new URL(urlName);
			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// ����ʵ�ʵ�����
			conn.connect();
			// ��ȡ������Ӧͷ�ֶ�
			Map<String, List<String>> map = conn.getHeaderFields();
			// �������е���Ӧͷ�ֶ�
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر�������
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * ��ȡ�汾��
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
}