package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
* @Description: 金融融资编号
*
* @author kangkang
*
* @date 2016年5月4日 下午5:46:15 
*
 */
public class FinanceInfo {

	public List<String> financeNos;
	public List<MapInfo> lists;
	public static List<List<FinanceInfo>> listFinance=new ArrayList<List<FinanceInfo>>();
	public static List<Boolean> isOpens=new ArrayList<Boolean>();
	
	public String number;
	public String context;
	public boolean isOpen;
	public static int size;
	
	public FinanceInfo(String number,String context) {
		this.number=number;
		this.context=context;
	}
	public FinanceInfo(boolean isOpen) {
		this.isOpen=isOpen;
		
	}
	
	public static void clearData()
	{
		listFinance.clear();
		isOpens.clear();
	}
	
	public FinanceInfo() {
		// TODO Auto-generated constructor stub
	}
	public static void setIsOpenData(int size)
	{
		FinanceInfo.size=size;
		for(int i=0;i<size;i++)
		{
			isOpens.add(false);
		}
	}
	public static void setIsOpen(boolean isOpen,int position)
	{
		for(int i=0;i<size;i++)
		{
			isOpens.remove(position);
			isOpens.add(position, isOpen);
		}
	}
	public static List<Boolean> getIsOpenData()
	{
		return isOpens;
	}
	
	public class MapInfo{
		//申请时间
		public String applyTime;
		//融资编号
		public String financeNo;
		//融资周期
		public String totalMonth;
		//状态
		public String payStatus;
		//融资金额
		public String rzAmount;
		//待还金额
		public String dhAmount;
		//已还金额
		public String yhAmount;
		//每月还款日
		public String monthlyPaydate;
		//每月还款额
		public String monthlyPaymoney;
		

	}
	public List<List<FinanceInfo>> getFinanceChild(String type)
	{
		for(int i=0;i<financeNos.size();i++)
		{
			List<FinanceInfo> list=new ArrayList<FinanceInfo>();
			MapInfo info = lists.get(i);
			if(info.applyTime!=null)
			{
				list.add(new FinanceInfo("申请时间", info.applyTime));
			}
			if(info.financeNo!=null)
			{
				list.add(new FinanceInfo("融资编号", info.financeNo));
			}
			if(info.totalMonth!=null)
			{
				list.add(new FinanceInfo("融资周期", info.totalMonth));
			}
			if(info.payStatus!=null)
			{
				list.add(new FinanceInfo("状态", info.payStatus));
			}
			if(info.rzAmount!=null)
			{
				list.add(new FinanceInfo("融资金额", info.rzAmount));
			}
			if(!"shouche".equals(type))
			{
				if(info.dhAmount!=null)
				{
					list.add(new FinanceInfo("待还金额", info.dhAmount));
				}
				
				if(info.monthlyPaydate!=null)
				{
					list.add(new FinanceInfo("每月还款日", info.monthlyPaydate));
				}
				
				if(info.monthlyPaymoney!=null)
				{
					list.add(new FinanceInfo("每月还款额", info.monthlyPaymoney));
				}
			}
			if(info.yhAmount!=null)
			{
				list.add(new FinanceInfo("已还金额", info.yhAmount));
			}
			listFinance.add(list);
		}
		return listFinance;
	}
	
	
}
