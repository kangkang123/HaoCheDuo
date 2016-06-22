package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.MyNews;
import cn.longchou.wholesale.domain.MyOrders;
import cn.longchou.wholesale.domain.MyOrders.Order;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyOrdersAdapter extends BaseAdapter{

	private Context context;
	private List<Order> list;
	
	public MyOrdersAdapter(Context context,List<Order> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Order getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_list_my_orders, null);
			holder.time=(TextView) convertView.findViewById(R.id.tv_my_orders_time);
			holder.code=(TextView) convertView.findViewById(R.id.tv_my_orders_code);
			holder.money=(TextView) convertView.findViewById(R.id.tv_my_orders_money);
			holder.state=(TextView) convertView.findViewById(R.id.tv_my_orders_state);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Order orders = getItem(position);
		holder.time.setText(orders.orderDate);
		holder.code.setText(orders.orderID+"");
		holder.money.setText(orders.orderSumMoney+"");
		if(orders.orderState.equals("已支付"))
		{
			holder.state.setText("已支付");
		}else if(orders.orderState.equals("待支付"))
		{
		    holder.state.setText("待支付");	
		    holder.state.setTextColor(Color.rgb(237, 108, 1));
		}else if(orders.orderState.equals("已取消"))
		{
			holder.state.setText("已取消");
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView time;
		TextView code;
		TextView money;
		TextView state;
	}

}
