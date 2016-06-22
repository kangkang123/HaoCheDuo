package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BudgetData {

	public static List<String> mBudgetList=new ArrayList<String>();
	
	public static String budget=null;
	
	public static boolean isFirst;
	
	//保存选中的时间
	public static String myBuyTime=null;
		
	
	//制造的数据的类型
	public static List<String> getBudget()
	{
		if(!isFirst){
			mBudgetList.add("5万以下");
			mBudgetList.add("5-10万");
			mBudgetList.add("10-15万");
			mBudgetList.add("15-20万");
			mBudgetList.add("20-30万");
			mBudgetList.add("30-50万");
			mBudgetList.add("50万以上");
		}
		isFirst=true;
		return mBudgetList;
	}
}
