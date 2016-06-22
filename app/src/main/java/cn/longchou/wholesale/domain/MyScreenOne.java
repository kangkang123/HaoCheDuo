package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyScreenOne implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String buy;
	public String choose;
	
	public static List<MyScreenOne> getScreenOne()
	{
		List<MyScreenOne> list=new ArrayList<MyScreenOne>();
		for(int i=0;i<6;i++)
		{
			MyScreenOne news=new MyScreenOne();
			switch (i) {
			case 0:
				news.buy="品牌";
				news.choose="全部";
				list.add(news);
				break;
			case 1:
				news.buy="车型";
				news.choose="全部";
				list.add(news);
				break;
			case 2:
				news.buy="变速箱";
				news.choose="全部";
				list.add(news);
				break;
			case 3:
				news.buy="车龄";
				news.choose="全部";
				list.add(news);
				break;
			case 4:
				news.buy="价格";
				news.choose="全部";
				list.add(news);
				break;
			case 5:
				news.buy="里程";
				news.choose="全部";
				list.add(news);
				break;

			default:
				break;
			}
		}
		return list;
	}
}
