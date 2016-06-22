package cn.longchou.wholesale.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.bumptech.glide.Glide;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.ItemSelect;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.global.Constant;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @Description: 车市首页listview的填充内容
 * 
 * @author kangkang
 * 
 * @date 2015年12月22日 下午11:18:10
 * 
 */
public class MyAttentionAdapter extends BaseAdapter{

	private Context context;
	
	private List<Cars> list;

	public MyAttentionAdapter(Context context, List<Cars> list) {
		this.context = context;
		this.list=list;
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(context, R.layout.item_list_market, null);
			holder.mCarImage = (ImageView) convertView
					.findViewById(R.id.iv_car_image);
			holder.mChekced = (ImageView) convertView
					.findViewById(R.id.iv_check);
			holder.mCarInfo = (TextView) convertView
					.findViewById(R.id.tv_car_info);
			holder.mCarTime = (TextView) convertView
					.findViewById(R.id.tv_car_time);
			holder.mCarMoney = (TextView) convertView
					.findViewById(R.id.tv_car_money);
			holder.mIvLevel = (ImageView) convertView
					.findViewById(R.id.iv_level);
			holder.mCartCar = (ImageView) convertView
					.findViewById(R.id.iv_cartcar);
			holder.Line = convertView.findViewById(R.id.view_line);
			holder.mCarMoneyMark=(TextView) convertView.findViewById(R.id.tv_car_money_tv);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mCartCar.setVisibility(View.INVISIBLE);
		holder.Line.setVisibility(View.INVISIBLE);
		Cars item = getItem(position);
		holder.mCarInfo.setText(item.carName);
		holder.mCarTime.setText(item.carDesc);
		holder.mCarMoney.setText(item.carPrice+"");
		
		if("已下架".equals(item.carPrice+""))
		{
			holder.mCarMoney.setTextColor(Color.GRAY);
			holder.mCarMoney.setTextSize(12);
			holder.mCarMoneyMark.setVisibility(View.INVISIBLE);
		}else{
			holder.mCarMoneyMark.setVisibility(View.VISIBLE);
		}
		
		if("抢购".equals(item.carAction))
		{
			holder.mIvLevel.setImageResource(R.drawable.miao);
		}
		Glide.with(context).load(item.carImgURL).placeholder(R.drawable.displaycar).into(holder.mCarImage);
		return convertView;
	}

	

	public class ViewHolder {
		ImageView mChekced;
		ImageView mCarImage;
		TextView mCarInfo;
		TextView mCarTime;
		TextView mCarMoney;
		TextView mCarMoneyMark;
		ImageView mIvLevel;
		ImageView mCartCar;
		View Line;
	}

}
