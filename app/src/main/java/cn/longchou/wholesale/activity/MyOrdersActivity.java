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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyOrdersAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.MyOrders;
import cn.longchou.wholesale.domain.MyOrders.Order;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;

public class MyOrdersActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private ListView mLvOrders;
	private RelativeLayout mRlOrdersNo;
	private List<Order> list;
	private String pay;
	private LoadingView loading;

	@Override
	public void initView() {
		setContentView(R.layout.activity_my_orders);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
	    mTitle = (TextView) findViewById(R.id.tv_my_news_title);
	    mLvOrders = (ListView) findViewById(R.id.lv_my_orders);
	    
	    mRlOrdersNo = (RelativeLayout) findViewById(R.id.rl_orders_no);
	    loading = (LoadingView) findViewById(R.id.loadView);

	}

	@Override
	public void initData() {
		mTitle.setText("我的订单");
		
		pay = getIntent().getStringExtra("pay");

		getServerData();
		
	}

	private void getServerData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestMyOrders;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				paraseData(result);
			}
		});
		
	}
	
	private void paraseData(String result) {
		Gson gson=new Gson();
		MyOrders orders = gson.fromJson(result, MyOrders.class);
		loading.setVisibility(View.GONE);
		list = orders.orders;
    	if(list==null || list.size()==0)
		{
			mRlOrdersNo.setVisibility(View.VISIBLE);
			mLvOrders.setVisibility(View.INVISIBLE);
		}else{
			mRlOrdersNo.setVisibility(View.INVISIBLE);
			mLvOrders.setVisibility(View.VISIBLE);
			
		}
		MyOrdersAdapter adapter=new MyOrdersAdapter(getApplicationContext(), list);
		mLvOrders.setAdapter(adapter);

	}

	@Override
	public void initListener() {

		mBack.setOnClickListener(this);
		
		mLvOrders.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			   Order myOrder = list.get(position);
			   Intent intent=new Intent(getApplicationContext(),MyOrdersDetailActivity.class);
			   Bundle bundle=new Bundle();
			   bundle.putSerializable("Order", myOrder);
			   intent.putExtras(bundle);
			   startActivityForResult(intent, 1);
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			getServerData();
			break;

		default:
			break;
		}
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			//当列表是从支付界面过来的时候
			if("pay".equals(pay))
			{
				setResult(1);
				finish();
			}else{
				
				finish();
			}
            break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if("pay".equals(pay))
			{
				setResult(1);
				finish();
			}else{
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
