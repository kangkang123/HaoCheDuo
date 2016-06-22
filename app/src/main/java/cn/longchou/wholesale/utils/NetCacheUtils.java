package cn.longchou.wholesale.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class NetCacheUtils {

	public static final int SUCCESS = 1;// 下载图片成功
	public static final int FAILED = 2;// 下载图片失败

	Handler mHandler;
	private ExecutorService mThreadPool;
	LocalCacheUtils mLocalUtils;
	MemoryCacheUtils mMemUtils;

	public NetCacheUtils(Handler handler, LocalCacheUtils localUtils,
			MemoryCacheUtils memUtils) {
		mHandler = handler;
		mThreadPool = Executors.newFixedThreadPool(5);// 创建一个容量为5的线程池
		mLocalUtils = localUtils;
		mMemUtils = memUtils;
	}

	/**
	 * 从网络下载图片
	 * 
	 * @param handler
	 * @param url
	 */
	public void getBitmapFromNet(String url) {
		// new InternalThread(url).start();
		mThreadPool.execute(new InternalThread(url));
	}

	class InternalThread extends Thread {

		String url;

		public InternalThread(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			HttpURLConnection con = null;
			try {
				con = (HttpURLConnection) new URL(url).openConnection();
				con.setConnectTimeout(5000);// 连接超时
				con.setReadTimeout(5000);
				con.setRequestMethod("GET");
				con.connect();
				int responseCode = con.getResponseCode();

				if (responseCode == 200) {
					InputStream inputStream = con.getInputStream();

					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					Message msg = Message.obtain();
					msg.what = SUCCESS;

					Bundle data = new Bundle();
					data.putString("url", url);
					data.putParcelable("bitmap", bitmap);
					msg.setData(data);

					mHandler.sendMessage(msg);

					// 将图片保存在本地缓存
					mLocalUtils.putBitmapToLocal(url, bitmap);
					// 将图片保存在内存
					mMemUtils.putBitmapToMem(url, bitmap);
				}

			} catch (Exception e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(FAILED);
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
		}
	}

}
