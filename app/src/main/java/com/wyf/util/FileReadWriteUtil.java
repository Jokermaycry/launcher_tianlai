package com.wyf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Environment;
import android.util.Log;

/*
 * �ļ���д����
 */
public class FileReadWriteUtil {

	/*
	 * ���SD��Ŀ¼��Ĭ�J���õģ�/mnt/sdcard
	 */
	public static String getSDPath(Context context) {
		String sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory().toString();// ��ȡ��Ŀ¼
		} else {
			// sdDir = context.getFilesDir().getParent();
			sdDir = null;
		}
		return sdDir.toString();

	}
	
	
	/*
	 * ���Ի�ȡ��ǰ����SD��·��
	 * 
	 * ʹ�õ�ʱ���Ƿ��
	 */
	// */mnt/private
	// */mnt/sdcard
	// */mnt/extsd

	public String getOutSDPath() {
		String mount = new String();

		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;

			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;

				if (line.contains("fat")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						mount = mount.concat("*" + columns[1] + "\n");
					}
				} else if (line.contains("fuse")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						mount = mount.concat(columns[1] + "\n");
					}
				}
			}
			Log.i("����SD��·��", mount);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mount;
	}
	
	/*
	 * ��SD���ϴ���Ŀ¼���ļ��У�
	 */

	public static  File creatSDDir(String SDcardpath, String dir) {
		File dirFile = new File(SDcardpath + File.separator+ dir );
		System.out.println(dirFile.mkdirs());
		return dirFile;
	}

	/**
	 * ��sd�����洴���ļ�
	 * 
	 */
	public static  File creatSDFile(String SDcardpath, String dir) {
		File dirFile = new File(SDcardpath + File.separator+ dir );
		try {
			System.out.println(dirFile.createNewFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dirFile;
	}

	
	// �ж�SD���ϵ��ļ����Ƿ����

	public static boolean isFileExist(String SDcardpath, String fileName) {
		File file = new File(SDcardpath + File.separator + fileName);
		return file.exists();
	}
	
	
	/*
	 * ��ȡָ���ļ����������
	 */
	//private static String readPath ="/mnt/usbhost2/address.txt";
	public static String  readFileContent(String readPath){
		if(readPath!=null){
			StringBuffer bufferString = null;
			try {
				FileInputStream fis = new FileInputStream(readPath);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
				String s;
				bufferString = new StringBuffer();
//				int count = 0;
				int i=0;
				while ((s = br.readLine()) != null) {
					bufferString.append(s);
				}
				return bufferString.toString();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
			return null;
	}
	
	/*
	 * ����ָ���ļ���ָ���ļ���
	 * �ɹ�����true
	 */
	/*public boolean copyFileToWhere(Context context,String golePath,String fileName){//Ŀ���ļ��У��ļ���
		try {
			InputStream is=context.getResources().openRawResource(R.raw.XXX);//����ļ�����raw��
			 //���ļ�
			FileOutputStream os = new FileOutputStream(golePath+"/"+fileName);
			  byte[] buffer = new byte[8192];
			  int count = 0;
			  // ���ļ�������Ŀ�ĵ�
			  while ((count = is.read(buffer)) > 0) {
//         д�뼴����
			    os.write(buffer, 0, count);
			  }
			  is.close();
			  os.close();
			  return true;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		return false;
		
	}*/
	
	
	
	/**
	   * ��ȡԴ�ļ�����
	   * @param filename String �ļ�·��
	   * @throws IOException
	   * @return byte[] �ļ�����
	   */
	public static byte[] readFile(String filename) throws IOException {
	    File file =new File(filename);
	    if(filename==null || filename.equals(""))
	    {
	      throw new NullPointerException("��Ч���ļ�·��");
	    }
	    long len = file.length();
	    byte[] bytes = new byte[(int)len];
	    BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
	    int r = bufferedInputStream.read( bytes );
	    if (r != len)
	      throw new IOException("��ȡ�ļ�����ȷ");
	    bufferedInputStream.close();
	    return bytes;
	}
	/**
	   * �����д���ļ�
	   * @param data byte[]
	   * @throws IOException
	   */
	public static void writeFile(byte[] data,String filename) throws IOException {
	    File file =new File(filename);
	    file.getParentFile().mkdirs();
	    BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(file));
	    bufferedOutputStream.write(data);
	    bufferedOutputStream.close();
	}
	/**
	   * ��jar�ļ����ȡclass
	   * @param filename String
	   * @throws IOException
	   * @return byte[]
	   */
	public byte[] readFileJar(String filename) throws IOException {
	    BufferedInputStream bufferedInputStream=new BufferedInputStream(getClass().getResource(filename).openStream());
	    int len=bufferedInputStream.available();
	    byte[] bytes=new byte[len];
	    int r=bufferedInputStream.read(bytes);
	    if(len!=r)
	    {
	      bytes=null;
	      throw new IOException("��ȡ�ļ�����ȷ");
	    }
	    bufferedInputStream.close();
	    return bytes;
	}

	/**
	   * ��ȡ��������Ϊ�˷�ֹ���ĵ����⣬�ڶ�ȡ�����û�н��б���ת�������Ҳ�ȡ�˶�̬��byte[]�ķ�ʽ������е�byte����
	   * @param bufferedInputStream BufferedInputStream
	   * @throws IOException
	   * @return byte[]
	   */
	public byte[] readUrlStream(BufferedInputStream bufferedInputStream) throws IOException {
	    byte[] bytes = new byte[100];
	    byte[] bytecount=null;
	    int n=0;
	    int ilength=0;
	    while((n=bufferedInputStream.read(bytes))>=0)
	    {
	      if(bytecount!=null)
	        ilength=bytecount.length;
	      byte[] tempbyte=new byte[ilength+n];
	      if(bytecount!=null)
	      {
	        System.arraycopy(bytecount,0,tempbyte,0,ilength);
	      }
	      System.arraycopy(bytes,0,tempbyte,ilength,n);
	      bytecount=tempbyte;
	      if(n<bytes.length)
	        break;
	    }
	    return bytecount;
	}
	
	
	
}
