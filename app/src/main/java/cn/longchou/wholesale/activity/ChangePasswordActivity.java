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
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.ChangePassword;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.Withdraw;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

public class ChangePasswordActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mLoginName;
	private TextView mEtLogin;
	private TextView mEtNew;
	private TextView mTvFinish;
	private String oldPassWord;
	private String newPassWord;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_change_password);
		
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mLoginName = (TextView) findViewById(R.id.tv_chang_password_login_name);
		mEtLogin = (TextView) findViewById(R.id.et_change_password_login);
		mEtNew = (TextView) findViewById(R.id.et_change_password_login_new);

		mTvFinish = (TextView) findViewById(R.id.tv_change_password_finish);
	}

	@Override
	public void initData() {
		mTitle.setText("更改密码");
		String result = PreferUtils.getString(getApplicationContext(), "myInfo", null);
		if(!TextUtils.isEmpty(result))
		{
			Gson gson=new Gson();
			LoginValidate data = gson.fromJson(result,LoginValidate.class);
			if(data!=null)
			{
				mLoginName.setText(data.phoneNumber);
			}
		}

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mTvFinish.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		case R.id.tv_change_password_finish:
			oldPassWord = mEtLogin.getText().toString().trim();
			newPassWord = mEtNew.getText().toString().trim();
			String password = PreferUtils.getString(getApplicationContext(), "password", null);
			if(TextUtils.isEmpty(oldPassWord))
			{
				UIUtils.showToastSafe("旧密码不能为空!");
			}else if(TextUtils.isEmpty(newPassWord))
			{
				UIUtils.showToastSafe("新密码不能为空!");
			}
			else if(!isPassword(oldPassWord))
			{
				UIUtils.showToastSafe("密码格式不正确!");
			}else if(!isPassword(newPassWord))
			{
				UIUtils.showToastSafe("密码格式不正确!");
			}
			else if(!password.equals(oldPassWord))
			{
				UIUtils.showToastSafe("旧密码不正确!");
			}
			else{
				
				getServerData();
			}
			break;
		
		default:
			break;
		}

	}
	
	private boolean isPassword(String password) {
		Pattern p = Pattern
				.compile("^[A-Za-z0-9@!#$_^&.-]{6,20}$");
		Matcher m = p.matcher(password);
		return m.matches();
	}


	private void getServerData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url = Constant.RequestResetPassword;
		RequestParams params=new RequestParams();
		params.addBodyParameter("old", oldPassWord);
		params.addBodyParameter("new", newPassWord);
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				Gson gson=new Gson();
				ChangePassword data = gson.fromJson(result, ChangePassword.class);
				if(data.success)
				{
					PreferUtils.putString(getApplicationContext(), "token", data.newToken);
					UIUtils.showToastSafe("更改密码成功");
					finish();
				}else{
					UIUtils.showToastSafe(data.errorMsg);
				}
			}
		});
		
	}

}
