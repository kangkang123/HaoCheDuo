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
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.Withdraw;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 提现成功页
*
* @author kangkang
*
* @date 2016年1月30日 下午1:36:11 
*
 */
public class WithdrawDepositSuccessActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	
	private TextView mSubmit;
	private TextView mMoney;
	private String money;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_withdraw_deposit_success);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mSubmit = (TextView) findViewById(R.id.tv_bugdet_confirm_commit);
		
		mMoney = (TextView) findViewById(R.id.tv_avail_withdraw_money);
	}

	@Override
	public void initData() {
		mTitle.setText("提现");

		money = getIntent().getStringExtra("money");
		mMoney.setText(money);
		
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
//			Intent intent=new Intent(WithdrawDepositSuccessActivity.this,BalanceActivity.class);
//			startActivity(intent);
			setResult(1);
			finish();
			break;

		default:
			break;
		}

	}

}
