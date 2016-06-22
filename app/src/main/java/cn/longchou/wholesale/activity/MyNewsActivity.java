package cn.longchou.wholesale.activity;

import java.util.ArrayList;
import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyNewsAdapter;
import cn.longchou.wholesale.adapter.MyNewsFragmentAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.CartAddDelete;
import cn.longchou.wholesale.domain.MyNews;
import cn.longchou.wholesale.domain.MyNews.News;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.manage.CarsManager;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mingle.widget.LoadingView;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyNewsActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	
	private ListView mLvNews;
	private RelativeLayout mRlNoNews;

	private List<News> list;
	private LoadingView loading;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_my_news);
		  mBack = (ImageView) findViewById(R.id.iv_my_news_back);
	      mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		
	      mLvNews = (ListView) findViewById(R.id.lv_my_news);
		  mRlNoNews = (RelativeLayout) findViewById(R.id.rl_my_news_empty);
		  
		  loading = (LoadingView) findViewById(R.id.loadView);
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		
		//设置内容的点击事件
		mLvNews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				News myNews = list.get(position);
				
				setReadState(myNews);
				
				Intent intent=new Intent(getApplicationContext(),MyNewsDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("myNews", myNews);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	//设置为已读的标志
	protected void setReadState(News myNews) {

		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestReadMsg;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("msgID", myNews.msgID);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {


			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result = resultInfo.result;
				Gson gson=new Gson();
				CartAddDelete data = gson.fromJson(result, CartAddDelete.class);
				if(TextUtils.isEmpty(data.errorMsg))
				{
					UIUtils.showToastSafe("消息已读");
				}else{
					UIUtils.showToastSafe(data.errorMsg);
				}
				
			}
		});
	}

	@Override
	public void initData() {
		mTitle.setText("我的消息");
		
		getServerData();
		
	}

	//我的消息的数据的获取
	private void getServerData() {

		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestMyMsg;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result = resultInfo.result;
				Gson gson=new Gson();
				MyNews data = gson.fromJson(result, MyNews.class);
				loading.setVisibility(View.GONE);
				if(TextUtils.isEmpty(data.errorMsg))
				{
					list = data.msgs;
					if(!(list==null || list.size()==0))
					{
						mLvNews.setVisibility(View.VISIBLE);
						mRlNoNews.setVisibility(View.INVISIBLE);
						
						MyNewsAdapter adapter=new MyNewsAdapter(getApplicationContext(), list);
						mLvNews.setAdapter(adapter);
						
					}else{
						mLvNews.setVisibility(View.INVISIBLE);
						mRlNoNews.setVisibility(View.VISIBLE);
						
					}
					
				}else{
					UIUtils.showToastSafe(data.errorMsg);
				}
				
			}
		});
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		}
	}
}
