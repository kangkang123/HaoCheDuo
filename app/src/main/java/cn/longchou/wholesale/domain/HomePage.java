package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.List;

import cn.longchou.wholesale.domain.HomePage.Notification;

public class HomePage {

	//banner图片数量
	public int bannerImageCount;
	
	public List<BannerImage> bannerImages;
	
	//Bannder图片
	public class BannerImage{
		
		public String bannerImgUrl;
		
		public String bannerReurl;
	}
	
	//通知的个数
	public int notificationsCount;
	
	public List<Notification> notifications;
	
	//通知
	public class Notification implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		//是否读过
		public boolean isRead;
		//通知的内容
		public String notificationContent;
		//通知的日期
		public String notificationDate;
		//通知的id
		public String notificationId;
		//通知的标题
		public String notificationTitle;
		//优惠活动
		public String notificationType;
		//通知的链接
		public String notificationUrl;
	}
	
	//显示的车子的数量
	public int carCount;
	
	//订单页,订单状态
	public String orderState;
	//订单所需金额
	public String orderSumMoney;
	//剩余时间
	public String remainTime;
	
	//车子的信息列表
	public List<Cars> cars;
	
	//车子的具体信息
	public class Cars implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		//活动类型
		public String carAction;
		//车辆的时间等信息
		public String carDesc;
		//车辆的id
		public Integer carID;
		//车辆图片的地址
		public String carImgURL;
		//车辆功能的描述
		public String carName;
		//车辆的价格
		public String carPrice;
		//车辆定金
		public int carSubscription;
		//车辆类型
		public String carType;
		//是否在购物车中
		public boolean inCart;
		//是否关注
		public boolean isFollow;
		//副标题
		public String subTitle;
	}

	public static com.kang.taobaohead.headview.Notification cloneNotification(
			Notification notification) {
		String content = notification.notificationContent;
		String string = replaceString(content);
		com.kang.taobaohead.headview.Notification no=new com.kang.taobaohead.headview.Notification(notification.isRead, 
				string,
				notification.notificationDate, 
				notification.notificationId, notification.notificationTitle, 
				notification.notificationType, notification.notificationUrl);
		return no;
	}
	
	public static String replaceString(String content)
	{
		String replace = content.replace("\\n", "\n");
		System.out.println("replace:"+replace);
		return replace;
	}
}


