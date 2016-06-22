package cn.longchou.wholesale.adapter;

import java.util.List;

import org.w3c.dom.Text;

import com.bumptech.glide.Glide;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.HomePage.Cars;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyOrdersDetailAdapter extends BaseAdapter {

	private Context context;
	private List<Cars> list;
	
	private LinearLayout llMarket;
	private LinearLayout llEarnest;
	private String orderState;
	
	public MyOrdersDetailAdapter(Context context, List<Cars> list, String orderState) {
		this.context=context;
		this.list=list;
		this.orderState=orderState;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Cars getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_list_market, null);
			holder.carDisplay=(ImageView) convertView.findViewById(R.id.iv_car_image);
			holder.carId = (TextView) convertView.findViewById(R.id.tv_car_ID);
			holder.carInfo = (TextView) convertView.findViewById(R.id.tv_car_info);
			holder.carTime = (TextView) convertView.findViewById(R.id.tv_car_time);
			holder.carMoney = (TextView) convertView.findViewById(R.id.tv_car_money);
			holder.carMoneyTv = (TextView) convertView.findViewById(R.id.tv_car_money_tv);
			holder.carEarnest = (TextView) convertView.findViewById(R.id.tv_car_earnest);
			holder.carEarnestTv = (TextView) convertView.findViewById(R.id.tv_car_earnest_sub);
			holder.mIvLevel = (ImageView) convertView
					.findViewById(R.id.iv_level);
			llMarket = (LinearLayout) convertView.findViewById(R.id.ll_car_market);
			llEarnest = (LinearLayout) convertView.findViewById(R.id.ll_car_earnest);
			
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		llMarket.setVisibility(View.GONE);
		llEarnest.setVisibility(View.VISIBLE);
		Cars item = getItem(position);
		holder.carInfo.setText(item.carName);
		holder.carTime.setText(item.carDesc);
		holder.carMoney.setText(item.carPrice);
		if(orderState.equals("待支付"))
		{
			if("已下架".equals(item.carPrice))
			{
				holder.carMoney.setTextColor(Color.GRAY);
				holder.carMoney.setTextSize(12);
				holder.carMoney.setVisibility(View.INVISIBLE);
				holder.carMoneyTv.setVisibility(View.INVISIBLE);
				holder.carEarnestTv.setVisibility(View.INVISIBLE);
				holder.carEarnest.setVisibility(View.INVISIBLE);
			}
		}else{
			if("已下架".equals(item.carPrice))
			{
				holder.carMoney.setTextColor(Color.GRAY);
				holder.carMoney.setTextSize(12);
				holder.carMoneyTv.setVisibility(View.INVISIBLE);
				holder.carEarnestTv.setVisibility(View.INVISIBLE);
				holder.carEarnest.setVisibility(View.INVISIBLE);
			}
		}
		holder.carEarnest.setText(item.carSubscription+"元");
		holder.carId.setText(item.carID+"");
		if("抢购".equals(item.carAction))
		{
			holder.mIvLevel.setImageResource(R.drawable.miao);
		}
		Glide.with(context).load(item.carImgURL).placeholder(R.drawable.displaycar).into(holder.carDisplay);
		return convertView;
	}
	
	class ViewHolder{
		private ImageView carDisplay;
		private ImageView mIvLevel;
		private TextView carId;
		private TextView carInfo;
		private TextView carTime;
		private TextView carMoney;
		private TextView carMoneyTv;
		private TextView carEarnest;
		private TextView carEarnestTv;
	}
}