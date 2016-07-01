package cn.longchou.wholesale.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.AddressManagerAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.AddressList;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;

/**
 * 
* @Description: 地址管理
*
* @author kangkang
*
* @date 2016年6月4日 下午3:42:03 
*
 */
public class AddressManagerActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private RelativeLayout mAddressNo;
	private ListView mListView;
	private TextView mAddress;
	private List<AddressList.UserAddressBean> list;
	@Override
	public void initView() {
		setContentView(R.layout.activity_address_manager);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		//没有地址的情况
		mAddressNo = (RelativeLayout) findViewById(R.id.rl_no_address);
		//地址列表
		mListView = (ListView) findViewById(R.id.lv_address_manager);
		//增加地址
		mAddress = (TextView) findViewById(R.id.tv_add_address);

	}

	@Override
	public void initData() {
		mTitle.setText("地址管理");

		getServerData();

	}

	private void getServerData() {
		HttpUtils http=new HttpUtils();
		String url= Constant.RequestFindAddress;
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token",token);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				paraseData(result);
			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

	private void paraseData(String result) {
		Gson gson=new Gson();
		AddressList data = gson.fromJson(result, AddressList.class);
		list=data.userAddress;
		if(list.size()>0 && list!=null){
			AddressManagerAdapter adapter=new AddressManagerAdapter(getApplicationContext(), list);
			mListView.setAdapter(adapter);
			mAddressNo.setVisibility(View.GONE);
		}else{
			mAddressNo.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mAddress.setOnClickListener(this);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				AddressList.UserAddressBean addressManager = list.get(position);
				Intent intent=new Intent(AddressManagerActivity.this,EditAddressActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("address", addressManager);
				intent.putExtras(bundle);
				startActivityForResult(intent,1);
			}
		});

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		case R.id.tv_add_address:
			Intent intent=new Intent(AddressManagerActivity.this,EditAddressActivity.class);
			startActivityForResult(intent,1);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
			case 1:
				getServerData();
				break;
		}
	}
}
