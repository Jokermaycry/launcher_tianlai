package com.csw.tp.util;

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
 * 文件读写操作
 */
public class FileReadWriteUtil {

	/*
	 * 获得SD卡目录，默認內置的，/mnt/sdcard
	 */
	public static String getSDPath(Context context) {
		String sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory().toString();// 获取跟目录
		} else {
			// sdDir = context.getFilesDir().getParent();
			sdDir = null;
		}
		return sdDir.toString();

	}
	
	
	/*
	 * 尝试获取当前外置SD卡路径
	 * 
	 * 使用的时候看是否包含
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
			Log.i("外置SD卡路径", mount);

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
	 * 在SD卡上创建目录（文件夹）
	 */

	public static  File creatSDDir(String SDcardpath, String dir) {
		File dirFile = new File(SDcardpath + dir + File.separator);
		System.out.println(dirFile.mkdirs());
		return dirFile;
	}

	/**
	 * 在sd卡里面创建文件
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
	// 判断SD卡上的文件夹是否存在

	public static boolean isFileExist(String SDcardpath, String fileName) {
		File file = new File(SDcardpath + File.separator + fileName);
		return file.exists();
	}
	
	
	/*
	 * 读取指定文件里面的内容
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
	 * 复制指定文件到指定文件夹
	 * 成功返回true
	 */
	/*public boolean copyFileToWhere(Context context,String golePath,String fileName){//目标文件夹，文件名
		try {
			InputStream is=context.getResources().openRawResource(R.raw.XXX);//如果文件放在raw里
			 //打开文件
			FileOutputStream os = new FileOutputStream(golePath+"/"+fileName);
			  byte[] buffer = new byte[8192];
			  int count = 0;
			  // 将文件拷贝到目的地
			  while ((count = is.read(buffer)) > 0) {
//         写入即拷贝
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
	   * 读取源文件内容
	   * @param filename String 文件路径
	   * @throws IOException
	   * @return byte[] 文件内容
	   */
	public static byte[] readFile(String filename) throws IOException {
	    File file =new File(filename);
	    if(filename==null || filename.equals(""))
	    {
	      throw new NullPointerException("无效的文件路径");
	    }
	    long len = file.length();
	    byte[] bytes = new byte[(int)len];
	    BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));//读取文件夹的数据流
	    int r = bufferedInputStream.read( bytes );
	    if (r != len)
	      throw new IOException("读取文件不正确");
	    bufferedInputStream.close();
	    return bytes;
	}
	/**
	   * 将数据写入文件
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
	   * 从jar文件里读取class
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
	      throw new IOException("读取文件不正确");
	    }
	    bufferedInputStream.close();
	    return bytes;
	}

	/**
	   * 读取网络流，为了防止中文的问题，在读取过程中没有进行编码转换，而且采取了动态的byte[]的方式获得所有的byte返回
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
