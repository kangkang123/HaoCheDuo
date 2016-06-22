package cn.longchou.wholesale.domain;

import java.util.List;
import java.util.Map;

/**
 * 
* @Description: 热门活动
*
* @author kangkang
*
* @date 2016年1月27日 下午7:15:19 
*
 */
public class HotActivity {

	//热门活动的数量
	public int actionCount;
	
	public List<HotAction> saleActionWholeSaleTypeList;
	
	//热门活动的具体内容
	public class HotAction{
		//热门活动名称
		public String actionName;
		
	}
}
