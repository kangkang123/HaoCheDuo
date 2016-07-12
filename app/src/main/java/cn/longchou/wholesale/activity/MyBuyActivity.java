package cn.longchou.wholesale.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
























import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyBuyAdapter;
import cn.longchou.wholesale.adapter.MyBuyDrawerAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.domain.AvailBrand;
import cn.longchou.wholesale.domain.BudgetData;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.MyBuy;
import cn.longchou.wholesale.domain.MyBuyData;
import cn.longchou.wholesale.domain.MyOrders;
import cn.longchou.wholesale.fragment.BudgetFragment.OnTitleListener;
import cn.longchou.wholesale.fragment.CarMarketFragment;
import cn.longchou.wholesale.fragment.CarMarketListFragment;
import cn.longchou.wholesale.fragment.FinanceFragment;
import cn.longchou.wholesale.fragment.BudgetFragment;
import cn.longchou.wholesale.fragment.MyCenterFrament;
import cn.longchou.wholesale.fragment.OrdersFragment;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

/**
 * 
 * @Description: 我的求购的页面
 * 
 * @author kangkang
 * 
 * @date 2016年1月4日 上午10:00:30
 * 
 */
public class MyBuyActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mName;
	private TextView mPhone;
	private ListView mLvBuy;
	private TextView mSubmit;
	private TextView mRightTitle;
	private ImageView mRightBack;
	private static DrawerLayout mDrawLayout;
	private ListView mLvRight;
	
	//选中的条目
	private ImageView image;
	private MyBuyAdapter adapter;
	private FrameLayout fl;
	
	private FragmentManager fragmentManager;
	private Bundle bundle;
	
	//预算的fragment
	private BudgetFragment mBudgetFragment;
	private FragmentTransaction ft;
	
	//设置时间
	private TextView mTime;
	
	//获取时间
	private Calendar calendar;
	
	private LoginValidate data;

	@Override
	public void initView() {
		setContentView(R.layout.activity_my_buy);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mName = (TextView) findViewById(R.id.tv_item_my_buy_name);
		mPhone = (TextView) findViewById(R.id.tv_item_my_buy_phone);
		
		mSubmit = (TextView) findViewById(R.id.tv_my_buy_submit);
		mLvBuy = (ListView) findViewById(R.id.lv_my_buy);
		
		mDrawLayout = (DrawerLayout) findViewById(R.id.dl_my_buy);
		
	}

	@Override
	public void initData() {
		mTitle.setText("车辆求购");
		
		//初始化关闭侧边栏
		mDrawLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		
		List<MyBuy> listBuy = MyBuy.getBuys();
		
		adapter = new MyBuyAdapter(getApplicationContext(), listBuy);
		mLvBuy.setAdapter(adapter);
		
        fragmentManager = getSupportFragmentManager();
		
        calendar = Calendar.getInstance();
        
        getHotBrandData();
        
        boolean isLogin = PreferUtils.getBoolean(getApplicationContext(), "isLogin", false);
        if(isLogin)
        {
        	//获取用户的行管信息
        	getServerData();
        }
        
	}
	
	//设置侧边的内容
	public static DrawerLayout getDrawLayout()
	{
	    return mDrawLayout;	
	}
	
	//获取热门品牌的内容
	private void getHotBrandData(){
		String token=PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.AvailableBrand;
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				parseData(result);
			}
		});
	}
	
	private void getServerData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url = Constant.RequestMy;
		RequestParams params=new RequestParams();
		params.addBodyParameter("token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				Gson gson=new Gson();
				data = gson.fromJson(result,LoginValidate.class);
				if(!TextUtils.isEmpty(data.toString()))
				{
					mName.setText(data.name);
					mPhone.setText(data.phoneNumber);
				}
				
			}
		});
		
	}



	protected void parseData(String result) {
		Gson gson=new Gson();
		AvailBrand data = gson.fromJson(result, AvailBrand.class);
		AvailBrand.setHotBrand(data.hotBrand);
		AvailBrand.setSortBrand(data.brand);
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		
		
		//我的求购的列表
		mLvBuy.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
               mTime = (TextView) view.findViewById(R.id.tv_item_my_buy_choose);
				//根据点击的条目的不同侧边栏设置不同的内容
                if(position==0)
                {
                	//设置城市
                	setLocation();
                }
                else if(position==1)
				{
                	//设置预算
					setBudget();
					
				}
				else if(position==2)
				{
					//设置时间
					setData();
				}
				else if(position==3)
				{
					//设置品牌
					setBrand();
					
				}else if(position==4)
				{
					if(AvailBrand.mHotBrand!=null)
					{
						//设置车系
						setCars();
					}else{
						UIUtils.showToastSafe("请先选中品牌");
					}
				}
			}
		});
	}
	
	protected void setLocation() {
		mDrawLayout.openDrawer(Gravity.RIGHT); 
		
		Bundle bundle=new Bundle();
		bundle.putInt("position", 0);
		ft = fragmentManager.beginTransaction();
		BudgetFragment fragment=new BudgetFragment();
		BudgetFragment.setBudgetContent("求购城市", new OnTitleListener() {
			
			@Override
			public void setImageBack(String city) {
				if (TextUtils.isEmpty(city)) {
				}else{
					MyBuy item = adapter.getItem(0);
					item.choose=city;
					adapter.notifyDataSetChanged();
				}
			}
		});
		fragment.setArguments(bundle);
		ft.replace(R.id.fl_my_buy, fragment);
		ft.commit();

	}

	private void setData() {
		DatePickerDialog datePickerDialog = new DatePickerDialog(MyBuyActivity.this, new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				 calendar.set(Calendar.YEAR, year);  
		         calendar.set(Calendar.MONTH, monthOfYear);  
		         calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); 
		         BudgetData.myBuyTime=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
				 mTime.setText(BudgetData.myBuyTime);
				 MyBuy item = adapter.getItem(2);
				 item.choose=BudgetData.myBuyTime;
				 adapter.notifyDataSetChanged();
				 
			}
		}, calendar  
        .get(Calendar.YEAR), calendar  
        .get(Calendar.MONTH), calendar  
        .get(Calendar.DAY_OF_MONTH));  
//		DatePicker datePicker = datePickerDialog.getDatePicker();
        datePickerDialog.show(); 
        

	}

	
	//设置车系
	private void setCars() {
		mDrawLayout.openDrawer(Gravity.RIGHT); 
		
		Bundle bundle=new Bundle();
		bundle.putInt("position", 4);
		ft = fragmentManager.beginTransaction();
		BudgetFragment fragment=new BudgetFragment();
		BudgetFragment.setBudgetContent("车系", new OnTitleListener() {
			
			@Override
			public void setImageBack(String city) {
				if (TextUtils.isEmpty(city)) {
				}else{
					MyBuy item = adapter.getItem(4);
					item.choose=city;
					adapter.notifyDataSetChanged();
				}
			}
		});
		fragment.setArguments(bundle);
		ft.replace(R.id.fl_my_buy, fragment);
		ft.commit();

	}
	
	
	
	//设置品牌
	private void setBrand() {
		mDrawLayout.openDrawer(Gravity.RIGHT); 
		
		Bundle bundle=new Bundle();
		bundle.putInt("position", 3);
		ft = fragmentManager.beginTransaction();
		BudgetFragment fragment=new BudgetFragment();
		BudgetFragment.setBudgetContent("品牌", new OnTitleListener() {
			
			@Override
			public void setImageBack(String city) {
				if (TextUtils.isEmpty(city)) {
				}else{
					MyBuy item = adapter.getItem(3);
					item.choose=city;
					adapter.notifyDataSetChanged();
				}
			}
		});
		fragment.setArguments(bundle);
		ft.replace(R.id.fl_my_buy, fragment);
		ft.commit();

	}
	
	//设置预算
	private void setBudget() {
		mDrawLayout.openDrawer(Gravity.RIGHT); 
		
		Bundle bundle=new Bundle();
		bundle.putInt("position", 1);
		ft = fragmentManager.beginTransaction();
		BudgetFragment fragment=new BudgetFragment();
		BudgetFragment.setBudgetContent("预算", new OnTitleListener() {
			
			@Override
			public void setImageBack(String city) {
				if (TextUtils.isEmpty(city)) {
				}else{
					MyBuy item = adapter.getItem(1);
					item.choose=city;
					adapter.notifyDataSetChanged();
				}
			}
		});
		fragment.setArguments(bundle);
		ft.replace(R.id.fl_my_buy, fragment);
		ft.commit();

	}


	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		case R.id.tv_my_buy_submit:
			chooseFinish();
            break;
		default:
			break;
		}

	}
	
	//提交数据
	private void myBuySubmit() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestMySeek;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("name",mName.getText().toString().trim());
		params.addBodyParameter("phone", mPhone.getText().toString().trim());
		params.addBodyParameter("city", CityLocation.mBuyCity);
		params.addBodyParameter("budge", BudgetData.budget);
		params.addBodyParameter("date", BudgetData.myBuyTime);
		params.addBodyParameter("brand", AvailBrand.mHotBrand);
		params.addBodyParameter("series", AvailBrand.mHotCarLine);
		params.addBodyParameter("deviceType", 2+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resulInfo) {
				String result=resulInfo.result;
				Gson gson=new Gson();
				MyBuyData data = gson.fromJson(result, MyBuyData.class);
				if(TextUtils.isEmpty(data.errorReason))
				{
					UIUtils.showToastSafe("提交成功");
				}else{
					UIUtils.showToastSafe(data.errorReason);
				}
				
			}
		});
		
	}

	private void chooseFinish() {
		String name = mName.getText().toString().trim();
		String phone = mPhone.getText().toString().trim();
		if(TextUtils.isEmpty(name))
		{
			UIUtils.showToastSafe("请输入姓名！");
		}else if(TextUtils.isEmpty(phone))
		{
			UIUtils.showToastSafe("请输入号码！");
		}
		else if(TextUtils.isEmpty(CityLocation.mBuyCity))
		{
			UIUtils.showToastSafe("请选择城市！");
		}
		else if(TextUtils.isEmpty(BudgetData.budget)){
			UIUtils.showToastSafe("请选择预算！");
		
		}else if(TextUtils.isEmpty(BudgetData.myBuyTime)){
			UIUtils.showToastSafe("请选择时间！");
			
		}else if(TextUtils.isEmpty(AvailBrand.mHotBrand)){
			UIUtils.showToastSafe("请选择品牌！");
			
		}else if(TextUtils.isEmpty(AvailBrand.mHotCarLine)){
			UIUtils.showToastSafe("请选择车系！");
		}else{
			myBuySubmit();
		}
	}

}
