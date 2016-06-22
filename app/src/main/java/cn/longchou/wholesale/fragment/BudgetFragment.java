package cn.longchou.wholesale.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Text;

import com.justlcw.letterlistview.letter.LetterListView;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.MyBuyActivity;
import cn.longchou.wholesale.adapter.AbcOrdersAdapter;
import cn.longchou.wholesale.adapter.MyBuyCityAdapter;
import cn.longchou.wholesale.adapter.MyBuyDrawerAdapter;
import cn.longchou.wholesale.adapter.MyHotBrandAdapter;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.domain.AvailBrand;
import cn.longchou.wholesale.domain.BudgetData;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.UIUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BudgetFragment extends BaseFragment {

	private ListView mLvRight;

//	private MyBuyBudgetAdapter budgetAdapter;

	private ImageView imageBack;

	private static TextView textTitle;
	
	private static String title;
	
	//自定义的接口
	private static OnTitleListener onTitleListener;

	private int index;

	//打钩的图片
	private ImageView mImage;
	

	private DrawerLayout mDrawerLayout;


	private AbcOrdersAdapter brandAdapter;

	private MyHotBrandAdapter cityAdapter;
	
	private List<String> listCars;
	
	private MyHotBrandAdapter carsAdapter;

	private MyHotBrandAdapter budgetAdapter;

	private List<String> listBudget;

	private LetterListView letterListView;
	
	public static void setBudgetContent(String title,OnTitleListener onTitleListener) {
		BudgetFragment.title=title;
		BudgetFragment.onTitleListener=onTitleListener;
	}
	
	
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_kang, null);
		mLvRight = (ListView) view.findViewById(R.id.lv_my_buy_right);
		
		imageBack = (ImageView) view.findViewById(R.id.iv_my_news_back);
		textTitle = (TextView) view.findViewById(R.id.tv_my_news_title);
		
		letterListView = (LetterListView) view.findViewById(R.id.letterListView);
		
		
		return view;
	}
	

	@Override
	public void initData() {
		
		textTitle.setText(title);
		
		index = getArguments().getInt("position");
		if(index==0)
		{
			//设置城市的adapter
			cityAdapter();
			
		}else if(index==1)
		{
			//设置预算的adapter
			budgetAdapter();
		}else if(index==3){
			letterListView.setVisibility(View.VISIBLE);
			mLvRight.setVisibility(View.GONE);
			//设置品牌的adapter
			hotBrandAdapter();
		}else if(index==4)
		{
			//设置车系的adapter
			carsAdapter();
		}
		
		mDrawerLayout = MyBuyActivity.getDrawLayout();
		
	}
	
	private void hotBrandAdapter() {
		brandAdapter=new AbcOrdersAdapter(getActivity(), "buy");
		letterListView.setAdapter(brandAdapter);
		
	}


	private void cityAdapter() {
		List<String> listCity = CityLocation.getBuyCity();
        cityAdapter = new MyHotBrandAdapter(getActivity(),listCity,"city");
		mLvRight.setAdapter(cityAdapter);
		
	}
	private void carsAdapter() {
		String brand = AvailBrand.mHotBrand;
		Map<String, List<String>> hotBrands = AvailBrand.sortBrands;
		listCars = hotBrands.get(brand);
		carsAdapter = new MyHotBrandAdapter(getActivity(), listCars, "car");
		mLvRight.setAdapter(carsAdapter);
		
	}


	private void budgetAdapter() {
		listBudget = BudgetData.getBudget();
        budgetAdapter = new MyHotBrandAdapter(getActivity(), listBudget, "budget");
		mLvRight.setAdapter(budgetAdapter);

	}
	
	//调用接口让让外部访问
	public interface OnTitleListener{
		void setImageBack(String city);
	}

	@Override
	public void initListener() {
		
		imageBack.setOnClickListener(this);
		
		letterListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setBrandData(position);
				
			}
		});
		
		mLvRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mImage = (ImageView) view.findViewById(R.id.iv_my_buy_right_check);
				if(index==0)
				{
					//设置城市的数据
					setCityData(position);
				}
				else if(index==1)
				{
					//设置预算的数据
					setBudgetData(position);
				}
				else if(index==3){
					//设置品牌的数据
//					setBrandData(position);
				}else if(index==4)
				{
					//设置车系的数据
					setCarsData(position);
				}
				
			}
		});

	}
	
	protected void setBudgetData(int position) {
		String budget = listBudget.get(position);
		BudgetData.budget=budget;
		mImage.setVisibility(View.VISIBLE);
		if(onTitleListener!=null)
		{
			onTitleListener.setImageBack(budget);
		}
		budgetAdapter.notifyDataSetChanged();
		
	}


	protected void setCarsData(int position) {
		String car = listCars.get(position);
		AvailBrand.mHotCarLine=car;
		mImage.setVisibility(View.VISIBLE);
		if(onTitleListener!=null)
		{
			onTitleListener.setImageBack(car);
		}
		carsAdapter.notifyDataSetChanged();
	}


	protected void setCityData(int position) {
		List<String> buyCity = CityLocation.getBuyCity();
		String city = buyCity.get(position);
		CityLocation.mBuyCity=city;
		mImage.setVisibility(View.VISIBLE);
		if(onTitleListener!=null)
		{
			onTitleListener.setImageBack(city);
		}
		cityAdapter.notifyDataSetChanged();
		
	}


	protected void setBrandData(int position) {
		List<String> hotBrand = AvailBrand.getSortABCBrand();
		String brand = hotBrand.get(position);
		
		if(!isContainsABC(brand))
		{
			AvailBrand.mHotBrand=brand;
//			mImage.setVisibility(View.VISIBLE);
			if(onTitleListener!=null)
			{
				onTitleListener.setImageBack(brand);
			}
			brandAdapter.notifyDataSetChanged();
		}
		
	}
	
	//是否包含字母
	private boolean isContainsABC(String brand)
	{
		String [] brands={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		for(int i=0;i<brands.length;i++)
		{
			if(brands[i].equals(brand))
			{
				return true;
			}
		}
		return false;
	}


	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			mDrawerLayout.closeDrawers();
			break;

		default:
			break;
		}

	}
}
