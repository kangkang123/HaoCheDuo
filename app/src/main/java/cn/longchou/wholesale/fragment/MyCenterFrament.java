package cn.longchou.wholesale.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.BalanceActivity;
import cn.longchou.wholesale.activity.HelpCenterActivity;
import cn.longchou.wholesale.activity.HelpQuestionActivity;
import cn.longchou.wholesale.activity.IntegrationActivity;
import cn.longchou.wholesale.activity.LoginActivity;
import cn.longchou.wholesale.activity.MainActivity;
import cn.longchou.wholesale.activity.MyAttentionActivity;
import cn.longchou.wholesale.activity.MyBuyActivity;
import cn.longchou.wholesale.activity.MyInformationActivity;
import cn.longchou.wholesale.activity.MyNewsActivity;
import cn.longchou.wholesale.activity.MyOrdersActivity;
import cn.longchou.wholesale.activity.PreferentialActivity;
import cn.longchou.wholesale.activity.RegisterActivity;
import cn.longchou.wholesale.activity.SettingActivity;
import cn.longchou.wholesale.adapter.MyCenterAdapter;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.domain.HomePage;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.manage.CarsManager;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.BadgeView;
import cn.longchou.wholesale.view.ListViewForScrollView;

/**
 * 
 * @Description: 我的界面的实现
 * 
 * @author kangkang
 * 
 * @date 2015年12月30日 下午6:32:33
 * 
 */
public class MyCenterFrament extends BaseFragment {

	private RelativeLayout mRLLogin;
	private RelativeLayout mRLNoLogin;
	private TextView mTvQuit;
	private ImageView mIvHead;
	private TextView mTvName;
	private TextView mTvConfirm;
	private TextView mTvInformation;
	private TextView mTvIntegration;
	private TextView mTvMoney;
	private ImageView mIvNoHead;
	private TextView mTvRegister;
	private TextView mTvLogin;
	private ListViewForScrollView mLvMyFrist;
	private ListViewForScrollView mLvMyTwo;

	private int[] id1 = { R.drawable.newsicon, R.drawable.ordericon,
			R.drawable.attention };
	private int[] id2 = { R.drawable.buy, R.drawable.help_activity,
			R.drawable.help };

	private LinearLayout mLLIntegration;
	private LinearLayout mLLMoney;
	
	public static final int LoginRequestCode = 1;
	public static final int BalanceRequestCode=2;
	private TextView mDail;
	private ImageView mSetting;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mycenter, null);
		// 用户登录的布局文件
		mRLLogin = (RelativeLayout) view.findViewById(R.id.rl_my_login);

		// 用户未登录的布局文件
		mRLNoLogin = (RelativeLayout) view.findViewById(R.id.rl_my_no_login);

		// 用户登录时显示退出按钮
		mTvQuit = (TextView) view.findViewById(R.id.tv_my_quit_login);

		// 用户登录部分的组件
		mIvHead = (ImageView) view.findViewById(R.id.iv_my_head_login);
		mTvName = (TextView) view.findViewById(R.id.tv_my_name);
		mTvConfirm = (TextView) view.findViewById(R.id.tv_my_confirm);
		mTvInformation = (TextView) view.findViewById(R.id.tv_my_information);
		mTvIntegration = (TextView) view.findViewById(R.id.tv_my_integration);
		mTvMoney = (TextView) view.findViewById(R.id.tv_my_money);

		// 用户未登录部分的组件
		mIvNoHead = (ImageView) view.findViewById(R.id.iv_my_head_no_login);
		mTvRegister = (TextView) view.findViewById(R.id.tv_my_register);
		mTvLogin = (TextView) view.findViewById(R.id.tv_my_login);

		mLvMyFrist = (ListViewForScrollView) view
				.findViewById(R.id.lv_my_first);

		mLvMyTwo = (ListViewForScrollView) view.findViewById(R.id.lv_my_two);

		// 可用积分部分
		mLLIntegration = (LinearLayout) view
				.findViewById(R.id.ll_avail_integration);
		// 可用余额部分
		mLLMoney = (LinearLayout) view.findViewById(R.id.ll_avail_money);
		
		//拨打电话
		mDail = (TextView) view.findViewById(R.id.tv_dail);
		
		mSetting = (ImageView) view.findViewById(R.id.iv_my_setting);

		return view;

	}

	@Override
	public void initData() {
		// 判断用户是否登录,显示相应的布局
		isLogin();

		List<String> list = Constant.getMyItems();
		List<String> first = new ArrayList<String>();
		List<String> two = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (i < 3) {
				String string = list.get(i);
				first.add(string);
			} else {
				String twoString = list.get(i);
				two.add(twoString);
			}
		}

		MyCenterAdapter Firstadapter = new MyCenterAdapter(getActivity(),
				first, id1, 2);
		mLvMyFrist.setAdapter(Firstadapter);

		MyCenterAdapter Twoadapter = new MyCenterAdapter(getActivity(), two,
				id2, 2);
		mLvMyTwo.setAdapter(Twoadapter);

		String result = PreferUtils.getString(getActivity(), "myInfo", null);
		if(!TextUtils.isEmpty(result))
		{
			parseData(result);
		}
		
		getServerData();
		
	}

	// 判断用户是否登录,显示相应的布局
	public void isLogin() {
		boolean isLobin = PreferUtils.getBoolean(getActivity(), "isLogin", false);
		if (isLobin) {
			mRLLogin.setVisibility(View.VISIBLE);
			mRLNoLogin.setVisibility(View.GONE);
			mTvQuit.setVisibility(View.VISIBLE);
		} else {
			mRLLogin.setVisibility(View.GONE);
			mRLNoLogin.setVisibility(View.VISIBLE);
			mTvQuit.setVisibility(View.GONE);
		}
	}

	@Override
	public void initListener() {
		mTvQuit.setOnClickListener(this);
		//个人信息的点击
		mTvInformation.setOnClickListener(this);
		
		//用户注册
		mTvRegister.setOnClickListener(this);
		//用户登录
		mTvLogin.setOnClickListener(this);
		
		mLLIntegration.setOnClickListener(this);
		mLLMoney.setOnClickListener(this);
		
		mDail.setOnClickListener(this);
		
		mSetting.setOnClickListener(this);
		
		
		
		
		mLvMyFrist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				boolean isLogin = PreferUtils.getBoolean(getActivity(), "isLogin", false);
				if(isLogin)
				{
					if(position==0)
					{
						Intent intent=new Intent(getActivity(),MyNewsActivity.class);
						startActivity(intent);
					}else if(position==1){
						Intent intent=new Intent(getActivity(),MyOrdersActivity.class);
						startActivity(intent);
					}else if(position==2){
						Intent intent=new Intent(getActivity(),MyAttentionActivity.class);
						startActivity(intent);
					}
				}else{
					UIUtils.showToastSafe("请登录！");
				}
				
			}
		});
		
		mLvMyTwo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==0){
					Intent intent=new Intent(getActivity(),MyBuyActivity.class);
					startActivity(intent);
				}else if(position==1){
					Intent intent=new Intent(getActivity(),PreferentialActivity.class);
					startActivity(intent);
				} 
				else if(position==2)
				{
					Intent intent=new Intent(getActivity(),HelpQuestionActivity.class);
					intent.putExtra("title", "常见问题");
					startActivity(intent);
				}
				
			}
		});

	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden)
		{
			String result = PreferUtils.getString(getActivity(), "myInfo", null);
			if(!TextUtils.isEmpty(result))
			{
				parseData(result);
			}
			getServerData();
		}
	}

	
	private void getServerData() {
		String token = PreferUtils.getString(getActivity(), "token", null);
		HttpUtils http=new HttpUtils();
		String url = Constant.RequestMy;
		RequestParams params=new RequestParams();
		params.addBodyParameter("token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				parseData(result);
				
			}
		});
		
	}
	
	//获取购物车的数据
  private void getCartCarData() {
		
		String token = PreferUtils.getString(getActivity(), "token", null);
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
					MainActivity mActivity=(MainActivity) getActivity();
					BadgeView budget=mActivity.getBadgeViewText();
					budget.show();
					budget.setText(carsList.size()+"");
					PreferUtils.putInt(getActivity(), "totalCars", carsList.size());
				}else{
					PreferUtils.putInt(getActivity(), "totalCars", 0);
					
				}
			}
		});
		
	}
	
	private void parseData(String result) {
		Gson gson=new Gson();
		LoginValidate data = gson.fromJson(result,LoginValidate.class);
		if(null!=data)
		{
			if(!TextUtils.isEmpty(data.toString()))
			{
				getCartCarData();
				isLogin();
				mTvIntegration.setText(data.score+"");
				if(data.balance==0.0)
				{
					mTvMoney.setText(data.balance+"0");
				}else{
					
					mTvMoney.setText(data.balance+"");
				}
//				Constant.isCertified=data.isCertified;
				
				//在重启获取数据的时候获取是否认证，并且保存起来
				PreferUtils.putBoolean(getActivity(), "isCertified", data.isCertified);
				if(data.isCertified)
				{
					mTvConfirm.setText("认证用户");
					if(TextUtils.isEmpty(data.name))
					{
						mTvName.setText(data.phoneNumber);
					}else{
						mTvName.setText(data.name);
					}
				}else{
					mTvConfirm.setText("待认证");
					mTvName.setText(data.phoneNumber);
				}
				//把我的信息保存起来
//				Constant.myInfo=result;
				PreferUtils.putString(getActivity(), "myInfo", result);
			}
		}

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.tv_my_quit_login:
			//设置登录状态为false
//			Constant.isLogin=false;
			
			//当退出登录的时候设置登录状态为false
			PreferUtils.putBoolean(getActivity(), "isLogin", false);
			PreferUtils.putBoolean(getActivity(), "isCertified", false);

			PreferUtils.putString(getActivity(), "token", "");
			PreferUtils.putString(getActivity(), "myInfo", "");
			//重新验证登录的情况
			isLogin();
			
			BadgeView budget=MainActivity.getBadgeViewText();
			budget.hide();
			
			break;
		case R.id.tv_dail:
			Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4008596677"));
            startActivity(intent1);
			break;
		case R.id.tv_my_information:
			Intent intent = new Intent(getActivity(),
					MyInformationActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_my_register:
			Intent intentRegister = new Intent(getActivity(),
					RegisterActivity.class);
//			startActivity(intentRegister);
			startActivityForResult(intentRegister, LoginRequestCode);
			break;

		case R.id.tv_my_login:
			Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
			startActivityForResult(intentLogin, LoginRequestCode);
			break;
		case R.id.ll_avail_integration:
			Intent intentIntegration = new Intent(getActivity(),
					IntegrationActivity.class);
			startActivity(intentIntegration);
			break;
		case R.id.ll_avail_money:
			Intent intentMoney = new Intent(getActivity(),
					BalanceActivity.class);
			startActivityForResult(intentMoney, BalanceRequestCode);
			break;
		case R.id.iv_my_setting:
			Intent intentSettion=new Intent(getActivity(),SettingActivity.class);
			startActivity(intentSettion);
			break;

		default:
			break;
		}

	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case LoginRequestCode:
			if(data!=null)
			{
				getCartCarData();
				LoginValidate login = (LoginValidate) data.getExtras().getSerializable("login");
				if(TextUtils.isEmpty(login.errorMsg))
				{
					isLogin();
					mTvIntegration.setText(login.score+"");
					mTvMoney.setText(login.balance+"");
//					Constant.isCertified=login.isCertified;
					//保存登录后的数据
					PreferUtils.putBoolean(getActivity(), "isCertified", login.isCertified);
					if(login.isCertified)
					{
						mTvConfirm.setText("认证用户");
						mTvName.setText(login.phoneNumber);
					}else{
						mTvConfirm.setText("未认证");
						mTvName.setText("待认证");
					}
				}
			}
			getServerData();
		case BalanceRequestCode:
			getServerData();
			
			break;

		default:
			break;
		}
	}

}
