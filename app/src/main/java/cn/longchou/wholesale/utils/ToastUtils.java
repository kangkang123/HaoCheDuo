package cn.longchou.wholesale.utils;

import android.content.Context;
import android.widget.Toast;
/**
 * 
 * 作者：康曼曼
 * 
 * 时间：2015-9-16
 * 
 * 描述：吐司工具类
 *
 */
public class ToastUtils {

	public static void showToast(Context context,String body)
	{
		Toast.makeText(context, body, 0).show();
	}
}
