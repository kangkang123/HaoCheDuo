package cn.longchou.wholesale.activity;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.LocationService;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.FirstLocationAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.base.BaseApplication;
import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;

public class FirstLocationActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTvOK;
	private TextView mTvNow;
	private TextView mTvCity;
	private ListView mLvLocation;
	private FirstLocationAdapter adapter;
	
	private int requestCode=1;
	private ArrayList<String> listChoose;
	public String conutry;
    
	
	private ImageView image;
	private List<CityProvinces> citys;
	
	private LocationService locationService;
	
	@Override
	public void initView() {
        setContentView(R.layout.activity_first_location);
        mBack = (ImageView) findViewById(R.id.iv_location_back);
        mTvOK = (TextView) findViewById(R.id.tv_location_ok);
        
        mTvNow = (TextView) findViewById(R.id.tv_location_now);
        mTvCity = (TextView) findViewById(R.id.tv_location_city);
        
        mLvLocation = (ListView) findViewById(R.id.lv_location);
	}

	@Override
	public void initData() {
		//解析获取的定位的城市
		citys = CityLocation.getCitys();
		String city=PreferUtils.getString(getApplicationContext(),"city",null);
		if(!TextUtils.isEmpty(city))
		{
			mTvCity.setText(city);
		}else{
			mTvCity.setText("定位失败");
		}
		
		adapter = new FirstLocationAdapter(getApplicationContext(), citys);
		mLvLocation.setAdapter(adapter);

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
		if(list==null || list.size()<=0)
		{
			if(CityLocation.getAllCitys().size()>0)
			{
				CityProvinces cityProvinces = CityLocation.getAllCitys().get(0);
				CityLocation.setCitySelectTrue(cityProvinces);
			}
		}
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mTvOK.setOnClickListener(this);
		
		mLvLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(FirstLocationActivity.this,SecondLocationActivity.class);
				image = (ImageView) view.findViewById(R.id.iv_location_arrow);
				
				//当点击为0时
				if(position==0)
				{
					//获取所有的城市中的第一个内容，就是全国
					List<CityProvinces> list = CityLocation.getAllCitys();
					CityProvinces provinces = list.get(0);
					//如果全国为选中状态，就让全国变为不选中状态
					if(provinces.isSelect)
					{
						image.setVisibility(View.INVISIBLE);
						CityLocation.setCitySelectFalse(provinces);
						
						CityLocation.getFirstRemove().add(provinces);
					}else{
						image.setVisibility(View.VISIBLE);
						for(int i=0;i<list.size();i++)
						{
							CityProvinces cityProvinces = list.get(i);
							CityLocation.setCitySelectFalse(cityProvinces);
							
							CityLocation.getFirstChoose().remove(provinces);
						}
						
						CityLocation.setCitySelectTrue(provinces);
						CityLocation.getFirstRemove().add(provinces);
					}
					
				}
//				else if(!(CityLocation.getAllCitys().get(0).isSelect) && position!=0){
				else if(position!=0){
					//当点击不是全国的时候，如果全国为选中状态则让全国变为不选中状态
					List<CityProvinces> list1 = CityLocation.getAllCitys();
					CityProvinces country = list1.get(0);
					if(country.isSelect)
					{
						CityLocation.setCitySelectFalse(country);
						
						CityLocation.getFirstRemove().add(country);
					}
					
					
					CityProvinces provinces = citys.get(position);
					if(!provinces.isProvinces)
					{
						List<CityProvinces> list = CityLocation.getAllCitys();
						for(int i=0;i<list.size();i++)
						{
							CityProvinces cityProvinces = list.get(i);
							if(cityProvinces.provinces.equals(provinces.provinces))
							{
								if(cityProvinces.isSelect)
								{
									image.setVisibility(View.INVISIBLE);
									CityLocation.setCitySelectFalse(cityProvinces);
									CityLocation.getFirstRemove().add(cityProvinces);
								}else{
									image.setVisibility(View.VISIBLE);
									CityLocation.setCitySelectTrue(cityProvinces);
									CityLocation.getFirstChoose().add(provinces);
								}
								
							}
						}
					}else{
						Bundle bundle=new Bundle();
						bundle.putSerializable("provinces", provinces);
						intent.putExtras(bundle);
						startActivityForResult(intent, requestCode);
					}
					
				}
				adapter.notifyDataSetChanged();
			}
			
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		//当结果码为1的时候表示通过返回键返回，取消上一步的所有操作结果
		case 1:
			//获取所有的选中的内容
			List<CityProvinces> secondChoose = CityLocation.getSecondChoose();
			for(int i=0;i<secondChoose.size();i++)
			{
				//遍历所有上一步选中的内容，让选中的内容全都变为没有选中
				CityProvinces cityProvinces = secondChoose.get(i);
				CityLocation.setCitySelectFalse(cityProvinces);
			}
			//遍历所有的没有选中的内容
			List<CityProvinces> secondRemove = CityLocation.getSecondRemove();
			for(int i=0;i<secondRemove.size();i++)
			{
				//让所有没有选中的内容变为选中状态
				CityProvinces cityProvinces = secondRemove.get(i);
				CityLocation.setCitySelectTrue(cityProvinces);
			}
			//清除上一步中选中和没有选中的集合，当重新进入的时候都是最新的
			CityLocation.clearSecondChoose();
			CityLocation.getSecondRemove().clear();
			break;
		case 2:
			//当结果码为2的时候表示返回的是需要上一次的改变。直接把选中和没有选中的集合都变为空
			CityLocation.clearSecondChoose();
			CityLocation.getSecondRemove().clear();
			break;

		default:
			break;
		}
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
		FirstLocationActivity.this.setResult(1, intent);
		finish();
	}

	//OK的返回上一次
	private void backOkData() {
		Intent intent = new Intent();
		FirstLocationActivity.this.setResult(2, intent);
		finish();
	}
}
