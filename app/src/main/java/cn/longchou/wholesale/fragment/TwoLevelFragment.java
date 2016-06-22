package cn.longchou.wholesale.fragment;

import java.util.List;
import java.util.Map;

import com.justlcw.letterlistview.letter.LetterListView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.MainActivity;
import cn.longchou.wholesale.adapter.AbcOrdersAdapter;
import cn.longchou.wholesale.adapter.MyBuyDrawerAdapter;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.domain.AvailBrand;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.global.Constant;
/**
 * 
* @Description: 筛选的第二个页面
*
* @author kangkang 
*
* @date 2016年1月8日 下午5:21:19 
*
 */
public class TwoLevelFragment extends BaseFragment {

	private ListView mLvRight;
	
	private ImageView imageBack;

	private static TextView textTitle;
	
	private DrawerLayout mDrawerLayout;
	
	private MyBuyDrawerAdapter rightAdapter;
	
	private AbcOrdersAdapter letterAdapter;
	
	//获取的条目的对象
	private ImageView image;

	//获取传递过来的前面的条目的下标
	private int index;
	
	private static String title;

	private String city;
	
	
	private static List<String> secondList;
	private static List<String> secondChoose;
	private static int secondIndex;

	//自定义的接口
	private static OnTitleListener onTitleListener;

	private static RadioButton mRBHotBrand;

	private RadioGroup mLLBrand;

	private RadioButton mRBSort;

	private List<String> list;
	
	private List<String> brandList;

	private LetterListView mLetterListView;

	private LinearLayout mLLBrandAll;

	private ImageView mTick;
	
	
	public static void setBudgetContent(String title,OnTitleListener onTitleListener) { 
		TwoLevelFragment.title=title;
		TwoLevelFragment.onTitleListener=onTitleListener;
	}
	
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_kang, null);
		
        mLvRight = (ListView) view.findViewById(R.id.lv_my_buy_right);
		
		imageBack = (ImageView) view.findViewById(R.id.iv_my_news_back);
		textTitle = (TextView) view.findViewById(R.id.tv_my_news_title);
		
		mLLBrand = (RadioGroup) view.findViewById(R.id.rg_brand);
		
		mRBHotBrand = (RadioButton) view.findViewById(R.id.rb_hot_brand);
		
		mRBSort = (RadioButton) view.findViewById(R.id.rb_abc_orders);
		
		mLetterListView = (LetterListView) view.findViewById(R.id.letterListView);
		
		//品牌的所有内容
		mLLBrandAll = (LinearLayout) view.findViewById(R.id.ll_two_brand);
		
		//品牌选中所有时的对号
		mTick = (ImageView) view.findViewById(R.id.iv_brand_tick);
		
		return view;
	}

	@Override
	public void initData() {
		mDrawerLayout = MainActivity.getDrawerLayout();
		
		textTitle.setText(title);
		
		setInitData();
		
	}
	//设置品牌是否可见
	private void setRadioButtonVisible() {
		if(index==0)
		{
			//当为品牌的时候让其显示
			mLLBrand.setVisibility(View.VISIBLE);
			mLLBrandAll.setVisibility(View.VISIBLE);
			//当选中全部的时候才让选中的对号显示
			if(Constant.isAllSelect)
			{
				mTick.setVisibility(View.VISIBLE);
			}else{
				mTick.setVisibility(View.INVISIBLE);
			}
		}else{
			mLLBrand.setVisibility(View.GONE);
			mLLBrandAll.setVisibility(View.GONE);
		}

	}
    
	
	//调用接口让让外部访问
	public interface OnTitleListener{
		void setImageBack(String imageBack);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden)
		{
			setInitData();
			//重新进入的时候默认让字母排序列表不选中。因为后面的刷选内容会被覆盖
			mRBHotBrand.setChecked(true);
			mRBSort.setChecked(false);
		}
	}
	
	//设置初始化信息
	private void setInitData() {
		list = OneLevelFragment.getList();
		index = OneLevelFragment.getIndex();
		String choose=null;
		setRadioButtonVisible();
		//根据不同位置index不同的位置设置筛选的条件
		if(index==0)
		{
			choose="screenHotBrand";
		}else if(index==1){
			choose="screenCarModel";
		
		}else if(index==2){
			choose="screenGearBox";
		
		}else if(index==3){
			choose="screenCarYears";
		
		}else if(index==4){
			choose="screenPrice";
		
		}else if(index==5){
			choose="screenMileage";
		}
		if(mRBHotBrand.isChecked())
		{
			rightAdapter = new MyBuyDrawerAdapter(getActivity(), list, choose);
			mLvRight.setAdapter(rightAdapter);
			mLvRight.setVisibility(View.VISIBLE);
			mLetterListView.setVisibility(View.GONE);
		}else{
			brandList=AvailBrand.getSortBrand();
			letterAdapter = new AbcOrdersAdapter(getActivity(), "brand");
			mLetterListView.setAdapter(letterAdapter);
			mLvRight.setVisibility(View.GONE);
			mLetterListView.setVisibility(View.VISIBLE);
		}

	}
	
	@Override
	public void initListener() {
		imageBack.setOnClickListener(this);
		mLLBrandAll.setOnClickListener(this);
		
		mLLBrand.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_hot_brand:
					mRBHotBrand.setChecked(true);
					mRBSort.setChecked(false);
					setInitData();
					break;
					
				case R.id.rb_abc_orders:
					mRBHotBrand.setChecked(false);
					mRBSort.setChecked(true);
					setInitData();
					break;

				default:
					break;
				}
				
			}
		});
		
		mLetterListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				image = (ImageView) view
						.findViewById(R.id.iv_my_buy_right_check);

				TextView text = (TextView) view
						.findViewById(R.id.tv_my_buy_right_choose);
//				String brand = brandList.get(position);
				String brand = AvailBrand.getSortABCBrand().get(position);
				if(!isContainsABC(brand))
				{
					MainActivity mActivity=(MainActivity) getActivity();
					mActivity.setButtonSelection(7);
//					AvailBrand.mScreenHotCarLine=brand;
					secondIndex=position;
					image.setVisibility(View.VISIBLE);
					text.setTextColor(Color.rgb(237, 108, 1));
					if(onTitleListener!=null)
					{
						onTitleListener.setImageBack(city);
					}
					
				}
			}
		});
		
		//二级选择
		mLvRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				image = (ImageView) view
						.findViewById(R.id.iv_my_buy_right_check);

				TextView text = (TextView) view
						.findViewById(R.id.tv_my_buy_right_choose);
				
				String city = list.get(position);
				
				
				
				//如果为5的话为里程
				if(index==5)
				{
					Constant.screenMileage=city;
				}//价格
				else if(index==4)
				{
					Constant.screenPrice=city;
				}//车龄
				else if(index==3)
				{
					Constant.screenCarYears=city;
				}//变速箱
				else if(index==2)
				{
					Constant.screenGearBox=city;
				}//车型
				else if(index==1)
				{
					Constant.screenCarModel=city;
				}//品牌
				else if(index==0)
				{
//					AvailBrand.mScreenHotBrand=city;
					//如果品牌选择的是全部则让车系变为全部
					if("全部".equals(city))
					{
						AvailBrand.mScreenHotCarLine="全部";
					}
					secondIndex=position;
					//品牌集合中不为0时才展现三级筛选
//					if(position!=0 && listBrand!=null)
//					{
//						MainActivity mActivity=(MainActivity) getActivity();
//						mActivity.setButtonSelection(7);
//					}
//					isAllSelect=false;
					Map<String, List<String>> hotBrands = AvailBrand.hotBrands;
					String brand = AvailBrand.getHotBrand().get(position);
					List<String> listBrand = hotBrands.get(brand);
					if(listBrand!=null)
					{
						MainActivity mActivity=(MainActivity) getActivity();
						mActivity.setButtonSelection(7);
					}
					
				}
				
				image.setVisibility(View.VISIBLE);
				text.setTextColor(Color.rgb(237, 108, 1));
				if(onTitleListener!=null)
				{
					if(index!=0)
					{
						onTitleListener.setImageBack(city);
					}else{
						onTitleListener.setImageBack("全部");
					}
				}
				rightAdapter.notifyDataSetChanged();
				
			}
		});

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
	
	//热门的品牌是否选中
	public static boolean isHotBrandChecked()
	{
		return mRBHotBrand.isChecked();
	}
	
	public static List<String> getList()
	{
		return secondList;
	}
	
	public static List<String> getChoose()
	{
		return secondChoose;
	}
	
	public static int getIndex(){
		return secondIndex;
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			MainActivity mActivity=(MainActivity) getActivity();
			mActivity.setButtonSelection(5);
			break;

		case R.id.ll_two_brand:
			if(!Constant.isAllSelect)
			{
				Constant.isAllSelect=true;
				mTick.setVisibility(View.VISIBLE);
				AvailBrand.mScreenHotCarLine="全部";
				AvailBrand.mScreenHotBrand="全部";
			}
			
			rightAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}

	}

}
