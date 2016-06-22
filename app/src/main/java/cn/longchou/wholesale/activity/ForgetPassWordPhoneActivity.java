package cn.longchou.wholesale.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 忘记密码输入手机号码页面
*
* @author kangkang
*
* @date 2016年1月26日 下午6:53:03 
*
 */
public class ForgetPassWordPhoneActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private EditText mPhone;
	private TextView mNext;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_forget_password);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mPhone = (EditText) findViewById(R.id.et_forget_password_phone);
		
		mNext = (TextView) findViewById(R.id.tv_forget_password_phone_next);
		

	}

	@Override
	public void initData() {
		mTitle.setText("忘记密码");

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mNext.setOnClickListener(this);
		
		
		mPhone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String phone = mPhone.getText().toString().trim();
				if(!TextUtils.isEmpty(phone))
				{
					mNext.setTextColor(Color.rgb(255, 255, 255));
					mNext.setBackgroundColor(Color.rgb(237, 108, 1));
					mNext.setEnabled(true);
				}else{
					mNext.setTextColor(Color.rgb(191, 191, 191));
					mNext.setBackgroundColor(Color.rgb(244, 244, 245));
					mNext.setEnabled(false);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
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
		case R.id.tv_forget_password_phone_next:
			String phone = mPhone.getText().toString().trim();
			if(!isNumber(phone))
			{
				UIUtils.showToastSafe("手机号码不正确");
			}else{
				
				Intent intent=new Intent(ForgetPassWordPhoneActivity.this,SetNewPassWordActivity.class);
				intent.putExtra("number", phone);
				startActivityForResult(intent, 1);
			}
			break;

		default:
			break;
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 4:
			finish();
			break;

		default:
			break;
		}
	}
	
	//是否是手机号码
	private boolean isNumber(String number)
	{
		String first = number.substring(0, 1);
		if(number.length()==11)
		{
			if(first.equals("1"))
			{
				return true;
			}
		}
		return false;
	}

	
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

}
