package cn.longchou.wholesale.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.BaseParamsAdapter;
import cn.longchou.wholesale.adapter.LicensePlateAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.CarBasicParameters;
import cn.longchou.wholesale.domain.LicensePlate;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.view.ListViewForScrollView;
/**
 * 
* @Description: 基本参数详情
*
* @author kangkang
*
* @date 2016年1月18日 上午10:25:50 
*
 */
public class BaseParamsDetailActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private ListViewForScrollView mlvBase;
	private ImageView mLine;
	@Override
	public void initView() {
		setContentView(R.layout.activity_base_param_detail);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mlvBase = (ListViewForScrollView) findViewById(R.id.lv_base_params);
	
		mLine = (ImageView) findViewById(R.id.iv_my_line);
	
	}

	@Override
	public void initData() {
		mTitle.setText("基本参数");
		
		mLine.setVisibility(View.INVISIBLE);
		
		Cars cars=(Cars) getIntent().getExtras().getSerializable("cars");
		getSerVerData(cars);

	}

	
	private void getSerVerData(Cars cars) {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestPrimary;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("carID", cars.carID+"");
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				try {
					parseData(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}

	protected void parseData(String result) throws Exception {
		List<CarBasicParameters> list = CarBasicParameters.getResult(result);
		BaseParamsAdapter adapter=new BaseParamsAdapter(getApplicationContext(),list);
		mlvBase.setAdapter(adapter);
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
