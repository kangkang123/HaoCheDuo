package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyNews implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String errorMsg;
	
	public List<News> msgs;
	
	public class News implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		//是否已读
		public boolean isRead;
		//消息内容
		public String msgContent;
		//消息日期
		public String msgDate;
		//消息ID
		public String msgID;
		//消息标题
		public String msgTitle;
	}

}
