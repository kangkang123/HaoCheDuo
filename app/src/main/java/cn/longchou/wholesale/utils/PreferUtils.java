package cn.longchou.wholesale.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferUtils {

	public static void putBoolean(Context context,String text,boolean value)
	{
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putBoolean(text, value).commit();
	}
	public static boolean getBoolean(Context context,String text,boolean defValue)
	{
		SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
		return sp.getBoolean(text, defValue);
	}
	
	public static void putString(Context context,String text,String value)
	{
		SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
		sp.edit().putString(text, value).commit();
	}
	public static String getString(Context context,String text ,String defValue)
	{
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(text, defValue);
	}
	public static void putInt(Context context,String text,int value)
	{
		SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
		sp.edit().putInt(text, value).commit();
	}
	public static int getInt(Context context,String text ,int defValue)
	{
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(text, defValue);
	}
}
