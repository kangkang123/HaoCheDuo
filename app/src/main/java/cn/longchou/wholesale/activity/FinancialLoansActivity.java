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

import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.kang.dialog.library.AlertsDialog;
import cn.kang.dialog.library.AlertsDialog.OnConfirmListener;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.QueryTrust;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.MySwitch;
/**
 * 
* @Description: 
*
* @author kangkang
*
* @date 2016年2月17日 下午2:38:52 
*
 */
public class FinancialLoansActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private EditText mName;
	private EditText mNumber;
	private ToggleButton mKR;
	private ToggleButton mSC;
	private ToggleButton mXF;
	private TextView mSubmit;
	
	private ToggleButton mSX;
	
	//是否授信
	private boolean isTrust;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_financial_loans);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mName = (EditText) findViewById(R.id.et_finance_loans_name);
		mNumber = (EditText) findViewById(R.id.et_finance_loans_number);
		
		//库容的按钮
		mKR = (ToggleButton) findViewById(R.id.tb_kr);
		//收车贷的按钮
		mSC = (ToggleButton) findViewById(R.id.tb_sc);
		//消费贷的按钮
		mXF = (ToggleButton) findViewById(R.id.tb_xf);
		
		mSubmit = (TextView) findViewById(R.id.tv_finance_loans_commit);
		
		mSX = (ToggleButton) findViewById(R.id.tb_sx);
	}

	@Override
	public void initData() {
		mTitle.setText("申请融资");

		String plan = getIntent().getStringExtra("plan");
//		UIUtils.showToastSafe(plan);
		
		String myInfo = PreferUtils.getString(getApplicationContext(), "myInfo", null);
//		String myInfo = Constant.myInfo;
		Gson gson=new Gson();
		LoginValidate data = gson.fromJson(myInfo,LoginValidate.class);
		if(null!=data)
		{
			if(!TextUtils.isEmpty(data.phoneNumber))
			{
				mNumber.setText(data.phoneNumber);
			}
			if(!TextUtils.isEmpty(data.name))
			{
				mName.setText(data.name);
			}
		}
		
		
		if("kc".equals(plan))
		{
			mKR.setChecked(true);
			
		}else if("sc".equals(plan)){
			
			mSC.setChecked(true);
			
		}else if("xf".equals(plan)){
			
			mXF.setChecked(true);
			
		}
		
		//获取是否授信
		getIsTrust();
		
	}

	//获取是否授信
	private void getIsTrust() {
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestQueryTrust;
		RequestParams params=new RequestParams();
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {


			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resuInfo) {
				String result=resuInfo.result;
				Gson gson=new Gson();
				QueryTrust data = gson.fromJson(result, QueryTrust.class);
				if(null!=data)
				{
					//获取是否授信
					isTrust = data.isTrust;
					if(isTrust)
					{
						mSX.setChecked(false);
					}else{
						mSX.setChecked(true);
					}
				}
			}
		});
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		
		//授信的点击事件
		mSX.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				if(isChecked){
					
				}else{
					if(!isTrust && (mKR.isChecked() || mSC.isChecked()))
					{
						mSX.setChecked(true);
					}else{
						mSX.setChecked(false);
					}
				}
			}
		});
		
		//库容的点击事件
		mKR.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					if(isTrust)
					{
						mSX.setChecked(false);
					}else{
						mSX.setChecked(true);
					}
				}else{
					//未选中
				}
			}
		});
		
		//收车的点击事件
		mSC.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					if(isTrust)
					{
						mSX.setChecked(false);
					}else{
						mSX.setChecked(true);
					}
				}else{
					//未选中
				}
			}
		});
		
		//消费的点击事件
		mXF.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					//选中
				}else{
					//未选中
				}
			}
		});
		
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		case R.id.tv_finance_loans_commit:
			String name = mName.getText().toString().trim();
			String phone = mNumber.getText().toString().trim();
			if(TextUtils.isEmpty(name))
			{
				UIUtils.showToastSafe("姓名不能为空!");
			}else if(TextUtils.isEmpty(phone))
			{
				UIUtils.showToastSafe("号码不能为空!");
			}else if(!isMobileNO(phone)){
				UIUtils.showToastSafe("电话号码格式不正确!");
			}else{
				//提交授信的内容
				commitTrust(name,phone);
			}
			break;
		default:
			break;
		}

	}
	// 检查电话号码是否正确
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	//提交授信的内容
	private void commitTrust(String name, String phone) {
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestApply;
		RequestParams params=new RequestParams();
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("trust", isTrust+"");
		params.addBodyParameter("kucun", mKR.isChecked()+"");
		params.addBodyParameter("shouche", mSC.isChecked()+"");
		params.addBodyParameter("xiaofei", mXF.isChecked()+"");
		params.addBodyParameter("name", name);
		params.addBodyParameter("phone", phone);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {


			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resuInfo) {
				String result=resuInfo.result;
				Gson gson=new Gson();
				QueryTrust data = gson.fromJson(result, QueryTrust.class);
				//获取是否授信
				if(null!=data)
				{
					if(data.success)
					{
						showAlertDialog();
					}else{
						showErrorDialog();
					}
					
				}
			}
		});
	}

	private void showAlertDialog() {
		AlertsDialog.showDialog(FinancialLoansActivity.this, "提交", "提交成功", new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	private void showErrorDialog() {
		AlertsDialog.showDialog(FinancialLoansActivity.this, "提交", "提交失败", new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

}
