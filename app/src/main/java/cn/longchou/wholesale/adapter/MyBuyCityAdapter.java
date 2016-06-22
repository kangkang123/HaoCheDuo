package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyBuyDrawerAdapter.ViewHolder;
import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.domain.BudgetData;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyBuyCityAdapter extends BaseAdapter {

	private Context context;
	private List<CityProvinces> list;
	
	public MyBuyCityAdapter(Context context,List<CityProvinces> list) {
		this.context=context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
			convertView=View.inflate(context, R.layout.item_dl_my_buy, null);
			holder.choose=(TextView) convertView.findViewById(R.id.tv_my_buy_right_choose);
			holder.tick=(ImageView) convertView.findViewById(R.id.iv_my_buy_right_check);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		CityProvinces item = getItem(position);
		holder.choose.setText(item.provinces);
		if(item.isBuy)
		{
			holder.tick.setVisibility(View.VISIBLE);
		}else{
			holder.tick.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	class ViewHolder{
		private TextView choose;
		private ImageView tick;
	}

}
