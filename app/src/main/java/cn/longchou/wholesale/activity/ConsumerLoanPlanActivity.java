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
* @Description: 消费贷方案
*
* @author kangkang
*
* @date 2016年1月28日 下午7:46:42 
*
 */
public class ConsumerLoanPlanActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mSubmit;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_consume_loan_plan);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mSubmit = (TextView) findViewById(R.id.tv_finance_consume_submit);
	}

	@Override
	public void initData() {
		mTitle.setText("消费贷方案");

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
		case R.id.tv_finance_consume_submit:
			Intent intent=new Intent(ConsumerLoanPlanActivity.this,FinancialLoansActivity.class);
			intent.putExtra("plan", "xf");
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
