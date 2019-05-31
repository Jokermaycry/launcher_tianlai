package com.wyf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

public class HImage {
	private static final String TAG = "HImage";
			

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 90;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float angle) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		//final float roundPx = 90;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, angle, angle, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}


	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		//BitmapFactory.decodeFile(filePath, options);
		//options.inSampleSize = calculateInSampleSize(options, 480, 800);
		options.inSampleSize = 2;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);// �ڴ����
	}
	
	public static Bitmap getSmallBitmap(String filePath,int width,int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeFile(filePath, options);// �ڴ����
	}


	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

/*	public static String bitmapToString(String filePath) {

		Bitmap bm = getSmallBitmap(filePath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);

	}*/


/*	public static String compressImage(String imagePath, String saveDir) {
		String compressPath = null;
		Bitmap bitmap = getSmallBitmap(imagePath);
		if (bitmap != null) {
			compressPath = saveDir+ UUID.randomUUID()+ imagePath.substring(imagePath.lastIndexOf("."),imagePath.length());
			saveBitmap(bitmap, compressPath);
		}
		return compressPath;

	}*/


	@SuppressLint("NewApi")
	public static void saveBitmap(Bitmap bitmap, String imagePath,String imageType) {
		try {
			File file = new File(imagePath);
			HFile.createDipPath(imagePath);
			FileOutputStream fOut = new FileOutputStream(file);
			if("PNG".equalsIgnoreCase(imageType)){
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			}else if("JPG".equalsIgnoreCase(imageType) || "JPEG".equalsIgnoreCase(imageType)){
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			}else  if("WEBP".equalsIgnoreCase(imageType) ){
				bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fOut);
			}else{
			//	HLog.w(TAG, "ͼƬ����ʧ�ܣ��޷�ȷ��ͼƬ���͡�����Ϊ��" + imageType);
			}
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
		//	HLog.e(TAG, e.toString());
		} catch (IOException e) {
			//HLog.e(TAG, e.toString());
		}
		
	}
	

	@SuppressLint("NewApi")
	public static void saveBitmap(Bitmap bitmap, String imagePath,int s) {
		File file = new File(imagePath);
		HFile.createDipPath(imagePath);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.WEBP, s, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static  String compressImage(String sourceImagePath, String outDirectory) {
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
		compressPath = outDirectory+ UUID.randomUUID()+ sourceImagePath.substring(sourceImagePath.lastIndexOf("."),sourceImagePath.length());
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
	

	public static Bitmap getBitmap(String url, String localPath) {
		Bitmap bitmap = null;
		File file = new File(localPath);
		if (file.exists()) {
			try {
				FileInputStream fs = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(fs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			bitmap = getHttpBitmap(url);
		}
		return bitmap;

	}
	

	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);


			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();

			conn.setConnectTimeout(6000);

			conn.setDoInput(true);

			conn.setUseCaches(false);

			InputStream is = conn.getInputStream();
			// �����õ�ͼƬ
			bitmap = BitmapFactory.decodeStream(is);
			// �ر�������
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;

	}
	

	public static Bitmap getLocalBitMap(String localPath) {
		System.out.println("localPath:" + localPath);
		File file = new File(localPath);
		Bitmap bitmap = null;
		if (file.exists()) {
			try {
				FileInputStream fs = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(fs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		return bitmap;
	}
	
	/**
	 * ���Ʒ���ͼƬ������Ŀ¼
	 * 
	 * @param context
	 */
	public static boolean copyAppFileToSDCard(Context context, int imgForm,
			String toPath) {
		boolean copyResult = true;
		try {

			HFile.createDipPath(toPath);
			InputStream is = context.getResources().openRawResource(imgForm);
			FileOutputStream fos = new FileOutputStream(new File(toPath));
			byte[] buffer = new byte[8192];
			int count = 0;
			// ��ʼ����dictionary.db�ļ�
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
			// setTitle("copy DB");

		} catch (Exception e) {
			copyResult = false;
		}
		return copyResult;
	}
	
	/**
	 * ����ͼƬѹ����ָ��Ŀ¼
	 * @param images
	 * @param compressDir
	 * @return
	 */
	public static ArrayList<String> compressImage(List<String> images,String compressDir){
		if(images == null)
			return null;
		//ͼƬ����ϴ���С��300KB
		int maxSize = 1000*1024;
		ArrayList<String> resultImage = new ArrayList<String>();
		for (int i = 0; i < images.size(); i++) {
			String imagePath = images.get(i);
			File file = new File(imagePath);
			if(file.exists()){
				long length = file.length();
				//int compressPercent = 100;
				if(length > maxSize){
					BitmapFactory.Options newOpts = new BitmapFactory.Options();  
			        //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��  
			        newOpts.inJustDecodeBounds = true;  
			        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,newOpts);//��ʱ����bmΪ��  
			        newOpts.inJustDecodeBounds = false;  
			        int w = newOpts.outWidth;  
			        int h = newOpts.outHeight;  
			        //���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ  
			        float hh = 800f;//�������ø߶�Ϊ800f  
			        float ww = 480f;//�������ÿ��Ϊ480f  
			        //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��  
			        int be = 1;//be=1��ʾ������  
			        if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����  
			            be = (int) (newOpts.outWidth / ww);  
			        } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����  
			            be = (int) (newOpts.outHeight / hh);  
			        }  
			        if (be <= 0)  
			            be = 1;  
			        newOpts.inSampleSize = be;//�������ű���  
			        //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��  
			        bitmap = BitmapFactory.decodeFile(imagePath, newOpts);  
					
					//����ͼƬ
					String fileName  = file.getName();
					String newFileName = null;
					if(fileName.indexOf(".") > 0)
						newFileName = compressDir + UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."),fileName.length());
					else
						newFileName = compressDir + UUID.randomUUID().toString() +".jpg";
					
					//String saveImagePath =compressDir +file.getName();
					File saveImagefile = new File(newFileName);
					HFile.createDipPath(newFileName);
					FileOutputStream fOut = null;
					try {
						fOut = new FileOutputStream(saveImagefile);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					file = new File(imagePath);
					length = file.length();
					//compressPercent =(int)(length /maxSize);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					try {
						fOut.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					resultImage.add(newFileName);
				}else{
					String strNewName = compressDir + UUID.randomUUID().toString() + imagePath.substring(imagePath.lastIndexOf("."), imagePath.length());
					HFile.copyFile(imagePath, strNewName);
					resultImage.add(strNewName);
				}
			}
		}
		return resultImage;
	}
/*
 * ѹ��ͼƬ
 */
	public static Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if( baos.toByteArray().length / 1024> 512) {//�ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���	
			baos.reset();//����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//����ѹ��50%����ѹ��������ݴ�ŵ�baos��
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
		float hh = 800f;//�������ø߶�Ϊ800f
		float ww = 480f;//�������ÿ��Ϊ480f
		//���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
		int be = 1;//be=1��ʾ������
		if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//�������ű���
		//���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��
	}
	
	
	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//��ʱ����bmΪ��
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
		float hh = 800f;//�������ø߶�Ϊ800f
		float ww = 480f;//�������ÿ��Ϊ480f
		//���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
		int be = 1;//be=1��ʾ������
		if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//�������ű���
		//���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��
	}
	
	public static Bitmap compressImage(Bitmap image) {
		try{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {	//ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��		
			baos.reset();//����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��
			options -= 10;//ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ
		return bitmap;
		}catch(Exception e){
			return null;
		}
	}
	
	
	/*
	 * ��תͼƬ
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		if(bitmap == null)
			return null;
		// ��תͼƬ ����
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// �����µ�ͼƬ
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	
	/**
	 * ��ȡͼƬ���ԣ���ת�ĽǶ�
	 * 
	 * @param path
	 *            ͼƬ����·��
	 * @return degree��ת�ĽǶ�
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	/**
	 * ��ȡ���ص�ͼƬ�õ�����ͼ����ͼƬ��Ҫ��ת����ת��
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static  Bitmap getLocalThumbImg(String path,float width,float height){
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path,newOpts);//��ʱ����bmΪ��
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
		int be = 1;//be=1��ʾ������
		if (w > h && w > width) {//�����ȴ�Ļ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / width);
		} else if (w < h && h > height) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / height);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//�������ű���
		//���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		bitmap = BitmapFactory.decodeFile(path, newOpts);
		bitmap = compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��
		int degree = readPictureDegree(path);
		bitmap = rotaingImageView(degree, bitmap);
		return bitmap;
	}
	
}
