package com.kang.taobaohead.headview;

public class Notification {
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
	public Notification(boolean isRead, String notificationContent,
			String notificationDate, String notificationId,
			String notificationTitle, String notificationType,
			String notificationUrl) {
		super();
		this.isRead = isRead;
		this.notificationContent = notificationContent;
		this.notificationDate = notificationDate;
		this.notificationId = notificationId;
		this.notificationTitle = notificationTitle;
		this.notificationType = notificationType;
		this.notificationUrl = notificationUrl;
	}
  
}
