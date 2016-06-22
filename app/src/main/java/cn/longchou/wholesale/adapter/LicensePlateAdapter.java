package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.LicensePlate;
import cn.longchou.wholesale.domain.MachineElectrical;
import cn.longchou.wholesale.domain.VehicleDetails.MustKnow;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
* @Description: 车牌号码的adapter
*
* @author kangkang
*
* @date 2016年1月20日 下午3:51:06 
*
 */
public class LicensePlateAdapter extends BaseAdapter {

	private Context context;
	List<MustKnow> list;
	public LicensePlateAdapter(Context context, List<MustKnow> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public MustKnow getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_list_license_plate, null);
			holder.mPosition=(TextView) convertView.findViewById(R.id.tv_license_plate_position);
			holder.mGoThere=(TextView) convertView.findViewById(R.id.tv_license_plate_host);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		MustKnow item = getItem(position);
		holder.mPosition.setText(item.key);
		holder.mGoThere.setText(item.value);
		return convertView;
	}

	class ViewHolder{
		TextView mPosition;
		TextView mGoThere;
	}

}
