package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.VehicleLicenseFee;
import cn.longchou.wholesale.domain.MustNotice.LicenseFee;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VehicleLicenseFeeAdapter extends BaseAdapter{

	private Context context;
	private List<LicenseFee> list;
	
	public VehicleLicenseFeeAdapter(Context context,List<LicenseFee> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public LicenseFee getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_list_car_license_fee, null);
			holder.city=(TextView) convertView.findViewById(R.id.tv_car_license_fee_city);
			holder.transfer=(TextView) convertView.findViewById(R.id.tv_car_license_fee_transferring);
			holder.membership=(TextView) convertView.findViewById(R.id.tv_car_license_fee_membership);
			holder.bus=(TextView) convertView.findViewById(R.id.tv_car_license_fee_bus);
			holder.fast=(TextView) convertView.findViewById(R.id.tv_car_license_fee_fast_to);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		LicenseFee item = list.get(position);
		holder.city.setText(item.qy);
		holder.transfer.setText(item.ghjyf);
		holder.membership.setText(item.zjjyf);
		holder.bus.setText(item.gcf);
		holder.fast.setText(item.kzfwf);
		
		return convertView;
	}
	
	class ViewHolder{
		//市区
	    TextView city;
		//过户交易费
	    TextView transfer;
		//转籍交易费
	    TextView membership;
		//公车费
	    TextView bus;
		//快至服务费
	    TextView fast;
	}

}
