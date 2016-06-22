package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import cn.longchou.wholesale.global.Constant;

public class StockFinance {

	public String title;
	public String content;
	
	public String 已还金额;
	public String 待还金额;
	public String 状态;
	public String 申请时间;
	public String 融资周期;
	public String 融资编号;
	public String 融资金额;
	
	public StockFinance(String title,String content) {
		this.title=title;
		this.content=content;
	}
	
	
	public static List<StockFinance> geStockFinance()
	{
		String myFinance = Constant.myFinance;
		LoginValidate data = parseData(myFinance);
		List<StockFinance> list=new ArrayList<StockFinance>();
		{
			list.add(new StockFinance("申请时间", "-"));
			list.add(new StockFinance("融资编号", "-"));
			list.add(new StockFinance("融资周期", "-"));
			list.add(new StockFinance("状态", "-"));
			list.add(new StockFinance("融资金额", "0.00元"));
			list.add(new StockFinance("已还金额", "0.00元"));
			list.add(new StockFinance("待还金额", "0.00元"));
		}
		return list;
	}
	
	public static List<StockFinance> getFinanceDetail(String result)
	{
		try {
			List<StockFinance> list=new ArrayList<StockFinance>();
			JSONObject json = new JSONObject(result);
			list.add(new StockFinance("申请时间", json.getString("申请时间")));
			list.add(new StockFinance("融资编号", json.getString("融资编号")));
			list.add(new StockFinance("融资周期", json.getString("融资周期")));
			list.add(new StockFinance("状态", json.getString("状态")));
			list.add(new StockFinance("融资金额", json.getString("融资金额")+"元"));
			list.add(new StockFinance("已还金额", json.getString("已还金额")+"元"));
			list.add(new StockFinance("待还金额", json.getString("待还金额")+"元"));
			return list;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static LoginValidate parseData(String myFinance) {
		Gson gson=new Gson();
		LoginValidate data = gson.fromJson(myFinance,LoginValidate.class);
		if(null!=data)
		{
			return data;
		}
		return null;
	}
	
}
