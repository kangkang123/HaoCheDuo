package cn.longchou.wholesale.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kang.taobaohead.headview.OnAdapterClickListener;
import com.kang.taobaohead.headview.ScrollTopView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mingle.widget.LoadingView;

import java.util.ArrayList;
import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.FirstLocationActivity;
import cn.longchou.wholesale.activity.HotSearchActivity;
import cn.longchou.wholesale.activity.LoginActivity;
import cn.longchou.wholesale.activity.MainActivity;
import cn.longchou.wholesale.activity.MyNewsDetailActivity;
import cn.longchou.wholesale.activity.PreferentialActivity;
import cn.longchou.wholesale.activity.VehicleDetailActivity;
import cn.longchou.wholesale.adapter.HomePagerAdapter;
import cn.longchou.wholesale.adapter.MarketCarAdapter;
import cn.longchou.wholesale.adapter.MarketCarAdapter.CallMarket;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.domain.CartAddDelete;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.domain.HomePage;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.domain.HomePage.Notification;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

public class CarMarketFragment extends BaseFragment{

	private ViewPager mViewPager;
	private LinearLayout mLL_point;
	private ImageView mRedPoint;
	private int width;
	public static final int SIZE = 3;
	
	private RelativeLayout mRLViewPager;
	private ListView mLvMarket;
	
	private int items[] = { R.drawable.header_image1, R.drawable.header_image2,
			R.drawable.header_image3 };
	
	private int requestCode=1;
	//热门搜索
	private static final int requestSearchCode=3;
	private static final int requestDetailCode=2;
	
	//判断是否是第一次加载首页
	private boolean isFirst=true;
	
	// 使用handler循环发送消息，获取当前viewpager页，然后设置当前页为viewpager的下一页实现轮播效果
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			int currentItem = mViewPager.getCurrentItem();
			if (currentItem < data.bannerImageCount - 1) {
				currentItem++;
			} else {
				currentItem = 0;
			}
			mViewPager.setCurrentItem(currentItem);
			handler.sendEmptyMessageDelayed(0, 3000);
			
//			mViewPager.getCurrentItem();
			
//			int currentItem = mViewPager.getCurrentItem();
//			
//			mViewPager.setCurrentItem(++currentItem);// 设置当前页面为下一页
//
//			handler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发送消息,自动更新轮播条位置
		};
	};
	
	
	private TextView mTvMore;
	private ImageView mLocation;
	private MarketCarAdapter adapter;
	private ArrayList<String> listChoose;
	private TextView mTvLocation;
	private EditText mEtSearch;
	private List<Cars> carsList;
	private List<CityProvinces> citys;
	private HomePage data;
	private ScrollTopView mTvAd;
	private TextView mAdDetail;
	
	private LoadingView loading;

	
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_carmarket, null);
		
		View header = View.inflate(getActivity(), R.layout.item_list_header, null);
		
		mViewPager = (ViewPager) header.findViewById(R.id.vp_home);
		mLL_point = (LinearLayout) header.findViewById(R.id.ll_point);
		mRedPoint = (ImageView) header.findViewById(R.id.red_point);
		mRLViewPager = (RelativeLayout) header.findViewById(R.id.rl_viewpager);
		

		//广告
		mTvAd = (ScrollTopView) header.findViewById(R.id.tv_market_ad);
		//广告详情
		mAdDetail = (TextView) header.findViewById(R.id.tv_market_detail);
		
		View footer = View.inflate(getActivity(), R.layout.item_list_footer, null);
		
		mLvMarket = (ListView) view.findViewById(R.id.lv_market);
		
		mTvMore = (TextView) footer.findViewById(R.id.tv_market_more);
		mLocation = (ImageView) view.findViewById(R.id.iv_location);
		
		mTvLocation = (TextView) view.findViewById(R.id.tv_title_location);
		
		mEtSearch = (EditText) view.findViewById(R.id.et_hot_serach);
		
		loading = (LoadingView) view.findViewById(R.id.loadView);
		
		//为listview添加头布局
		mLvMarket.addHeaderView(header);
		mLvMarket.addFooterView(footer,null,false);
		
		return view;
	}

	@Override
	public void initData() {
		
		String result = PreferUtils.getString(getActivity(), "homeData", null);
		if(!TextUtils.isEmpty(result))
		{
			paraseData(result);
		}
		
		getServerData();
		
		
		getCityLocationData();
		
		int TotalCars = PreferUtils.getInt(getActivity(), "totalCars", 0);
		if(TotalCars!=0)
		{
			String token = PreferUtils.getString(getActivity(), "token", null);
			if(!TextUtils.isEmpty(token))
			{
				MainActivity.getBadgeViewText().show();
				MainActivity.getBadgeViewText().setText(TotalCars+"");
				PreferUtils.putInt(getActivity(), "totalCars", TotalCars);
			}
		}
		
	}
	//获取城市的数据
	public void getCityLocationData()
	{
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestCityLocation;
		RequestParams params=new RequestParams();
		String token = PreferUtils.getString(getActivity(), "token", null);
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resuInfo) {
				//保存获取的城市
//				Constant.cityLocation = resuInfo.result;
				if(!TextUtils.isEmpty(resuInfo.result))
				{
					PreferUtils.putString(getActivity(), "cityLocation", resuInfo.result);
				}
				paraseCityData();
			}
		});
	}
	
	private void paraseCityData() {
		String result = PreferUtils.getString(getActivity(), "cityLocation", null);
		if(!TextUtils.isEmpty(result))
		{
			Gson gson=new Gson();
			CityLocation data = gson.fromJson(result, CityLocation.class);
			CityLocation.setCityLocation(data);
			citys = CityLocation.getFirstLocation(data);
		}
	}
	
	//获取车市首页的数据
	private void getServerData() {
		HttpUtils http=new HttpUtils();
		String url=Constant.HomePage;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c","as");
		String token = PreferUtils.getString(getActivity(), "token", null);
		params.addBodyParameter("Token", token);
		
		//设置城市的数据
		List<String> list=new ArrayList<String>();
		StringBuffer sb=new StringBuffer();
	    List<CityProvinces> allCitys = CityLocation.getAllCitys();
	    
	    for(int i=0;i<allCitys.size();i++)
	    {
	    	if(allCitys.get(i).isSelect)
	    	{
	    			list.add(allCitys.get(i).provinces);
	    			sb.append(allCitys.get(i).provinces+",");
	    	}
	    }
	    //让city默认选择全国，只有当sb不为空时才给city赋值
	    String city="全国";
	    if(!TextUtils.isEmpty(sb.toString()))
	    {
	    	city= sb.deleteCharAt(sb.length()-1).toString();
	    }
	    
	    if(!"全国".equals(city))
	    {
	    	
	    	params.addBodyParameter("city", city);
	    }
		
	    
		http.send(HttpMethod.POST, url,params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				paraseData(result);
				PreferUtils.putString(getActivity(), "homeData", result);
			}
		});
		
	}


	//解析数据
	protected void paraseData(String result) {
		Gson gson=new Gson();
		data = gson.fromJson(result, HomePage.class);
		loading.setVisibility(View.GONE);
		System.out.println(data);
//		if(isFirst)
//		{
//			
//			setImage();
//		}
//		isFirst=false;
		
		//轮播图的操作
		carouselData();
		carouselListener();
		//轮播图的百分比
		setViewPagerPercent();
		
		carsList = data.cars;
		
		List<Notification> notifications = data.notifications;
		List<com.kang.taobaohead.headview.Notification> list=new ArrayList<com.kang.taobaohead.headview.Notification>();
		for(int i=0;i<notifications.size();i++)
		{
			com.kang.taobaohead.headview.Notification cloneNotification = HomePage.cloneNotification(notifications.get(i));
			list.add(cloneNotification);
		}
		
		
		mTvAd.setData(list);
		
		mTvAd.setClickListener(new OnAdapterClickListener<com.kang.taobaohead.headview.Notification>() {
			
			@Override
			public void onAdapterClick(View v, com.kang.taobaohead.headview.Notification t) {
				if(!TextUtils.isEmpty(t.notificationContent))
				{
					Intent intentAdDetail=new Intent(getActivity(),MyNewsDetailActivity.class);
					intentAdDetail.putExtra("notificationContent", t.notificationContent);
					intentAdDetail.putExtra("notificationDate", t.notificationDate);
					intentAdDetail.putExtra("notificationTitle", t.notificationTitle);
					startActivity(intentAdDetail);
				}
			}
		});
		
		if(carsList!=null)
		{
			adapter = new MarketCarAdapter(getActivity(), carsList, Constant.Market, new CallMarket() {
				
				@Override
				public void click(View v) {
					boolean isLogin = PreferUtils.getBoolean(getActivity(), "isLogin", false);
					if(isLogin)
					{
						
						adapter.selectSingle((Integer) v.getTag());
						addCartData((Integer) v.getTag());
					}else{
						Intent intent=new Intent(getActivity(),LoginActivity.class);
						startActivityForResult(intent, requestDetailCode);

					}
				}
			});
		    mLvMarket.setAdapter(adapter);
		}
	}

	//添加到购物车的接口
	protected void addCartData(int carId) {
		String token = PreferUtils.getString(getActivity(), "token", null);
		Cars item = carsList.get(carId);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestAddToCart;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("carID", item.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				System.out.println("result:"+result);
				Gson gson=new Gson();
				CartAddDelete data = gson.fromJson(result, CartAddDelete.class);
				if(null!=data)
				{
					if(TextUtils.isEmpty(data.errorMsg))
					{
						UIUtils.showToastSafe("添加购物车成功");
						int TotalCars = PreferUtils.getInt(getActivity(), "totalCars", 0);
						MainActivity.getBadgeViewText().show();
						int cars=TotalCars+1;
						MainActivity.getBadgeViewText().setText(cars+"");
						PreferUtils.putInt(getActivity(), "totalCars", cars);
					}else{
						UIUtils.showToastSafe(data.errorMsg);
					}
				}
			}
		});
		
	}
	

	//初始化城市
	private void initCity()
	{
		List<String> list=new ArrayList<String>();
		
		//遍历list集合
		List<CityProvinces> allCitys = CityLocation.getAllCitys();
		for(int i=0;i<allCitys.size();i++)
		{
			CityProvinces cityProvinces = allCitys.get(i);
			if(cityProvinces.isSelect)
			{
				//获取每一个对象，让后如果是选中状态则取出城市放入一个新的集合中
				list.add(cityProvinces.provinces);
				
			}
		}
		//遍历放入城市的集合
		if(list!=null && list.size()>0)
		{
			String city = list.get(0);
			if(list.size()>1)
			{
				//如果城市的个数大于1，则在城市后面加上+标识后面还有城市
				mTvLocation.setText(city.subSequence(0, 2)+"+");
			}else if(city.length()>2)
			{
				//如果城市个数不大于一，并且城市的数字大于2，则省略号标识，表示城市名字较长
				mTvLocation.setText(city.subSequence(0, 2)+"...");
			}else{
				//否则直接显示名称
				mTvLocation.setText(city);
			}
			
		}else{
			mTvLocation.setText("全国");
			if(CityLocation.getAllCitys()!=null && CityLocation.getAllCitys().size()>0)
			{
				CityProvinces cityProvinces = CityLocation.getAllCitys().get(0);
				CityLocation.setCitySelectTrue(cityProvinces);
			}
		}
	}
	

	@Override
	public void initListener() {
		mTvMore.setOnClickListener(this);
		mLocation.setOnClickListener(this);
		mAdDetail.setOnClickListener(this);
		
		
		//搜索框实现点击事件
		mEtSearch.setOnClickListener(this);
		
		mLvMarket.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cars cars = carsList.get(position-1);
				Intent intent=new Intent(getActivity(),VehicleDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("cars", cars);
				intent.putExtras(bundle);
				startActivityForResult(intent, requestDetailCode);
				
			}
		});
		
		// 当手点击viewpager时停止viewpager的轮播效果
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					handler.removeCallbacksAndMessages(null);// 删除所有消息,停止广告条自动切换
					break;
				case MotionEvent.ACTION_MOVE:
					handler.removeCallbacksAndMessages(null);
					break;
				case MotionEvent.ACTION_UP:
					handler.sendEmptyMessageDelayed(0, 3000);// 继续自动切换广告条
					break;

				default:
					break;
				}

				return false;// 这里需要返回false, 不能消耗掉事件,
								// 这样的话ViewPager才能够响应触摸滑动的事件,页面跟随手指移动
			}
		});

	}
	
	

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.tv_market_more:
			MainActivity mActivity=(MainActivity) getActivity();
			mActivity.setButtonSelection(4);
			break;
		case R.id.iv_location:
			Intent intent=new Intent(getActivity(),FirstLocationActivity.class);
			startActivityForResult(intent, requestCode);
			break;
		case R.id.et_hot_serach:
			
			Intent intentSearch=new Intent(getActivity(),HotSearchActivity.class);
			startActivityForResult(intentSearch, requestSearchCode);
			break;
		case R.id.tv_market_detail:
			Intent intentActivity=new Intent(getActivity(),PreferentialActivity.class);
			startActivity(intentActivity);
			break;

		default:
			break;
		}

	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		
		super.onHiddenChanged(hidden);
		if(!hidden)
		{
			//初始化城市，如果车辆列表中选中的话，需要同步
			initCity();
			getServerData();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case requestDetailCode:
			boolean isLogin = PreferUtils.getBoolean(getActivity(), "isLogin", false);
			if(isLogin)
			{
				//当在车辆详情中登录后，重新加载数据，加载登录后的数据
				getServerData();
			}
			break;
		case requestSearchCode:
			if(resultCode==3)
			{
				MainActivity mActivity = (MainActivity) getActivity();
				mActivity.setButtonSelection(4);
			}

		default:
			break;
		}
		switch (resultCode) {
		case 1:
			List<CityProvinces> firstChoose = CityLocation.getFirstChoose();
			for(int i=0;i<firstChoose.size();i++)
			{
				CityProvinces cityProvinces = firstChoose.get(i);
				CityLocation.setCitySelectFalse(cityProvinces);
			}
			
			List<CityProvinces> secondChoose = CityLocation.getFirstRemove();
			for(int i=0;i<secondChoose.size();i++)
			{
				CityProvinces cityProvinces = secondChoose.get(i);
				CityLocation.setCitySelectTrue(cityProvinces);
			}
			
			CityLocation.getFirstChoose().clear();
			CityLocation.getFirstRemove().clear();
			
			break;
		case 2:
			CityLocation.getFirstChoose().clear();
			CityLocation.getFirstRemove().clear();
			break;

		default:
			break;
		}
		initCity();
		getServerData();
	}
	
	
	//轮播图数据的初始化操作
	private void carouselData() {
		List<ImageView> images = new ArrayList<ImageView>();
		List<String> urls=new ArrayList<String>();
		for (int i = 0; i < data.bannerImages.size(); i++) {
			ImageView image = new ImageView(getActivity());
			
			if(null!=data)
			{
				String url=data.bannerImages.get(i).bannerImgUrl;
				String reUrl=data.bannerImages.get(i).bannerReurl;
				urls.add(reUrl+"");
				Glide.with(getActivity()).load(url).placeholder(R.drawable.car_detail_default).into(image);
			}else{
				image.setBackgroundResource(R.drawable.car_detail_default);
			}
			images.add(image);
			
		}
		
		
		mLL_point.removeAllViews();
		handler.removeCallbacksAndMessages(null);// 删除所有消息,停止广告条自动切换
		
		// 根据图片显示的数量动态设置小圆点
		for (int i = 0; i < images.size(); i++) {
			ImageView point = new ImageView(getActivity());
			point.setBackgroundResource(R.drawable.shape_gray_point);
			// 设置小圆点的参数，左边距为10
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i > 0) {
				params.leftMargin = 18;
				params.bottomMargin=10;
			}
			point.setLayoutParams(params);
//			if(isFirst)
//			{
				mLL_point.addView(point);
				
//			}

		}
//		isFirst=false;
		
		HomePagerAdapter adapter = new HomePagerAdapter(getActivity(), images,urls,"return");
		mViewPager.setAdapter(adapter);
		
		// 发送延迟消息，3秒发送一次
		handler.sendEmptyMessageDelayed(0, 3000);

	}
	
	
	//设置viewpager的轮播图的百分比
	private void setViewPagerPercent() {
		//窗口高度    
	    int screenHeight = getScreenWidth()*2/3;
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mRLViewPager
				.getLayoutParams();
		params.width=getScreenWidth();
		params.height=screenHeight;
		mRLViewPager.setLayoutParams(params);
	}
	
	//获取屏幕的高度
	private int getScreenWidth() {
		DisplayMetrics  dm = new DisplayMetrics();    
	    //取得窗口属性    
	    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);   
	         
	    //窗口的宽度    
	    int screenWidth = dm.widthPixels;  
	    return screenWidth;
	}
	
	//轮播图事件的处理
	private void carouselListener() {

		// 获取到小圆点与小圆点之间的距离
		mRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						mRedPoint.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						width = mLL_point.getChildAt(1).getLeft()
								- mLL_point.getChildAt(0).getLeft();
					}
				});

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				

			}

			// 设置当viewpager滑动时设置红色小圆点的左边距的位置
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				System.out.println(data.bannerImageCount);
				int pos = position % data.bannerImageCount;
				int offset = (int) (width * positionOffset + width * pos);
				LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mRedPoint
						.getLayoutParams();
				params.leftMargin = offset;
				params.bottomMargin=10;
				mRedPoint.setLayoutParams(params);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		
	}
}
