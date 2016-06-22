package cn.longchou.wholesale.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * ============================================================
 * 
 * 版权 ：安博教育集团 版权所有 (c) 2013
 * 
 * 作者:mwqi
 * 
 * 版本 ：1.0
 * 
 * 创建日期 ： 2013-1-18 上午1:05:05
 * 
 * 描述 ：
 * 
 *     图片缓存类，封装了网络获取图片，本地缓存，内存缓存
 * 修订历史 ：
 * 
 * ============================================================
 **/

public class ImageUtil {
	private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	/**
	 * JPG图片缓存
	 * 
	 * @param imagePath
	 * @param bitmap
	 * @throws IOException
	 */
	public static void saveImageJpeg(String imagePath, Bitmap bitmap)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		byte[] b = bos.toByteArray();
		saveImage(imagePath, b);
		bos.flush();
		bos.close();
	}

	/**
	 * PNG图片缓存
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */

	public static void saveImagePng(String imagePath, Bitmap bitmap)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] b = bos.toByteArray();
		saveImage(imagePath, b);
		bos.flush();
		bos.close();
	}

	/**
	 * 缓存图片
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */
	public static void saveImage(String imagePath, byte[] buffer)
			throws IOException {
		File f = new File(imagePath);
		if (f.exists()) {
			return;
		} else {
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(imagePath);
			fos.write(buffer);
			fos.flush();
			fos.close();
		}
	}

	/**
	 * 从本地获取图片
	 * 
	 * @param imagePath
	 * @return Bitmap
	 */
	public static Bitmap getImageFromLocal(String imagePath) {
		File file = new File(imagePath);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			file.setLastModified(System.currentTimeMillis());
			return bitmap;
		}
		return null;
	}

	/**
	 * 从网络获取图片并缓存
	 * 
	 * @return Bitmap
	 * @throws IOException
	 */
	public Bitmap loadImage(final String imagePath, final String imgUrl,
			final boolean isbusy, final ImageCallback callback) {
		callback.onStart();
		Bitmap bitmap = null;
		if (imageCache.containsKey(imgUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imgUrl);
			bitmap = softReference.get();
			if (bitmap != null) {
				// System.out.println("从软应用获取图片");
				return bitmap;
			}
		}
		// if(!isbusy){
		// 从本地获取图片
		bitmap = getImageFromLocal(imagePath);
		// System.out.println("从本地获取图片");
		// }
		if (bitmap != null) {
			imageCache.put(imgUrl, new SoftReference<Bitmap>(bitmap));
			// System.out.println("向软应用储存图片");
			return bitmap;
		} else {// 从网络获取图片
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.obj != null) {
						Bitmap bitmap = (Bitmap) msg.obj;
						imageCache.put(imgUrl,
								new SoftReference<Bitmap>(bitmap));
						if (android.os.Environment.getExternalStorageState()
								.equals(android.os.Environment.MEDIA_MOUNTED)) {
							try {
								if (imgUrl.endsWith(".png")) {
									saveImagePng(imagePath, bitmap);
								} else if (imgUrl.endsWith(".jpg")) {
									saveImageJpeg(imagePath, bitmap);
								}
								// System.out.println("向SD卡获取图片");

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						callback.loadImage(bitmap, imagePath);

					}
				}
			};

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {

						Bitmap bitmap = null;
						if (imgUrl != null && !"".equals(imgUrl)) {
							byte[] b = getUrlBytes(imgUrl);
							/*
							 * BitmapFactory.Options opts = new
							 * BitmapFactory.Options(); opts.inJustDecodeBounds
							 * = true;
							 * BitmapFactory.decodeStream(getUrlInputStream
							 * (imgUrl), null, opts);
							 * 
							 * opts.inSampleSize = computeSampleSize(opts, -1,
							 * 128*128); opts.inJustDecodeBounds = false; try {
							 * bitmap =
							 * BitmapFactory.decodeStream(getUrlInputStream
							 * (imgUrl), null, opts); } catch (OutOfMemoryError
							 * err) { }
							 */
							bitmap = BitmapFactory.decodeByteArray(b, 0,
									b.length);
							// Bitmap bitmap = BitmapFactory.decodeStream(bis);
						}
						Message msg = handler.obtainMessage();
						msg.obj = bitmap;
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						callback.onFailed();
					}
				}
			};
			ThreadPoolManager.getInstance().addTask(runnable);
		}
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 获取指定路径的Byte[]数据-通用
	 * 
	 * @param urlpath
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getUrlBytes(String urlpath) throws Exception {
		InputStream in_s = getUrlInputStream(urlpath);
		return readStream(in_s);
	}

	/**
	 * 获取指定路径的InputStream数据-通用
	 * 
	 * @param urlpath
	 * @return byte[]
	 * @throws Exception
	 */
	public static InputStream getUrlInputStream(String urlpath)
			throws Exception {
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestMethod("GET");
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(15*1000);// 10秒超时
		int responseCode = conn.getResponseCode();
		Log.i("image", responseCode + "");
		System.out.println(responseCode + "");
		if (responseCode == HttpURLConnection.HTTP_OK) {// 返回码200等于返回成功
			InputStream inputStream = conn.getInputStream();
			return inputStream;
		}
		return null;
	}

	/**
	 * 从InputStream中读取数据-通用
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[128];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		inStream.close();
		return outstream.toByteArray();
	}

	/**
	 * 获取缓存路径
	 * 
	 * @return sd卡路径
	 */
	public static String getCacheImgPath() {
		return Environment.getExternalStorageDirectory().getPath()
				+ "/ambow/cache/";
	}

	/**
	 * 类功能描述：给网络上获取的图片设置的回调
	 * 
	 * @author mwqi
	 * @version 1.0 </p> 修改时间：</br> 修改备注：</br>
	 */
	public interface ImageCallback {
		public void onStart();
		public void loadImage(Bitmap bitmap, String imagePath);
		public void onFailed();
	}

}
