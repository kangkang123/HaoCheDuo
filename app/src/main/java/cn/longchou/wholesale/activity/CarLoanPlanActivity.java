package cn.longchou.wholesale.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 收车贷方案
*
* @author kangkang
*
* @date 2016年1月28日 下午7:41:24 
*
 */
public class CarLoanPlanActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	
	private ImageView mBanner;
	private ImageView mIvRange;
	private TextView mTvRange;
	private TextView mTvLimit;
	private TextView mTvRepay;
	private TextView mSubmit;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_batch_financing_plan);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mSubmit = (TextView) findViewById(R.id.tv_finance_apply_finance_plan);
		
		mIvRange = (ImageView) findViewById(R.id.iv_finance_applay_range);
		mTvRange = (TextView) findViewById(R.id.tv_finance_applay_range);
		
		mTvLimit = (TextView) findViewById(R.id.tv_finance_loan_limit);
		
		mTvRepay = (TextView) findViewById(R.id.tv_finance_repay_way);
		
		mSubmit = (TextView) findViewById(R.id.tv_finance_apply_finance_plan);

	}

	@Override
	public void initData() {
		mTitle.setText("收车贷方案");
		
        mTvRange.setText("二手车商收购单辆车");
		
		mTvLimit.setText("15-90天");
		
		mTvRepay.setText("按天计息,随借随还");
		
		mSubmit.setText("立即申请收车贷");

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
		case R.id.tv_finance_apply_finance_plan:
			Intent intent=new Intent(CarLoanPlanActivity.this,FinancialLoansActivity.class);
			intent.putExtra("plan", "sc");
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
