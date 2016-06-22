package cn.longchou.wholesale.domain;
/**
 * 
* @Description: 微信支付获取access_token
*
* @author kangkang
*
* @date 2016年3月4日 上午11:17:10 
*
 */
public class AccessToken {

	//获取到的凭证
	public String access_token;
	//凭证有效时间
	public String expires_in;
	
	//当获取access_token失败的时候
	public String errcode;
	
	public String errmsg;
}
