package cn.longchou.wholesale.base;

import android.app.Application;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.InputType;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.utils.UIUtils;

import com.baidu.location.service.LocationService;
import com.baidu.location.service.WriteLog;
import com.baidu.mapapi.SDKInitializer;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.model.MessageFormInputModel;
import com.meiqia.meiqiasdk.uilimageloader.UILImageLoader;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;

public class BaseApplication extends Application {
	//获取到主线程的上下文
	private static BaseApplication mContext;
	//获取到主线程的handler
	private static Handler mMainThreadHanler;
	//获取到主线程
	private static Thread mMainThread;
	//获取到主线程的id
	private static int mMainThreadId;
	
	public LocationService locationService;
    public Vibrator mVibrator;
    private PushAgent mPushAgent;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		BaseApplication.mContext = this;
		BaseApplication.mMainThreadHanler = new Handler();
		BaseApplication.mMainThread = Thread.currentThread();
		//获取到调用线程的id
		BaseApplication.mMainThreadId = android.os.Process.myTid();

		//图片查看
		initImageLook();
		
		//友盟推送的内容
//		setYouMeng();

		initMeiqiaSDK();

		MQManager.setDebugMode(true);

		  /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        WriteLog.getInstance().init(); // 初始化日志
        SDKInitializer.initialize(getApplicationContext()); 
	}
	
	public static BaseApplication getApplication(){
		return mContext;
	}
	
	public static Handler getMainThreadHandler(){
		return mMainThreadHanler;
	}
	
	public static Thread getMainThread(){
		return mMainThread;
	}
	
	public static int getMainThreadId(){
		return mMainThreadId;
	}
	
	//图片查看
	public void initImageLook()
	{
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
		.showImageForEmptyUri(R.drawable.ic_launcher) //
		.showImageOnFail(R.drawable.ic_launcher) //
		.cacheInMemory(true) //
		.cacheOnDisk(true) //
		.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
		.Builder(getApplicationContext())//
				.defaultDisplayImageOptions(defaultOptions)//
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs()//
				.build();//
		ImageLoader.getInstance().init(config);
	}

	private void initMeiqiaSDK() {
		MQManager.setDebugMode(true);

		// 替换成自己的key
		String meiqiaKey = "a4bfce9e249d2954f0ca826b767e0841";
		MQConfig.init(this, meiqiaKey, new UILImageLoader(), new OnInitCallback() {
			@Override
			public void onSuccess(String clientId) {
//				Toast.makeText(BaseApplication.this, "init success", Toast.LENGTH_SHORT).show();
//				UIUtils.showToastSafe("success_kang");
//				System.out.print("success_kang");
			}

			@Override
			public void onFailure(int code, String message) {
				Toast.makeText(BaseApplication.this, "int failure message = " + message, Toast.LENGTH_SHORT).show();
				UIUtils.showToastSafe(message+"error_kang");
				System.out.print("message"+message);
			}
		});

		// 可选
//		customMeiqiaSDK();
	}

	private void customMeiqiaSDK() {
		// 配置自定义信息
//        MQConfig.ui.titleGravity = MQConfig.ui.MQTitleGravity.LEFT;
//        MQConfig.ui.backArrowIconResId = android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha;
//        MQConfig.ui.titleBackgroundResId = R.color.test_red;
//        MQConfig.ui.titleTextColorResId = R.color.test_blue;
//        MQConfig.ui.leftChatBubbleColorResId = R.color.test_green;
//        MQConfig.ui.leftChatTextColorResId = R.color.test_red;
//        MQConfig.ui.rightChatBubbleColorResId = R.color.test_red;
//        MQConfig.ui.rightChatTextColorResId = R.color.test_green;
//        MQConfig.ui.robotEvaluateTextColorResId = R.color.test_red;
//        MQConfig.ui.robotMenuItemTextColorResId = R.color.test_blue;
//        MQConfig.ui.robotMenuTipTextColorResId = R.color.test_blue;


		// 自定义留言表单引导文案，配置了该引导文案后将不会读取工作台配置的引导文案
		MQConfig.leaveMessageIntro = "自定义留言表单引导文案";

		// 初始化自定义留言表单字段，如果不配置该选项则留言表单界面默认有留言、邮箱、手机三个输入项
		MQConfig.messageFormInputModels = new ArrayList<MessageFormInputModel>();
		MessageFormInputModel phoneMfim = new MessageFormInputModel();
		phoneMfim.tip = "手机";
		phoneMfim.key = "tel";
		phoneMfim.required = true;
		phoneMfim.hint = "请输入你的手机号";
		phoneMfim.inputType = InputType.TYPE_CLASS_PHONE;

		MessageFormInputModel emailMfim = new MessageFormInputModel();
		emailMfim.tip = "邮箱";
		emailMfim.key = "email";
		emailMfim.required = true;
		emailMfim.hint = "请输入你的邮箱";
		emailMfim.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;

		MessageFormInputModel nameMfim = new MessageFormInputModel();
		nameMfim.tip = "姓名";
		nameMfim.key = "name";
		nameMfim.hint = "请输入你的姓名";
		nameMfim.inputType = InputType.TYPE_CLASS_TEXT;

		MessageFormInputModel customMfim = new MessageFormInputModel();
		customMfim.tip = "自定义";
		customMfim.key = "自定义";
		customMfim.hint = "请输入你的自定义信息";
		customMfim.singleLine = false;
		customMfim.inputType = InputType.TYPE_CLASS_TEXT;


		MQConfig.messageFormInputModels.add(phoneMfim);
		MQConfig.messageFormInputModels.add(emailMfim);
		MQConfig.messageFormInputModels.add(nameMfim);
		MQConfig.messageFormInputModels.add(customMfim);
	}
	
	public void setYouMeng()
	{
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setDebugMode(true);
		
		UmengMessageHandler messageHandler = new UmengMessageHandler(){
			/**
			 * 参考集成文档的1.6.3
			 * http://dev.umeng.com/push/android/integration#1_6_3
			 * */
			@Override
			public void dealWithCustomMessage(final Context context, final UMessage msg) {
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 对自定义消息的处理方式，点击或者忽略
						boolean isClickOrDismissed = true;
						if(isClickOrDismissed) {
							//自定义消息的点击统计
							UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
						} else {
							//自定义消息的忽略统计
							UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
						}
						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
					}
				});
			}
			
			@Override
			public void dealWithNotificationMessage(Context context, UMessage msg) {
				// TODO Auto-generated method stub
				super.dealWithNotificationMessage(context, msg);
			}
		};
		
		mPushAgent.setMessageHandler(messageHandler);

		/**
		 * 自定义通知打开动作
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * 参考集成文档的1.6.2
		 * http://dev.umeng.com/push/android/integration#1_6_2
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
//				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
				openActivity(context, msg);
				
			}
		};
		//使用自定义的NotificationHandler，来结合友盟统计处理消息通知
		//参考http://bbs.umeng.com/thread-11112-1-1.html
		//CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
		
//		if (MiPushRegistar.checkDevice(this)) {
//            MiPushRegistar.register(this, "2882303761517400865", "5501740053865");
//		}
	}
}
