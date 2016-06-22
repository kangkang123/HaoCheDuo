package cn.longchou.wholesale.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.CookieStore;
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
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyHotBrandAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.RegisterValidate;
import cn.longchou.wholesale.domain.RigisterCity;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.SystemUtils;
import cn.longchou.wholesale.utils.ToastUtils;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 用户注册页面
*
* @author kangkang
*
* @date 2016年1月14日 下午3:17:23 
*
 */
public class RegisterActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mLogin;
	private EditText mPhone;
	private EditText mCode;
	private EditText mPassWord;
	private TextView mGetCode;
	private TextView mProcotol;
	private TextView mFinish;
	
	private String number;
	private String code;
	private String password;
	private LinearLayout mLLChoose;
	private ListView mLvChoose;
	private ListView mLvProvinceChoose;
	private DrawerLayout mDrawerLayout;
	private ImageView mChooseBack;
	private TextView mConfirm;
	private TextView mEtCity;
	private List<String> list=null;
	private List<String> listProvince=null;
	private String city;
	private MyHotBrandAdapter adapter;
	private MyHotBrandAdapter provinceAdapter;
	private boolean isCity=false;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_register);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		mLogin = (TextView) findViewById(R.id.tv_my_title_login);
		
		mPhone = (EditText) findViewById(R.id.et_register_phone);
		mCode = (EditText) findViewById(R.id.et_register_code);
		mPassWord = (EditText) findViewById(R.id.et_register_password);
		
		mGetCode = (TextView) findViewById(R.id.tv_register_get_code);
		mProcotol = (TextView) findViewById(R.id.tv_register_procotol);
		mFinish = (TextView) findViewById(R.id.tv_register_finish);
		
		mEtCity = (TextView) findViewById(R.id.et_city_choose);
		mLLChoose = (LinearLayout) findViewById(R.id.ll_city_choose);
		mLvChoose = (ListView) findViewById(R.id.lv_city_choose);
		mLvProvinceChoose = (ListView) findViewById(R.id.lv_province_choose);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_city_choose);
		
		mChooseBack = (ImageView) findViewById(R.id.iv_choose_city_back);
		mConfirm = (TextView) findViewById(R.id.tv_city_choose_confirm);
	}

	@Override
	public void initData() {
		mTitle.setText("注册");
		mLogin.setVisibility(View.VISIBLE);
		mLogin.setText("登录");

		mConfirm.setVisibility(View.GONE);
		//初始化关闭侧边栏
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		adapter = new MyHotBrandAdapter(getApplicationContext(), list, "cityChoose");
		provinceAdapter = new MyHotBrandAdapter(getApplicationContext(), listProvince, "provinceChoose");
		getServerData();
		
	}

	private void getServerData() {
		String url=Constant.RequestRegisterCity;
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				if(!TextUtils.isEmpty(result))
				{
					listProvince=RigisterCity.getProvinceCity(result);
					provinceAdapter = new MyHotBrandAdapter(getApplicationContext(), listProvince, "provinceChoose");
					mLvProvinceChoose.setAdapter(provinceAdapter);
				}
			}
		});
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		
		mLogin.setOnClickListener(this);
		
		mGetCode.setOnClickListener(this);
		mProcotol.setOnClickListener(this);
		mFinish.setOnClickListener(this);
		
		mLLChoose.setOnClickListener(this);
		mChooseBack.setOnClickListener(this);
		mConfirm.setOnClickListener(this);
		
		mLvChoose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//city保存上次选中的内容
				city=Constant.cityChoose;
				//重新给选中内容赋值
				Constant.cityChoose = list.get(position);
				adapter.notifyDataSetChanged();
			}
		});
		
		mLvProvinceChoose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					mLvChoose.setVisibility(View.VISIBLE);
					mLvProvinceChoose.setVisibility(View.GONE);
					isCity=true;
					Map<String, List<String>> provinceCity = RigisterCity.getProvinceCity();
					String string = listProvince.get(position);
					list = provinceCity.get(string);
					adapter = new MyHotBrandAdapter(getApplicationContext(), list, "cityChoose");
					mLvChoose.setAdapter(adapter);
					mConfirm.setVisibility(View.VISIBLE);
			}
		});

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		case R.id.tv_my_title_login:
			Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.tv_register_get_code:
			number = mPhone.getText().toString().trim();
			if(TextUtils.isEmpty(number))
			{
				ToastUtils.showToast(getApplicationContext(), "号码不能为空");
			}
//			else if (!isMobileNO(number)) {
//				ToastUtils.showToast(getApplicationContext(), "您输入的号码不正确请重新输入");
//
//			}
			else if(!isNumber(number))
			{
				ToastUtils.showToast(getApplicationContext(), "您输入的号码不正确请重新输入");
			}else if(TextUtils.isEmpty(Constant.cityChoose))
			{
				UIUtils.showToastSafe("请选择城市!");
			}
			else{
				getSMSCode();
				countDown();
				mGetCode.setEnabled(false);
			}
			break;
		case R.id.tv_register_procotol:
			Intent intenProtocol=new Intent(RegisterActivity.this,ProtocolActivity.class);
			intenProtocol.putExtra("register", "register");
			startActivity(intenProtocol);
			break;
		case R.id.tv_register_finish:
			number = mPhone.getText().toString().trim();
			code = mCode.getText().toString().trim();
			password = mPassWord.getText().toString().trim();
			checkInput();
			break;
		case R.id.ll_city_choose:
			mDrawerLayout.openDrawer(Gravity.RIGHT);
			adapter.notifyDataSetChanged();
			isCity=false;
			mLvChoose.setVisibility(View.GONE);
			mLvProvinceChoose.setVisibility(View.VISIBLE);
			mConfirm.setVisibility(View.GONE);
			getServerData();
			break;
		case R.id.iv_choose_city_back:
			if(isCity){
				isCity=false;
				mLvChoose.setVisibility(View.GONE);
				mLvProvinceChoose.setVisibility(View.VISIBLE);
				mConfirm.setVisibility(View.GONE);
			}else{
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				//如果取消的话，让城市选择恢复原来的值
				Constant.cityChoose=city;
			}
			
			break;
		case R.id.tv_city_choose_confirm:
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			if(!TextUtils.isEmpty(Constant.cityChoose))
			{
				mEtCity.setText(Constant.cityChoose);
			}
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
		else if(!isNumber(number))
		{
			UIUtils.showToastSafe("手机号码不正确!");
		}
		else if (TextUtils.isEmpty(code)) {
			ToastUtils.showToast(getApplicationContext(), "验证码不能为空");

		}else if (TextUtils.isEmpty(password)) {
			ToastUtils.showToast(getApplicationContext(), "密码不能为空");

		} else if (!isPassword(password)) {
			ToastUtils.showToast(getApplicationContext(), "密码格式不正确");

		}else {
			getPhoneData();
		}
		
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


	private void getPhoneData() {
		number = mPhone.getText().toString().trim();
		code = mCode.getText().toString().trim();
		password = mPassWord.getText().toString().trim();
		System.out.println("number:"+number+"code:"+code+"password:"+password);
		String url=Constant.Register;
		HttpUtils http=new HttpUtils();
		RequestParams params=new RequestParams();
		params.addBodyParameter("user",number);
		params.addBodyParameter("password",password);
		params.addBodyParameter("SMS",code);
		params.addBodyParameter("udid",SystemUtils.getDeviceId());
		params.addBodyParameter("deviceType","2");
		params.addBodyParameter("c","as");
		params.addBodyParameter("step","2");
		params.addBodyParameter("area",Constant.cityChoose);
		String cookie=PreferUtils.getString(getApplicationContext(), "cookie", null);
		params.addHeader("Cookie:JSESSIONID="+cookie, url);
//		System.out.println("Cookie:"+Constant.cookieStore.toString());
		http.send(HttpMethod.POST, url,params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				Gson gson=new Gson();
				RegisterValidate json = gson.fromJson(result, RegisterValidate.class);
				String msg = json.errorMsg;
				String token=json.Token;
//				System.out.println("resultRegister:"+result);
				if (!TextUtils.isEmpty(msg))
				{
					UIUtils.showToastSafe(msg);
				}else{
					PreferUtils.putString(getApplicationContext(), "token", token);
					PreferUtils.putString(getApplicationContext(), "phone", number);
					Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
					startActivityForResult(intent, 1);
//					finish();
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
			setResult(1);
			finish();
			break;

		default:
			break;
		}
	}

	//获取验证码
	private void getSMSCode() {
		number = mPhone.getText().toString().trim();
		final HttpUtils http=new HttpUtils();
		String url=Constant.Register;
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("step", "1");
		params.addBodyParameter("user", number);
		http.send(HttpMethod.POST, url,params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("msg"+msg);
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
//				http.configCookieStore(cookieStore)
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
//					 Constant.cookieStore=value;
					 PreferUtils.putString(getApplicationContext(), "cookie", value);
			    } 

				}
//				http.configCookieStore(cookie);
				Gson gson=new Gson();
				RegisterValidate json = gson.fromJson(result, RegisterValidate.class);
				String msg = json.errorMsg;
				System.out.println("resultCode:"+result);
				if (!TextUtils.isEmpty(msg))
				{
					UIUtils.showToastSafe(msg);
				}else{
					UIUtils.showToastSafe("验证码获取成功");
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
	
	//判断是否符合密码规则
	public boolean isPassword(String password) {
		Pattern p = Pattern
				.compile("^[A-Za-z0-9@!#$_^&.-]{6,20}$");
		Matcher m = p.matcher(password);
		return m.matches();
	}

	//倒计时
	public void countDown() {
		CountDownTimer time = new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				mGetCode.setText(millisUntilFinished / 1000 + "秒后重发");
				mGetCode.setBackgroundColor(Color.rgb(155, 153, 154));
				mGetCode.setTextColor(Color.WHITE);
				mGetCode.setTextSize(15);
			}

			@Override
			public void onFinish() {
				mGetCode.setText("重新获取");
				mGetCode.setEnabled(true);
			}
		}.start();
	}

}
