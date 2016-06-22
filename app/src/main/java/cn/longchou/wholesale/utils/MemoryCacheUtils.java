package cn.longchou.wholesale.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存工具类
 * 
 * @author Kevin
 * 
 */
public class MemoryCacheUtils {

	private LruCache<String, Bitmap> mMemCache;

	public MemoryCacheUtils() {
		long maxMemory = Runtime.getRuntime().maxMemory();// 模拟器默认是16M内存
		Logger.d("最大内存:" + maxMemory);
		mMemCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();// 返回图片大小
			}
		};
	}

	/**
	 * 从内存中取图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromMem(String url) {
		return mMemCache.get(url);
	}

	/**
	 * 向内存中存图片
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void putBitmapToMem(String url, Bitmap bitmap) {
		mMemCache.put(url, bitmap);
	}

}
