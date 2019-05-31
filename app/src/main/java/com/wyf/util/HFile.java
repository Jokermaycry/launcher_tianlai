package com.wyf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.util.Log;


public class HFile {
	private static final String TAG = "HFile";

	public static void createDipPath(String file) {
		String parentFile = file.substring(0, file.lastIndexOf("/"));
		File file1 = new File(file);
		File parent = new File(parentFile);
		if (!file1.exists()) {
			parent.mkdirs();
			try {
				file1.createNewFile();
				Log.i(TAG,"Create new file :" + file);
			} catch (IOException e) {
			//	HLog.e(TAG, e.getMessage());
			}
		}
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param path
	 */
	public static boolean deleteFile(String path) {
		boolean bl;
		File file = new File(path);
		if (file.exists()) {
			bl = file.delete();
		} else {
			bl = false;
		}
		return bl;
	}

	// �����ļ�
	public static void copyFile(String sourcePath, String toPath) {
		File sourceFile = new File(sourcePath);
		File targetFile = new File(toPath);
		createDipPath(toPath);
		try {
			BufferedInputStream inBuff = null;
			BufferedOutputStream outBuff = null;
			try {
				// �½��ļ����������������л���
				inBuff = new BufferedInputStream(
						new FileInputStream(sourceFile));

				// �½��ļ���������������л���
				outBuff = new BufferedOutputStream(new FileOutputStream(
						targetFile));

				// ��������
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = inBuff.read(b)) != -1) {
					outBuff.write(b, 0, len);
				}
				// ˢ�´˻���������
				outBuff.flush();
			} finally {
				// �ر���
				if (inBuff != null)
					inBuff.close();
				if (outBuff != null)
					outBuff.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �����ļ�
	public static void copyFile(File sourceFile, File targetFile) {

		try {
			BufferedInputStream inBuff = null;
			BufferedOutputStream outBuff = null;
			try {
				// �½��ļ����������������л���
				inBuff = new BufferedInputStream(
						new FileInputStream(sourceFile));

				// �½��ļ���������������л���
				outBuff = new BufferedOutputStream(new FileOutputStream(
						targetFile));

				// ��������
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = inBuff.read(b)) != -1) {
					outBuff.write(b, 0, len);
				}
				// ˢ�´˻���������
				outBuff.flush();
			} finally {
				// �ر���
				if (inBuff != null)
					inBuff.close();
				if (outBuff != null)
					outBuff.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �����ļ���
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// �½�Ŀ��Ŀ¼
		(new File(targetDir)).mkdirs();
		// ��ȡԴ�ļ��е�ǰ�µ��ļ���Ŀ¼
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// Դ�ļ�
				File sourceFile = file[i];
				// Ŀ���ļ�
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// ׼�����Ƶ�Դ�ļ���
				String dir1 = sourceDir + "/" + file[i].getName();
				// ׼�����Ƶ�Ŀ���ļ���
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	/**
	 * 
	 * @param srcFileName
	 * @param destFileName
	 * @param srcCoding
	 * @param destCoding
	 * @throws IOException
	 */
	public static void copyFile(File srcFileName, File destFileName,
			String srcCoding, String destCoding) throws IOException {// ���ļ�ת��ΪGBK�ļ�
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					srcFileName), srcCoding));
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(destFileName), destCoding));
			char[] cbuf = new char[1024 * 5];
			int len = cbuf.length;
			int off = 0;
			int ret = 0;
			while ((ret = br.read(cbuf, off, len)) > 0) {
				off += ret;
				len -= ret;
			}
			bw.write(cbuf, 0, off);
			bw.flush();
		} finally {
			if (br != null)
				br.close();
			if (bw != null)
				bw.close();
		}
	}

	/**
	 * ɾ��Ŀ¼�µ������ļ�
	 * 
	 * @param filepath
	 * @throws IOException
	 */
	public static void del(String filepath) throws IOException {
		File f = new File(filepath);// �����ļ�·��
		if (f.exists() && f.isDirectory()) {// �ж����ļ�����Ŀ¼
			if (f.listFiles().length == 0) {// ��Ŀ¼��û���ļ���ֱ��ɾ��
				f.delete();
			} else {// ��������ļ��Ž����飬���ж��Ƿ����¼�Ŀ¼
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						del(delFile[j].getAbsolutePath());// �ݹ����del������ȡ����Ŀ¼·��
					}
					delFile[j].delete();// ɾ���ļ�
				}
			}
		}
	}

	public static boolean copyTxtToSDCard(Context context, int fileForm,
			String toPath) {
		boolean copyResult = true;
		try {
			// File dir = new File(toPath);
			// ���/sdcard/dictionaryĿ¼�д��ڣ��������Ŀ¼

			// �����/sdcard/dictionaryĿ¼�в�����
			// dictionary.db�ļ������res\rawĿ¼�и�������ļ���
			// SD����Ŀ¼��/sdcard/dictionary��

			// ��÷�װdictionary.db�ļ���InputStream����
			createDipPath(toPath);
			InputStream is = context.getResources().openRawResource(fileForm);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			if (sb != null) {
				OutputStreamWriter fileOutputStream = new OutputStreamWriter(
						new FileOutputStream(new File(toPath)), "GBK");
				fileOutputStream.write(sb.toString());
				fileOutputStream.close();
				copyResult = true;
			} else {
				copyResult = false;
			}
			br.close();
			isr.close();
			is.close();
		} catch (Exception e) {
			copyResult = false;
		}
		return copyResult;
	}

	boolean flag = false;
	File file;

	/*** ��ȡ�ļ���С ***/
	public static long getFileSizes(File f) throws Exception {

		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("�ļ�������");
		}
		return s;
	}

	/*** ��ȡ�ļ��д�С ***/
	/*public static long getFileSize(File f) throws Exception {
		long size = 0;
		List<File> list = IOUtils.listAll(f);
		for (int i = 0; i < list.size(); i++) {
			size += list.get(i).length();
		}
		return size;
	}*/

	public static String FormetFileSize(long fileS) {// ת���ļ���С
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static long getlist(File f) {// �ݹ���ȡĿ¼�ļ�����
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}

	public boolean deleteDirectory(String dirPath) {// ɾ��Ŀ¼���ļ��У��Լ�Ŀ¼�µ��ļ�
		// ���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���
		if (!dirPath.endsWith(File.separator)) {
			dirPath = dirPath + File.separator;
		}
		File dirFile = new File(dirPath);
		// ���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		File[] files = dirFile.listFiles();// ��ô���·���µ������ļ�
		for (int i = 0; i < files.length; i++) {// ѭ������ɾ���ļ����µ������ļ�(������Ŀ¼)
			if (files[i].isFile()) {// ɾ�����ļ�
				flag = deleteFile(files[i].getAbsolutePath());
				System.out.println(files[i].getAbsolutePath() + " ɾ���ɹ�");
				if (!flag)
					break;// ���ɾ��ʧ�ܣ�������
			} else {// ���õݹ飬ɾ����Ŀ¼
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;// ���ɾ��ʧ�ܣ�������
			}
		}
		if (!flag) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * public boolean deleteFile(String filePath) {// ɾ�������ļ� flag = false; file
	 * = new File(filePath); if (file.isFile() && file.exists()) {//
	 * ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ�� file.delete();// �ļ�ɾ�� flag = true; } return flag; }
	 */
/*
	public boolean downloadFile(String url) {
		// ���������ļ�
		int bytesum = 0;
		int byteread = 0;
		URL url = new URL("windine.blogdriver.com/logo.gif");
		try {
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream("c:/abc.gif");

			byte[] buffer = new byte[1204];
			int length;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}*/
	/**
	 * �õ��ļ�������ͼ
	 * @param imgPath
	 * @return
	 */
	public static String getSmallImg (String imgPath ){
		String newImg = imgPath.substring(0, imgPath.lastIndexOf(".")) + "_s"  + imgPath.substring(imgPath.lastIndexOf("."), imgPath.length());
		return newImg;
	}
}
