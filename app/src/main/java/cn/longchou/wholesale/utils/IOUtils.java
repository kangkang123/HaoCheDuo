package cn.longchou.wholesale.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
	
	/**
	 * 读取输入流,转成字符串返回
	 * 
	 * @param in
	 *            输入流
	 * @return 读取后的字符串
	 * @throws IOException
	 */
	public static String readFromStream(InputStream in) throws IOException {
		if (in != null) {
			ByteArrayOutputStream out = null;
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			String result = out.toString();
			in.close();
			out.close();

			return result;
		}
		return "";
	}
	
	/**
	 * 打开Asset下的文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static InputStream openAssetFile(Context context, String fileName) {
		AssetManager am = context.getAssets();
		InputStream is = null;
		try {
			is = am.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
}
