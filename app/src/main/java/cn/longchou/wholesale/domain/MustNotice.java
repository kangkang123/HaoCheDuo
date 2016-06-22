package cn.longchou.wholesale.domain;

import java.util.List;

/**
 * 
* @Description: 办证须知
*
* @author kangkang
*
* @date 2016年2月3日 上午11:29:36 
*
 */
public class MustNotice {

	//补证费
	public List<CompensationFee> bzf;
	
	//车辆办证费
	public List<LicenseFee> clbzf;
	
	//居住证
	public List<ResidencePermit> jzz;
	
	//违章处理
	public List<IllegalDisposal> wzcl;
	
	//车辆年审
	public List<VehiclesExam> clns;
	
	//车牌号码
	public String shortCarPlate;
	
	//办证须知的其他
	public String other;
	
	//补证费
	public class CompensationFee{
		//产证
		public String cz;
		//牌照
		public String pz;
		//区县
		public String qy;
		//行驶证
		public String xsz;
	}
	
	//车辆办证费
	public class LicenseFee{
		//公车费
		public String gcf;
		//过户交易费
		public String ghjyf;
		//快至服务费
		public String kzfwf;
		//区县
		public String qy;
		//转籍交易费
		public String zjjyf;
	}
	
	//车辆年审
	public class VehiclesExam{
		//本地
		public String bd;
		//绿标
		public String lb;
		//地区
		public String qy;
		//上海
		public String sh;
	}
	
	//居住证
	public class ResidencePermit{
		//居住证
		public String jzz;
		//区县
		public String qy;
		//暂快证
		public String zkz;
		//暂慢证
		public String zmz;
	}
	
	public class IllegalDisposal{
		//服务费
		public String fwf;
		//积分处理
		public String kfcl;
		//地区
		public String qy;
		//违章本金
		public String wzbj;
	}
	
}
