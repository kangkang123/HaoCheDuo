package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

/**
 * 
* @Description: 检查报告
*
* @author kangkang
*
* @date 2016年2月3日 上午9:45:20 
*
 */
public class TestReport {
	
	public String msg;
	
	public List<String> carReportImages;
	
	/*------------------------------------------------------------------*/
	
	public Items items;
	
	//车辆内饰
	public Inside inside;
	
	//车辆外观
	public Outside outside;
	
	//车辆骨架
	public Skeleton skeleton;
	
	//车辆故障
	public Breakdowns breakdowns;
	
	//车辆故障
	public class Breakdowns{
		public String breakdownDesc;
	}
	
	//车辆内饰
	public class Inside{
		public String outsideImageUrl;
		public float 星级;
	}
	
	//车辆外观
	public class Outside{
		public String outsideImageUrl;
		public float 星级;
	}
	//车辆骨架
	public class Skeleton{
		public String numbericImage1;
		public String numbericImage2;
		public float 星级;
		
	}
	
	public class Items{
		public String   右前A柱;
		public String   右前B柱;
		public String   右前C柱;
		public String   右前减震座;
		public String   右前纵梁;
		public String   右后减震座;
		public String   右后纵梁;
		public String   右地板纵梁;
		public String   后围板;
		public String   左前A柱;
		public String   左前B柱;
		public String   左前C柱;
		public String   左前减震座;
		public String   左前纵梁;
		public String   左后减震座;
		public String   左后纵梁;
		public String   左地板纵梁;
	    public String   引擎室防火墙;
	    public String   水箱框架;
	    public String   行李箱底板;
	    
	    public String key;
	    public String value;
	    public Items(String key,String value) {
			this.key=key;
			this.value=value;
		}
	}
	//车辆的具体损伤情况
	public Map<String, String> mechanicals;

	private static TestReport instance;
	
	public static synchronized TestReport getInstance()
	{
		if(instance==null)
		{
			instance = new TestReport();
		}
		return instance;
	}
	public List<Items> getItems(Items items)
	{
		List<Items> list=new ArrayList<Items>();
		if(!TextUtils.isEmpty(items.左前纵梁))
		{
			list.add(new Items("左前纵梁", items.左前纵梁+""));
		}
		list.add(new Items("右前纵梁", items.右前纵梁+""));
		list.add(new Items("左地板纵梁", items.左地板纵梁+""));
		list.add(new Items("右地板纵梁", items.右地板纵梁+""));
		list.add(new Items("左后纵梁", items.左后纵梁+""));
		list.add(new Items("右后纵梁", items.右后纵梁+""));
		list.add(new Items("行李箱底板", items.行李箱底板+""));
		list.add(new Items("水箱框架", items.水箱框架+""));
		list.add(new Items("左前减震座", items.左前减震座+""));
		list.add(new Items("右前减震座", items.右前减震座+""));
		list.add(new Items("引擎室防火墙", items.引擎室防火墙+""));
		list.add(new Items("左前A柱", items.左前A柱+""));
		list.add(new Items("右前A柱", items.右前A柱+""));
		list.add(new Items("左前B柱", items.左前B柱+""));
		list.add(new Items("右前B柱", items.右前B柱+""));
		list.add(new Items("左前C柱", items.左前C柱+""));
		list.add(new Items("右前C柱", items.右前C柱+""));
		list.add(new Items("左后减震座", items.左后减震座+""));
		list.add(new Items("右后减震座", items.右后减震座+""));
		list.add(new Items("后围板", items.后围板+""));
		return list;
	}
	
	public List<Mechanical> getMechanicals(Map<String, String> data)
	{
		List<Mechanical> list=new ArrayList<Mechanical>();
		Iterator<String> it=data.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			String value=data.get(key);
			list.add(new Mechanical(key, value));
		}
		return list;
	}
	
	public class Mechanical{
		public String key;
		public String value;
		public Mechanical(String key,String value) {
			this.key=key;
			this.value=value;
		}
	}
	
	//当没有数据的时候调用
	public List<Items> getTestItems()
	{
		List<Items> list=new ArrayList<Items>();
		list.add(new Items("左前纵梁", "正常"));
		list.add(new Items("右前纵梁", "正常"));
		list.add(new Items("左地板纵梁", "正常"));
		list.add(new Items("右地板纵梁", "正常"));
		list.add(new Items("左后纵梁", "正常"));
		list.add(new Items("右后纵梁", "正常"));
		list.add(new Items("行李箱底板", "正常"));
		list.add(new Items("水箱框架", "正常"));
		list.add(new Items("左前减震座", "正常"));
		list.add(new Items("右前减震座", "正常"));
		list.add(new Items("引擎室防火墙", "正常"));
		list.add(new Items("左前A柱", "正常"));
		list.add(new Items("右前A柱", "正常"));
		list.add(new Items("左前B柱", "正常"));
		list.add(new Items("右前B柱", "正常"));
		list.add(new Items("左前C柱", "正常"));
		list.add(new Items("右前C柱", "正常"));
		list.add(new Items("左后减震座", "正常"));
		list.add(new Items("右后减震座", "正常"));
		list.add(new Items("后围板", "正常"));
		return list;
	}
}
