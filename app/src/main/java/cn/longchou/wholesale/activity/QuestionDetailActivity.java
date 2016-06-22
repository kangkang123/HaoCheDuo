package cn.longchou.wholesale.activity;

import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.ItemHelpCenter;

public class QuestionDetailActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView detail;
	private TextView mDetailContent;
	
	List<String> list=null;
	@Override
	public void initView() {
		setContentView(R.layout.activity_question_detail);
		
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
	    mTitle = (TextView) findViewById(R.id.tv_my_news_title);
	    
	    detail = (TextView) findViewById(R.id.question_detail);
	    
	    mDetailContent = (TextView) findViewById(R.id.tv_help_detail);

	}

	@Override
	public void initData() {
		
		
		String title = getIntent().getStringExtra("title");
		String help = getIntent().getStringExtra("help");
		
		mTitle.setText(help);
		
		int position=getIntent().getIntExtra("position", 0);
		
		if("常见问题".equals(help))
		{
			list = Constant.getManyQuestionDetail();
		}else if("买车流程".equals(help))
		{
			list=Constant.getBuyCarDetail();
		}
		else if("过户流程".equals(help))
		{
			list=Constant.getTransferProcessDetail();
		}
		else if("退款流程".equals(help))
		{
			list=Constant.getRefundProcessDetail();
		}
		else if("隆筹好车介绍".equals(help))
		{
			list=Constant.getCarintroduceDetail();
		}
		
		String content=list.get(position);
		detail.setText(title);
		mDetailContent.setText(content);

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
