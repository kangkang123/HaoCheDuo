package cn.longchou.wholesale.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.Subject;

import com.alipay.sdk.pay.demo.PayDemoActivity;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mingle.widget.LoadingView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyOrdersDetailAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.HomePage;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.domain.CarsInfo;
import cn.longchou.wholesale.domain.MyNews;
import cn.longchou.wholesale.domain.MyOrders;
import cn.longchou.wholesale.domain.MyOrders.Order;
import cn.longchou.wholesale.domain.OrderPayState;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

public class MyOrdersDetailActivity extends BaseActivity {


	private ImageView mBack;
	private TextView mTitle;
	
	ImageView round;
	TextView title;
	ImageView mark;
	TextView time;
	TextView content;
	private ListView mLvOrder;
	private TextView mState;
	private TextView mTime;
	private TextView mOrderNumber;
	private TextView mCancelOrder;
	private TextView mPayNow;
	private String pay;
	
	public float TotalMoney=0;
	private static final int RESULT_ORDER=1;
	private Order order;
	private String state;
	private String orderNumber=null;
	private List<Cars> cars;
	
	int day, hour , miniute , second ;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				Bundle bundle = msg.getData();
				runtime();
				break;

			default:
				break;
			}
		};
	};
	private LoadingView loading;

	@Override
	public void initView() {
		setContentView(R.layout.activity_my_orders_detail);
        mBack = (ImageView) findViewById(R.id.iv_my_news_back);
        mTitle = (TextView) findViewById(R.id.tv_my_news_title);
        
        mState = (TextView) findViewById(R.id.tv_my_orders_detail);
        
        mTime = (TextView) findViewById(R.id.tv_my_orders_time);
        
        mLvOrder = (ListView) findViewById(R.id.lv_my_orders_detail);
        
        //订单编号
        mOrderNumber = (TextView) findViewById(R.id.tv_my_orders_number);
        
        //取消订单
        mCancelOrder = (TextView) findViewById(R.id.tv_my_orders_cancel_order);
        //立即支付
        mPayNow = (TextView) findViewById(R.id.tv_my_orders_pay_now);
        
        loading = (LoadingView) findViewById(R.id.loadView);
        
        
	}

	@Override
	public void initData() {
		//当是支付界面跳转过来时
		pay = getIntent().getStringExtra("pay");
		state = getIntent().getStringExtra("state");
		orderNumber = getIntent().getStringExtra("orderNumber");
		
		
		//这个是从订单列表界面进来的
		if(getIntent().getExtras()!=null)
		{
			order = (Order) getIntent().getExtras().getSerializable("Order");
		}
        mTitle.setText("订单详情");
        
        //订单详情的数据进行判断，支付列表过来
        if(null!=order)
        {
        	 orderNumber=order.orderID;
        	 mOrderNumber.setText("订单编号:"+orderNumber);
        	 String state=order.orderState;
             setOrderState(state);
             getServerData(orderNumber);
        }
        
        //从支付界面过来的
        if(!TextUtils.isEmpty(state))
        {
        	setOrderState(state);
        	getServerData(orderNumber);
        	mOrderNumber.setText("订单编号:"+orderNumber);
        }
       
	}
	
	//根据订单状态设置
	private void setOrderState(String state) {
		 if("已支付".equals(state))
  		{
          	mState.setText("已支付");
          	Drawable drawable = getResources().getDrawable( R.drawable.orders_pay);
              drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
              mState.setCompoundDrawables(drawable, null, null, null);
              mTime.setVisibility(View.INVISIBLE);
              
              
  		}else if("待支付".equals(state))
  		{
  			mState.setText("待支付");	
  			mState.setTextColor(Color.rgb(237, 108, 1));
  			mTime.setVisibility(View.VISIBLE);
  			
  			mCancelOrder.setVisibility(View.VISIBLE);
             mPayNow.setVisibility(View.VISIBLE);
  		}else if("已取消".equals(state))
  		{
  			mState.setText("已取消");	
  			Drawable drawable = getResources().getDrawable( R.drawable.order_cancel);
             drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
             mState.setCompoundDrawables(drawable, null, null, null);
             mTime.setVisibility(View.INVISIBLE);
  		}

	}

	private void getServerData(String orderID) {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		System.out.println("token:"+token);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestMyOrdersDetails;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("orderID", orderID);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				Gson gson=new Gson();
				HomePage data = gson.fromJson(result, HomePage.class);
				loading.setVisibility(View.GONE);
				cars = data.cars;
				if(cars!=null)
				{
					 MyOrdersDetailAdapter adapter=new MyOrdersDetailAdapter(getApplicationContext(),cars,data.orderState);
				     mLvOrder.setAdapter(adapter);
				     
				     if(data.orderState.equals("待支付"))
				     {
				    	 getCountTimer(data.remainTime);
//				    	 TotalMoney=data.orderSumMoney;
				    	 String orderSumMoney = data.orderSumMoney.replace(",", "");
				    	 TotalMoney= Float.parseFloat(orderSumMoney);
				     }
				   
				}
				
				
			}
		});
		
	}
	
	public void setLeftTime(int day, int hour ,int miniute ,int second)
	{
		this.day=day;
		this.hour=hour;
		this.miniute=miniute;
		this.second=second;
		TimeBack();
	}
	
	public void runtime() {
		second--;
		if (second < 0) {
			second = 0;
			if (day > 0 || hour > 0 || miniute > 0) {
				miniute--;
				second = 59;
				if (miniute < 0) {
					miniute = 0;
					if (day > 0 || hour > 0) {
						hour--;
						miniute = 59;
						if (hour < 0) {
							hour = 0;
							if (day > 0) {
								day--;
								hour = 23;
								if (day < 0) {
									day = 0;

								}
							}
						}
					}
				}
			}
		}
		String text=miniute+"分:" + second+ "秒";
		mTime.setText("订单将为您保留"+text+",超时将自动取消"); 
		//当时间为0时让状态改变
		if(second==0 && miniute==0)
		{
			mState.setText("已取消");	
  			Drawable drawable = getResources().getDrawable( R.drawable.order_cancel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
            mState.setCompoundDrawables(drawable, null, null, null);
            mTime.setVisibility(View.INVISIBLE);
            mCancelOrder.setVisibility(View.GONE);
            mPayNow.setVisibility(View.GONE);
		}
		
	}
	private void TimeBack() {
		Timer timer = new Timer();
		timer.schedule(new Task(), 1000, 1000);
	}
	class Task extends TimerTask {
		@Override
		public void run() {
			handler.sendEmptyMessage(1);
		}

	}
	
	//获取时间
	private void getCountTimer(String remainTime) {
		
		System.out.println("time:"+remainTime);
		int index = remainTime.indexOf(":");
		String miniteString = remainTime.substring(0, index);
		String secondString = remainTime.substring(index+1, remainTime.length());
		int minite = Integer.parseInt(miniteString);
		int second = Integer.parseInt(secondString);
		
		setLeftTime(0, 0, minite, second);
		

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mCancelOrder.setOnClickListener(this);
		mPayNow.setOnClickListener(this);
		
		mLvOrder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Cars car = cars.get(position);
				if(!car.carPrice.equals("已下架"))
				{
					Intent intent=new Intent(getApplicationContext(),VehicleDetailActivity.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("cars", car);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				
			}
		});

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			if("pay".equals(pay))
			{
				setResult(1);
				finish();
			}else{
				finish();
			}
			
			break;
		case R.id.tv_my_orders_cancel_order:
			UIUtils.showToastSafe("订单取消中...");
			//取消订单
			cancelOrder(orderNumber);
			break;
		case R.id.tv_my_orders_pay_now:
			UIUtils.showToastSafe("去支付");
			if(TextUtils.isEmpty(pay))
			{
				Intent intent=new Intent(MyOrdersDetailActivity.this,PayMoneyActivity.class);
				intent.putExtra("orderList", "orderList");
				intent.putExtra("money", TotalMoney);
				startActivity(intent);
			}else{
				finish();
				
			}
			break;
		default:
			break;
		}

	}
	//取消订单
	private void cancelOrder(String orderNumber) {

		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestCancelOrders;
		RequestParams params=new RequestParams();
		params.addBodyParameter("orderNo", orderNumber);
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
				OrderPayState data = gson.fromJson(result, OrderPayState.class);
				if(null!=data)
				{
					if(data.success)
					{
						//当pay为空时表示是从列表也没进来，直接finish掉当前页
						if(TextUtils.isEmpty(pay))
						{
							setResult(2);
							finish();
						}else{
							//如果不是从列表也进来则启动列表页
							Intent intent=new Intent(MyOrdersDetailActivity.this,MyOrdersActivity.class);
							intent.putExtra("pay", pay);
							startActivityForResult(intent, 1);
						}
					}
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			setResult(1);
			finish();
		}
		return super.onKeyDown(keyCode, event);
		
	}
	
	
}
