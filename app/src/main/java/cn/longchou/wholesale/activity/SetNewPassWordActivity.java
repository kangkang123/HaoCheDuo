package cn.longchou.wholesale.activity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.RegisterValidate;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.SystemUtils;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 设置新密码
*
* @author kangkang
*
* @date 2016年1月26日 下午7:18:26 
*
 */
public class SetNewPassWordActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private EditText mCode;
	private TextView mGetCode;
	private EditText mPassWord;
	private TextView mFinish;
	
	private static String defaultCode="1234";
	//获取到的手机号码
	private String code;
	//获取到的验证码
	private String password;
	private TextView mPhone;
	private String number;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_set_new_password);
	
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mPhone = (TextView) findViewById(R.id.tv_set_new_password_sms);
		
		mCode = (EditText) findViewById(R.id.et_set_password_code);
		
		mGetCode = (TextView) findViewById(R.id.tv_set_password_get_code);
		
		mPassWord = (EditText) findViewById(R.id.et_set_password_new_password);
		
		mFinish = (TextView) findViewById(R.id.tv_set_password_finish);
		

	}

	@Override
	public void initData() {
		mTitle.setText("设置新密码");
		
		
		number = getIntent().getStringExtra("number");
		mPhone.setText(number);
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mGetCode.setOnClickListener(this);
		mFinish.setOnClickListener(this);
		
		
		//验证码输入时的监听
		mPassWord.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				password = mPassWord.getText().toString();
				code = mCode.getText().toString().trim();
				if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(code))
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
		case R.id.tv_set_password_get_code:
			countDown();
			getCode();
			break;
		case R.id.tv_set_password_finish:
			password = mPassWord.getText().toString();
			checkPassWord();
			break;
		
		default:
			break;
		}

	}
	
	private void getCode() {
		String url=Constant.ForgetPassword;
		final HttpUtils http=new HttpUtils();
		RequestParams params=new RequestParams();
		params.addBodyParameter("user",number);
		params.addBodyParameter("c","as");
		params.addBodyParameter("step","1");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				System.out.println("第一步："+result.toString());
				DefaultHttpClient httpClient = (DefaultHttpClient) http.getHttpClient();
				List<Cookie> cookies = httpClient.getCookieStore().getCookies();
				if (cookies.isEmpty()) {  
					System.out.println("Cookie为空");
				}
				else {                   
					 for (int i = 0; i < cookies.size(); i++ ) {    
					 //保存cookie     
					 Cookie cookie = cookies.get(i);
					 String name = cookie.getName();
					 String value = cookie.getValue();
					 System.out.println("name:"+name+"value:"+value);  
					 PreferUtils.putString(getApplicationContext(), "cookieForget", value);
			    } 

				Gson gson=new Gson();
				RegisterValidate json = gson.fromJson(result, RegisterValidate.class);
				if(TextUtils.isEmpty(json.errorMsg))
				{
					UIUtils.showToastSafe("验证码获取成功");
				}else{
					UIUtils.showToastSafe(json.errorMsg);
				}
			 }
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
	
	//检查手机号码是否正确
	private void checkPassWord() {
		if (TextUtils.isEmpty(password)) {
			UIUtils.showToastSafe("验证码不能为空");

		} else if (!isPassword(password)) {

			UIUtils.showToastSafe("您输入密码格式，不正确请重新输入");
		}else{
//			UIUtils.showToastSafe("修改成功");
			getNewPasswordData();
//			Intent intent=new Intent(SetNewPassWordActivity.this,LoginActivity.class);
//			startActivity(intent);
		}
	}
	
	private void getNewPasswordData() {
		String url=Constant.ForgetPassword;
		HttpUtils http=new HttpUtils();
		RequestParams params=new RequestParams();
		params.addBodyParameter("user",number);
		params.addBodyParameter("password",password);
		params.addBodyParameter("SMS",code);
		params.addBodyParameter("c","as");
		params.addBodyParameter("step","2");
		String cookie=PreferUtils.getString(getApplicationContext(), "cookieForget", null);
		params.addHeader("Cookie:JSESSIONID="+cookie, url);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				System.out.println("第二步："+result.toString());
				Gson gson=new Gson();
				RegisterValidate json = gson.fromJson(result, RegisterValidate.class);
				if(TextUtils.isEmpty(json.errorMsg))
				{
					UIUtils.showToastSafe("设置密码成功!");
//					Intent intent=new Intent(SetNewPassWordActivity.this,LoginActivity.class);
//					startActivity(intent);
					setResult(4);
					finish();
				}else{
					UIUtils.showToastSafe(json.errorMsg);
				}
				
			}
		});
		
	}

	// 检查验证码是否正确
	public boolean isPassword(String password) {
		Pattern p = Pattern
				.compile("^[A-Za-z0-9@!#$_^&.-]{6,20}$");
		Matcher m = p.matcher(password);
		return m.matches();
	}

}
