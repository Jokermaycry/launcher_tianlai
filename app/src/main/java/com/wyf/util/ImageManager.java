package com.wyf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.UUID;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageManager {

	private HashMap<Integer, SoftReference<Bitmap>> mImageCache = new HashMap<Integer, SoftReference<Bitmap>>();
	private Bitmap bmp;

	/*
	 * @auther Robin-wu
	 * 
	 * @param 得到图片ID压缩质量压缩方法：
	 */
	public Drawable getBitmapFromResources(Activity act, int resId) {
		// Resources res = act.getResources();
		SoftReference<Bitmap> reference = mImageCache.get(resId);
		if (reference != null) {
			bmp = reference.get();
			if (bmp != null) {
				BitmapDrawable bd = new BitmapDrawable(bmp);
				System.out.println("调用了软引用");
				return bd;
			}
		}
		// Bitmap bm = compressImage(BitmapFactory.decodeResource(res, resId));
		 compressImageFromFile(act, resId);
		SoftReference<Bitmap> reference2 = mImageCache.get(resId);
		if(reference2 != null){
		     bmp = reference2.get();
		     BitmapDrawable bd = new BitmapDrawable(bmp);
		     return bd;
		}  
	     	if (bmp == null) {  
		     	return null;
	     	}  
	//	BitmapDrawable bd = new BitmapDrawable(bm);
		return null;
	}

	/*
	 * @auther Robin-wu
	 * 
	 * @param 得到图片ID压缩质量压缩方法：
	 */
	public Drawable getBitmapFromResources_mainUI(Activity act, int resId) {
		Resources res = act.getResources();
		SoftReference<Bitmap> reference = mImageCache.get(resId);
		if (reference != null) {
			bmp = reference.get();
			if (bmp != null) {
				BitmapDrawable bd = new BitmapDrawable(bmp);
				System.out.println("调用了软引用");
				return bd;
			}
		}
		// Bitmap bm = compressImage(BitmapFactory.decodeResource(res, resId));
		// Bitmap bm = compressImageFromFile( act,resId);
		Bitmap yasuo = compressImage(BitmapFactory.decodeResource(res, resId));
		mImageCache.put(resId, new SoftReference<Bitmap>(yasuo));
		BitmapDrawable bd = new BitmapDrawable(yasuo);
		return bd;
	}

	/*
	 * @auther Robin-wu
	 * 
	 * @param 得到图片ID压缩分辨率压缩方法：
	 */
	public Bitmap compressImageFromFile(Activity act, int resId) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Resources res = act.getResources();
		// Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
		Bitmap bitmap = BitmapFactory.decodeResource(res, resId, newOpts);

		/*
		 * InputStream is = act.getResources().openRawResource(resId);
		 * BitmapFactory.Options options=new BitmapFactory.Options();
		 * options.inJustDecodeBounds = false; options.inSampleSize = 10;
		 * //width，hight设为原来的十分一 Bitmap btp
		 * =BitmapFactory.decodeStream(is,null,options);
		 */

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 720f;//
		float ww = 1280f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		// bitmap = BitmapFactory.decodeResource(res, resId);
		bitmap = BitmapFactory.decodeResource(res, resId, newOpts);
		// Bitmap yasuo = compressImage(bitmap);
		mImageCache.put(resId, new SoftReference<Bitmap>(bitmap));
		
		return null;// 原来的方法调用了这个方法企图进行二次压缩
						// 其实是无效的,大家尽管尝试
		// return bitmap;
	}

	/*
	 * @auther Robin-wu
	 * 
	 * @param 得到图片ID压缩质量压缩方法：
	 */
	public Bitmap compressImageFromFile2(Activity act, int resId) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Resources res = act.getResources();
		// Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
		Bitmap bitmap = BitmapFactory.decodeResource(res, resId, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 480f;//
		float ww = 800f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		// bitmap = BitmapFactory.decodeResource(res, resId);
		bitmap = BitmapFactory.decodeResource(res, resId, newOpts);
		// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
		return bitmap;
	}

	/*
	 * @auther Robin-wu
	 * 
	 * @param 质量压缩方法：
	 */
	public Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			if (options <= 10) {
				break;
			}
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 20;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/*
	 * @auther Robin-wu
	 * 
	 * @param 图片按比例大小压缩方法（根据路径获取图片并压缩）：
	 */
	public Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/*
	 * @auther Robin-wu
	 * 
	 * @param 图片按比例大小压缩方法（根据Bitmap图片压缩）：
	 */
	public Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 480f;// 这里设置高度为800f
		float ww = 800f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 根据指定的大小压缩图片
	 * 
	 * @param sourceImagePath
	 * @param outDirectory
	 * @return 压缩后的图片路径, 图片不需要压缩则返回原路径
	 */
	public static String compressImage(String sourceImagePath,
			String outDirectory) {
		int maxWidth = 480;
		int maxHeight = 800;
		String compressPath = null;
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(sourceImagePath, ops);
		double ratio = 1.0;
		if (ops.outWidth > ops.outHeight && ops.outWidth > maxWidth) {
			ratio = ops.outWidth / maxWidth;
		} else if (ops.outHeight > ops.outWidth && ops.outHeight > maxHeight) {
			ratio = ops.outHeight / maxHeight;
		} else {
			return sourceImagePath;
		}
		BitmapFactory.Options newOps = new BitmapFactory.Options();
		newOps.inSampleSize = (int) (ratio + 1);
		newOps.outWidth = (int) (ops.outWidth / ratio);
		newOps.outHeight = (int) (ops.outHeight / ratio);
		Bitmap bitmap = BitmapFactory.decodeFile(sourceImagePath, newOps);
		compressPath = outDirectory
				+ UUID.randomUUID()
				+ sourceImagePath.substring(sourceImagePath.lastIndexOf("."),
						sourceImagePath.length());
		File outFile = new File(compressPath);
		try {
			File parent = outFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			outFile.createNewFile();
			OutputStream os = new FileOutputStream(outFile);
			bitmap.compress(CompressFormat.PNG, 100, os);
			os.close();
			bitmap.recycle();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return compressPath;
	}

}
