package cn.longchou.wholesale.activity;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.AddressManager;
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
				

	}

	@Override
	public void initData() {
		mTitle.setText("编辑地址");
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null)
		{
			AddressManager address = (AddressManager)bundle.getSerializable("address");
			
			mName.setText(address.name);
			mPhone.setText(address.phone);
			mCity.setText(address.city);
			mDeleteAddress.setVisibility(View.VISIBLE);
		}else{
			mDeleteAddress.setVisibility(View.GONE);
		}

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mDeleteAddress.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		case R.id.tv_delete_address:
			break;

		default:
			break;
		}

	}

}
