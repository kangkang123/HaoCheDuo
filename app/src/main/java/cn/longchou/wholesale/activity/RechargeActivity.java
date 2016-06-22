package cn.longchou.wholesale.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.H5PayDemoActivity;
import com.alipay.sdk.pay.demo.PayDemoActivity;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.SignUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.utils.WeiXinUtils;
/**
 * 
* @Description: 充值界面
*
* @author kangkang
*
* @date 2016年1月30日 上午10:39:58 
*
 */
public class RechargeActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	
	private LinearLayout mBao;
	private LinearLayout mWei;
	private ImageView mBaoChoose;
	private ImageView mWeiChoose;
	private TextView mFinish;
	
	//默认支付宝支付方式
	private boolean isBao=true;
	//默认微信支付方式
	private boolean isWei=false;
	private EditText mEtMoney;
	
	private IWXAPI api;
	
	private static final int SDK_PAY_FLAG = 1;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					
					setResult(1);
					finish();
					
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(RechargeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
							setResult(1);
							finish();
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_recharge);
		
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
		api.registerApp(Constant.APP_ID);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mEtMoney = (EditText) findViewById(R.id.et_recharge_money);
		//支付宝的线性布局
		mBao = (LinearLayout) findViewById(R.id.ll_budget_confirm_bao);
		//微信的线性布局
		mWei = (LinearLayout) findViewById(R.id.ll_budget_confirm_wei);
		//支付宝的选择
		mBaoChoose = (ImageView) findViewById(R.id.iv_budget_confirm_bao);
		//微信的选择
		mWeiChoose = (ImageView) findViewById(R.id.iv_budget_confirm_wei);
		//提交订单
		mFinish = (TextView) findViewById(R.id.tv_bugdet_confirm_commit);
	}

	@Override
	public void initData() {
		mTitle.setText("充值");

	}

	@Override
	public void initListener() {
		 mBack.setOnClickListener(this);
		
		 mBao.setOnClickListener(this);
	     mWei.setOnClickListener(this);
	     
	     mFinish.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			setResult(1);
			finish();
			break;
		case R.id.ll_budget_confirm_bao:
			if(!isBao)
			{
				mBaoChoose.setImageResource(R.drawable.finance_plan_choose);
				mWeiChoose.setImageResource(R.drawable.finance_plan_default);
				isBao=true;
				isWei=false;
			}
			break;
		case R.id.ll_budget_confirm_wei:
			if(!isWei)
			{
				mWeiChoose.setImageResource(R.drawable.finance_plan_choose);
				mBaoChoose.setImageResource(R.drawable.finance_plan_default);
				isBao=false;
				isWei=true;
			}
			break;
		case R.id.tv_bugdet_confirm_commit:
			String choose = null;
			if(isBao)
			{
				choose="支付宝";
			}else if(isWei)
			{
				choose="微信";
			}
			if("微信".equals(choose))
			{
				String money = mEtMoney.getText().toString().trim();
				if(TextUtils.isEmpty(money))
				{
					UIUtils.showToastSafe("请输入充值金额！");
				}else{
					useWeiPay(money);
				}
			}else if("支付宝".equals(choose))
			{
				useBaoPay();
			}
			break;

		default:
			break;
		}

	}

	//支付宝方式
	private void useBaoPay() {
		String money = mEtMoney.getText().toString().trim();
		if(TextUtils.isEmpty(money))
		{
			UIUtils.showToastSafe("请输入充值金额！");
		}else{
			
			baoPayMoney(money);
		}
		
	}
	
  private void baoPayMoney(String money) {


		if (TextUtils.isEmpty(Constant.PARTNER) || TextUtils.isEmpty(Constant.RSA_PRIVATE) || TextUtils.isEmpty(Constant.SELLER)) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();
			return;
		}
		String orderInfo = getOrderInfo("车辆", "隆筹好车", money);

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(RechargeActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	
	
		
	}

	
	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 * 
	 * @param v
	 */
	public void h5Pay(View v) {
		Intent intent = new Intent(this, H5PayDemoActivity.class);
		Bundle extras = new Bundle();
		/**
		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
		 * 商户可以根据自己的需求来实现
		 */
		String url = "http://m.meituan.com";
		// url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
		extras.putString("url", url);
		intent.putExtras(extras);
		startActivity(intent);

	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price) {

		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Constant.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Constant.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + generateOrders() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
//		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
		orderInfo += "&notify_url=" + "\"" + Constant.RequestAliNotify+ token+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, Constant.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Constant.isRecharge)
		{
			finish();
			Constant.isRecharge=false;
		}
	}


	//微信方式
	private void useWeiPay(final String money) {
		final float payMoney = Float.parseFloat(money);
		
		final String orderNumber = generateOrders();
		final String token = PreferUtils.getString(getApplicationContext(), "token", null);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				String notify_url=Constant.RequestWeiNotify+token;
				try {
					String nonce_str=UUID.randomUUID().toString().replace("-", "");
					System.out.println("nonce:"+nonce_str);
					StringBuilder xml = new StringBuilder();
					xml.append("<xml>");
					xml.append("<appid>"+Constant.APP_ID+"</appid>");
					xml.append("<body>"+"test"+"</body>");
					xml.append("<mch_id>"+Constant.mch_id+"</mch_id>");
					xml.append("<nonce_str>"+nonce_str+"</nonce_str>");
					xml.append("<notify_url>"+notify_url+"</notify_url>");
//					xml.append("<notify_url>"+"0"+"</notify_url>");
					
					xml.append("<out_trade_no>"+orderNumber+"</out_trade_no>");
					xml.append("<spbill_create_ip>"+"14.23.150.211"+"</spbill_create_ip>");
					xml.append("<total_fee>"+(int)(payMoney*100)+"</total_fee>");
					xml.append("<trade_type>"+"APP"+"</trade_type>");
					xml.append("<sign>"+WeiXinUtils.getSign(orderNumber, (int)(payMoney*100),nonce_str,notify_url)+"</sign>");
					xml.append("</xml>");
					
					byte[] xmlbyte = xml.toString().getBytes("ISO8859-1");
					URL url = new URL(Constant.RequestOrder);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			        conn.setConnectTimeout(5000);
			        conn.setDoOutput(true);// 允许输出
			        conn.setDoInput(true);
			        conn.setUseCaches(false);// 不使用缓存
			        conn.setRequestMethod("POST");
			        conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			        conn.setRequestProperty("Charset", "UTF-8");
			        conn.setRequestProperty("Content-Length",
			                String.valueOf(xmlbyte.length));
			        conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			        conn.setRequestProperty("X-ClientType", "2");//发送自定义的头信息

			        conn.getOutputStream().write(xmlbyte);
			        conn.getOutputStream().flush();
			        conn.getOutputStream().close();
			        
			        if (conn.getResponseCode() != 200)
			            throw new RuntimeException("请求url失败");

			        InputStream is = conn.getInputStream();// 获取返回数据
			          
			        // 使用输出流来输出字符(可选)
			        ByteArrayOutputStream out = new ByteArrayOutputStream();
			        byte[] buf = new byte[1024];
			        int len;
			        while ((len = is.read(buf)) != -1) {
			            out.write(buf, 0, len);
			        }
			        String string = out.toString("UTF-8");
			        System.out.println(string);
			        String substring = string.substring(string.indexOf("<prepay_id><![CDATA["), string.indexOf("]]></prepay_id>")).replace("<prepay_id><![CDATA[", "").trim();
			        System.out.println("sub:"+substring);
			        out.close();
			        
			        long currentTimeMillis = System.currentTimeMillis()/1000;
			        String time = String.valueOf(currentTimeMillis);
			        System.out.println("time:"+time);
			        
			        PayReq request=new PayReq();
			        request.appId=Constant.APP_ID;
			        request.partnerId = Constant.mch_id;
			        request.prepayId= substring;
			        request.packageValue = "Sign=WXPay";
			        request.nonceStr= nonce_str;
			        request.timeStamp= time;

			        request.sign=WeiXinUtils.getResultSign(nonce_str, request.prepayId, request.timeStamp);

			        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//			        api.registerApp(Constant.APP_ID);
			        api.sendReq(request);
			        
			        Constant.isRecharge=true;
			        
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}).start();
		
        
	}
	
	//生成订单编号
	private String generateOrders()
	{
        java.util.Date current=new java.util.Date();
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyyMMddhhmmss"); 
        String c="c"+sdf.format(current)+generateRandom();
        return c;
	
	}
	//生成随机数
	private String generateRandom()
	{
		StringBuilder str=new StringBuilder();
		Random random=new Random();
		for(int i=0;i<7;i++)
		{
			int nextInt = random.nextInt(10);
			str.append(nextInt);
		}
		return str.toString();
	}

}
