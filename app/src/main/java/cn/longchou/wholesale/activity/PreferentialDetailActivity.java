package cn.longchou.wholesale.activity;

import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyNewsAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.MyNews;
import cn.longchou.wholesale.domain.MyNews.News;
import cn.longchou.wholesale.domain.PromotionActivity;
import cn.longchou.wholesale.domain.PromotionActivity.Activitys;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 优惠活动详情
*
* @author kangkang
*
* @date 2016年2月1日 下午4:02:40 
*
 */
public class PreferentialDetailActivity extends BaseActivity {


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
		Activitys activitys=(Activitys) getIntent().getSerializableExtra("activitys");
        mTitle.setText("优惠详情");
        
        round.setVisibility(View.GONE);
        title.setText(activitys.activityTitle);
        time.setText(activitys.activityDate);
		content.setText(replaceString(activitys.activityContent));
	}
	
	public static String replaceString(String content)
	{
		String replace = content.replace("\\n", "\n");
		System.out.println("replace:"+replace);
		return replace;
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
