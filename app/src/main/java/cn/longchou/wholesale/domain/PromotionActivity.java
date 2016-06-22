package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 
* @Description: 优惠活动
*
* @author kangkang
*
* @date 2016年2月1日 下午3:31:09 
*
 */
public class PromotionActivity {

	
	public List<Activitys> activitys;
	
	public class Activitys implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		//活动日期
		public String activityDate;
		//活动ID
		public String activityID;
		//活动副标题
		public String activitySubTitle;
		//活动标题
		public String activityTitle;
		//活动链接
		public String activityUrl;
		//活动内容
		public String activityContent;
	}
}
