package cn.longchou.wholesale.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

/**
 * 短信备份工具类
 * 
 * @author Kevin
 * 
 */
public class SmsUtils {

	/**
	 * 注意权限: <uses-permission android:name="android.permission.READ_SMS"/>
	 * <uses-permission android:name="android.permission.WRITE_SMS"/>
	 * 
	 * @param ctx
	 * @param pbProgress
	 * @param mProgressDialog 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 */
	public static void smsBackup(Context ctx, File output,
			SmsBackupCallback callback) throws Exception {
		Uri uri = Uri.parse("content://sms/");// 所有短信
		Cursor cursor = ctx.getContentResolver().query(uri,
				new String[] { "address", "date", "type", "body" }, null, null,
				null);

		XmlSerializer serializer = Xml.newSerializer();// 初始化xml序列化工具
		serializer.setOutput(new FileOutputStream(output), "utf-8");
		/*
		 * startDocument(String encoding, Boolean standalone)encoding代表编码方式
		 * standalone 用来表示该文件是否呼叫其它外部的约束文件。 若值是 ”yes” 表示没有呼叫外部规则文件，若值是 ”no”
		 * 则表示有呼叫外部规则文件。默认值是 “yes”。
		 */
		serializer.startDocument("utf-8", null);// 生成xml顶栏描述语句<?xml
												// version="1.0"
												// encoding="utf-8"?>
		serializer.startTag(null, "smss");// 起始标签

		callback.preSmsBackup(cursor.getCount());
		int progress = 0;
		while (cursor.moveToNext()) {
			serializer.startTag(null, "sms");

			serializer.startTag(null, "address");// 起始标签
			String address = cursor.getString(0);
			serializer.text(address);// 设置内容
			serializer.endTag(null, "address");// 结束标签

			serializer.startTag(null, "date");
			String date = cursor.getString(1);
			serializer.text(date);// 设置内容
			serializer.endTag(null, "date");

			serializer.startTag(null, "type");
			String type = cursor.getString(2);
			serializer.text(type);// 设置内容
			serializer.endTag(null, "type");

			serializer.startTag(null, "body");
			String body = cursor.getString(3);
			serializer.text(body);// 设置内容
			serializer.endTag(null, "body");

			serializer.endTag(null, "sms");

			Thread.sleep(500);//为了方便看效果,故意延时1秒钟
			
			progress++;
			callback.onSmsBackup(progress);
		}

		serializer.endTag(null, "smss");// 结束标签
		serializer.endDocument();// 结束xml文档

		cursor.close();
	}
	
	/**
	 * 短信备份回调接口
	 * @author Kevin
	 *
	 */
	public interface SmsBackupCallback {
		/**
		 * 备份之前获取短信总数
		 * @param total
		 */
		public void preSmsBackup(int total);
		/**
		 * 备份过程中实时获取备份进度
		 * @param progress
		 */
		public void onSmsBackup(int progress);
	}
}
