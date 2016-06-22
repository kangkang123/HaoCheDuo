package cn.longchou.wholesale.activity;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyCenterAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.global.Constant;
/**
 * 
* @Description: 帮助中心的问题界面
*
* @author kangkang
*
* @date 2016年1月14日 上午11:04:48 
*
 */
public class HelpQuestionActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private ListView mLvQuestion;
	
	private int ids[]={R.drawable.help_many_question};
	
	List<String> list=null;
	private String title;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_help_question);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
	    mTitle = (TextView) findViewById(R.id.tv_my_news_title);
	    
	    mLvQuestion = (ListView)findViewById(R.id.lv_question);

	}

	@Override
	public void initData() {
		
		title = getIntent().getStringExtra("title");
		mTitle.setText(title);
		
		if("常见问题".equals(title))
		{
			list = Constant.getManyQuestion();
		}else if("买车流程".equals(title))
		{
			list=Constant.getBuyCar();
		}
		else if("卖车流程".equals(title))
		{
			list=Constant.getTransferProcess();
		}
		else if("申请库存融资流程".equals(title))
		{
			list=Constant.getRefundProcess();
		}
		else if("申请认证用户流程".equals(title))
		{
			list=Constant.getRefundProcess();
		}
		else if("隆筹好车介绍".equals(title))
		{
			list=Constant.getCarintroduce();
		}
		
		MyCenterAdapter adapter=new MyCenterAdapter(getApplicationContext(), list, ids, 1);
		mLvQuestion.setAdapter(adapter);

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);

		mLvQuestion.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(HelpQuestionActivity.this,QuestionDetailActivity.class);
				String string = list.get(position);
				intent.putExtra("title", string);
				intent.putExtra("help", title);
				intent.putExtra("position", position);
				startActivity(intent);
				
			}
		});
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
