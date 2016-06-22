package cn.longchou.wholesale.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.HomePage.Notification;
import cn.longchou.wholesale.domain.MyNews;
import cn.longchou.wholesale.domain.MyNews.News;

public class MyNewsDetailActivity extends BaseActivity {


	private ImageView mBack;
	private TextView mTitle;
	
	ImageView round;
	TextView title;
	ImageView mark;
	TextView time;
	TextView content;

	@Override
	public void initView() {
		setContentView(R.layout.activity_my_news_detail);
        mBack = (ImageView) findViewById(R.id.iv_my_news_back);
        mTitle = (TextView) findViewById(R.id.tv_my_news_title);
        
        round=(ImageView) findViewById(R.id.iv_my_news_round);
		title=(TextView) findViewById(R.id.tv_my_news_detail_title);
		time=(TextView) findViewById(R.id.tv_my_news_time);
		content=(TextView) findViewById(R.id.iv_my_news_content);
	}

	@Override
	public void initData() {
		News myNews=(News) getIntent().getSerializableExtra("myNews");
		
		Notification mNotification=(Notification) getIntent().getSerializableExtra("notification");
		
		String newsContent = getIntent().getStringExtra("notificationContent");
		String newsDate = getIntent().getStringExtra("notificationDate");
		String newsTitle = getIntent().getStringExtra("notificationTitle");
		
		
        mTitle.setText("消息详情");
        
        if(null!=myNews)
        {
        	round.setVisibility(View.GONE);
        	title.setText(myNews.msgTitle);
        	time.setText(myNews.msgDate);
        	content.setText(myNews.msgContent);
        }else{
        	round.setVisibility(View.GONE);
        	title.setText(newsTitle);
        	time.setText(newsDate);
        	content.setText(newsContent);
        }
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
