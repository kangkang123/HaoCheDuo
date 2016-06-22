package cn.longchou.wholesale.activity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.Withdraw;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

public class WithdrawDepositActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mSubmit;
	private EditText mEtMoney;
	private TextView mTvMoney;
	private float amount;
	@Override
	public void initView() {
		setContentView(R.layout.activity_withdraw_deposit);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mEtMoney = (EditText) findViewById(R.id.et_withdraw_deposit_money);
		mTvMoney = (TextView) findViewById(R.id.tv_avail_withdraw_money);
		
		mSubmit = (TextView) findViewById(R.id.tv_bugdet_confirm_commit);
	}

	@Override
	public void initData() {
		mTitle.setText("提现");

		amount = getIntent().getFloatExtra("amount", 0);
		mTvMoney.setText(amount+"元");
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;
		case R.id.tv_bugdet_confirm_commit:
			String money = mEtMoney.getText().toString().trim();
			if(!TextUtils.isEmpty(money))
			{
				float mMoney = Float.parseFloat(money);
				if(amount>=mMoney)
				{
					getServerData(money);
				}else{
					UIUtils.showToastSafe("您提现的金额不正确");
				}
			}else{
				UIUtils.showToastSafe("请输入金额");
			}
			
			
			break;

		default:
			break;
		}

	}
	
	private void getServerData(final String money) {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url = Constant.RequestWithdraw;
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token", token);
		params.addBodyParameter("amount", money);
		params.addBodyParameter("c", "as");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				Gson gson=new Gson();
				Withdraw data = gson.fromJson(result, Withdraw.class);
				if(null!=data)
				{
					if(TextUtils.isEmpty(data.errorMsg))
					{
						Intent intent=new Intent(WithdrawDepositActivity.this,WithdrawDepositSuccessActivity.class);
						intent.putExtra("money", money);
						startActivityForResult(intent, 1);
					}else{
						UIUtils.showToastSafe(data.errorMsg);
					}
				}
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			setResult(1);
			finish();
			break;

		default:
			break;
		}
	}

}
