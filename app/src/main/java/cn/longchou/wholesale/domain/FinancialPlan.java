package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.List;

public class FinancialPlan {

	public String plan;
	public boolean isChoose;
	
	public FinancialPlan() {
		// TODO Auto-generated constructor stub
	}
	
	public FinancialPlan(String plan,boolean isChoose) {
		this.plan=plan;
		this.isChoose=isChoose;
		// TODO Auto-generated constructor stub
	}
	
	public static List<FinancialPlan> getFinancialPlan()
	{
		List<FinancialPlan> list=new ArrayList<FinancialPlan>();
		for(int i=0;i<2;i++)
		{
			if(i==0)
			{
				FinancialPlan plan=new FinancialPlan();
				plan.plan="A 批售融资方案按天计息即时到账";
				plan.isChoose=true;
				list.add(plan);
			}else if(i==1){
				FinancialPlan plan=new FinancialPlan();
				plan.plan="B 不选择融资方案";
				plan.isChoose=false;
				list.add(plan);
			}
		}
		return list;
	}
}
