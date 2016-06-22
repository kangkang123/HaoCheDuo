package cn.longchou.wholesale.wxapi;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.MyOrdersDetailActivity;
import cn.longchou.wholesale.activity.PayMoneyActivity;
import cn.longchou.wholesale.activity.PayMoneyActivity.OnActicityFinishListener;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
//官方文档里竟然没有这个activity，坑死了 fuck
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}
//点击完成后，弹出支付完成 在这里跳转回app
	@Override
	public void onResp(BaseResp resp) {
//		
		if(resp.errCode==0)
		{
			//当不是充值的时候
			if(!Constant.isRecharge)
			{
				UIUtils.showToastSafe("支付成功");
//			submitPaySuccess();
				Intent intent=new Intent(WXPayEntryActivity.this,MyOrdersDetailActivity.class);
				intent.putExtra("pay", "pay");
				intent.putExtra("state", "已支付");
				intent.putExtra("orderNumber", Constant.orderNumber);
				startActivityForResult(intent, 1);
				Constant.isPayFinish=true;
			}
		}else
		{
			//当不是充值的时候
			if(!Constant.isRecharge)
			{
				if(TextUtils.isEmpty(Constant.orderList))
				{
					
					UIUtils.showToastSafe("支付失败");
					Intent intent=new Intent(WXPayEntryActivity.this,MyOrdersDetailActivity.class);
					intent.putExtra("pay", "pay");
					intent.putExtra("state", "待支付");
					intent.putExtra("orderNumber", Constant.orderNumber);
					startActivityForResult(intent, 1);
				}else{
//				finish();
				}
			}
		}
		PayMoneyActivity.setOnActicityFinishListener(new OnActicityFinishListener() {
			
			@Override
			public void finishActicity(PayMoneyActivity activity) {
				activity.finish();
				
			}
		});
		setResult(1);
		finish();
	}
	//支付成功时调用,这是购买汽车的时候
	protected void submitPaySuccess() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestPaySuccess;
		RequestParams params=new RequestParams();
		params.addBodyParameter("orderNo", Constant.orderNumber);
		params.addBodyParameter("Token", token);
		params.addBodyParameter("balance", Constant.balanceMoney+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				Intent intent=new Intent(WXPayEntryActivity.this,MyOrdersDetailActivity.class);
				intent.putExtra("pay", "pay");
				intent.putExtra("state", "已支付");
				intent.putExtra("orderNumber", Constant.orderNumber);
				startActivityForResult(intent, 1);
				
			}
		});
	}
}