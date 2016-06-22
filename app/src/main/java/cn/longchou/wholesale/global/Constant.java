package cn.longchou.wholesale.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;

import android.widget.ImageView;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.domain.ItemSelect;
import cn.longchou.wholesale.domain.LoginValidate;

public class Constant {
	
	public static String cookieStore=null;
	
//	public static String url="http://121.40.204.61:8099/longchoucar/";
	
//	public static String url="http://120.55.82.202/";
	public static String url="http://10.0.0.14:8080/";
//	public static String url="http://10.0.2.137:8080/longchoucar/";
//	public static String url="http://10.0.2.77:8080/";
	
	
//	public static String url="http://172.19.243.3:8080/longchoucar/";
	
//	public static String url="http://10.0.2.137:8080/longchoucar/";
	
	public static String Register=url+"register?";
	public static String Login=url+"login?";
	public static String ForgetPassword=url+"resetPwd?";
	//首页
	public static String HomePage=url+"homepage?";
	//城市定位
	public static String RequestCityLocation=url+"ourcity?";
	//车市列表
	public static String CarList=url+"carlist?";
	//筛选热门活动
	public static String HotActivity=url+"carlist/filter?";
	//筛选界面的品牌
	public static String AvailableBrand=url+"availableBrand?";
	//筛选热门搜索
	public static String HotSearch=url+"carlist/search?";
	//购物车的内容
	public static String RequestCartCar=url+"mycart?";
	//添加到购物车
	public static String RequestAddToCart=url+"addToCart?";
	//移除购物车
	public static String RequestRemoveFromCart=url+"removeFromCart?";
	//我的界面，获取用户的相关信息
	public static String RequestMy=url+"my?";
	//我的订单
	public static String RequestMyOrders=url+"myOrder?";
	//订单详情
	public static String RequestMyOrdersDetails=url+"myOrderDetail?";
	//我的关注
	public static String RequestMyFavorite=url+"myFavorite?";
	//添加到关注
	public static String RequestAddFavorite=url+"addFavorite?";
	//取消关注
	public static String RequestRemoveFavorite=url+"removeFavorite?";
	//我的求购
	public static String RequestMySeek=url+"mySeek?";
	//我的消息
	public static String RequestMyMsg=url+"myMsg?";
	//我的消息设置为已读的状态
	public static String RequestReadMsg=url+"readMsg?";
	//优惠活动
	public static String RequestPromotionActivity=url+"promotionActivity?";
	//车辆详情
	public static String RequestCarDetail=url+"carDetail?";
	//检查报告
	public static String RequestCheckReport=url+"checkReport?";
	//办证须知
	public static String RequestMust=url+"must?";
	//基本参数
	public static String RequestPrimary=url+"primary?";
	//金融首页图片
	public static String RequestFinaceHome=url+"finaceHome?";
	//我的金融的内容
	public static String RequestmyFinance=url+"myFinance?";
	//我的积分
	public static String RequestSocre=url+"socre?";
	//我的余额
	public static String RequestBalance=url+"balance?";
	//我的支出明细
	public static String RequestBalanceDetail=url+"balance/detail?";
	//是否授信
	public static String RequestQueryTrust=url+"queryTrust?";
	//申请金融贷款
	public static String RequestApply=url+"aply?";
	//金融的详情
	public static String RequestFinanceDetail=url+"financeDetail?";
	//更改密码
	public static String RequestResetPassword=url+"resetPassword?";
	//更改手机号码
	public static String RequestResetPhone=url+"resetPhone?";
	//取消订单
	public static String RequestCancelOrders=url+"cancelAppOrder?";
	//生成订单时调用
	public static String RequestAddOrders=url+"addAppOrder?";
	//支付成功的时候调用
	public static String RequestPaySuccess=url+"paySuccess?";
	//充值
	public static String RequestRecharge=url+"recharge?";
	//支付宝充值回调
	public static String RequestAliNotify=url+"/aliRecharge/";
	//微信支付回调
	public static String RequestWeiNotify=url+"/weixinRecharge/";
	
	//微信支付成功
	public static String RequestPayWeiSuccess=url+"/weixinPaySuccess/";
	//支付宝支付成功
	public static String RequestPayBaoSuccess=url+"/aliPaySuccess/";
	//余额支付
	public static String RequestBalancePay=url+"balancePay?";
	
	//用户访问的次数
	public static String RequestVerticalVisit=url+"whole/track?";
	
	//应用更新
	public static String RequestVersionUpdate=url+"appVersion/newest";
	
	//应用更新版本
	public static String Update="http://120.55.82.202/appVersion/update?version=20";
	
	//提现
	public static String RequestWithdraw=url+"withdraw?";
	//注册协议
	public static String RequestRigisterProtocol=url+"views/front/text/protocol.html";
	//质保说明
	public static String RequestZhiBao=url+"views/front/text/warranty.html";
	
	//注册时的城市
	public static String RequestRegisterCity=url+"app/api/city/getAllCity";

	//维保记录
	public static String RequestMaintenance=url+"maintenance/";

	//积分
	public static String RequestScore=url+"app/api/u/v1.0/point/index?";
	
//	public static String RequestNotifyUrl=url+"weixinPaySuccess/orderNo=";
	
//	public static String RequestAccessToken="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxbcc3051c0f5de023&secret=ff4c7880b9318342eba9dc00e6427557";

//	public static String RequestBudgetOrder="https://api.weixin.qq.com/pay/genprepay?access_token=";
	//统一下单
	public static String RequestOrder="https://api.mch.weixin.qq.com/pay/unifiedorder";
	//微信退款
	public static String RequestWeiRefund="https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	/** 支付宝部分*/
	// 商户PID
	public static final String PARTNER = "2088121899497389";
	// 商户收款账号
	public static final String SELLER = "lczf@longchoucar.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMSDX867jSTQoLVaaAbyLAlFdGPFFky3aqwXBpr2tNhipsJ/jguSa21jGO02ilW0+zQ7DwSyAPHscr51f+tC0a8F+EZZZ3zz1qqQGqfWEGNSQZQLwNcOkgwZDrTZfbEZYoxsN5PEiDI4byP0dY9ruJ0EemAVr2UNHNHc4hchr+Q7AgMBAAECgYAQCsF5eOoOxVE5PMYdOwvJPfhAZMhrPtXgcojBgb5Fo9gFLLCF1VAbv+k7BCbK1FllbCTPt1BIb4r2bVUh+XRwdqdkgtxZiFBPIr5uvTj44yTYeNQT3/WxFnHdda2noo5sFw0tXvJFn2X6rqqxPm/vXvjeTKvhBjJqiTBar8cPgQJBAOGWRqhb9Bqbmenjah1wjNsIW2OzUt7jYQyZ71lF55XgfDBTcpE9x1K8GP+6ZjUo2qb/j+0HhZawURtIeMqokyECQQDfAar78AyxVL0+TiNrgNNNUb03xPD/FkMcZIHENTxwv3j0uYXGr+3U+ISPmun/GCRqSs5czeF3+1lxKy+DECfbAkBR2plg1ZzJWtSKTX5fmEtD3tBqaNMVFSRN0j1LA1Z4x6/IST80/Fmq552ajSQw/dX46ppqw2PxvaBwaeeKgYiBAkEAr3W0fj1E/1FZBGieXbsihbdGVVbS9yEg9Hnvz9zDDULZfFNr+gP58JMIWaTdbyQjoq3w5/vk5m4Q1YXRACH5+QJAdgiIwET3XX62r/Yd/+lyNyCn+EflchWPcaSw4AAdgzgSSJjwNETiHuXuD9yVA1/i8zdzDD0Qa/KNGf/5miRySw==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEg1/Ou40k0KC1WmgG8iwJRXRjxRZMt2qsFwaa9rTYYqbCf44LkmttYxjtNopVtPs0Ow8EsgDx7HK+dX/rQtGvBfhGWWd889aqkBqn1hBjUkGUC8DXDpIMGQ602X2xGWKMbDeTxIgyOG8j9HWPa7idBHpgFa9lDRzR3OIXIa/kOwIDAQAB";

	//保存在微信支付是提交的订单
	public static String orderNumber=null;
	//保存微信支付提交时的可用余额
	public static float balanceMoney=0;
	//保存微信支付时是否从列表也过来
	public static String orderList=null;
	
	public static String CartCar="carcar";
	public static String Market="market";
	
	//判断用户是否已经登录
	public static boolean isLogin=false;
	
	//保存用户是否认证
	public static boolean isCertified;
	
	public static List<Cars> carsList;
	
	//保存筛选界面的热门活动
	public static String hotActivity=null;
	
	//保存筛选界面的所有品牌
	public static String availableBrand=null;
	
	//保存定位的城市,所有的城市
	public static String cityLocation=null;
	
	//保存我的金融的各种贷款的内容
	public static String myFinance=null;
	
	//保存我的相关的个人信息
	public static String myInfo=null;
	
	//保存定位到的城市
	public static String LocationCity=null;
	
	//保存选中的图片
	public static ImageView mImage=null;
	
	//保存热门搜索的内容
	public static String searchSelect="";
	
	//是否选中全部,筛选页面的第二层
	public static boolean isAllSelect=true;
	
	//在刷选页面是否选中了取消，默认没有取消
	public static boolean isCancel=false;
	
	//是否让支付页面消失
	public static boolean isPayFinish=false;
	
	//订单支付
	public static boolean isOrderPay=false;
	//充值
	public static boolean isRecharge=false;
	//是否是车市列表页面的返回
	public static boolean isCarMarketList=false;
	
	//保存选中的方案的车的B方案的id
	public static List<String> carPlanSelect=new ArrayList<String>();
	
	/**微信部分*/
	//微信的APPid
	public static String APP_ID="wxbcc3051c0f5de023";
	//微信的AppSecret
	public static String AppSecret="ff4c7880b9318342eba9dc00e6427557";
    //微信支付商户号
	public static String mch_id="1318467901";
	//微信秘钥
	public static String key="longchoucar20160218longchoucar10";
	
	//是否选中全国
	public static boolean isCountry;
	
	public static List<Integer> ids=new ArrayList<Integer>();
	

	public static class ShowMsgActivity {
			public static final String STitle = "showmsg_title";
			public static final String SMessage = "showmsg_message";
			public static final String BAThumbData = "showmsg_thumb_data";
		}
	
	public static List<String> getMyItems()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("我的消息");
		provinces.add("我的订单");
		provinces.add("我的关注");
		provinces.add("车辆求购");
		provinces.add("优惠活动");
//		provinces.add("常见问题");
		return provinces;
	}
	
	//常见问题
	public static List<String> getManyQuestion()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("如何完成线上车辆订购？");
		provinces.add("如何在线支付？");
		provinces.add("隆筹好车4S店已开通哪些城市？");
		provinces.add("我是商户，如何与隆筹好车合作？");
		provinces.add("对车商经营年限有要求吗？");
		provinces.add("关于商户的金融服务如何申请，有没有限制？ ");
		provinces.add("你们的车为什么比网上其它平台售价高？");
		provinces.add("如何联系隆筹好车？ ");
		provinces.add("隆筹好车线下门店地址");
		return provinces;
	}
	
	//常见问题详情
	public static List<String> getManyQuestionDetail()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("线上订购车辆并支付定金500元；3日内到店付清尾款，签订销售协议、办理过户便可完成交易。");
		provinces.add("在选择支付功能后即可选择“支付宝”或“微信”进行在线快捷支付。");
		provinces.add("目前隆筹好车已经覆盖徐州、临沂、南通、嘉兴、淄博、淮安、湖州等城市，其他城市正在努力筹备中，敬请期待。");
		provinces.add("感谢您对隆筹好车的支持，您可以致电客服热线4008596677 或通过APP在线提交申请，我们将尽快与您取得联系。");
		provinces.add("目前对经营年限没有要求，只要您有正规经营的营业场所和营业执照即可。");
		provinces.add("目前对商户的金融服务申请没有限制，只要您是正规经营的商户就可以在App端的金融专区里直接在线申请，我们会尽快为您办理。如有不清楚的地方也可致电客服热线4008596677。");
		provinces.add("我们的价格有可能会出现比其他平台略高的情况，但相比其他网上平台，我们有全国连锁的4S店，实体品牌连锁为您提供全面的售前、售中、售后服务，因此价格可能会出现略高的情况。");
		provinces.add("您可以通过以下方式联系我们：\n（1）微信公众号：在微信里搜索“车商之家”；\n（2）客服热线：4008596677（服务时间09:00-17:30）。");
		provinces.add("1、徐州店：江苏省徐州市云龙区两山口欣欣路汽车产业园区（雪佛兰4S店南侧）\n2、临沂店：山东省临沂市兰山区海关路78号\n3、南通店：江苏省南通市港闸区城港路139号\n4、嘉兴店：浙江省嘉兴市南湖区中环南路汽车商贸城999-2-3号"
				+ "5、淄博店：山东省淄博市张店区山泉路中端（山泉中队南行100米路西）\n6、湖州店：浙江省湖州市吴兴区蜀山路2995号1幢\n7、淮安店：江苏省淮安市清河区旺旺路北侧（奥迪4S店东侧）");
		return provinces;
	}
	
	
	
	//买车流程
	public static List<String> getBuyCar()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("如何预约买车？有哪些预约方式？");
		provinces.add("在隆筹好车商城可以买到哪些车？");
		provinces.add("隆筹好车商城出售的车辆包含车牌吗？");
		provinces.add("隆筹好车商城是否出售未上牌新车？");
		return provinces;
	}
	
	//买车流程详情
	public static List<String> getBuyCarDetail()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("你可以从隆筹好车网站、APP、手机网站、微信四个入口的“我要买车”页面进行订购。也可到店咨询或致电400-859-6677。");
		provinces.add("6年12万公里以内，非重大事故、非火烧、非水泡，非营运，手续齐全的车辆。");
		provinces.add("隆筹好车商城上出售的车辆都不包含车牌。如果相关需求，买家可与门店上牌专员协商办理。");
		provinces.add("隆筹好车商城出售的都是优质的二手车源，没有未上牌新车。");
		return provinces;
	}
	
	//过户流程
	public static List<String> getTransferProcess()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("过户需要的手续 ");
		provinces.add("二手车过户交易流程");
		return provinces;
	}
	
	//过户流程详情
	public static List<String> getTransferProcessDetail()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("卖方：车主身份证、车辆登记证、车辆行驶本、购车原始发票（如果之前有过户历史需提供过户票）。卖方是单位则需要组织机构代码证书原件及公章。\n\n"+
					  "买方：身份证，外地人上当地牌照另需有效期内暂住证（部分城市改用居住证）。买方是单位则需要组织机构代码证书原件及公章。\n\n"+
					  "双方：签订二手车买卖合同。带齐以上所有手续，到二手车过户大厅办理。");
		provinces.add("1.检查车辆并填写检查记录表\n\n"+
					  "2.领取照片\n\n"+
					  "3.取号机取号\n\n"+
					  "4.转移受理\n\n"+
					  "5.领取回执单\n\n"+
					  "6.收费\n\n"+
					  "7.选号\n\n"+
					  "8.领行驶证，登记证，年检标志\n\n"+
					  "9.领取号牌");
		return provinces;
	}
	
	//退款流程
	public static List<String> getRefundProcess()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("退款流程 ");
		return provinces;
	}
	
	//退款流程详情
	public static List<String> getRefundProcessDetail()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("如果您对购买的车，自您通过隆筹好车POS系统支付车款次日零时开始7日内，发现有任何'重大事故'可以通过以下步骤开启退车赔付流程：\n\n"+
					  "1、您需要通过合同所指定的检测机构（国家认可的具有专业鉴定资质的第三方鉴定评估机构或所购车辆品牌4S店或保险公司）对所购车辆进行检测，出具检测报告（保险公司仅指定损报告）确定您所购买的车辆为重大事故车，方可以提出退车申诉。\n\n"+
					  "2、您需要通过拨打隆筹好车客服电话提出申诉。客服电话：400-859-6677 ，周一至周日 09:30-17:30。\n\n"+
					  "3、在退车过程中，您需要提供您的个人信息资料，包括购车合同、无重大事故协议书及身份证原件。\n\n"+
					  "4、隆筹好车在接到您的申诉及收到检测报告后，予以受理。\n\n"+
					  "5、自隆筹好车受理48小时内，您需将车辆送至指定地点。\n\n"+
					  "6、在您送达车辆后的48小时内，隆筹好车会对您的退车进行鉴定，并作出最终判定（以隆筹好车作出的鉴定报告为最终判断标准）。\n\n"+
					  "7、鉴定您所购车辆为重大事故车后，隆筹好车将与您签订退车协议。\n\n"+
					  "8、签订退车协议后的7个工作日内，您需要配合隆筹好车把车辆再次过户。\n\n"+
					  "9、在您过户成功3个工作日后，隆筹好车将车款全数退还给您，退车流程结束。");
		return provinces;
	}
	
	//隆筹好车简介
	public static List<String> getCarintroduce()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("隆筹好车二手车 ");
		return provinces;
	}
	
	
	//隆筹好车简介
	public static List<String> getCarintroduceDetail()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("全国车商最信赖的二手车批售车源交易平台。\n\n"+
					  "海量优质车源，放心车源，真实车主4S店置换车源一网打尽。\n\n"+
					  "覆盖范围华东江苏、上海、安徽、浙江、山东、福建。每天有上万车商访问交易，实时发布求购车源，隆筹好车为您寻找市场上的优质车源，并提供金融解决方案。\n\n"+
					  "二手车APP致力于打造专业的便捷的放心的二手车交易平台，真实的车况。可靠的车源，并依托公正、严格、周密的发车审核体系，为用户提供优质批售车源，客观展现真实车况。\n\n"+
					  "主要功能：\n\n"+
					  "1. 买车：数万条车源信息实时滚动更新，精准筛选便捷找车。\n\n"+
					  "2. 求购：满足您的各种需求，精挑细选优质好车。\n\n"+
					  "3. 金融：车商专有金融服务，预授权购车，并提供购车贷款方案。");
		return provinces;
	}
	
	
	
	//获取筛选条件的里程条件
	public static List<String> getMileage()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("全部");
		provinces.add("1万公里以内");
		provinces.add("1-3万公里");
		provinces.add("3-5万公里");
		provinces.add("5-8万公里");
		provinces.add("8-10万公里");
		provinces.add("10万公里以上");
		return provinces;
	}
	
	//获取筛选条件的价格条件
	public static List<String> getPrice()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("全部");
		provinces.add("5万以内");
		provinces.add("5-10万");
		provinces.add("10-15万");
		provinces.add("15-20万");
		provinces.add("20-30万");
		provinces.add("30-50万");
		provinces.add("50万以上");
		return provinces;
	}
	
	//获取筛选条件的车龄条件
	public static List<String> getCarYears()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("全部");
		provinces.add("1年以内");
		provinces.add("1-2年");
		provinces.add("2-3年");
		provinces.add("3-5年");
		provinces.add("5年以上");
		return provinces;
	}
	
	//处理字符串
	public static String getHandleString(String string,boolean isMileage)
	{
		if(string.indexOf("以内") != -1)  
		{  
			 if(isMileage)
			 {
				 String substring = string.substring(0, string.length()-5);
				 return "0-"+substring;
			 }else{
				 String substring = string.substring(0, string.length()-3);
				 return "0-"+substring;
			 }
		     
		}
		else if(string.indexOf("以上") != -1)
		{
			if(isMileage)
			 {
		    	String substring = string.substring(0, string.length()-5);
		    	return substring+"-";
			 }else{
				 String substring = string.substring(0, string.length()-3);
			     return substring+"-";
			 }
			
		}else{
			if(isMileage)
			 {
				return string.substring(0, string.length()-3);
				
			 }else{
				 return string.substring(0, string.length()-1);
			 }
		}
	}
	
	//获取筛选条件的车龄条件
	public static List<String> getGearBox()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("全部");
		provinces.add("手动");
		provinces.add("自动");
		return provinces;
	}
	
	//获取筛选条件的车型
	public static List<String> getCarModel()
	{
		List<String> provinces=new ArrayList<String>();
		provinces.add("全部");
		provinces.add("SUV");
		provinces.add("小轿车");
		provinces.add("紧凑型车");
		provinces.add("中型车");
		provinces.add("豪华车");
		provinces.add("跑车");
		return provinces;
	}
	
	//保存筛选选中的品牌
	public static String screenHotBrand="全部";
	//保存筛选选中的车型
	public static String screenCarModel="全部";
	//保存筛选选中的变速箱
	public static String screenGearBox="全部";
	//保存筛选选中的车龄
	public static String screenCarYears="全部";
	//保存筛选选中的价格
	public static String screenPrice="全部";
	//保存筛选选中的里程
	public static String screenMileage="全部";
	//保存筛选选中的活动
	public static String screenAvtivity=null;
	//保存热门活动中的自定义筛选
//	public static String filterAction=null;
	
	//注册时的城市选择
	public static String cityChoose="";
	//性别选择
	public static String sexChoose="保密";
	
}
