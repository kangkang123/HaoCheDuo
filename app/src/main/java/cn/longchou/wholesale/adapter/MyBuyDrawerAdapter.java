package cn.longchou.wholesale.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.AvailBrand;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.global.Constant;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
* @Description: 筛选部分的adapter
*
* @author kangkang
*
* @date 2016年2月22日 下午3:49:11 
*
 */
public class MyBuyDrawerAdapter extends BaseAdapter{

	private Context context;
	private List<String> list;
	private String choose;
	
	public MyBuyDrawerAdapter(Context context,List<String> list,String choose) {
		this.context = context;
		this.list=list;
		this.choose=choose;
		//初始化的时候就设置值
		
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return super.getViewTypeCount()+1;
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
		//里程
		if(choose.equals("screenMileage"))
		{
			if(item.equals(Constant.screenMileage))
			{
				holder.tick.setVisibility(View.VISIBLE);
				holder.choose.setTextColor(Color.rgb(237, 108, 1));
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
				holder.choose.setTextColor(Color.rgb(51, 51, 51));
			}
		}//价格
		else if(choose.equals("screenPrice")){
			
			if(item.equals(Constant.screenPrice))
			{
				holder.tick.setVisibility(View.VISIBLE);
				holder.choose.setTextColor(Color.rgb(237, 108, 1));
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
				holder.choose.setTextColor(Color.rgb(51, 51, 51));
			}
		}//车龄
		else if(choose.equals("screenCarYears")){
			
			if(item.equals(Constant.screenCarYears))
			{
				holder.tick.setVisibility(View.VISIBLE);
				holder.choose.setTextColor(Color.rgb(237, 108, 1));
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
				holder.choose.setTextColor(Color.rgb(51, 51, 51));
			}
		}//变速箱
		else if(choose.equals("screenGearBox")){
			
			if(item.equals(Constant.screenGearBox))
			{
				holder.tick.setVisibility(View.VISIBLE);
				holder.choose.setTextColor(Color.rgb(237, 108, 1));
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
				holder.choose.setTextColor(Color.rgb(51, 51, 51));
			}
		}//车型
		else if(choose.equals("screenCarModel")){
			
			if(item.equals(Constant.screenCarModel))
			{
				holder.tick.setVisibility(View.VISIBLE);
				holder.choose.setTextColor(Color.rgb(237, 108, 1));
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
				holder.choose.setTextColor(Color.rgb(51, 51, 51));
			}
		}//品牌
		else if(choose.equals("screenHotBrand")){
			
			if(item.equals(AvailBrand.mScreenHotBrand))
			{
				holder.tick.setVisibility(View.VISIBLE);
				holder.choose.setTextColor(Color.rgb(237, 108, 1));
			}else{
				holder.tick.setVisibility(View.INVISIBLE);
				holder.choose.setTextColor(Color.rgb(51, 51, 51));
			}
		}
		holder.choose.setText(item);
		return convertView;
	}
	
	class ViewHolder{
		private TextView choose;
		private ImageView tick;
	}

}
