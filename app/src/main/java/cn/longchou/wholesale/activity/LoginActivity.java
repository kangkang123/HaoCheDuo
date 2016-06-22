package cn.longchou.wholesale.activity;

import java.util.List;
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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.HomePage;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.SystemUtils;
import cn.longchou.wholesale.utils.ToastUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.BadgeView;
/**
 * 
* @Description: 用户登录页面
*
* @author kangkang
*
* @date 2016年1月14日 下午3:17:47 
*
 */
public class LoginActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mRegister;
	private EditText mPhone;
	private EditText mPassword;
	private TextView mFinish;
	private TextView mForget;
	
	private String number;
	private String password;
	
	private static int LoginResultCode=1;

	@Override
	public void initView() {
		setContentView(R.layout.activity_login);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		mRegister = (TextView) findViewById(R.id.tv_my_title_login);
		
		mPhone = (EditText) findViewById(R.id.et_login_phone); 
		mPassword = (EditText) findViewById(R.id.et_login_password); 
		
		mFinish = (TextView) findViewById(R.id.tv_login_finish);
		mForget = (TextView) findViewById(R.id.tv_forget_password);
	}

	@Override
	public void initData() {
		mTitle.setText("登录");
		mRegister.setVisibility(View.VISIBLE);
		mRegister.setText("注册");
		
		String phone = PreferUtils.getString(getApplicationContext(), "phone", null);
		if(!TextUtils.isEmpty(phone))
		{
			mPhone.setText(phone);
		}

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mRegister.setOnClickListener(this);
		mFinish.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mForget.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;
		case R.id.tv_my_title_login:
			Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.tv_login_finish:
			number = mPhone.getText().toString().trim();
			password = mPassword.getText().toString().trim();
			checkInput();
			break;
		case R.id.tv_forget_password:
			Intent intentForget=new Intent(LoginActivity.this,ForgetPassWordPhoneActivity.class);
			startActivity(intentForget);
			break;
		default:
			break;
		}

	}
	
	private void checkInput() {
		if (TextUtils.isEmpty(number)) {
			ToastUtils.showToast(getApplicationContext(), "电话号码不能为空");

		} 
//		else if (!isMobileNO(number)) {
//			ToastUtils.showToast(getApplicationContext(), "您输入的号码不正确请重新输入");
//
//		}
		else if(!isNumber(number)){
			UIUtils.showToastSafe("手机号码不正确!");
		}
		else if (TextUtils.isEmpty(password)) {
			ToastUtils.showToast(getApplicationContext(), "密码不能为空");

		}else if (!isPassword(password)) {
			ToastUtils.showToast(getApplicationContext(), "密码格式不正确");

		}else{
//			ToastUtils.showToast(getApplicationContext(), "提交");
			getData();
		}
	}
	
	private void getData() {
		String url=Constant.Login;
		number = mPhone.getText().toString().trim();
		password = mPassword.getText().toString().trim();
		HttpUtils http=new HttpUtils();
		RequestParams params=new RequestParams();
		params.addBodyParameter("user",number);
		params.addBodyParameter("password",password);
		params.addBodyParameter("udid",SystemUtils.getDeviceId());
		params.addBodyParameter("deviceType","2");
		params.addBodyParameter("c","as");
		
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println("1:"+arg1.toString());
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				System.out.println("result:"+resultInfo.result);
				String result=resultInfo.result;
				Gson gson=new Gson();
				LoginValidate login = gson.fromJson(result, LoginValidate.class);
				if(TextUtils.isEmpty(login.errorMsg))
				{
					System.out.println("token:"+login.Token);
					PreferUtils.putString(getApplicationContext(), "token", login.Token);
					PreferUtils.putString(getApplicationContext(), "loginInfo", result);
					PreferUtils.putString(getApplicationContext(), "phone", number);
					
					PreferUtils.putBoolean(getApplicationContext(), "isLogin", true);
					PreferUtils.putBoolean(getApplicationContext(), "isCertified", login.isCertified);
					PreferUtils.putString(getApplicationContext(), "password", password);
//					Constant.isLogin=true;
//					Constant.isCertified=login.isCertified;
					
					getCartCarData();
					
					Intent intent=new Intent();
					Bundle bundle=new Bundle();
					bundle.putSerializable("login", login);
					intent.putExtras(bundle);
					setResult(LoginResultCode, intent);
					finish();
				}else{
					UIUtils.showToastSafe(login.errorMsg);
				}
				
			}
		});
		
	}
	
	//获取购物车的数据
	  private void getCartCarData() {
			
			String token = PreferUtils.getString(getApplicationContext(), "token", null);
			HttpUtils http=new HttpUtils();
			String url=Constant.RequestCartCar;
			RequestParams params=new RequestParams();
			params.addBodyParameter("Token", token);
			http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {


				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String result = responseInfo.result;
					Gson gson=new Gson();
					HomePage json = gson.fromJson(result, HomePage.class);
					
					
					List<Cars> carsList = json.cars;
					if(carsList!=null && carsList.size()>0)
					{
						BadgeView budget=MainActivity.getBadgeViewText();
						budget.show();
						budget.setText(carsList.size()+"");
						PreferUtils.putInt(getApplicationContext(), "totalCars", carsList.size());
					}else{
						PreferUtils.putInt(getApplicationContext(), "totalCars", 0);
						
					}
				}
			});
			
		}
	
	//是否是手机号码
	private boolean isNumber(String number)
	{
		String first = number.substring(0, 1);
//		UIUtils.showToastSafe(number.length()+":"+first);
		if(number.length()==11)
		{
			if(first.equals("1"))
			{
				return true;
			}
		}
		return false;
	}

	private boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	public boolean isPassword(String password) {
		Pattern p = Pattern
				.compile("^[A-Za-z0-9@!#$_^&.-]{6,20}$");
		Matcher m = p.matcher(password);
		return m.matches();
	}

}
