package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyBuy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String buy;
	public String choose;
	
	public static boolean isFirst;
	
	static List<MyBuy> mBuylist=new ArrayList<MyBuy>();
	
	public MyBuy(String buy,String choose) {
		this.buy=buy;
		this.choose=choose;
	}
	
	public static List<MyBuy> getBuys()
	{
		if(!isFirst)
		{
			mBuylist.add(new MyBuy("所在城市","上海"));
			mBuylist.add(new MyBuy("预算","请选择"));
			mBuylist.add(new MyBuy("预计购车时间","请选择时间"));
			mBuylist.add(new MyBuy("品牌","请选择"));
			mBuylist.add(new MyBuy("车系","请选择"));
		}
		isFirst=true;
		return mBuylist;
	}
	
	//根据位置改变选中的值
	public static void setChangeBuy(String buy,int position)
	{
		MyBuy myBuy = mBuylist.get(position);
		mBuylist.remove(position);
		mBuylist.add(new MyBuy(myBuy.buy, buy));
	}

}
