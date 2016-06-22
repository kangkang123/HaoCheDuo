package cn.longchou.wholesale.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.SecondLocationAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.global.Constant;

public class SecondLocationActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTvOK;
	private TextView mTvNow;
	private ListView mLvLocation;
	private SecondLocationAdapter adapter;
	private ArrayList<String> list;
	List<String> listChoose = new ArrayList<String>();
	
	private ImageView image;
	private List<String> citys;

	@Override
	public void initView() {
		setContentView(R.layout.activity_second_location);
		mBack = (ImageView) findViewById(R.id.iv_location_back);
		mTvOK = (TextView) findViewById(R.id.tv_location_ok);

		mTvNow = (TextView) findViewById(R.id.tv_location_now);

		mLvLocation = (ListView) findViewById(R.id.lv_second_location);
	}

	@Override
	public void initData() {

		CityProvinces provinces = (CityProvinces) getIntent().getExtras().getSerializable("provinces");
		String province = provinces.provinces;
		CityLocation cityProvinces = CityLocation.getCityProvinces();
		Map<String, List<String>> provinces2 = cityProvinces.provinces;
		citys = provinces2.get(province);
		
		mTvNow.setText(province);
		
		adapter = new SecondLocationAdapter(getApplicationContext(),citys);
		mLvLocation.setAdapter(adapter);
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mTvOK.setOnClickListener(this);

		mLvLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				image = (ImageView) view
						.findViewById(R.id.iv_location_arrow);
				//获取城市列表中的要填充的城市
				String city = citys.get(position);
				List<CityProvinces> list = CityLocation.getAllCitys();
				//遍历所有的城市的集合
				for(int i=0;i<list.size();i++)
				{
					CityProvinces cityProvinces = list.get(i);
					//判断当前点击的城市和集合中获取的第i个城市是否相同
					if(cityProvinces.provinces.equals(city))
					{
						//如果是同一个城市，判断当前城市的选择状态是否为选中
						if(cityProvinces.isSelect)
						{
							//如果选中的话让选中时的图片隐藏，让当前的选中变为不选中
							image.setVisibility(View.INVISIBLE);
							CityLocation.setCitySelectFalse(cityProvinces);
							
							//当取消选择时把取消选择时选中的内容放入取消选择的集合中
							CityLocation.getSecondRemove().add(list.get(i));
							
						}else{
							//如果没有选中的话让图片显示，让没有选中状态变为选中状态
							image.setVisibility(View.VISIBLE);
							CityLocation.setCitySelectTrue(cityProvinces);
							
							//当选中时，把选中的对象放入选中集合中
							CityLocation.getSecondChoose().add(list.get(i));
							
						}
						
					}
				}

				//刷新，让重新加载
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_location_back:
			backCancelData();

			break;
		case R.id.tv_location_ok:
			backOkData();
			
			break;

		default:
			break;
		}

	}

	//取消的返回上一次
	private void backCancelData() {
		Intent intent = new Intent();
		SecondLocationActivity.this.setResult(1, intent);
		finish();
	}

	//OK的返回上一次
	private void backOkData() {
		Intent intent = new Intent();
		SecondLocationActivity.this.setResult(2, intent);
		finish();
	}

}
