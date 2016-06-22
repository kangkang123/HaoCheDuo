package cn.longchou.wholesale.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

/**
 * 本地缓存
 */
public class LocalCacheUtils {

	private static final String LOCAL_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/zhbj_cache";

	/**
	 * 从本地缓存获取图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromLocal(String url) {
		try {
//			String md5 = MD5Encoder.encode(url);
			String md5=MD5Utils.getMd5(url);
			File file = new File(LOCAL_PATH, md5);

			if (file.exists()) {
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
						file));
				return bitmap;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将图片保存在本地
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void putBitmapToLocal(String url, Bitmap bitmap) {
		try {
//			String md5 = MD5Encoder.encode(url);
			String md5=MD5Utils.getMd5(url);
			File file = new File(LOCAL_PATH, md5);

			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}

			FileOutputStream stream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, stream);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
