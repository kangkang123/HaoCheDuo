package cn.longchou.wholesale.activity;

import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mingle.widget.LoadingView;

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
import cn.longchou.wholesale.adapter.PreferentialActivityAdapter;
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
* @Description: 优惠活动
*
* @author kangkang
*
* @date 2016年2月1日 下午4:02:40 
*
 */
public class PreferentialActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	
	private ListView mLvNews;
	private RelativeLayout mRlNoNews;
	
	private List<Activitys> list;
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
	public void initData() {
		mTitle.setText("优惠活动");
		
		getServerData();
		
	}

	private void getServerData() {

		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestPromotionActivity;
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
				PromotionActivity data = gson.fromJson(result, PromotionActivity.class);
				loading.setVisibility(View.GONE);
				list = data.activitys;
				if(!(list==null || list.size()==0))
				{
					mLvNews.setVisibility(View.VISIBLE);
					mRlNoNews.setVisibility(View.INVISIBLE);
					
					PreferentialActivityAdapter adapter=new PreferentialActivityAdapter(getApplicationContext(), list); 
					mLvNews.setAdapter(adapter);
					
				}else{
					mLvNews.setVisibility(View.INVISIBLE);
					mRlNoNews.setVisibility(View.VISIBLE);
					
				}
			}
		});
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		
		//设置内容的点击事件
		mLvNews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Activitys activitys = list.get(position);
				Intent intent=new Intent(getApplicationContext(),PreferentialDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("activitys", activitys);
				intent.putExtras(bundle);
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
		}

	}

}
