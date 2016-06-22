package cn.longchou.wholesale.utils;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

/**
 * 三级缓存工具类
 * 
 * @author Kevin
 * 
 */
public class MyBitmapUtils {

	HashMap<String, ImageView> mImageViewMap = new HashMap<String, ImageView>();// 保存ImageView集合

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NetCacheUtils.SUCCESS:
				Bundle data = msg.getData();
				String url = data.getString("url");
				Bitmap bitmap = data.getParcelable("bitmap");
				ImageView ivImage = mImageViewMap.get(url);

				if (ivImage != null) {
					Logger.d("加载图片成功!" + url);
					ivImage.setImageBitmap(bitmap);
				}
				break;
			case NetCacheUtils.FAILED:
				Logger.d("加载图片失败!");
				break;

			default:
				break;
			}
		};
	};

	private MemoryCacheUtils memUtils;// 内存缓存
	private LocalCacheUtils localUtils;// 本地缓存
	private NetCacheUtils netUtils;// 网络缓存

	public MyBitmapUtils() {
		memUtils = new MemoryCacheUtils();
		localUtils = new LocalCacheUtils();
		netUtils = new NetCacheUtils(mHandler, localUtils, memUtils);
	}

	public void display(ImageView ivPhoto, String url) {
		mImageViewMap.put(url, ivPhoto);

		Bitmap bitmap = null;
		// 从内存加载图片
		bitmap = memUtils.getBitmapFromMem(url);
		if (bitmap != null) {
			Logger.d("内存有图片哦.." + url);
			ivPhoto.setImageBitmap(bitmap);
			return;
		}

		// 从本地加载图片
		bitmap = localUtils.getBitmapFromLocal(url);

		if (bitmap != null) {
			Logger.d("本地有图片哦.." + url);
			ivPhoto.setImageBitmap(bitmap);
			memUtils.putBitmapToMem(url, bitmap);// 将图片保存在内存
			return;
		}

		// 从网络加载图片
		Logger.d("网络获取图片.." + url);
		netUtils.getBitmapFromNet(url);
	}

}
