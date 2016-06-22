package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.List;

import cn.longchou.wholesale.R;

public class FinanceFirst {

	public String title;
	public int id;
	
	public FinanceFirst(String title, int id) {
		this.title=title;
		this.id=id;
	}

	public static List<FinanceFirst> getFinanceFirst(){
		List<FinanceFirst> list=new ArrayList<FinanceFirst>();
		list.add(new FinanceFirst("库存融资方案",R.drawable.banner_stock_financing));
		list.add(new FinanceFirst("收车贷方案",R.drawable.banner_car_loan));
		list.add(new FinanceFirst("消费贷方案",R.drawable.banner_consumer_credi));
		list.add(new FinanceFirst("我要理财",R.drawable.banner_financing));
		return list;
	}
}
