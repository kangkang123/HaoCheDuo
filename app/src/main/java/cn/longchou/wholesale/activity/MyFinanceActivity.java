package cn.longchou.wholesale.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.HomePagerAdapter;
import cn.longchou.wholesale.adapter.MyNewsFragmentAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.MyFinance;
import cn.longchou.wholesale.fragment.ConsumerCreditFragment;
import cn.longchou.wholesale.fragment.StockFinanceFragment;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 我的金融的页面
*
* @author kangkang
*
* @date 2016年1月25日 下午2:44:28 
*
 */
public class MyFinanceActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private LinearLayout mLLLoan;
	private TextView mStock;
	private TextView mLoan;
	private TextView mConsumer;
	private ViewPager mViewPager;
	private LinearLayout mLLPoint;
	private ImageView mRedPoint;
	
	private List<Fragment> fragments;
	private MyNewsFragmentAdapter adapter;
	
	private int size=2;
	private int width=2;
	private MyFinance data;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_my_finance);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		//库存融资和收车贷的线性布局
		mLLLoan = (LinearLayout) findViewById(R.id.ll_my_finance);
		//库存融资
		mStock = (TextView) findViewById(R.id.tv_my_finance_stock_financing);
		//收车贷
		mLoan = (TextView) findViewById(R.id.tv_my_finance_car_loan);
		//消费贷
		mConsumer = (TextView) findViewById(R.id.tv_my_finance_consumer_credit);
		
		mViewPager = (ViewPager) findViewById(R.id.vp_my_finance);
		
		//底部的小圆点的线性布局
		mLLPoint = (LinearLayout) findViewById(R.id.ll_finance_point);
		//底部的选中的小圆点
		mRedPoint = (ImageView) findViewById(R.id.iv_finance_red_point);

	}

	@Override
	public void initData() {
		mTitle.setText("我的金融");
		
		//获取服务器数据
//		getServerData();
		
		fragments = new ArrayList<Fragment>();
		//创建Fragment对象，存入集合
		StockFinanceFragment fragment1 = new StockFinanceFragment();
		ConsumerCreditFragment fragment2=new ConsumerCreditFragment();
		fragments.add(fragment1);
//		fragments.add(fragment2);
		
		adapter = new MyNewsFragmentAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(adapter);
		
//		carouselData();
		
		getPlanData();
		
	}
	
	private void getServerData() {
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestmyFinance;
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
				data = gson.fromJson(result, MyFinance.class);
				
			}
		});
	}


	//轮播图数据的初始化操作
	private void carouselData() {
		
		// 根据图片显示的数量动态设置小圆点
		for (int i = 0; i < size; i++) {
			ImageView point = new ImageView(getApplicationContext());
			point.setBackgroundResource(R.drawable.shape_orange_circle);
			// 设置小圆点的参数，左边距为10
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i > 0) {
				params.leftMargin = 18;
				params.bottomMargin=10;
			}
			point.setLayoutParams(params);
			mLLPoint.addView(point);

		}
		
		// 获取到小圆点与小圆点之间的距离
		mRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						mRedPoint.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						width = mLLPoint.getChildAt(1).getLeft()
								- mLLPoint.getChildAt(0).getLeft();
					}
				});
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		
		mStock.setOnClickListener(this);
		mLoan.setOnClickListener(this);
		mConsumer.setOnClickListener(this);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(position==0)
				{
					mLLLoan.setVisibility(View.VISIBLE);
					mConsumer.setVisibility(View.GONE);
				}else if(position==1){
					
					mLLLoan.setVisibility(View.GONE);
					mConsumer.setVisibility(View.VISIBLE);
				}
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				int pos = position % size;
				int offset = (int) (width * positionOffset + width * pos);
				LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mRedPoint
						.getLayoutParams();
				params.leftMargin = offset;
				params.bottomMargin=10;
				mRedPoint.setLayoutParams(params);
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
				
			}
		});

	}
	
	//获取我的信息
	private void getPlanData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
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
				Gson gson=new Gson();
				LoginValidate data = gson.fromJson(result,LoginValidate.class);
				if(null!=data)
				{
						//把我的信息保存起来
					Constant.myInfo=result;
				}
			}
				
		});
		
	}

	@Override
	public void processClick(View v) {
		
		Intent intent=new Intent(this,FinancePlanActivcity.class);
		
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		case R.id.tv_my_finance_stock_financing:
			intent.putExtra("plan", "库存融资");
			UIUtils.startActivity(intent);
			break;
		case R.id.tv_my_finance_car_loan:
			intent.putExtra("plan", "收车贷");
			UIUtils.startActivity(intent);
			break;
		case R.id.tv_my_finance_consumer_credit:
			intent.putExtra("plan", "消费贷");
			UIUtils.startActivity(intent);
			break;
		default:
			break;
		}


	}

}
