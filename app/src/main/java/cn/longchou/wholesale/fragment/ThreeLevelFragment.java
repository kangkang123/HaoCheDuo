package cn.longchou.wholesale.fragment;

import java.util.ArrayList;
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
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.MainActivity;
import cn.longchou.wholesale.adapter.MyBuyDrawerAdapter;
import cn.longchou.wholesale.adapter.MyHotBrandAdapter;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.domain.AvailBrand;
import cn.longchou.wholesale.domain.BudgetData;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 筛选的第三个页面
*
* @author kangkang 
*
* @date 2016年1月8日 下午5:21:19 
*
 */
public class ThreeLevelFragment extends BaseFragment {

	private ListView mLvRight;
	
	private ImageView imageBack;

	private static TextView textTitle;
	
	private DrawerLayout mDrawerLayout;
	
	private MyHotBrandAdapter rightAdapter;
	
	private static String title;
	
	
	private int index;
	
	//获取的条目的对象
	private ImageView image;
	
	//自定义的接口
	private static OnTitleListener onTitleListener;

	private List<String> listHotBrand;

	
	private LetterListView mLetterListView;
	
	private LinearLayout mLLBrandAll;

	private ImageView mTick;
	
	//品牌的内容
//	private List<String> brandList=new ArrayList<String>();
	
	//是否第一次加入
	private boolean isFirst=false;
	
	
	public static void setBudgetContent(String title,OnTitleListener onTitleListener) {
		ThreeLevelFragment.title=title;
		ThreeLevelFragment.onTitleListener=onTitleListener;
	}
	
	//调用接口让让外部访问
	public interface OnTitleListener{
		void setImageBack(String imageBack);
	}
	
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_kang, null);
		
        mLvRight = (ListView) view.findViewById(R.id.lv_my_buy_right);
		
		imageBack = (ImageView) view.findViewById(R.id.iv_my_news_back);
		textTitle = (TextView) view.findViewById(R.id.tv_my_news_title);
		
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

		index = TwoLevelFragment.getIndex();
		
		mLvRight.setVisibility(View.VISIBLE);
		mLetterListView.setVisibility(View.GONE);
		mLLBrandAll.setVisibility(View.VISIBLE);
		
		//如果是热门品牌的页面
		if(TwoLevelFragment.isHotBrandChecked())
		{
			//获取热门品牌的数据
			Map<String, List<String>> hotBrands = AvailBrand.hotBrands;
			String brand = AvailBrand.getHotBrand().get(index);
			listHotBrand = hotBrands.get(brand);
		}else{
			//获取字母排序品牌的数据
			Map<String, List<String>> sortBrands = AvailBrand.getMapSortBrand();
			String brandSort = AvailBrand.getSortABCBrand().get(index);
			listHotBrand = sortBrands.get(brandSort);
//			UIUtils.showToastSafe(brandSort+":"+index);
		}
		
		if(listHotBrand!=null)
		{
			rightAdapter = new MyHotBrandAdapter(getActivity(), listHotBrand, "hotBrand");
			mLvRight.setAdapter(rightAdapter);
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden)
		{
			//在Fragment重新显示的时候让重新获取数据
			mLvRight.setVisibility(View.VISIBLE);
			mLetterListView.setVisibility(View.GONE);
			index = TwoLevelFragment.getIndex();
			
			if(TwoLevelFragment.isHotBrandChecked())
			{
				Map<String, List<String>> hotBrands = AvailBrand.hotBrands;
				String brand = AvailBrand.getHotBrand().get(index);
				listHotBrand = hotBrands.get(brand);
				
				//保存内容和当前获取的品牌内容相同，则让对号显示，否则不让对号显示
				String availBrand = AvailBrand.mScreenHotCarLine;
				if(availBrand.equals(brand))
				{
					mTick.setVisibility(View.VISIBLE);
				}else{
					mTick.setVisibility(View.INVISIBLE);
				}
			}else{
				//获取字母排序品牌的数据
				Map<String, List<String>> sortBrands = AvailBrand.getMapSortBrand();
				String brandSort = AvailBrand.getSortABCBrand().get(index);
				listHotBrand = sortBrands.get(brandSort);
//				UIUtils.showToastSafe(brandSort+":"+index);
				
				//保存内容和当前获取的品牌内容相同，则让对号显示，否则不让对号显示
				String availBrand = AvailBrand.mScreenHotCarLine;
				if(availBrand.equals(brandSort))
				{
					mTick.setVisibility(View.VISIBLE);
				}else{
					mTick.setVisibility(View.INVISIBLE);
				}
			}
			
			
			//重新填充内容
			if(listHotBrand!=null || listHotBrand.size()>0)
			{
				rightAdapter = new MyHotBrandAdapter(getActivity(), listHotBrand, "hotBrand");
				mLvRight.setAdapter(rightAdapter);
			}
		}
	}

	@Override
	public void initListener() {
		imageBack.setOnClickListener(this);
		mLLBrandAll.setOnClickListener(this);
		
		mLvRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				image = (ImageView) view
						.findViewById(R.id.iv_my_buy_right_check);

					setBudgetData(position);
			}
		});

	}
	
	protected void setBudgetData(int position) {
		
		String brand = listHotBrand.get(position);
//		String brandCar = AvailBrand.getHotBrand().get(index);
//		AvailBrand.mScreenHotBrand=brandCar;
//		if("全部".equals(brand))
//		{
//			AvailBrand.mScreenHotCarLine = brandCar;
//		}else{
//			AvailBrand.mScreenHotCarLine=brand;
//		}
		AvailBrand.mScreenHotCarLine=brand;
		AvailBrand.mScreenHotBrand=AvailBrand.getSortABCBrand().get(index);
		mTick.setVisibility(View.INVISIBLE);
		image.setVisibility(View.VISIBLE);
		Constant.isAllSelect=false;
		if(onTitleListener!=null)
		{
			onTitleListener.setImageBack(AvailBrand.mScreenHotCarLine);
		}
		rightAdapter.notifyDataSetChanged();
		
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			MainActivity mActivity=(MainActivity) getActivity();
			mActivity.setButtonSelection(5);
			break;

		case R.id.ll_two_brand:
			if(TwoLevelFragment.isHotBrandChecked())
			{
				//从热门品牌列表获取的车系
				String brandCar = AvailBrand.getHotBrand().get(TwoLevelFragment.getIndex());
				AvailBrand.mScreenHotCarLine=brandCar;
				AvailBrand.mScreenHotBrand=brandCar;
				mTick.setVisibility(View.VISIBLE);
			}else{
				String brandSort = AvailBrand.getSortABCBrand().get(TwoLevelFragment.getIndex());
				AvailBrand.mScreenHotCarLine=brandSort;
				AvailBrand.mScreenHotBrand=brandSort;
				mTick.setVisibility(View.VISIBLE);
			}
			Constant.isAllSelect=false;
			rightAdapter.notifyDataSetChanged();
			if(onTitleListener!=null)
			{
				onTitleListener.setImageBack(AvailBrand.mScreenHotCarLine);
			}
			break;
		default:
			break;
		}

	}

}
