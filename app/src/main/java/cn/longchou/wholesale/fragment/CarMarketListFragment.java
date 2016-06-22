package cn.longchou.wholesale.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mingle.widget.LoadingView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.FirstLocationActivity;
import cn.longchou.wholesale.activity.HotSearchActivity;
import cn.longchou.wholesale.activity.LoginActivity;
import cn.longchou.wholesale.activity.MainActivity;
import cn.longchou.wholesale.activity.MyBuyActivity;
import cn.longchou.wholesale.activity.VehicleDetailActivity;
import cn.longchou.wholesale.activity.MainActivity.OnScreenLisener;
import cn.longchou.wholesale.adapter.MarketCarAdapter;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.domain.AvailBrand;
import cn.longchou.wholesale.domain.CartAddDelete;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.domain.HomePage;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.fragment.OneLevelFragment.OnIndexListener;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.XListView;
import cn.longchou.wholesale.view.XListView.IXListViewListener;
import cn.longchou.wholesale.adapter.MarketCarAdapter.CallMarket;

public class CarMarketListFragment extends BaseFragment implements IXListViewListener{

	private LinearLayout mllTitle;
	private ImageView mBack;
	private TextView mTvLocation;
	private ImageView mIvLocation;
	
	private MarketCarAdapter carAdapter;
	private XListView mCarList;

	private Handler mHandler = new Handler();
	private TextView mTvDefault;
	private TextView mTvPrice;
	private TextView mTvAge;
	private TextView mTvMileage;
	private TextView mTvChoose;;
	
	//城市选择的请求码
	private int requestCode=1;
	//热门搜索的请求码
	private  static final int requestSearchCode=3;
	
	//设置筛选条件
	private boolean priceIsLow=true;
	private boolean ageIsLow=true;
	private boolean mileageIsLow=true;
	private View view1;
	
	//下拉刷新默认为false
	private boolean isRefresh=false;
	
	//是否加载更多
	private boolean isLoadMore=false;
	
	//是否排序
	private boolean isSort=false;
	
	
	
	//是否是搜索
	private boolean isSearch=false;
	
	private EditText mEtSearch;
	
	//存放获取选中的城市
	private ArrayList<String> listChoose;
	
	//获取DrawerLayout对象
	private static DrawerLayout mDrawerLayout;
	
	//请求的页数
	private int pageIndex=0;
	//每次请求的数量
	private int pageSize=5;
	
	//保存所有的车辆
	private List<Cars> TotalCar=new ArrayList<HomePage.Cars>();
	private String token;
	
	private LoadingView loading;
	
	private static final int requestDetailCode=2;
	
	public CarMarketListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=View.inflate(getActivity(), R.layout.fragment_market_list, null);
		mllTitle = (LinearLayout) view.findViewById(R.id.ll_title);
		
	    //标题的相关组件
		mBack = (ImageView) view.findViewById(R.id.title_back);
		mTvLocation = (TextView) view.findViewById(R.id.tv_title_location);
		mIvLocation = (ImageView) view.findViewById(R.id.iv_location);
		mEtSearch = (EditText) view.findViewById(R.id.et_hot_serach);
		
		//车市列表
		mCarList = (XListView) view.findViewById(R.id.lv_market_list);
		
		loading = (LoadingView) view.findViewById(R.id.loadView);
		
		//筛选相关的组件
		mTvDefault = (TextView) view.findViewById(R.id.tv_market_list_default);
		mTvPrice = (TextView) view.findViewById(R.id.tv_market_list_price);
		mTvAge = (TextView) view.findViewById(R.id.tv_market_list_age);
		mTvMileage = (TextView) view.findViewById(R.id.tv_market_list_mileage);
		mTvChoose = (TextView) view.findViewById(R.id.tv_market_list_choose);
		
		view1 = view.findViewById(R.id.market_list_no_car);
		
		mTryBuy = (TextView) view.findViewById(R.id.tv_try_shop);
		
		
		view.setFocusable(true);//这个和下面的这个命令必须要设置了，才能监听back事件。
		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(backlistener);
		return view;
	}

	@Override
	public void initData() {
		
		mTryBuy.setText("试试求购吧!");
		
		mEtSearch.setText(Constant.searchSelect);
		
		token = PreferUtils.getString(getActivity(), "token", null);
		
		//标题内容的相关设置
		initTitle(false);
		
		//获取车市列表的数据
		getServerData();
		
		//获取筛选热门活动的内容
		getHotActivityData();
		
		//获取筛选界面品牌的内容
		getHotBrandData();
		
		mCarList.setPullLoadEnable(true);
		mCarList.setXListViewListener(this);
		
		Constant.isCarMarketList=true;
	}

	
	private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (((keyCode == KeyEvent.KEYCODE_BACK) ||(keyCode == KeyEvent.KEYCODE_HOME))&& event.getRepeatCount() == 0) {
                
                	UIUtils.showToastSafe("我返回了。");
                
            }
            return false;
        }
    };
	private TextView mTryBuy;
	
	//添加到购物车的接口
	protected void addCartData(int carId) {
		String token = PreferUtils.getString(getActivity(), "token", null);
		Cars item = TotalCar.get(carId);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestAddToCart;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("carID", item.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				Gson gson=new Gson();
				CartAddDelete data = gson.fromJson(result, CartAddDelete.class);
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
		});
		
	}
	
	

	private void getServerData() {
		pageIndex++;
		HttpUtils http=new HttpUtils();
		String url=Constant.CarList;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c","as");
		params.addBodyParameter("Token",token);
		params.addBodyParameter("pageIndex", pageIndex+"");
		params.addBodyParameter("pageSize", pageSize+"");
		
//		UIUtils.showToastSafe("pageIndex:"+pageIndex);
		
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
//	    UIUtils.showToastSafe(city);
	    
	    if(!"全国".equals(city))
	    {
	    	
	    	params.addBodyParameter("city", city);
	    }
	    
	    //热门搜索的内容
	    String select = Constant.searchSelect;
	    if(!TextUtils.isEmpty(select))
	    {
//	    	UIUtils.showToastSafe("searchSelect:"+select);
	    	params.addBodyParameter("searchCondition", select);
	    	//重新让值变为null
//	    	Constant.searchSelect=null;
	    }
		
		//品牌
		if(!"全部".equals(AvailBrand.mScreenHotBrand))
		{
			params.addBodyParameter("carBrand", AvailBrand.mScreenHotBrand);
		}
		//车系
//		if(!"全部".equals(AvailBrand.mScreenHotCarLine))
//		{
//			params.addBodyParameter("carSeries", AvailBrand.mScreenHotCarLine);
//		}
		//车型
		if(!"全部".equals(Constant.screenCarModel))
		{
			params.addBodyParameter("carModel", Constant.screenCarModel);
		}
		
		//变速箱
		if(!"全部".equals(Constant.screenGearBox))
		{
			params.addBodyParameter("carGearbox", Constant.screenGearBox);
		}
		
		//车龄
		if(!"全部".equals(Constant.screenCarYears))
		{
			params.addBodyParameter("carYear", Constant.getHandleString(Constant.screenCarYears, false));
			UIUtils.showToastSafe(Constant.getHandleString(Constant.screenCarYears, false));

		}
		
		//车价
		if(!"全部".equals(Constant.screenPrice))
		{
			params.addBodyParameter("carPrice", Constant.getHandleString(Constant.screenPrice, false));
			UIUtils.showToastSafe(Constant.getHandleString(Constant.screenPrice, false));
		}
		
		//里程
		if(!"全部".equals(Constant.screenMileage))
		{
			params.addBodyParameter("carMileage", Constant.getHandleString(Constant.screenMileage, true));
			UIUtils.showToastSafe(Constant.getHandleString(Constant.screenMileage, true));
		}
		
		//活动搜索条件
		if(!TextUtils.isEmpty(Constant.screenAvtivity))
		{
			params.addBodyParameter("filterAction", Constant.screenAvtivity);
		}
		
//		//活动搜索条件
//		if(!"全部".equals(Constant.screenAvtivity))
//		{
//			params.addBodyParameter("searchCondition", Constant.screenAvtivity);
//		}
		//活动搜索条件
		if(ageIsLow && mileageIsLow)
		{
			if(priceIsLow)
			{
				params.addBodyParameter("sort", "priceDescending");
			}else{
				params.addBodyParameter("sort", "priceAscending");
				
			}
		}
		else if(ageIsLow && mileageIsLow)
		{
			if(ageIsLow)
			{
				params.addBodyParameter("sort", "ageDescending");
			}else{
				params.addBodyParameter("sort", "ageAscending");
			}
		}
		else if(ageIsLow && priceIsLow)
		{
			if(mileageIsLow)
			{
				params.addBodyParameter("sort", "mileageDescending");
			}else{
				params.addBodyParameter("sort", "mileageAscending");
			}
		}else if(ageIsLow && priceIsLow && mileageIsLow){
			params.addBodyParameter("sort", "orderDefault");
		}
		
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				try{
					parseData(result);
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		});
		
	}
	

	protected void parseData(String result) {
		Gson gson=new Gson();
		HomePage data= gson.fromJson(result, HomePage.class);
		loading.setVisibility(View.GONE);
		List<Cars> cars = data.cars;
		if(null!=cars && cars.size()>0)
		{
			//是否下拉筛选
			if(isRefresh)
			{
				//因为下来刷新的时候应该是最新的数据在上面，所以应该在开头的位置，创建新的集合临时保存以前的数据
				List<Cars> carRefresh=new ArrayList<HomePage.Cars>();
				carRefresh.addAll(TotalCar);
				//把总集合清空
				TotalCar.clear();
				//把最新的数据放到集合中
				TotalCar.addAll(cars);
				//把以前的数据放到集合中
				TotalCar.addAll(carRefresh);
				//让下拉刷新的状态变为false
				isRefresh=false;
			}else{
				//判断是否是从搜索页面进来
				if(isSearch)
				{
					//如果是搜索页面进来清空集合并且添加最新的内容
					TotalCar.clear();
					TotalCar.addAll(cars);
					isSearch=false;
				}else{
					//保存从服务器获取的车辆内容，这是加载更多的时候调用
					TotalCar.addAll(cars);

				}
				
				if(isSort)
				{
					TotalCar.clear();
					TotalCar.addAll(cars);
					isSort=false;
				}
				
			}
			
			//每次请求之后重新填充列表
			carAdapter = new MarketCarAdapter(getActivity(), TotalCar, Constant.Market, new CallMarket() {
				
				@Override
				public void click(View v) {
					boolean isLogin = PreferUtils.getBoolean(getActivity(), "isLogin", false);
					if(isLogin)
					{
						carAdapter.selectSingle((Integer) v.getTag());
						addCartData((Integer) v.getTag());
					}else{
						Intent intent=new Intent(getActivity(),LoginActivity.class);
						startActivity(intent);
					}
				}
			});
			mCarList.setAdapter(carAdapter);
			if(isLoadMore)
			{
				mCarList.setSelection(TotalCar.size()-cars.size());
			}

			view1.setVisibility(View.GONE);
			mCarList.setVisibility(View.VISIBLE);
		}else{
			//是否加载更多，如果是加载更多，则当没有数据的时候提示没有更多数据了
			if(isLoadMore || isRefresh)
			{
			   UIUtils.showToastSafe("没有更多数据了！");
			}else{
				//如果不是加载更多咋让显示为购物车为空
				mCarList.setVisibility(View.INVISIBLE);
				view1.setVisibility(View.VISIBLE);
			}
			//让加载更多变为false，状态变为不是加载更多 
			isLoadMore=false;
			
			//让下拉刷新的状态变为false
			isRefresh=false;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		
		super.onHiddenChanged(hidden);
		if(!hidden)
		{
			Constant.isCarMarketList=true;
			
			//只有当筛选页面没有选中取消的时候才为刷新
			if(Constant.isCancel)
			{
				initTitle(false);
				
			}else{
				initTitle(true);
			}
			//让取消状态重新变为false
			Constant.isCancel=false;
			
			mEtSearch.setText(Constant.searchSelect);
			
//			initCity(true);
			OneLevelFragment.setScreenConfirmListener(new OnIndexListener() {
				
				@Override
				public void getScreenConfirm(boolean isConfirm) {
					if(isConfirm)
					{
						pageIndex=0;
						TotalCar.clear();
						getServerData();
					}
					
				}
			});
			
			
//			mCarList.setVisibility(View.INVISIBLE);
//			loading.setVisibility(View.VISIBLE);
//			view1.setVisibility(View.GONE);
//			pageIndex=0;
//			TotalCar.clear();
//			getServerData();
			
		}
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mIvLocation.setOnClickListener(this);
		
		mTvDefault.setOnClickListener(this);
		mTvPrice.setOnClickListener(this);
		mTvAge.setOnClickListener(this);
		mTvMileage.setOnClickListener(this);
		mTvChoose.setOnClickListener(this);
		
		mTryBuy.setOnClickListener(this);
		
		//搜索框实现点击事件
		mEtSearch.setOnClickListener(this);
		
		mCarList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cars cars = TotalCar.get(position-1);
				Intent intent=new Intent(getActivity(),VehicleDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("cars", cars);
				intent.putExtras(bundle);
				startActivityForResult(intent, requestDetailCode);
				
			}
		});

	}
	//初始化城市
	private void initCity(boolean isTrue)
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
			if(CityLocation.getAllCitys().size()>0)
			{
				CityProvinces cityProvinces = CityLocation.getAllCitys().get(0);
				CityLocation.setCitySelectTrue(cityProvinces);
			}
		}
		if(isTrue)
		{
			mCarList.setVisibility(View.INVISIBLE);
			loading.setVisibility(View.VISIBLE);
			view1.setVisibility(View.GONE);
			pageIndex=0;
			TotalCar.clear();
			getServerData();
		}
			
	}
	
	//标题内容的相关的设置
	private void initTitle(boolean b) {
		mllTitle.setBackgroundColor(Color.rgb(237, 108, 1));
        mBack.setVisibility(View.VISIBLE);
        mTvLocation.setTextColor(Color.WHITE);
        mIvLocation.setImageResource(R.drawable.locationlist);
        
        initCity(b);
	}

	@Override
	public void processClick(View v) {
		
		Drawable defaultArrow = getResources().getDrawable(R.drawable.default_arrow);
		defaultArrow.setBounds(0, 0, defaultArrow.getMinimumWidth(), defaultArrow.getMinimumHeight()); 
		
		Drawable lowHigh = getResources().getDrawable(R.drawable.low_high);
		lowHigh.setBounds(0, 0, lowHigh.getMinimumWidth(), lowHigh.getMinimumHeight());
		
		Drawable highLow = getResources().getDrawable(R.drawable.high_low);
		highLow.setBounds(0, 0, highLow.getMinimumWidth(), highLow.getMinimumHeight());
		
		
		switch (v.getId()) {
		
		case R.id.title_back:
			Constant.searchSelect="";
			mEtSearch.setText(Constant.searchSelect);
			MainActivity mActivity=(MainActivity) getActivity();
			mActivity.setButtonSelection(0);
			break;
		case R.id.tv_try_shop:
			Intent intentBuy=new Intent(getActivity(),MyBuyActivity.class);
			startActivity(intentBuy);
			break;
			
		case R.id.et_hot_serach:
			Intent intentSearch=new Intent(getActivity(),HotSearchActivity.class);
			intentSearch.putExtra("source", "carList");
			startActivityForResult(intentSearch, requestSearchCode);
			break;

		case R.id.tv_market_list_default:
			//点击默认设置文字的颜色
			mTvDefault.setTextColor(Color.rgb(237, 108, 1));
			mTvPrice.setTextColor(Color.rgb(128, 128, 128));
			mTvAge.setTextColor(Color.rgb(128, 128, 128));
			mTvMileage.setTextColor(Color.rgb(128, 128, 128));
			
			//点击默认设置排序的图标
			mTvPrice.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
			mTvAge.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
			mTvMileage.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
			
			priceIsLow=true;
			ageIsLow=true;
			mileageIsLow=true;
			
			isSort=true;
			pageIndex=0;
//			TotalCar.clear();
			getServerData();
			break;
		case R.id.tv_market_list_price:
			
			if(priceIsLow)
			{
				mTvDefault.setTextColor(Color.rgb(128, 128, 128));
				mTvPrice.setTextColor(Color.rgb(237, 108, 1));
				mTvAge.setTextColor(Color.rgb(128, 128, 128));
				mTvMileage.setTextColor(Color.rgb(128, 128, 128));
				
				mTvPrice.setCompoundDrawables(null, null, lowHigh, null);//画在右边
				mTvAge.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				mTvMileage.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				
				priceIsLow=false;
			}else{
				
				mTvDefault.setTextColor(Color.rgb(128, 128, 128));
				mTvPrice.setTextColor(Color.rgb(237, 108, 1));
				mTvAge.setTextColor(Color.rgb(128, 128, 128));
				mTvMileage.setTextColor(Color.rgb(128, 128, 128));
				
				mTvPrice.setCompoundDrawables(null, null, highLow, null);//画在右边
				mTvAge.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				mTvMileage.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				
				priceIsLow=true;
			}
			
			ageIsLow=true;
			mileageIsLow=true;
			
			pageIndex=0;
			isSort=true;
//			TotalCar.clear();
			getServerData();
			
			break;
		case R.id.tv_market_list_age:
			if(ageIsLow)
			{
				mTvDefault.setTextColor(Color.rgb(128, 128, 128));
				mTvPrice.setTextColor(Color.rgb(128, 128, 128));
				mTvAge.setTextColor(Color.rgb(237, 108, 1));
				mTvMileage.setTextColor(Color.rgb(128, 128, 128));
				
				mTvPrice.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				mTvAge.setCompoundDrawables(null, null, lowHigh, null);//画在右边
				mTvMileage.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				
				ageIsLow=false;
			}else{
				
				mTvDefault.setTextColor(Color.rgb(128, 128, 128));
				mTvPrice.setTextColor(Color.rgb(128, 128, 128));
				mTvAge.setTextColor(Color.rgb(237, 108, 1));
				mTvMileage.setTextColor(Color.rgb(128, 128, 128));
				
				mTvPrice.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				mTvAge.setCompoundDrawables(null, null, highLow, null);//画在右边
				mTvMileage.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				
				ageIsLow=true;
			}
			priceIsLow=true;
			mileageIsLow=true;
			
			pageIndex=0;
//			TotalCar.clear();
			isSort=true;
			getServerData();
			
			break;
		case R.id.tv_market_list_mileage:
			
			if(mileageIsLow)
			{
				mTvDefault.setTextColor(Color.rgb(128, 128, 128));
				mTvPrice.setTextColor(Color.rgb(128, 128, 128));
				mTvAge.setTextColor(Color.rgb(128, 128, 128));
				mTvMileage.setTextColor(Color.rgb(237, 108, 1));
				
				mTvPrice.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				mTvAge.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				mTvMileage.setCompoundDrawables(null, null, lowHigh, null);//画在右边
				
				mileageIsLow=false;
			}else{
				
				mTvDefault.setTextColor(Color.rgb(128, 128, 128));
				mTvPrice.setTextColor(Color.rgb(128, 128, 128));
				mTvAge.setTextColor(Color.rgb(128, 128, 128));
				mTvMileage.setTextColor(Color.rgb(237, 108, 1));
				
				mTvPrice.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				mTvAge.setCompoundDrawables(null, null, defaultArrow, null);//画在右边
				mTvMileage.setCompoundDrawables(null, null, highLow, null);//画在右边
				
				mileageIsLow=true;
			}
			ageIsLow=true;
			priceIsLow=true;
			
			pageIndex=0;
//			TotalCar.clear();
			isSort=true;
			getServerData();
			
			break;
		case R.id.tv_market_list_choose:
//			view1.setVisibility(View.VISIBLE);
//			mCarList.setVisibility(View.GONE);
			//显示筛选的内容
			showScreen();
			break;
			
		case R.id.iv_location:
			Intent intent=new Intent(getActivity(),FirstLocationActivity.class);
			startActivityForResult(intent, requestCode);
			break;
		default:
			break;
		}

	}
	private void showScreen() {

		//获取activity传递过来的DrawerLayout对象
		DrawerLayout mDrawerLayout = MainActivity.getDrawerLayout();
		mDrawerLayout.openDrawer(Gravity.RIGHT);
		MainActivity mActivity=(MainActivity) getActivity();
		mActivity.setButtonSelection(5);
	}
	
	//获取热门活动的内容
	public void getHotActivityData()
	{
		HttpUtils http=new HttpUtils();
		String url=Constant.HotActivity;
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Constant.hotActivity=responseInfo.result;
			}
		});
	}
	//获取热门品牌的内容
	public void getHotBrandData(){
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
//				System.out.println("availBrand:"+responseInfo.result);
//				Constant.availableBrand=responseInfo.result;
				if(!TextUtils.isEmpty(responseInfo.result))
				{
					PreferUtils.putString(getActivity(), "availableBrand", responseInfo.result);
				}
			}
		});
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
				//让加载更多变为false，状态变为不是加载更多 
				isLoadMore=true;
				
				//让下拉刷新的状态变为false
				isRefresh=true;
				//当在车辆详情中登录后，重新加载数据，加载登录后的数据
				getServerData();
			}
			break;

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
			
			initCity(true);
			
			break;
		case 2:
			CityLocation.getFirstChoose().clear();
			CityLocation.getFirstRemove().clear();
			initCity(true);
			break;
			
		case 3:
		
			mEtSearch.setText(Constant.searchSelect);
			pageIndex=0;
//			TotalCar.clear();
			isSearch=true;
			getServerData();
			
		break;
		case 4:
			
			break;

			
		default:
			break;
		}
		
	}

	//下拉刷新的方法
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				isRefresh=true;
				getServerData();
				carAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	//加载更多的方法
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				isLoadMore=true;
				getServerData();
				carAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
		
	}
	
	//下拉刷新中时间的设置
	private String setDateTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());
		return time;
//		tvTime.setText(time);
	}
	
	private void onLoad() {
		mCarList.stopRefresh();
		mCarList.stopLoadMore();
		mCarList.setRefreshTime(setDateTime());
	}
	
}
