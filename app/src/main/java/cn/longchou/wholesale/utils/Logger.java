package cn.longchou.wholesale.utils;

import android.util.Log;

/**
 * 日志的封装
 * 
 * @author Kevin
 * 
 */
public class Logger {

	public static final int LOG_LEVEL = 6;// 如果想屏蔽所有log,可以设置为0

	public static final int VERBOSE = 5;
	public static final int DEBUG = 4;
	public static final int INFO = 3;
	public static final int WARN = 2;
	public static final int ERROR = 1;

	public static void v(String tag, String msg) {
		if (LOG_LEVEL > VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LOG_LEVEL > DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LOG_LEVEL > INFO) {
			Log.i(tag, msg);
		}

	}

	public static void w(String tag, String msg) {
		if (LOG_LEVEL > WARN) {
			Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LOG_LEVEL > ERROR) {
			Log.e(tag, msg);
		}
	}

	public static void v(String msg) {
		if (LOG_LEVEL > VERBOSE) {
			Log.v(getCallerName(), msg);
		}
	}

	public static void d(String msg) {
		if (LOG_LEVEL > DEBUG) {
			Log.d(getCallerName(), msg);
		}
	}

	public static void i(String msg) {
		if (LOG_LEVEL > INFO) {
			Log.i(getCallerName(), msg);
		}
	}

	public static void w(String msg) {
		if (LOG_LEVEL > WARN) {
			Log.w(getCallerName(), msg);
		}
	}

	public static void e(String msg) {
		if (LOG_LEVEL > ERROR) {
			Log.e(getCallerName(), msg);
		}
	}

	/**
	 * 获取调用者的类名
	 * 
	 * @return
	 */
	public static String getCallerName() {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
		String className = caller.getClassName();// 带有包名信息
		className = className.substring(className.lastIndexOf(".") + 1);

		return className;
	}

}
