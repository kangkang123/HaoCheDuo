package cn.longchou.wholesale.service;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.MyFinanceActivity;

import com.umeng.common.message.Log;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

/**
 * Developer defined push intent service. 
 * Remember to call {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}. 
 * @author lucas
 *
 */
//消息的完全自定义处理
//参考文档的1.6.5
//http://dev.umeng.com/push/android/integration#1_6_5
public class MyPushIntentService extends UmengBaseIntentService{
	private static final String TAG = MyPushIntentService.class.getName();

	@Override
	protected void onMessage(Context context, Intent intent) {
		// 需要调用父类的函数，否则无法统计到消息送达
		super.onMessage(context, intent);
		try {
			//可以通过MESSAGE_BODY取得消息体
			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
			UMessage msg = new UMessage(new JSONObject(message));
			Log.d(TAG, "message="+message);    //消息体
			Log.d(TAG, "custom="+msg.custom);    //自定义消息的内容
			Log.d(TAG, "title="+msg.title);    //通知标题
			Log.d(TAG, "text="+msg.text);    //通知内容
			// code  to handle message here
			// ...
			
			// 对完全自定义消息的处理方式，点击或者忽略
			boolean isClickOrDismissed = true;
			if(isClickOrDismissed) {
				//完全自定义消息的点击统计
				UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
				PackageManager pm=getApplicationContext().getPackageManager();
			    Intent intents = new Intent();  
		        intents = pm.getLaunchIntentForPackage("cn.longchou.wholesale");  
	            startActivity(intents); 
			} else {
				//完全自定义消息的忽略统计
				UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
			}
			
			
			
			System.out.println("message:"+message);
			Intent intentActicity=new Intent(context,MyFinanceActivity.class);
			intentActicity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			showNotification(context, msg, intentActicity);
			
			// 使用完全自定义消息来开启应用服务进程的示例代码
			// 首先需要设置完全自定义消息处理方式
			// mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
			// code to handle to start/stop service for app process
			JSONObject json = new JSONObject(msg.custom);
            String topic = json.getString("topic");
            Log.d(TAG, "topic="+topic);
			if(topic != null && topic.equals("appName:startService")) {
				// 在友盟portal上新建自定义消息，自定义消息文本如下
				//{"topic":"appName:startService"}
				if(Helper.isServiceRunning(context, NotificationService.class.getName())) 
					return; 
				Intent intent1 = new Intent(); 
				intent1.setClass(context, NotificationService.class); 
				context.startService(intent1);
			} else if(topic != null && topic.equals("appName:stopService")) {
				// 在友盟portal上新建自定义消息，自定义消息文本如下
				//{"topic":"appName:stopService"}
				if(!Helper.isServiceRunning(context, NotificationService.class.getName())) 
					return; 
				Intent intent1 = new Intent(); 
				intent1.setClass(context, NotificationService.class); 
				context.stopService(intent1);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	public void showNotification(Context context,UMessage msg,Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        Notification mNotification = builder.build();
        mNotification.icon = R.drawable.icon;//notification通知栏图标
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.defaults |= Notification.DEFAULT_VIBRATE ;
        //自定义布局
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_view);
        contentView.setImageViewResource(R.id.notification_large_icon, R.drawable.icon);
        contentView.setTextViewText(R.id.notification_title, msg.title);
        contentView.setTextViewText(R.id.notification_text, msg.text);
        mNotification.contentView = contentView;
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //notifcation.flags = Notification.FLAG_NO_CLEAR;// 永久在通知栏里
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        //使用自定义下拉视图时，不需要再调用setLatestEventInfo()方法，但是必须定义contentIntent
        mNotification.contentIntent = contentIntent;
        mNotificationManager.notify(103, mNotification);
    }
}
