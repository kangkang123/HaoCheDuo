package cn.longchou.wholesale.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.MainActivity;
import cn.longchou.wholesale.adapter.HotSearchAdapter;
import cn.longchou.wholesale.adapter.ScreenOneAdapter;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.domain.AvailBrand;
import cn.longchou.wholesale.domain.HotActivity;
import cn.longchou.wholesale.domain.HotActivity.HotAction;
import cn.longchou.wholesale.domain.MyBuy;
import cn.longchou.wholesale.domain.MyScreenOne;
import cn.longchou.wholesale.fragment.TwoLevelFragment.OnTitleListener;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 筛选的第一个页面
*
* @author kangkang 
*
* @date 2016年1月8日 下午5:21:19 
*
 */
public class OneLevelFragment extends BaseFragment {

	private TextView mCancel;
	private DrawerLayout mDrawerLayout;
	private GridView mGridView;
	
	private String chooseSave;
	private HotSearchAdapter adapter;
	private ListView mLvScreen;
	private TextView mTvReset;
	private TextView mTvConfirm;
	private ScreenOneAdapter screenAdapter;
	
	private static List<String> list;
	private static String choose;
	
	private static int index;
	
	private static OnIndexListener onIndexListener;
	private MainActivity mActivity;
	private List<HotAction> hotActivityData;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_one_level, null);
		
		mCancel = (TextView) view.findViewById(R.id.tv_one_screen_cancel);
		
		mGridView = (GridView) view.findViewById(R.id.gv_sreen_one);
		
		mLvScreen = (ListView) view.findViewById(R.id.lv_screen_one);
		
		mTvReset = (TextView) view.findViewById(R.id.tv_one_screen_reset);
		mTvConfirm = (TextView) view.findViewById(R.id.tv_one_screen_confirm);
		
		return view;
	}

	@Override
	public void initData() {
	   mActivity = (MainActivity) getActivity();
	   mDrawerLayout = MainActivity.getDrawerLayout();

	   hotActivityData = paraseHotActivityData();
	   paraseHotBrand();
	   
	   //热门活动
	   adapter = new HotSearchAdapter(getActivity(),hotActivityData);
	   mGridView.setAdapter(adapter);
	   
	   MyScreenOne screenOne=new MyScreenOne();
	   screenAdapter = new ScreenOneAdapter(getActivity(),MyScreenOne.getScreenOne());
	   mLvScreen.setAdapter(screenAdapter);
	}

	//解析筛选的热门活动
	private List<HotAction> paraseHotActivityData() {
		String hotActivity = Constant.hotActivity;
//		UIUtils.showToastSafe(hotActivity);
		Gson gson=new Gson();
		HotActivity json = gson.fromJson(hotActivity, HotActivity.class);
		List<HotAction> hotActivityList = json.saleActionWholeSaleTypeList;
		return hotActivityList;
	}
	
	//解析数据并把数据保存起来
	private void paraseHotBrand() {
		String result = PreferUtils.getString(getActivity(), "availableBrand", null);
//		String result = Constant.availableBrand;
		if(!TextUtils.isEmpty(result))
		{
			Gson gson=new Gson();
			AvailBrand fromJson = gson.fromJson(result, AvailBrand.class);
			if(null!=fromJson)
			{
				Map<String, Map<String, List<String>>> brand = fromJson.brand;
				if(null!=fromJson.hotBrand)
				{
					//设置热搜品牌的数据
					AvailBrand.setHotBrand(fromJson.hotBrand);
				}
				
				//设置品牌字母排序的数据
				AvailBrand.setSortBrand(brand);
			}
		}
	}

	//设置里程
	private void setMileage() {
		TwoLevelFragment.setBudgetContent("里程", new OnTitleListener() {
			
			@Override
			public void setImageBack(String imageBack) {
				if (!TextUtils.isEmpty(imageBack)) {
					MyScreenOne item = screenAdapter.getItem(5);
					item.choose=imageBack;
					screenAdapter.notifyDataSetChanged();
				}
				
			}
		});
	}
	
	//设置价格
	private void setPrice() {
		TwoLevelFragment.setBudgetContent("价格", new OnTitleListener() {
			
			@Override
			public void setImageBack(String imageBack) {
				if (!TextUtils.isEmpty(imageBack)) {
					MyScreenOne item = screenAdapter.getItem(4);
					item.choose=imageBack;
					screenAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	//设置车龄
	private void setCarYears() {
		TwoLevelFragment.setBudgetContent("车龄", new OnTitleListener() {
			
			@Override
			public void setImageBack(String imageBack) {
				if (!TextUtils.isEmpty(imageBack)) {
					MyScreenOne item = screenAdapter.getItem(3);
					item.choose=imageBack;
					screenAdapter.notifyDataSetChanged();
				}
				
			}
		});
	}
	
	//设置变速箱
	private void setGearBox() {
		TwoLevelFragment.setBudgetContent("变速箱", new OnTitleListener() {
			
			@Override
			public void setImageBack(String imageBack) {
				if (!TextUtils.isEmpty(imageBack)) {
					MyScreenOne item = screenAdapter.getItem(2);
					item.choose=imageBack;
					screenAdapter.notifyDataSetChanged();
				}
				
			}
		});
	}
	//设置车型
	private void setCarModel() {
		TwoLevelFragment.setBudgetContent("车型", new OnTitleListener() {
			
			@Override
			public void setImageBack(String imageBack) {
				if (!TextUtils.isEmpty(imageBack)) {
					MyScreenOne item = screenAdapter.getItem(1);
					item.choose=imageBack;
					screenAdapter.notifyDataSetChanged();
				}
				
			}
		});
	}
	//设置品牌
	private void setHotBrand() {
		TwoLevelFragment.setBudgetContent("品牌", new OnTitleListener() {
			
			@Override
			public void setImageBack(String imageBack) {
				if (!TextUtils.isEmpty(imageBack)) {
					MyScreenOne item = screenAdapter.getItem(0);
					item.choose=imageBack;
//					item.choose=AvailBrand.mScreenHotCarLine;
					screenAdapter.notifyDataSetChanged();
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
			MyScreenOne item = screenAdapter.getItem(0);
			if(AvailBrand.mScreenHotCarLine!=null)
			{
				item.choose=AvailBrand.mScreenHotCarLine;
				screenAdapter.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public void initListener() {
		mCancel.setOnClickListener(this);
		mTvReset.setOnClickListener(this);
		mTvConfirm.setOnClickListener(this);
		
		
		
		mLvScreen.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//清空活动
				Constant.screenAvtivity=null;
				adapter.notifyDataSetChanged();
				
				MainActivity mActivity=(MainActivity) getActivity();
				mActivity.setButtonSelection(6);
				if(position==5)
				{
					index=position;
					list = Constant.getMileage();
					setMileage();
				}
				else if(position==4)
				{
					index=position;
					list=Constant.getPrice();
					setPrice();
				}else if(position==3)
				{
					index=position;
					list=Constant.getCarYears();
					setCarYears();
				}else if(position==2)
				{
					index=position;
					list=Constant.getGearBox();
					setGearBox();
				}
				else if(position==1)
				{
					index=position;
					list=Constant.getCarModel();
					setCarModel();
				}
				else if(position==0)
				{
					index=position;
					list=AvailBrand.getHotBrand();
					setHotBrand();
				}
				
			}
		});
		
		
		// 给GridView设置点击事件
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView mText=(TextView) view.findViewById(R.id.tv_item_hot);
				
				//获取默认的点击的内容
				HotAction chooseData = hotActivityData.get(position);
				Constant.screenAvtivity=chooseData.actionName;
				mText.setTextColor(Color.rgb(237, 108, 1));
				Drawable drawable = getResources().getDrawable(R.drawable.shape_screen_item_choose);
				mText.setBackgroundDrawable(drawable);
				adapter.notifyDataSetChanged();
				
				setScreenClear();
			}
		});

	}
	
	//清空筛选的条件
	private void setScreenClear() {
		
		//保存筛选选中的里程
		Constant.screenHotBrand="全部";
		//保存筛选选中的车型
		Constant.screenCarModel="全部";
		//保存筛选选中的变速箱
		Constant.screenGearBox="全部";
		//保存筛选选中的车龄
		Constant.screenCarYears="全部";
		//保存筛选选中的价格
		Constant.screenPrice="全部";
		//保存筛选选中的里程
		Constant.screenMileage="全部";
		
		AvailBrand.mScreenHotCarLine="全部";
		
		AvailBrand.mScreenHotBrand="全部";

		for(int i=0;i<MyScreenOne.getScreenOne().size();i++)
		{
			MyScreenOne item = screenAdapter.getItem(i);
			item.choose="全部";
		}
		screenAdapter.notifyDataSetChanged();
	}
	
	//获取各个筛选条件的不同的list集合
	public static List<String> getList()
	{
		return list;
	}
	
	//获取筛选条件的要筛选内容的下标
	public static int getIndex(){
		return index;
	}
	
	public static void setScreenConfirmListener(OnIndexListener onIndexListener){
	    OneLevelFragment.onIndexListener=onIndexListener;
	}
	
	//判断是取消还是确认，根据isConfirm的状态
	public interface OnIndexListener{
		void getScreenConfirm(boolean isConfirm);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.tv_one_screen_cancel:
			mDrawerLayout.closeDrawers();
			if(onIndexListener!=null)
			{
				onIndexListener.getScreenConfirm(false);
			}
			//当取消选则的时候调用，让取消状态变为false
			Constant.isCancel=true;
			mActivity.setButtonSelection(4);
			break;
		case R.id.tv_one_screen_confirm:
			mDrawerLayout.closeDrawers();
			if(onIndexListener!=null)
			{
				onIndexListener.getScreenConfirm(true);
			}
			mActivity.setButtonSelection(4);
			
			break;
		case R.id.tv_one_screen_reset:
			setScreenClear();
			Constant.screenAvtivity=null;
			adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}

	}

}
