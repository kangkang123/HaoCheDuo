package cn.longchou.wholesale.activity;

import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

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
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.ChangePhone;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.Withdraw;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

public class VerificationCodeActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private EditText mCode;
	private TextView mGetCode;
	private TextView mNext;
	
	//获取到的验证码
	private String code;
	
	private static String defaultCode="1234";
	private TextView mPhone;
	private LoginValidate data;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_verification_code);
	
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mCode = (EditText) findViewById(R.id.et_get_code);
		mGetCode = (TextView) findViewById(R.id.tv_get_code);
		
		mNext = (TextView) findViewById(R.id.tv_get_code_next);
		
		mPhone = (TextView) findViewById(R.id.tv_change_phone_get_number);

	}

	@Override
	public void initData() {
		mTitle.setText("获取验证码");
		String result = PreferUtils.getString(getApplicationContext(), "myInfo", null);
		if(!TextUtils.isEmpty(result))
		{
			Gson gson=new Gson();
			data = gson.fromJson(result,LoginValidate.class);
			if(data!=null)
			{
				mPhone.setText("请输入"+data.phoneNumber+"收到的短信验证码");
			}
		}
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mGetCode.setOnClickListener(this);
		mNext.setOnClickListener(this);
		
		mCode.addTextChangedListener(new TextWatcher() {
			
			

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				code = mCode.getText().toString();
				if(!TextUtils.isEmpty(code))
				{
					mNext.setTextColor(Color.rgb(255, 255, 255));
					mNext.setBackgroundColor(Color.rgb(237, 108, 1));
					mNext.setEnabled(true);
				}else{
					mNext.setTextColor(Color.rgb(191, 191, 191));
					mNext.setBackgroundColor(Color.rgb(224, 224, 225));
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

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		case R.id.tv_get_code:
			countDown();
			//获取验证码
			getCodeData();
            break;
		case R.id.tv_get_code_next:
			
			//提交输入的验证码
			getCommitCode();
            break;
		
		default:
			break;
		}

	}

	//提交输入的验证码
	private void getCommitCode() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		String cookie = PreferUtils.getString(getApplicationContext(), "cookie", null);
		final HttpUtils http=new HttpUtils();
		String url = Constant.RequestResetPhone;
		RequestParams params=new RequestParams();
		params.addBodyParameter("step", "2");
		params.addBodyParameter("Token", token);
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
			   if(data!=null && data.correct)
			   {
				   Intent intent=new Intent(VerificationCodeActivity.this,SetNewPhoneActivity.class);
				   startActivityForResult(intent, 1);
			   }else{
				   UIUtils.showToastSafe(data.errorMsg);
			   }
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			finish();
			break;

		default:
			break;
		}
	}
	
	

	//获取验证码
	private void getCodeData() {

		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		final HttpUtils http=new HttpUtils();
		String url = Constant.RequestResetPhone;
		RequestParams params=new RequestParams();
		params.addBodyParameter("step", "1");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("phone", data.phoneNumber);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				
				DefaultHttpClient httpClient = (DefaultHttpClient) http.getHttpClient();
				List<Cookie> cookies = httpClient.getCookieStore().getCookies();
				if (cookies.isEmpty()) 
				{  
					System.out.println("Cookie为空");
				}
				else
				{                   
					 for (int i = 0; i < cookies.size(); i++ )
					 {    
						 //保存cookie     
						 Cookie cookie = cookies.get(i);
						 String name = cookie.getName();
						 String value = cookie.getValue();
						 PreferUtils.putString(getApplicationContext(), "cookie", value);
			         } 
			   }
			   Gson gson=new Gson();
			   ChangePhone data = gson.fromJson(result, ChangePhone.class);
			   if(data!=null && !data.success)
			   {
				   UIUtils.showToastSafe(data.errorMsg);
			   }
			}
		});
		
	 }
		
}
