package cn.longchou.wholesale.activity;

import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.IntegrateAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.MyScore;
import cn.longchou.wholesale.domain.MyScore.ScoreDetail;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;

/**
 * 
 * @Description: 支出明细界面
 * 
 * @author kangkang
 * 
 * @date 2016年1月14日 下午6:18:35
 * 
 */
public class ExpenditureActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private ListView mLvDetail;

	@Override
	public void initView() {
		setContentView(R.layout.activity_expenditure_detail);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mLvDetail = (ListView) findViewById(R.id.lv_expendture_detail);
	}

	@Override
	public void initData() {
		mTitle.setText("明细");
		
		getServerData();

	}

	private void getServerData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url = Constant.RequestBalanceDetail;
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				parseData(result);
				
			}
		});
		
	}

	protected void parseData(String result) {
		Gson gson=new Gson();
		MyScore data = gson.fromJson(result, MyScore.class);
		List<ScoreDetail> listDetail = data.detail;
		
		if(listDetail.size()>0 || listDetail!=null)
		{
			IntegrateAdapter adapter=new IntegrateAdapter(getApplicationContext(),listDetail);
			mLvDetail.setAdapter(adapter);
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
