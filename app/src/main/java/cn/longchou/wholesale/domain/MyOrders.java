package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyOrders implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//订单数量
	public int orderCount;
	
	public List<Order> orders;
	
	//订单内容
	public class Order implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		//订单日期
		public String orderDate;
		//订单编号
		public String orderID ;
		//订单状态
		public String orderState;
		//订单金额
		public String orderSumMoney;
	}
}
