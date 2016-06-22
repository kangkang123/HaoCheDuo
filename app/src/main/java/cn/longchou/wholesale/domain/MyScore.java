package cn.longchou.wholesale.domain;

import java.util.List;

/**
 * 
* @Description: 我的页面中的积分
*
* @author kangkang
*
* @date 2016年2月17日 下午12:23:33 
*
 */
public class MyScore {

	//可用积分
	public String avaliable;
	
	//冻结积分
	public String freze;
	
	//积分详情
	public List<ScoreDetail> detail;
	
	//积分详情
	public class ScoreDetail{
		//增（减）量
		public String pointAmount;
		//日期
		public String createTimeStr;
		//增/减积分 （increase、decrease）
		public String typeName;
		//积分增减方式
		public String description;
	}
}
