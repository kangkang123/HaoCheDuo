package cn.longchou.wholesale.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.BudgetConfirmActivity;
import cn.longchou.wholesale.activity.MainActivity;
import cn.longchou.wholesale.activity.VehicleDetailActivity;
import cn.longchou.wholesale.adapter.CartCarAdapter;
import cn.longchou.wholesale.adapter.CartCarAdapter.CallbackCartCar;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.domain.AttentionAddDelete;
import cn.longchou.wholesale.domain.CarsInfo;
import cn.longchou.wholesale.domain.CartAddDelete;
import cn.longchou.wholesale.domain.HomePage;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.domain.ItemSelect;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.manage.CarsManager;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.utils.WeiXinUtils;
/**
 * 
* @Description: 购物车的操作
*
* @author kangkang 
*
* @date 2015年12月24日 下午4:26:04 
*
 */
public class OrdersFragment extends BaseFragment{

	//判断文字是否为编辑
	boolean isEdit=true;
	//全选的控制
	boolean isCheck;
	public CheckBox mCheck;
	private TextView mTvEarnest;
	private TextView mTvTotal;
	private TextView mTvCommit;
	private SwipeMenuListView mLvCartCar;
	private View adView;
	private List<Integer> ids;
	private View mEmpty;
	private View finish;
	private TextView mGoMarket;
	private String token;
	
	private CartCarAdapter adapter;
	private List<CarsInfo> cartCarList;
	
	//从服务器获取的购物车的信息
	private List<Cars> carsList;
	private LoadingView loading;
	private TextView mOrderEmpty;
	private float totalSub;
	private float totalMoney;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_orders, null);
		
		mLvCartCar = (SwipeMenuListView) view.findViewById(R.id.lv_cartcar);
		
		//当购物车为空的时候
		mEmpty = view.findViewById(R.id.view_no_car);
		
		//购物车底部的提交订单的内容
		finish = view.findViewById(R.id.item_cart_car);
		
		//提交订单的选中按钮
		mCheck = (CheckBox) view.findViewById(R.id.cb_choose);
		
		//订单完成部分
		//定金
		mTvEarnest = (TextView) view.findViewById(R.id.tv_finish_earnest);
		//总金额
		mTvTotal = (TextView) view.findViewById(R.id.tv_finish_total);
		//提交订单
		mTvCommit = (TextView) view.findViewById(R.id.tv_finish_commit);
		
		mGoMarket = (TextView) view.findViewById(R.id.tv_try_shop);
		
		mOrderEmpty = (TextView) view.findViewById(R.id.tv_try_message);
		
		loading = (LoadingView) view.findViewById(R.id.loadView);
	 
		return view;
	}

	private void setCartCarAdapter() {
		
		if(cartCarList.size()>0 && cartCarList!=null)
		{
			
			adapter = new CartCarAdapter(getActivity(), cartCarList, mCheck, Constant.CartCar, new CallbackCartCar() {
				
				@Override
				public void click(View v) {
					boolean isCertified = PreferUtils.getBoolean(getActivity(), "isCertified", false);
					//只有认证的时候才能选择
					if(isCertified)
					{
						CarsInfo carsInfo = cartCarList.get((Integer) v.getTag());
						if(carsInfo.carPrice.equals("已下架"))
						{
							UIUtils.showToastSafe("已下架不能选择");
						}else{
							adapter.selectSingle((Integer) v.getTag());
							setTotalMoney();
							//每次点击的时候判断是否全选，如果全选的话让全选按钮变为true
//							if(checkCarSelectAll())
//							{
//								mCheck.setChecked(true);
//							}
						}
					}else{
						UIUtils.showToastSafe("认证用户才能选择");
					}
				}
			});
		}
	}
	
	//设置选中车辆是所需要付的金钱
	private void setTotalMoney() {
		totalSub = 0;
		totalMoney = 0;
		List<CarsInfo> list = CarsManager.getInstance().getCartCarList();
		for(int i=0;i<list.size();i++)
		{
			CarsInfo carsInfo = list.get(i);
			if(carsInfo.isSelect)
			{
				totalSub+=carsInfo.carSubscription;
				float price = Float.parseFloat(carsInfo.carPrice.replace(",", ""));
				totalMoney+=price;
			}
		}
		
		
		mTvEarnest.setText(totalSub+"0元");
		
		if(isTwoLevel(totalMoney))
		{
			mTvTotal.setText(totalMoney+"万元");
		}else{
			mTvTotal.setText(totalMoney+"0万元");
		}
		
	}
	
	private boolean isTwoLevel(float money)
	{
		String test=money+"";
		int len=test.length()-test.indexOf(".");
		if(len==3)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void initData() {
		
		mOrderEmpty.setText("购物车是空的哦！\n去车市挑喜欢的车吧");
		
		boolean isLogin = PreferUtils.getBoolean(getActivity(), "isLogin", false);
		if(isLogin)
		{
			//获取购物车的数据
			getServerData();
		}else{
			UIUtils.showToastSafe("请登录！");
		}
		
		
		//添加侧边栏的内容
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem carItem = new SwipeMenuItem(
						getActivity());
				carItem.setBackground(new ColorDrawable(Color.rgb(255, 181,
						14)));
				carItem.setWidth(dp2px(60));
				carItem.setTitle("加入关注");
				carItem.setTitleSize(12);
				carItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(carItem);
				
				
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getActivity());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(255,
						52, 13)));
				deleteItem.setTitleSize(12);
				deleteItem.setWidth(dp2px(60));
				deleteItem.setTitle("删除");
				deleteItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(deleteItem);
			}
		};
		
		mLvCartCar.setMenuCreator(creator);

		mLvCartCar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				CarsInfo carsInfo = cartCarList.get(position);
				switch (index) {
				case 0:
					if(!carsInfo.isFollow)
					{
						addAttention(carsInfo,position);
					}else{
						UIUtils.showToastSafe("该车已经关注");
					}
					break;
				
				case 1:
					deleteCartData(carsInfo,position);
					if(CarsManager.getInstance().getCartCarList()==null)
					{
						mEmpty.setVisibility(View.VISIBLE);
						mLvCartCar.setVisibility(View.INVISIBLE);
						finish.setVisibility(View.GONE);
					}
					break;
				
				}
			}
		});
		
	}
	
	protected void deleteCartData(final CarsInfo carsInfo, final int position) {

		String token = PreferUtils.getString(getActivity(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestRemoveFromCart;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("carID", carsInfo.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result = resultInfo.result;
				Gson gson=new Gson();
				CartAddDelete data = gson.fromJson(result, CartAddDelete.class);
				if(TextUtils.isEmpty(data.errorMsg))
				{
					UIUtils.showToastSafe("购物车删除成功");
					cartCarList.remove(position);
					carsList.remove(position);
					CarsManager.getInstance().removeCartCar(carsInfo);
					adapter.notifyDataSetChanged();
					setCartCarVisible();
					
					int TotalCars = PreferUtils.getInt(getActivity(), "totalCars", 0);
					if(TotalCars-1!=0)
					{
						MainActivity.getBadgeViewText().setText(TotalCars-1+"");
						PreferUtils.putInt(getActivity(), "totalCars", TotalCars-1);
					}else{
						MainActivity.getBadgeViewText().hide();
						MainActivity.getBadgeViewText().setText(TotalCars-1+"");
						PreferUtils.putInt(getActivity(), "totalCars", TotalCars-1);
					}
					
				}else{
					UIUtils.showToastSafe(data.errorMsg);
				}
				
			}
		});
	}

	//添加到关注
	protected void addAttention(CarsInfo carsInfo, int position) {

		String token = PreferUtils.getString(getActivity(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestAddFavorite;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("carID", carsInfo.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result = resultInfo.result;
				Gson gson=new Gson();
				AttentionAddDelete data = gson.fromJson(result, AttentionAddDelete.class);
				if(data.success)
				{
					UIUtils.showToastSafe("添加关注成功");
					
				}else{
					if(TextUtils.isEmpty(data.errorReason))
					{
						UIUtils.showToastSafe("已经关注不能重复关注!");
					}else{
						UIUtils.showToastSafe(data.errorReason);
					}
				}
				
			}
		});
		
	}

	private void setCartCarVisible() {
        //如果购物车不为空的情况下
		if(cartCarList!=null && cartCarList.size()>0)
		{
			mEmpty.setVisibility(View.INVISIBLE);
			mLvCartCar.setVisibility(View.VISIBLE);
			finish.setVisibility(View.VISIBLE);
			
		}else{
			mEmpty.setVisibility(View.VISIBLE);
			mLvCartCar.setVisibility(View.INVISIBLE);
			finish.setVisibility(View.GONE);
		}

	}
	
	public boolean checkCarState()
	{
		if(cartCarList!=null)
		{
			for(int i=0;i<cartCarList.size();i++)
			{
				CarsInfo carsInfo = cartCarList.get(i);
				if(carsInfo.carPrice.equals("已下架"))
					return false;
			}
		}
		return true;
	}
	
	private void getServerData() {
		
		token = PreferUtils.getString(getActivity(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestCartCar;
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {


			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				UIUtils.showToastSafe(arg1);
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				Gson gson=new Gson();
				HomePage json = gson.fromJson(result, HomePage.class);
				
				//在获取到数据之后无让loading消失
				loading.setVisibility(View.GONE);
				
				carsList = json.cars;
				if(carsList!=null)
				{
					//首先清除所有的本地种的购物车
					CarsManager.getInstance().clearCartCar();
					
					//遍历获取的购物车的集合，重新放入本地
					for(int i=0;i<carsList.size();i++)
					{
						//遍历所有的集合放入购物车的管理器中
						Cars info = carsList.get(i);
						CarsManager.getInstance().joinCartCar(info);
					}
					cartCarList = CarsManager.getInstance().getCartCarList();
					
					//加载数据的时候重新判断是否全选，如果没有全选的话，让全选按钮变为false，否则全选按钮会一直选中
					if(!checkCarSelectAll())
					{
						mCheck.setChecked(false);
						//当重新进入的时候让让价格显示为0
						mTvEarnest.setText("0.00元");
						mTvTotal.setText("0.00万元");
					}
					
					if(cartCarList.size()>0)
					{
						setCartCarAdapter();
						mLvCartCar.setAdapter(adapter);
						
						MainActivity.getBadgeViewText().show();
						PreferUtils.putInt(getActivity(), "totalCars", cartCarList.size());
						MainActivity.getBadgeViewText().setText(cartCarList.size()+"");
					}else{
						MainActivity.getBadgeViewText().hide();
					}
					
					setCartCarVisible();
					
				}
			}
		});
		
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden)
		{
			
//			loading.setVisibility(View.VISIBLE);
			boolean isLogin = PreferUtils.getBoolean(getActivity(), "isLogin", false);
			if(isLogin)
			{
				//获取购物车的数据
				getServerData();
			}else{
				UIUtils.showToastSafe("请登录！");
			}
			//此句会让加载中和空页面同时显示重叠
//			setCartCarVisible();
			
//			String localIpAddress = WeiXinUtils.getLocalIpAddress(getActivity());
//			UIUtils.showToastSafe(localIpAddress);
		}
	}

	@Override
	public void initListener() {
		mTvCommit.setOnClickListener(this);
		mGoMarket.setOnClickListener(this);
		
		//listview的点击事件
		mLvCartCar.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if("已下架".equals(carsList.get(position).carPrice+""))
				{
					UIUtils.showToastSafe("车辆已下架不能查看详情!");
				}else{
					Intent intent=new Intent(getActivity(),VehicleDetailActivity.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("cars", carsList.get(position));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		
		//复选框的点击事件，只有当不是选中状态的时候让取消全部选中状态，并且改变价格
		mCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isCheck)
				{
					adapter.cancelSeclectAll();
					setTotalMoney();
				}
			}
		});
		
		//全选按钮的监听
		mCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean isCertified = PreferUtils.getBoolean(getActivity(), "isCertified", false);
				//如果认证的话
				if(isCertified)
				{
					isCheck=isChecked;
					if(isCheck)
					{
						if(checkCarState())
						{
							adapter.selectAll();
							setTotalMoney();
						}else{
							mCheck.setChecked(false);
							UIUtils.showToastSafe("有已下架的不能全选!");
						}
					}else{  //如果不是选中状态让改变金钱的价格
							setTotalMoney();
					}
				}//如果没有认证的话
				else{
					mCheck.setChecked(false);
					UIUtils.showToastSafe("认证用户才能选择");
				}
				
			}
		});
	}
	
	//检查是否全选,只有当全部选中才返回true
	public boolean checkCarSelectAll()
	{
		List<CarsInfo> list = CarsManager.getInstance().getCartCarList();
		for(int i=0;i<list.size();i++)
		{
			CarsInfo carsInfo = list.get(i);
			if(!carsInfo.isSelect)
			{
				return false;
			}
		}
		return true;
	}
	
	
	//检查是否有选中的
	public boolean checkCarSelect()
	{
		List<CarsInfo> list = CarsManager.getInstance().getCartCarList();
		for(int i=0;i<list.size();i++)
		{
			CarsInfo carsInfo = list.get(i);
			if(carsInfo.isSelect)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.tv_finish_commit:
			if(checkCarSelect())
			{
				Intent intent=new Intent(getActivity(),BudgetConfirmActivity.class);
				startActivityForResult(intent, 1);
			}else{
				UIUtils.showToastSafe("请选择!");
			}
			break;
		case R.id.tv_try_shop:
			MainActivity mActivity=(MainActivity) getActivity();
			mActivity.setButtonSelection(0);
			break;
		
		default:
			break;
		}

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			mCheck.setChecked(false);
			getServerData();
			break;
		case 3:
			adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
	
}
