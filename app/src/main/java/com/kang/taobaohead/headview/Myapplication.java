package com.kang.taobaohead.headview;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

public class Myapplication extends Application{

	private static Myapplication instance;

	/** ��Ļ��� */
	public static int screenWidth;
	/** ��Ļ�߶� */
	public static int screenHeight;
	/** ����·�� */
	public static String cachePath;


	public static SharedPreferences sp;
	

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;

		getScreenSize();
		// ��ʼ��ͼƬ����
		// ��ݿ��������
		// �쳣����

		sp = getSharedPreferences("meiya", 0);
		
	}

	public static Context getContext() {
		return instance;
	}

	/**
	 * SD��·��
	 * 
	 * @return String
	 */

	/**
	 * ��ȡ����·��
	 * 
	 * @return String
	 */
	public static String getCachePath() {
		return cachePath;
	}

	/**
	 * ��ȡ��Ļ���
	 * void
	 */
	private void getScreenSize() {

		DisplayMetrics dm = new DisplayMetrics();
		dm = getApplicationContext().getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels; // 1024
		screenHeight = dm.heightPixels; // 720
		float density = dm.density; // 1.0
		float densityDpi = dm.densityDpi; // 160.0
	}
	/**
	 * ͼƬ������
	 */
}
