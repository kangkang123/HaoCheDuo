package com.alipay.sdk.pay.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.BudgetConfirmActivity;
import cn.longchou.wholesale.activity.MyOrdersDetailActivity;
import cn.longchou.wholesale.activity.PayMoneyActivity;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PayDemoActivity extends FragmentActivity {

	// 商户PID
	public static final String PARTNER = "2088121899497389";
	// 商户收款账号
	public static final String SELLER = "lczf@longchoucar.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMSDX867jSTQoLVaaAbyLAlFdGPFFky3aqwXBpr2tNhipsJ/jguSa21jGO02ilW0+zQ7DwSyAPHscr51f+tC0a8F+EZZZ3zz1qqQGqfWEGNSQZQLwNcOkgwZDrTZfbEZYoxsN5PEiDI4byP0dY9ruJ0EemAVr2UNHNHc4hchr+Q7AgMBAAECgYAQCsF5eOoOxVE5PMYdOwvJPfhAZMhrPtXgcojBgb5Fo9gFLLCF1VAbv+k7BCbK1FllbCTPt1BIb4r2bVUh+XRwdqdkgtxZiFBPIr5uvTj44yTYeNQT3/WxFnHdda2noo5sFw0tXvJFn2X6rqqxPm/vXvjeTKvhBjJqiTBar8cPgQJBAOGWRqhb9Bqbmenjah1wjNsIW2OzUt7jYQyZ71lF55XgfDBTcpE9x1K8GP+6ZjUo2qb/j+0HhZawURtIeMqokyECQQDfAar78AyxVL0+TiNrgNNNUb03xPD/FkMcZIHENTxwv3j0uYXGr+3U+ISPmun/GCRqSs5czeF3+1lxKy+DECfbAkBR2plg1ZzJWtSKTX5fmEtD3tBqaNMVFSRN0j1LA1Z4x6/IST80/Fmq552ajSQw/dX46ppqw2PxvaBwaeeKgYiBAkEAr3W0fj1E/1FZBGieXbsihbdGVVbS9yEg9Hnvz9zDDULZfFNr+gP58JMIWaTdbyQjoq3w5/vk5m4Q1YXRACH5+QJAdgiIwET3XX62r/Yd/+lyNyCn+EflchWPcaSw4AAdgzgSSJjwNETiHuXuD9yVA1/i8zdzDD0Qa/KNGf/5miRySw==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEg1/Ou40k0KC1WmgG8iwJRXRjxRZMt2qsFwaa9rTYYqbCf44LkmttYxjtNopVtPs0Ow8EsgDx7HK+dX/rQtGvBfhGWWd889aqkBqn1hBjUkGUC8DXDpIMGQ602X2xGWKMbDeTxIgyOG8j9HWPa7idBHpgFa9lDRzR3OIXIa/kOwIDAQAB";

	private static final int SDK_PAY_FLAG = 1;
	
	
	private TextView mPrice;
	private String money;
	private String orderNumber;
	private String balance;

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
					Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					
					//如果没有订单的话就是充值
					if(TextUtils.isEmpty(orderNumber))
					{
						//充值
						rechargeMoney();
					}else{
						//支付成功的时候调用
						submitPaySuccess();
					}
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayDemoActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
						
						if(TextUtils.isEmpty(orderNumber))
						{
							setResult(1);
							finish();
						}else{
							Intent intent=new Intent(PayDemoActivity.this,MyOrdersDetailActivity.class);
							intent.putExtra("pay", "pay");
							intent.putExtra("state", "待支付");
							intent.putExtra("orderNumber", orderNumber);
							startActivity(intent);
						}

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
		mPrice = (TextView) findViewById(R.id.product_price);
		
		money = getIntent().getStringExtra("fee");
		orderNumber = getIntent().getStringExtra("orderNumber");
		balance = getIntent().getStringExtra("balance");
		
		mPrice.setText(money+"");
		UIUtils.showToastSafe(money+"");
		
	}

	//充值
	private void rechargeMoney() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestRecharge;
		RequestParams params=new RequestParams();
		params.addBodyParameter("amount", money);
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				System.out.println("result:"+result);
				setResult(1);
				finish();
			}
		});
	}

	//支付成功时调用
	protected void submitPaySuccess() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestPaySuccess;
		RequestParams params=new RequestParams();
		params.addBodyParameter("orderNo", orderNumber);
		params.addBodyParameter("Token", token);
		params.addBodyParameter("balance", balance);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				Intent intent=new Intent(PayDemoActivity.this,MyOrdersDetailActivity.class);
				intent.putExtra("pay", "pay");
				intent.putExtra("state", "已支付");
				intent.putExtra("orderNumber", orderNumber);
				startActivity(intent);
				
			}
		});
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
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
				PayTask alipay = new PayTask(PayDemoActivity.this);
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

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		if(TextUtils.isEmpty(orderNumber))
		{
			// 商户网站唯一订单号
			orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		}else{
			// 商户网站唯一订单号
			orderInfo += "&out_trade_no=" + "\"" + orderNumber + "\"";
		}
		

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

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
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
