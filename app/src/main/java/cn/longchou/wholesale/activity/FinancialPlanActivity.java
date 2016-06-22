package cn.longchou.wholesale.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
/**
 * 
* @Description: 我要理财方案
*
* @author kangkang
*
* @date 2016年1月28日 下午7:49:05 
*
 */
public class FinancialPlanActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_financing_plan);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
	}

	@Override
	public void initData() {
		mTitle.setText("我要理财");

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;
		default:
			break;
		}

	}

}
