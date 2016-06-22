package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyBuyDrawerAdapter.ViewHolder;
import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.domain.AvailBrand;
import cn.longchou.wholesale.domain.BudgetData;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.domain.RigisterCity;
import cn.longchou.wholesale.global.Constant;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
* @Description: 品牌的adapter
*
* @author kangkang
*
* @date 2016年2月18日 下午2:02:54 
*
 */
public class MyHotBrandAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;
	private String choose;
	
	public MyHotBrandAdapter(Context context,List<String> list,String choose) {
		this.context=context;
		this.list=list;
		this.choose=choose;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_dl_my_buy, null);
			holder.choose=(TextView) convertView.findViewById(R.id.tv_my_buy_right_choose);
			holder.tick=(ImageView) convertView.findViewById(R.id.iv_my_buy_right_check);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		String item = getItem(position);
		holder.choose.setText(item);
		//如果是品牌的话
		if(choose.equals("brand"))
		{
			if(item.equals(AvailBrand.mHotBrand))
			{
				holder.tick.setVisibility(View.VISIBLE);
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
			}
		}//如果是城市的话
		else if(choose.equals("city")){
			
			if(item.equals(CityLocation.mBuyCity))
			{
				holder.tick.setVisibility(View.VISIBLE);
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
			}
		}//如果是车系的话
		else if(choose.equals("car")){
			
			if(item.equals(AvailBrand.mHotCarLine))
			{
				holder.tick.setVisibility(View.VISIBLE);
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
			}
		}
		else if(choose.equals("budget")){
			
			if(item.equals(BudgetData.budget))
			{
				holder.tick.setVisibility(View.VISIBLE);
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
			}
		}
		else if(choose.equals("hotBrand")){
			String series = AvailBrand.mScreenHotCarLine;
			if(item.equals(AvailBrand.mScreenHotCarLine))
			{
				holder.tick.setVisibility(View.VISIBLE);
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
//				String brand = AvailBrand.mScreenHotBrand;
//				if(!TextUtils.isEmpty(series))
//				{
//					if(series.equals(brand))
//					{
//						holder.tick.setVisibility(View.VISIBLE);
//					}
//				}
			}
			
		}else if(choose.equals("cityChoose")){
			holder.tick.setVisibility(View.INVISIBLE);
			if(item.equals(Constant.cityChoose))
			{
				holder.tick.setVisibility(View.VISIBLE);
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
			}
		}else if(choose.equals("sexChoose")){
			if(item.equals(Constant.sexChoose))
			{
				holder.tick.setVisibility(View.VISIBLE);
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
			}
		}else if(choose.equals("provinceChoose"))
		{
			holder.tick.setVisibility(View.VISIBLE);
			holder.tick.setImageResource(R.drawable.location_city);
		}
		
		return convertView;
	}
	
	class ViewHolder{
		private TextView choose;
		private ImageView tick;
	}

}
