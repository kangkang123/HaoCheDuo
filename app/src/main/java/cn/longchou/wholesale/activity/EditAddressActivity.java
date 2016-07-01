package cn.longchou.wholesale.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.AddressList;
import cn.longchou.wholesale.domain.SaveMyInfo;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

/**
 * 
* @Description: 编辑地址
*
* @author kangkang
*
* @date 2016年6月4日 下午4:16:07 
*
 */
public class EditAddressActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mDeleteAddress;
	private EditText mName;
	private EditText mPhone;
	private EditText mCode;
	private EditText mCity;
	private EditText mCityDetail;
	private TextView mSave;
	private AddressList.UserAddressBean address;
	@Override
	public void initView() {
		setContentView(R.layout.activity_eidt_address);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mDeleteAddress = (TextView) findViewById(R.id.tv_delete_address);
		mName = (EditText) findViewById(R.id.et_address_name);
		mPhone = (EditText) findViewById(R.id.et_address_phone);
		mCode = (EditText) findViewById(R.id.et_address_code);
		mCity = (EditText) findViewById(R.id.et_address_city);
		mCityDetail = (EditText) findViewById(R.id.et_address_detail);
		mSave = (TextView) findViewById(R.id.tv_my_title_login);


	}

	@Override
	public void initData() {
		mTitle.setText("编辑地址");
		mSave.setText("保存");
		mSave.setVisibility(View.VISIBLE);
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null)
		{
			address = (AddressList.UserAddressBean)bundle.getSerializable("address");
			mName.setText(address.userName);
			mPhone.setText(address.phone);
			mCode.setText(address.postCode);
			String city=address.province+address.city+address.county;
			mCity.setText(city);
			mCityDetail.setText(address.address);
			mDeleteAddress.setVisibility(View.VISIBLE);
		}else{
			mDeleteAddress.setVisibility(View.GONE);

		}

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mDeleteAddress.setOnClickListener(this);
		mSave.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		//删除地址
		case R.id.tv_delete_address:
			deleteAddress();
			break;
		//保存地址
		case R.id.tv_my_title_login:
            saveAddress();
			break;

		default:
			break;
		}

	}

	//删除地址
	private void deleteAddress() {
		String token=PreferUtils.getString(getApplicationContext(),"token",null);
		HttpUtils http=new HttpUtils();
		String url= Constant.RequestDeleteAddress+address.id+"?";
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token",token);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				Gson gson=new Gson();
				SaveMyInfo json = gson.fromJson(result, SaveMyInfo.class);
				UIUtils.showToastSafe(json.msg);
				setResult(1);
				EditAddressActivity.this.finish();

			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

	//保存地址
	private void saveAddress() {
		String name = mName.getText().toString().trim();
		String phone = mPhone.getText().toString().trim();
		String code = mCode.getText().toString().trim();
		String city = mCity.getText().toString().trim();
		String cityDetail = mCityDetail.getText().toString().trim();
		if(TextUtils.isEmpty(name)){
			UIUtils.showToastSafe("姓名不能为空");
		}else if(TextUtils.isEmpty(phone)){
			UIUtils.showToastSafe("手机号不能为空");
		}else if(TextUtils.isEmpty(city)){
			UIUtils.showToastSafe("请选择省市区");
		}else if(TextUtils.isEmpty(cityDetail)){
			UIUtils.showToastSafe("请填写详细地址");
		}else{
			upLoadAddress(name,phone,code,city,cityDetail);
		}
	}

	private void upLoadAddress(String name, String phone, String code, String city, String cityDetail) {
		String token=PreferUtils.getString(getApplicationContext(),"token",null);
		HttpUtils http=new HttpUtils();
		RequestParams params=new RequestParams();
		String url="";
		//如果address不为空的话是修改内容
		if(address!=null)
		{
			url=Constant.RequestUpdateAddress;
			String id=address.id;
			params.addBodyParameter("id",id);
		}//如果address为空的话是增加地址内容
		else{
			url= Constant.RequestAddressSave;
		}

		params.addBodyParameter("Token",token);
		params.addBodyParameter("userName",name);
		params.addBodyParameter("phone",phone);
		params.addBodyParameter("postCode",code);
		params.addBodyParameter("province","安徽省");
		params.addBodyParameter("city","宿州市");
		params.addBodyParameter("county","埇桥区");
		params.addBodyParameter("address",cityDetail);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				System.out.print(result);
				Gson gson=new Gson();
				SaveMyInfo json = gson.fromJson(result, SaveMyInfo.class);
				UIUtils.showToastSafe(json.msg);
				setResult(1);
				EditAddressActivity.this.finish();
			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

}
