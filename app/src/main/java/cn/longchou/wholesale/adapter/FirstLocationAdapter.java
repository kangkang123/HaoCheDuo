package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.global.Constant;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FirstLocationAdapter extends BaseAdapter{

	private Context context;
	private boolean isArrow;
	
	private List<String> firstCity;
	private TextView city;
	private ImageView ivIcon;
	
	//传递过来的城市的集合
	private List<CityProvinces> list;
	
	public FirstLocationAdapter(Context context,List<CityProvinces> list) {
		this.context = context;
		this.list=list;
	}

	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public CityProvinces getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_location, null);
			holder.city = (TextView) convertView.findViewById(R.id.tv_city);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_location_arrow);
		    convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		CityProvinces province = getItem(position);
		if(province.isProvinces)
		{
			holder.ivIcon.setVisibility(View.VISIBLE);
			holder.ivIcon.setImageResource(R.drawable.location_city);
		}
		holder.city.setText(province.provinces);
		List<CityProvinces> allCitys = CityLocation.getAllCitys();
		for(int i=0;i<allCitys.size();i++)
		{
			CityProvinces citys = allCitys.get(i);
			if(citys.provinces.equals(province.provinces))
			{
				if(citys.isSelect)
				{
					holder.ivIcon.setVisibility(View.VISIBLE);
				}else{
					holder.ivIcon.setVisibility(View.INVISIBLE);
				}
			}
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView city;
		ImageView ivIcon;
	}

}
