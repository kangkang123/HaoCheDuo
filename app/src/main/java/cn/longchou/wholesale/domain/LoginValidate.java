package cn.longchou.wholesale.domain;

import java.io.Serializable;

/**
 * 
* @Description: 登录的json数据
*
* @author kangkang
*
* @date 2016年1月26日 下午3:02:53 
*
 */
public class LoginValidate implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//	积分 score
//	余额 balance
//	是否认证 isCertified
//	“name”:真实姓名（若未线下认证则为空）
//	“idNo”: 身份证号码（若未线下认证则为空）
//	errorMsg:空表示成功；“该手机号尚未注册，请先注册”，“密码错误，请重新输入”，“服务器内部错误”；……..。
    	public String Token;
		public float balance;
		public boolean isCertified;
		public String errorMsg;
		public int score;
		public String idNo;
		public String name;
		public String phoneNumber;
		
		@Override
		public String toString() {
			return "LoginValidate [Token=" + Token + ", balance=" + balance
					+ ", isCertified=" + isCertified + ", errorMsg=" + errorMsg
					+ ", score=" + score + "]";
		}
		
}
