package cn.longchou.wholesale.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.umeng.message.PushAgent;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * 
* @Description: 抽象的activity基类
*
* @author kangkang 
*
* @date 2015年12月21日 下午2:26:36 
*
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener{

	/** 记录处于前台的Activity */
	private static BaseActivity mForegroundActivity = null;
	/** 记录所有活动的Activity */
	private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
	
	public static List<BaseActivity> mActivity=new ArrayList<BaseActivity>();
	
	/** Notification管理 */
	public NotificationManager mNotificationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		PushAgent.getInstance(this).onAppStart();
		initView();
		initData();
		initListener();
	}
	
	    //初始化view
		public abstract void initView();
		
		//初始化数据
		public abstract void initData();
		
		//初始化点击事件监听
		public abstract void initListener();
		
		//点击事件的处理
		public abstract void processClick(View v);

	
	@Override
	public void onClick(View v) {
		processClick(v);
		
	}
	
	/** 关闭所有Activity */
	public static void finishAll() {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			activity.finish();
		}
	}

	/** 关闭所有Activity，除了参数传递的Activity */
	public static void finishAll(BaseActivity except) {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			if (activity != except) activity.finish();
		}
	}

	/** 是否有启动的Activity */
	public static boolean hasActivity() {
		return mActivities.size() > 0;
	}

	/** 获取当前处于前台的activity */
	public static BaseActivity getForegroundActivity() {
		return mForegroundActivity;
	}

	/** 获取当前处于栈顶的activity，无论其是否处于前台 */
	public static BaseActivity getCurrentActivity() {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		if (copy.size() > 0) {
			return copy.get(copy.size() - 1);
		}
		return null;
	}

	/** 推出应用 */
	public void exitApp() {
		finishAll();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BaseActivity.mForegroundActivity = this;
	}

}
