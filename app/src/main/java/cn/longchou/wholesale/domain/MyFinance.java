package cn.longchou.wholesale.domain;
/**
 * 
* @Description: 我的金融的内容
*
* @author kangkang
*
* @date 2016年2月17日 上午10:34:03 
*
 */
public class MyFinance {

	//是否有库存融资贷
	public boolean hasKuncunDai;
	//是否有收车贷
	public boolean hasShouCheDai;
	//是否有消费贷
	public boolean hasXiaoFeiDai;
	//是否授信
	public boolean isTrust;
	
	//库存贷信息
	public KunCunDaiInfo kunCunDaiInfo;
	
	//收车贷信息
	public ShouCheDaiInfo shouCheDaiInfo;
	
	//消费贷信息
	public XiaoFeiDaiInfo xiaoFeiDaiInfo;
	
	//授信信息
	public TrustInfo trustInfo;
	
    //库存贷信息
	public class KunCunDaiInfo{
		
	    //库存融资金额
		public String kunCunDai;
		//库存融资待还
		public String kunCunDaiHuan;
		//库存id
		public String kunCunDaiId;
	}
	
    //收车贷信息
	public class ShouCheDaiInfo{
		
		//收车贷金额
		public String shouCheDai;
		
		//收车贷待还金额
		public String shouCheDaiHuan;
		//收车id
		public String shouCheDaiId;
	}
	
	public class XiaoFeiDaiInfo{
		
		public String remainMonth;
		public String toPay;
		public String totalLimit;
		public String totalMonth;
		public String id;
	}
	
	//授信信息
	public class TrustInfo{
		//可用余额
		public String available;
		//授信到期日期
		public String limitDate;
		//授信额度
		public String trustTotal;
	}
	
}
