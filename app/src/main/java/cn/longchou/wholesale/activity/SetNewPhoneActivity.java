package cn.longchou.wholesale.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.ChangePhone;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

public class SetNewPhoneActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private EditText mNumber;
	private TextView mGetCode;
	private EditText mCode;
	private TextView mFinish;
	
	private static String defaultCode="1234";
	//获取到的手机号码
	private String number;
	//获取到的验证码
	private String code;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_set_new_phone);
	
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mNumber = (EditText) findViewById(R.id.et_get_code_new_number);
		
		mGetCode = (TextView) findViewById(R.id.tv_get_code_new_number);
		
		mCode = (EditText) findViewById(R.id.et_new_number_code);
		
		mFinish = (TextView) findViewById(R.id.tv_new_number_finish);
		

	}

	@Override
	public void initData() {
		mTitle.setText("设置新手机号");

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mGetCode.setOnClickListener(this);
		mFinish.setOnClickListener(this);
		
		
		//验证码输入时的监听
		mCode.addTextChangedListener(new TextWatcher() {
			
			

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				code = mCode.getText().toString();
				if(!TextUtils.isEmpty(code))
				{
					mFinish.setTextColor(Color.rgb(255, 255, 255));
					mFinish.setBackgroundColor(Color.rgb(237, 108, 1));
					mFinish.setEnabled(true);
				}else{
					mFinish.setTextColor(Color.rgb(191, 191, 191));
					mFinish.setBackgroundColor(Color.rgb(224, 224, 225));
					mFinish.setEnabled(false);
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
		case R.id.tv_get_code_new_number:
			number = mNumber.getText().toString().trim();
			checkNumber();
			break;
		case R.id.tv_new_number_finish:
			checkCode();
			break;
		
		default:
			break;
		}

	}
	
	//验证码倒计时
	public void countDown() {
		CountDownTimer time = new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				mGetCode.setText(millisUntilFinished / 1000 + "秒后重新获取");
				mGetCode.setTextColor(Color.rgb(153, 153, 153));
				mGetCode.setEnabled(false);
			}

			@Override
			public void onFinish() {
				mGetCode.setText("重新获取验证码");
				mGetCode.setTextColor(Color.rgb(153, 153, 153));
				mGetCode.setEnabled(true);
			}
		}.start();
	}
	
	//检查验证码是否正确
	private void checkCode() {
		if(TextUtils.isEmpty(code)) {
			
			UIUtils.showToastSafe("验证码不能为空");
		} else{
			//更改手机号码的最后一步
			getCommitCodeData();
		}
	}
	
	//更改手机号码的最后一步
	private void getCommitCodeData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		String cookie = PreferUtils.getString(getApplicationContext(), "cookie", null);
		final HttpUtils http=new HttpUtils();
		String url = Constant.RequestResetPhone;
		RequestParams params=new RequestParams();
		params.addBodyParameter("step", "4");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("newPhone", number);
		params.addBodyParameter("SMS", code);
		params.addHeader("Cookie:JSESSIONID="+cookie, url);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
			   String result=resultInfo.result;
				
			   Gson gson=new Gson();
			   ChangePhone data = gson.fromJson(result, ChangePhone.class);
			   if(data!=null && !data.success)
			   {
				   UIUtils.showToastSafe(data.errorMsg);
			   }else{
				   UIUtils.showToastSafe("号码修改成功");
				   PreferUtils.putString(getApplicationContext(), "token", data.newToken);
//				   Intent intent=new Intent(SetNewPhoneActivity.this,MyInformationActivity.class);
//				   startActivity(intent);
				   setResult(1);
				   finish();
			   }
			}
		});
	}
	
	//检查手机号码是否正确
	private void checkNumber() {
		if (TextUtils.isEmpty(number)) {
			UIUtils.showToastSafe("电话号码不能为空");

		} 
//		else if (!isMobileNO(number)) {
//
//			UIUtils.showToastSafe("您输入的号码不正确请重新输入");
//		}
		else if(!isNumber(number))
		{
			UIUtils.showToastSafe("您输入的号码不正确请重新输入");
		}
		else{
			countDown();
			//获取新手机号码验证码
			getNewNumberCode();
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

	
	//获取新手机号码验证码
	private void getNewNumberCode() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		String cookie = PreferUtils.getString(getApplicationContext(), "cookie", null);
		
		final HttpUtils http=new HttpUtils();
		String url = Constant.RequestResetPhone;
		RequestParams params=new RequestParams();
		params.addBodyParameter("step", "3");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("newPhone", number);
		params.addHeader("Cookie:JSESSIONID="+cookie, url);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
			   String result=resultInfo.result;
				
			   Gson gson=new Gson();
			   ChangePhone data = gson.fromJson(result, ChangePhone.class);
			   if(data!=null && !data.success)
			   {
				   UIUtils.showToastSafe(data.errorMsg);
			   }
			}
		});
	}

	// 检查电话号码是否正确
	private boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

}
