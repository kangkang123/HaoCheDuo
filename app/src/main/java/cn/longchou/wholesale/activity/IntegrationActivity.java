package cn.longchou.wholesale.activity;

import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.IntegrateAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.MyScore;
import cn.longchou.wholesale.domain.MyScore.ScoreDetail;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;

/**
 * 
 * @Description: 可用积分的界面
 * 
 * @author kangkang
 * 
 * @date 2016年1月14日 下午3:45:47
 * 
 */
public class IntegrationActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mAvailIntegration;
	private TextView mFreezeIntegration;
	private ListView mLvIntegration;

	@Override
	public void initView() {
		setContentView(R.layout.activity_integration);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mAvailIntegration = (TextView) findViewById(R.id.tv_avail_integration);
		mFreezeIntegration = (TextView) findViewById(R.id.tv_freezing_integral);
		
		mLvIntegration = (ListView) findViewById(R.id.lv_avail_integration);
		

	}

	@Override
	public void initData() {
		mTitle.setText("积分");
		
		getServerData();
		

	}

	private void getServerData() {
//		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		String token= "df43e07927c1e46a9649f9b56f8f812c";
		HttpUtils http=new HttpUtils();
		String url = Constant.RequestScore;
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
				if(result!=null)
				{
					parseData(result);
				}
				
			}
		});
		
	}

	protected void parseData(String result) {
		Gson gson=new Gson();
		MyScore score = gson.fromJson(result, MyScore.class);
		
		mAvailIntegration.setText(score.avaliable+"");
		mFreezeIntegration.setText("0");
		
		List<ScoreDetail> listDetail = score.detail;
		
		if(listDetail.size()>0 || listDetail!=null)
		{
			IntegrateAdapter adapter=new IntegrateAdapter(getApplicationContext(),listDetail);
			mLvIntegration.setAdapter(adapter);
		}
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		
		mLvIntegration.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
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
