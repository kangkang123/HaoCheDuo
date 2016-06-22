package cn.longchou.wholesale.activity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.MyBalance;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.ToastUtils;
/**
 * 
* @Description:可用余额的界面 
*
* @author kangkang
*
* @date 2016年1月14日 下午3:45:28 
*
 */
public class BalanceActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mDetail;
	//可用余额
	private TextView mAvailMoney;
	//总金额
	private TextView mTotalMoney;
	//体现中
	private TextView mWithDrawing;
	//充值
	private TextView mRecharge;
	//提现
	private TextView mWithdraw;
	
	private String withdrawing;
	private MyBalance data;
	
	private String available;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_balance);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		mDetail = (TextView) findViewById(R.id.tv_my_title_login);
		
		mAvailMoney = (TextView) findViewById(R.id.tv_balance_avail_money);
		mTotalMoney = (TextView) findViewById(R.id.tv_balance_totol_money);
		mWithDrawing = (TextView) findViewById(R.id.tv_balance_withdrawing);
		mRecharge = (TextView) findViewById(R.id.tv_balance_recharge);
		mWithdraw = (TextView) findViewById(R.id.tv_balance_withdraw);
		

	}

	@Override
	public void initData() {
		mTitle.setText("余额");
		mDetail.setVisibility(View.VISIBLE);
		mDetail.setText("明细");
		mDetail.setTextColor(Color.GRAY);
		
		getServerData();


	}

	private void getServerData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url = Constant.RequestBalance;
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				Gson gson=new Gson();
				data = gson.fromJson(result, MyBalance.class);
				if(null!=data)
				{
					available = data.available;
					withdrawing = data.withdrawing;
					//可用余额
					mAvailMoney.setText(data.available+"元");
					//总金额
					mTotalMoney.setText(data.total+"元");
					//体现中
					mWithDrawing.setText(data.withdrawing+"元");
				}
				
			}
		});
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mRecharge.setOnClickListener(this);
		mWithdraw.setOnClickListener(this);
		mWithDrawing.setOnClickListener(this);

		mDetail.setOnClickListener(this);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;
		case R.id.tv_balance_recharge:
			Intent intentRecharge=new Intent(BalanceActivity.this,RechargeActivity.class);
			startActivityForResult(intentRecharge, 1);
			break;
		case R.id.tv_balance_withdraw:
			Intent intentWithdraw=new Intent(BalanceActivity.this,WithdrawDepositActivity.class);
			intentWithdraw.putExtra("amount", available);
			startActivityForResult(intentWithdraw, 1);
			break;
		case R.id.tv_my_title_login:
			Intent intent=new Intent(BalanceActivity.this,ExpenditureActivity.class);
			startActivity(intent);
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			getServerData();
			break;

		default:
			break;
		}
	}

}
